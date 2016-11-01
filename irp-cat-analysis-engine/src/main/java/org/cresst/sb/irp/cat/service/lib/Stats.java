package org.cresst.sb.irp.cat.service.lib;

import java.util.Map;

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
}
