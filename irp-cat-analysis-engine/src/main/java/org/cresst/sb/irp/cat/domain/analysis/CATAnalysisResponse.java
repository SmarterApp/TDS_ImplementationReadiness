package org.cresst.sb.irp.cat.domain.analysis;

import java.util.Map;

public class CATAnalysisResponse {
    private Map<String, Double> exposureRates;
    private int unusedItems;
    private int itemPoolCount;
    private double percentUnused;

    public Map<String, Double> getExposureRates() {
        return exposureRates;
    }

    public void setExposureRates(Map<String, Double> exposureRates) {
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

    public double getPercentUnused() {
        return percentUnused;
    }

    public void setPercentUnused(double percentUnused) {
        this.percentUnused = percentUnused;
    }
}
