package org.cresst.sb.irp.cat.service;

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
import org.cresst.sb.irp.cat.domain.analysis.ItemResponseCAT;
import org.cresst.sb.irp.cat.domain.analysis.PoolItem;
import org.cresst.sb.irp.cat.domain.analysis.TrueTheta;
import org.junit.Before;
import org.junit.Test;

public class CATAnalysisServiceTest {

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
        catData.setTrueThetas(trueThetas);

        List<ItemResponseCAT> itemResponses = new ArrayList<>();
        ItemResponseCAT resp = new ItemResponseCAT();
        resp.setsId("1");
        resp.setItemId("1");
        resp.setScore(1);
        catData.setItemResponses(itemResponses);

        List<ELAStudentScoreCAT> scores = new ArrayList<>();
        ELAStudentScoreCAT score = new ELAStudentScoreCAT();
        score.setSid("1");
        scores.add(score);
        catData.setStudentScores(scores);

        response = catAnalysisService.analyzeCatResults(catData);
        assertNotNull(response);
    }
}
