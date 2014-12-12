package org.cresst.sb.irp.domain.analysis;

import org.apache.log4j.Logger;

public class ResponseCategory {
	private static Logger logger = Logger.getLogger(ResponseCategory.class);
	
	private String date;
	private String type;
	private String content;
	private FieldCheckType dateFieldCheckType;
	private FieldCheckType typeFieldCheckType;
	private FieldCheckType contentFieldCheckType;
	
	private boolean isMC;
	
	public ResponseCategory() {
		logger.info("initializing");
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public FieldCheckType getDateFieldCheckType() {
		return dateFieldCheckType;
	}

	public void setDateFieldCheckType(FieldCheckType dateFieldCheckType) {
		this.dateFieldCheckType = dateFieldCheckType;
	}

	public FieldCheckType getTypeFieldCheckType() {
		return typeFieldCheckType;
	}

	public void setTypeFieldCheckType(FieldCheckType typeFieldCheckType) {
		this.typeFieldCheckType = typeFieldCheckType;
	}

	public FieldCheckType getContentFieldCheckType() {
		return contentFieldCheckType;
	}

	public void setContentFieldCheckType(FieldCheckType contentFieldCheckType) {
		this.contentFieldCheckType = contentFieldCheckType;
	}

	//Test 
	public boolean isMC() {
		return isMC;
	}

	public void setMC(boolean isMC) {
		this.isMC = isMC;
	}

	@Override
	public String toString() {
		return "ResponseCategory [date=" + date + ", type=" + type + ", dateFieldCheckType="
				+ dateFieldCheckType + ", typeFieldCheckType=" + typeFieldCheckType + ", contentFieldCheckType="
				+ contentFieldCheckType + ", isMC=" + isMC + "]";
	}

	
	
	
}
