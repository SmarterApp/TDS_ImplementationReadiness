package org.cresst.sb.irp.cat.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.cresst.sb.irp.cat.domain.analysis.BlueprintCondition;
import org.cresst.sb.irp.cat.domain.analysis.BlueprintStatement;
import org.cresst.sb.irp.cat.domain.analysis.CATAnalysisResponse;
import org.cresst.sb.irp.cat.domain.analysis.CATDataModel;
import org.cresst.sb.irp.cat.domain.analysis.ELAStudentScoreCAT;
import org.cresst.sb.irp.cat.domain.analysis.ExposureRate;
import org.cresst.sb.irp.cat.domain.analysis.ItemResponseCAT;
import org.cresst.sb.irp.cat.domain.analysis.PoolItem;
import org.cresst.sb.irp.cat.domain.analysis.PoolItemELA;
import org.cresst.sb.irp.cat.domain.analysis.TrueTheta;
import org.junit.Before;
import org.junit.Test;

public class CATAnalysisServiceTest {
    private static final double DELTA = 1e-15;
    private CATDataModel catData;
    private CATAnalysisService catAnalysisService;

    @Before
    public void setUp() throws Exception {
        catData = new CATDataModel();
        catAnalysisService = new CATAnalysisServiceImpl();
    }

    @Test
    public void test_analyzeCatResults() throws IOException {
        CATAnalysisResponse response = catAnalysisService.analyzeCatResults(catData);
        assertNull(response);

        catData.setGrade(11);
        catData.setSubject("ela");

        response = catAnalysisService.analyzeCatResults(catData);
        assertNotNull(response);

        List<BlueprintStatement> blueprints = new ArrayList<>();
        catData.setBlueprintStatements(blueprints);
        response = catAnalysisService.analyzeCatResults(catData);
        assertNotNull(response);

        BlueprintStatement blueprint = new BlueprintStatement();
        blueprint.setGrade(11);
        blueprint.setSubject("ela");
        blueprint.setMin(1);
        blueprint.setMax(1);
        blueprint.setCondition(new BlueprintCondition() {

            @Override
            public boolean test(PoolItem item) {
                return item.getItemGrade().equals("11") && item.getPoolGrade().equals("11");
            }
        });

        blueprints.add(blueprint);

        List<TrueTheta> trueThetas = new ArrayList<>();
        TrueTheta theta = new TrueTheta();
        theta.setScore(1.0);
        theta.setSid("1");
        trueThetas.add(theta);
        catData.setTrueThetas(trueThetas);

        List<ItemResponseCAT> itemResponses = new ArrayList<>();
        ItemResponseCAT resp = new ItemResponseCAT();
        resp.setsId("1");
        resp.setItemId("1");
        resp.setScore(1);
        itemResponses.add(resp);
        catData.setItemResponses(itemResponses);

        List<ELAStudentScoreCAT> scores = new ArrayList<>();
        ELAStudentScoreCAT score = new ELAStudentScoreCAT();
        score.setOverallScore(5.0);
        score.setOverallSEM(0.5);
        score.setClaim1Score(1.0);
        score.setClaim1SEM(0.1);
        score.setClaim2Score(2.0);
        score.setClaim2SEM(0.2);
        score.setClaim3Score(3.0);
        score.setClaim3SEM(0.3);
        score.setClaim4Score(4.0);
        score.setClaim4SEM(0.4);
        score.setSid("1");
        scores.add(score);
        catData.setStudentScores(scores);

        List<PoolItemELA> poolItems = new ArrayList<>();
        PoolItemELA poolItem = new PoolItemELA();
        poolItem.setItemId("1");
        poolItem.setItemGrade("11");
        poolItem.setPoolGrade("11");
        poolItems.add(poolItem);
        catData.setPoolItems(poolItems);
        response = catAnalysisService.analyzeCatResults(catData);
        System.out.println(response);
        assertNotNull(response);

        ExposureRate respExposure1 = response.getExposureRates().get("1"); 
        assertEquals(1.0, respExposure1.getExposureRate(), DELTA);
        assertEquals(0.1, response.getClaim1SEM(), DELTA);
        assertEquals(0.2, response.getClaim2SEM(), DELTA);
        assertEquals(0.3, response.getClaim3SEM(), DELTA);
        assertEquals(0.4, response.getClaim4SEM(), DELTA);
        assertEquals(4.0, Math.abs(response.getAverageBias()), DELTA);
    }
}
