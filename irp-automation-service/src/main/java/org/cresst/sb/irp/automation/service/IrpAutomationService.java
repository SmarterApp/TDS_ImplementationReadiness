package org.cresst.sb.irp.automation.service;

import org.cresst.sb.irp.automation.service.domain.AutomationRequest;
import org.cresst.sb.irp.automation.service.engine.AutomationEngine;

public class IrpAutomationService implements AutomationService {

    private final AutomationEngine automationEngine;

    public IrpAutomationService(AutomationEngine automationEngine) {
        this.automationEngine = automationEngine;
    }

    @Override
    public void automate(AutomationRequest automationRequest) {
        automationEngine.automate(automationRequest);
    }
}
