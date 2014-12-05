package org.cresst.sb.irp.utils;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.student.Student;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Test;
import org.cresst.sb.irp.domain.testpackage.Property;
import org.cresst.sb.irp.domain.testpackage.Testpackage;
import org.cresst.sb.irp.service.StudentService;
import org.cresst.sb.irp.service.TestPackageService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AnalysisAction {
	private static Logger logger = Logger.getLogger(AnalysisAction.class);

	@Autowired
	public TestPackageService testPackageService;

	@Autowired
	public StudentService studentService;
	
	private IndividualResponse individualResponse;
	private TDSReport tdsReport;
	private Test test;

	public enum EnumFieldName {
		TestName, TestSubject, TestTestId, TestBankKey, TestHandScoreProject, ExamineeKey, ExamineeAttributeName;
	}

	public AnalysisAction() {
		logger.info("initializing");
	}

	public IndividualResponse getIndividualResponse() {
		return individualResponse;
	}

	public void setIndividualResponse(IndividualResponse individualResponse) {
		this.individualResponse = individualResponse;
	}

	public TDSReport getTdsReport() {
		return tdsReport;
	}

	public void setTdsReport(TDSReport tdsReport) {
		this.tdsReport = tdsReport;
	}

	public Test getTest() {
		test = tdsReport.getTest();
		if (test != null) {
			return test;
		}
		return null;
	}

	public void setTest(Test test) {
		this.test = test;
	}

	public void validateField(Test test, EnumFieldName enumFieldName, EnumFieldCheckType enumFieldCheckType,
			FieldCheckType fieldCheckType) {

		switch (enumFieldCheckType) {
		case D:
			break;
		case P:
			checkP(test, enumFieldName, fieldCheckType);
			break;
		case PC:
			checkP(test, enumFieldName, fieldCheckType);
			checkC(test, enumFieldName, fieldCheckType);
			break;
		}
	}
	
	public void validateField(Long key, EnumFieldCheckType enumFieldCheckType, FieldCheckType fieldCheckType){
		switch (enumFieldCheckType) {
		case D:
			break;
		case P:
			processP(key, fieldCheckType);
			break;
		case PC:
			processP(key, fieldCheckType);
			processExaminee(key, fieldCheckType);
			break;
		}
		
	}

	// Field Check Type (P) --> check that field is not empty, and field value is of correct data type
	// and within acceptable values
	private void checkP(Test test, EnumFieldName enumFieldName, FieldCheckType fieldCheckType) {

		try {
			switch (enumFieldName) {
			case TestName:
				processP(test.getName(), fieldCheckType);
				break;
			case TestSubject:
				processP(test.getSubject(), fieldCheckType);
				break;
			case TestTestId:
				processP(test.getTestId(), fieldCheckType);
				break;
			case TestBankKey:
				break;
			case TestHandScoreProject:
				break;

			default:
				break;
			}
		} catch (Exception e) {
			logger.error("checkP exception: ", e);
		}
	}
	
	private void processP(String fieldValue, FieldCheckType fieldCheckType) {
		try {
			if (fieldValue != null && !fieldValue.trim().isEmpty()) {
				fieldCheckType.setCorrectDataType(true);
				fieldCheckType.setFieldEmpty(false);
			}
			if (StringUtils.isAsciiPrintable(fieldValue)) {
				fieldCheckType.setAcceptableValue(true);
			}
		} catch (Exception e) {
			logger.error("processP exception: ", e);
		}
	}
	
	private void processP(Long key, FieldCheckType fieldCheckType) {
		try {
			if (sign(key) > 0){
				fieldCheckType.setCorrectDataType(true);
				fieldCheckType.setFieldEmpty(false);
				fieldCheckType.setAcceptableValue(true);
			}
		} catch (Exception e) {
			logger.error("processP exception: ", e);
		}
	}
	
	private void processExaminee(Long key, FieldCheckType fieldCheckType){
		try {
			String tempKey = "8505";
			//Student student = studentService.getStudentByStudentSSID(key.toString());
			Student student = studentService.getStudentByStudentSSID(tempKey);
			if (student != null){
				fieldCheckType.setCorrectValue(true);
			}
		} catch (Exception e) {
			logger.error("processExaminee exception: ", e);
		}
	}
	
	private int sign(long i) {
	    if (i == 0) return 0;
	    if (i >> 63 != 0) return -1;
	    return +1;
	}

	// Field Check Type (PC) --> check everything the same as (P) plus check if field value is correct
	private void checkC(Test test, EnumFieldName enumFieldName, FieldCheckType fieldCheckType) {

		try {
			// String uniqueid = test.getName();
			String uniqueid = "(SBAC_PT)SBAC-Mathematics-11-Spring-2013-2015"; // "(SBAC_PT)SBAC ELA 3-ELA-3-Spring-2014-2015";
			Testpackage testpackage = testPackageService.getTestpackageByIdentifierUniqueid(uniqueid);

			switch (enumFieldName) {
			case TestName:
				if (testpackage != null) {
					fieldCheckType.setCorrectValue(true);
				}
				break;
			case TestSubject:
				processTestSubject(test, testpackage, fieldCheckType);
				break;
			case TestTestId:
				processTestTestId(test, testpackage, fieldCheckType);
				break;
			case TestBankKey:
				break;
			case TestHandScoreProject:
				break;

			default:
				break;
			}

		} catch (Exception e) {
			logger.error("checkC exception: ", e);
		}
	}

	private void processTestSubject(Test test, Testpackage testpackage, FieldCheckType fieldCheckType) {
		try {
			String testSubject = test.getSubject();
			List<Property> listProperty = testpackage.getProperty();
			String subjectValueFromTestPackage = testPackageService.getSubjectPropertyValueFromListProperty(listProperty);
			if (subjectValueFromTestPackage != null) {
				if (subjectValueFromTestPackage.equals("MATH") && testSubject.equals("MA")
						|| subjectValueFromTestPackage.equals("ELA") && testSubject.equals("ELA")) {
					fieldCheckType.setCorrectValue(true);
				}
			}
		} catch (Exception e) {
			logger.error("processTestSubject exception: ", e);
		}

	}

	private void processTestTestId(Test test, Testpackage testpackage, FieldCheckType fieldCheckType) {
		try {
			//don't know corresponding object in testpacke
			// TODO Auto-generated method stub
		} catch (Exception e) {
			logger.error("processTestTestId exception: ", e);
		}

	}
	
	abstract public void analysisTest() throws IOException;
	
	abstract public void analysisExaminee() throws IOException;
	
	abstract public void analysisExamineeAttribute() throws IOException;
	

}
