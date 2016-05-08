package org.cresst.sb.irp.analysis.engine;

import java.util.List;

import builders.CellCategoryBuilder;
import builders.ExamineeAttributeBuilder;
import builders.TestStudentMappingBuilder;

import org.cresst.sb.irp.analysis.engine.examinee.EnumExamineeAttributeFieldName;
import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.TestPropertiesCategory;
import org.cresst.sb.irp.domain.tdsreport.Context;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.service.TestStudentMappingService;
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
	private TestStudentMappingService testStudentMappingService;

    @InjectMocks
    ExamineeAnalysisAction underTest = new ExamineeAnalysisAction();

    private IndividualResponse createIndividualResponse(Long examineeKey, String studentIdentifier, String testName) {

        final TDSReport.Examinee.ExamineeAttribute studentIdentifierAttribute = new ExamineeAttributeBuilder()
                .name(EnumExamineeAttributeFieldName.StudentIdentifier.name())
                .value(studentIdentifier)
                .context(Context.FINAL)
                .toExamineeAttribute();

        final TDSReport.Examinee examinee = new TDSReport.Examinee();
        examinee.setKey(examineeKey);
        examinee.getExamineeAttributeOrExamineeRelationship().add(studentIdentifierAttribute);

        final TDSReport tdsReport = new TDSReport();
        tdsReport.setExaminee(examinee);

        final IndividualResponse individualResponse = new IndividualResponse();
        individualResponse.setTDSReport(tdsReport);

    	final CellCategory cellCategory = new CellCategory();
		cellCategory.setTdsFieldName("name");
		cellCategory.setTdsFieldNameValue(testName);
        
        TestPropertiesCategory testPropertiesCategory = new TestPropertiesCategory();
        testPropertiesCategory.addCellCategory(cellCategory);
    	individualResponse.setTestPropertiesCategory(testPropertiesCategory);
        
        return individualResponse;
    }

    /**
     * Verify that when the Test name and Student ID match an existing IRP TestStudentMapping, then the Examinee is valid.
     *
     * @throws Exception
     */
    @Test
    public void whenTestNameStudentIdentifierMatchTestStudentMapping_ExamineeValid() throws Exception {

        // Arrange
        final IndividualResponse individualResponse = createIndividualResponse(9999L, "StudentID", "test");

        when(testStudentMappingService.getTestStudentMapping("test", "StudentID")).thenReturn(new TestStudentMappingBuilder()
        		.test("test").studentSSID("StudentID").toTestStudentMapping());

        // Act
        underTest.analyze(individualResponse);

        // We are testing a single attribute so there should only be one CellCategory
        final List<CellCategory> actualCellCategory = individualResponse.getExamineeCategory().getCellCategories();

        // Assert
        Assert.assertTrue(individualResponse.isValidExaminee());
        Assert.assertThat(actualCellCategory.size(), is(2));
    }

    /** 
     * Verify that when the Test name and Student Identifier do not match any existing IRP TestStudentMapping object,
     * then examinee is invalid.
     *
     * @throws Exception
     */
    @Test
    public void whenTestNameStudentIdentifierNotMatchTestStudentMapping_ExamineeInvalid() throws Exception {

        // Arrange
        // Create an IndividualResponse with a TDS Report Examinee and TestPropertiesCategory
        final IndividualResponse individualResponse = createIndividualResponse(1000L, "BadStudentID", "test");

        when(testStudentMappingService.getTestStudentMapping("test", "BadStudentID")).thenReturn(null);

        // Act
        underTest.analyze(individualResponse);

        final List<CellCategory> actualCellCategory = individualResponse.getExamineeCategory().getCellCategories();
       
        // Assert
        Assert.assertFalse(individualResponse.isValidExaminee());
        Assert.assertThat(actualCellCategory.size(), is(2));
    }
    
    /**
     * Verify the Examinee key is analyzed when the Student Identifier does not match an existing IRP Student.
     *
     * @throws Exception
     */
    @Test
    public void whenStudentIdentifierDoesNotExistInIRP_ExamineeKeyStillAnalyzed() throws Exception {

        // Arrange
        // Create an IndividualResponse with a TDS Report Examinee and TestPropertiesCategory
        final IndividualResponse individualResponse = createIndividualResponse(9999L, "BadStudentID", "test");

        when(testStudentMappingService.getTestStudentMapping("test", "BadStudentID")).thenReturn(null);

        // Act
        underTest.analyze(individualResponse);

        List<CellCategory> cellCategories = individualResponse.getExamineeCategory().getCellCategories();
        
        // We are testing a single attribute so there should only be one CellCategory
        final CellCategory actualCellCategory = cellCategories.get(0);

        // Setup expected results after analysis that indicates the field has an incorrect value
        final CellCategory expectedCellCategory = new CellCategoryBuilder()
                .tdsFieldName(ExamineeAnalysisAction.EnumExamineeFieldName.key.toString())
                .tdsFieldNameValue("9999")
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.P)
                .fieldEmpty(false)
                .correctDataType(true)
                .acceptableValue(true) 
                .correctValue(false)
                .toCellCategory();

        // Assert
        Assert.assertFalse(individualResponse.isValidExaminee());
        Assert.assertEquals(actualCellCategory, expectedCellCategory);
    }

    /**
     * <xs:element name="Examinee" minOccurs="1" maxOccurs="1">
     * TDS Report should have only one Examinee element. Otherwise, trigger exception
     * in xmlValidate.validateXMLSchema(TDSReportXSDResource, tmpPath.toString()) in TdsReportAnalysisEngine.java
     * 
     * Verify that when the TDS Report doesn't have an Examinee that an empty, non-null ExamineeCategory still exists.
     * This is so consumers of the IndividualResponse don't have to check the existence of the Category.
     */
    @Test
    public void whenExamineeIsNull_EmptyCellCategories() throws Exception {

        // Arrange
        final TDSReport tdsReport = new TDSReport();

        final IndividualResponse individualResponse = new IndividualResponse();
        individualResponse.setTDSReport(tdsReport);

        //<xs:element name="Examinee" minOccurs="1" maxOccurs="1">
        
        // Act
        underTest.analyze(individualResponse);

        // Assert
        Assert.assertThat(individualResponse.getExamineeCategory().getCellCategories().size(), is(0));
    }

    /**
     * //<xs:attribute name="key" type="xs:long" use="required"/>
     * 
     * Verify that when the Examinee key doesn't exist the field is marked as being invalid
     */
    @Test
    public void whenExamineeKeyIsNull_InvalidField() throws Exception {

        // Arrange
        // Create an Examinee without a key
        final IndividualResponse individualResponse = createIndividualResponse(null, "StudentID", "test");


        // Act
        underTest.analyze(individualResponse);

        // We are testing a single attribute so there should only be one CellCategory
        final CellCategory actualCellCategory = individualResponse.getExamineeCategory().getCellCategories().get(0);

        // Setup expected results after analysis that indicates the field has an incorrect value
        final CellCategory expectedCellCategory = new CellCategoryBuilder()
                .tdsFieldName(ExamineeAnalysisAction.EnumExamineeFieldName.key.toString())
                .fieldEmpty(true)
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.P)
                .toCellCategory();

        // Assert
        Assert.assertEquals(expectedCellCategory, actualCellCategory);
    }
}