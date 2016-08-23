package org.cresst.sb.irp.auto.engine;

import org.cresst.sb.irp.domain.automation.AutomationRequest;

interface AutomationRequestProcessor {
    void processAutomationRequest(AutomationRequest automationRequest);
}
