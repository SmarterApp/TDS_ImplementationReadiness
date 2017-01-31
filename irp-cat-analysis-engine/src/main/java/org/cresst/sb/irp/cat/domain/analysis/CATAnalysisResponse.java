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
    private double[] cutoffLevels;
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

    public double[] getCutoffLevels() {
        return cutoffLevels;
    }

    public void setCutoffLevels(double[] cutoffLevels) {
        this.cutoffLevels = cutoffLevels;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(averageBias);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((blueprintStatements == null) ? 0 : blueprintStatements.hashCode());
        temp = Double.doubleToLongBits(claim1SEM);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(claim2SEM);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(claim2_4SEM);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(claim3SEM);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(claim4SEM);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((claimViolations == null) ? 0 : claimViolations.hashCode());
        result = prime * result + ((claimViolationsMap == null) ? 0 : claimViolationsMap.hashCode());
        result = prime * result + Arrays.deepHashCode(classAccMatrix);
        temp = Double.doubleToLongBits(classAccuracy);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + Arrays.hashCode(cutoffLevels);
        result = prime * result + ((dateTimeAnalyzed == null) ? 0 : dateTimeAnalyzed.hashCode());
        result = prime * result + Arrays.hashCode(decileAverageBias);
        result = prime * result + Arrays.hashCode(decileRmse);
        result = prime * result + (error ? 1231 : 1237);
        result = prime * result + ((errorMessage == null) ? 0 : errorMessage.hashCode());
        result = prime * result + ((exposureRateResponse == null) ? 0 : exposureRateResponse.hashCode());
        result = prime * result + grade;
        result = prime * result + ((irpVersion == null) ? 0 : irpVersion.hashCode());
        temp = Double.doubleToLongBits(overallSEM);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(rmse);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((subject == null) ? 0 : subject.hashCode());
        result = prime * result + ((vendorName == null) ? 0 : vendorName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CATAnalysisResponse other = (CATAnalysisResponse) obj;
        if (Double.doubleToLongBits(averageBias) != Double.doubleToLongBits(other.averageBias))
            return false;
        if (blueprintStatements == null) {
            if (other.blueprintStatements != null)
                return false;
        } else if (!blueprintStatements.equals(other.blueprintStatements))
            return false;
        if (Double.doubleToLongBits(claim1SEM) != Double.doubleToLongBits(other.claim1SEM))
            return false;
        if (Double.doubleToLongBits(claim2SEM) != Double.doubleToLongBits(other.claim2SEM))
            return false;
        if (Double.doubleToLongBits(claim2_4SEM) != Double.doubleToLongBits(other.claim2_4SEM))
            return false;
        if (Double.doubleToLongBits(claim3SEM) != Double.doubleToLongBits(other.claim3SEM))
            return false;
        if (Double.doubleToLongBits(claim4SEM) != Double.doubleToLongBits(other.claim4SEM))
            return false;
        if (claimViolations == null) {
            if (other.claimViolations != null)
                return false;
        } else if (!claimViolations.equals(other.claimViolations))
            return false;
        if (claimViolationsMap == null) {
            if (other.claimViolationsMap != null)
                return false;
        } else if (!claimViolationsMap.equals(other.claimViolationsMap))
            return false;
        if (!Arrays.deepEquals(classAccMatrix, other.classAccMatrix))
            return false;
        if (Double.doubleToLongBits(classAccuracy) != Double.doubleToLongBits(other.classAccuracy))
            return false;
        if (!Arrays.equals(cutoffLevels, other.cutoffLevels))
            return false;
        if (dateTimeAnalyzed == null) {
            if (other.dateTimeAnalyzed != null)
                return false;
        } else if (!dateTimeAnalyzed.equals(other.dateTimeAnalyzed))
            return false;
        if (!Arrays.equals(decileAverageBias, other.decileAverageBias))
            return false;
        if (!Arrays.equals(decileRmse, other.decileRmse))
            return false;
        if (error != other.error)
            return false;
        if (errorMessage == null) {
            if (other.errorMessage != null)
                return false;
        } else if (!errorMessage.equals(other.errorMessage))
            return false;
        if (exposureRateResponse == null) {
            if (other.exposureRateResponse != null)
                return false;
        } else if (!exposureRateResponse.equals(other.exposureRateResponse))
            return false;
        if (grade != other.grade)
            return false;
        if (irpVersion == null) {
            if (other.irpVersion != null)
                return false;
        } else if (!irpVersion.equals(other.irpVersion))
            return false;
        if (Double.doubleToLongBits(overallSEM) != Double.doubleToLongBits(other.overallSEM))
            return false;
        if (Double.doubleToLongBits(rmse) != Double.doubleToLongBits(other.rmse))
            return false;
        if (subject == null) {
            if (other.subject != null)
                return false;
        } else if (!subject.equals(other.subject))
            return false;
        if (vendorName == null) {
            if (other.vendorName != null)
                return false;
        } else if (!vendorName.equals(other.vendorName))
            return false;
        return true;
    }

}
