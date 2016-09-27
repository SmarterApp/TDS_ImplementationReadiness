package org.cresst.sb.irp.automation.service.requesthandler;

import org.cresst.sb.irp.automation.service.domain.IrpAutomationRequest;
import org.cresst.sb.irp.automation.service.domain.IrpAutomationRequestError;
import org.cresst.sb.irp.automation.service.domain.IrpAutomationToken;

/**
 * After an Automation Request has been processed, this handler is called. Implementors will handle the result of
 * the asynchronously processed Automation Request. The handler will be called in the same thread as the processed
 * Automation Request.
 */
public interface AutomationRequestResultHandler {
    void handleAutomationRequestResult(final IrpAutomationRequest irpAutomationRequest, final IrpAutomationToken irpAutomationToken);
    void handleAutomationRequestError(final IrpAutomationRequestError irpAutomationRequestError);
}
