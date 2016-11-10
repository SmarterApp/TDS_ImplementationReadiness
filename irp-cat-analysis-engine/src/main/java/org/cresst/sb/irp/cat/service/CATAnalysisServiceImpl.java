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

        double[] cutoffLevels = getThetaCutoffLevels(3);
        classificationCalculations(catData, response, cutoffLevels);

        precisionStats(catData, response);
        List<BlueprintStatement> blueprintStatements = getGradeBlueprints(3);
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
    private double[] getThetaCutoffLevels(int grade) {
        if (grade == 11) {
            return new double[]{-0.177, 0.872, 2.026};
        } else if (grade == 3) {
            return new double[]{-1.646, -0.888, -0.212};
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

    private List<BlueprintStatement> getGradeBlueprints(int grade) {
        if (grade != 11 && grade != 3) {
            logger.error("Only grade 11 implemented");
            return null;
        }

        List<BlueprintStatement> statements = new ArrayList<>();

        BlueprintStatement blueprint = new BlueprintStatement();
        blueprint.setSpecification("Claim 1: Reading");
        if (grade == 11) {
            blueprint.setMin(15);
        } else if (grade == 3) {
            blueprint.setMin(14);
        }
        blueprint.setMax(16);
        blueprint.setCondition(new BlueprintCondition() {
            @Override
            public boolean test(PoolItem item) {
                return item.getClaim().equals("1");
            }
        });
        statements.add(blueprint);

        blueprint = new BlueprintStatement();
        blueprint.setSpecification("Claim 1, DOK=2");
        blueprint.setMin(7);
        blueprint.setMax(Integer.MAX_VALUE);
        blueprint.setCondition(new BlueprintCondition() {
            @Override
            public boolean test(PoolItem item) {
                return item.getClaim().equals("1") && item.getDok().equals("2");
            }
        });
        statements.add(blueprint);

        blueprint = new BlueprintStatement();
        blueprint.setSpecification("Claim 1, DOK>=3");
        blueprint.setMin(2);
        blueprint.setMax(Integer.MAX_VALUE);
        blueprint.setCondition(new BlueprintCondition() {
            @Override
            public boolean test(PoolItem item) {
                int dok = Integer.parseInt(item.getDok());
                return item.getClaim().equals("1") && dok >= 3;
            }
        });
        statements.add(blueprint);

        blueprint = new BlueprintStatement();
        blueprint.setSpecification("Claim 1, Literary");
        blueprint.setMin(0);
        blueprint.setMax(8);
        blueprint.setCondition(new BlueprintCondition() {
            @Override
            public boolean test(PoolItem item) {
                String target = item.getTarget();
                return (item.getClaim().equals("1") && target.matches("[1234567]"));

            }
        });
        statements.add(blueprint);

        blueprint = new BlueprintStatement();
        blueprint.setSpecification("Claim 2: Writing");
        blueprint.setMin(10);
        blueprint.setMax(10);
        blueprint.setCondition(new BlueprintCondition() {
            @Override
            public boolean test(PoolItem item) {
                return item.getClaim().equals("2");
            }
        });
        statements.add(blueprint);

        blueprint = new BlueprintStatement();
        blueprint.setSpecification("Claim 3: Speaking/Listening");
        blueprint.setMin(8);
        blueprint.setMax(9);
        blueprint.setCondition(new BlueprintCondition() {
            @Override
            public boolean test(PoolItem item) {
                return item.getClaim().equals("3");
            }
        });
        statements.add(blueprint);

        blueprint = new BlueprintStatement();
        blueprint.setSpecification("Claim 4: Research");
        blueprint.setMin(6);
        blueprint.setMax(6);
        blueprint.setCondition(new BlueprintCondition() {
            @Override
            public boolean test(PoolItem item) {
                return item.getClaim().equals("4");
            }
        });
        statements.add(blueprint);

        return statements;
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

    private void precisionStats(CATDataModel catData, CATAnalysisResponse response) {
        double overallSEM = 0.0d;
        double claim1SEM = 0.0d;
        double claim2SEM = 0.0d;
        double claim3SEM = 0.0d;
        double claim4SEM = 0.0d;
        List<StudentScoreCAT> scores = catData.getStudentScores();
        for(StudentScoreCAT score : scores) {
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
}
