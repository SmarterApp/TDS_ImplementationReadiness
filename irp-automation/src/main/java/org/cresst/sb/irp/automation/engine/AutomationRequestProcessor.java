package org.cresst.sb.irp.automation.engine;

import org.cresst.sb.irp.domain.automation.AutomationRequest;

interface AutomationRequestProcessor {
    void processAutomationRequest(AutomationRequest automationRequest);
}
