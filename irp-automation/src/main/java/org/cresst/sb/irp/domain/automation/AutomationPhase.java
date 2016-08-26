package org.cresst.sb.irp.domain.automation;

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

    @Override
    public String toString() {
        return name;
    }
}
