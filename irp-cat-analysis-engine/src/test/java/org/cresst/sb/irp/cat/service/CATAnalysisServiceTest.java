package org.cresst.sb.irp.cat.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.cresst.sb.irp.cat.domain.analysis.BlueprintStatement;
import org.cresst.sb.irp.cat.domain.analysis.CATAnalysisResponse;
import org.cresst.sb.irp.cat.domain.analysis.CATDataModel;
import org.cresst.sb.irp.cat.domain.analysis.ItemResponseCAT;
import org.cresst.sb.irp.cat.domain.analysis.PoolItem;
import org.cresst.sb.irp.cat.domain.analysis.PoolItemELA;
import org.cresst.sb.irp.cat.domain.analysis.ViolationCount;
import org.junit.Before;
import org.junit.Test;

public class CATAnalysisServiceTest {
    private CATAnalysisResponse response;
    private CATDataModel catData;
    private BlueprintStatement blueState;
    private List<BlueprintStatement> blueprintStatements;
    private List<PoolItem> poolItems;
    private List<ItemResponseCAT> itemResponses;
    private CATAnalysisService catAnalysisService;

    @Before
    public void setUp() throws Exception {
        response = new CATAnalysisResponse();
        catData = new CATDataModel();
        blueprintStatements = new ArrayList<>();
        poolItems = new ArrayList<>();
        itemResponses = new ArrayList<>();
        blueState = new BlueprintStatement();
        catAnalysisService = new CATAnalysisServiceImpl();
    }

    @Test
    public void test_calculateBlueprintViolations() {
        catAnalysisService.calculateBlueprintViolations(catData, response, blueprintStatements);
        assertNotNull(response);

        blueState.setClaimName("Testing");
        blueState.setClaimNumber(1);
        blueState.setMin(1);
        blueState.setMax(1);

        blueprintStatements.add(blueState);

        PoolItemELA item = new PoolItemELA();
        item.setClaim("1");
        item.setItemId("1");
        poolItems.add(item);

        itemResponses.add(new ItemResponseCAT("1", "1", 1));

        catData.setPoolItems(poolItems);
        catData.setItemResponses(itemResponses);
        catAnalysisService.calculateBlueprintViolations(catData, response, blueprintStatements);

        ViolationCount claim1Violations = response.getClaimViolations().get(1);

        assertEquals(0, claim1Violations.getUnder());
        assertEquals(1, claim1Violations.getMatch());
        assertEquals(0, claim1Violations.getOver());
    }
}
