package org.cresst.sb.irp.service;

import org.cresst.sb.irp.auto.engine.AutomationEngine;
import org.cresst.sb.irp.domain.automation.AutomationRequest;
import org.cresst.sb.irp.domain.automation.AutomationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IrpAutomationService implements AutomationService {

    @Autowired
    AutomationEngine automationEngine;

    /**
     * Using the information about a vendor's implementation, automates the pre-loading and Student simulation phases of
     * IRP.
     *
     * @param automationRequest
     * @return The results of the IRP automation phase.
     */
    @Override
    public AutomationResponse automate(AutomationRequest automationRequest) throws Exception {
        return automationEngine.automate(automationRequest);
    }
}
