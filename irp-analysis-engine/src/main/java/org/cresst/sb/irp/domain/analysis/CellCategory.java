package org.cresst.sb.irp.domain.analysis;

public class CellCategory {
    private String tdsFieldName; //name, subject, testId, bankKey. . .
    private String tdsFieldNameValue;
    private FieldCheckType fieldCheckType;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CellCategory that = (CellCategory) o;

        if (fieldCheckType != null ? !fieldCheckType.equals(that.fieldCheckType) : that.fieldCheckType != null)
            return false;
        if (tdsFieldName != null ? !tdsFieldName.equals(that.tdsFieldName) : that.tdsFieldName != null) return false;
        if (tdsFieldNameValue != null ? !tdsFieldNameValue.equals(that.tdsFieldNameValue) : that.tdsFieldNameValue != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = tdsFieldName != null ? tdsFieldName.hashCode() : 0;
        result = 31 * result + (tdsFieldNameValue != null ? tdsFieldNameValue.hashCode() : 0);
        result = 31 * result + (fieldCheckType != null ? fieldCheckType.hashCode() : 0);
        return result;
    }
}

