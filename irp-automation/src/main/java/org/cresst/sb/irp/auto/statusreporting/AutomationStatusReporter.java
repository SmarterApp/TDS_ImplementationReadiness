package org.cresst.sb.irp.auto.statusreporting;

public interface AutomationStatusReporter {
    /**
     * Reports the current status to the user
     * @param message The status message to send
     */
    void status(String message);

    /**
     * Indicate that IRP Automation has completed
     */
    void markAutomationComplete();
}
