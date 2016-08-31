package org.cresst.sb.irp.automation.statusreporting;

import org.cresst.sb.irp.automation.engine.AutomationStatusHandler;
import org.cresst.sb.irp.domain.automation.AutomationPhase;
import org.cresst.sb.irp.domain.automation.AutomationStatusReport;
import org.cresst.sb.irp.domain.automation.AutomationToken;

public class IrpAutomationStatusReporter implements AutomationStatusReporter {

    private final AutomationToken automationToken;
    private final AutomationStatusReport automationStatusReport;
    private final AutomationStatusHandler automationStatusHandler;
    private final AutomationPhase automationPhase;

    public IrpAutomationStatusReporter(AutomationPhase automationPhase,
                                       AutomationToken automationToken,
                                       AutomationStatusReport automationStatusReport,
                                       AutomationStatusHandler automationStatusHandler) {
        this.automationPhase = automationPhase;
        this.automationToken = automationToken;
        this.automationStatusReport = automationStatusReport;
        this.automationStatusHandler = automationStatusHandler;
    }

    @Override
    public void status(String message) {
        automationStatusReport.appendMessage(automationPhase, message);
        automationStatusHandler.handleAutomationStatus(automationToken, automationStatusReport);
    }

    @Override
    public void markAutomationComplete() {
        automationStatusReport.markAutomationComplete();
    }
}
