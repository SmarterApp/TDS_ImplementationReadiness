package org.cresst.sb.irp.cat.domain.analysis;

import java.util.Arrays;
import java.util.Map;

/**
 * Collection of data and result for calculating exposure rates
 * 
 */
public class ExposureRateResponse {
    private Map<String, ExposureRate> exposureRates;
    private int unusedItems;
    private int itemPoolCount;
    private int usedItems;
    private double percentUnused;
    private double percentUsed;
    private int[] bins;
    private double binSize;

    public ExposureRateResponse() {
    }

    public Map<String, ExposureRate> getExposureRates() {
        return exposureRates;
    }

    public void setExposureRates(Map<String, ExposureRate> exposureRates) {
        this.exposureRates = exposureRates;
    }

    public int getUnusedItems() {
        return unusedItems;
    }

    public void setUnusedItems(int unusedItems) {
        this.unusedItems = unusedItems;
    }

    public int getItemPoolCount() {
        return itemPoolCount;
    }

    public void setItemPoolCount(int itemPoolCount) {
        this.itemPoolCount = itemPoolCount;
    }

    public int getUsedItems() {
        return usedItems;
    }

    public void setUsedItems(int usedItems) {
        this.usedItems = usedItems;
    }

    public double getPercentUnused() {
        return percentUnused;
    }

    public void setPercentUnused(double percentUnused) {
        this.percentUnused = percentUnused;
    }

    public double getPercentUsed() {
        return percentUsed;
    }

    public void setPercentUsed(double percentUsed) {
        this.percentUsed = percentUsed;
    }

    public int[] getBins() {
        return bins;
    }

    public void setBins(int[] bins) {
        this.bins = bins;
    }

    public double getBinSize() {
        return binSize;
    }

    public void setBinSize(double binSize) {
        this.binSize = binSize;
    }

    @Override
    public String toString() {
        return "ExposureRateResponse [exposureRates=" + exposureRates + ", unusedItems=" + unusedItems
                + ", itemPoolCount=" + itemPoolCount + ", usedItems=" + usedItems + ", percentUnused=" + percentUnused
                + ", percentUsed=" + percentUsed + ", bins=" + Arrays.toString(bins) + ", binSize=" + binSize + "]";
    }
}