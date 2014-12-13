package org.cresst.sb.irp.dao;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Test;
import org.cresst.sb.irp.domain.testpackage.Property;
import org.cresst.sb.irp.domain.testpackage.Testpackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class TestAnalysisAction extends AnalysisAction {
	private static Logger logger = Logger.getLogger(TestAnalysisAction.class);

	public enum EnumTestFieldName {
		name, subject, testId, bankKey, contract, mode, grade, assessmentType, academicYear, assessmentVersion;
	}

	List<String> listGradeAcceptValues = Arrays.asList("IT", "PR", "PK", "TK", "KG", "01", "02", "03", "04", "05", 
			"06", "07", "08", "09", "10", "11", "12", "13", "PS", "UG"); 
	
	@Override
	public void analysis() throws IOException {
		try {
			IndividualResponse individualResponse = getIndividualResponse();
			List<CellCategory> listTestPropertyCategory = individualResponse.getListTestPropertyCategory();
			TDSReport tdsReport = individualResponse.getTDSReport();
			Test tdsTest = getTest(tdsReport);

			CellCategory testCategory;
			FieldCheckType fieldCheckType;

			testCategory = new CellCategory();
			listTestPropertyCategory.add(testCategory);
			testCategory.setTdsFieldName(EnumTestFieldName.name.toString());
			testCategory.setTdsFieldNameValue(tdsTest.getName());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			testCategory.setFieldCheckType(fieldCheckType);
			validateField(tdsTest, EnumFieldCheckType.PC, EnumTestFieldName.name, fieldCheckType);

			testCategory = new CellCategory();
			listTestPropertyCategory.add(testCategory);
			testCategory.setTdsFieldName(EnumTestFieldName.subject.toString());
			testCategory.setTdsFieldNameValue(tdsTest.getSubject());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			testCategory.setFieldCheckType(fieldCheckType);
			validateField(tdsTest, EnumFieldCheckType.PC, EnumTestFieldName.subject, fieldCheckType);

			testCategory = new CellCategory();
			listTestPropertyCategory.add(testCategory);
			testCategory.setTdsFieldName(EnumTestFieldName.testId.toString());
			testCategory.setTdsFieldNameValue(tdsTest.getTestId());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			testCategory.setFieldCheckType(fieldCheckType);
			validateField(tdsTest, EnumFieldCheckType.PC, EnumTestFieldName.testId, fieldCheckType);

			testCategory = new CellCategory();
			listTestPropertyCategory.add(testCategory);
			testCategory.setTdsFieldName(EnumTestFieldName.bankKey.toString());
			testCategory.setTdsFieldNameValue(Long.toString(tdsTest.getBankKey()));
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			testCategory.setFieldCheckType(fieldCheckType);
			validateField(tdsTest, EnumFieldCheckType.P, EnumTestFieldName.bankKey, fieldCheckType);

			testCategory = new CellCategory();
			listTestPropertyCategory.add(testCategory);
			testCategory.setTdsFieldName(EnumTestFieldName.contract.toString());
			testCategory.setTdsFieldNameValue(tdsTest.getContract());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			testCategory.setFieldCheckType(fieldCheckType);
			validateField(tdsTest, EnumFieldCheckType.P, EnumTestFieldName.contract, fieldCheckType);

			testCategory = new CellCategory();
			listTestPropertyCategory.add(testCategory);
			testCategory.setTdsFieldName(EnumTestFieldName.mode.toString());
			testCategory.setTdsFieldNameValue(tdsTest.getMode());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			testCategory.setFieldCheckType(fieldCheckType);
			validateField(tdsTest, EnumFieldCheckType.P, EnumTestFieldName.mode, fieldCheckType);
			
			testCategory = new CellCategory();
			listTestPropertyCategory.add(testCategory);
			testCategory.setTdsFieldName(EnumTestFieldName.grade.toString());
			testCategory.setTdsFieldNameValue(tdsTest.getGrade());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			testCategory.setFieldCheckType(fieldCheckType);
			validateField(tdsTest, EnumFieldCheckType.PC, EnumTestFieldName.grade, fieldCheckType);
			
			testCategory = new CellCategory();
			listTestPropertyCategory.add(testCategory);
			testCategory.setTdsFieldName(EnumTestFieldName.assessmentType.toString());
			testCategory.setTdsFieldNameValue(tdsTest.getAssessmentType());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			testCategory.setFieldCheckType(fieldCheckType);
			validateField(tdsTest, EnumFieldCheckType.P, EnumTestFieldName.assessmentType, fieldCheckType);

			testCategory = new CellCategory();
			listTestPropertyCategory.add(testCategory);
			testCategory.setTdsFieldName(EnumTestFieldName.academicYear.toString());
			testCategory.setTdsFieldNameValue(Long.toString(tdsTest.getAcademicYear()));
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			testCategory.setFieldCheckType(fieldCheckType);
			validateField(tdsTest, EnumFieldCheckType.P, EnumTestFieldName.academicYear, fieldCheckType);

			testCategory = new CellCategory();
			listTestPropertyCategory.add(testCategory);
			testCategory.setTdsFieldName(EnumTestFieldName.assessmentVersion.toString());
			testCategory.setTdsFieldNameValue(tdsTest.getAssessmentVersion());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			testCategory.setFieldCheckType(fieldCheckType);
			validateField(tdsTest, EnumFieldCheckType.PC, EnumTestFieldName.assessmentVersion, fieldCheckType);

		} catch (Exception e) {
			logger.error("analysis exception: ", e);
		}
	}

	private void validateField(Test tdsTest, EnumFieldCheckType enumFieldCheckType, EnumTestFieldName enumFieldName,
			FieldCheckType fieldCheckType) {
		try {
			switch (enumFieldCheckType) {
			case D:
				break;
			case P:
				checkP(tdsTest, enumFieldName, fieldCheckType);
				break;
			case PC:
				checkP(tdsTest, enumFieldName, fieldCheckType);
				checkC(tdsTest, enumFieldName, fieldCheckType);
				break;
			}
		} catch (Exception e) {
			logger.error("checkP exception: ", e);
		}
	}

	// Field Check Type (P) --> check that field is not empty, and field value is of correct data type
	// and within acceptable values
	private void checkP(Test tdsTest, EnumTestFieldName enumFieldName, FieldCheckType fieldCheckType) {
		try {
			switch (enumFieldName) {
			case name:
				//<xs:attribute name="name" use="required" />
				processP_PritableASCIIone(tdsTest.getName(), fieldCheckType);
				break;
			case subject:
				//<xs:attribute name="subject" use="required" />
				processP_PritableASCIIone(tdsTest.getSubject(), fieldCheckType);
				break;
			case testId:
				//<xs:attribute name="testId" use="required" />
				processP_PritableASCIIone(tdsTest.getTestId(), fieldCheckType);
				break;
			case bankKey:
				//<xs:attribute name="bankKey" type="xs:unsignedInt" />
				processP_Positive32bit(Long.toString(tdsTest.getBankKey()), fieldCheckType);
				break;
			case contract:
				//<xs:attribute name="contract" use="required" />
				processP_PritableASCIIone(tdsTest.getSubject(), fieldCheckType);
				break;
			case mode:
				//	<xs:attribute name="mode" use="required">
				//<xs:simpleType>
				//<xs:restriction base="xs:token">
				//	<xs:enumeration value="online" />
				//	<xs:enumeration value="paper" />
				//	<xs:enumeration value="scanned" />
				//</xs:restriction>
				//</xs:simpleType>
				processP(tdsTest.getMode(), fieldCheckType);
				break;
			case grade:
				//<xs:attribute name="grade" use="required" />
				validateToken(tdsTest.getGrade(), fieldCheckType);
				processAcceptValue(tdsTest.getGrade(), fieldCheckType, listGradeAcceptValues);
				break;
			case assessmentType:
				//  <xs:attribute name="assessmentType" />
				processP_PritableASCIIone(tdsTest.getAssessmentType(), fieldCheckType);
				break;
			case academicYear:	
				// <xs:attribute name="academicYear" type="xs:unsignedInt" />
				processP_Year(tdsTest.getAcademicYear(), fieldCheckType);
				break;
			case assessmentVersion:		
				// <xs:attribute name="assessmentVersion" />
				processP(tdsTest.getAssessmentVersion(), fieldCheckType);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("checkP exception: ", e);
		}
	}

	// Field Check Type (PC) --> check everything the same as (P) plus check if field value is correct
	private void checkC(Test tdsTest, EnumTestFieldName enumTestFieldName, FieldCheckType fieldCheckType) {

		try {
			String uniqueid = tdsTest.getName(); // String uniqueid = "(SBAC_PT)SBAC-Mathematics-11-Spring-2013-2015";
			Testpackage testpackage = getTestpackageByIdentifierUniqueid(uniqueid);
			if (testpackage != null) {
				switch (enumTestFieldName) {
				case name:
					setCcorrect(fieldCheckType);
					break;
				case subject:
					processC_Subject(tdsTest, testpackage, fieldCheckType);
					break;
				case testId:
					processC_TestId(tdsTest, testpackage, fieldCheckType);
					break;
				case bankKey:
					break;
				case contract:
					break;
				case mode:
					break;
				case grade:
					processC_Grade(tdsTest, testpackage, fieldCheckType);
					break;
				case assessmentType:
					break;
				case academicYear:	
					break;	
				case assessmentVersion:
					processC_AssessmentVersion(tdsTest, testpackage, fieldCheckType);
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			logger.error("checkC exception: ", e);
		}
	}

	private void processC_Subject(Test tdsTest, Testpackage testpackage, FieldCheckType fieldCheckType) {
		try {
			String testSubject = tdsTest.getSubject();
			List<Property> listProperty = testpackage.getProperty();
			String subjectValueFromTestPackage = getSubjectPropertyValueFromListProperty(listProperty);
			if (subjectValueFromTestPackage != null) {
				if (subjectValueFromTestPackage.equals("MATH") && testSubject.equals("MA")
						|| subjectValueFromTestPackage.equals("ELA") && testSubject.equals("ELA")) {
					fieldCheckType.setCorrectValue(true);
				}
			}
		} catch (Exception e) {
			logger.error("processSubject exception: ", e);
		}

	}

	private void processC_TestId(Test test, Testpackage testpackage, FieldCheckType fieldCheckType) {
		try {
			// TODO Auto-generated method stub
		} catch (Exception e) {
			logger.error("processTestId exception: ", e);
		}

	}
	
	private void processC_Grade(Test tdsTest, Testpackage testpackage, FieldCheckType fieldCheckType){
		String testGrade = tdsTest.getGrade();
		List<Property> listProperty = testpackage.getProperty();
		String gradeValueFromTestPackage = getGradePropertyValueFromListProperty(listProperty);
		if (gradeValueFromTestPackage != null) {
			if (gradeValueFromTestPackage.equals(testGrade))
				fieldCheckType.setCorrectValue(true);
		}
	}
	
	private void processC_AssessmentVersion(Test tdsTest, Testpackage testpackage, FieldCheckType fieldCheckType){
		String assessmentVersion = tdsTest.getAssessmentVersion();
		String version = testpackage.getIdentifier().getVersion();
		if (assessmentVersion != null && assessmentVersion.length() > 0 && version != null && version.length() > 0){
			if (assessmentVersion.trim().toLowerCase().equals(version.trim().toLowerCase()))
				fieldCheckType.setCorrectValue(true);
		}
	}
	

}
