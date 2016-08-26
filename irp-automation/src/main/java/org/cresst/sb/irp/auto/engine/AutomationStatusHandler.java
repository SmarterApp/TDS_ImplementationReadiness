package org.cresst.sb.irp.auto.engine;

import org.cresst.sb.irp.domain.automation.AutomationStatusReport;
import org.cresst.sb.irp.domain.automation.AutomationToken;

public interface AutomationStatusHandler {
    void handleAutomationStatus(final AutomationToken automationToken, final AutomationStatusReport automationStatusReport);
}
