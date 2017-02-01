package org.cresst.sb.irp.cat.service.lib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cresst.sb.irp.cat.domain.analysis.CATAnalysisResponse;
import org.cresst.sb.irp.cat.domain.analysis.ELAStudentScoreCAT;
import org.cresst.sb.irp.cat.domain.analysis.ExposureRate;
import org.cresst.sb.irp.cat.domain.analysis.ItemResponseCAT;
import org.cresst.sb.irp.cat.domain.analysis.MathStudentScoreCAT;
import org.cresst.sb.irp.cat.domain.analysis.PoolItem;
import org.cresst.sb.irp.cat.domain.analysis.Score;
import org.cresst.sb.irp.cat.domain.analysis.StudentScoreCAT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Library of statistical functions used for CAT analysis report
 *
 */
public class Stats {
    private final static Logger logger = LoggerFactory.getLogger(Stats.class);

    /**
     *
     * @param trueValues Pairs of <id, value> are the population parameter for each id
     * @param estimatedValues Pairs of <id, value> which represent the estimated (simulated) value for id
     * @return the mean of the bias between each of the values in trueValues and estimatedValues
     */
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

    /**
     *
     * @param trueValues Pairs of <id, value> are the population parameter for each id
     * @param estimatedValuesPairs of <id, value> which represent the estimated (simulated) value for id
     * @return the mean squared error between each of the values in trueValues and estimatedValues
     */
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

    /**
     *
     * @param trueValues Pairs of <id, value> are the population parameter for each id
     * @param estimatedValuesPairs of <id, value> which represent the estimated (simulated) value for id
     * @return the root mean squared error between each of the values in trueValues and estimatedValues
     */
    public static double rmse(Map<String, Double> trueValues, Map<String, Double> estimatedValues) {
        return Math.sqrt(meanSquaredError(trueValues, estimatedValues));
    }

    /**
     *
     * @param poolItems
     *            A collection of items that a test should get items from
     * @param itemResponses
     *            A collection of items that were actual given on simulated
     *            tests
     * @return exposure rates in a Map with pairs <itemid, ExposureRate>
     */
    public static Map<String, ExposureRate> calculateExposureRates(List<? extends PoolItem> poolItems,
            Collection<ItemResponseCAT> itemResponses, String grade) {
        Map<String, ExposureRate> exposureRates = new HashMap<>();

        // Initialize exposures to 0
        for (PoolItem poolItem : poolItems) {
            if (!poolItem.getPoolGrade().equals(grade)) {
                continue;
            }
            ExposureRate exposureRate = new ExposureRate();
            exposureRate.setExposureRate(0.0);
            exposureRate.setInItemPool(true);
            exposureRate.setInTestQuestions(false);
            exposureRates.put(poolItem.getItemId(), exposureRate);
        }

        int n = getUniqueStudentIds(itemResponses).size();
        for(ItemResponseCAT itemResponse : itemResponses) {
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

    /**
     *
     * @param itemResponses Collection of item responses
     * @return A set of unique student ids
     */
    private static Set<String> getUniqueStudentIds(Collection<ItemResponseCAT> itemResponses) {
        Set<String> sids = new HashSet<>();
        for(ItemResponseCAT item : itemResponses) {
            sids.add(item.getsId());
        }
        return sids;
    }

    /**
     * Calculates the partition index of a given `value`, where an array is partitioned into
     * `stepSize` equal partitions.
     * @param value the value we want to know the index for
     * @param stepSize the size of each partition
     * @return A zero based index of where to place `value` in a equally partitioned array
     */
    private static int binIndex(double value, double stepSize) {
        return (int) Math.floor(value / stepSize);
    }

    /**
     * Populates `CATAnalysisResponse response` with exposure rate analysis calculations.
     * @param response Current results of the cat analysis
     * @param binSize the size of partition for exposure rates
     */
    public static void calculateExposureBins(CATAnalysisResponse response, double binSize) {
        int unusedCount = 0;
        int totalCount = 0;
        double maxValue = 1.0;
        int binCount = (int) Math.floor(maxValue / binSize);
        int[] bins = new int[binCount];

        if (response.getExposureRates() == null) {
            return;
        }
        for(ExposureRate exposureRate : response.getExposureRates().values()) {
            double exposureValue = exposureRate.getExposureRate();
            if (exposureValue == 0) {
                unusedCount++;
            } else {
                // Increment the count for bin
                int bindex = binIndex(exposureValue, binSize);
                // If we have the max value, put it in last bin
                if (bindex >= bins.length) {
                    bindex = bins.length - 1;
                }
                bins[bindex]++;
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

    /**
     *
     * @param scores A collection of values to be divided into 10 sections
     * @return An array of 9 values representing the cutoff values for each of the partitions.
     * The array will be in sorted order, and approximately 10% of the data in `scores` will fall between
     * each of the values found in the array.
     */
    // Algorithm from: http://mba-lectures.com/statistics/descriptive-statistics/222/deciles.html
    public static double[] decileValues(Collection<Double> scores) {
        // Make copy of scores before sorting
        List<Double> sortedScores = new ArrayList<>(scores);
        Collections.sort(sortedScores);

        int n = sortedScores.size();
        int limit = Integer.min(n, 10);
        double[] deciles = new double[9];
        for (int i = 1; i < limit; i++) {
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

    /**
     * Calculates a "Score level Matrix" based Theta Cut from page 8 and 9 of http://www.smarterapp.org/documents/TestScoringSpecs2014-2015.pdf
     *
     * The true score estimates are what we expect to receive from the cut off levels based on the population parameter
     * The simulated score levels are what was actually received from the simulation
     * @param estimatedScores the simulated scores from a cat simulation
     * @param trueScores the population scores
     * @param cutoffs an array of the values that make up the partition for the score levels
     * @return A square matrix indexed by [simulated scores][true score estimates], with dimension cutoffs.length + 1
     */
    public static int[][] scoreLevelMatrix(Collection<? extends Score> estimatedScores, Collection<? extends Score> trueScores, double[] cutoffs) {
        int[][] results = new int[cutoffs.length + 1][cutoffs.length + 1];
        Map<String, Integer> studentLevels = scoreLevel(estimatedScores, cutoffs);
        Map<String, Integer> trueLevels = scoreLevel(trueScores, cutoffs);

        for(String sid : studentLevels.keySet()) {
            if(trueLevels.containsKey(sid)) {
                results[studentLevels.get(sid) - 1][trueLevels.get(sid) - 1] += 1;
            } else {
                logger.error("Could not find sid {} in true levels", sid);
            }
        }

        return results;
    }

    /**
     *
     * @param scores A collection of score values
     * @param cutoffs The score level cut off values
     * @return A HashMap of pairs of <student id, score level>
     */
    public static Map<String, Integer> scoreLevel(Collection<? extends Score> scores, double[] cutoffs) {
        Map<String, Integer> scoreLevelMap = new HashMap<>();
        for(Score score : scores) {
            scoreLevelMap.put(score.getSid(), findBinIndex(score.getScore(), cutoffs) + 1);
        }
        return scoreLevelMap;
    }

    /**
     *
     * @param value the value we want to place in a 'bin'
     * @param cutoffValues sorted array with limits
     * @return the correct 'bin' index where the value fits
     */
    private static int findBinIndex(double value, double[] cutoffValues) {
        int n = cutoffValues.length;
        for(int i = 0; i < n; i++) {
            if (i == 0 && value < cutoffValues[0]) {
                return 0;
            } else if (i > 0 && value >= cutoffValues[i - 1] && value < cutoffValues[i]) {
                return i;
            }
        }
        assert (value >= cutoffValues[n - 1]);
        return n;
    }

    /**
     * Partitions scores into decile bin
     * @param scores A Map of <student id, value> that represent a student's score
     * @return A length 10 ArrayList that is partitioned by deciles
     */
    public static List<Map<String, Double>> decilePartition(Map<String, Double> scores) {
        List<Map<String, Double>> results = new ArrayList<>();
        for(int i = 0; i < 10; i++) results.add(new HashMap<String, Double>());

        double[] decileValues = decileValues(scores.values());
        for(String key : scores.keySet()) {
            double value = scores.get(key);
            int decileBin = findBinIndex(value, decileValues);
            results.get(decileBin).put(key, value);
        }

        return results;
    }

    /**
     *
     * @param classAccMatrix A "Score Level Matrix", which can be generated using `scoreLevelMatrix` function
     * @return The percentage (expressed as a decimal) of correctly classified simulated scores vs true scores
     * Correctly classified scores are scores in which the simulated score level is equal to the true score level
     * Simply a sum of the diagonal of the Score Level Matrix
     */
    public static double classificationAccuracy(int[][] classAccMatrix) {
        int x = classAccMatrix.length;
        int y = classAccMatrix[0].length;
        if (x != y) {
            return -1;
        }

        int diagonal = 0;
        int total = 0;
        for(int i = 0; i < x; i++) {
            for(int j = 0; j < y; j++) {
                total += classAccMatrix[i][j];
                if (i == j) {
                    diagonal += classAccMatrix[i][j];
                }
            }

        }
        return diagonal / ((double) total);
    }
    
    /**
    *
    * @param scores simulated student scores
    * @param response Current results of the cat analysis, will be populated with mean SEM values after calling.
    */
   public static void calculateAverageSEM_Math(List<MathStudentScoreCAT> scores, CATAnalysisResponse response) {
       double overallSEM = 0.0d;
       double claim1SEM = 0.0d;
       double claim2_4SEM = 0.0d;
       double claim3SEM = 0.0d;
       for(MathStudentScoreCAT score : scores) {
           overallSEM += score.getOverallSEM();
           claim1SEM += score.getClaim1SEM();
           claim2_4SEM += score.getClaim2_4SEM();
           claim3SEM += score.getClaim3SEM();
       }

       response.setOverallSEM(overallSEM / scores.size());
       response.setClaim1SEM(claim1SEM / scores.size());
       response.setClaim2SEM(claim2_4SEM / scores.size());
       response.setClaim3SEM(claim3SEM / scores.size());
   }

    /**
     *
     * @param scores simulated student scores
     * @param response Current results of the cat analysis, will be populated with mean SEM values after calling.
     */
    public static void calculateAverageSEM_ELA(List<ELAStudentScoreCAT> scores, CATAnalysisResponse response) {
        double overallSEM = 0.0d;
        double claim1SEM = 0.0d;
        double claim2SEM = 0.0d;
        double claim3SEM = 0.0d;
        double claim4SEM = 0.0d;
        for(ELAStudentScoreCAT score : scores) {
            overallSEM += score.getOverallSEM();
            claim1SEM += score.getClaim1SEM();
            claim2SEM += score.getClaim2SEM();
            claim3SEM += score.getClaim3SEM();
            claim4SEM += score.getClaim4SEM();
        }

        response.setOverallSEM(overallSEM / scores.size());
        response.setClaim1SEM(claim1SEM / scores.size());
        response.setClaim2SEM(claim2SEM / scores.size());
        response.setClaim3SEM(claim3SEM / scores.size());
        response.setClaim4SEM(claim4SEM / scores.size());
    }

    public static void calculateAverageSEM(String subject, List<? extends StudentScoreCAT> scores,
            CATAnalysisResponse response) {
        subject = subject.toLowerCase();
        double overallSEM = 0.0d;
        double claim1SEM = 0.0d;
        double claim2SEM = 0.0d;
        double claim3SEM = 0.0d;
        double claim4SEM = 0.0d;
        double claim2_4SEM = 0.0d;
        
        if (scores == null)
            return;

        for(StudentScoreCAT score : scores) {

            overallSEM += score.getOverallSEM();
            claim1SEM += score.getClaim1SEM();
            claim3SEM += score.getClaim3SEM();

            // TODO: refactor
            if (subject.equals("ela")) {
                claim2SEM += ((ELAStudentScoreCAT) score).getClaim2SEM();
                claim4SEM += ((ELAStudentScoreCAT) score).getClaim4SEM();
            } else if (subject.equals("math")) {
                claim2_4SEM += ((MathStudentScoreCAT) score).getClaim2_4SEM();
            }
        }

        response.setOverallSEM(overallSEM / scores.size());
        response.setClaim1SEM(claim1SEM / scores.size());
        response.setClaim2SEM(claim2SEM / scores.size());
        response.setClaim3SEM(claim3SEM / scores.size());
        response.setClaim4SEM(claim4SEM / scores.size());
        response.setClaim2_4SEM(claim2_4SEM / scores.size());
    }
}
