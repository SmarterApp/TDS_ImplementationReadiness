package org.cresst.sb.irp.cat.domain.analysis;

import java.util.Map;

public class CATAnalysisResponse {
    private ExposureRateResponse exposureRateResponse = new ExposureRateResponse();
    private double averageBias;
    private double rmse;

    public Map<String, ExposureRate> getExposureRates() {
        return exposureRateResponse.getExposureRates();
    }

    public void setExposureRates(Map<String, ExposureRate> exposureRates) {
        this.exposureRateResponse.setExposureRates(exposureRates);
    }

    public int getUnusedItems() {
        return exposureRateResponse.getUnusedItems();
    }

    public void setUnusedItems(int unusedItems) {
        this.exposureRateResponse.setUnusedItems(unusedItems);
    }

    public int getItemPoolCount() {
        return exposureRateResponse.getItemPoolCount();
    }

    public void setItemPoolCount(int itemPoolCount) {
        this.exposureRateResponse.setItemPoolCount(itemPoolCount);
    }

    public double getPercentUnused() {
        return exposureRateResponse.getPercentUnused();
    }

    public void setPercentUnused(double percentUnused) {
        this.exposureRateResponse.setPercentUnused(percentUnused);
    }

    public double getPercentUsed() {
        return exposureRateResponse.getPercentUsed();
    }

    public void setPercentUsed(double percentUsed) {
        this.exposureRateResponse.setPercentUsed(percentUsed);
    }

    public int getUsedItems() {
        return exposureRateResponse.getUsedItems();
    }

    public void setUsedItems(int usedItems) {
        this.exposureRateResponse.setUsedItems(usedItems);
    }

    public int[] getBins() {
        return exposureRateResponse.getBins();
    }

    public void setBins(int[] bins) {
        this.exposureRateResponse.setBins(bins);
    }

    public double getBinSize() {
        return exposureRateResponse.getBinSize();
    }

    public void setBinSize(double binSize) {
        this.exposureRateResponse.setBinSize(binSize);
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
