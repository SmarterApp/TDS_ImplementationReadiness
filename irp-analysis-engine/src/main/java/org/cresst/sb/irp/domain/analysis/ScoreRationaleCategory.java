package org.cresst.sb.irp.domain.analysis;

import org.apache.log4j.Logger;

public class ScoreRationaleCategory {
	private static Logger logger = Logger.getLogger(ScoreRationaleCategory.class);
	
	private MessageCategory messageCategory;
	
	public ScoreRationaleCategory() {
		logger.info("initializing");
	}

	public MessageCategory getMessageCategory() {
		return messageCategory;
	}

	public void setMessageCategory(MessageCategory messageCategory) {
		this.messageCategory = messageCategory;
	}
}
