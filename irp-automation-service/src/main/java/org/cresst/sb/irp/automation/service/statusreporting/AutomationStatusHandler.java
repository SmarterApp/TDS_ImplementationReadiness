package org.cresst.sb.irp.automation.service.statusreporting;

import org.cresst.sb.irp.automation.service.domain.AutomationStatusReport;
import org.cresst.sb.irp.automation.service.domain.AutomationToken;

public interface AutomationStatusHandler {
    void handleAutomationStatus(final AutomationToken automationToken, final AutomationStatusReport automationStatusReport);
}
