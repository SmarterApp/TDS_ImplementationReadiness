package org.cresst.sb.irp.analysis.engine.examinee;

/**
 * Enum for the Examinee Relationships
 * The order matches the way they are defined in the TRT document.
 */
public enum EnumExamineeRelationshipFieldName {
    StateAbbreviation(2),
    DistrictId(40, false),
    DistrictName(60, false),
    SchoolId(40, false),
    SchoolName(60, false),
    StateName(50, false),
    StudentGroupName(50, false);

    private int maxWidth;
    private boolean isRequired;

    EnumExamineeRelationshipFieldName(int maxWidth) {
        this.maxWidth = maxWidth;
        this.isRequired = true;
    }

    EnumExamineeRelationshipFieldName(int maxWidth, boolean isRequired) {
        this.maxWidth = maxWidth;
        this.isRequired = isRequired;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public boolean isRequired() {
        return isRequired;
    }
}