package org.cresst.sb.irp.automation.service.statusreporting;

import org.cresst.sb.irp.automation.service.domain.AutomationStatusReport;
import org.cresst.sb.irp.automation.service.domain.IrpAutomationToken;

public interface AutomationStatusHandler {
    void handleAutomationStatus(final IrpAutomationToken irpAutomationToken, final AutomationStatusReport automationStatusReport);
}
