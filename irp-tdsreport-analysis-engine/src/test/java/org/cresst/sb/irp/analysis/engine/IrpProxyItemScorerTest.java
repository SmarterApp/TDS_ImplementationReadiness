package org.cresst.sb.irp.analysis.engine;

import org.cresst.sb.irp.itemscoring.client.IrpProxyItemScorer;
import org.cresst.sb.irp.itemscoring.client.converter.ItemScoreMessageConverter;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import tds.itemscoringengine.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class IrpProxyItemScorerTest {
    private final static Logger logger = LoggerFactory.getLogger(IrpProxyItemScorerTest.class);

    /**
     * Integration test. Tests IrpProxyItemScorer with actual service call
     */
    @Ignore("Integration test. Item Scoring Service URL must be active.")
    @Test
    public void equationScoreTest() {

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new ItemScoreMessageConverter());

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(messageConverters);

        IItemScorer proxyItemScorer = new IrpProxyItemScorer(restTemplate, "http://localhost:8080/item-scoring-service/Scoring/ItemScoring");
        
        ResponseInfo responseInfo = new ResponseInfo(
                "EQ",
                "1576",
                "<response><math xmlns=\"http://www.w3.org/1998/Math/MathML\" title=\"218\"><mstyle><mn>218</mn></mstyle></math></response>",
                "file:/Users/mentisdominus/Documents/SmarterBalanced/2015.08.19.IrpTestPackageAndContent/IrpContentPackage/Items/Item-187-1576/Item_1576_v5.qrx",
                RubricContentType.Uri,
                "",
                false);


        ItemScore itemScore = proxyItemScorer.ScoreItem(responseInfo, null);
        ItemScoreInfo itemScoreInfo = itemScore.getScoreInfo();

        logger.info("{} - {} - {}", itemScoreInfo.getStatus(), itemScoreInfo.getPoints(), itemScoreInfo.getRationale().getMsg());

        assertEquals(ScoringStatus.Scored, itemScoreInfo.getStatus());
        assertEquals(0, itemScoreInfo.getPoints());
    }
}
