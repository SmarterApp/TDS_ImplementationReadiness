package builders;

import java.util.ArrayList;
import java.util.List;

import org.cresst.sb.irp.itemscoring.client.IrpProxyItemScorer;
import org.cresst.sb.irp.itemscoring.client.converter.ItemScoreMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import tds.itemscoringengine.IItemScorer;

public class IItemScorerBuilder {
	
	private RestTemplate restTemplate;
    private String itemScoringServiceUrl;
    private IItemScorer proxyItemScorer;
    
    public IItemScorerBuilder restTemplate(){
    	List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new ItemScoreMessageConverter());

        this.restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(messageConverters);
    	return this;
    }
    
    public IItemScorerBuilder itemScoringServiceUrl(String itemScoringServiceUrl){
    	this.itemScoringServiceUrl = itemScoringServiceUrl;
    	return this;
    }
    
	public IItemScorer toIItemScorer(){
		this.proxyItemScorer = new IrpProxyItemScorer(restTemplate, itemScoringServiceUrl);
		return proxyItemScorer;
	}

}
