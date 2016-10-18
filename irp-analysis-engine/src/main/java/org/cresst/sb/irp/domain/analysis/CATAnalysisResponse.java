package org.cresst.sb.irp.domain.analysis;

import java.util.Map;

public class CATAnalysisResponse {
    private Map<String, Double> exposureRates;

    public Map<String, Double> getExposureRates() {
        return exposureRates;
    }

    public void setExposureRates(Map<String, Double> exposureRates) {
        this.exposureRates = exposureRates;
    }
}
