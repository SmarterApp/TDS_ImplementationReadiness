package org.cresst.sb.irp.automation.engine;

import org.cresst.sb.irp.domain.automation.AutomationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Handles IRP Automation requests making sure only a single execution of automation against a vendor system is run.
 * It runs IRP Automation asynchronously while handling messages back to the client.
 */
@Component
public class IrpAutomationEngine implements AutomationEngine {
    private final static Logger logger = LoggerFactory.getLogger(IrpAutomationEngine.class);

    private final AutomationRequestProcessor automationRequestProcessor;

    public IrpAutomationEngine(AutomationRequestProcessor automationRequestProcessor) {
        this.automationRequestProcessor = automationRequestProcessor;
    }

    @Override
    public void automate(final AutomationRequest automationRequest) {
        logger.info("Automation Requested for {}", automationRequest);
        automationRequestProcessor.processAutomationRequest(automationRequest);
    }
}
