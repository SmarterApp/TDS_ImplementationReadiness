package org.cresst.sb.irp.dao;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.analysis.AnalysisResponse;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.student.Student;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee.ExamineeAttribute;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee.ExamineeRelationship;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Test;
import org.cresst.sb.irp.domain.testpackage.Identifier;
import org.cresst.sb.irp.domain.testpackage.Property;
import org.cresst.sb.irp.domain.testpackage.Testpackage;
import org.cresst.sb.irp.service.StudentService;
import org.cresst.sb.irp.service.TestPackageService;
import org.cresst.sb.irp.utils.XMLValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Repository;

@Repository
public class AnalysisDaoImpl implements AnalysisDao {
	private static Logger logger = Logger.getLogger(AnalysisDaoImpl.class);

	@Autowired
	public TestPackageService testPackageService;
	
	@Autowired
	public StudentService studentService;

	@Value("classpath:irp-package/TDSReport.xsd")
	private Resource TDSReportXSDResource;

	@Autowired
	private XMLValidate xmlValidate;

	@Autowired
	private Unmarshaller unmarshaller;

	public AnalysisDaoImpl() {
		logger.info("initializing");
	}

	public enum EnumFieldName {
		TestName, TestSubject, TestTestId, TestBankKey, TestHandScoreProject, ExamineeKey, ExamineeAttributeName;
	}
	
	public enum EnumExamineeAttributeAcceptValues {
		LastOrSurname, FirstName, MiddleName, Birthdate, DOB, StudentIdentifier, AlternateSSID, GradeLevelWhenAssessed,
		Sex, HispanicOrLatinoEthnicity, AmericanIndianOrAlaskaNative, Asian, BlackOrAfricanAmerican, White,
		NativeHawaiianOrOtherPacificIslander, DemographicRaceTwoOrMoreRaces, IDEAIndicator, LEPStatus, Section504Status,
		EconomicDisadvantageStatus, LanguageCode, EnglishLanguageProficiencyLevel, MigrantStatus, 
		FirstEntryDateIntoUSSchool, LimitedEnglishProficiencyEntryDate, LEPExitDate,
		TitleIIILanguageInstructionProgramType, PrimaryDisabilityType;
	}

	@Override
	public AnalysisResponse analysisProcess(Iterable<Path> tdsReportPaths) {
		AnalysisResponse analysisResponse = new AnalysisResponse();
		for (Path tmpPath : tdsReportPaths) {
			IndividualResponse individualResponse = new IndividualResponse();
			individualResponse.setFileName(tmpPath.getFileName().toString());
			analysisResponse.addListIndividualResponse(individualResponse);
			try {
				if (xmlValidate.validateXMLSchema(TDSReportXSDResource, tmpPath.toString())) {
					individualResponse.setValidXMLfile(true);
					TDSReport tdsReport = (TDSReport) unmarshaller.unmarshal(new StreamSource(tmpPath.toString()));
					individualResponse.setTDSReport(tdsReport);

					Test test = tdsReport.getTest();
					if (test != null) {
						analysisTest(test, individualResponse);
					}

					Examinee examinee = tdsReport.getExaminee();
					if (examinee != null) {
						analysisExaminee(examinee, individualResponse);
					}

					List<Object> listObject = examinee.getExamineeAttributeOrExamineeRelationship();
					List<ExamineeAttribute> listExamineeAttribute = getListExamineeAttribute(listObject);
					if (listExamineeAttribute != null && listExamineeAttribute.size() > 0) {
						analysisExamineeAttribute(listExamineeAttribute, individualResponse, examinee.getKey());
					}

				}
				System.out.println("individualResponse --->" + individualResponse.toString());
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("analysisProcess exception: ", e);
			}

		}
		return analysisResponse;
	}

	// @param -> test object in tds report xml file
	// @param -> individualResponse object includes all of the information for a upload file
	private void analysisTest(Test test, IndividualResponse individualResponse) {

		try {
			// HashMap Key -> Category like Test, Examinee, Opportunity...
			HashMap<String, Hashtable<String, FieldCheckType>> mapCategoryField = individualResponse.getMapCategoryField();

			// Hashtable Key -> TDS Field Name like EnumFieldName.TestName, EnumFieldName.TestSubject...
			Hashtable<String, FieldCheckType> tdsFieldResultMap = new Hashtable<String, FieldCheckType>();

			mapCategoryField.put("Test", tdsFieldResultMap);
			validateTest(test, tdsFieldResultMap);
			System.out.println("individualResponse ========>" + individualResponse);
		} catch (Exception e) {
			logger.error("analysisTest exception: ", e);
		}

	}

	// @param -> examinee object in tds report xml file
	// @param -> individualResponse object includes all of the information for a upload file
	private void analysisExaminee(Examinee examinee, IndividualResponse individualResponse) {

		try {
			// HashMap Key -> Category like Test, Examinee, Opportunity...
			HashMap<String, Hashtable<String, FieldCheckType>> mapCategoryField = individualResponse.getMapCategoryField();

			// Hashtable Key -> TDS Field Name
			Hashtable<String, FieldCheckType> tdsFieldResultMap = new Hashtable<String, FieldCheckType>();

			mapCategoryField.put("Examinee", tdsFieldResultMap);
			validateExaminee(examinee, tdsFieldResultMap);
		} catch (Exception e) {
			logger.error("analysisExaminee exception: ", e);
		}

	}

	// @param -> listExamineeAttribute object in tds report xml file includes DOB, HispanicOrLatinoEthnicity. . .
	// @param -> individualResponse object includes all of the information for a upload file
	// @param -> examineeKey - student ID
	private void analysisExamineeAttribute(List<ExamineeAttribute> listExamineeAttribute, 
			IndividualResponse individualResponse, Long examineeKey ) {

		try {
			// HashMap Key -> Category like Test, Examinee, Opportunity...
			HashMap<String, Hashtable<String, FieldCheckType>> mapCategoryField = individualResponse.getMapCategoryField();
			
			// Hashtable Key -> TDS Field Name like EnumFieldName.TestName, EnumFieldName.TestSubject...
			Hashtable<String, FieldCheckType> tdsFieldResultMap = new Hashtable<String, FieldCheckType>();
			
			mapCategoryField.put("ExamineeAttribute", tdsFieldResultMap);
			validateExamineeAttributes(listExamineeAttribute, tdsFieldResultMap, examineeKey);

			System.out.println("individualResponse ========>" + individualResponse);
		} catch (Exception e) {
			logger.error("analysisExamineeAttribute exception: ", e);
		}

	}

	// @param -> test the Test object in tds report xml file.
	// @param -> tdsFieldResultMap hashTable Key -> TDS Field Name.
	// Validation rules based on Alan's "TDS Field Validation Type.xlsx".
	private void validateTest(Test test, Hashtable<String, FieldCheckType> tdsFieldResultMap) {

		try {
			FieldCheckType fieldCheckType;

			// validate name
			fieldCheckType = new FieldCheckType();
			tdsFieldResultMap.put("name", fieldCheckType);
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			validateField(test, EnumFieldName.TestName, EnumFieldCheckType.PC, fieldCheckType);
			System.out.println("fieldCheckType for name ---->" + fieldCheckType);

			// validate subject
			fieldCheckType = new FieldCheckType();
			tdsFieldResultMap.put("subject", fieldCheckType);
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			validateField(test, EnumFieldName.TestSubject, EnumFieldCheckType.PC, fieldCheckType);
			System.out.println("fieldCheckType for subject ---->" + fieldCheckType);

			// validate testId
			fieldCheckType = new FieldCheckType();
			tdsFieldResultMap.put("testId", fieldCheckType);
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			validateField(test, EnumFieldName.TestTestId, EnumFieldCheckType.PC, fieldCheckType);
			// System.out.println("fieldCheckType for testId ---->" + fieldCheckType);

		} catch (Exception e) {
			logger.error("validateTest exception: ", e);
		}
	}

	// @param -> examinee the Examinee object in tds report xml file.
	// @param -> tdsFieldResultMap hashTable Key -> TDS Field Name.
	private void validateExaminee(Examinee examinee, Hashtable<String, FieldCheckType> tdsFieldResultMap) {

		try {
			FieldCheckType fieldCheckType;

			// validate key
			fieldCheckType = new FieldCheckType();
			tdsFieldResultMap.put("key", fieldCheckType);
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.D);
			// validateField(examinee, EnumFieldName.ExamineeKey, EnumFieldCheckType.D, fieldCheckType);

		} catch (Exception e) {
			logger.error("validateExaminee exception: ", e);
		}

	}

	// @param -> listExamineeAttribute object in tds report xml file includes DOB, HispanicOrLatinoEthnicity. . .
	// @param -> tdsFieldResultMap hashTable Key -> TDS Field Name.
	// @param -> examineeKey - student ID
	private void validateExamineeAttributes(List<ExamineeAttribute> listExamineeAttribute, 
			Hashtable<String, FieldCheckType> tdsFieldResultMap, Long examineeKey){
		try {
			System.out.println("examineeKey");
			Student student = studentService.getStudentByStudentSSID(examineeKey.toString());
			FieldCheckType fieldCheckType;
			for (ExamineeAttribute e : listExamineeAttribute) {
				System.out.println("e...." + e.getName());
				String nameValue = e.getName(); //DOB, HispanicOrLatinoEthnicity, AmericanIndianOrAlaskaNative
				
				fieldCheckType = new FieldCheckType();
				tdsFieldResultMap.put(nameValue, fieldCheckType);
				fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
				validateField(nameValue, EnumFieldName.ExamineeAttributeName, EnumFieldCheckType.PC, fieldCheckType, student);
			}

			// validate key
			fieldCheckType = new FieldCheckType();
			tdsFieldResultMap.put("key", fieldCheckType);
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.D);
			// validateField(examinee, EnumFieldName.ExamineeKey, EnumFieldCheckType.D, fieldCheckType);

		} catch (Exception e) {
			logger.error("validateExamineeAttributes exception: ", e);
		}

	}
	
	
	private void validateField(Test test, EnumFieldName enumFieldName, EnumFieldCheckType enumFieldCheckType,
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

	private void validateField(String nameValue, EnumFieldName enumFieldName, EnumFieldCheckType enumFieldCheckType,
			FieldCheckType fieldCheckType, Student student) {

		switch (enumFieldCheckType) {
		case D:
			break;
		case P:
			checkP(nameValue, enumFieldName, fieldCheckType);
			break;
		case PC:
			checkP(nameValue, enumFieldName, fieldCheckType);
			checkC(nameValue, enumFieldName, fieldCheckType);
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
	
	// Field Check Type (P) --> check that field is not empty, and field value is of correct data type
	// and within acceptable values
	private void checkP(String value, EnumFieldName enumFieldName, FieldCheckType fieldCheckType) {

		try {
			switch (enumFieldName) {
			case ExamineeAttributeName:
				processP(value, fieldCheckType);
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

	// Field Check Type (PC) --> check everything the same as (P) plus check if field value is correct
	private void checkC(Test test, EnumFieldName enumFieldName, FieldCheckType fieldCheckType) {

		try {
			// String uniqueid = test.getName();
			String uniqueid = "(SBAC_PT)SBAC-Mathematics-11-Spring-2013-2015";
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
	
	// Field Check Type (PC) --> check everything the same as (P) plus check if field value is correct
	private void checkC(String nameValue, EnumFieldName enumFieldName, FieldCheckType fieldCheckType) {

		try {
			switch (enumFieldName) {
			case ExamineeAttributeName:
				processExamineeAttributeName(nameValue, fieldCheckType);
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
			String subjectValueFromTestPackage = getValueFromListProperty(listProperty, "subject");
			if (subjectValueFromTestPackage != null && subjectValueFromTestPackage.equals("MATH") && testSubject.equals("MA")) {
				fieldCheckType.setCorrectValue(true);
			}
		} catch (Exception e) {
			logger.error("processTestSubject exception: ", e);
		}

	}

	private void processTestTestId(Test test, Testpackage testpackage, FieldCheckType fieldCheckType) {
		String testId = test.getTestId();
		/*
		 * List<Property> listProperty = testpackage.getProperty(); String subjectValueFromTestPackage =
		 * getValueFromListProperty(listProperty, "subject"); if (subjectValueFromTestPackage != null &&
		 * subjectValueFromTestPackage.equals("MATH") && testSubject.equals("MA")) { fieldCheckType.setCorrectValue(true); }
		 */
	}
	
	private void processExamineeAttributeName(String nameValue, FieldCheckType fieldCheckType){
		try {
			EnumExamineeAttributeAcceptValues ev = EnumValueExist(nameValue);
			if (ev != null){
				fieldCheckType.setCorrectValue(true);
			}
			
			
		} catch (Exception e) {
			logger.error("processExamineeAttributeName exception: ", e);
		}
	}

	//may need to change this function tomorrow
	private EnumExamineeAttributeAcceptValues EnumValueExist(String nameValue){
		for (EnumExamineeAttributeAcceptValues me : EnumExamineeAttributeAcceptValues.values()) {
	        if (me.name().equalsIgnoreCase(nameValue))
	            return me;
	    }
	    return null;
	}
	
	private String getValueFromListProperty(List<Property> listProperty, String name) {
		try {
			for (Property property : listProperty) {
				if (property.getName().trim().toLowerCase().equals(name)) {
					return property.getValue();
				}
			}
		} catch (Exception e) {
			logger.error("getValueFromListProperty exception: ", e);
		}
		return null;
	}

	private List<ExamineeAttribute> getListExamineeAttribute(List<Object> list) {
		List<ExamineeAttribute> listExamineeAttribute = new ArrayList<ExamineeAttribute>();
		try {
			for (Object o : list) {
				if (o instanceof ExamineeAttribute) {
					listExamineeAttribute.add((ExamineeAttribute) o);
				}
			}
		} catch (Exception e) {
			logger.error("getListExamineeAttribute exception: ", e);
		}
		return listExamineeAttribute;
	}

}
