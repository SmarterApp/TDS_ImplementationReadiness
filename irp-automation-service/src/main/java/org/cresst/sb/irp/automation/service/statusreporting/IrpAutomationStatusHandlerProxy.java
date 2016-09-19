package org.cresst.sb.irp.automation.service.statusreporting;

import org.cresst.sb.irp.automation.service.domain.AutomationStatusReport;
import org.cresst.sb.irp.automation.service.domain.AutomationToken;

public class IrpAutomationStatusHandlerProxy implements AutomationStatusHandler {

    private AutomationStatusHandler handler;

    public void setAutomationStatusHandler(AutomationStatusHandler automationStatusHandler) {
        this.handler = automationStatusHandler;
    }

    @Override
    public void handleAutomationStatus(AutomationToken automationToken, AutomationStatusReport automationStatusReport) {
        handler.handleAutomationStatus(automationToken, automationStatusReport);
    }
}
