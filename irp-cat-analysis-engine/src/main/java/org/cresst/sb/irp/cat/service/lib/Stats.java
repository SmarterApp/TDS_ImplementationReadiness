package org.cresst.sb.irp.cat.service.lib;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.cresst.sb.irp.cat.domain.analysis.CATDataModel;
import org.cresst.sb.irp.cat.domain.analysis.ItemResponseCAT;
import org.cresst.sb.irp.cat.domain.analysis.PoolItemELA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Stats {
    private final static Logger logger = LoggerFactory.getLogger(Stats.class);

    public static double averageBias(Map<String, Double> trueValues, Map<String, Double> estimatedValues) {
        int n = estimatedValues.size();
        double diffResults = 0;
        double diff = 0;
        for(String key : estimatedValues.keySet()) {
            if (! trueValues.containsKey(key)) {
                logger.warn("True value not found for key: {}", key);
            } else {
                diff = trueValues.get(key) - estimatedValues.get(key);
                diffResults += diff;
            }
        }
        return diffResults / n;
    }

    public static double meanSquaredError(Map<String, Double> trueValues, Map<String, Double> estimatedValues) {
        int n = estimatedValues.size();
        double diffResults = 0;
        double diffSquared = 0;
        for(String key : estimatedValues.keySet()) {
            if (! trueValues.containsKey(key)) {
                logger.warn("True value not found for key: {}", key);
            } else {
                diffSquared = Math.pow(trueValues.get(key) - estimatedValues.get(key), 2);
                diffResults += diffSquared;
            }
        }
        return diffResults / n;
    }

    public static double rmse(Map<String, Double> trueValues, Map<String, Double> estimatedValues) {
        return Math.sqrt(meanSquaredError(trueValues, estimatedValues));
    }

    public static Map<String, Double> calculateExposureRates(CATDataModel catData) {
        Map<String, Double> exposureRates = new HashMap<>();

        // Initialize exposures to 0
        for(PoolItemELA poolItem : catData.getPoolItems()) {
            exposureRates.put(poolItem.getItemId(), 0.0);
        }

        int n = getUniqueStudentIds(catData).size();
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

        return exposureRates;
    }

    private static Set<String> getUniqueStudentIds(CATDataModel catData) {
        Set<String> sids = new HashSet<>();
        for(ItemResponseCAT item : catData.getItemResponses()) {
            sids.add(item.getsId());
        }
        return sids;
    }
}
