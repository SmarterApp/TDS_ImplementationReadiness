package org.cresst.sb.irp.auto.engine;

import org.cresst.sb.irp.domain.automation.AutomationRequest;
import org.cresst.sb.irp.domain.automation.AutomationResponse;

public interface AutomationEngine {
    AutomationResponse automate(AutomationRequest automationRequest) throws Exception;
}
