package org.cresst.sb.irp.automation.service.requesthandler;

import org.cresst.sb.irp.automation.service.domain.AutomationRequest;
import org.cresst.sb.irp.automation.service.domain.AutomationRequestError;
import org.cresst.sb.irp.automation.service.domain.AutomationToken;

/**
 * After an Automation Request has been processed, this handler is called. Implementors will handle the result of
 * the asynchronously processed Automation Request. The handler will be called in the same thread as the processed
 * Automation Request.
 */
public interface AutomationRequestResultHandler {
    void handleAutomationRequestResult(final AutomationRequest automationRequest, final AutomationToken automationToken);
    void handleAutomationRequestError(final AutomationRequestError automationRequestError);
}
