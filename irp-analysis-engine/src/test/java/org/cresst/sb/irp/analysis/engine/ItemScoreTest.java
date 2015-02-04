package org.cresst.sb.irp.analysis.engine;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tds.itemscoringengine.*;
import tds.itemscoringengine.itemscorers.QTIItemScorer;

import java.util.HashMap;
import java.util.Map;

public class ItemScoreTest {
    private final static Logger logger = LoggerFactory.getLogger(ItemScoreTest.class);

    /**
     * Not an actual unit test. This is just a feasibility test for using ItemScoreManager
     * to Score an EQ item type.
     */
    @Test
    public void equationScoreTest() {
        Map<String, IItemScorer> engines = new HashMap<>();
        engines.put("EQ", new QTIItemScorer());
        IItemScorerManager scorerManager = new ItemScorerManagerImpl(engines, 1, 2, 1);

        ResponseInfo responseInfo = new ResponseInfo(
                "EQ",
                "1483",
                "<itemResponse><response id=\"RESPONSE\"><math xmlns=\"http://www.w3.org/1998/Math/MathML\" title=\"255\"><mstyle><mn>255</mn></mstyle></math></response></itemResponse>",
                "<AssessmentItem xmlns=\"http://www.imsglobal.org/xsd/imsqti_v2p1\"><outcomeDeclaration baseType=\"integer\" cardinality=\"single\" identifier=\"SCORE\"><defaultValue><value>0</value></defaultValue> </outcomeDeclaration> <responseDeclaration baseType=\"string\" cardinality=\"single\" identifier=\"RESPONSE\" /> <outcomeDeclaration baseType=\"string\" cardinality=\"ordered\" identifier=\"PP_RESPONSE\" /> <outcomeDeclaration identifier=\"obj1\" baseType=\"string\" cardinality=\"ordered\" /> <outcomeDeclaration identifier=\"obj1Count\" baseType=\"integer\" cardinality=\"single\" /> <outcomeDeclaration identifier=\"obj\" baseType=\"string\" cardinality=\"ordered\" /> <outcomeDeclaration identifier=\"objCount\" baseType=\"integer\" cardinality=\"single\" /> <responseProcessing> <setOutcomeValue identifier=\"PP_RESPONSE\"> <customOperator type=\"EQ\" functionName=\"PREPROCESSRESPONSE\" response=\"RESPONSE\" /> </setOutcomeValue> <setOutcomeValue identifier=\"obj1\"> <customOperator type=\"CTRL\" functionName=\"mapExpression\" container=\"PP_RESPONSE\"> <customOperator type=\"EQ\" functionName=\"ISEQUIVALENT\" object=\"@\" exemplar=\"4960/32\" simplify=\"False\" /> </customOperator> </setOutcomeValue> <setOutcomeValue identifier=\"obj1Count\"> <containerSize> <variable identifier=\"obj1\" /> </containerSize> </setOutcomeValue> <setOutcomeValue identifier=\"obj\"> <customOperator type=\"CTRL\" functionName=\"mapExpression\" container=\"PP_RESPONSE\"> <customOperator type=\"EQ\" functionName=\"ISEQUIVALENT\" object=\"@\" exemplar=\"155\" simplify=\"True\" /> </customOperator> </setOutcomeValue> <setOutcomeValue identifier=\"objCount\"> <containerSize> <variable identifier=\"obj\" /> </containerSize> </setOutcomeValue> <responseCondition> <responseIf> <and> <equal> <variable identifier=\"obj1Count\" /> <baseValue baseType=\"integer\">0</baseValue> </equal> <equal> <variable identifier=\"objCount\" /> <baseValue baseType=\"integer\">1</baseValue> </equal> </and> <setOutcomeValue identifier=\"SCORE\"> <baseValue baseType=\"integer\">1</baseValue> </setOutcomeValue> </responseIf> </responseCondition> </responseProcessing></AssessmentItem>",
                RubricContentType.ContentString,
                null,
                false);

        ItemScore itemScore = scorerManager.ScoreItem(responseInfo, null);
        ItemScoreInfo itemScoreInfo = itemScore.getScoreInfo();

        logger.info("{} - {} - {}", itemScoreInfo.getStatus(), itemScoreInfo.getPoints(), itemScoreInfo.getRationale().getMsg());

        scorerManager.shutdown();
    }
}
