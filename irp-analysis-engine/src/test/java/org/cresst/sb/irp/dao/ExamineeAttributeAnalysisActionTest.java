package org.cresst.sb.irp.dao;

import builders.ExamineeAttributeBuilder;
import builders.StudentBuilder;
import com.google.common.collect.Lists;
import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.ExamineeAttributeCategory;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.tdsreport.Context;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.service.StudentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.cresst.sb.irp.dao.ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName;
import static org.hamcrest.CoreMatchers.is;
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

        assertThat(actualCellCategories.size(), is(26));
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