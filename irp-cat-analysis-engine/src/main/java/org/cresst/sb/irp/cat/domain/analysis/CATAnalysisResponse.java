package org.cresst.sb.irp.cat.domain.analysis;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Results from the Cat Simulation Analysis. Designed to be filled by `CATAnalysisService` and serialized to display on front end 
 */
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
    private double overallSEM;
    private double claim1SEM;
    private double claim2SEM;
    private double claim3SEM;
    private double claim4SEM;
    private double claim2_4SEM;
    private int grade;
    private String subject;
    private String vendorName;
    private String irpVersion;
    private String dateTimeAnalyzed;
    private String errorMessage;
    private boolean error;

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
                + ", claimViolationsMap=" + claimViolationsMap + ", claimViolations=" + claimViolations
                + ", overallSEM=" + overallSEM + ", claim1SEM=" + claim1SEM + ", claim2SEM=" + claim2SEM
                + ", claim3SEM=" + claim3SEM + ", claim4SEM=" + claim4SEM + ", claim2_4SEM=" + claim2_4SEM + "]";
    }

    public double getOverallSEM() {
        return overallSEM;
    }

    public void setOverallSEM(double overallSEM) {
        this.overallSEM = overallSEM;
    }

    public double getClaim1SEM() {
        return claim1SEM;
    }

    public void setClaim1SEM(double claim1sem) {
        claim1SEM = claim1sem;
    }

    public double getClaim2SEM() {
        return claim2SEM;
    }

    public void setClaim2SEM(double claim2sem) {
        claim2SEM = claim2sem;
    }

    public double getClaim3SEM() {
        return claim3SEM;
    }

    public void setClaim3SEM(double claim3sem) {
        claim3SEM = claim3sem;
    }

    public double getClaim4SEM() {
        return claim4SEM;
    }

    public void setClaim4SEM(double claim4sem) {
        claim4SEM = claim4sem;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public double getClaim2_4SEM() {
        return claim2_4SEM;
    }

    public void setClaim2_4SEM(double claim2_4sem) {
        claim2_4SEM = claim2_4sem;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getIrpVersion() {
        return irpVersion;
    }

    public void setIrpVersion(String irpVersion) {
        this.irpVersion = irpVersion;
    }

    public String getDateTimeAnalyzed() {
        return dateTimeAnalyzed;
    }

    public void setDateTimeAnalyzed(String dateTimeAnalyzed) {
        this.dateTimeAnalyzed = dateTimeAnalyzed;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
