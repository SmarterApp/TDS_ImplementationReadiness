package org.cresst.sb.irp.automation;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.cresst.sb.irp.automation.service.AutomationService;
import org.cresst.sb.irp.automation.service.domain.*;
import org.cresst.sb.irp.automation.service.requesthandler.AutomationRequestResultHandler;
import org.cresst.sb.irp.automation.service.requesthandler.AutomationRequestResultHandlerProxy;
import org.cresst.sb.irp.automation.service.statusreporting.AutomationStatusHandler;
import org.cresst.sb.irp.automation.service.statusreporting.IrpAutomationStatusHandlerProxy;
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
    private final ConcurrentHashMap<IrpAutomationRequest, DeferredResult<IrpAutomationToken>> automationRequests = new ConcurrentHashMap<>();
    private final Multimap<IrpAutomationToken, DeferredResult<AutomationStatusReport>> statusRequests =
            Multimaps.synchronizedListMultimap(ArrayListMultimap.<IrpAutomationToken, DeferredResult<AutomationStatusReport>>create());

    // Automation status reports
    private final ConcurrentHashMap<IrpAutomationToken, AutomationStatusReport> automationStatusReports = new ConcurrentHashMap<>();

    public AutomationController(AutomationService automationService,
                                AutomationRequestResultHandlerProxy automationRequestResultHandlerProxy,
                                IrpAutomationStatusHandlerProxy automationStatusHandlerProxy) {
        this.automationService = automationService;
        automationRequestResultHandlerProxy.setAutomationRequestResultHandler(this);
        automationStatusHandlerProxy.setAutomationStatusHandler(this);
    }

    @RequestMapping(value = "/automate", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public DeferredResult<IrpAutomationToken> automate(@Valid @RequestBody final IrpAutomationRequest irpAutomationRequest) {
        logger.info("Automation Request: " + irpAutomationRequest);

        final DeferredResult<IrpAutomationToken> deferredAutomationRequest = new DeferredResult<>(null, null);

        // If this is a duplicate request, respond with previous DeferredResult
        final DeferredResult<IrpAutomationToken> previousRequest =
                automationRequests.putIfAbsent(irpAutomationRequest, deferredAutomationRequest);

        if (previousRequest == null) {
            deferredAutomationRequest.onCompletion(new Runnable() {
                @Override
                public void run() {
                    // Remove the request from the map is ok. The automation engine prevents duplicate automation runs.
                    automationRequests.remove(irpAutomationRequest);
                }
            });

            automationService.automate(irpAutomationRequest);
        }

        return previousRequest == null ? deferredAutomationRequest : previousRequest;
    }

    @RequestMapping(value = "/automationStatus", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public DeferredResult<AutomationStatusReport> status(@Valid @RequestBody final IrpAutomationStatusRequest irpAutomationStatusRequest) {

        final IrpAutomationToken irpAutomationToken = irpAutomationStatusRequest.getIrpAutomationToken();

        final DeferredResult<AutomationStatusReport> deferredStatusReport = new DeferredResult<>(null, null);
        statusRequests.put(irpAutomationToken, deferredStatusReport);

        deferredStatusReport.onCompletion(new Runnable() {
            @Override
            public void run() {
                statusRequests.remove(irpAutomationToken, deferredStatusReport);
            }
        });

        // Check if there are any status updates to return immediately
        AutomationStatusReport latestStatusReport = getLatestStatuses(irpAutomationStatusRequest);
        if (latestStatusReport != null) {
            deferredStatusReport.setResult(latestStatusReport);
        }

        return deferredStatusReport;
    }

    @Override
    public void handleAutomationRequestResult(IrpAutomationRequest irpAutomationRequest,
                                              IrpAutomationToken irpAutomationToken) {
        DeferredResult<IrpAutomationToken> deferredAutomationRequest = automationRequests.get(irpAutomationRequest);
        deferredAutomationRequest.setResult(irpAutomationToken);
    }

    @Override
    public void handleAutomationRequestError(IrpAutomationRequestError irpAutomationRequestError) {
        IrpAutomationRequest irpAutomationRequest = irpAutomationRequestError.getIrpAutomationRequest();
        DeferredResult<IrpAutomationToken> deferredAutomationRequest = automationRequests.get(irpAutomationRequest);
        deferredAutomationRequest.setErrorResult(irpAutomationRequestError);
    }

    @Override
    public void handleAutomationStatus(IrpAutomationToken irpAutomationToken, AutomationStatusReport automationStatusReport) {
        automationStatusReports.put(irpAutomationToken, automationStatusReport);

        // Notify clients
        Collection<DeferredResult<AutomationStatusReport>> deferredStatusReports = statusRequests.get(irpAutomationToken);
        synchronized (statusRequests) {
            for (DeferredResult<AutomationStatusReport> deferredReport : deferredStatusReports) {
                deferredReport.setResult(automationStatusReport);
            }
        }
    }

    private AutomationStatusReport getLatestStatuses(IrpAutomationStatusRequest irpAutomationStatusRequest) {
        AutomationStatusReport automationStatusReport = automationStatusReports.get(irpAutomationStatusRequest.getIrpAutomationToken());

        if (automationStatusReport != null &&
                irpAutomationStatusRequest.getTimeOfLastStatus() < automationStatusReport.getLastUpdateTimestamp()) {
            return automationStatusReport;
        }

        return null;
    }
}
