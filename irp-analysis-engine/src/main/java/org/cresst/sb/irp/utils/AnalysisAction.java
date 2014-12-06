package org.cresst.sb.irp.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.student.Student;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Test;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee.ExamineeAttribute;
import org.cresst.sb.irp.domain.testpackage.Property;
import org.cresst.sb.irp.domain.testpackage.Testpackage;
import org.cresst.sb.irp.service.StudentService;
import org.cresst.sb.irp.service.TDSReportService;
import org.cresst.sb.irp.service.TestPackageService;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class AnalysisAction {
	private static Logger logger = Logger.getLogger(AnalysisAction.class);

	@Autowired
	public TestPackageService testPackageService;

	@Autowired
	public StudentService studentService;

	@Autowired
	public TDSReportService tdsReportService;

	private IndividualResponse individualResponse;
	private TDSReport tdsReport;
	private Test test;
	
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

	public Testpackage getTestpackageByIdentifierUniqueid(String uniqueid){
		return testPackageService.getTestpackageByIdentifierUniqueid(uniqueid);
	}
	
	public String getSubjectPropertyValueFromListProperty(List<Property> listProperty){
		return testPackageService.getSubjectPropertyValueFromListProperty(listProperty);
	}
	
	public List<ExamineeAttribute> getExamineeAttributes(Examinee examinee) {
		List<ExamineeAttribute> listExamineeAttribute = new ArrayList<ExamineeAttribute>();
		try {
			listExamineeAttribute = tdsReportService.getExamineeAttributes(examinee);
			if (listExamineeAttribute != null) {
				return listExamineeAttribute;
			}
		} catch (Exception e) {
			logger.error("getExamineeAttributes exception: ", e);
		}
		return null;
	}

	public Student getStudent(Long key) {
		Student student = new Student();
		try {
			String tempKey = "8505";
			// Student student = studentService.getStudentByStudentSSID(key.toString());
			student = studentService.getStudentByStudentSSID(tempKey);
			if (student != null) {
				return student;
			}
		} catch (Exception e) {
			logger.error("getStudent exception: ", e);
		}
		return null;
	}	
	
	public int sign(long i) {
		if (i == 0)
			return 0;
		if (i >> 63 != 0)
			return -1;
		return +1;
	}

	abstract public void analysis() throws IOException;
	
	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
	
	public void processP(String fieldValue, FieldCheckType fieldCheckType) {
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
	
	







}
