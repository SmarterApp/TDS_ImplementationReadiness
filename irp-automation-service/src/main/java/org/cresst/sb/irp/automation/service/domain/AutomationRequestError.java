package org.cresst.sb.irp.automation.service.domain;

public class AutomationRequestError {
    private final boolean isAutomationRequestError = true;
    private final String errorMessage;
    private final AutomationRequest automationRequest;

    public AutomationRequestError(final String errorMessage, final AutomationRequest automationRequest) {
        this.errorMessage = errorMessage;
        this.automationRequest = automationRequest;
    }

    public boolean isAutomationRequestError() {
        return isAutomationRequestError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public AutomationRequest getAutomationRequest() {
        return automationRequest;
    }
}
