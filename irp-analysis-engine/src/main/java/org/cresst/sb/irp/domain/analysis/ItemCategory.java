package org.cresst.sb.irp.domain.analysis;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class ItemCategory {
	private static Logger logger = Logger.getLogger(ItemCategory.class);

	private List<CellCategory> listItemAttribute;
	private ResponseCategory responseCategory;
	private ScoreInfoCategory scoreInfoCategory; 
	
	public ItemCategory() {
		logger.info("initializing");
		setListItemAttribute(new ArrayList<CellCategory>());
	}

	public List<CellCategory> getListItemAttribute() {
		return listItemAttribute;
	}

	public void setListItemAttribute(List<CellCategory> listItemAttribute) {
		this.listItemAttribute = listItemAttribute;
	}
	
	public ResponseCategory getResponseCategory() {
		return responseCategory;
	}

	public void setResponseCategory(ResponseCategory responseCategory) {
		this.responseCategory = responseCategory;
	}

	public ScoreInfoCategory getScoreInfoCategory() {
		return scoreInfoCategory;
	}

	public void setScoreInfoCategory(ScoreInfoCategory scoreInfoCategory) {
		this.scoreInfoCategory = scoreInfoCategory;
	}

	@Override
	public String toString() {
		return "ItemCategory [listItemAttribute=" + listItemAttribute + ", responseCategory=" + responseCategory
				+ ", scoreInfoCategory=" + scoreInfoCategory + "]";
	}


}
