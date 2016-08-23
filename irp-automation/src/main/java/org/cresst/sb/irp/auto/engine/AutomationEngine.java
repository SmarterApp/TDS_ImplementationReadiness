package org.cresst.sb.irp.auto.engine;

import org.cresst.sb.irp.domain.automation.AutomationRequest;
import org.cresst.sb.irp.domain.automation.AutomationToken;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Using the AutomationRequest, implementing classes will run IRP Automation against a vendor's Smarter Balanced
 * Open Source Assessment Delivery System.
 */
public interface AutomationEngine {
    /**
     * Performs the IRP Automation
     * @param automationRequest The information needed to run IRP Automation
     */
    void automate(AutomationRequest automationRequest);
}
