package org.cresst.sb.irp.automation.service.domain;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class AutomationStatusRequest {
    // Client sends back server generated timestamp of previous message sent
    private long timeOfLastStatus;

    @NotNull
    private AutomationToken automationToken;

    public long getTimeOfLastStatus() {
        return timeOfLastStatus;
    }

    public AutomationToken getAutomationToken() {
        return automationToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AutomationStatusRequest that = (AutomationStatusRequest) o;
        return timeOfLastStatus == that.timeOfLastStatus &&
                Objects.equals(automationToken, that.automationToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeOfLastStatus, automationToken);
    }
}
