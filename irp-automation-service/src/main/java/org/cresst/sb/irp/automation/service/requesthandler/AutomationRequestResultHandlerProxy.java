package org.cresst.sb.irp.automation.service.requesthandler;

import org.cresst.sb.irp.automation.service.domain.AutomationRequest;
import org.cresst.sb.irp.automation.service.domain.AutomationRequestError;
import org.cresst.sb.irp.automation.service.domain.AutomationToken;

public class AutomationRequestResultHandlerProxy implements AutomationRequestResultHandler {
    private AutomationRequestResultHandler handler;

    @Override
    public void handleAutomationRequestResult(AutomationRequest automationRequest, AutomationToken automationToken) {
        handler.handleAutomationRequestResult(automationRequest, automationToken);
    }

    @Override
    public void handleAutomationRequestError(AutomationRequestError automationRequestError) {
        handler.handleAutomationRequestError(automationRequestError);
    }

    public void setAutomationRequestResultHandler(AutomationRequestResultHandler automationRequestResultHandler) {
        this.handler = automationRequestResultHandler;
    }
}
