package org.cresst.sb.irp.automation.service.statusreporting;

import org.cresst.sb.irp.automation.service.domain.AutomationStatusReport;
import org.cresst.sb.irp.automation.service.domain.IrpAutomationToken;

public class IrpAutomationStatusHandlerProxy implements AutomationStatusHandler {

    private AutomationStatusHandler handler;

    public void setAutomationStatusHandler(AutomationStatusHandler automationStatusHandler) {
        this.handler = automationStatusHandler;
    }

    @Override
    public void handleAutomationStatus(IrpAutomationToken irpAutomationToken, AutomationStatusReport automationStatusReport) {
        handler.handleAutomationStatus(irpAutomationToken, automationStatusReport);
    }
}
