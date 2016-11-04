package org.cresst.sb.irp.cat.domain.analysis;

public class BlueprintStatement {
    private String claimName;
    private int claimNumber;
    private int min;
    private int max;
    public String getClaimName() {
        return claimName;
    }
    public void setClaimName(String claimName) {
        this.claimName = claimName;
    }
    public int getClaimNumber() {
        return claimNumber;
    }
    public void setClaimNumber(int claimNumber) {
        this.claimNumber = claimNumber;
    }
    public int getMin() {
        return min;
    }
    public void setMin(int min) {
        this.min = min;
    }
    public int getMax() {
        return max;
    }
    public void setMax(int max) {
        this.max = max;
    }
    @Override
    public String toString() {
        return "BlueprintStatement [claimName=" + claimName + ", claimNumber=" + claimNumber + ", min=" + min + ", max="
                + max + "]";
    }
}
