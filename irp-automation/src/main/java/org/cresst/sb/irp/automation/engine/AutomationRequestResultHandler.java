package org.cresst.sb.irp.automation.engine;

import org.cresst.sb.irp.domain.automation.AutomationRequest;
import org.cresst.sb.irp.domain.automation.AutomationRequestError;
import org.cresst.sb.irp.domain.automation.AutomationToken;

public interface AutomationRequestResultHandler {
    void handleAutomationRequestResult(final AutomationRequest automationRequest, final AutomationToken automationToken);
    void handleAutomationRequestError(final AutomationRequestError automationRequestError);
}
