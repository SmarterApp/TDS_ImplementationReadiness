package org.cresst.sb.irp.itemscoring.client;

import org.springframework.web.client.RestTemplate;
import tds.itemscoringengine.*;

/**
 * Gets item score from the Item Scoring Service
 */
public class IrpProxyItemScorer implements IItemScorer {

    private RestTemplate restTemplate;
    private String itemScoringServiceUrl;

    public IrpProxyItemScorer(RestTemplate restTemplate, String itemScoringServiceUrl) {
        this.restTemplate = restTemplate;
        this.itemScoringServiceUrl = itemScoringServiceUrl;
    }

    @Override
    public ItemScore ScoreItem(ResponseInfo responseInfo, IItemScorerCallback iItemScorerCallback) {
        ItemScoreRequest itemScoreRequest = new ItemScoreRequest (responseInfo);
        ItemScoreResponse itemScoreResponse = restTemplate.postForObject(itemScoringServiceUrl, itemScoreRequest, ItemScoreResponse.class);
        return itemScoreResponse.getScore();
    }

    @Override
    public ScorerInfo GetScorerInfo(String s) {
        return null;
    }

    @Override
    public void shutdown() {
    }
}
