package org.cresst.sb.irp.cat.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.cresst.sb.irp.cat.domain.analysis.CATAnalysisResponse;
import org.cresst.sb.irp.cat.domain.analysis.CATDataModel;
import org.cresst.sb.irp.cat.domain.analysis.ItemResponseCAT;
import org.cresst.sb.irp.cat.domain.analysis.PoolItemCAT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CATAnalysisServiceImpl implements CATAnalysisService {
    private final static Logger logger = LoggerFactory.getLogger(CATAnalysisServiceImpl.class);

    @Override
    public CATAnalysisResponse analyzeCatResults(CATDataModel catData) {
        CATAnalysisResponse response = new CATAnalysisResponse();
        // Number of times an item appears
        Map<String, Double> exposureRates= new HashMap<>();

        // Initialize exposures to 0
        for(PoolItemCAT poolItem : catData.getPoolItems()) {
            exposureRates.put(poolItem.getItemId(), 0.0);
        }

        int n = catData.getStudentScores().size();
        for(ItemResponseCAT itemResponse : catData.getItemResponses()) {
            String itemId = itemResponse.getItemId();
            Double oldResult = exposureRates.get(itemId);
            if(oldResult != null) {
                exposureRates.put(itemId, oldResult + (1 /(double) n));
            } else {
                logger.warn("item id: {} was not found in item pool.", itemId);
                exposureRates.put(itemId, 1/(double) n);
            }

        }

        response.setExposureRates(exposureRates);

        // % increase for bins; hard-coded for 5%
        double binSize = .05;
        if (exposureCalculations(response, binSize)) {
            logger.debug("Successfully did exposure rate calculations");
        }

        return response;
    }

    private int binIndex(double value, double stepSize) {
        return (int) Math.floor(value / stepSize);
    }

    private boolean exposureCalculations(CATAnalysisResponse response, double binSize) {
        int unusedCount = 0;
        int totalCount = 0;
        double maxValue = Collections.max(response.getExposureRates().values());
        int binCount = (int) Math.floor(maxValue / binSize) + 1;
        int[] bins = new int[binCount];

        logger.debug("maxValue: {}, binCount: {}", maxValue, binCount);
        for(double exposureValue : response.getExposureRates().values()) {
            if (exposureValue == 0) {
                unusedCount++;
            } else {
                // Increment the count for bin
                bins[binIndex(exposureValue, binSize)]++;
            }
            totalCount++;
        }

        int usedCount = totalCount - unusedCount;
        response.setUnusedItems(unusedCount);
        response.setUsedItems(usedCount);
        response.setItemPoolCount(totalCount);
        response.setPercentUnused(unusedCount / ((double) totalCount));
        response.setPercentUsed(usedCount / ((double) totalCount));
        response.setBins(bins);
        response.setBinSize(binSize);
        return true;
    }
}
