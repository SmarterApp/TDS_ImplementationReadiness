package org.cresst.sb.irp.service;

import org.cresst.sb.irp.domain.analysis.AutomationRequest;
import org.cresst.sb.irp.domain.analysis.AutomationResponse;

public class IrpAutomationService implements AutomationService {
    /**
     * Using the information about a vendor's implementation, automates the pre-loading and Student simulation phases of
     * IRP.
     *
     * @param automationRequest
     * @return The results of the IRP automation phase.
     */
    @Override
    public AutomationResponse automate(AutomationRequest automationRequest) {
        return null;
    }
}
