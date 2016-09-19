package org.cresst.sb.irp.automation.service.domain;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class AutomationStatusReport {
    private boolean automationComplete = false;
    private long lastUpdateTimestamp;
    private Map<AutomationPhase, List<String>> phaseStatuses = new EnumMap<>(AutomationPhase.class);

    public void appendMessage(AutomationPhase automationPhase, String message, long timestamp) {
        lastUpdateTimestamp = timestamp;

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
