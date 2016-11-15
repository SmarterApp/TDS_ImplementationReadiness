package org.cresst.sb.irp.cat.service.lib;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cresst.sb.irp.cat.domain.analysis.CATAnalysisResponse;
import org.cresst.sb.irp.cat.domain.analysis.CATDataModel;
import org.cresst.sb.irp.cat.domain.analysis.ExposureRate;
import org.cresst.sb.irp.cat.domain.analysis.ItemResponseCAT;
import org.cresst.sb.irp.cat.domain.analysis.PoolItem;
import org.cresst.sb.irp.cat.domain.analysis.PoolItemELA;
import org.cresst.sb.irp.cat.domain.analysis.StudentScoreCAT;
import org.junit.Before;
import org.junit.Test;

public class StatsTest {
    private final double epsilon = .000001;
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void test_bias_values_equals_parameter() {
        Map<String, Double> values = new HashMap<>();
        values.put("1", 1.0);
        assertEquals(0.0, Stats.averageBias(values, values), epsilon);
    }

    @Test
    public void test_bias_values_different() {
        Map<String, Double> parameters = new HashMap<>();
        Map<String, Double> values = new HashMap<>();

        parameters.put("1", 1.0);
        values.put("1", 0.5);
        assertEquals(0.5, Stats.averageBias(parameters, values), epsilon);

        parameters.put("2", 2.0);
        values.put("2", 1.0);
        assertEquals(0.75, Stats.averageBias(parameters, values), epsilon);

        parameters.put("3", 1.0);
        values.put("3", 0.5);
        assertEquals(2/3.0, Stats.averageBias(parameters, values), epsilon);

        parameters.put("4", 3.0);
        values.put("4", 3.0);
        assertEquals(0.5, Stats.averageBias(parameters, values), epsilon);
    }

    @Test
    public void test_mse_values_equals_parameters() {
        Map<String, Double> values = new HashMap<>();
        values.put("1", 1.0);
        assertEquals(0.0, Stats.meanSquaredError(values, values), epsilon);
    }

    @Test
    public void test_mse_values_different() {
        Map<String, Double> parameters = new HashMap<>();
        Map<String, Double> values = new HashMap<>();

        double expected = 0.25;
        parameters.put("1", 1.0);
        values.put("1", 0.5);
        assertEquals(expected, Stats.meanSquaredError(parameters, values), epsilon);

        expected = 0.25;
        parameters.put("2", 2.0);
        values.put("2", 2.5);
        assertEquals(expected, Stats.meanSquaredError(parameters, values), epsilon);

        expected = 0.5;
        parameters.put("3", 1.0);
        values.put("3", 2.0);
        assertEquals(expected,  Stats.meanSquaredError(parameters, values), epsilon);

        expected = 5.5/4;
        parameters.put("4", 2.0);
        values.put("4", 0.0);
        assertEquals(expected,  Stats.meanSquaredError(parameters, values), epsilon);
    }

    @Test
    public void test_exposure_rates_no_items() {
        CATDataModel catData = new CATDataModel();
        List<ItemResponseCAT> itemResponses = new ArrayList<>();
        List<PoolItem> poolItems = new ArrayList<>();
        catData.setItemResponses(itemResponses);
        catData.setPoolItems(poolItems);
        Map<String, ExposureRate> exposureRates = Stats.calculateExposureRates(catData.getPoolItems(), catData.getItemResponses());
        assertNotNull(exposureRates);
        assertEquals(0, exposureRates.size());
    }

    @Test
    public void test_exposure_rates_one_item() {
        CATDataModel catData = new CATDataModel();
        List<ItemResponseCAT> itemResponses = new ArrayList<>();
        List<PoolItem> poolItems = new ArrayList<>();

        // Add student responses
        ItemResponseCAT item = new ItemResponseCAT();
        item.setItemId("1");
        item.setsId("1");
        itemResponses.add(item);
        catData.setItemResponses(itemResponses);

        // Add pool items
        PoolItemELA poolItem = new PoolItemELA();
        poolItem.setItemId("1");
        poolItems.add(poolItem);
        catData.setPoolItems(poolItems);

        Map<String, ExposureRate> exposureRates = Stats.calculateExposureRates(catData.getPoolItems(), catData.getItemResponses());
        assertNotNull(exposureRates);
        assertEquals(1, exposureRates.size());
        assertEquals(1.0, exposureRates.get("1").getExposureRate(), epsilon);
    }

    @Test
    public void test_decileValues() {
        List<Double> scores = new ArrayList<>();
        for(int i = 0; i <= 100; i++) {
            scores.add((double) i);
        }
        double[] deciles = Stats.decileValues(scores);
        assertNotNull(deciles);
        assertEquals(9, deciles.length);

        // Test case from: http://mba-lectures.com/statistics/descriptive-statistics/222/deciles.html
        List<Double> scores2 = new ArrayList<>();
        double[] scoresArr = {500, 850, 925, 800, 600, 750, 650, 625, 800, 400, 725, 550};
        for(int i = 0; i < scoresArr.length; i++) {
            scores2.add(scoresArr[i]);
        }
        double[] deciles2 = Stats.decileValues(scores2);
        assertNotNull(deciles2);
        assertEquals(9, deciles2.length);
        assertEquals(630, deciles2[3], epsilon);
        assertEquals(800, deciles2[6], epsilon);
        assertEquals(902.5, deciles2[8], epsilon);
    }

    @Test
    public void test_decilePartition() {
        Map<String, Double> scores = new HashMap<>();
        // Put 20 scores, should result in 2 in each decile bin
        for(int i = 0; i < 20; i++) {
            scores.put(String.valueOf(i), (double) i);
        }
        List<Map<String, Double>> decilePartitions = Stats.decilePartition(scores);
        assertNotNull(decilePartitions);
        assertEquals(10, decilePartitions.size());
        for(Map<String, Double> map : decilePartitions) {
            assertEquals(2, map.size());
        }
    }

    @Test
    public void test_classificationAccuracy() {
        int[][] classMatrix = {{1,0},{0,1}};
        assertEquals(1.0, Stats.classificationAccuracy(classMatrix), epsilon);

        int [][] classMatrix2 = {{5,5},{5,5}};
        assertEquals(0.5, Stats.classificationAccuracy(classMatrix2), epsilon);
    }

    @Test
    public void test_scoreLevel() {
        double[] cutoffs = {1.0};
        List<StudentScoreCAT> scores = new ArrayList<>();
        StudentScoreCAT score1 = new StudentScoreCAT();
        score1.setSid("1");
        score1.setOverallScore(.5);
        scores.add(score1);
        StudentScoreCAT score2 = new StudentScoreCAT();
        score2.setSid("2");
        score2.setOverallScore(1.5);
        scores.add(score2);
        Map<String, Integer> scoreLevels = Stats.scoreLevel(scores, cutoffs);
        assertNotNull(scoreLevels);
        assertEquals(1, (int) scoreLevels.get("1"));
        assertEquals(2, (int) scoreLevels.get("2"));

        // Score levels should no higher than one more the number of cutoffs
        Set<Integer> scoreLevelSet = new HashSet<>(scoreLevels.values());
        assertTrue(cutoffs.length + 1 <= scoreLevelSet.size());
    }
}
