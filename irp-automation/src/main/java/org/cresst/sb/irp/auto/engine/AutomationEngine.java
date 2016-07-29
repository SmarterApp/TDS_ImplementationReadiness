package org.cresst.sb.irp.auto.engine;

import org.cresst.sb.irp.domain.analysis.AutomationRequest;
import org.cresst.sb.irp.domain.analysis.AutomationResponse;

public interface AutomationEngine {
    AutomationResponse automate(AutomationRequest automationRequest);
}
