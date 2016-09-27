package org.cresst.sb.irp.automation.service;

import org.cresst.sb.irp.automation.service.domain.IrpAutomationRequest;
import org.cresst.sb.irp.automation.service.engine.AutomationEngine;

public class IrpAutomationService implements AutomationService {

    private final AutomationEngine automationEngine;

    public IrpAutomationService(AutomationEngine automationEngine) {
        this.automationEngine = automationEngine;
    }

    @Override
    public void automate(IrpAutomationRequest irpAutomationRequest) {
        automationEngine.automate(irpAutomationRequest);
    }
}
