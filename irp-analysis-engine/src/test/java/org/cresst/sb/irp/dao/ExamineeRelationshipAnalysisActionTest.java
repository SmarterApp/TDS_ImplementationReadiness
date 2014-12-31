package org.cresst.sb.irp.dao;

import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.student.Student;
import org.cresst.sb.irp.domain.tdsreport.Context;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.service.StudentService;
import org.cresst.sb.irp.service.TDSReportService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.datatype.DatatypeFactory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test class to verify the behavior of ExamineeRelationshipAnalysisAction.
 */
@RunWith(MockitoJUnitRunner.class)
public class ExamineeRelationshipAnalysisActionTest {

    @Mock
    private StudentService studentService;

    @Mock
    private TDSReportService tdsReportService;

    @InjectMocks
    private ExamineeRelationshipAnalysisAction underTest = new ExamineeRelationshipAnalysisAction();

    /**
     * Given an ExamineeRelationship the analyzer will produces a CellCategory that matches expectations.
     * @throws Exception Caught by test runner if there's a problem
     */
    @Test
    public void testAnalysis_ProducesKnownResults() throws Exception {

        // Arrange
        final IndividualResponse individualResponse = generateIndividualResponse();

        when(studentService.getStudentByStudentSSID("1")).thenReturn(generateStudent());
        when(tdsReportService.getExamineeRelationships(individualResponse.getTDSReport().getExaminee())).thenReturn(generateExamineeRelationships());

        // Act
        underTest.analyze(individualResponse);

        // Assert
        CellCategory expectedCellCategory = generateExpectedCellCategory();
        List<CellCategory> cellCategories = individualResponse.getExamineeRelationshipCategories().get(0).getCellCategories();
        assertEquals(expectedCellCategory, cellCategories.get(0));
    }

    /**
     * This is a supporting method to help test. It generates a 'known' ExamineeRelationship to help verify the analyzers
     * behavior.
     * @return A List containing a single ExamineeRelationship
     * @throws Exception
     */
    private List<TDSReport.Examinee.ExamineeRelationship> generateExamineeRelationships() throws Exception {
        List<TDSReport.Examinee.ExamineeRelationship> relationships = new ArrayList<>();
        relationships.add(generateExamineeRelationship());
        return relationships;
    }

    /**
     * Creates a CellCategory that is expected to be produced by the analyzer
     * @return A CellCategory containing the values that is expected from the analyzer
     */
    private CellCategory generateExpectedCellCategory() {
        FieldCheckType fieldCheckType = new FieldCheckType();
        fieldCheckType.setEnumfieldCheckType(FieldCheckType.EnumFieldCheckType.P);
        fieldCheckType.setAcceptableValue(true);
        fieldCheckType.setCorrectDataType(true);
        fieldCheckType.setFieldEmpty(false);

        CellCategory cellCategory = new CellCategory();
        cellCategory.setTdsFieldName("name");
        cellCategory.setTdsFieldNameValue("NameOfInstitution");
        cellCategory.setFieldCheckType(fieldCheckType);

        return cellCategory;
    }

    private Student generateStudent() {
        return new Student();
    }

    private IndividualResponse generateIndividualResponse() throws Exception  {
        IndividualResponse individualResponse = new IndividualResponse();
        individualResponse.setTDSReport(generateTdsReport());
        return individualResponse;
    }

    private TDSReport generateTdsReport() throws Exception {
        TDSReport tdsReport = new TDSReport();
        tdsReport.setExaminee(generateExaminee());
        return tdsReport;
    }

    private TDSReport.Examinee generateExaminee() throws Exception {
        TDSReport.Examinee examinee = new TDSReport.Examinee();
        examinee.setKey(1l);
        examinee.getExamineeAttributeOrExamineeRelationship().add(generateExamineeRelationship());
        return examinee;
    }

    private TDSReport.Examinee.ExamineeRelationship generateExamineeRelationship() throws Exception {
        TDSReport.Examinee.ExamineeRelationship examineeRelationship = new TDSReport.Examinee.ExamineeRelationship();
        examineeRelationship.setContext(Context.INITIAL);
        examineeRelationship.setContextDate(DatatypeFactory.newInstance().newXMLGregorianCalendar());
        examineeRelationship.setEntityKey(BigInteger.valueOf(100));
        examineeRelationship.setName("NameOfInstitution");
        examineeRelationship.setValue("Some School");

        return examineeRelationship;
    }
}