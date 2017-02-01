package org.cresst.sb.irp.cat.domain.analysis;

import java.util.Objects;

/**
 * Maintains a count of various blueprint violations
 */
public class ViolationCount {
    private int under;
    private int match;
    private int over;
    private int claim;

    public void incUnder() {
        this.under += 1;
    }
    public void incMatch() {
        this.match += 1;
    }
    public void incOver() {
        this.over += 1;
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

    @Override
    public int hashCode() {
        return Objects.hash(claim, match, over, under);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ViolationCount other = (ViolationCount) obj;
        if (claim != other.claim)
            return false;
        if (match != other.match)
            return false;
        if (over != other.over)
            return false;
        if (under != other.under)
            return false;
        return true;
    }

}
