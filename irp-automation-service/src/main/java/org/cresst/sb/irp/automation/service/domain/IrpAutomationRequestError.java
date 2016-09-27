package org.cresst.sb.irp.automation.service.domain;

public class IrpAutomationRequestError {
    private final boolean isAutomationRequestError = true;
    private final String errorMessage;
    private final IrpAutomationRequest irpAutomationRequest;

    public IrpAutomationRequestError(final String errorMessage, final IrpAutomationRequest irpAutomationRequest) {
        this.errorMessage = errorMessage;
        this.irpAutomationRequest = irpAutomationRequest;
    }

    public boolean isAutomationRequestError() {
        return isAutomationRequestError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public IrpAutomationRequest getIrpAutomationRequest() {
        return irpAutomationRequest;
    }
}
