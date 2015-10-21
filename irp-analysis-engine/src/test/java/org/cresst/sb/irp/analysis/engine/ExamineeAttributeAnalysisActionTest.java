package org.cresst.sb.irp.analysis.engine;

import builders.CellCategoryBuilder;
import builders.ExamineeAttributeBuilder;
import builders.StudentBuilder;

import com.google.common.collect.Lists;

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
     * @param examineeKey
     * @param examineeAttributes
     * @return
     */
    private IndividualResponse generateIndividualResponse(Long examineeKey,
                                                          List<TDSReport.Examinee.ExamineeAttribute> examineeAttributes) {

        final TDSReport.Examinee examinee = new TDSReport.Examinee();
        examinee.setKey(examineeKey);

        if (examineeAttributes != null) {
            examinee.getExamineeAttributeOrExamineeRelationship().addAll(examineeAttributes);
        }

        final TDSReport tdsReport = new TDSReport();
        tdsReport.setExaminee(examinee);

        final IndividualResponse individualResponse = new IndividualResponse();
        individualResponse.setTDSReport(tdsReport);

        
        return individualResponse;
    }

    @Test
    public void testAnalyze() throws Exception {

        final IndividualResponse individualResponse = generateIndividualResponse(9999L, generateAllExamineeAttributes());
   
        when(studentService.getStudentByStudentSSID(9999L)).thenReturn(new StudentBuilder(9999L)
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
                .studentIdentifier("9990001")
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
        final long SSID = 9999L;
        final List<TDSReport.Examinee.ExamineeAttribute> examineeAttributes = Lists.newArrayList(
                new ExamineeAttributeBuilder()
                	.name(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.Birthdate.name())
                        .value("2002-10-15")
                        .context(Context.FINAL)
                        .toExamineeAttribute());
        
        final IndividualResponse individualResponse = generateIndividualResponse(SSID, examineeAttributes);

        when(studentService.getStudentByStudentSSID(SSID)).thenReturn(new StudentBuilder(9999L)
        		.birthdate("2002-10-15")
        		.toStudent());
        
        // Act
        underTest.analyze(individualResponse);
        
        // Assert
        List<CellCategory> actualCellCategories = individualResponse.getExamineeAttributeCategories().get(0).getCellCategories();
        CellCategory expectedCellCategory = new CellCategoryBuilder()
	        .correctValue(true)
	        .correctDataType(true)
	        .acceptableValue(true)
	        .tdsExpectedValue("2002-10-15")
	        .isFieldEmpty(false)
	        .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
	        .tdsFieldName(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.Birthdate.name())
	        .tdsFieldNameValue("2002-10-15")
	        .toCellCategory();
        
        assertEquals(expectedCellCategory, actualCellCategories.get(0));
        
    }
    
    /**
     * Verifies that the fields of an Examinee are marked as having an error when the Examinee key doesn't match
     * an IRP student.
     */
    @Test
    public void whenExamineeKeyDoesNotMatchExistingIRPStudent_IncorrectField() {

        // Arrange
        final long SSID = 9999L;
        final List<TDSReport.Examinee.ExamineeAttribute> examineeAttributes = Lists.newArrayList(
                new ExamineeAttributeBuilder()
                        .name(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.AmericanIndianOrAlaskaNative.name())
                        .value("N")
                        .context(Context.FINAL)
                        .toExamineeAttribute());
        final IndividualResponse individualResponse = generateIndividualResponse(SSID, examineeAttributes);
      
        when(studentService.getStudentByStudentSSID(SSID)).thenThrow(new NotFoundException("test"));

        // Act
        underTest.analyze(individualResponse);

        // Assert
        List<CellCategory> actualCellCategories = individualResponse.getExamineeAttributeCategories().get(0).getCellCategories();
        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .correctValue(false)
                .correctDataType(true)
                .acceptableValue(true)
                .tdsExpectedValue(null)
                .isFieldEmpty(false)
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .tdsFieldName(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.AmericanIndianOrAlaskaNative.name())
                .tdsFieldNameValue("N")
                .toCellCategory();

        assertEquals(expectedCellCategory, actualCellCategories.get(0));
    }

    @Test
    public void whenEnglishLanguageProficiencLevel_Typo(){
    	
        final long SSID = 9999L;
        
        final List<TDSReport.Examinee.ExamineeAttribute> examineeAttributes = Lists.newArrayList(
                new ExamineeAttributeBuilder()
                	.name(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.EnglishLanguageProficiencLevel.name())
                    .value("progresS")
                    .context(Context.FINAL)
                    .toExamineeAttribute());
        
        final IndividualResponse individualResponse = generateIndividualResponse(SSID, examineeAttributes);
      
        when(studentService.getStudentByStudentSSID(SSID)).thenReturn(new StudentBuilder(9999L)
			.englishLanguageProficiencyLevel("PROGRESS")
			.toStudent());
        
        // Act
        underTest.analyze(individualResponse);
        
        // Assert
        List<CellCategory> actualCellCategories = individualResponse.getExamineeAttributeCategories().get(0).getCellCategories();
        CellCategory expectedCellCategory = new CellCategoryBuilder()
	        .tdsFieldName(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.EnglishLanguageProficiencLevel.name())
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
                        .name(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.AlternateSSID.name())
                        .value("8888")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.AmericanIndianOrAlaskaNative.name())
                        .value("No")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.Asian.name())
                        .value("No")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.Birthdate.name())
                        .value("01202000")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.BlackOrAfricanAmerican.name())
                        .value("No")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.DemographicRaceTwoOrMoreRaces.name())
                        .value("None")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.EconomicDisadvantageStatus.name())
                        .value("None")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.EnglishLanguageProficiencyLevel.name())
                        .value("10")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.FirstEntryDateIntoUSSchool.name())
                        .value("2001")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.FirstName.name())
                        .value("TestFirstName")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.GradeLevelWhenAssessed.name())
                        .value("10")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.HispanicOrLatinoEthnicity.name())
                        .value("Yes")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.IDEAIndicator.name())
                        .value("No")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.LanguageCode.name())
                        .value("EN")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.LastOrSurname.name())
                        .value("TestLastName")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.LEPExitDate.name())
                        .value("")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.LEPStatus.name())
                        .value("false")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.LimitedEnglishProficiencyEntryDate.name())
                        .value("")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.MiddleName.name())
                        .value("TestMiddleName")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.MigrantStatus.name())
                        .value("None")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.NativeHawaiianOrOtherPacificIslander.name())
                        .value("No")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.PrimaryDisabilityType.name())
                        .value("None")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.Section504Status.name())
                        .value("No")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.Sex.name())
                        .value("M")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                		.context(Context.FINAL)
                		.name(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.StudentIdentifier.name()) 
                		.value("9990001")
                		.toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.TitleIIILanguageInstructionProgramType.name())
                        .value("")
                        .toExamineeAttribute(),
                new ExamineeAttributeBuilder()
                        .context(Context.FINAL)
                        .name(ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName.White.name())
                        .value("No")
                        .toExamineeAttribute());

        return examineeAttributes;
    }
}