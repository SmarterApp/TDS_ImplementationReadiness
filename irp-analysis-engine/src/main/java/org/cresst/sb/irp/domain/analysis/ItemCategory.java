package org.cresst.sb.irp.domain.analysis;

import org.apache.log4j.Logger;

public class ItemCategory {
	private static Logger logger = Logger.getLogger(ItemCategory.class);

	private ResponseCategory responseCategory;
	private ScoreInfoCategory scoreInfoCategory;
	
	public ItemCategory() {
		logger.info("initializing");
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

}
