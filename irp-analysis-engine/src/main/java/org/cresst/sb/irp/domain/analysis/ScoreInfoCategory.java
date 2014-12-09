package org.cresst.sb.irp.domain.analysis;

import org.apache.log4j.Logger;

public class ScoreInfoCategory {
	private static Logger logger = Logger.getLogger(ScoreInfoCategory.class);
	
	private ScoreRationaleCategory scoreRationaleCategory;
	
	public ScoreInfoCategory() {
		logger.info("initializing");
	}

	public ScoreRationaleCategory getScoreRationaleCategory() {
		return scoreRationaleCategory;
	}

	public void setScoreRationaleCategory(ScoreRationaleCategory scoreRationaleCategory) {
		this.scoreRationaleCategory = scoreRationaleCategory;
	}
}
