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

        // TODO: Add selector for grade and add all grade levels
        // Hardcoded from HS cutoff levels http://www.smarterapp.org/documents/TestScoringSpecs2014-2015.pdf
        double[] cutoffLevels = {-0.177, 0.872, 2.026};
        classificationCalculations(catData, response, cutoffLevels);

        return response;
    }

    private boolean biasCalculations(CATDataModel catData, CATAnalysisResponse response) {
        List<TrueTheta> trueScores = catData.getTrueThetas();
        List<StudentScoreCAT> studentScores = catData.getStudentScores();

        Map<String, Double> trueScoreMap = scoresToMap(trueScores);
        Map<String, Double> studentScoreMap = scoresToMap(studentScores);

        response.setAverageBias(Stats.averageBias(trueScoreMap, studentScoreMap));
        response.setRmse(Stats.rmse(trueScoreMap, studentScoreMap));

        List<Map<String, Double>> studentDecilePartitions = Stats.decilePartition(studentScoreMap);
        double[] decileBias = new double[10];
        double[] decileRmse = new double[10];
        int i = 0;
        for(Map<String, Double> studentPart : studentDecilePartitions) {
            decileBias[i] = Stats.averageBias(trueScoreMap, studentPart);
            decileRmse[i] = Stats.rmse(trueScoreMap, studentPart);
            i += 1;
        }
        response.setDecileAverageBias(decileBias);
        response.setDecileRmse(decileRmse);

        return true;
    }

    private Map<String, Double> scoresToMap(List<? extends Score> scores) {
        Map<String, Double> results = new HashMap<>();
        for(Score score : scores) {
            results.put(score.getSid(), score.getScore());
        }
        return results;
    }

    private boolean exposureCalculations(CATDataModel catData, CATAnalysisResponse response, double binSize) {
        response.setExposureRates(Stats.calculateExposureRates(catData));

        Stats.calculateExposureBins(response, binSize);
        return true;
    }

    private void classificationCalculations(CATDataModel catData, CATAnalysisResponse response, double[] cutoffLevels) {
        int[][] classAccMatrix = Stats.scoreLevelMatrix(catData.getStudentScores(), catData.getTrueThetas(), cutoffLevels);
        double classAccuracy = Stats.classificationAccuracy(classAccMatrix);
        response.setClassAccMatrix(classAccMatrix);
        response.setClassAccuracy(classAccuracy);
    }
}
