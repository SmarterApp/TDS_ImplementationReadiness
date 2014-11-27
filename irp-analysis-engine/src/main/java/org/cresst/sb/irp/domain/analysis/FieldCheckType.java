package org.cresst.sb.irp.domain.analysis;

//Field Check Type (P) --> check that field is not empty, and field value is of correct data type and within acceptable values
//Field Check Type (PC) --> check everything the same as (P) plus check if  field value is correct
public class FieldCheckType {

	private boolean isFieldEmpty;
	private boolean isCorrectDataType;
	private boolean isAcceptableValue;
	private boolean isCorrectValue;
	private fieldCheckType _fieldCheckType;

	public enum fieldCheckType {
		D, P, PC;
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

	public fieldCheckType get_fieldCheckType() {
		return _fieldCheckType;
	}

	public void set_fieldCheckType(fieldCheckType _fieldCheckType) {
		this._fieldCheckType = _fieldCheckType;
	}

	@Override
	public String toString() {
		return "FieldCheckType [isFieldEmpty=" + isFieldEmpty
				+ ", isCorrectDataType=" + isCorrectDataType
				+ ", isAcceptableValue=" + isAcceptableValue
				+ ", isCorrectValue=" + isCorrectValue + ", _fieldCheckType="
				+ _fieldCheckType + "]";
	}

}
