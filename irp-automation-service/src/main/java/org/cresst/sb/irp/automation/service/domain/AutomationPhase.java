package org.cresst.sb.irp.automation.service.domain;

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

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
