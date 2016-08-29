package org.cresst.sb.irp.domain.automation;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AutomationPhase {
    INITIALIZATION("Initialization"),
    PRELOADING("Preloading"),
    SIMULATION("Student Simulation"),
    ANALYSIS("Analysis"),
    CLEANUP("Cleanup");

    private String name;

    AutomationPhase(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
