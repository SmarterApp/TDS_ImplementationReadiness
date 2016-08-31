package org.cresst.sb.irp.automation.engine;

import org.cresst.sb.irp.domain.automation.AutomationStatusReport;
import org.cresst.sb.irp.domain.automation.AutomationToken;

public class AutomationStatusHandlerProxy implements AutomationStatusHandler {

    private AutomationStatusHandler handler;

    public void setAutomationStatusHandler(AutomationStatusHandler handler) {
        this.handler = handler;
    }

    @Override
    public void handleAutomationStatus(AutomationToken automationToken, AutomationStatusReport automationStatusReport) {
        handler.handleAutomationStatus(automationToken, automationStatusReport);
    }
}
