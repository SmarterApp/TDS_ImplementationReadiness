package org.cresst.sb.irp.dao;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
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

	@Override
	public void analysis() throws IOException {
		try {
			IndividualResponse individualResponse = getIndividualResponse();
			List<CellCategory> listTestPropertyCategory = individualResponse.getListTestPropertyCategory();
			CellCategory testCategory;
			FieldCheckType fieldCheckType;
			Test test = getTest();

			testCategory = new CellCategory();
			listTestPropertyCategory.add(testCategory);
			testCategory.setTdsFieldName(EnumTestFieldName.name.toString());
			testCategory.setTdsFieldNameValue(test.getName());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			testCategory.setFieldCheckType(fieldCheckType);
			validateField(test, EnumFieldCheckType.PC, EnumTestFieldName.name, fieldCheckType);

			testCategory = new CellCategory();
			listTestPropertyCategory.add(testCategory);
			testCategory.setTdsFieldName(EnumTestFieldName.subject.toString());
			testCategory.setTdsFieldNameValue(test.getSubject());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			testCategory.setFieldCheckType(fieldCheckType);
			validateField(test, EnumFieldCheckType.PC, EnumTestFieldName.subject, fieldCheckType);

			testCategory = new CellCategory();
			listTestPropertyCategory.add(testCategory);
			testCategory.setTdsFieldName(EnumTestFieldName.testId.toString());
			testCategory.setTdsFieldNameValue(test.getTestId());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			testCategory.setFieldCheckType(fieldCheckType);
			validateField(test, EnumFieldCheckType.PC, EnumTestFieldName.testId, fieldCheckType);

		} catch (Exception e) {
			logger.error("analysis exception: ", e);
		}
	}

	private void validateField(Test test, EnumFieldCheckType enumFieldCheckType, EnumTestFieldName enumFieldName,
			FieldCheckType fieldCheckType) {
		try {
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
		} catch (Exception e) {
			logger.error("checkP exception: ", e);
		}
	}

	// Field Check Type (P) --> check that field is not empty, and field value is of correct data type
	// and within acceptable values
	private void checkP(Test test, EnumTestFieldName enumFieldName, FieldCheckType fieldCheckType) {
		try {
			switch (enumFieldName) {
			case name:
				validateToken(test.getName(), fieldCheckType);
				validatePritableASCII(test.getName(), fieldCheckType);
				break;
			case subject:
				validateToken(test.getSubject(), fieldCheckType);
				validatePritableASCII(test.getSubject(), fieldCheckType);
				break;
			case testId:
				validateToken(test.getTestId(), fieldCheckType);
				validatePritableASCII(test.getTestId(), fieldCheckType);
				break;
			case bankKey:
				break;
			case contract:
				break;
			case mode:
				break;
			case grade:
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("checkP exception: ", e);
		}
	}

	// Field Check Type (PC) --> check everything the same as (P) plus check if field value is correct
	private void checkC(Test test, EnumTestFieldName enumTestFieldName, FieldCheckType fieldCheckType) {

		try {
			String uniqueid = test.getName(); //String uniqueid = "(SBAC_PT)SBAC-Mathematics-11-Spring-2013-2015"; 
			Testpackage testpackage = getTestpackageByIdentifierUniqueid(uniqueid);
			if (testpackage != null) {
				switch (enumTestFieldName) {
				case name:
					processName(fieldCheckType);
					break;
				case subject:
					processSubject(test, testpackage, fieldCheckType);
					break;
				case testId:
					processTestId(test, testpackage, fieldCheckType);
					break;
				case bankKey:
					break;
				case contract:
					break;
				case mode:
					break;
				case grade:
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			logger.error("checkC exception: ", e);
		}
	}

	private void processName(FieldCheckType fieldCheckType) {
		try {
			fieldCheckType.setCorrectValue(true);
		} catch (Exception e) {
			logger.error("processName exception: ", e);
		}
	}

	private void processSubject(Test test, Testpackage testpackage, FieldCheckType fieldCheckType) {
		try {
			String testSubject = test.getSubject();
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

	private void processTestId(Test test, Testpackage testpackage, FieldCheckType fieldCheckType) {
		try {
			// don't know corresponding object in testpacke
			// TODO Auto-generated method stub
		} catch (Exception e) {
			logger.error("processTestId exception: ", e);
		}

	}

}
