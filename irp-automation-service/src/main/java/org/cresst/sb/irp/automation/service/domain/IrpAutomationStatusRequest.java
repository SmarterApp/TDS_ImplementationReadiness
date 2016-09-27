package org.cresst.sb.irp.automation.service.domain;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class IrpAutomationStatusRequest {
    // Client sends back server generated timestamp of previous message sent
    private long timeOfLastStatus;

    @NotNull
    private IrpAutomationToken irpAutomationToken;

    public long getTimeOfLastStatus() {
        return timeOfLastStatus;
    }

    public IrpAutomationToken getIrpAutomationToken() {
        return irpAutomationToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IrpAutomationStatusRequest that = (IrpAutomationStatusRequest) o;
        return timeOfLastStatus == that.timeOfLastStatus &&
                Objects.equals(irpAutomationToken, that.irpAutomationToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeOfLastStatus, irpAutomationToken);
    }
}
