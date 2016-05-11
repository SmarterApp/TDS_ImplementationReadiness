package org.cresst.sb.irp.domain.analysis;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

//Field Check Type (P) --> check that field is not empty, and field value is of correct data type and within acceptable values
//Field Check Type (PC) --> check everything the same as (P) plus check if  field value is correct
public class FieldCheckType {

    public enum EnumFieldCheckType {
        D, P, PC
    }

    private boolean isFieldValueEmpty = true;    // Field value is empty
    private boolean isRequiredFieldMissing;      // Required field is missing
    private boolean isCorrectDataType;
    private boolean isAcceptableValue;
    private boolean isCorrectValue;
    private boolean isCorrectWidth;
    private boolean isOptionalValue;
    private boolean isUnknownField = false;

    private EnumFieldCheckType enumfieldCheckType;

    public FieldCheckType() {
        isFieldValueEmpty = true;
    }

    public boolean isFieldValueEmpty() {
        return isFieldValueEmpty;
    }

    public void setFieldValueEmpty(boolean isFieldEmpty) {
        this.isFieldValueEmpty = isFieldEmpty;
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

    public boolean isCorrectWidth() {
        return isCorrectWidth;
    }

    public void setCorrectWidth(boolean correctWidth) {
        isCorrectWidth = correctWidth;
    }

    public boolean isOptionalValue() {
        return isOptionalValue;
    }

    public void setOptionalValue(boolean isOptionalValue) {
        this.isOptionalValue = isOptionalValue;
    }

    public boolean isRequiredFieldMissing() {
        return isRequiredFieldMissing;
    }

    public void setRequiredFieldMissing(boolean requiredFieldMissing) {
        isRequiredFieldMissing = requiredFieldMissing;
    }

    public boolean isUnknownField() {
        return isUnknownField;
    }

    public void setUnknownField(boolean unknownField) {
        isUnknownField = unknownField;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("isOptionalValue", isOptionalValue)
                .append("isFieldValueEmpty", isFieldValueEmpty)
                .append("isCorrectDataType", isCorrectDataType)
                .append("isAcceptableValue", isAcceptableValue)
                .append("isCorrectValue", isCorrectValue)
                .append("isCorrectWidth", isCorrectWidth)
                .append("enumfieldCheckType", enumfieldCheckType)
                .append("isRequiredFieldMissing", isRequiredFieldMissing)
                .append("isUnknownField", isUnknownField)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldCheckType that = (FieldCheckType) o;
        return isOptionalValue == that.isOptionalValue &&
                isFieldValueEmpty == that.isFieldValueEmpty &&
                isCorrectDataType == that.isCorrectDataType &&
                isAcceptableValue == that.isAcceptableValue &&
                isCorrectValue == that.isCorrectValue &&
                isCorrectWidth == that.isCorrectWidth &&
                enumfieldCheckType == that.enumfieldCheckType &&
                isRequiredFieldMissing == that.isRequiredFieldMissing &&
                isUnknownField == that.isUnknownField;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isOptionalValue, isFieldValueEmpty, isCorrectDataType, isAcceptableValue, isCorrectValue, isCorrectWidth, enumfieldCheckType, isRequiredFieldMissing, isUnknownField);
    }
}
