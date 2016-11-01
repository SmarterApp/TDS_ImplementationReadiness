package org.cresst.sb.irp.cat.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cresst.sb.irp.cat.domain.analysis.CATAnalysisResponse;
import org.cresst.sb.irp.cat.domain.analysis.CATDataModel;
import org.cresst.sb.irp.cat.domain.analysis.ItemResponseCAT;
import org.cresst.sb.irp.cat.domain.analysis.PoolItemELA;
import org.cresst.sb.irp.cat.domain.analysis.PoolItemMath;
import org.cresst.sb.irp.cat.domain.analysis.Score;
import org.cresst.sb.irp.cat.domain.analysis.StudentScoreCAT;
import org.cresst.sb.irp.cat.domain.analysis.TrueTheta;
import org.cresst.sb.irp.cat.service.lib.Stats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CATAnalysisServiceImpl implements CATAnalysisService {
    private final static Logger logger = LoggerFactory.getLogger(CATAnalysisServiceImpl.class);

    @Override
    public CATAnalysisResponse analyzeCatResults(CATDataModel catData) {
        CATAnalysisResponse response = new CATAnalysisResponse();

        // % increase for bins; hard-coded for 5%
        double binSize = .05;
        if (exposureCalculations(catData, response, binSize)) {
            logger.debug("Successfully did exposure rate calculations");
        } else {
            logger.error("Failed to do exposure rate calculations");
        }

        if (biasCalculations(catData, response)) {
            logger.debug("Successfully did bias calculations.");
        } else {
            logger.error("Failed to do bias calculations");
        }

        return response;
    }

    private boolean biasCalculations(CATDataModel catData, CATAnalysisResponse response) {
        List<TrueTheta> trueScores = catData.getTrueThetas();
        List<StudentScoreCAT> studentScores = catData.getStudentScores();

        Map<String, Double> trueScoreMap = scoresToMap(trueScores);
        Map<String, Double> studentScoreMap = scoresToMap(studentScores);

        response.setAverageBias(Stats.averageBias(trueScoreMap, studentScoreMap));
        response.setRmse(Stats.rmse(trueScoreMap, studentScoreMap));
        return true;
    }

    private Map<String, Double> scoresToMap(List<? extends Score> scores) {
        Map<String, Double> results = new HashMap<>();
        for(Score score : scores) {
            results.put(score.getSid(), score.getScore());
        }
        return results;
    }

    private int binIndex(double value, double stepSize) {
        return (int) Math.floor(value / stepSize);
    }

    private boolean exposureCalculations(CATDataModel catData, CATAnalysisResponse response, double binSize) {
        response.setExposureRates(Stats.calculateExposureRates(catData));

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
