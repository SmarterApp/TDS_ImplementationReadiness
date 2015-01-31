package org.cresst.sb.irp.analysis.engine;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.TestPropertiesCategory;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Test;
import org.cresst.sb.irp.domain.testpackage.Identifier;
import org.cresst.sb.irp.domain.testpackage.Property;
import org.cresst.sb.irp.domain.testpackage.Testspecification;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class TestAnalysisAction extends AnalysisAction<Test, TestAnalysisAction.EnumTestFieldName, Testspecification> {
	private static Logger logger = Logger.getLogger(TestAnalysisAction.class);

	static public enum EnumTestFieldName {
		name, subject, testId, bankKey, contract, mode, grade, assessmentType, academicYear, assessmentVersion
	}

	static final List<String> listGradeAcceptValues = Arrays.asList("IT", "PR", "PK", "TK", "KG", "01", "02", "03", "04", "05",
			"06", "07", "08", "09", "10", "11", "12", "13", "PS", "UG");

	@Override
	public void analyze(IndividualResponse individualResponse) {
		try {
			TDSReport tdsReport = individualResponse.getTDSReport();
			Test tdsTest = tdsReport.getTest();

			String uniqueid = tdsTest.getName();
			System.out.println("uniqueid ---->" + uniqueid);
			Testspecification testPackage = getTestpackageByIdentifierUniqueid(uniqueid);

			if (testPackage == null) {
				Map<String, Testspecification> mapTestpackage = getMapTestpackage();
				for (Map.Entry<String, Testspecification> entry : mapTestpackage.entrySet()) {
					testPackage = entry.getValue();
					break;
				}
			}

			TestPropertiesCategory testPropertiesCategory = new TestPropertiesCategory();
			individualResponse.setTestPropertiesCategory(testPropertiesCategory);

			validate(testPropertiesCategory, tdsTest, tdsTest.getName(), EnumFieldCheckType.PC, EnumTestFieldName.name,
					testPackage);
			validate(testPropertiesCategory, tdsTest, tdsTest.getSubject(), EnumFieldCheckType.PC, EnumTestFieldName.subject,
					testPackage);
			validate(testPropertiesCategory, tdsTest, tdsTest.getTestId(), EnumFieldCheckType.PC, EnumTestFieldName.testId,
					testPackage);
			validate(testPropertiesCategory, tdsTest, tdsTest.getBankKey(), EnumFieldCheckType.P, EnumTestFieldName.bankKey,
					testPackage);
			validate(testPropertiesCategory, tdsTest, tdsTest.getContract(), EnumFieldCheckType.P, EnumTestFieldName.contract,
					testPackage);
			validate(testPropertiesCategory, tdsTest, tdsTest.getMode(), EnumFieldCheckType.P, EnumTestFieldName.mode,
					testPackage);
			validate(testPropertiesCategory, tdsTest, tdsTest.getGrade(), EnumFieldCheckType.PC, EnumTestFieldName.grade,
					testPackage);
			validate(testPropertiesCategory, tdsTest, tdsTest.getAssessmentType(), EnumFieldCheckType.P,
					EnumTestFieldName.assessmentType, testPackage);
			validate(testPropertiesCategory, tdsTest, tdsTest.getAcademicYear(), EnumFieldCheckType.P,
					EnumTestFieldName.academicYear, testPackage);
			validate(testPropertiesCategory, tdsTest, tdsTest.getAssessmentVersion(), EnumFieldCheckType.PC,
					EnumTestFieldName.assessmentVersion, testPackage);

		} catch (Exception e) {
			logger.error("analyze exception: ", e);
		}
	}

	/**
	 * Field Check Type (P) --> check that field is not empty, and field value is of correct data type and within acceptable
	 * values
	 *
	 * @param tdsTest
	 *            Test with fields to check
	 * @param enumFieldName
	 *            Specifies the field to check
	 * @param fieldCheckType
	 *            This is where the results are stored
	 */
	@Override
	protected void checkP(Test tdsTest, EnumTestFieldName enumFieldName, FieldCheckType fieldCheckType) {
		try {
			switch (enumFieldName) {
			case name:
				// <xs:attribute name="name" use="required" />
				processP_PritableASCIIone(tdsTest.getName(), fieldCheckType);
				break;
			case subject:
				// <xs:attribute name="subject" use="required" />
				processP_PritableASCIIone(tdsTest.getSubject(), fieldCheckType);
				break;
			case testId:
				// <xs:attribute name="testId" use="required" />
				processP_PritableASCIIone(tdsTest.getTestId(), fieldCheckType);
				break;
			case bankKey:
				// <xs:attribute name="bankKey" type="xs:unsignedInt" />
				processP_Positive32bit(Long.toString(tdsTest.getBankKey()), fieldCheckType);
				break;
			case contract:
				// <xs:attribute name="contract" use="required" />
				processP_PritableASCIIone(tdsTest.getSubject(), fieldCheckType);
				break;
			case mode:
				// <xs:attribute name="mode" use="required">
				// <xs:simpleType>
				// <xs:restriction base="xs:token">
				// <xs:enumeration value="online" />
				// <xs:enumeration value="paper" />
				// <xs:enumeration value="scanned" />
				// </xs:restriction>
				// </xs:simpleType>
				processP(tdsTest.getMode(), fieldCheckType, true); // last param -> required Y
				break;
			case grade:
				// <xs:attribute name="grade" use="required" />
				if (listGradeAcceptValues.contains(tdsTest.getGrade())) {
					setPcorrect(fieldCheckType);
				}
				break;
			case assessmentType:
				// <xs:attribute name="assessmentType" />
				processP_PritableASCIIone(tdsTest.getAssessmentType(), fieldCheckType);
				break;
			case academicYear:
				// <xs:attribute name="academicYear" type="xs:unsignedInt" />
				processP_Year(tdsTest.getAcademicYear(), fieldCheckType);
				break;
			case assessmentVersion:
				// <xs:attribute name="assessmentVersion" />
				processP(tdsTest.getAssessmentVersion(), fieldCheckType, true); // last param -> required Y
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("checkP exception: ", e);
		}
	}

	/**
	 * Checks if the Test field has the correct value
	 *
	 * @param tdsTest
	 *            Test with fields to check
	 * @param enumFieldName
	 *            Specifies the field to check
	 * @param fieldCheckType
	 *            This is where the results are stored
	 * @param testPackage
	 *            IRP Testspecification to compare against the given Test
	 */
	@Override
	protected void checkC(Test tdsTest, EnumTestFieldName enumFieldName, FieldCheckType fieldCheckType,
			Testspecification testPackage) {
		try {
			if (testPackage != null) {
				switch (enumFieldName) {
				case name:
					setCcorrect(fieldCheckType);
					break;
				case subject:
					processC_Subject(tdsTest, testPackage, fieldCheckType);
					break;
				case testId:
					processC_TestId(tdsTest, testPackage, fieldCheckType);
					break;
				case bankKey:
					break;
				case contract:
					break;
				case mode:
					break;
				case grade:
					processC_Grade(tdsTest, testPackage, fieldCheckType);
					break;
				case assessmentType:
					break;
				case academicYear:
					break;
				case assessmentVersion:
					processC_AssessmentVersion(tdsTest, testPackage, fieldCheckType);
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			logger.error("checkC exception: ", e);
		}
	}

	private void processC_Subject(Test tdsTest, Testspecification testpackage, FieldCheckType fieldCheckType) {
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

	private void processC_TestId(Test test, Testspecification testpackage, FieldCheckType fieldCheckType) {
		fieldCheckType.setCorrectValue(StringUtils.equalsIgnoreCase(test.getTestId(), testpackage.getIdentifier().getName()));
	}

	private void processC_Grade(Test test, Testspecification testpackage, FieldCheckType fieldCheckType) {
		String testGrade = test.getGrade();
		List<Property> listProperty = testpackage.getProperty();
		String gradeValueFromTestPackage = getGradePropertyValueFromListProperty(listProperty);
		if (gradeValueFromTestPackage != null) {
			if (gradeValueFromTestPackage.equals(testGrade))
				fieldCheckType.setCorrectValue(true);
		}
	}

	private void processC_AssessmentVersion(Test tdsTest, Testspecification testpackage, FieldCheckType fieldCheckType) {
		String assessmentVersion = tdsTest.getAssessmentVersion();
		String version = testpackage.getIdentifier().getVersion();
		if (assessmentVersion != null && assessmentVersion.length() > 0 && version != null && version.length() > 0) {
			if (assessmentVersion.trim().toLowerCase().equals(version.trim().toLowerCase()))
				fieldCheckType.setCorrectValue(true);
		}
	}
	
	 /**
     * Uses the IRP Testspecification object to populate the expected value of the field being analyzed
     *
     * @param testPackage       IRP Testspecification with the expected values
     * @param enumFieldName Specifies the field to check
     * @return The value of the Testspecification object that is expected for the given EnumTestFieldName
     */
	@Override
	protected String expectedValue(Testspecification testPackage, EnumTestFieldName enumFieldName) {
		String strReturn ="";
		Identifier identifier;
		List<Property> listProperty;
		
		try {
			switch (enumFieldName) {
			case name:
				identifier = testPackage.getIdentifier();
				strReturn = identifier.getUniqueid();
				break;
			case subject:
				listProperty = testPackage.getProperty();
				String subjectValueFromTestPackage = getSubjectPropertyValueFromListProperty(listProperty);
				strReturn = subjectValueFromTestPackage;
				break;
			case testId:
				identifier = testPackage.getIdentifier();
				strReturn = identifier.getUniqueid();
				break;
			case bankKey:
				break;
			case contract:
				break;
			case mode:
				break;
			case grade:
				listProperty = testPackage.getProperty();
				String gradeValueFromTestPackage = getGradePropertyValueFromListProperty(listProperty);
				strReturn = gradeValueFromTestPackage;
				break;
			case assessmentType:
				break;
			case academicYear:
				break;
			case assessmentVersion:
				identifier = testPackage.getIdentifier();
				strReturn = identifier.getVersion();
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("expectedValue exception: ", e);
		}

		return strReturn;
	}

	
}
