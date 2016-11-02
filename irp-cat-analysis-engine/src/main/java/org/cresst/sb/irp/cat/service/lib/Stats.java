package org.cresst.sb.irp.cat.service.lib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cresst.sb.irp.cat.domain.analysis.CATAnalysisResponse;
import org.cresst.sb.irp.cat.domain.analysis.CATDataModel;
import org.cresst.sb.irp.cat.domain.analysis.ExposureRate;
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

    public static Map<String, ExposureRate> calculateExposureRates(CATDataModel catData) {
        Map<String, ExposureRate> exposureRates = new HashMap<>();

        // Initialize exposures to 0
        for(PoolItemELA poolItem : catData.getPoolItems()) {
            ExposureRate exposureRate = new ExposureRate();
            exposureRate.setExposureRate(0.0);
            exposureRate.setInItemPool(true);
            exposureRate.setInTestQuestions(false);
            exposureRates.put(poolItem.getItemId(), exposureRate);
        }

        int n = getUniqueStudentIds(catData).size();
        for(ItemResponseCAT itemResponse : catData.getItemResponses()) {
            String itemId = itemResponse.getItemId();
            ExposureRate oldResult = exposureRates.get(itemId);
            double incrementValue =  1 /(double) n;
            if(oldResult != null) {
                oldResult.setExposureRate(oldResult.getExposureRate() + incrementValue);
            } else {
                logger.warn("item id: {} was not found in item pool.", itemId);
                ExposureRate exposureRate = new ExposureRate();
                exposureRate.setExposureRate(incrementValue);
                exposureRate.setInItemPool(false);
                exposureRate.setInTestQuestions(true);

                exposureRates.put(itemId, exposureRate);
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

    private static int binIndex(double value, double stepSize) {
        return (int) Math.floor(value / stepSize);
    }

    public static void calculateExposureBins(CATAnalysisResponse response, double binSize) {
        int unusedCount = 0;
        int totalCount = 0;
        double maxValue = Collections.max(response.getExposureRates().values()).getExposureRate();
        int binCount = (int) Math.floor(maxValue / binSize) + 1;
        int[] bins = new int[binCount];

        logger.debug("maxValue: {}, binCount: {}", maxValue, binCount);
        for(ExposureRate exposureRate : response.getExposureRates().values()) {
            double exposureValue = exposureRate.getExposureRate();
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
    }

    // Algorithm from: http://mba-lectures.com/statistics/descriptive-statistics/222/deciles.html
    public static double[] decileValues(Collection<Double> scores) {
        // Make copy of scores before sorting
        List<Double> sortedScores = new ArrayList<>(scores);
        Collections.sort(sortedScores);

        int n = sortedScores.size();
        double[] deciles = new double[9];
        for(int i = 1; i < 10; i ++) {
            double obs = (i * (n + 1)) / 10.0;
            int obsInt = (int) Math.floor(obs);
            double obsFrac = obs - obsInt;

            if (obsInt < n) {
                deciles[i - 1] = sortedScores.get(obsInt - 1) + obsFrac * (sortedScores.get(obsInt) - sortedScores.get(obsInt - 1));
            }
            else {
                deciles[i - 1] = sortedScores.get(obsInt - 1);
            }
        }

        return deciles;
    }

    private static int findDecileIndex(double[] deciles, double value) {
        int n = deciles.length;
        for(int i = 0; i < n; i++) {
            if (i == 0 && value < deciles[0]) {
                return 0;
            } else if (i > 0 && value >= deciles[i - 1] && value < deciles[i]) {
                return i;
            }
        }
        assert(value > deciles[n - 1]);
        return n;
    }

    // Partitions scores into decile bin
    public static List<Map<String, Double>> decilePartition(Map<String, Double> scores) {
        List<Map<String, Double>> results = new ArrayList<>();
        for(int i = 0; i < 10; i++) results.add(new HashMap<String, Double>());

        double[] decileValues = decileValues(scores.values());
        for(String key : scores.keySet()) {
            double value = scores.get(key);
            int decileBin = findDecileIndex(decileValues, value);
            results.get(decileBin).put(key, value);
        }

        return results;
    }
}
