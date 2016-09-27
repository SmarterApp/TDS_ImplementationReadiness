package org.cresst.sb.irp.automation.service.statusreporting;

import org.cresst.sb.irp.automation.service.domain.AutomationPhase;
import org.cresst.sb.irp.automation.service.domain.AutomationStatusReport;
import org.cresst.sb.irp.automation.service.domain.IrpAutomationToken;
import org.joda.time.Instant;

public class IrpAutomationStatusReporter implements AutomationStatusReporter {

    private final IrpAutomationToken irpAutomationToken;
    private final AutomationStatusReport automationStatusReport;
    private final AutomationStatusHandler automationStatusHandler;
    private final AutomationPhase automationPhase;

    public IrpAutomationStatusReporter(AutomationPhase automationPhase,
                                       IrpAutomationToken irpAutomationToken,
                                       AutomationStatusReport automationStatusReport,
                                       AutomationStatusHandler automationStatusHandler) {
        this.automationPhase = automationPhase;
        this.irpAutomationToken = irpAutomationToken;
        this.automationStatusReport = automationStatusReport;
        this.automationStatusHandler = automationStatusHandler;
    }

    @Override
    public void status(String message) {
        automationStatusReport.appendMessage(automationPhase, message, Instant.now().getMillis());
        automationStatusHandler.handleAutomationStatus(irpAutomationToken, automationStatusReport);
    }

    @Override
    public void markAutomationComplete() {
        automationStatusReport.markAutomationComplete();
    }
}
