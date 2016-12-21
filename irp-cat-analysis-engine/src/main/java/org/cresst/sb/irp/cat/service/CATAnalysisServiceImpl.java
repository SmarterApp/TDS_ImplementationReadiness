package org.cresst.sb.irp.cat.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cresst.sb.irp.cat.analysis.engine.CATParsingServiceImpl;
import org.cresst.sb.irp.cat.domain.analysis.BlueprintStatement;
import org.cresst.sb.irp.cat.domain.analysis.CATAnalysisResponse;
import org.cresst.sb.irp.cat.domain.analysis.CATDataModel;
import org.cresst.sb.irp.cat.domain.analysis.ItemResponseCAT;
import org.cresst.sb.irp.cat.domain.analysis.PoolItem;
import org.cresst.sb.irp.cat.domain.analysis.Score;
import org.cresst.sb.irp.cat.domain.analysis.StudentScoreCAT;
import org.cresst.sb.irp.cat.domain.analysis.ThresholdLevels;
import org.cresst.sb.irp.cat.domain.analysis.TrueTheta;
import org.cresst.sb.irp.cat.service.lib.Stats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CATAnalysisServiceImpl implements CATAnalysisService {
    private final static Logger logger = LoggerFactory.getLogger(CATAnalysisServiceImpl.class);

    @Override
    public CATAnalysisResponse analyzeCatResults(CATDataModel catData) throws IOException {
        CATAnalysisResponse response = new CATAnalysisResponse();
        
        response.setGrade(catData.getGrade());
        String subject = catData.getSubject();
        // Format the subject nicely
        if (subject.equalsIgnoreCase("ela")) {
            response.setSubject("ELA");
        } else if (subject.equalsIgnoreCase("math")) {
            response.setSubject("Math");
        } else {
            response.setSubject(subject);
        }

        // % increase for bins; hard-coded for 10%
        double binSize = .10;
        exposureCalculations(catData, response, binSize, String.valueOf(catData.getGrade()));

        biasCalculations(catData, response);

        double[] cutoffLevels = getThetaCutoffLevels(catData.getSubject(), catData.getGrade());

        response.setCutoffLevels(cutoffLevels);
        classificationCalculations(catData, response, cutoffLevels);

        precisionStats(catData, response);

        calculateBlueprintViolations(catData, response, catData.getBlueprintStatements());

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
    // Data can be changed in irp-cat-analysis-engine's resource folder: ThresholdScores2014-2015.csv
    private double[] getThetaCutoffLevels(String subject, int grade) throws IOException {
        // Change this file if thresholds scores change, found in irp-cat-analysis-engine resources 
        InputStream thresholdLevelStream = ThresholdLevels.class.getClassLoader().getResourceAsStream("ThresholdScores2014-2015.csv");
        // Parse the csv file
        List<ThresholdLevels> thresholdLevels = CATParsingServiceImpl.parseCsv(thresholdLevelStream, ThresholdLevels.class);
        String strGrade = String.valueOf(grade);
        
        for (ThresholdLevels levels : thresholdLevels) {
            String lvlSubject = levels.getSubject().toLowerCase();
            String lvlGrade = levels.getGrade().toLowerCase();
            if (lvlSubject.equals(subject) &&
                    // Grades with numbers just need to be compared directly
                    (lvlGrade.equals(strGrade) || 
                            // Grade can be expressed as HS for high school, grade must fall between 9th and 12th grade
                            (lvlGrade.equals("hs") && grade >= 9 && grade <= 12))) {
                // Return the 3 theta levels as a double for the given grade/subject
                return new double[]{levels.getTheta_1_2(), levels.getTheta_2_3(), levels.getTheta_3_4()};
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
        List<? extends StudentScoreCAT> studentScores = catData.getStudentScores();

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

    private void exposureCalculations(CATDataModel catData, CATAnalysisResponse response, double binSize,
            String grade) {
        response.setExposureRates(
                Stats.calculateExposureRates(catData.getPoolItems(), catData.getItemResponses(), grade));

        Stats.calculateExposureBins(response, binSize);
    }

    private void classificationCalculations(CATDataModel catData, CATAnalysisResponse response, double[] cutoffLevels) {
        int[][] classAccMatrix = Stats.scoreLevelMatrix(catData.getStudentScores(), catData.getTrueThetas(), cutoffLevels);
        double classAccuracy = Stats.classificationAccuracy(classAccMatrix);
        response.setClassAccMatrix(classAccMatrix);
        response.setClassAccuracy(classAccuracy);
    }

    private void precisionStats(CATDataModel catData, CATAnalysisResponse response) {
        Stats.calculateAverageSEM(catData.getSubject(), catData.getStudentScores(), response);
    }
}
