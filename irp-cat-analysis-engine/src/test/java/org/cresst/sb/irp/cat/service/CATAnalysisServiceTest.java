package org.cresst.sb.irp.cat.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.cresst.sb.irp.cat.domain.analysis.BlueprintStatement;
import org.cresst.sb.irp.cat.domain.analysis.CATAnalysisResponse;
import org.cresst.sb.irp.cat.domain.analysis.CATDataModel;
import org.cresst.sb.irp.cat.domain.analysis.ELAStudentScoreCAT;
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
        ELAStudentScoreCAT score = new ELAStudentScoreCAT();
        List<ELAStudentScoreCAT> scores = new ArrayList<>();
        scores.add(score);
        catData.setStudentScores(scores);

        response = catAnalysisService.analyzeCatResults(catData);
        assertNotNull(response);

        List<BlueprintStatement> blueprints = new ArrayList<>();
        catData.setBlueprintStatements(blueprints);
        response = catAnalysisService.analyzeCatResults(catData);
        assertNotNull(response);
    }
}
