package org.cresst.sb.irp.service;

import org.cresst.sb.irp.domain.automation.AutomationRequest;

public interface AutomationService {
    /**
     * Using the information about a vendor's implementation, automates the pre-loading and Student simulation phases of
     * IRP.
     * @param automationRequest The information needed to run the automation.
     */
    void automate(AutomationRequest automationRequest);
}
