package org.cresst.sb.irp.cat.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.cresst.sb.irp.cat.domain.analysis.BlueprintCondition;
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
        catAnalysisService = new CATAnalysisServiceImpl();
    }

    @Test
    public void test_calculateBlueprintViolations() {
        catAnalysisService.calculateBlueprintViolations(catData, response, blueprintStatements);
        assertNotNull(response);

        blueprintStatements.add(new BlueprintStatement("Unit Testing", 1, 1, 1, new BlueprintCondition() {

            @Override
            public boolean test(PoolItem item) {
                return item.getClaim().equals("1");
            }
        }));

        poolItems.add(createSimplePoolItem("1", "1"));

        itemResponses.add(new ItemResponseCAT("1", "1", 1));

        catData.setPoolItems(poolItems);
        catData.setItemResponses(itemResponses);
        catAnalysisService.calculateBlueprintViolations(catData, response, blueprintStatements);

        ViolationCount claim1Violations = response.getClaimViolationsMap().get(1);

        assertEquals(0, claim1Violations.getUnder());
        assertEquals(1, claim1Violations.getMatch());
        assertEquals(0, claim1Violations.getOver());

        poolItems.add(createSimplePoolItem("2", "2"));
        poolItems.add(createSimplePoolItem("2", "3"));

        // Claim 2
        // min 2, max 2
        blueprintStatements.add(new BlueprintStatement("Spring", 2, 2, 2, new BlueprintCondition() {

            @Override
            public boolean test(PoolItem item) {
                return item.getClaim().equals("2");
            }
        }));

        itemResponses.add(new ItemResponseCAT("1", "2", 1));
        catAnalysisService.calculateBlueprintViolations(catData, response, blueprintStatements);

        ViolationCount claim2Violations = response.getClaimViolationsMap().get(2);
        claim1Violations = response.getClaimViolationsMap().get(1);

        // Should still be the same
        assertEquals(0, claim1Violations.getUnder());
        assertEquals(1, claim1Violations.getMatch());
        assertEquals(0, claim1Violations.getOver());

        // Don't have enough items in claim 2
        assertEquals(1, claim2Violations.getUnder());
        assertEquals(0, claim2Violations.getMatch());
        assertEquals(0, claim2Violations.getOver());
    }

    private PoolItem createSimplePoolItem(String claim, String itemId) {
        PoolItemELA item = new PoolItemELA();
        item.setClaim(claim);
        item.setItemId(itemId);
        return item;
    }
}
