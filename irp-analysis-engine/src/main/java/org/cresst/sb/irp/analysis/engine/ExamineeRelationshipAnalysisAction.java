package org.cresst.sb.irp.analysis.engine;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.ExamineeRelationshipCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.student.Student;
import org.cresst.sb.irp.domain.tdsreport.Context;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee.ExamineeRelationship;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExamineeRelationshipAnalysisAction extends AnalysisAction<ExamineeRelationship, ExamineeRelationshipAnalysisAction.EnumExamineeRelationshipFieldName, Student> {
    private final static Logger logger = LoggerFactory.getLogger(ExamineeRelationshipAnalysisAction.class);

    // This is the list found in the project tdsdll_release file ReportingDLL.java
    static public enum EnumExamineeRelationshipFieldName {
        ResponsibleDistrictIdentifier, OrganizationName, ResponsibleInstitutionIdentifier, NameOfInstitution, StateName, StateAbbreviation,
        contextDate
    }

    @Override
    public void analyze(IndividualResponse individualResponse) {
        TDSReport tdsReport = individualResponse.getTDSReport();
        Examinee examinee = tdsReport.getExaminee();

        if (examinee != null) {
            Long examineeKey = examinee.getKey();
            Student student = null;
            if (examineeKey != null) {
                try {
                    student = getStudent(examineeKey);
                } catch (NotFoundException ex) {
                    logger.info(String.format("TDS Report contains an Examinee Key (%d) that does not match an IRP Student", examineeKey));
                }
            }

            // Analyze all the ExamineeRelationships that have a FINAL context
            List<ExamineeRelationship> examineeRelationships = getFinalExamineeRelationships(examinee);
            for (ExamineeRelationship examineeRelationship : examineeRelationships) {
                ExamineeRelationshipCategory examineeRelationshipCategory = new ExamineeRelationshipCategory();
                individualResponse.addExamineeRelationshipCategory(examineeRelationshipCategory);

                analyzeExamineeRelationship(examineeRelationshipCategory, examineeRelationship, student);
            }
        }
    }

    /**
     * Gets all of the ExamineeRelationships that have the FINAL context attribute
     *
     * @param examinee Examinee containing the ExamineeRelationships
     * @return List of ExamineeRelationships marked with FINAL context attribute. Never null.
     */
    public List<ExamineeRelationship> getFinalExamineeRelationships(Examinee examinee) {
        List<ExamineeRelationship> listExamineeRelationship = new ArrayList<>();

        if (examinee != null) {
            List<Object> listObject = examinee.getExamineeAttributeOrExamineeRelationship();
            for (Object object : listObject) {
                if (object instanceof ExamineeRelationship) {
                    ExamineeRelationship examineeRelationship = (ExamineeRelationship) object;
                    if (examineeRelationship.getContext() == Context.FINAL) {
                        listExamineeRelationship.add(examineeRelationship);
                    }
                }
            }
        }

        return listExamineeRelationship;
    }

    /**
     * Analyzes the known ExamineeRelationship types
     *
     * @param examineeRelationshipCategory The category to store the results
     * @param examineeRelationship         ExamineeRelationship to analyze
     * @param student                      IRP Student to compare against the ExamineeRelationship
     */
    private void analyzeExamineeRelationship(ExamineeRelationshipCategory examineeRelationshipCategory,
                                             ExamineeRelationship examineeRelationship, Student student) {

        EnumExamineeRelationshipFieldName fieldName = convertToFieldName(examineeRelationship.getName());
        if (fieldName != null) {
            EnumFieldCheckType enumFieldCheckType = EnumFieldCheckType.P;
            switch (fieldName) {
                case ResponsibleDistrictIdentifier:
                case ResponsibleInstitutionIdentifier:
                case StateAbbreviation:
                    enumFieldCheckType = EnumFieldCheckType.PC;
                    break;
                default:
                    break;
            }

            validate(examineeRelationshipCategory, examineeRelationship, examineeRelationship.getValue(), enumFieldCheckType, fieldName, student);
//			validate(examineeRelationshipCategory, examineeRelationship, examineeRelationship.getContextDate(), EnumFieldCheckType.P, EnumExamineeRelationshipFieldName.contextDate, null);
        } else {
            // For unknown ExamineeRelationships, let user know that it is incorrect
            final FieldCheckType fieldCheckType = new FieldCheckType();
            fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
            fieldCheckType.setFieldEmpty(false);
            fieldCheckType.setCorrectDataType(true);

            final CellCategory cellCategory = new CellCategory();
            cellCategory.setTdsFieldName("name");
            cellCategory.setTdsFieldNameValue(examineeRelationship.getName());
            cellCategory.setFieldCheckType(fieldCheckType);

            examineeRelationshipCategory.addCellCategory(cellCategory);
        }
    }

    /**
     * Converts the given string to an {@link ExamineeRelationshipAnalysisAction.EnumExamineeRelationshipFieldName}.
     * If it can't convert, then null is returned.
     *
     * @param nameFieldValue
     * @return The enum if string represents a valid enum. Null otherwise.
     */
    private EnumExamineeRelationshipFieldName convertToFieldName(String nameFieldValue) {
        if (EnumUtils.isValidEnum(EnumExamineeRelationshipFieldName.class, nameFieldValue)) {
            return EnumExamineeRelationshipFieldName.valueOf(nameFieldValue);
        }

        return null;
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
        switch (enumFieldName) {
            case contextDate:
                if (examineeRelationship.getContextDate() != null) {
                    setPcorrect(fieldCheckType);
                }
                break;
            default:
                processP_PritableASCIIone(examineeRelationship.getValue(), fieldCheckType);
                break;
        }
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
            case ResponsibleDistrictIdentifier:
            	//strip left side zeros e.g 001
                if (StringUtils.equalsIgnoreCase(StringUtils.stripStart(student.getResponsibleDistrictIdentifier(), "0"), examineeRelationship.getValue())) {
                    setCcorrect(fieldCheckType);
                }
                break;
            case OrganizationName:
                break;
            case ResponsibleInstitutionIdentifier:
                if (StringUtils.equalsIgnoreCase(student.getResponsibleInstitutionIdentifier(), examineeRelationship.getValue())) {
                    setCcorrect(fieldCheckType);
                }
                break;
            case NameOfInstitution:
                break;
            case StateAbbreviation:
                if (StringUtils.equalsIgnoreCase(student.getStateAbbreviation(), examineeRelationship.getValue())) {
                    setCcorrect(fieldCheckType);
                }
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
            case ResponsibleDistrictIdentifier:
                expectedValue = student.getResponsibleDistrictIdentifier();
                break;
            case OrganizationName:
                break;
            case ResponsibleInstitutionIdentifier:
                expectedValue = student.getResponsibleInstitutionIdentifier();
                break;
            case NameOfInstitution:
                break;
            case StateAbbreviation:
                expectedValue = student.getStateAbbreviation();
                break;
            default:
                break;
        }

        return expectedValue;
    }
}
