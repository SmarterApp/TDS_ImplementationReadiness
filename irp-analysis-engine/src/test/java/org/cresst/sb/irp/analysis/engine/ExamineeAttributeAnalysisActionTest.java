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
import org.cresst.sb.irp.domain.tdsreport.Context;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.cresst.sb.irp.service.StudentService;
import org.junit.Assert;
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
    

    @Test
    public void whenBirthdateMatchesStudent_CorrectField() throws Exception {
    	
        // Arrange
        final String SSID = "StudentID";
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
        List<CellCategory> actualCellCategories = individualResponse.getExamineeAttributeCategories().get(1).getCellCategories();
        CellCategory expectedCellCategory = new CellCategoryBuilder()
	        .correctValue(true)
	        .correctDataType(true)
	        .acceptableValue(true)
	        .tdsExpectedValue("2002-10-15")
	        .isFieldEmpty(false)
	        .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
	        .tdsFieldName(EnumExamineeAttributeFieldName.Birthdate.name())
	        .tdsFieldNameValue("2002-10-15")
	        .toCellCategory();
        
        assertEquals(expectedCellCategory, actualCellCategories.get(0));
        
    }
    
    /**
     * Verifies that the fields of an Examinee are marked as having an error when the Student Identifier doesn't match
     * an IRP student.
     */
    @Test
    public void whenStudentIdentifierDoesNotMatchExistingIRPStudent_IncorrectField() {

        // Arrange
        final String SSID = "StudentID";
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
        List<CellCategory> actualCellCategories = individualResponse.getExamineeAttributeCategories().get(1).getCellCategories();
        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .correctValue(false)
                .correctDataType(true)
                .acceptableValue(true)
                .tdsExpectedValue(null)
                .isFieldEmpty(false)
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .tdsFieldName(EnumExamineeAttributeFieldName.AmericanIndianOrAlaskaNative.name())
                .tdsFieldNameValue("N")
                .toCellCategory();

        assertEquals(expectedCellCategory, actualCellCategories.get(0));
    }

    @Test
    public void whenEnglishLanguageProficiencLevel_Typo(){
    	
        final String SSID = "9999";
        
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
        List<CellCategory> actualCellCategories = individualResponse.getExamineeAttributeCategories().get(1).getCellCategories();
        CellCategory expectedCellCategory = new CellCategoryBuilder()
	        .tdsFieldName(EnumExamineeAttributeFieldName.EnglishLanguageProficiencLevel.name())
	        .tdsFieldNameValue("progresS")
	        .tdsExpectedValue("PROGRESS")    
	        .isFieldEmpty(false)
	        .correctDataType(true)
	        .acceptableValue(true)
	        .correctValue(true)
	        .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
	        .toCellCategory();
        
        assertEquals(expectedCellCategory, actualCellCategories.get(0));

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