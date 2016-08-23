package org.cresst.sb.irp.domain.automation;

public class AutomationStatus {
    private String message;

    public AutomationStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
