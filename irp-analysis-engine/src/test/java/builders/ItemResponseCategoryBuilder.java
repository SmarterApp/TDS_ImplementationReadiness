package builders;

import org.cresst.sb.irp.domain.analysis.ResponseCategory;

import tds.itemscoringengine.ScoringStatus;

public class ItemResponseCategoryBuilder {
	
	private ResponseCategory responseCategory = new ResponseCategory();
	
	public ItemResponseCategoryBuilder scoringStatus(ScoringStatus scoringStatus){
		responseCategory.setScoringStatus(scoringStatus);
		return this;
	}
	
	public ItemResponseCategoryBuilder itemScore(int itemScore){
		responseCategory.setItemScore(itemScore);
		return this;
	}
	
	public ResponseCategory toResponseCategory(){
		return responseCategory;
	}

}
