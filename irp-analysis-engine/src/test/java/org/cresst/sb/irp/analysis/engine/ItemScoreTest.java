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
                "<assessmentItem xmlns=\"http://www.imsglobal.org/xsd/imsqti_v2p1\" identifier=\"\" title=\"\" timeDependent=\"false\"><responseDeclaration baseType=\"string\" cardinality=\"single\" identifier=\"RESPONSE\" /><outcomeDeclaration baseType=\"integer\" cardinality=\"single\" identifier=\"SCORE\"><defaultValue><value>0</value></defaultValue></outcomeDeclaration><outcomeDeclaration baseType=\"string\" cardinality=\"ordered\" identifier=\"PP_RESPONSE\" /><outcomeDeclaration identifier=\"correctans\" baseType=\"string\" cardinality=\"ordered\" /><outcomeDeclaration identifier=\"correctansCount\" baseType=\"integer\" cardinality=\"single\" /><responseProcessing><setOutcomeValue identifier=\"PP_RESPONSE\"><customOperator type=\"EQ\" functionName=\"PREPROCESSRESPONSE\" response=\"RESPONSE\" /></setOutcomeValue><setOutcomeValue identifier=\"correctans\"><customOperator type=\"CTRL\" functionName=\"mapExpression\" container=\"PP_RESPONSE\"><or><customOperator type=\"EQ\" functionName=\"ISEQUIVALENT\" object=\"@\" exemplar=\"Eq( ,59)\" simplify=\"False\" /><customOperator type=\"EQ\" functionName=\"ISEQUIVALENT\" object=\"@\" exemplar=\"59\" simplify=\"True\" /><customOperator type=\"EQ\" functionName=\"ISEQUIVALENT\" object=\"@\" exemplar=\"Eq(59,59)\" simplify=\"True\" /><and><customOperator type=\"EQ\" functionName=\"ISEQUIVALENT\" object=\"@\" exemplar=\"Eq(55,55)\" simplify=\"True\" /><customOperator type=\"EQ\" functionName=\"EXRESSIONCONTAINS\" object=\"@\" string=\"55\" /><customOperator type=\"EQ\" functionName=\"EXRESSIONCONTAINS\" object=\"@\" string=\"4\" /></and></or></customOperator></setOutcomeValue><setOutcomeValue identifier=\"correctansCount\"><containerSize><variable identifier=\"correctans\" /></containerSize></setOutcomeValue><responseCondition><responseIf><equal><variable identifier=\"correctansCount\" /><baseValue baseType=\"float\">1</baseValue></equal><setOutcomeValue identifier=\"SCORE\"><baseValue baseType=\"integer\">1</baseValue></setOutcomeValue></responseIf></responseCondition></responseProcessing></assessmentItem>",
                RubricContentType.ContentString,
                null,
                false);

        ItemScore itemScore = scorerManager.ScoreItem(responseInfo, null);
        ItemScoreInfo itemScoreInfo = itemScore.getScoreInfo();

        logger.info("{} - {} - {}", itemScoreInfo.getStatus(), itemScoreInfo.getPoints(), itemScoreInfo.getRationale().getMsg());

        scorerManager.shutdown();
        
    }
}
