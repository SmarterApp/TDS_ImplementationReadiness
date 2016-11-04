package org.cresst.sb.irp.cat.domain.analysis;

public class ViolationCount {
    private int under;
    private int match;
    private int over;
    private int claim;

    public void incUnder() {
        this.under += 1;
    }
    public void incMatch() {
        this.under += 1;
    }
    public void incOver() {
        this.under += 1;
    }

    public int getUnder() {
        return under;
    }
    public void setUnder(int under) {
        this.under = under;
    }
    public int getMatch() {
        return match;
    }
    public void setMatch(int match) {
        this.match = match;
    }
    public int getOver() {
        return over;
    }
    public void setOver(int over) {
        this.over = over;
    }
    public int getClaim() {
        return claim;
    }
    public void setClaim(int claim) {
        this.claim = claim;
    }
    @Override
    public String toString() {
        return "ViolationCount [under=" + under + ", match=" + match + ", over=" + over + ", claim=" + claim + "]";
    }
}
