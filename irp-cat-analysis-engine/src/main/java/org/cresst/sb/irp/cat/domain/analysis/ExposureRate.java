package org.cresst.sb.irp.cat.domain.analysis;

import java.util.Objects;

/**
 * Defines the "Exposure Rate" of an item
 * Contains a percentage of exposure rate (number of times an item occurs / total number of items in pool)
 * along with additional information about whether or not item was found in item pool and also in the test questions.
 * 
 */
public class ExposureRate implements Comparable<ExposureRate> {
    private String itemId;
    private double exposureRate;
    private boolean inItemPool;
    private boolean inTestQuestions;

    public String getItemId() {
        return itemId;
    }
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    public double getExposureRate() {
        return exposureRate;
    }
    public void setExposureRate(double exposureRate) {
        this.exposureRate = exposureRate;
    }
    public boolean isInItemPool() {
        return inItemPool;
    }
    public void setInItemPool(boolean inItemPool) {
        this.inItemPool = inItemPool;
    }
    public boolean isInTestQuestions() {
        return inTestQuestions;
    }
    public void setInTestQuestions(boolean inTestQuestions) {
        this.inTestQuestions = inTestQuestions;
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, exposureRate, inItemPool, inTestQuestions);
    }

    @Override
    public String toString() {
        return "ExposureRate [itemId=" + itemId + ", exposureRate=" + exposureRate + ", inItemPool=" + inItemPool
                + ", inTestQuestions=" + inTestQuestions + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ExposureRate other = (ExposureRate) obj;
        if (Double.doubleToLongBits(exposureRate) != Double.doubleToLongBits(other.exposureRate))
            return false;
        if (inItemPool != other.inItemPool)
            return false;
        if (inTestQuestions != other.inTestQuestions)
            return false;
        if (itemId == null) {
            if (other.itemId != null)
                return false;
        } else if (!itemId.equals(other.itemId))
            return false;
        return true;
    }
    @Override
    public int compareTo(ExposureRate o) {
        return Double.compare(this.exposureRate, o.getExposureRate());
    }

}
