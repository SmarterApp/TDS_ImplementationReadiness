package org.cresst.sb.irp.analysis.engine;

import org.cresst.sb.irp.domain.analysis.ExamineeCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.TestPropertiesCategory;
import org.cresst.sb.irp.domain.student.Student;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee;
import org.cresst.sb.irp.domain.teststudentmapping.TestStudentMapping;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ExamineeAnalysisAction extends AnalysisAction<Examinee, ExamineeAnalysisAction.EnumExamineeFieldName, Student> {
    private final static Logger logger = LoggerFactory.getLogger(ExamineeAnalysisAction.class);

    static public enum EnumExamineeFieldName {
        key, isDemo
    }

    @Override
    public void analyze(IndividualResponse individualResponse) {
        TDSReport tdsReport = individualResponse.getTDSReport();
        Examinee examinee = tdsReport.getExaminee();  //<xs:element name="Examinee" minOccurs="1" maxOccurs="1">
        Long examineeKey = examinee.getKey(); //<xs:attribute name="key" type="xs:long" use="required"/>
        
        ExamineeCategory examineeCategory = new ExamineeCategory();
        individualResponse.setExamineeCategory(examineeCategory);

        TestPropertiesCategory testPropertiesCategory = individualResponse.getTestPropertiesCategory();
        String testName = getTdsFieldNameValueByFieldName(testPropertiesCategory.getCellCategories(), "name");
        
        TestStudentMapping testStudentMapping = getTestStudentMapping(testName, examineeKey);	
        
        if (testStudentMapping != null) {
        	individualResponse.setValidExaminee(true);
            Student student = null;
            
            try {
                student = getStudent(examineeKey);
            } catch (NotFoundException ex) {
                logger.info(String.format("TDS Report contains an Examinee Key (%d) that does not match an IRP Student", examineeKey));
            }
           
            validate(examineeCategory, examinee, examinee.getKey(), EnumFieldCheckType.PC, EnumExamineeFieldName.key, student);
            //<xs:attribute name="isDemo" type="Bit"/> does not match isDemo data type in
            //http://www.smarterapp.org/documents/TestResultsTransmissionFormat.pdf 23 May 2015
            validate(examineeCategory, examinee, examinee.getIsDemo(), EnumFieldCheckType.D, EnumExamineeFieldName.isDemo, student);
        }else {
        	 logger.info(String.format("TDS Report contains Test name (%s) and an Examinee Key (%d) that does not match an IRP TestStudentMapping", testName,  examineeKey));
        }
    }

    @Override
    protected void checkP(Examinee examinee, EnumExamineeFieldName enumFieldName, FieldCheckType fieldCheckType) {
        switch (enumFieldName) {
            case key:
                // <xs:attribute name="key" type="xs:long" use="required"/>
                processP_Positive64bit(examinee.getKey(), fieldCheckType);
                break;
            default:
                break;
        }
    }

    /**
     * Checks if the field has the correct value
     *
     * @param examinee       Examinee fields are checked
     * @param enumFieldName  Specifies the field to check
     * @param fieldCheckType This is where the results are stored
     * @param student        Examinee will be compared to Student record
     */
    @Override
    protected void checkC(Examinee examinee, EnumExamineeFieldName enumFieldName, FieldCheckType fieldCheckType, Student student) {
        switch (enumFieldName) {
            case key:
                if (student != null && student.getSSID() == examinee.getKey()) {
                    setCcorrect(fieldCheckType);
                }
                break;
            default:
                break;
        }
    }

    /**
     * Uses the IRP Student object to populate the expected value of the field being analyzed
     *
     * @param student       IRP Student with the expected values
     * @param enumFieldName Specifies the field to check
     * @return The value of the Student object that is expected for the given EnumExamineeFieldName
     */
    @Override
    protected String expectedValue(Student student, EnumExamineeFieldName enumFieldName) {

        String expectedValue = null;

        switch (enumFieldName) {
            case key:
                if (student != null) {
                    expectedValue = String.valueOf(student.getSSID());
                }
                break;
            default:
                break;
        }

        return expectedValue;
    }
}
