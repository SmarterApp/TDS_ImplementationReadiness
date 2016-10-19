package builders;

import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;

/**
 * Builder to create CellCategory objects
 */
public class CellCategoryBuilder {
    private CellCategory cellCategory = new CellCategory();
    private FieldCheckType fieldCheckType = new FieldCheckType();

    public CellCategoryBuilder() {
        // This is the default value that Analyzers insert when the value is null
        cellCategory.setTdsFieldNameValue("");
    }

    public CellCategoryBuilder tdsFieldName(String fieldName) {
        cellCategory.setTdsFieldName(fieldName);
        return this;
    }

    public CellCategoryBuilder tdsFieldNameValue(String fieldValue) {
        cellCategory.setTdsFieldNameValue(fieldValue);
        return this;
    }

    public CellCategoryBuilder tdsExpectedValue(String expectedValue) {
        cellCategory.setTdsExpectedValue(expectedValue);
        return this;
    }

    public CellCategoryBuilder correctValue(boolean correctValue) {
        fieldCheckType.setCorrectValue(correctValue);
        return this;
    }

    public CellCategoryBuilder correctDataType(boolean correctDataType) {
        fieldCheckType.setCorrectDataType(correctDataType);
        return this;
    }

    public CellCategoryBuilder acceptableValue(boolean isAcceptableValue) {
        fieldCheckType.setAcceptableValue(isAcceptableValue);
        return this;
    }

    public CellCategoryBuilder fieldEmpty(boolean isFieldEmpty) {
        fieldCheckType.setFieldValueEmpty(isFieldEmpty);
        return this;
    }

    public CellCategoryBuilder fieldMissing(boolean isFieldMissing) {
        fieldCheckType.setRequiredFieldMissing(isFieldMissing);
        return this;
    }

    public CellCategoryBuilder correctWidth(boolean isCorrectWidth) {
        fieldCheckType.setCorrectWidth(isCorrectWidth);
        return this;
    }

    public CellCategoryBuilder optionalFieldValue(boolean isOptionalFieldValue) {
        fieldCheckType.setOptionalValue(isOptionalFieldValue);
        return this;
    }

    public CellCategoryBuilder unkownField(boolean isUnknownField) {
        fieldCheckType.setUnknownField(isUnknownField);
        return this;
    }

    public CellCategoryBuilder enumFieldCheckType(FieldCheckType.EnumFieldCheckType enumFieldCheckType) {
        fieldCheckType.setEnumfieldCheckType(enumFieldCheckType);
        return this;
    }

    public CellCategory toCellCategory() {
        cellCategory.setFieldCheckType(fieldCheckType);
        return cellCategory;
    }
}