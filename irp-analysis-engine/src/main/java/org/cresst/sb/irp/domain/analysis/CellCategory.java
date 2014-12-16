package org.cresst.sb.irp.domain.analysis;

import org.apache.log4j.Logger;

public class CellCategory {
	private static Logger logger = Logger.getLogger(CellCategory.class);
	
	private String tdsFieldName; //name, subject, testId, bankKey. . .
	private String tdsFieldNameValue;
	private FieldCheckType fieldCheckType;
	
	public CellCategory(){
		logger.info("initializing");
	}

	public String getTdsFieldName() {
		return tdsFieldName;
	}

	public void setTdsFieldName(String tdsFieldName) {
		this.tdsFieldName = tdsFieldName;
	}

	public String getTdsFieldNameValue() {
		return tdsFieldNameValue;
	}

	public void setTdsFieldNameValue(String tdsFieldNameValue) {
		this.tdsFieldNameValue = tdsFieldNameValue;
	}

	public FieldCheckType getFieldCheckType() {
		return fieldCheckType;
	}

	public void setFieldCheckType(FieldCheckType fieldCheckType) {
		this.fieldCheckType = fieldCheckType;
	}

	@Override
	public String toString() {
		return "CellCategory [tdsFieldName=" + tdsFieldName + ", tdsFieldNameValue=" + tdsFieldNameValue + ", fieldCheckType="
				+ fieldCheckType + "]";
	}

	

}