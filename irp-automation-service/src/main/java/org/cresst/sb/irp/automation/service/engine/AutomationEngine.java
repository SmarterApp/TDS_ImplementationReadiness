package org.cresst.sb.irp.automation.service.engine;

import org.cresst.sb.irp.automation.service.domain.IrpAutomationRequest;

public interface AutomationEngine {
    void automate(IrpAutomationRequest irpAutomationRequest);
}
