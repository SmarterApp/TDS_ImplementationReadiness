package org.cresst.sb.irp.analysis.engine;

import builders.CellCategoryBuilder;
import builders.ExamineeAttributeBuilder;
import builders.ExamineeRelationshipBuilder;
import builders.StudentBuilder;
import com.google.common.collect.Lists;
import org.cresst.sb.irp.analysis.engine.examinee.EnumExamineeAttributeFieldName;
import org.cresst.sb.irp.analysis.engine.examinee.EnumExamineeRelationshipFieldName;
import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.ExamineeRelationshipCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.tdsreport.Context;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.cresst.sb.irp.service.StudentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Test class to verify the behavior of ExamineeRelationshipAnalysisAction.
 */
@RunWith(MockitoJUnitRunner.class)
public class ExamineeRelationshipAnalysisActionTest {

    final private String DISTRICT_ID = EnumExamineeRelationshipFieldName.DistrictId.name();
    final private String DISTRICT_NAME = EnumExamineeRelationshipFieldName.DistrictName.name();
    final private String SCHOOL_ID = EnumExamineeRelationshipFieldName.SchoolId.name();
    final private String SCHOOL_NAME = EnumExamineeRelationshipFieldName.SchoolName.name();
    final private String STATE_NAME = EnumExamineeRelationshipFieldName.StateName.name();
    final private String STATE_ABBREVIATION = EnumExamineeRelationshipFieldName.StateAbbreviation.name();

    @Mock
    private StudentService studentService;

    @InjectMocks
    private ExamineeRelationshipAnalysisAction underTest = new ExamineeRelationshipAnalysisAction();

    /**
     * Helper method to create an IndividualResponse with a TDSReport containing an Examinee and ExamineeRelationships
     *
     * @param studentIdentifier Student's SSID
     * @param examineeRelationships List of ExamineeRelationship objects
     * @return A pre-populated IndividualResponse
     */
    private IndividualResponse generateIndividualResponse(String studentIdentifier,
                                                          List<TDSReport.Examinee.ExamineeRelationship> examineeRelationships) {

        final TDSReport.Examinee examinee = new TDSReport.Examinee();

        TDSReport.Examinee.ExamineeAttribute studentIdentifierAttribute = new ExamineeAttributeBuilder()
                .name(EnumExamineeAttributeFieldName.StudentIdentifier.name())
                .value(studentIdentifier)
                .context(Context.FINAL)
                .toExamineeAttribute();

        examinee.getExamineeAttributeOrExamineeRelationship().add(studentIdentifierAttribute);

        if (examineeRelationships != null) {
            examinee.getExamineeAttributeOrExamineeRelationship().addAll(examineeRelationships);
        }

        final TDSReport tdsReport = new TDSReport();
        tdsReport.setExaminee(examinee);

        final IndividualResponse individualResponse = new IndividualResponse();
        individualResponse.setTDSReport(tdsReport);

        return individualResponse;
    }

    /**
     * Verify a positive result occurs when Student data matches the ExamineeRelationship
     */
    @Test
    public void whenStateAbbreviationMatchesStudent_CorrectField() throws Exception {

        // Arrange
        final String SSID = "StudentID";
        final List<TDSReport.Examinee.ExamineeRelationship> examineeRelationships = Lists.newArrayList(
                new ExamineeRelationshipBuilder()
                        .name(STATE_ABBREVIATION)
                        .value("HI")
                        .context(Context.FINAL)
                        .toExamineeRelationship());
        final IndividualResponse individualResponse = generateIndividualResponse(SSID, examineeRelationships);

        // Create a Student with the same State Abbreviation as the ExamineeRelationship
        when(studentService.getStudentByStudentSSID(SSID)).thenReturn(new StudentBuilder(SSID).stateAbbreviation("HI").toStudent());

        // Act
        underTest.analyze(individualResponse);

        // Assert
        CellCategory actualCellCategory = getActualCellCategory(EnumExamineeRelationshipFieldName.StateAbbreviation, individualResponse);
        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .tdsFieldName(STATE_ABBREVIATION)
                .tdsFieldNameValue("HI")
                .tdsExpectedValue("HI")
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .correctValue(true)
                .correctDataType(true)
                .acceptableValue(true)
                .fieldEmpty(false)
                .correctWidth(true)
                .toCellCategory();

        assertEquals(expectedCellCategory, actualCellCategory);
    }

    /**
     * When the IRP Student fields match a ExamineeRelationships then the ExamineeRelationships should be marked
     * as being correct fields. When an ExamineeRelationship attribute that the IRP Student doesn't have is encountered
     * and the attribute is known (e.g. OrganizationName, StateName) then the ExamineeRelationship is marked as a
     * correct field.
     *
     * @throws Exception
     */
    @Test
    public void whenAllExamineeRelationshipsMatchStudent_CorrectFields() throws Exception {
        // Arrange
        // Test student SSID
        final String SSID = "StudentID";

        final List<TDSReport.Examinee.ExamineeRelationship> examineeRelationships = Lists.newArrayList(
                new ExamineeRelationshipBuilder()
                        .name(DISTRICT_ID)
                        .value("44444")
                        .context(Context.FINAL)
                        .toExamineeRelationship(),
                new ExamineeRelationshipBuilder()
                        .name(DISTRICT_NAME)
                        .value("HI State Schools")
                        .context(Context.FINAL)
                        .toExamineeRelationship(),
                new ExamineeRelationshipBuilder()
                        .name(SCHOOL_ID)
                        .value("12345")
                        .context(Context.FINAL)
                        .toExamineeRelationship(),
                new ExamineeRelationshipBuilder()
                        .name(SCHOOL_NAME)
                        .value("High School High")
                        .context(Context.FINAL)
                        .toExamineeRelationship(),
                new ExamineeRelationshipBuilder()
                        .name(STATE_NAME)
                        .value("Hawaii")
                        .context(Context.FINAL)
                        .toExamineeRelationship(),
                new ExamineeRelationshipBuilder()
                        .name(STATE_ABBREVIATION)
                        .value("HI")
                        .context(Context.FINAL)
                        .toExamineeRelationship());
        final IndividualResponse individualResponse = generateIndividualResponse(SSID, examineeRelationships);

        // Create a Student with the same attributes as the ExamineeRelationships
        when(studentService.getStudentByStudentSSID(SSID)).thenReturn(
                new StudentBuilder(SSID)
                        .responsibleDistrictIdentifier("44444")
                        .responsibleInstitutionIdentifier("12345")
                        .stateAbbreviation("HI")
                        .toStudent());

        // Act
        underTest.analyze(individualResponse);

        // Assert
        // Flatten the results into a single list
        List<CellCategory> actualCellCategories = new ArrayList<>();
        for (ExamineeRelationshipCategory actualExamineeRelationshipCategory : individualResponse.getExamineeRelationshipCategories()) {
            for (CellCategory actualCellCategory : actualExamineeRelationshipCategory.getCellCategories()) {
                actualCellCategories.add(actualCellCategory);
            }
        }

        // Field order matches how they are listed in the TRT document
        List<CellCategory> expectedCellCategories = Lists.newArrayList(
                new CellCategoryBuilder()
                        .tdsFieldName(STATE_ABBREVIATION)
                        .tdsFieldNameValue("HI")
                        .tdsExpectedValue("HI")
                        .correctValue(true)
                        .correctDataType(true)
                        .acceptableValue(true)
                        .fieldEmpty(false)
                        .correctWidth(true)
                        .optionalFieldValue(false)
                        .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                        .toCellCategory(),
                new CellCategoryBuilder()
                        .tdsFieldName(DISTRICT_ID)
                        .tdsFieldNameValue("44444")
                        .tdsExpectedValue("44444")
                        .correctValue(true)
                        .correctDataType(true)
                        .acceptableValue(true)
                        .fieldEmpty(false)
                        .correctWidth(true)
                        .optionalFieldValue(true)
                        .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                        .toCellCategory(),
                new CellCategoryBuilder()
                        .tdsFieldName(DISTRICT_NAME)
                        .tdsFieldNameValue("HI State Schools")
                        .correctValue(true)
                        .correctDataType(true)
                        .acceptableValue(true)
                        .fieldEmpty(false)
                        .correctWidth(true)
                        .optionalFieldValue(true)
                        .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                        .toCellCategory(),
                new CellCategoryBuilder()
                        .tdsFieldName(SCHOOL_ID)
                        .tdsFieldNameValue("12345")
                        .tdsExpectedValue("12345")
                        .correctValue(true)
                        .correctDataType(true)
                        .acceptableValue(true)
                        .fieldEmpty(false)
                        .correctWidth(true)
                        .optionalFieldValue(true)
                        .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                        .toCellCategory(),
                new CellCategoryBuilder()
                        .tdsFieldName(SCHOOL_NAME)
                        .tdsFieldNameValue("High School High")
                        .correctValue(true)
                        .correctDataType(true)
                        .acceptableValue(true)
                        .fieldEmpty(false)
                        .correctWidth(true)
                        .optionalFieldValue(true)
                        .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                        .toCellCategory(),
                new CellCategoryBuilder()
                        .tdsFieldName(STATE_NAME)
                        .tdsFieldNameValue("Hawaii")
                        .correctValue(true)
                        .correctDataType(true)
                        .acceptableValue(true)
                        .fieldEmpty(false)
                        .correctWidth(true)
                        .optionalFieldValue(true)
                        .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                        .toCellCategory());

        assertEquals(expectedCellCategories, actualCellCategories);
    }

    /**
     * When an ExamineeRelationship is unknown then mark the field as being invalid
     * @throws Exception
     */
    @Test
    public void whenExamineeRelationshipIsNotKnown_IncorrectField() throws Exception {
        // Arrange
        final String SSID = "StudentID";
        final List<TDSReport.Examinee.ExamineeRelationship> examineeRelationships = Lists.newArrayList(
                new ExamineeRelationshipBuilder()
                        .name("UnknownExamineeRelationship")
                        .value("SomeValue")
                        .context(Context.FINAL)
                        .toExamineeRelationship());
        final IndividualResponse individualResponse = generateIndividualResponse(SSID, examineeRelationships);

        // Create a Student with the same State Abbreviation as the ExamineeRelationship
        when(studentService.getStudentByStudentSSID(SSID)).thenReturn(new StudentBuilder(SSID).stateAbbreviation("HI").toStudent());

        // Act
        underTest.analyze(individualResponse);

        // Assert
        CellCategory actualCellCategory = null;

        for (ExamineeRelationshipCategory examineeRelationshipCategory : individualResponse.getExamineeRelationshipCategories()) {
            for (CellCategory cellCategory : examineeRelationshipCategory.getCellCategories()) {
                if (cellCategory.getTdsFieldName() == "name") {
                    actualCellCategory = cellCategory;
                    break;
                }
            }

            if (actualCellCategory != null) break;
        }

        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .unkownField(true)
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.P)
                .tdsFieldName("name")
                .tdsFieldNameValue("UnknownExamineeRelationship")
                .toCellCategory();

        assertEquals(expectedCellCategory, actualCellCategory);
    }

    /**
     * Verify only ExamineeRelationships with FINAL context are analyzed
     * @throws Exception
     */
    @Test
    public void verifyOnlyFinalContextExamineeRelationshipsAreAnalyzed() throws Exception {
        // Arrange
        final String SSID = "StudentID";
        final List<TDSReport.Examinee.ExamineeRelationship> examineeRelationships = Lists.newArrayList(
                new ExamineeRelationshipBuilder()
                        .name(STATE_ABBREVIATION)
                        .value("HI")
                        .context(Context.INITIAL)
                        .toExamineeRelationship(),
                new ExamineeRelationshipBuilder()
                        .name(STATE_ABBREVIATION)
                        .value("HI")
                        .context(Context.FINAL)
                        .toExamineeRelationship());
        final IndividualResponse individualResponse = generateIndividualResponse(SSID, examineeRelationships);

        // Create a Student with the same State Abbreviation as the ExamineeRelationship
        when(studentService.getStudentByStudentSSID(SSID)).thenReturn(new StudentBuilder(SSID).stateAbbreviation("HI").toStudent());

        // Act
        underTest.analyze(individualResponse);

        // Assert
        CellCategory actualCellCategory = getActualCellCategory(EnumExamineeRelationshipFieldName.StateAbbreviation, individualResponse);
        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .correctValue(true)
                .correctDataType(true)
                .acceptableValue(true)
                .correctWidth(true)
                .tdsExpectedValue("HI")
                .fieldEmpty(false)
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .tdsFieldName(STATE_ABBREVIATION)
                .tdsFieldNameValue("HI")
                .toCellCategory();

        assertEquals(expectedCellCategory, actualCellCategory);
    }

    @Test
    public void whenExamineeKeyDoesNotMatchExistingIRPStudent_IncorrectField() {
        // Arrange
        final String SSID = "StudentID";
        final List<TDSReport.Examinee.ExamineeRelationship> examineeRelationships = Lists.newArrayList(
                new ExamineeRelationshipBuilder()
                        .name(STATE_ABBREVIATION)
                        .value("HI")
                        .context(Context.FINAL)
                        .toExamineeRelationship());
        final IndividualResponse individualResponse = generateIndividualResponse(SSID, examineeRelationships);

        // Create a scenario where the student is not found
        when(studentService.getStudentByStudentSSID(SSID)).thenThrow(new NotFoundException("test"));

        // Act
        underTest.analyze(individualResponse);

        // Assert
        CellCategory actualCellCategory = getActualCellCategory(EnumExamineeRelationshipFieldName.StateAbbreviation, individualResponse);
        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .correctValue(false)
                .correctDataType(true)
                .acceptableValue(true)
                .correctWidth(true)
                .tdsExpectedValue(null)
                .fieldEmpty(false)
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .tdsFieldName(STATE_ABBREVIATION)
                .tdsFieldNameValue("HI")
                .toCellCategory();
        
        assertEquals(expectedCellCategory, actualCellCategory);
    }
    
    @Test
    public void whenResponsibleDistrictIdentifierMatchesStudentWithLeadingZeros_CorrectFields() throws Exception {

        // Arrange
        final String SSID = "StudentID";
        final List<TDSReport.Examinee.ExamineeRelationship> examineeRelationships = Lists.newArrayList(
                new ExamineeRelationshipBuilder()
                        .name(DISTRICT_ID)
                        .value("1")
                        .context(Context.FINAL)
                        .toExamineeRelationship());
        
        final IndividualResponse individualResponse = generateIndividualResponse(SSID, examineeRelationships);
        
        // Create a Student with the DistrictId value leading zeros (HI Students1_3.xlsx)
        when(studentService.getStudentByStudentSSID(SSID)).thenReturn(new StudentBuilder(SSID).responsibleDistrictIdentifier("001").toStudent());

        // Act
        underTest.analyze(individualResponse);
        
        // Assert
        CellCategory actualCellCategory = getActualCellCategory(EnumExamineeRelationshipFieldName.DistrictId, individualResponse);
        
        CellCategory expectedCellCategory = new CellCategoryBuilder()
            .tdsFieldName(DISTRICT_ID)
            .tdsFieldNameValue("1")
            .tdsExpectedValue("001")
            .fieldEmpty(false)
            .correctDataType(true)
            .acceptableValue(true)
            .correctValue(true)
            .optionalFieldValue(true)
            .correctWidth(true)
            .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
            .toCellCategory();

        assertEquals(expectedCellCategory, actualCellCategory);
    }

    CellCategory getActualCellCategory(EnumExamineeRelationshipFieldName examineeRelationshipFieldName, IndividualResponse individualResponse) {
        for (ExamineeRelationshipCategory examineeRelationshipCategory : individualResponse.getExamineeRelationshipCategories()) {
            for (CellCategory cellCategory : examineeRelationshipCategory.getCellCategories()) {
                if (cellCategory.getTdsFieldName() == examineeRelationshipFieldName.toString()) {
                    return cellCategory;
                }
            }
        }

        return null;
    }
    
}