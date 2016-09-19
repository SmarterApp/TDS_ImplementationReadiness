package org.cresst.sb.irp.automation.service.engine;

import org.cresst.sb.irp.automation.service.domain.AutomationRequest;

public interface AutomationEngine {
    void automate(AutomationRequest automationRequest);
}
