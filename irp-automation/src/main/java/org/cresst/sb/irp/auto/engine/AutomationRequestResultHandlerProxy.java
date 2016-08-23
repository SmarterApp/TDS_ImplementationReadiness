package org.cresst.sb.irp.auto.engine;

import org.cresst.sb.irp.domain.automation.AutomationRequest;
import org.cresst.sb.irp.domain.automation.AutomationRequestError;
import org.cresst.sb.irp.domain.automation.AutomationToken;

public class AutomationRequestResultHandlerProxy implements AutomationRequestResultHandler {

    private AutomationRequestResultHandler handler;

    public void setAutomationRequestResultHandler(AutomationRequestResultHandler handler) {
        this.handler = handler;
    }

    @Override
    public void handleAutomationRequestResult(AutomationRequest automationRequest, AutomationToken automationToken) {
        handler.handleAutomationRequestResult(automationRequest, automationToken);
    }

    @Override
    public void handleAutomationRequestError(AutomationRequestError automationRequestError) {
        handler.handleAutomationRequestError(automationRequestError);
    }
}
