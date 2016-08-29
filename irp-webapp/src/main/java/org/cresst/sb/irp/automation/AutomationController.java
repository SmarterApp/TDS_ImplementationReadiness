package org.cresst.sb.irp.automation;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.cresst.sb.irp.auto.engine.AutomationRequestResultHandler;
import org.cresst.sb.irp.auto.engine.AutomationRequestResultHandlerProxy;
import org.cresst.sb.irp.auto.engine.AutomationStatusHandler;
import org.cresst.sb.irp.auto.engine.AutomationStatusHandlerProxy;
import org.cresst.sb.irp.domain.automation.*;
import org.cresst.sb.irp.service.AutomationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.Valid;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This controller handles automation requests and status reports. It is designed to run as a single instance
 * on a single server since it stores status results in-memory.
 */
@Controller
public class AutomationController implements AutomationRequestResultHandler, AutomationStatusHandler {
    private final static Logger logger = LoggerFactory.getLogger(AutomationController.class);

    private AutomationService automationService;

    // Requests
    private final ConcurrentHashMap<AutomationRequest, DeferredResult<AutomationToken>> automationRequests = new ConcurrentHashMap<>();
    private final Multimap<AutomationToken, DeferredResult<AutomationStatusReport>> statusRequests =
            Multimaps.synchronizedListMultimap(ArrayListMultimap.<AutomationToken, DeferredResult<AutomationStatusReport>>create());

    // Automation status reports
    private final ConcurrentHashMap<AutomationToken, AutomationStatusReport> automationStatusReports = new ConcurrentHashMap<>();

    public AutomationController(AutomationService automationService,
                                AutomationRequestResultHandlerProxy automationRequestResultHandlerProxy,
                                AutomationStatusHandlerProxy automationStatusHandlerProxy) {
        this.automationService = automationService;
        automationRequestResultHandlerProxy.setAutomationRequestResultHandler(this);
        automationStatusHandlerProxy.setAutomationStatusHandler(this);
    }

    @RequestMapping(value = "/automate", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public DeferredResult<AutomationToken> automate(@Valid @RequestBody final AutomationRequest automationRequest) {
        logger.info("Automation Request: " + automationRequest);

        final DeferredResult<AutomationToken> deferredAutomationRequest = new DeferredResult<>(null, null);

        // If this is a duplicate request, respond with previous DeferredResult
        final DeferredResult<AutomationToken> previousRequest =
                automationRequests.putIfAbsent(automationRequest, deferredAutomationRequest);

        if (previousRequest == null) {
            deferredAutomationRequest.onCompletion(new Runnable() {
                @Override
                public void run() {
                    // Remove the request from the map is ok. The automation engine prevents duplicate automation runs.
                    automationRequests.remove(automationRequest);
                }
            });

            automationService.automate(automationRequest);
        }

        return previousRequest == null ? deferredAutomationRequest : previousRequest;
    }

    @RequestMapping(value = "/automationStatus", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public DeferredResult<AutomationStatusReport> status(@Valid @RequestBody final AutomationStatusRequest automationStatusRequest) {

        final AutomationToken automationToken = automationStatusRequest.getAutomationToken();

        final DeferredResult<AutomationStatusReport> deferredStatusReport = new DeferredResult<>(null, null);
        statusRequests.put(automationToken, deferredStatusReport);

        deferredStatusReport.onCompletion(new Runnable() {
            @Override
            public void run() {
                statusRequests.remove(automationToken, deferredStatusReport);
            }
        });

        // Check if there are any status updates to return immediately
        AutomationStatusReport latestStatusReport = getLatestStatuses(automationStatusRequest);
        if (latestStatusReport != null) {
            deferredStatusReport.setResult(latestStatusReport);
        }

        return deferredStatusReport;
    }

    @Override
    public void handleAutomationRequestResult(AutomationRequest automationRequest,
                                              AutomationToken automationToken) {
        DeferredResult<AutomationToken> deferredAutomationRequest = automationRequests.get(automationRequest);
        deferredAutomationRequest.setResult(automationToken);
    }

    @Override
    public void handleAutomationRequestError(AutomationRequestError automationRequestError) {
        AutomationRequest automationRequest = automationRequestError.getAutomationRequest();
        DeferredResult<AutomationToken> deferredAutomationRequest = automationRequests.get(automationRequest);
        deferredAutomationRequest.setErrorResult(automationRequestError);
    }

    @Override
    public void handleAutomationStatus(AutomationToken automationToken, AutomationStatusReport automationStatusReport) {
        automationStatusReports.put(automationToken, automationStatusReport);

        // Notify clients
        Collection<DeferredResult<AutomationStatusReport>> deferredStatusReports = statusRequests.get(automationToken);
        synchronized (statusRequests) {
            for (DeferredResult<AutomationStatusReport> deferredReport : deferredStatusReports) {
                deferredReport.setResult(automationStatusReport);
            }
        }
    }

    private AutomationStatusReport getLatestStatuses(AutomationStatusRequest automationStatusRequest) {
        AutomationStatusReport automationStatusReport = automationStatusReports.get(automationStatusRequest.getAutomationToken());

        if (automationStatusReport != null &&
                automationStatusRequest.getTimeOfLastStatus() < automationStatusReport.getLastUpdateTimestamp()) {
            return automationStatusReport;
        }

        return null;
    }
}
