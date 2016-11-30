package org.cresst.sb.irp.cat.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cresst.sb.irp.cat.domain.analysis.BlueprintCondition;
import org.cresst.sb.irp.cat.domain.analysis.BlueprintStatement;
import org.cresst.sb.irp.cat.domain.analysis.CATAnalysisResponse;
import org.cresst.sb.irp.cat.domain.analysis.CATDataModel;
import org.cresst.sb.irp.cat.domain.analysis.ItemResponseCAT;
import org.cresst.sb.irp.cat.domain.analysis.PoolItem;
import org.cresst.sb.irp.cat.domain.analysis.Score;
import org.cresst.sb.irp.cat.domain.analysis.StudentScoreCAT;
import org.cresst.sb.irp.cat.domain.analysis.TrueTheta;
import org.cresst.sb.irp.cat.domain.analysis.ViolationCount;
import org.cresst.sb.irp.cat.service.lib.BlueprintSpecs;
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
        
        response.setGrade(catData.getGrade());
        response.setSubject(catData.getSubject());

        // % increase for bins; hard-coded for 5%
        double binSize = .05;
        exposureCalculations(catData, response, binSize);

        biasCalculations(catData, response);

        double[] cutoffLevels = getThetaCutoffLevels(catData.getSubject(), catData.getGrade());
        classificationCalculations(catData, response, cutoffLevels);

        precisionStats(catData, response);
        List<BlueprintStatement> blueprintStatements = BlueprintSpecs.getGradeBlueprints(catData.getSubject(), catData.getGrade());
        calculateBlueprintViolations(catData, response, blueprintStatements);

        return response;
    }

    @Override
    public void calculateBlueprintViolations(CATDataModel catData, CATAnalysisResponse response,
            List<BlueprintStatement> blueprintStatements) {
        response.setBlueprintStatements(blueprintStatements);
        if (catData == null) {
            return;
        }

        if (catData.getPoolItems() == null) {
            return;
        }
        
        if (blueprintStatements == null || blueprintStatements.size() == 0) {
            return;
        }
        
        // Make a pool item map by item id
        Map<String, PoolItem> poolItems = new HashMap<>();
        for(PoolItem item : catData.getPoolItems()) {
            poolItems.put(item.getItemId(), item);
        }

        // Get map of all items taken by each student
        Map<String, List<ItemResponseCAT>> studentItems = groupItemsByStudent(catData.getItemResponses());
        for(List<ItemResponseCAT> itemResponses : studentItems.values()) {

            // Check item against all blueprint statement, increment counter for each statement
            for(BlueprintStatement blueprint : blueprintStatements) {
                for(ItemResponseCAT item : itemResponses) {
                    PoolItem poolItem = poolItems.get(item.getItemId());

                    if(blueprint.test(poolItem)) {
                        blueprint.incMatch();
                    }
                }
                blueprint.updateViolations();
            }
        }
    }

    // Returns theta score cutoff levels from
    // http://www.smarterapp.org/documents/TestScoringSpecs2014-2015.pdf
    // TODO: Could abstract out into own csv file so smarterbalanced could change without changing code
    private double[] getThetaCutoffLevels(String subject, int grade) {
        if (subject.toLowerCase().equals("ela")) {
            switch (grade) {
            case 3:
                return new double[]{-1.646, -0.888, -0.212};
            case 4:
                return new double[]{-1.075, -0.410, 0.289};
            case 5:
                return new double[]{-0.772, -0.072, 0.860};
            case 6:
                return new double[]{-0.597, 0.266, 1.280};
            case 7:
                return new double[]{-0.340, 0.510, 1.641};
            case 8:
                return new double[]{-0.247, 0.685, 1.862};
            case 9:
            case 10:
            case 11:
            case 12:
                return new double[]{-0.177, 0.872, 2.026};
            default:
                return null;
            }
        } else if (subject.toLowerCase().equals("math")) {
            switch (grade) {
            case 3:
                return new double[]{-1.689, -0.995, -0.175};
            case 4:
                return new double[]{-1.310, -0.377, 0.430};
            case 5:
                return new double[]{-0.755, 0.165, 0.808};
            case 6:
                return new double[]{-0.528, 0.468, 1.199};
            case 7:
                return new double[]{-0.390, 0.657, 1.515};
            case 8:
                return new double[]{-0.137, 0.897, 1.741 };
            case 9:
            case 10:
            case 11:
            case 12:
                return new double[]{0.354, 1.426, 2.561};
            default:
                return null;
            }            
        }

        return null;
    }

    private Map<String, List<ItemResponseCAT>> groupItemsByStudent(List<ItemResponseCAT> itemResponses) {
        Map<String, List<ItemResponseCAT>> results = new HashMap<>();
        for(ItemResponseCAT resp : itemResponses) {
            String sid = resp.getsId();
            if(results.containsKey(sid)) {
                results.get(sid).add(resp);
            } else {
                List<ItemResponseCAT> newList = new ArrayList<>();
                newList.add(resp);
                results.put(sid, newList);
            }
        }
        return results;
    }

    private void biasCalculations(CATDataModel catData, CATAnalysisResponse response) {
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
    }

    private Map<String, Double> scoresToMap(List<? extends Score> scores) {
        Map<String, Double> results = new HashMap<>();
        for(Score score : scores) {
            results.put(score.getSid(), score.getScore());
        }
        return results;
    }

    private void exposureCalculations(CATDataModel catData, CATAnalysisResponse response, double binSize) {
        response.setExposureRates(Stats.calculateExposureRates(catData.getPoolItems(), catData.getItemResponses()));

        Stats.calculateExposureBins(response, binSize);
    }

    private void classificationCalculations(CATDataModel catData, CATAnalysisResponse response, double[] cutoffLevels) {
        int[][] classAccMatrix = Stats.scoreLevelMatrix(catData.getStudentScores(), catData.getTrueThetas(), cutoffLevels);
        double classAccuracy = Stats.classificationAccuracy(classAccMatrix);
        response.setClassAccMatrix(classAccMatrix);
        response.setClassAccuracy(classAccuracy);
    }

    private void precisionStats(CATDataModel catData, CATAnalysisResponse response) {
        Stats.calculateAverageSEM(catData.getStudentScores(), response);
    }
}
