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

        // TODO: Add selector for grade and add all grade levels
        // Hardcoded from HS cutoff levels http://www.smarterapp.org/documents/TestScoringSpecs2014-2015.pdf
        double[] cutoffLevels = {-0.177, 0.872, 2.026};
        classificationCalculations(catData, response, cutoffLevels);

        List<BlueprintStatement> blueprintStatements = getGradeClaimBlueprints(11);
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

        //logger.debug("poolItems: {}", poolItems);

        // Get map of all items taken by each student

        Map<Integer, ViolationCount> violationCounts = new HashMap<>();
        Map<String, List<ItemResponseCAT>> studentItems = groupItemsByStudent(catData.getItemResponses());
        // TODO: Need to keep track of all violations
        for(List<ItemResponseCAT> itemResponses : studentItems.values()) {
            Map<Integer, Integer> claimMap = computeClaimNumbers(itemResponses, poolItems);
            logger.debug("claimMap: {}", claimMap);
            if(claimMap.size() == 0) {
                continue;
            }
            //Map<Integer, Violation> violationMap = new HashMap<>();
            for(BlueprintStatement blueprint : blueprintStatements) {
                int claimNumber = blueprint.getClaimNumber();
                int totalClaim = claimMap.get(claimNumber);
                ViolationCount vCount;
                if(violationCounts.containsKey(claimNumber)) {
                    vCount = violationCounts.get(claimNumber);
                } else {
                    vCount = new ViolationCount();
                }

                vCount.setClaim(claimNumber);

                if(totalClaim < blueprint.getMin()) {
                    vCount.incUnder();
                } else if (blueprint.getMin() <= totalClaim && totalClaim <= blueprint.getMax()) {
                    vCount.incMatch();
                } else if (blueprint.getMax() < totalClaim) {
                    vCount.incOver();
                }
                violationCounts.put(claimNumber, vCount);
            }
        }

        response.setClaimViolationsMap(violationCounts);
        response.setClaimViolations(new ArrayList<ViolationCount>(violationCounts.values()));
    }

    /**
     *
     * @param itemResponses is a list of the items we want to count claim numbers for
     * @return A map of <claim-number, count> pairs where the number of items in each claim is counted
     */
    private Map<Integer, Integer> computeClaimNumbers(List<ItemResponseCAT> itemResponses, Map<String, PoolItem> poolItems) {
        Map<Integer, Integer> result = new HashMap<>();
        for(ItemResponseCAT item : itemResponses) {
            int claimNumber = getClaimFromItemId(item.getItemId(), poolItems);
            if (claimNumber == -1) {
                //logger.warn("Item {} not found in item pool", item.getItemId());
                continue;
            }

            if (result.containsKey(claimNumber)) {
                result.put(claimNumber, result.get(claimNumber) + 1);
            } else {
                result.put(claimNumber, 1);
            }
        }
        return result;
    }

    /**
     *
     * @param itemId id we want a claim number for
     * @param poolItems the <ItemId, PoolItem> map for test
     * @return integer claim number for the given item, -1 if not found
     */
    private int getClaimFromItemId(String itemId, Map<String, PoolItem> poolItems) {
        PoolItem strClaim = poolItems.get(itemId);
        if(strClaim != null) {
            return Integer.parseInt(strClaim.getClaim());
        }
        return -1;
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

    private BlueprintStatement getClaimBlueprint(int grade, int claim) {
        if (grade != 11) {
            logger.error("Only grade 11 implemented");
            return null;
        }

        BlueprintStatement blueprint = new BlueprintStatement();
        switch (claim) {
        case 1:
            blueprint.setClaimName("Reading");
            blueprint.setMin(15);
            blueprint.setMax(16);
            blueprint.setCondition(new BlueprintCondition() {
                @Override
                public boolean test(PoolItem item) {
                    return item.getClaim().equals("1");
                }
            });
            break;
        case 2:
            blueprint.setClaimName("Writing");
            blueprint.setMin(10);
            blueprint.setMax(10);
            blueprint.setCondition(new BlueprintCondition() {
                @Override
                public boolean test(PoolItem item) {
                    return item.getClaim().equals("2");
                }
            });
            break;
        case 3:
            blueprint.setClaimName("Speaking/Listening");
            blueprint.setMin(8);
            blueprint.setMax(9);
            blueprint.setCondition(new BlueprintCondition() {
                @Override
                public boolean test(PoolItem item) {
                    return item.getClaim().equals("3");
                }
            });
            break;
        case 4:
            blueprint.setClaimName("Research");
            blueprint.setMin(6);
            blueprint.setMax(6);
            blueprint.setCondition(new BlueprintCondition() {
                @Override
                public boolean test(PoolItem item) {
                    return item.getClaim().equals("4");
                }
            });
            break;
        default:
            break;
        }
        blueprint.setClaimNumber(claim);
        return blueprint;
    }

    private List<BlueprintStatement> getGradeClaimBlueprints(int grade) {
        if (grade != 11) {
            logger.error("Only grade 11 implemented");
            return null;
        }

        int claimTotals = 4;
        List<BlueprintStatement> statements = new ArrayList<>();
        for(int i = 1; i <= claimTotals; i++) {
            statements.add(getClaimBlueprint(grade, i));
        }
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
}
