package org.cresst.sb.irp.analysis.engine;

import org.apache.commons.lang3.StringUtils;
import org.cresst.sb.irp.analysis.engine.examinee.ExamineeHelper;
import org.cresst.sb.irp.domain.analysis.ExamineeCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.TestPropertiesCategory;
import org.cresst.sb.irp.domain.student.Student;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee;
import org.cresst.sb.irp.domain.teststudentmapping.TestStudentMapping;
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

        ExamineeCategory examineeCategory = new ExamineeCategory();
        individualResponse.setExamineeCategory(examineeCategory);

        if (examinee != null) {
            TestPropertiesCategory testPropertiesCategory = individualResponse.getTestPropertiesCategory();
            String testName = getTdsFieldNameValueByFieldName(testPropertiesCategory.getCellCategories(), "name");
  
            validate(examineeCategory, examinee, examinee.getKey(), EnumFieldCheckType.P, EnumExamineeFieldName.key, null);
            //<xs:attribute name="isDemo" type="Bit"/> does not match isDemo data type in
            //http://www.smarterapp.org/documents/TestResultsTransmissionFormat.pdf 23 May 2015
            validate(examineeCategory, examinee, examinee.getIsDemo(), EnumFieldCheckType.D, EnumExamineeFieldName.isDemo, null);

            // The examinee key is not the student identifier (SSID). It is an internal system ID that can't be verified.
            // The student's identifier (SSID) has to be obtained from the Examinee's attributes.
            String studentIdentifier = ExamineeHelper.getStudentIdentifier(examinee);
      
            TestStudentMapping testStudentMapping = getTestStudentMapping(testName, studentIdentifier);
            if (testStudentMapping != null) {
                individualResponse.setValidExaminee(true);
                
                if (StringUtils.equalsIgnoreCase(testStudentMapping.getTestType(), "single")) {
                	  if(testStudentMapping.isCAT()) 
                      	individualResponse.setCAT(true);
                }else if (StringUtils.equalsIgnoreCase(testStudentMapping.getTestType(), "combined"))
                	individualResponse.setCombo(true);
            } else {
                logger.info(String.format("TDS Report contains Test name (%s) and an Student Identifier (%s) that does not match an IRP TestStudentMapping", testName, studentIdentifier));
            }
        }
    }

    @Override
    protected void checkP(Examinee examinee, EnumExamineeFieldName enumFieldName, FieldCheckType fieldCheckType) {
        switch (enumFieldName) {
            case key:
                // <xs:attribute name="key" type="xs:long" use="required"/>
                final Long key = examinee.getKey();
                processP_Positive64bit(key, fieldCheckType);
                fieldCheckType.setOptionalValue(true);
                fieldCheckType.setCorrectWidth(key != null);
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
        return null;
    }
}
