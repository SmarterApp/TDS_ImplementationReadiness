package org.cresst.sb.irp.dao;

import builders.CellCategoryBuilder;
import builders.StudentBuilder;
import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.cresst.sb.irp.service.StudentService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExamineeAnalysisActionTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    ExamineeAnalysisAction underTest = new ExamineeAnalysisAction();

    private IndividualResponse createIndividualResponse(Long examineeKey) {
        final TDSReport.Examinee examinee = new TDSReport.Examinee();
        examinee.setKey(examineeKey);

        final TDSReport tdsReport = new TDSReport();
        tdsReport.setExaminee(examinee);

        final IndividualResponse individualResponse = new IndividualResponse();
        individualResponse.setTDSReport(tdsReport);

        return individualResponse;
    }

    /**
     * Verify that when the Examinee key matches an existing IRP Student that the field is marked as correct
     * @throws Exception
     */
    @Test
    public void whenExamineeKeyMatchesIRPStudentSSID_CorrectKeyValue() throws Exception {

        // Arrange
        final IndividualResponse individualResponse = createIndividualResponse(9999L);

        when(studentService.getStudentByStudentSSID(9999L)).thenReturn(new StudentBuilder(9999L).toStudent());

        // Act
        underTest.analyze(individualResponse);

        // We are testing a single attribute so there should only be one CellCategory
        final CellCategory actualCellCategory = individualResponse.getExamineeCategory().getCellCategories().get(0);

        // Setup expected results after analysis that indicates the field has an incorrect value
        final CellCategory expectedCellCategory = new CellCategoryBuilder()
                .tdsFieldName(ExamineeAnalysisAction.EnumExamineeFieldName.key.toString())
                .tdsFieldNameValue("9999")
                .tdsExpectedValue("9999")
                .correctValue(true)
                .correctDataType(true)
                .acceptableValue(true)
                .isFieldEmpty(false)
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .toCellCategory();

        // Assert
        Assert.assertEquals(expectedCellCategory, actualCellCategory);
    }

    /**
     * Verify the Examinee key is analyzed when a matching IRP Student doesn't exist.
     * @throws Exception
     */
    @Test
    public void whenExamineeKeyNotIRPStudentSSID_IncorrectKeyValue() throws Exception {

        // Arrange
        // Create an IndividualResponse with a TDS Report Examinee that has a key that which does not exist
        final IndividualResponse individualResponse = createIndividualResponse(1000L);

        // The Examinee with key 1000 does not exist in this test scenario
        when(studentService.getStudentByStudentSSID(1000L)).thenThrow(new NotFoundException("test"));

        // Act
        underTest.analyze(individualResponse);

        // We are testing a single attribute so there should only be one CellCategory
        final CellCategory actualCellCategory = individualResponse.getExamineeCategory().getCellCategories().get(0);

        // Setup expected results after analysis that indicates the field has an incorrect value
        final CellCategory expectedCellCategory = new CellCategoryBuilder()
                .tdsFieldName(ExamineeAnalysisAction.EnumExamineeFieldName.key.toString())
                .tdsFieldNameValue("1000")
                .correctValue(false)
                .correctDataType(true)
                .isFieldEmpty(false)
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .acceptableValue(true)
                .toCellCategory();

        // Assert
        Assert.assertEquals(expectedCellCategory, actualCellCategory);
    }

    /**
     * Verify that when the TDS Report doesn't have an Examinee that an empty, non-null ExamineeCategory still exists.
     * This is so consumers of the IndividualResponse don't have to check the existence of the Category.
     */
    @Test
    public void whenExamineeIsNull_EmptyCellCategories() throws Exception {

        // Arrange
        final TDSReport tdsReport = new TDSReport();

        final IndividualResponse individualResponse = new IndividualResponse();
        individualResponse.setTDSReport(tdsReport);

        // Act
        underTest.analyze(individualResponse);

        // Assert
        Assert.assertThat(individualResponse.getExamineeCategory().getCellCategories().size(), is(0));
    }

    /**
     * Verify that when the Examinee key doesn't exist the field is marked as being invalid
     */
    @Test
    public void whenExamineeKeyIsNull_InvalidField() throws Exception {

        // Arrange
        // Create an Examinee without a key
        final IndividualResponse individualResponse = createIndividualResponse(null);

        // Act
        underTest.analyze(individualResponse);

        // We are testing a single attribute so there should only be one CellCategory
        final CellCategory actualCellCategory = individualResponse.getExamineeCategory().getCellCategories().get(0);

        // Setup expected results after analysis that indicates the field has an incorrect value
        final CellCategory expectedCellCategory = new CellCategoryBuilder()
                .tdsFieldName(ExamineeAnalysisAction.EnumExamineeFieldName.key.toString())
                .isFieldEmpty(true)
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .toCellCategory();

        // Assert
        Assert.assertEquals(expectedCellCategory, actualCellCategory);
    }
}