package org.cresst.sb.irp.service;

import org.cresst.sb.irp.auto.engine.AutomationEngine;
import org.cresst.sb.irp.domain.automation.AutomationRequest;
import org.springframework.stereotype.Service;

@Service
public class IrpAutomationService implements AutomationService {

    AutomationEngine automationEngine;

    public IrpAutomationService(AutomationEngine automationEngine) {
        this.automationEngine = automationEngine;
    }

    /**
     * Using the information about a vendor's implementation, automates the pre-loading and Student simulation phases of
     * IRP.
     *
     * @param automationRequest The information needed to run the automation
     * @return The results of the IRP automation phase.
     */
    @Override
    public void automate(AutomationRequest automationRequest) {
        automationEngine.automate(automationRequest);
    }
}
