package org.cresst.sb.irp.service;

import org.cresst.sb.irp.domain.automation.AutomationRequest;
import org.cresst.sb.irp.domain.automation.AutomationResponse;

public interface AutomationService {
    /**
     * Using the information about a vendor's implementation, automates the pre-loading and Student simulation phases of
     * IRP.
     * @return The results of the IRP automation phase.
     */
    AutomationResponse automate(AutomationRequest automationRequest) throws Exception;
}
