package org.cresst.sb.irp.analysis.engine;

import builders.CellCategoryBuilder;
import builders.ExamineeRelationshipBuilder;
import builders.StudentBuilder;
import com.google.common.collect.Lists;
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

    final String RESPONSIBLE_DISTRICT_ID = ExamineeRelationshipAnalysisAction.EnumExamineeRelationshipFieldName.ResponsibleDistrictIdentifier.toString();
    final String ORGANIZATION_NAME = ExamineeRelationshipAnalysisAction.EnumExamineeRelationshipFieldName.OrganizationName.toString();
    final String RESPONSIBLE_INSTITUTION_ID = ExamineeRelationshipAnalysisAction.EnumExamineeRelationshipFieldName.ResponsibleInstitutionIdentifier.toString();
    final String NAME_OF_INSTITUTION= ExamineeRelationshipAnalysisAction.EnumExamineeRelationshipFieldName.NameOfInstitution.toString();
    final String STATE_NAME = ExamineeRelationshipAnalysisAction.EnumExamineeRelationshipFieldName.StateName.toString();
    final String STATE_ABBREVIATION = ExamineeRelationshipAnalysisAction.EnumExamineeRelationshipFieldName.StateAbbreviation.toString();

    @Mock
    private StudentService studentService;

    @InjectMocks
    private ExamineeRelationshipAnalysisAction underTest = new ExamineeRelationshipAnalysisAction();

    /**
     * Helper method to create an IndividualResponse with a TDSReport containing an Examinee and ExamineeRelationships
     * @param examineeKey
     * @param examineeRelationships
     * @return
     */
    private IndividualResponse generateIndividualResponse(Long examineeKey,
                                                          List<TDSReport.Examinee.ExamineeRelationship> examineeRelationships) {

        final TDSReport.Examinee examinee = new TDSReport.Examinee();
        examinee.setKey(examineeKey);

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
        final long SSID = 9999L;
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
        List<CellCategory> actualCellCategories = individualResponse.getExamineeRelationshipCategories().get(0).getCellCategories();
        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .correctValue(true)
                .correctDataType(true)
                .acceptableValue(true)
                .isFieldEmpty(false)
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .tdsExpectedValue("HI")
                .tdsFieldName(STATE_ABBREVIATION)
                .tdsFieldNameValue("HI")
                .toCellCategory();

        assertEquals(expectedCellCategory, actualCellCategories.get(0));
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
        final long SSID = 9999L;

        final List<TDSReport.Examinee.ExamineeRelationship> examineeRelationships = Lists.newArrayList(
                new ExamineeRelationshipBuilder()
                        .name(RESPONSIBLE_DISTRICT_ID)
                        .value("44444")
                        .context(Context.FINAL)
                        .toExamineeRelationship(),
                new ExamineeRelationshipBuilder()
                        .name(ORGANIZATION_NAME)
                        .value("HI State Schools")
                        .context(Context.FINAL)
                        .toExamineeRelationship(),
                new ExamineeRelationshipBuilder()
                        .name(RESPONSIBLE_INSTITUTION_ID)
                        .value("12345")
                        .context(Context.FINAL)
                        .toExamineeRelationship(),
                new ExamineeRelationshipBuilder()
                        .name(NAME_OF_INSTITUTION)
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

        List<CellCategory> expectedCellCategories = Lists.newArrayList(
                new CellCategoryBuilder()
                        .tdsFieldName(RESPONSIBLE_DISTRICT_ID)
                        .tdsFieldNameValue("44444")
                        .tdsExpectedValue("44444")
                        .correctValue(true)
                        .correctDataType(true)
                        .acceptableValue(true)
                        .isFieldEmpty(false)
                        .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                        .toCellCategory(),
                new CellCategoryBuilder()
                        .tdsFieldName(ORGANIZATION_NAME)
                        .tdsFieldNameValue("HI State Schools")
                        .correctValue(false)
                        .correctDataType(true)
                        .acceptableValue(true)
                        .isFieldEmpty(false)
                        .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.P)
                        .toCellCategory(),
                new CellCategoryBuilder()
                        .tdsFieldName(RESPONSIBLE_INSTITUTION_ID)
                        .tdsFieldNameValue("12345")
                        .tdsExpectedValue("12345")
                        .correctValue(true)
                        .correctDataType(true)
                        .acceptableValue(true)
                        .isFieldEmpty(false)
                        .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                        .toCellCategory(),
                new CellCategoryBuilder()
                        .tdsFieldName(NAME_OF_INSTITUTION)
                        .tdsFieldNameValue("High School High")
                        .correctValue(false)
                        .correctDataType(true)
                        .acceptableValue(true)
                        .isFieldEmpty(false)
                        .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.P)
                        .toCellCategory(),
                new CellCategoryBuilder()
                        .tdsFieldName(STATE_NAME)
                        .tdsFieldNameValue("Hawaii")
                        .correctValue(false)
                        .correctDataType(true)
                        .acceptableValue(true)
                        .isFieldEmpty(false)
                        .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.P)
                        .toCellCategory(),
                new CellCategoryBuilder()
                        .tdsFieldName(STATE_ABBREVIATION)
                        .tdsFieldNameValue("HI")
                        .tdsExpectedValue("HI")
                        .correctValue(true)
                        .correctDataType(true)
                        .acceptableValue(true)
                        .isFieldEmpty(false)
                        .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                        .toCellCategory());

        assertEquals(expectedCellCategories, actualCellCategories);
    }

    /**
     * When an ExamineeRelationship is unknown then mark the field as being invalide
     * @throws Exception
     */
    @Test
    public void whenExamineeRelationshipIsNotKnown_IncorrectField() throws Exception {
        // Arrange
        final long SSID = 9999L;
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
        List<CellCategory> actualCellCategories = individualResponse.getExamineeRelationshipCategories().get(0).getCellCategories();
        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .correctValue(false)
                .correctDataType(true)
                .acceptableValue(false)
                .isFieldEmpty(false)
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.P)
                .tdsFieldName("name")
                .tdsFieldNameValue("UnknownExamineeRelationship")
                .toCellCategory();

        assertEquals(expectedCellCategory, actualCellCategories.get(0));
    }

    /**
     * Verify only ExamineeRelationships with FINAL context are analyzed
     * @throws Exception
     */
    @Test
    public void verifyOnlyFinalContextExamineeRelationshipsAreAnalyzed() throws Exception {
        // Arrange
        final long SSID = 9999L;
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
        List<CellCategory> actualCellCategories = individualResponse.getExamineeRelationshipCategories().get(0).getCellCategories();
        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .correctValue(true)
                .correctDataType(true)
                .acceptableValue(true)
                .tdsExpectedValue("HI")
                .isFieldEmpty(false)
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .tdsFieldName(STATE_ABBREVIATION)
                .tdsFieldNameValue("HI")
                .toCellCategory();

        assertThat(individualResponse.getExamineeRelationshipCategories().size(), is(1));
        assertEquals(expectedCellCategory, actualCellCategories.get(0));
    }

    @Test
    public void whenExamineeIsNull_EmptyExamineeRelationshipCategory() {
        // Arrange
        TDSReport tdsReport = new TDSReport();
        IndividualResponse individualResponse = new IndividualResponse();
        individualResponse.setTDSReport(tdsReport);

        // Act
        underTest.analyze(individualResponse);

        assertThat(individualResponse.getExamineeRelationshipCategories().size(), is(0));
    }

    @Test
    public void whenExamineeKeyDoesNotMatchExistingIRPStudent_IncorrectField() {
        // Arrange
        final long SSID = 9999L;
        final List<TDSReport.Examinee.ExamineeRelationship> examineeRelationships = Lists.newArrayList(
                new ExamineeRelationshipBuilder()
                        .name(STATE_ABBREVIATION)
                        .value("HI")
                        .context(Context.FINAL)
                        .toExamineeRelationship());
        final IndividualResponse individualResponse = generateIndividualResponse(SSID, examineeRelationships);

        // Create a Student with the same State Abbreviation as the ExamineeRelationship
        when(studentService.getStudentByStudentSSID(SSID)).thenThrow(new NotFoundException("test"));

        // Act
        underTest.analyze(individualResponse);

        // Assert
        List<CellCategory> actualCellCategories = individualResponse.getExamineeRelationshipCategories().get(0).getCellCategories();
        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .correctValue(false)
                .correctDataType(true)
                .acceptableValue(true)
                .tdsExpectedValue(null)
                .isFieldEmpty(false)
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .tdsFieldName(STATE_ABBREVIATION)
                .tdsFieldNameValue("HI")
                .toCellCategory();
        
        assertEquals(expectedCellCategory, actualCellCategories.get(0));
    }
}