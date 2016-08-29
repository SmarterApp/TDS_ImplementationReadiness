package org.cresst.sb.irp.domain.automation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class AutomationToken {
    private int token;

    public AutomationToken(AutomationRequest automationRequest) {
        this.token = automationRequest.hashCode();
    }

    @JsonCreator
    public AutomationToken(@JsonProperty("token") int token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AutomationToken that = (AutomationToken) o;
        return token == that.token;
    }

    @Override
    public int hashCode() {
        return token;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("token", token)
                .toString();
    }

    public int getToken() {
        return token;
    }
}
