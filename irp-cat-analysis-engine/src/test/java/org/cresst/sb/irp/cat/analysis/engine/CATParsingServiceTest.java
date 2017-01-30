package org.cresst.sb.irp.cat.analysis.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.cresst.sb.irp.cat.domain.analysis.BlueprintStatement;
import org.cresst.sb.irp.cat.domain.analysis.ELAStudentScoreCAT;
import org.cresst.sb.irp.cat.domain.analysis.ItemResponseCAT;
import org.cresst.sb.irp.cat.domain.analysis.MathStudentScoreCAT;
import org.cresst.sb.irp.cat.domain.analysis.PoolItemELA;
import org.cresst.sb.irp.cat.domain.analysis.TrueTheta;
import org.cresst.sb.irp.cat.domain.analysis.ViolationCount;
import org.junit.Before;
import org.junit.Test;

public class CATParsingServiceTest {
    private CATParsingService service;
    private static final double DELTA = 1e-15;

    private ByteArrayInputStream stringToInputStream(String source) {
        return new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8));
    }
    @Before
    public void setUp() throws Exception {
        service = new CATParsingServiceImpl();
    }

    @Test
    public void testParseTrueThetas_valid_string() throws IOException {
        final String trueThetaString = "sid,score\n1,0.5\n2,1.0";
        final InputStream trueThetaStream = stringToInputStream(trueThetaString);
        List<TrueTheta> parsedThetas = service.parseTrueThetas(trueThetaStream);
        assertEquals("1", parsedThetas.get(0).getSid());
        assertEquals(0.5, parsedThetas.get(0).getScore(), DELTA);
        assertEquals("2", parsedThetas.get(1).getSid());
        assertEquals(1.0, parsedThetas.get(1).getScore(), DELTA);
        assertEquals(2, parsedThetas.size());
    }

    @Test
    public void testParseBlueprintCsv_valid_blueprint() throws IOException {
        final String blueprintString = "description,subject,grade,claim,min,max,target,dok,dok>=,passage,short_answer,stim,weight\n"
                + "test1,ela,11,1,1,1,,,,,,item,1\n" 
                + "test2,ela,11,1,1,,\"\"\"1,3\"\"\",1,,,,item,1";
        final InputStream blueprintStream = stringToInputStream(blueprintString);
        List<BlueprintStatement> parsedBlueprints = service.parseBlueprintCsv(blueprintStream);
        assertEquals(2, parsedBlueprints.size());
        BlueprintStatement row1 = parsedBlueprints.get(0);
        BlueprintStatement row2 = parsedBlueprints.get(1);
        
        assertEquals("ela", row1.getSubject());
        assertEquals(11, row1.getGrade());
        assertEquals(1, row1.getMin());
        assertEquals(1, row1.getMax());
        assertEquals("Claim 1 , test1", row1.getSpecification());
        assertEquals("1", row1.getMaxStr());

        PoolItemELA matchedPoolItem = new PoolItemELA();
        matchedPoolItem.setItemGrade("11");
        matchedPoolItem.setClaim("1");
        matchedPoolItem.setPoolGrade("11");

        assertTrue(row1.testAndIncrementMatch(matchedPoolItem));
        row1.updateViolations();

        ViolationCount violations = new ViolationCount();
        violations.setMatch(1);
        assertEquals(violations, row1.getViolationCount());
        
        assertEquals(Integer.MAX_VALUE, row2.getMax());
        assertEquals("Claim 1 , test2, DOK = 1 , Targets: 1, 3", row2.getSpecification());
        assertEquals("NA", row2.getMaxStr());
    }

    @Test
    public void testParseItemCsv() throws IOException {
        final String itemString = "sId, itemId, score\n1,1,1";
        final InputStream itemStream = stringToInputStream(itemString);
        List<ItemResponseCAT> items = service.parseItemCsv(itemStream);
        assertEquals(1, items.size());

        ItemResponseCAT item = items.get(0);
        assertEquals("1", item.getItemId());
        assertEquals("1", item.getsId());
        assertEquals(1, item.getScore());
    }

    @Test
    public void testParseStudentScoreELACAT() throws IOException {
        final String itemString = "SID,Overall,Overall_SEM,Claim1,Claim1_SEM,Claim2,Claim2_SEM,Claim3,Claim3_SEM,Claim4,Claim4_SEM\n"
                + "1, 5.0, 0.5, 1.0, 0.1, 2.0, 0.2, 3.0, 0.3, 4.0, 0.4";
        final InputStream itemStream = stringToInputStream(itemString);
        List<ELAStudentScoreCAT> items = service.parseStudentELACsv(itemStream);
        assertEquals(1, items.size());

        ELAStudentScoreCAT item = items.get(0);
        assertEquals("1", item.getSid());
        assertEquals(5.0, item.getOverallScore(), DELTA);
        assertEquals(0.5, item.getOverallSEM(), DELTA);
        assertEquals(1.0, item.getClaim1Score(), DELTA);
        assertEquals(0.1, item.getClaim1SEM(), DELTA);
        assertEquals(2.0, item.getClaim2Score(), DELTA);
        assertEquals(0.2, item.getClaim2SEM(), DELTA);
        assertEquals(3.0, item.getClaim3Score(), DELTA);
        assertEquals(0.3, item.getClaim3SEM(), DELTA);
        assertEquals(4.0, item.getClaim4Score(), DELTA);
        assertEquals(0.4, item.getClaim4SEM(), DELTA);
    }

    @Test
    public void testParseStudentScoreMath() throws IOException {
        final String itemString = "SID,Overall,Overall_SEM,Claim1,Claim1_SEM,Claim2_4Score,Claim2_4SEM,Claim3,Claim3_SEM\n"
                + "1, 5.0, 0.5, 1.0, 0.1, 2.4, 4.2, 3.0, 0.3";
        final InputStream itemStream = stringToInputStream(itemString);
        List<MathStudentScoreCAT> items = service.parseStudentMathCsv(itemStream);
        assertEquals(1, items.size());

        MathStudentScoreCAT item = items.get(0);
        assertEquals("1", item.getSid());
        assertEquals(5.0, item.getOverallScore(), DELTA);
        assertEquals(0.5, item.getOverallSEM(), DELTA);
        assertEquals(1.0, item.getClaim1Score(), DELTA);
        assertEquals(0.1, item.getClaim1SEM(), DELTA);
        assertEquals(2.4, item.getClaim2_4Score(), DELTA);
        assertEquals(4.2, item.getClaim2_4SEM(), DELTA);
        assertEquals(3.0, item.getClaim3Score(), DELTA);
        assertEquals(0.3, item.getClaim3SEM(), DELTA);
    }

}
