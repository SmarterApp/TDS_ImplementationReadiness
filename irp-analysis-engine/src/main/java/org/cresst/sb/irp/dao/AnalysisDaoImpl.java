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
import org.cresst.sb.irp.service.TDSReportService;
import org.cresst.sb.irp.service.TestPackageService;
import org.cresst.sb.irp.utils.ProcessAnalysisAction;
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
	public ProcessAnalysisAction processAnalysisAction;
	
	@Autowired
	public StudentService studentService;

	@Value("classpath:irp-package/reportxml_oss.xsd") //from Rami on 12/4/14 vs sample_oss_report2.xml
	private Resource TDSReportXSDResource;

	@Autowired
	private XMLValidate xmlValidate;

	@Autowired
	private Unmarshaller unmarshaller;

	public AnalysisDaoImpl() {
		logger.info("initializing");
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

					processAnalysisAction.setIndividualResponse(individualResponse);
					processAnalysisAction.setTdsReport(tdsReport);
					
					processAnalysisAction.analysisTest();
					processAnalysisAction.analysisExaminee();
					processAnalysisAction.analysisExamineeAttribute();
					
					System.out.println("individualResponse ->" + individualResponse.toString());
					
					//List<Object> listObject = examinee.getExamineeAttributeOrExamineeRelationship();
					//List<ExamineeAttribute> listExamineeAttribute = getListExamineeAttribute(listObject);
					/*if (listExamineeAttribute != null && listExamineeAttribute.size() > 0) {
						analysisExamineeAttribute(listExamineeAttribute, individualResponse, examinee.getKey());
					}*/

				}
				System.out.println("individualResponse --->" + individualResponse.toString());
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("analysisProcess exception: ", e);
			}

		}
		return analysisResponse;
	}


	// @param -> listExamineeAttribute object in tds report xml file includes DOB, HispanicOrLatinoEthnicity. . .
	// @param -> individualResponse object includes all of the information for a upload file
	// @param -> examineeKey - student ID
	private void analysisExamineeAttribute(List<ExamineeAttribute> listExamineeAttribute, 
			IndividualResponse individualResponse, Long examineeKey ) {

		try {
			// HashMap Key -> Category like Test, Examinee, Opportunity...
			//HashMap<String, Hashtable<String, FieldCheckType>> mapCategoryField = individualResponse.getMapCategoryField();
			
			// Hashtable Key -> TDS Field Name like EnumFieldName.TestName, EnumFieldName.TestSubject...
			//Hashtable<String, FieldCheckType> tdsFieldResultMap = new Hashtable<String, FieldCheckType>();
			
		//	mapCategoryField.put("ExamineeAttribute", tdsFieldResultMap);
		//	validateExamineeAttributes(listExamineeAttribute, tdsFieldResultMap, examineeKey);
		} catch (Exception e) {
			logger.error("analysisExamineeAttribute exception: ", e);
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
			//	validateField(nameValue, EnumFieldName.ExamineeAttributeName, EnumFieldCheckType.PC, fieldCheckType, student);
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
	

}
