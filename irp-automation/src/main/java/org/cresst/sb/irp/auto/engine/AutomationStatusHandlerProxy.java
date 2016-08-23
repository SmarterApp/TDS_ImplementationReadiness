package org.cresst.sb.irp.auto.engine;

import org.cresst.sb.irp.domain.automation.AutomationStatus;
import org.cresst.sb.irp.domain.automation.AutomationToken;

public class AutomationStatusHandlerProxy implements AutomationStatusHandler {

    private AutomationStatusHandler handler;

    public void setAutomationStatusHandler(AutomationStatusHandler handler) {
        this.handler = handler;
    }

    @Override
    public void handleAutomationStatus(AutomationToken automationToken, AutomationStatus automationStatus) {
        handler.handleAutomationStatus(automationToken, automationStatus);
    }
}
