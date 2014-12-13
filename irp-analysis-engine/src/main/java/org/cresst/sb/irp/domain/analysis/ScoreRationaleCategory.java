package org.cresst.sb.irp.domain.analysis;

import org.apache.log4j.Logger;

public class ScoreRationaleCategory {
	private static Logger logger = Logger.getLogger(ScoreRationaleCategory.class);
	
	private String message;
	
	private FieldCheckType messageFieldCheckType;
	
	public ScoreRationaleCategory() {
		logger.info("initializing");
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public FieldCheckType getMessageFieldCheckType() {
		return messageFieldCheckType;
	}

	public void setMessageFieldCheckType(FieldCheckType messageFieldCheckType) {
		this.messageFieldCheckType = messageFieldCheckType;
	}

	@Override
	public String toString() {
		return "ScoreRationaleCategory [message=" + message + ", messageFieldCheckType=" + messageFieldCheckType + "]";
	}
	
	
}
