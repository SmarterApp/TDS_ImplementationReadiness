package org.cresst.sb.irp.domain.automation;

import org.joda.time.Instant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutomationStatusReport {
    private boolean automationComplete = false;
    private long lastUpdateTimestamp;
    private Map<AutomationPhase, List<String>> phaseStatuses = new HashMap<>();

    public void appendMessage(AutomationPhase automationPhase, String message) {
        lastUpdateTimestamp = Instant.now().getMillis();

        List<String> messages = phaseStatuses.get(automationPhase);
        if (messages == null) {
            messages = new ArrayList<>();
            phaseStatuses.put(automationPhase, messages);
        }
        messages.add(message);
    }

    public void markAutomationComplete() {
        automationComplete = true;
    }

    public boolean isAutomationComplete() {
        return automationComplete;
    }

    public long getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public Map<AutomationPhase, List<String>> getPhaseStatuses() {
        return phaseStatuses;
    }
}
