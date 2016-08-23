package org.cresst.sb.irp.auto.engine;

import org.cresst.sb.irp.domain.automation.AutomationStatus;
import org.cresst.sb.irp.domain.automation.AutomationToken;

public interface AutomationStatusHandler {
    void handleAutomationStatus(final AutomationToken automationToken, final AutomationStatus automationStatus);
}
