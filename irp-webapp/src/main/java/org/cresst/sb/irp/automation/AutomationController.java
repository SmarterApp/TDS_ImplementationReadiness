package org.cresst.sb.irp.automation;

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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This controller handles automation requests and status reports. It is designed to run as a single instance
 * on a single server since it stores status results in-memory.
 */
@Controller
public class AutomationController implements AutomationRequestResultHandler, AutomationStatusHandler {
    private final static Logger logger = LoggerFactory.getLogger(AutomationController.class);

    private AutomationService automationService;

    private final ConcurrentMap<AutomationRequest, DeferredResult<AutomationToken>> automationRequests = new ConcurrentHashMap<>();
    private final Map<AutomationToken, DeferredResult<List<AutomationStatus>>> statusRequests = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<AutomationToken, List<AutomationStatus>> automationStatuses = new ConcurrentHashMap<>();

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
    public DeferredResult<List<AutomationStatus>> status(@RequestBody final AutomationToken automationToken) {

        final DeferredResult<List<AutomationStatus>> deferredStatuses = new DeferredResult<>(null, Collections.emptyList());
        statusRequests.put(automationToken, deferredStatuses);

        deferredStatuses.onCompletion(new Runnable() {
            @Override
            public void run() {
                statusRequests.remove(automationToken);
            }
        });

        List<AutomationStatus> latestStatuses = getLatestStatuses(automationToken);
        if (latestStatuses != null && !latestStatuses.isEmpty()) {
            deferredStatuses.setResult(latestStatuses);
        }

        return deferredStatuses;
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
    public void handleAutomationStatus(AutomationToken automationToken, AutomationStatus automationStatus) {
        List<AutomationStatus> emptyStatues = new CopyOnWriteArrayList<>();
        List<AutomationStatus> previousStatuses = automationStatuses.putIfAbsent(automationToken, emptyStatues);
        DeferredResult<List<AutomationStatus>> deferredResult = statusRequests.get(automationToken);

        if (previousStatuses == null) {
            emptyStatues.add(automationStatus);

            if (deferredResult != null) {
                deferredResult.setResult(emptyStatues);
            }
        } else {
            previousStatuses.add(automationStatus);

            if (deferredResult != null) {
                deferredResult.setResult(previousStatuses);
            }
        }
    }

    private List<AutomationStatus> getLatestStatuses(AutomationToken automationToken) {
        return automationStatuses.get(automationToken);
    }
}
