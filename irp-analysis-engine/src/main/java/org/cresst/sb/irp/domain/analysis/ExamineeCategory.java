package org.cresst.sb.irp.domain.analysis;

import org.apache.log4j.Logger;

public class ExamineeCategory {
	private static Logger logger = Logger.getLogger(ExamineeCategory.class);

	private Long key;
	private String isDemo;
	private FieldCheckType keyFieldCheckType;
	private FieldCheckType isDemoFieldCheckType;

	public ExamineeCategory() {
		logger.info("initializing");
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getIsDemo() {
		return isDemo;
	}

	public void setIsDemo(String isDemo) {
		this.isDemo = isDemo;
	}

	public FieldCheckType getKeyFieldCheckType() {
		return keyFieldCheckType;
	}

	public void setKeyFieldCheckType(FieldCheckType keyFieldCheckType) {
		this.keyFieldCheckType = keyFieldCheckType;
	}

	public FieldCheckType getIsDemoFieldCheckType() {
		return isDemoFieldCheckType;
	}

	public void setIsDemoFieldCheckType(FieldCheckType isDemoFieldCheckType) {
		this.isDemoFieldCheckType = isDemoFieldCheckType;
	}

	@Override
	public String toString() {
		return "ExamineeCategory [key=" + key + ", isDemo=" + isDemo + ", keyFieldCheckType=" + keyFieldCheckType
				+ ", isDemoFieldCheckType=" + isDemoFieldCheckType + "]";
	}
	

}
