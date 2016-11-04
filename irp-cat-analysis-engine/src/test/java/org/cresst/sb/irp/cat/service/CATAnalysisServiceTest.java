package org.cresst.sb.irp.cat.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.cresst.sb.irp.cat.AppConfig;
import org.cresst.sb.irp.cat.domain.analysis.BlueprintStatement;
import org.cresst.sb.irp.cat.domain.analysis.CATAnalysisResponse;
import org.cresst.sb.irp.cat.domain.analysis.CATDataModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class CATAnalysisServiceTest {
    private CATAnalysisResponse response;
    private CATDataModel catData;
    private List<BlueprintStatement> blueprintStatements = new ArrayList<>();

    @Autowired
    private CATAnalysisService catAnalysisService;

    @Before
    public void setUp() throws Exception {
        response = new CATAnalysisResponse();
        catData = new CATDataModel();
    }

    @Test
    public void test_calculateBlueprintViolations() {
        catAnalysisService.calculateBlueprintViolations(catData, response, blueprintStatements);
        assertNotNull(response);
    }
}
