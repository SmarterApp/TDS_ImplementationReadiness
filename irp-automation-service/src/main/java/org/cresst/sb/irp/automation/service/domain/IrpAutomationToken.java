package org.cresst.sb.irp.automation.service.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IrpAutomationToken {
    private int token;

    public IrpAutomationToken(IrpAutomationRequest irpAutomationRequest) {
        this.token = irpAutomationRequest.hashCode();
    }

    @JsonCreator
    public IrpAutomationToken(@JsonProperty("token") int token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IrpAutomationToken that = (IrpAutomationToken) o;
        return token == that.token;
    }

    @Override
    public int hashCode() {
        return token;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AutomationToken{");
        sb.append("token=").append(token);
        sb.append('}');
        return sb.toString();
    }

    public int getToken() {
        return token;
    }
}
