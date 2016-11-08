package org.cresst.sb.irp.cat.domain.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CATAnalysisResponse {
    private ExposureRateResponse exposureRateResponse = new ExposureRateResponse();
    private double averageBias;
    private double rmse;
    private double[] decileAverageBias;
    private double[] decileRmse;
    private int[][] classAccMatrix;
    private double classAccuracy;
    private List<BlueprintStatement> blueprintStatements;
    private Map<Integer, ViolationCount> claimViolationsMap;
    private List<ViolationCount> claimViolations;

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

    public double[] getDecileAverageBias() {
        return decileAverageBias;
    }

    public void setDecileAverageBias(double[] decileAverageBias) {
        this.decileAverageBias = decileAverageBias;
    }

    public double[] getDecileRmse() {
        return decileRmse;
    }

    public void setDecileRmse(double[] decileRmse) {
        this.decileRmse = decileRmse;
    }

    public int[][] getClassAccMatrix() {
        return classAccMatrix;
    }

    public void setClassAccMatrix(int[][] classAccMatrix) {
        this.classAccMatrix = classAccMatrix;
    }

    public double getClassAccuracy() {
        return classAccuracy;
    }

    public void setClassAccuracy(double classAccuracy) {
        this.classAccuracy = classAccuracy;
    }

    public List<BlueprintStatement> getBlueprintStatements() {
        return blueprintStatements;
    }

    public void setBlueprintStatements(List<BlueprintStatement> blueprintStatements) {
        this.blueprintStatements = blueprintStatements;
    }

    public Map<Integer, ViolationCount> getClaimViolationsMap() {
        return claimViolationsMap;
    }

    public void setClaimViolationsMap(Map<Integer, ViolationCount> violationCounts) {
        this.claimViolationsMap = violationCounts;
    }

    public List<ViolationCount> getClaimViolations() {
        return claimViolations;
    }

    public void setClaimViolations(List<ViolationCount> claimViolations) {
        this.claimViolations = claimViolations;
    }

    @Override
    public String toString() {
        return "CATAnalysisResponse [exposureRateResponse=" + exposureRateResponse + ", averageBias=" + averageBias
                + ", rmse=" + rmse + ", decileAverageBias=" + Arrays.toString(decileAverageBias) + ", decileRmse="
                + Arrays.toString(decileRmse) + ", classAccMatrix=" + Arrays.toString(classAccMatrix)
                + ", classAccuracy=" + classAccuracy + ", blueprintStatements=" + blueprintStatements
                + ", claimViolationsMap=" + claimViolationsMap + "]";
    }
}
