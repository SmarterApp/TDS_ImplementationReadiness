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
import org.cresst.sb.irp.utils.AnalysisAction;
import org.cresst.sb.irp.utils.ExamineeAnalysisAction;
import org.cresst.sb.irp.utils.ExamineeAttributeAnalysisAction;
import org.cresst.sb.irp.utils.TestAnalysisAction;
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
	public TestAnalysisAction testAnalysisAction;
	
	@Autowired
	public ExamineeAnalysisAction examineeAnalysisAction;
	
	@Autowired
	public ExamineeAttributeAnalysisAction examineeAttributeAnalysisAction;
	
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
					
					testAnalysisAction.setIndividualResponse(individualResponse);
					testAnalysisAction.setTdsReport(tdsReport);
					testAnalysisAction.analysis();
					
					examineeAnalysisAction.setIndividualResponse(individualResponse);
					examineeAnalysisAction.setTdsReport(tdsReport);
					examineeAnalysisAction.analysis();
					
					examineeAttributeAnalysisAction.setIndividualResponse(individualResponse);
					examineeAttributeAnalysisAction.setTdsReport(tdsReport);
					examineeAttributeAnalysisAction.analysis();
					
					System.out.println("individualResponse 22222222222 ->" + individualResponse.toString());

				}
				System.out.println("individualResponse --->" + individualResponse.toString());
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("analysisProcess exception: ", e);
			}

		}
		return analysisResponse;
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
