package org.cresst.sb.irp.domain.analysis;

//Field Check Type (P) --> check that field is not empty, and field value is of correct data type and within acceptable values
//Field Check Type (PC) --> check everything the same as (P) plus check if  field value is correct
public class FieldCheckType {

	private boolean isFieldEmpty;
	private boolean isCorrectDataType;
	private boolean isAcceptableValue;
	private boolean isCorrectValue;
	private EnumFieldCheckType enumfieldCheckType;

	public enum EnumFieldCheckType {
		D, P, PC;
	}

	public FieldCheckType(){
		isFieldEmpty = true; 
	}
	
	public boolean isFieldEmpty() {
		return isFieldEmpty;
	}

	public void setFieldEmpty(boolean isFieldEmpty) {
		this.isFieldEmpty = isFieldEmpty;
	}

	public boolean isCorrectDataType() {
		return isCorrectDataType;
	}

	public void setCorrectDataType(boolean isCorrectDataType) {
		this.isCorrectDataType = isCorrectDataType;
	}

	public boolean isAcceptableValue() {
		return isAcceptableValue;
	}

	public void setAcceptableValue(boolean isAcceptableValue) {
		this.isAcceptableValue = isAcceptableValue;
	}

	public boolean isCorrectValue() {
		return isCorrectValue;
	}

	public void setCorrectValue(boolean isCorrectValue) {
		this.isCorrectValue = isCorrectValue;
	}

	public EnumFieldCheckType getEnumfieldCheckType() {
		return enumfieldCheckType;
	}

	public void setEnumfieldCheckType(EnumFieldCheckType enumfieldCheckType) {
		this.enumfieldCheckType = enumfieldCheckType;
	}

	@Override
	public String toString() {
		return "FieldCheckType [isFieldEmpty=" + isFieldEmpty + ", isCorrectDataType=" + isCorrectDataType
				+ ", isAcceptableValue=" + isAcceptableValue + ", isCorrectValue=" + isCorrectValue + ", enumfieldCheckType="
				+ enumfieldCheckType + "]";
	}

	
}
