package org.cresst.sb.irp.analysis.engine;

import org.apache.commons.lang3.StringUtils;
import org.cresst.sb.irp.analysis.engine.examinee.EnumExamineeRelationshipFieldName;
import org.cresst.sb.irp.analysis.engine.examinee.ExamineeHelper;
import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.ExamineeRelationshipCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.student.Student;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee.ExamineeRelationship;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamineeRelationshipAnalysisAction extends AnalysisAction<ExamineeRelationship, EnumExamineeRelationshipFieldName, Student> {
    private final static Logger logger = LoggerFactory.getLogger(ExamineeRelationshipAnalysisAction.class);

    static private String[] stateAbbreviationAcceptableValues = {
            "AK", "AL", "AR", "AS", "AZ", "CA", "CO", "CT", "DC", "DE", "FL", "FM", "GA", "GU", "HI", "IA", "ID", "IL",
            "IN", "KS", "KY", "LA", "MA", "MD", "ME", "MH", "MI", "MN", "MO", "MP", "MS", "MT", "NC", "ND", "NE", "NH",
            "NJ", "NM", "NV", "NY", "OH", "OK", "OR", "PA", "PR", "PW", "RI", "SC", "SD", "TN", "TX", "UT", "VA", "VI",
            "VT", "WA", "WI", "WV", "WY", "AA", "AE", "AP"
    };

    @Override
    public void analyze(IndividualResponse individualResponse) {
        TDSReport tdsReport = individualResponse.getTDSReport();
        Examinee tdsReportExaminee = tdsReport.getExaminee(); //<xs:element name="Examinee" minOccurs="1" maxOccurs="1">

        String studentIdentifier = ExamineeHelper.getStudentIdentifier(tdsReportExaminee);

        Student student = null;
        try {
            student = getStudent(studentIdentifier);
        } catch (NotFoundException ex) {
            logger.info(String.format("TDS Report contains an Student Identifier (%s) that does not match an IRP Student", studentIdentifier));
        }
  
        // Analyze all the ExamineeRelationships that have a FINAL context
        List<ExamineeRelationship> tdsExamineeRelationships = ExamineeHelper.getFinalExamineeRelationships(tdsReportExaminee);

        for (EnumExamineeRelationshipFieldName enumExamineeRelationshipFieldName : EnumExamineeRelationshipFieldName.values()) {

            ExamineeRelationshipCategory examineeRelationshipCategory = new ExamineeRelationshipCategory();
            individualResponse.addExamineeRelationshipCategory(examineeRelationshipCategory);

            ExamineeRelationship examineeRelationship = ExamineeHelper.getFinalExamineeRelationship(tdsReportExaminee, enumExamineeRelationshipFieldName);

            analyzeExamineeRelationship(examineeRelationshipCategory, enumExamineeRelationshipFieldName, examineeRelationship, student);

            if (examineeRelationship != null) {
                // Remove ExamineeRelationships from tdsExamineeRelationships so that extraneous Relationships can be marked as such
                for (int i = tdsExamineeRelationships.size() - 1; i >= 0; i--) {
                    ExamineeRelationship toRemove = tdsExamineeRelationships.get(i);
                    if (ExamineeHelper.convertToExamineeRelationshipEnum(toRemove.getName()) == enumExamineeRelationshipFieldName) {
                        tdsExamineeRelationships.remove(i);
                    }
                }
            }
        }

        // The remaining ExamineeRelationships are not defined in the Open System spec
        for (ExamineeRelationship extraExamineeRelationship : tdsExamineeRelationships) {

            ExamineeRelationshipCategory examineeRelationshipCategory = new ExamineeRelationshipCategory();
            individualResponse.addExamineeRelationshipCategory(examineeRelationshipCategory);

            // For unknown ExamineeRelationships, let user know that it is incorrect
            final FieldCheckType fieldCheckType = new FieldCheckType();
            fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
            fieldCheckType.setUnknownField(true);

            final CellCategory cellCategory = new CellCategory();
            cellCategory.setTdsFieldName("name");
            cellCategory.setTdsFieldNameValue(extraExamineeRelationship.getName());
            cellCategory.setFieldCheckType(fieldCheckType);

            examineeRelationshipCategory.addCellCategory(cellCategory);
        }
        
    }

    /**
     * Analyzes the known ExamineeRelationship types
     *
     * @param examineeRelationshipCategory The category to store the results
     * @param examineeRelationship         ExamineeRelationship to analyze
     * @param student                      IRP Student to compare against the ExamineeRelationship
     */
    private void analyzeExamineeRelationship(ExamineeRelationshipCategory examineeRelationshipCategory,
                                             EnumExamineeRelationshipFieldName enumExamineeRelationshipFieldName,
                                             ExamineeRelationship examineeRelationship, Student student) {


        if (examineeRelationship != null) {
            validate(examineeRelationshipCategory, examineeRelationship, examineeRelationship.getValue(), EnumFieldCheckType.PC,
                    enumExamineeRelationshipFieldName, student);
        } else if (enumExamineeRelationshipFieldName.isRequired()){

            final FieldCheckType fieldCheckType = new FieldCheckType();
            fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
            fieldCheckType.setFieldValueEmpty(true);
            fieldCheckType.setRequiredFieldMissing(true);
            fieldCheckType.setOptionalValue(false);

            final CellCategory cellCategory = new CellCategory();
            cellCategory.setTdsFieldName(enumExamineeRelationshipFieldName.name());
            cellCategory.setTdsExpectedValue(expectedValue(student, enumExamineeRelationshipFieldName));
            cellCategory.setFieldCheckType(fieldCheckType);

            examineeRelationshipCategory.addCellCategory(cellCategory);
        }
    }

    /**
     * Checks the given ExamineeRelationship's fields for appropriate values
     *
     * @param examineeRelationship ExamineeRelationship with fields to check
     * @param enumFieldName        Specifies the field to check
     * @param fieldCheckType       This is where the results are stored
     */
    @Override
    protected void checkP(ExamineeRelationship examineeRelationship, EnumExamineeRelationshipFieldName enumFieldName, FieldCheckType fieldCheckType) {

        String inputValue = examineeRelationship.getValue();

        switch (enumFieldName) {
            case StateAbbreviation:
                for (int i = 0; i < stateAbbreviationAcceptableValues.length; i++) {
                    if (stateAbbreviationAcceptableValues[i].equals(inputValue)) {
                        fieldCheckType.setAcceptableValue(true);
                        fieldCheckType.setCorrectDataType(true);
                        break;
                    }
                }

                fieldCheckType.setCorrectWidth(inputValue != null && inputValue.length() <= enumFieldName.getMaxWidth());
                fieldCheckType.setFieldValueEmpty(StringUtils.isBlank(inputValue));
                break;
            default:
                processP_PrintableASCIIoneMaxWidth(inputValue, fieldCheckType, enumFieldName.getMaxWidth());
                break;
        }

        fieldCheckType.setOptionalValue(!enumFieldName.isRequired());
    }

    /**
     * Checks if the field has the correct value
     *
     * @param examineeRelationship ExamineeRelationship with fields to check
     * @param enumFieldName        Specifies the field to check
     * @param fieldCheckType       This is where the results are stored
     * @param student              IRP Student that will be compared against the given ExamineeRelationship
     */
    @Override
    protected void checkC(ExamineeRelationship examineeRelationship, EnumExamineeRelationshipFieldName enumFieldName, FieldCheckType fieldCheckType, Student student) {
        if (student == null) {
            return;
        }

        switch (enumFieldName) {
            case StateAbbreviation:
                if (StringUtils.equalsIgnoreCase(student.getStateAbbreviation(), examineeRelationship.getValue())) {
                    setCcorrect(fieldCheckType);
                }
                break;
            case DistrictId:
            	//strip left side zeros e.g 001
                if (StringUtils.equalsIgnoreCase(StringUtils.stripStart(student.getResponsibleDistrictIdentifier(), "0"), examineeRelationship.getValue())) {
                    setCcorrect(fieldCheckType);
                }
                break;
            case SchoolId:
                if (StringUtils.equalsIgnoreCase(student.getResponsibleInstitutionIdentifier(), examineeRelationship.getValue())) {
                    setCcorrect(fieldCheckType);
                }
                break;
            case DistrictName:
            case SchoolName:
            case StateName:
            case StudentGroupName:
                // Set these correct since they are PC type but don't have corresponding values in the Student object
                setCcorrect(fieldCheckType);
                break;
            default:
                break;
        }
    }

    /**
     * Gets the expected value that the field being analyzed should contain.
     *
     * @param student       IRP Student containing the field with the expected value
     * @param enumFieldName Specifies the field to check
     * @return The value of the student that is expected for the given Relationship field
     */
    @Override
    protected String expectedValue(Student student, EnumExamineeRelationshipFieldName enumFieldName) {

        if (student == null) {
            return null;
        }

        String expectedValue = null;

        switch (enumFieldName) {
            case StateAbbreviation:
                expectedValue = student.getStateAbbreviation();
                break;
            case DistrictId:
                expectedValue = student.getResponsibleDistrictIdentifier();
                break;
            case DistrictName:
                break;
            case SchoolId:
                expectedValue = student.getResponsibleInstitutionIdentifier();
                break;
            default:
                break;
        }

        return expectedValue;
    }
}
