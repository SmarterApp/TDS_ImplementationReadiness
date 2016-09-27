package org.cresst.sb.irp.automation.service.engine.task;

import org.cresst.sb.irp.automation.adapter.domain.AdapterAutomationTicket;
import org.cresst.sb.irp.automation.adapter.domain.TDSReport;
import org.cresst.sb.irp.automation.service.domain.AutomationPhase;
import org.cresst.sb.irp.automation.service.domain.IrpAutomationRequest;
import org.cresst.sb.irp.automation.service.domain.AutomationStatusReport;
import org.cresst.sb.irp.automation.service.domain.IrpAutomationToken;
import org.cresst.sb.irp.automation.service.engine.adapterclient.AdapterStateMachine;
import org.cresst.sb.irp.automation.service.statusreporting.AutomationStatusHandler;
import org.cresst.sb.irp.automation.service.statusreporting.AutomationStatusReporter;
import org.cresst.sb.irp.automation.service.statusreporting.IrpAutomationStatusReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UrlPathHelper;

import javax.xml.ws.Response;
import java.net.URI;
import java.util.List;

/**
 * Communicates with an IRP Automation Adapter and performs IRP Analysis against the resulting TDSReports
 */
public class AutomationTask implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(AutomationTask.class);

    private IrpAutomationRequest irpAutomationRequest;
    private IrpAutomationToken irpAutomationToken;
    private final AutomationStatusHandler automationStatusHandler;
    private final AdapterStateMachine adapterStateMachine;

    private Runnable onCompletionCallback;

    public AutomationTask(AdapterStateMachine adapterStateMachine, AutomationStatusHandler automationStatusHandler) {
        this.adapterStateMachine = adapterStateMachine;
        this.automationStatusHandler = automationStatusHandler;
    }

    public void setIrpAutomationRequest(IrpAutomationRequest irpAutomationRequest) {
        this.irpAutomationRequest = irpAutomationRequest;
    }

    public void setIrpAutomationToken(IrpAutomationToken irpAutomationToken) {
        this.irpAutomationToken = irpAutomationToken;
    }

    /**
     * Sets a external method to call after automation is completed. It will be run regardless of automation errors.
     * @param callback The method to call after automation is finished.
     */
    public void onCompletion(Runnable callback) {
        onCompletionCallback = callback;
    }

    @Override
    public void run() {

        final AutomationStatusReport automationReport = new AutomationStatusReport();
        final AutomationStatusReporter statusReporter = new IrpAutomationStatusReporter(AutomationPhase.INITIALIZATION,
                irpAutomationToken,
                automationReport,
                automationStatusHandler);

        try {
            statusReporter.status(String.format("Calling IRP Automation Adapter at %s", irpAutomationRequest.getAdapterUrl()));

            adapterStateMachine.start();

        } catch (Exception ex) {
            logger.error("Ending automation task for " + irpAutomationRequest + " because of exception", ex);
            throw new RuntimeException(ex);
        } finally {
            logger.info("Automation task for {} is complete.", irpAutomationRequest);
            onCompletionCallback.run();
            statusReporter.markAutomationComplete();
        }
    }
}

