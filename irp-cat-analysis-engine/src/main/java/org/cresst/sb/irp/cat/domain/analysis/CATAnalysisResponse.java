package org.cresst.sb.irp.cat.domain.analysis;

import java.util.Map;

public class CATAnalysisResponse {
    private Map<String, Double> exposureRates;
    private int unusedItems;
    private int itemPoolCount;
    private int usedItems;
    private double percentUnused;
    private double percentUsed;
    private int[] bins;
    private double binSize;
    private double averageBias;
    private double rmse;

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

    public double getPercentUsed() {
        return percentUsed;
    }

    public void setPercentUsed(double percentUsed) {
        this.percentUsed = percentUsed;
    }

    public int getUsedItems() {
        return usedItems;
    }

    public void setUsedItems(int usedItems) {
        this.usedItems = usedItems;
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

    public double getAverageBias() {
        return averageBias;
    }

    public void setAverageBias(double averageBias) {
        this.averageBias = averageBias;
    }

    public double getRmse() {
        return rmse;
    }

    public void setRmse(double rmse) {
        this.rmse = rmse;
    }
}
