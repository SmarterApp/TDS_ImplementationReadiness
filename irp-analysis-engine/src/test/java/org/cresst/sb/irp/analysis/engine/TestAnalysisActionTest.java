package org.cresst.sb.irp.analysis.engine;

import builders.CellCategoryBuilder;

import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.testpackage.Identifier;
import org.cresst.sb.irp.domain.testpackage.Property;
import org.cresst.sb.irp.domain.testpackage.Testspecification;
import org.cresst.sb.irp.service.TestPackageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestAnalysisActionTest {

    @Mock
    private TestPackageService testPackageService;

    @InjectMocks
    private TestAnalysisAction underTest = new TestAnalysisAction();

    @Test
    public void whenTdsGradeSingleAcceptableValue_CellCategoryHasPassingValue() {
        // Arrange
        final TDSReport.Test tdsTest = new TDSReport.Test();
        tdsTest.setName("test");
        tdsTest.setGrade("9"); //if tdsTest.setGrade("09"); look at tdsFieldNameValue("09") in expectedCellCategory

        final TDSReport tdsReport = new TDSReport();
        tdsReport.setTest(tdsTest);

        final IndividualResponse individualResponse = new IndividualResponse();
        individualResponse.setTDSReport(tdsReport);

        Testspecification testPackage = new Testspecification();
        for (String grade : new String[] { "10", "11", "12", "13", "9", "PS" }) {
            final Property testPackageProperty = new Property();
            testPackageProperty.setName("grade");
            testPackageProperty.setValue(grade);
            testPackage.getProperty().add(testPackageProperty);
        }

        when(testPackageService.getTestpackageByIdentifierUniqueid("test")).thenReturn(testPackage);

        // Act
        underTest.analyze(individualResponse);

        // Search for the "grade" CellCategory
        CellCategory actualCellCategory = null;
        for (CellCategory cellCategory : individualResponse.getTestPropertiesCategory().getCellCategories()) {
            if (cellCategory.getTdsFieldName().equalsIgnoreCase("grade")) {
                actualCellCategory = cellCategory;
                break;
            }
        }

        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .tdsFieldName("grade")
                .tdsFieldNameValue("9") // .tdsFieldNameValue("09") 
                .tdsExpectedValue("10,11,12,13,09,PS")
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .isFieldEmpty(false)
                .acceptableValue(true)
                .correctDataType(true)
                .correctValue(true)
                .toCellCategory();

        // Assert
        assertEquals(expectedCellCategory, actualCellCategory);
    }

    @Test
    public void whenTdsGradeListOfAcceptableValue_CellCategoryHasPassingValue() {
        // Arrange
        final TDSReport.Test tdsTest = new TDSReport.Test();
        tdsTest.setName("test");
        tdsTest.setGrade("09, 10,PS ,11");

        final TDSReport tdsReport = new TDSReport();
        tdsReport.setTest(tdsTest);

        final IndividualResponse individualResponse = new IndividualResponse();
        individualResponse.setTDSReport(tdsReport);

        Testspecification testPackage = new Testspecification();
        for (String grade : new String[] { "10", "11", "12", "13", "9", "PS" }) {
            final Property testPackageProperty = new Property();
            testPackageProperty.setName("grade");
            testPackageProperty.setValue(grade);
            testPackage.getProperty().add(testPackageProperty);
        }

        when(testPackageService.getTestpackageByIdentifierUniqueid("test")).thenReturn(testPackage);

        // Act
        underTest.analyze(individualResponse);

        // Search for the "grade" CellCategory
        CellCategory actualCellCategory = null;
        for (CellCategory cellCategory : individualResponse.getTestPropertiesCategory().getCellCategories()) {
            if (cellCategory.getTdsFieldName().equalsIgnoreCase("grade")) {
                actualCellCategory = cellCategory;
                break;
            }
        }

        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .tdsFieldName("grade")
                .tdsFieldNameValue("09, 10,PS ,11")
                .tdsExpectedValue("10,11,12,13,09,PS")
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .isFieldEmpty(false)
                .acceptableValue(true)
                .correctDataType(true)
                .correctValue(true)
                .toCellCategory();

        // Assert
        assertEquals(expectedCellCategory, actualCellCategory);
    }

    @Test
    public void whenTdsGradeClosedRangeOfAcceptableValue_CellCategoryHasPassingValue() {
        // Arrange
        final TDSReport.Test tdsTest = new TDSReport.Test();
        tdsTest.setName("test");
        tdsTest.setGrade("10-13");

        final TDSReport tdsReport = new TDSReport();
        tdsReport.setTest(tdsTest);

        final IndividualResponse individualResponse = new IndividualResponse();
        individualResponse.setTDSReport(tdsReport);

        Testspecification testPackage = new Testspecification();
        for (String grade : new String[] { "10", "11", "12", "13", "9", "PS" }) {
            final Property testPackageProperty = new Property();
            testPackageProperty.setName("grade");
            testPackageProperty.setValue(grade);
            testPackage.getProperty().add(testPackageProperty);
        }

        when(testPackageService.getTestpackageByIdentifierUniqueid("test")).thenReturn(testPackage);

        // Act
        underTest.analyze(individualResponse);

        // Search for the "grade" CellCategory
        CellCategory actualCellCategory = null;
        for (CellCategory cellCategory : individualResponse.getTestPropertiesCategory().getCellCategories()) {
            if (cellCategory.getTdsFieldName().equalsIgnoreCase("grade")) {
                actualCellCategory = cellCategory;
                break;
            }
        }

        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .tdsFieldName("grade")
                .tdsFieldNameValue("10-13")
                .tdsExpectedValue("10,11,12,13,09,PS")
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .isFieldEmpty(false)
                .acceptableValue(true)
                .correctDataType(true)
                .correctValue(true)
                .toCellCategory();

        // Assert
        assertEquals(expectedCellCategory, actualCellCategory);
    }

    @Test
    public void whenTdsGradeLeftOpenRangeOfAcceptableValue_CellCategoryHasPassingValue() {
        // Arrange
        final TDSReport.Test tdsTest = new TDSReport.Test();
        tdsTest.setName("test");
        tdsTest.setGrade("-13");

        final TDSReport tdsReport = new TDSReport();
        tdsReport.setTest(tdsTest);

        final IndividualResponse individualResponse = new IndividualResponse();
        individualResponse.setTDSReport(tdsReport);

        Testspecification testPackage = new Testspecification();
        for (String grade : new String[] { "10", "11", "12", "13", "9", "PS" }) {
            final Property testPackageProperty = new Property();
            testPackageProperty.setName("grade");
            testPackageProperty.setValue(grade);
            testPackage.getProperty().add(testPackageProperty);
        }

        when(testPackageService.getTestpackageByIdentifierUniqueid("test")).thenReturn(testPackage);

        // Act
        underTest.analyze(individualResponse);

        // Search for the "grade" CellCategory
        CellCategory actualCellCategory = null;
        for (CellCategory cellCategory : individualResponse.getTestPropertiesCategory().getCellCategories()) {
            if (cellCategory.getTdsFieldName().equalsIgnoreCase("grade")) {
                actualCellCategory = cellCategory;
                break;
            }
        }

        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .tdsFieldName("grade")
                .tdsFieldNameValue("-13")
                .tdsExpectedValue("10,11,12,13,09,PS")
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .isFieldEmpty(false)
                .acceptableValue(true)
                .correctDataType(true)
                .correctValue(true)
                .toCellCategory();

        // Assert
        assertEquals(expectedCellCategory, actualCellCategory);
    }

    @Test
    public void whenTdsGradeRightOpenRangeOfAcceptableValue_CellCategoryHasPassingValue() {
        // Arrange
        final TDSReport.Test tdsTest = new TDSReport.Test();
        tdsTest.setName("test");
        tdsTest.setGrade("10-");

        final TDSReport tdsReport = new TDSReport();
        tdsReport.setTest(tdsTest);

        final IndividualResponse individualResponse = new IndividualResponse();
        individualResponse.setTDSReport(tdsReport);

        Testspecification testPackage = new Testspecification();
        for (String grade : new String[] { "10", "11", "12", "13", "9", "PS" }) {
            final Property testPackageProperty = new Property();
            testPackageProperty.setName("grade");
            testPackageProperty.setValue(grade);
            testPackage.getProperty().add(testPackageProperty);
        }

        when(testPackageService.getTestpackageByIdentifierUniqueid("test")).thenReturn(testPackage);

        // Act
        underTest.analyze(individualResponse);

        // Search for the "grade" CellCategory
        CellCategory actualCellCategory = null;
        for (CellCategory cellCategory : individualResponse.getTestPropertiesCategory().getCellCategories()) {
            if (cellCategory.getTdsFieldName().equalsIgnoreCase("grade")) {
                actualCellCategory = cellCategory;
                break;
            }
        }

        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .tdsFieldName("grade")
                .tdsFieldNameValue("10-")
                .tdsExpectedValue("10,11,12,13,09,PS")
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .isFieldEmpty(false)
                .acceptableValue(true)
                .correctDataType(true)
                .correctValue(true)
                .toCellCategory();

        // Assert
        assertEquals(expectedCellCategory, actualCellCategory);
    }

    @Test
    public void whenTdsGradeRangeNoValuesOfAcceptableValue_CellCategoryHasPassingValue() {
        // Arrange
        final TDSReport.Test tdsTest = new TDSReport.Test();
        tdsTest.setName("test");
        tdsTest.setGrade("-");

        final TDSReport tdsReport = new TDSReport();
        tdsReport.setTest(tdsTest);

        final IndividualResponse individualResponse = new IndividualResponse();
        individualResponse.setTDSReport(tdsReport);

        Testspecification testPackage = new Testspecification();
        for (String grade : new String[] { "10", "11", "12", "13", "9", "PS" }) {
            final Property testPackageProperty = new Property();
            testPackageProperty.setName("grade");
            testPackageProperty.setValue(grade);
            testPackage.getProperty().add(testPackageProperty);
        }

        when(testPackageService.getTestpackageByIdentifierUniqueid("test")).thenReturn(testPackage);

        // Act
        underTest.analyze(individualResponse);

        // Search for the "grade" CellCategory
        CellCategory actualCellCategory = null;
        for (CellCategory cellCategory : individualResponse.getTestPropertiesCategory().getCellCategories()) {
            if (cellCategory.getTdsFieldName().equalsIgnoreCase("grade")) {
                actualCellCategory = cellCategory;
                break;
            }
        }

        CellCategory expectedCellCategory = new CellCategoryBuilder()
                .tdsFieldName("grade")
                .tdsFieldNameValue("-")
                .tdsExpectedValue("10,11,12,13,09,PS")
                .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
                .isFieldEmpty(true)
                .acceptableValue(false)
                .correctDataType(false)
                .correctValue(false)
                .toCellCategory();

        // Assert
        assertEquals(expectedCellCategory, actualCellCategory);
    }
    
    @Test
    public void whenSubjectTestAcceptableValue_CellCategoryHasPassingValue(){
        final TDSReport.Test tdsTest = new TDSReport.Test();
        tdsTest.setName("test");
        tdsTest.setSubject("math");
        
        final TDSReport tdsReport = new TDSReport();
        tdsReport.setTest(tdsTest);
    
        final IndividualResponse individualResponse = new IndividualResponse();
        individualResponse.setTDSReport(tdsReport);
        
        Testspecification testPackage = new Testspecification();
        final Property testPackageProperty = new Property();
        testPackageProperty.setName("subject");
        testPackageProperty.setValue("math");
        testPackage.getProperty().add(testPackageProperty);
        
        when(testPackageService.getTestpackageByIdentifierUniqueid("test")).thenReturn(testPackage);
        
        // Act
        underTest.analyze(individualResponse);

        // Search for the "subject" CellCategory
        CellCategory actualCellCategory = null;
        for (CellCategory cellCategory : individualResponse.getTestPropertiesCategory().getCellCategories()) {
            if (cellCategory.getTdsFieldName().equalsIgnoreCase("subject")) {
                actualCellCategory = cellCategory;
                break;
            }
        }
        
        CellCategory expectedCellCategory = new CellCategoryBuilder()
	        .tdsFieldName("subject")
	        .tdsFieldNameValue("math")
	        .tdsExpectedValue("math")
	        .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
	        .isFieldEmpty(false)
	        .acceptableValue(true)
	        .correctDataType(true)
	        .correctValue(true)
	        .toCellCategory();

        // Assert
        assertEquals(expectedCellCategory, actualCellCategory);
    }
    
    @Test
    public void whenSubjectTestNotAcceptableValue_CellCategoryHasPassingValue(){
        final TDSReport.Test tdsTest = new TDSReport.Test();
        tdsTest.setName("test");
        tdsTest.setSubject("mathX");
        
        final TDSReport tdsReport = new TDSReport();
        tdsReport.setTest(tdsTest);
    
        final IndividualResponse individualResponse = new IndividualResponse();
        individualResponse.setTDSReport(tdsReport);
        
        Testspecification testPackage = new Testspecification();
        final Property testPackageProperty = new Property();
        testPackageProperty.setName("subject");
        testPackageProperty.setValue("math");
        testPackage.getProperty().add(testPackageProperty);
        
        when(testPackageService.getTestpackageByIdentifierUniqueid("test")).thenReturn(testPackage);
        
        // Act
        underTest.analyze(individualResponse);

        // Search for the "subject" CellCategory
        CellCategory actualCellCategory = null;
        for (CellCategory cellCategory : individualResponse.getTestPropertiesCategory().getCellCategories()) {
            if (cellCategory.getTdsFieldName().equalsIgnoreCase("subject")) {
                actualCellCategory = cellCategory;
                break;
            }
        }
        
        CellCategory expectedCellCategory = new CellCategoryBuilder()
	        .tdsFieldName("subject")
	        .tdsFieldNameValue("mathX")
	        .tdsExpectedValue("math")
	        .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
	        .isFieldEmpty(false)
	        .acceptableValue(true)
	        .correctDataType(true)
	        .correctValue(false)
	        .toCellCategory();

        // Assert
        assertEquals(expectedCellCategory, actualCellCategory);
    }
    
    @Test
    public void whenAssessmentVersionMatchValue(){
        final TDSReport.Test tdsTest = new TDSReport.Test();
        tdsTest.setName("test");
        tdsTest.setAssessmentVersion("1234");
        
        final TDSReport tdsReport = new TDSReport();
        tdsReport.setTest(tdsTest);
    
        final IndividualResponse individualResponse = new IndividualResponse();
        individualResponse.setTDSReport(tdsReport);
        
        Testspecification testPackage = new Testspecification();
        final Identifier identifier = new Identifier();
        identifier.setVersion("1234");
        testPackage.setIdentifier(identifier);
    
        when(testPackageService.getTestpackageByIdentifierUniqueid("test")).thenReturn(testPackage);
        
        // Act
        underTest.analyze(individualResponse);
        
        // Search for the "assessmentVersion" CellCategory
        CellCategory actualCellCategory = null;
        for (CellCategory cellCategory : individualResponse.getTestPropertiesCategory().getCellCategories()) {
            if (cellCategory.getTdsFieldName().equalsIgnoreCase("assessmentVersion")) {
                actualCellCategory = cellCategory;
                break;
            }
        }
        
        CellCategory expectedCellCategory = new CellCategoryBuilder()
	        .tdsFieldName("assessmentVersion")
	        .tdsFieldNameValue("1234")
	        .tdsExpectedValue("1234")
	        .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
	        .isFieldEmpty(false)
	        .acceptableValue(true)
	        .correctDataType(true)
	        .correctValue(true)
	        .toCellCategory();

        // Assert
        assertEquals(expectedCellCategory, actualCellCategory);
    }
    
    @Test
    public void whenAssessmentVersionNotMatchValue(){
        final TDSReport.Test tdsTest = new TDSReport.Test();
        tdsTest.setName("test");
        tdsTest.setAssessmentVersion("1234");
        
        final TDSReport tdsReport = new TDSReport();
        tdsReport.setTest(tdsTest);
    
        final IndividualResponse individualResponse = new IndividualResponse();
        individualResponse.setTDSReport(tdsReport);
        
        Testspecification testPackage = new Testspecification();
        final Identifier identifier = new Identifier();
        identifier.setVersion("2222");
        testPackage.setIdentifier(identifier);
    
        when(testPackageService.getTestpackageByIdentifierUniqueid("test")).thenReturn(testPackage);
        
        // Act
        underTest.analyze(individualResponse);
        
        // Search for the "assessmentVersion" CellCategory
        CellCategory actualCellCategory = null;
        for (CellCategory cellCategory : individualResponse.getTestPropertiesCategory().getCellCategories()) {
            if (cellCategory.getTdsFieldName().equalsIgnoreCase("assessmentVersion")) {
                actualCellCategory = cellCategory;
                break;
            }
        }
        
        CellCategory expectedCellCategory = new CellCategoryBuilder()
	        .tdsFieldName("assessmentVersion")
	        .tdsFieldNameValue("1234")
	        .tdsExpectedValue("2222")
	        .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.PC)
	        .isFieldEmpty(false)
	        .acceptableValue(true)
	        .correctDataType(true)
	        .correctValue(false)
	        .toCellCategory();

        // Assert
        assertEquals(expectedCellCategory, actualCellCategory);
    }
}
