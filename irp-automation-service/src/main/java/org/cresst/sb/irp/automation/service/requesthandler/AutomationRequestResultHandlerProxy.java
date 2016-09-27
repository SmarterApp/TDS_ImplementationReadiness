package org.cresst.sb.irp.automation.service.requesthandler;

import org.cresst.sb.irp.automation.service.domain.IrpAutomationRequest;
import org.cresst.sb.irp.automation.service.domain.IrpAutomationRequestError;
import org.cresst.sb.irp.automation.service.domain.IrpAutomationToken;

public class AutomationRequestResultHandlerProxy implements AutomationRequestResultHandler {
    private AutomationRequestResultHandler handler;

    @Override
    public void handleAutomationRequestResult(IrpAutomationRequest irpAutomationRequest, IrpAutomationToken irpAutomationToken) {
        handler.handleAutomationRequestResult(irpAutomationRequest, irpAutomationToken);
    }

    @Override
    public void handleAutomationRequestError(IrpAutomationRequestError irpAutomationRequestError) {
        handler.handleAutomationRequestError(irpAutomationRequestError);
    }

    public void setAutomationRequestResultHandler(AutomationRequestResultHandler automationRequestResultHandler) {
        this.handler = automationRequestResultHandler;
    }
}
