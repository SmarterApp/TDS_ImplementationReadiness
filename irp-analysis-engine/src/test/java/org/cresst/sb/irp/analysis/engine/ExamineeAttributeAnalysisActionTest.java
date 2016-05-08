package org.cresst.sb.irp.analysis.engine;

import builders.CellCategoryBuilder;
import builders.ExamineeAttributeBuilder;
import builders.StudentBuilder;

import com.google.common.collect.Lists;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.cresst.sb.irp.analysis.engine.examinee.EnumExamineeAttributeFieldName;
import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.ExamineeAttributeCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.student.Student;
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
 * Test class to verify the behavior of ExamineeAttributeAnalysisAction
 */
@RunWith(MockitoJUnitRunner.class)
public class ExamineeAttributeAnalysisActionTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private ExamineeAttributeAnalysisAction underTest = new ExamineeAttributeAnalysisAction();
    public static final String SSID = "9999";

    /**
     * Helper method to create an IndividualResponse with a TDSReport containing an Examinee and ExamineeAttributes
     *
     * @param studentIdentifier The Student Identifier associated with the mock Examinee
     * @param examineeAttributes Any additional attributes to add to the mock Examinee
     * @return
     */
    private IndividualResponse generateIndividualResponse(String studentIdentifier,
                                                          List<TDSReport.Examinee.ExamineeAttribute> examineeAttributes) {

        final TDSReport.Examinee examinee = new TDSReport.Examinee();

        if (examineeAttributes != null) {
            TDSReport.Examinee.ExamineeAttribute studentIdentifierAttribute =
                    (TDSReport.Examinee.ExamineeAttribute) CollectionUtils.find(examineeAttributes, new Predicate() {
                        @Override
                        public boolean evaluate(Object o) {
                            TDSReport.Examinee.ExamineeAttribute attribute = (TDSReport.Examinee.ExamineeAttribute) o;
                            return attribute.getContext() == Context.FINAL &&
                                    EnumExamineeAttributeFieldName.StudentIdentifier.name().equalsIgnoreCase(attribute.getName());
                        }
                    });

            if (studentIdentifierAttribute != null) {
                studentIdentifierAttribute.setValue(studentIdentifier);
                studentIdentifierAttribute.setContext(Context.FINAL);
                examinee.getExamineeAttributeOrExamineeRelationship().addAll(examineeAttributes);
            } else {
                studentIdentifierAttribute = new ExamineeAttributeBuilder()
                        .name(EnumExamineeAttributeFieldName.StudentIdentifier.name())
                        .value(studentIdentifier)
                        .context(Context.FINAL)
                        .toExamineeAttribute();

                examinee.getExamineeAttributeOrExamineeRelationship().add(studentIdentifierAttribute);
                examinee.getExamineeAttributeOrExamineeRelationship().addAll(examineeAttributes);
            }
        } else {
            examinee.getExamineeAttributeOrExamineeRelationship().add(new ExamineeAttributeBuilder()
                    .name(EnumExamineeAttributeFieldName.StudentIdentifier.name())
                    .value(studentIdentifier)
                    .context(Context.FINAL)
                    .toExamineeAttribute());
        }

        final TDSReport tdsReport = new TDSReport();
        tdsReport.setExaminee(examinee);

        final IndividualResponse individualResponse = new IndividualResponse();
        individualResponse.setTDSReport(tdsReport);

        
        return individualResponse;
    }

    @Test
    public void testAnalyze() throws Exception {

        final IndividualResponse individualResponse = generateIndividualResponse("StudentID", generateAllExamineeAttributes());
   
        when(studentService.getStudentByStudentSSID("StudentID")).thenReturn(new StudentBuilder("StudentID")
                .alternateSSID("8888")
                .americanIndianOrAlaskaNative("No")
                .asian("No")
                .birthdate("01202000")
                .blackOrAfricanAmerican("No")
                .demographicRaceTwoOrMoreRaces("None")
                .economicDisadvantageStatus("None")
                .englishLanguageProficiencyLevel("10")
                .firstEntryDateIntoUSSchool("2001")
                .firstName("TestFirstName")
                .gradeLevelWhenAssessed("10")
                .hispanicOrLatinoEthnicity("Yes")
                .ideaIndicator("No")
                .languageCode("EN")
                .lastOrSurname("TestLastName")
                .lepExitDate("")
                .lepStatus("false")
                .limitedEnglishProficiencyEntryDate("")
                .middleName("TestMiddleName")
                .migrantStatus("None")
                .nativeHawaiianOrOtherPacificIslander("No")
                .primaryDisabilityType("None")
                .section504Status("No")
                .sex("M")
                .studentIdentifier("StudentID")
                .titleIIILanguageInstructionProgramType("")
                .white("No")
                .toStudent());

        underTest.analyze(individualResponse);

        // Flatten ExamineeAttributeCategories
        List<CellCategory> actualCellCategories = new ArrayList<>();
        for (ExamineeAttributeCategory actualExamineeAttributeCategory : individualResponse.getExamineeAttributeCategories()) {
            for (CellCategory cellCategory : actualExamineeAttributeCategory.getCellCategories()) {
                actualCellCategories.add(cellCategory);
            }
        }

        assertThat(actualCellCategories.size(), is(27));
    }

    //region LastOrSurname
    @Test
    public void whenLastOrSurnameHasTooManyCharacters() {
        final String actualFieldValue = "1234567890123456789012345678901234567890";
        final String expectedFieldValue = "LastOrSurname";
        final Student mockStudent = new StudentBuilder(SSID)
                .lastOrSurname(expectedFieldValue)
                .toStudent();

        testMaxWidthError(EnumExamineeAttributeFieldName.LastOrSurname, mockStudent,
                expectedFieldValue, actualFieldValue, false);
    }

    @Test
    public void whenLastOrSurnameHasUnacceptableValue() {
        final String actualFieldValue = "\u0082";
        final String expectedFieldValue = "LastOrSurname";
        final Student mockStudent = new StudentBuilder(SSID)
                .lastOrSurname(expectedFieldValue)
                .toStudent();

        testUnacceptableValue(EnumExamineeAttributeFieldName.LastOrSurname,
                mockStudent, expectedFieldValue, actualFieldValue, false);
    }

    @Test
    public void whenLastOrSurnameMatchesStudent() {
        final String actualFieldValue = "TestLastName";
        final String expectedFieldValue = "TestLastName";
        final Student mockStudent = new StudentBuilder(SSID)
                .lastOrSurname(expectedFieldValue)
                .toStudent();

        testMatchesStudent(EnumExamineeAttributeFieldName.LastOrSurname, mockStudent,
                expectedFieldValue, actualFieldValue, false);
    }
    //endregion

    //region FirstName
    @Test
    public void whenFirstNameHasTooManyCharacters() {
        final String actualFieldValue = "1234567890123456789012345678901234567890";
        final String expectedFieldValue = "FirstName";
        final Student mockStudent = new StudentBuilder(SSID)
                .firstName(expectedFieldValue)
                .toStudent();

        testMaxWidthError(EnumExamineeAttributeFieldName.FirstName, mockStudent,
                expectedFieldValue, actualFieldValue, false);
    }

    @Test
    public void whenFirstNameHasUnacceptableValue() {
        final String actualFieldValue = "\u0082";
        final String expectedFieldValue = "FirstName";
        final Student mockStudent = new StudentBuilder(SSID)
                .firstName(expectedFieldValue)
                .toStudent();

        testUnacceptableValue(EnumExamineeAttributeFieldName.FirstName,
                mockStudent, expectedFieldValue, actualFieldValue, false);
    }

    @Test
    public void whenFirstNameMatchesStudent() {
        final String actualFieldValue = "TestFirstName";
        final String expectedFieldValue = "TestFirstName";
        final Student mockStudent = new StudentBuilder(SSID)
                .firstName(expectedFieldValue)
                .toStudent();

        testMatchesStudent(EnumExamineeAttributeFieldName.FirstName, mockStudent,
                expectedFieldValue, actualFieldValue, false);
    }
    //endregion

    //region MiddleName
    @Test
    public void whenMiddleNameHasTooManyCharacters() {
        final String actualFieldValue = "1234567890123456789012345678901234567890";
        final String expectedFieldValue = "MiddleName";
        final Student mockStudent = new StudentBuilder(SSID)
                .middleName(expectedFieldValue)
                .toStudent();

        testMaxWidthError(EnumExamineeAttributeFieldName.MiddleName, mockStudent,
                expectedFieldValue, actualFieldValue, true);
    }

    @Test
    public void whenMiddleNameHasUnacceptableValue() {
        final String actualFieldValue = "\u0082";
        final String expectedFieldValue = "MiddleName";
        final Student mockStudent = new StudentBuilder(SSID)
                .middleName(expectedFieldValue)
                .toStudent();

        testUnacceptableValue(EnumExamineeAttributeFieldName.MiddleName,
                mockStudent, expectedFieldValue, actualFieldValue, true);
    }

    @Test
    public void whenOptionalMiddleNameIsBlank() {
        final String actualFieldValue = "";
        final String expectedFieldValue = "";
        final Student mockStudent = new StudentBuilder(SSID)
                .middleName(expectedFieldValue)
                .toStudent();

        testOptionalField(EnumExamineeAttributeFieldName.MiddleName, mockStudent,
                expectedFieldValue, actualFieldValue);
    }

    @Test
    public void whenMiddleNameMatchesStudent() {
        final String actualFieldValue = "TestMiddleName";
        final String expectedFieldValue = "TestMiddleName";
        final Student mockStudent = new StudentBuilder(SSID)
                .middleName(expectedFieldValue)
                .toStudent();

        testMatchesStudent(EnumExamineeAttributeFieldName.MiddleName, mockStudent,
                expectedFieldValue, actualFieldValue, true);
    }
    //endregion

    //region Birthday
    @Test
    public void whenBirthdateMatchesStudent_CorrectField() {
    	
        // Arrange
        final List<TDSReport.Examinee.ExamineeAttribute> examineeAttributes = Lists.newArrayList(
                new ExamineeAttributeBuilder()
                        .name(EnumExamineeAttributeFieldName.Birthdate.name())
                        .value("2002-10-15")
                        .context(Context.FINAL)
                        .toExamineeAttribute());
        
        final IndividualResponse individualResponse = generateIndividualResponse(SSID, examineeAttributes);

        when(studentService.getStudentByStudentSSID(SSID)).thenReturn(new StudentBuilder(SSID)
        		.birthdate("2002-10-15")
        		.toStudent());
        
        // Act
        underTest.analyze(individualResponse);
        
        // Assert
        CellCategory actualCellCategory = getActualCellCategory(EnumExamineeAttributeFieldName.Birthdate, individualResponse);
        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .correctValue(true)
                .correctDataType(true)
                .acceptableValue(true)
                .correctWidth(true)
                .tdsExpectedValue("2002-10-15")
                .fieldEmpty(false)
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .tdsFieldName(EnumExamineeAttributeFieldName.Birthdate.name())
                .tdsFieldNameValue("2002-10-15")
                .toCellCategory();
        
        assertEquals(expectedCellCategory, actualCellCategory);
    }

    @Test
    public void whenBirthdayMonthIsLessThanTwoDigitThenAddLeadingZero() {
        birthdayTest("2016-01-15", "2016-1-15");
    }

    @Test
    public void whenBirthdayDayIsLessThanTwoDigitThenAddLeadingZero() {
        birthdayTest("2016-01-02", "2016-01-2");
    }

    void birthdayTest(String expectedBirthday, String actualBirthday) {

        // Arrange
        final List<TDSReport.Examinee.ExamineeAttribute> examineeAttributes = Lists.newArrayList(
                new ExamineeAttributeBuilder()
                        .name(EnumExamineeAttributeFieldName.Birthdate.name())
                        .value(actualBirthday)
                        .context(Context.FINAL)
                        .toExamineeAttribute());

        final IndividualResponse individualResponse = generateIndividualResponse(SSID, examineeAttributes);

        when(studentService.getStudentByStudentSSID(SSID)).thenReturn(new StudentBuilder(SSID)
                .birthdate(expectedBirthday)
                .toStudent());

        // Act
        underTest.analyze(individualResponse);

        // Assert
        CellCategory actualCellCategory = getActualCellCategory(EnumExamineeAttributeFieldName.Birthdate, individualResponse);
        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .correctValue(false)
                .correctDataType(false)
                .acceptableValue(false)
                .correctWidth(false)
                .tdsExpectedValue(expectedBirthday)
                .fieldEmpty(false)
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .tdsFieldName(EnumExamineeAttributeFieldName.Birthdate.name())
                .tdsFieldNameValue(actualBirthday)
                .toCellCategory();

        assertEquals(expectedCellCategory, actualCellCategory);
    }
    //endregion

    //region StudentIdentifier (Special Test Cases)
    @Test
    public void whenStudentIdentifierHasTooManyCharacters() {
        final String actualFieldValue = "12345678901234567890123456789012345678901234567890";
        final String expectedFieldValue = null;

        // Arrange
        final List<TDSReport.Examinee.ExamineeAttribute> examineeAttributes = Lists.newArrayList(
                new ExamineeAttributeBuilder()
                        .name(EnumExamineeAttributeFieldName.StudentIdentifier.name())
                        .value(actualFieldValue)
                        .context(Context.FINAL)
                        .toExamineeAttribute());

        final IndividualResponse individualResponse = generateIndividualResponse(actualFieldValue, examineeAttributes);

        when(studentService.getStudentByStudentSSID(actualFieldValue)).thenThrow(NotFoundException.class);

        // Act
        underTest.analyze(individualResponse);

        // Assert
        CellCategory actualCellCategory = getActualCellCategory(EnumExamineeAttributeFieldName.StudentIdentifier, individualResponse);
        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .tdsFieldName(EnumExamineeAttributeFieldName.StudentIdentifier.name())
                .tdsFieldNameValue(actualFieldValue)
                .tdsExpectedValue(expectedFieldValue)
                .fieldEmpty(false)
                .correctDataType(true)
                .acceptableValue(true)
                .correctWidth(false)
                .correctValue(false)
                .optionalFieldValue(false)
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .toCellCategory();

        assertEquals(expectedCellCategory, actualCellCategory);
    }

    @Test
    public void whenStudentIdentifierHasUnacceptableValue() {
        final String actualFieldValue = "\u0082";
        final String expectedFieldValue = null;

        // Arrange
        final List<TDSReport.Examinee.ExamineeAttribute> examineeAttributes = Lists.newArrayList(
                new ExamineeAttributeBuilder()
                        .name(EnumExamineeAttributeFieldName.StudentIdentifier.name())
                        .value(actualFieldValue)
                        .context(Context.FINAL)
                        .toExamineeAttribute());

        final IndividualResponse individualResponse = generateIndividualResponse(actualFieldValue, examineeAttributes);

        when(studentService.getStudentByStudentSSID(actualFieldValue)).thenThrow(NotFoundException.class);

        // Act
        underTest.analyze(individualResponse);

        // Assert
        CellCategory actualCellCategory = getActualCellCategory(EnumExamineeAttributeFieldName.StudentIdentifier, individualResponse);
        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .tdsFieldName(EnumExamineeAttributeFieldName.StudentIdentifier.name())
                .tdsFieldNameValue(actualFieldValue)
                .tdsExpectedValue(expectedFieldValue)
                .fieldEmpty(false)
                .correctDataType(false)
                .acceptableValue(false)
                .correctWidth(true)
                .correctValue(false)
                .optionalFieldValue(false)
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .toCellCategory();

        assertEquals(expectedCellCategory, actualCellCategory);
    }

    @Test
    public void whenStudentIdentifierMatchesStudent() {
        final String actualFieldValue = SSID;
        final String expectedFieldValue = SSID;
        final Student mockStudent = new StudentBuilder(SSID)
                .toStudent();

        // Arrange
        final List<TDSReport.Examinee.ExamineeAttribute> examineeAttributes = Lists.newArrayList(
                new ExamineeAttributeBuilder()
                        .name(EnumExamineeAttributeFieldName.StudentIdentifier.name())
                        .value(actualFieldValue)
                        .context(Context.FINAL)
                        .toExamineeAttribute());

        final IndividualResponse individualResponse = generateIndividualResponse(SSID, examineeAttributes);

        when(studentService.getStudentByStudentSSID(SSID)).thenReturn(mockStudent);

        // Act
        underTest.analyze(individualResponse);

        // Assert
        CellCategory actualCellCategory = getActualCellCategory(EnumExamineeAttributeFieldName.StudentIdentifier, individualResponse);
        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .correctValue(true)
                .correctDataType(true)
                .acceptableValue(true)
                .correctWidth(true)
                .fieldEmpty(false)
                .optionalFieldValue(false)
                .tdsExpectedValue(expectedFieldValue)
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .tdsFieldName(EnumExamineeAttributeFieldName.StudentIdentifier.name())
                .tdsFieldNameValue(actualFieldValue)
                .toCellCategory();

        assertEquals(expectedCellCategory, actualCellCategory);
    }
    //endregion

    //region AlternateSSID
    @Test
    public void whenAlternateSSIDHasTooManyCharacters() {
        final String actualFieldValue = "123456789012345678901234567890123456789012345678901234567890";
        final String expectedFieldValue = "AlternateSSID";
        final Student mockStudent = new StudentBuilder(SSID)
                .alternateSSID(expectedFieldValue)
                .toStudent();

        testMaxWidthError(EnumExamineeAttributeFieldName.AlternateSSID, mockStudent,
                expectedFieldValue, actualFieldValue, true);
    }

    @Test
    public void whenAlternateSSIDHasUnacceptableValue() {
        final String actualFieldValue = "\u0082";
        final String expectedFieldValue = "AlternateSSID";
        final Student mockStudent = new StudentBuilder(SSID)
                .alternateSSID(expectedFieldValue)
                .toStudent();

        testUnacceptableValue(EnumExamineeAttributeFieldName.AlternateSSID,
                mockStudent, expectedFieldValue, actualFieldValue, true);
    }

    @Test
    public void whenOptionalAlternateSSIDNameIsBlank() {
        final String actualFieldValue = "";
        final String expectedFieldValue = "";
        final Student mockStudent = new StudentBuilder(SSID)
                .alternateSSID(expectedFieldValue)
                .toStudent();

        testOptionalField(EnumExamineeAttributeFieldName.AlternateSSID, mockStudent,
                expectedFieldValue, actualFieldValue);
    }

    @Test
    public void whenAlternateSSIDMatchesStudent() {
        final String actualFieldValue = "TestAlternateSSID";
        final String expectedFieldValue = "TestAlternateSSID";
        final Student mockStudent = new StudentBuilder(SSID)
                .alternateSSID(expectedFieldValue)
                .toStudent();

        testMatchesStudent(EnumExamineeAttributeFieldName.AlternateSSID, mockStudent,
                expectedFieldValue, actualFieldValue, true);
    }
    //endregion

    //region GradeLevelWhenAssessed
    @Test
    public void whenGradeLevelWhenAssessedIsLessThanTwoCharacters() {

        // Configure incorrect grade level using only a single digit
        final List<TDSReport.Examinee.ExamineeAttribute> examineeAttributes = Lists.newArrayList(
                new ExamineeAttributeBuilder()
                        .name(EnumExamineeAttributeFieldName.GradeLevelWhenAssessed.name())
                        .value("3")
                        .context(Context.FINAL)
                        .toExamineeAttribute());

        final IndividualResponse individualResponse = generateIndividualResponse(SSID, examineeAttributes);

        when(studentService.getStudentByStudentSSID(SSID)).thenReturn(new StudentBuilder(SSID)
                .gradeLevelWhenAssessed("03")
                .toStudent());

        // Act
        underTest.analyze(individualResponse);

        // Assert
        CellCategory actualCellCategory = getActualCellCategory(EnumExamineeAttributeFieldName.GradeLevelWhenAssessed, individualResponse);
        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .tdsFieldName(EnumExamineeAttributeFieldName.GradeLevelWhenAssessed.name())
                .tdsFieldNameValue("3")
                .tdsExpectedValue("03")
                .fieldEmpty(false)
                .correctDataType(true)
                .acceptableValue(false)
                .correctWidth(false)
                .correctValue(false)
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .toCellCategory();

        assertEquals(expectedCellCategory, actualCellCategory);
    }

    @Test
    public void whenGradeLevelWhenAssessedIsTwoCharacters() {

        // Arrange
        final List<TDSReport.Examinee.ExamineeAttribute> examineeAttributes = Lists.newArrayList(
                new ExamineeAttributeBuilder()
                        .name(EnumExamineeAttributeFieldName.GradeLevelWhenAssessed.name())
                        .value("03")
                        .context(Context.FINAL)
                        .toExamineeAttribute());

        final IndividualResponse individualResponse = generateIndividualResponse(SSID, examineeAttributes);

        when(studentService.getStudentByStudentSSID(SSID)).thenReturn(new StudentBuilder(SSID)
                .gradeLevelWhenAssessed("03")
                .toStudent());

        // Act
        underTest.analyze(individualResponse);

        // Assert
        CellCategory actualCellCategory = getActualCellCategory(EnumExamineeAttributeFieldName.GradeLevelWhenAssessed, individualResponse);
        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .tdsFieldName(EnumExamineeAttributeFieldName.GradeLevelWhenAssessed.name())
                .tdsFieldNameValue("03")
                .tdsExpectedValue("03")
                .fieldEmpty(false)
                .correctDataType(true)
                .acceptableValue(true)
                .correctWidth(true)
                .correctValue(true)
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .toCellCategory();

        assertEquals(expectedCellCategory, actualCellCategory);
    }

    @Test
    public void whenGradeLevelWhenAssessedHasTooManyCharacters() {
        final String actualFieldValue = "1234567890";
        final String expectedFieldValue = "03";
        final Student mockStudent = new StudentBuilder(SSID)
                .gradeLevelWhenAssessed(expectedFieldValue)
                .toStudent();

        // Arrange
        final List<TDSReport.Examinee.ExamineeAttribute> examineeAttributes = Lists.newArrayList(
                new ExamineeAttributeBuilder()
                        .name(EnumExamineeAttributeFieldName.GradeLevelWhenAssessed.name())
                        .value(actualFieldValue)
                        .context(Context.FINAL)
                        .toExamineeAttribute());

        final IndividualResponse individualResponse = generateIndividualResponse(SSID, examineeAttributes);

        when(studentService.getStudentByStudentSSID(SSID)).thenReturn(mockStudent);

        // Act
        underTest.analyze(individualResponse);

        // Assert
        CellCategory actualCellCategory = getActualCellCategory(EnumExamineeAttributeFieldName.GradeLevelWhenAssessed, individualResponse);
        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .tdsFieldName(EnumExamineeAttributeFieldName.GradeLevelWhenAssessed.name())
                .tdsFieldNameValue(actualFieldValue)
                .tdsExpectedValue(expectedFieldValue)
                .fieldEmpty(false)
                .correctDataType(true)
                .acceptableValue(false)
                .correctWidth(false)
                .correctValue(false)
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .toCellCategory();

        assertEquals(expectedCellCategory, actualCellCategory);
    }

    @Test
    public void whenGradeLevelWhenAssessedHasUnacceptableValue() {
        final String actualFieldValue = "42";
        final String expectedFieldValue = "03";
        final Student mockStudent = new StudentBuilder(SSID)
                .gradeLevelWhenAssessed(expectedFieldValue)
                .toStudent();

        // Arrange
        final List<TDSReport.Examinee.ExamineeAttribute> examineeAttributes = Lists.newArrayList(
                new ExamineeAttributeBuilder()
                        .name(EnumExamineeAttributeFieldName.GradeLevelWhenAssessed.name())
                        .value(actualFieldValue)
                        .context(Context.FINAL)
                        .toExamineeAttribute());

        final IndividualResponse individualResponse = generateIndividualResponse(SSID, examineeAttributes);

        when(studentService.getStudentByStudentSSID(SSID)).thenReturn(mockStudent);

        // Act
        underTest.analyze(individualResponse);

        // Assert
        CellCategory actualCellCategory = getActualCellCategory(EnumExamineeAttributeFieldName.GradeLevelWhenAssessed, individualResponse);
        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .tdsFieldName(EnumExamineeAttributeFieldName.GradeLevelWhenAssessed.name())
                .tdsFieldNameValue(actualFieldValue)
                .tdsExpectedValue(expectedFieldValue)
                .fieldEmpty(false)
                .correctDataType(true)
                .acceptableValue(false)
                .correctWidth(true)
                .correctValue(false)
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .toCellCategory();

        assertEquals(expectedCellCategory, actualCellCategory);
    }

    @Test
    public void whenGradeLevelWhenAssessedMatchesStudent() {
        final String actualFieldValue = "03";
        final String expectedFieldValue = "03";
        final Student mockStudent = new StudentBuilder(SSID)
                .gradeLevelWhenAssessed(expectedFieldValue)
                .toStudent();

        testMatchesStudent(EnumExamineeAttributeFieldName.GradeLevelWhenAssessed, mockStudent,
                expectedFieldValue, actualFieldValue, false);
    }

    @Test
    public void whenGradeLevelWhenAssessedFieldIsMissing() {
        final String actualFieldValue = null;
        final String expectedFieldValue = "03";
        final Student mockStudent = new StudentBuilder(SSID)
                .gradeLevelWhenAssessed(expectedFieldValue)
                .toStudent();

        // Arrange
        final List<TDSReport.Examinee.ExamineeAttribute> examineeAttributes = Lists.newArrayList();

        final IndividualResponse individualResponse = generateIndividualResponse(SSID, examineeAttributes);

        when(studentService.getStudentByStudentSSID(SSID)).thenReturn(mockStudent);

        // Act
        underTest.analyze(individualResponse);

        // Assert
        CellCategory actualCellCategory = getActualCellCategory(EnumExamineeAttributeFieldName.GradeLevelWhenAssessed, individualResponse);
        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .tdsFieldName(EnumExamineeAttributeFieldName.GradeLevelWhenAssessed.name())
                .tdsFieldNameValue(actualFieldValue)
                .tdsExpectedValue(expectedFieldValue)
                .fieldEmpty(true)
                .fieldMissing(true)
                .correctDataType(false)
                .acceptableValue(false)
                .correctWidth(false)
                .correctValue(false)
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .toCellCategory();

        assertEquals(expectedCellCategory, actualCellCategory);
    }
    //endregion
    
    /**
     * Verifies that the fields of an Examinee are marked as having incorrect value when the Student Identifier doesn't match
     * an IRP student.
     */
    @Test
    public void whenStudentIdentifierDoesNotMatchExistingIRPStudent_IncorrectField() {

        // Arrange
        final List<TDSReport.Examinee.ExamineeAttribute> examineeAttributes = Lists.newArrayList(
                new ExamineeAttributeBuilder()
                        .name(EnumExamineeAttributeFieldName.AmericanIndianOrAlaskaNative.name())
                        .value("N")
                        .context(Context.FINAL)
                        .toExamineeAttribute());
        final IndividualResponse individualResponse = generateIndividualResponse(SSID, examineeAttributes);
      
        when(studentService.getStudentByStudentSSID(SSID)).thenThrow(new NotFoundException("test"));

        // Act
        underTest.analyze(individualResponse);

        // Assert
        CellCategory actualCellCategory = getActualCellCategory(EnumExamineeAttributeFieldName.AmericanIndianOrAlaskaNative, individualResponse);
        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .correctValue(false)
                .correctDataType(true)
                .acceptableValue(true)
                .correctWidth(true)
                .tdsExpectedValue(null)
                .fieldEmpty(false)
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .tdsFieldName(EnumExamineeAttributeFieldName.AmericanIndianOrAlaskaNative.name())
                .tdsFieldNameValue("N")
                .toCellCategory();

        assertEquals(expectedCellCategory, actualCellCategory);
    }

    /**
     * The Open Test System has a bug where there is a typo in the EnglishLanguageProficiencyLevel attribute.
     * IRP handles this case.
     */
    @Test
    public void whenEnglishLanguageProficiencLevel_Typo(){

        final List<TDSReport.Examinee.ExamineeAttribute> examineeAttributes = Lists.newArrayList(
                new ExamineeAttributeBuilder()
                        .name(EnumExamineeAttributeFieldName.EnglishLanguageProficiencLevel.name())
                        .value("progresS")
                        .context(Context.FINAL)
                        .toExamineeAttribute());
        
        final IndividualResponse individualResponse = generateIndividualResponse(SSID, examineeAttributes);
      
        when(studentService.getStudentByStudentSSID(SSID)).thenReturn(new StudentBuilder(SSID)
			.englishLanguageProficiencyLevel("PROGRESS")
			.toStudent());
        
        // Act
        underTest.analyze(individualResponse);
        
        // Assert
        CellCategory actualCellCategory = getActualCellCategory(EnumExamineeAttributeFieldName.EnglishLanguageProficiencLevel, individualResponse);
        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .tdsFieldName(EnumExamineeAttributeFieldName.EnglishLanguageProficiencLevel.name())
                .tdsFieldNameValue("progresS")
                .tdsExpectedValue("PROGRESS")
                .fieldEmpty(false)
                .correctDataType(true)
                .acceptableValue(true)
                .correctWidth(true)
                .correctValue(true)
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .toCellCategory();
        
        assertEquals(expectedCellCategory, actualCellCategory);

    }

    void testMaxWidthError(EnumExamineeAttributeFieldName examineeAttributeFieldName,
                           Student mockStudent,
                           String expectedFieldValue, String actualFieldValue,
                           boolean isOptionalFieldValue) {

        // Arrange
        final List<TDSReport.Examinee.ExamineeAttribute> examineeAttributes = Lists.newArrayList(
                new ExamineeAttributeBuilder()
                        .name(examineeAttributeFieldName.name())
                        .value(actualFieldValue)
                        .context(Context.FINAL)
                        .toExamineeAttribute());

        final IndividualResponse individualResponse = generateIndividualResponse(SSID, examineeAttributes);

        when(studentService.getStudentByStudentSSID(SSID)).thenReturn(mockStudent);

        // Act
        underTest.analyze(individualResponse);

        // Assert
        CellCategory actualCellCategory = getActualCellCategory(examineeAttributeFieldName, individualResponse);
        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .tdsFieldName(examineeAttributeFieldName.name())
                .tdsFieldNameValue(actualFieldValue)
                .tdsExpectedValue(expectedFieldValue)
                .fieldEmpty(false)
                .correctDataType(true)
                .acceptableValue(true)
                .correctWidth(false)
                .correctValue(false)
                .optionalFieldValue(isOptionalFieldValue)
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .toCellCategory();

        assertEquals(expectedCellCategory, actualCellCategory);
    }

    void testUnacceptableValue(EnumExamineeAttributeFieldName examineeAttributeFieldName,
                               Student mockStudent,
                               String expectedFieldValue, String actualFieldValue,
                               boolean isOptionalFieldValue) {
        // Arrange
        final List<TDSReport.Examinee.ExamineeAttribute> examineeAttributes = Lists.newArrayList(
                new ExamineeAttributeBuilder()
                        .name(examineeAttributeFieldName.name())
                        .value(actualFieldValue)
                        .context(Context.FINAL)
                        .toExamineeAttribute());

        final IndividualResponse individualResponse = generateIndividualResponse(SSID, examineeAttributes);

        when(studentService.getStudentByStudentSSID(SSID)).thenReturn(mockStudent);

        // Act
        underTest.analyze(individualResponse);

        // Assert
        CellCategory actualCellCategory = getActualCellCategory(examineeAttributeFieldName, individualResponse);
        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .tdsFieldName(examineeAttributeFieldName.name())
                .tdsFieldNameValue(actualFieldValue)
                .tdsExpectedValue(expectedFieldValue)
                .fieldEmpty(false)
                .correctDataType(false)
                .acceptableValue(false)
                .correctWidth(true)
                .correctValue(false)
                .optionalFieldValue(isOptionalFieldValue)
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .toCellCategory();

        assertEquals(expectedCellCategory, actualCellCategory);
    }

    void testOptionalField(EnumExamineeAttributeFieldName examineeAttributeFieldName,
                           Student mockStudent,
                           String expectedFieldValue, String actualFieldValue) {
        // Arrange
        final List<TDSReport.Examinee.ExamineeAttribute> examineeAttributes = Lists.newArrayList(
                new ExamineeAttributeBuilder()
                        .name(examineeAttributeFieldName.name())
                        .value(actualFieldValue)
                        .context(Context.FINAL)
                        .toExamineeAttribute());

        final IndividualResponse individualResponse = generateIndividualResponse(SSID, examineeAttributes);

        when(studentService.getStudentByStudentSSID(SSID)).thenReturn(mockStudent);

        // Act
        underTest.analyze(individualResponse);

        // Assert
        CellCategory actualCellCategory = getActualCellCategory(examineeAttributeFieldName, individualResponse);
        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .optionalFieldValue(true)
                .correctValue(true)
                .correctDataType(true)
                .acceptableValue(true)
                .correctWidth(true)
                .tdsExpectedValue(expectedFieldValue)
                .fieldEmpty(true)
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .tdsFieldName(examineeAttributeFieldName.name())
                .tdsFieldNameValue(actualFieldValue)
                .toCellCategory();

        assertEquals(expectedCellCategory, actualCellCategory);
    }

    void testMatchesStudent(EnumExamineeAttributeFieldName examineeAttributeFieldName,
                            Student mockStudent,
                            String expectedFieldValue, String actualFieldValue,
                            boolean isOptionalFieldValue) {
        // Arrange
        final List<TDSReport.Examinee.ExamineeAttribute> examineeAttributes = Lists.newArrayList(
                new ExamineeAttributeBuilder()
                        .name(examineeAttributeFieldName.name())
                        .value(actualFieldValue)
                        .context(Context.FINAL)
                        .toExamineeAttribute());

        final IndividualResponse individualResponse = generateIndividualResponse(SSID, examineeAttributes);

        when(studentService.getStudentByStudentSSID(SSID)).thenReturn(mockStudent);

        // Act
        underTest.analyze(individualResponse);

        // Assert
        CellCategory actualCellCategory = getActualCellCategory(examineeAttributeFieldName, individualResponse);

        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .correctValue(true)
                .correctDataType(true)
                .acceptableValue(true)
                .correctWidth(true)
                .fieldEmpty(false)
                .optionalFieldValue(isOptionalFieldValue)
                .tdsExpectedValue(expectedFieldValue)
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .tdsFieldName(examineeAttributeFieldName.name())
                .tdsFieldNameValue(actualFieldValue)
                .toCellCategory();

        assertEquals(expectedCellCategory, actualCellCategory);
    }

    CellCategory getActualCellCategory(EnumExamineeAttributeFieldName examineeAttributeFieldName, IndividualResponse individualResponse) {
        for (ExamineeAttributeCategory examineeAttributeCategory : individualResponse.getExamineeAttributeCategories()) {
            for (CellCategory cellCategory : examineeAttributeCategory.getCellCategories()) {
                if (cellCategory.getTdsFieldName() == examineeAttributeFieldName.toString()) {
                    return cellCategory;
                }
            }
        }

        return null;
    }

    /**
     * Constructs a list of all the ExamineeAttributes
     *
     * @return
     */
    private List<TDSReport.Examinee.ExamineeAttribute> generateAllExamineeAttributes() {
        final List<TDSReport.Examinee.ExamineeAttribute> examineeAttributes = Lists.newArrayList(
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(EnumExamineeAttributeFieldName.AlternateSSID.name())
                        .value("8888")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(EnumExamineeAttributeFieldName.AmericanIndianOrAlaskaNative.name())
                        .value("No")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(EnumExamineeAttributeFieldName.Asian.name())
                        .value("No")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(EnumExamineeAttributeFieldName.Birthdate.name())
                        .value("01202000")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(EnumExamineeAttributeFieldName.BlackOrAfricanAmerican.name())
                        .value("No")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(EnumExamineeAttributeFieldName.DemographicRaceTwoOrMoreRaces.name())
                        .value("None")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(EnumExamineeAttributeFieldName.EconomicDisadvantageStatus.name())
                        .value("None")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(EnumExamineeAttributeFieldName.EnglishLanguageProficiencyLevel.name())
                        .value("10")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(EnumExamineeAttributeFieldName.FirstEntryDateIntoUSSchool.name())
                        .value("2001")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(EnumExamineeAttributeFieldName.FirstName.name())
                        .value("TestFirstName")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(EnumExamineeAttributeFieldName.GradeLevelWhenAssessed.name())
                        .value("10")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(EnumExamineeAttributeFieldName.HispanicOrLatinoEthnicity.name())
                        .value("Yes")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(EnumExamineeAttributeFieldName.IDEAIndicator.name())
                        .value("No")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(EnumExamineeAttributeFieldName.LanguageCode.name())
                        .value("EN")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(EnumExamineeAttributeFieldName.LastOrSurname.name())
                        .value("TestLastName")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(EnumExamineeAttributeFieldName.LEPExitDate.name())
                        .value("")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(EnumExamineeAttributeFieldName.LEPStatus.name())
                        .value("false")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(EnumExamineeAttributeFieldName.LimitedEnglishProficiencyEntryDate.name())
                        .value("")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(EnumExamineeAttributeFieldName.MiddleName.name())
                        .value("TestMiddleName")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(EnumExamineeAttributeFieldName.MigrantStatus.name())
                        .value("None")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(EnumExamineeAttributeFieldName.NativeHawaiianOrOtherPacificIslander.name())
                        .value("No")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(EnumExamineeAttributeFieldName.PrimaryDisabilityType.name())
                        .value("None")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(EnumExamineeAttributeFieldName.Section504Status.name())
                        .value("No")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(EnumExamineeAttributeFieldName.Sex.name())
                        .value("M")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(EnumExamineeAttributeFieldName.StudentIdentifier.name())
                        .value("StudentID")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(EnumExamineeAttributeFieldName.TitleIIILanguageInstructionProgramType.name())
                        .value("")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(EnumExamineeAttributeFieldName.White.name())
                        .value("No")
                        .toExamineeAttribute());

        return examineeAttributes;
    }
}