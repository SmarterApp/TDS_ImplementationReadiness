package org.cresst.sb.irp.service;

import java.util.HashMap;
import java.util.Map;

import org.cresst.sb.irp.domain.analysis.CATAnalysisResponse;
import org.cresst.sb.irp.domain.analysis.CATDataModel;
import org.cresst.sb.irp.domain.analysis.ItemResponseCAT;
import org.cresst.sb.irp.domain.analysis.PoolItemCAT;
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

        return response;
    }

}
