package org.cresst.sb.irp.dao;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.analysis.AnalysisResponse;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Test;
import org.cresst.sb.irp.domain.testpackage.Property;
import org.cresst.sb.irp.domain.testpackage.Testpackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AnalysisDaoImpl implements AnalysisDao {
	private static Logger logger = Logger.getLogger(AnalysisDaoImpl.class);

	@Autowired
	private TestpackageDao testpackageDao;

	private Map<String, Testpackage> mapTestpackage;

	private Hashtable<String, String> tdsFieldResultMap;

	public AnalysisDaoImpl() {
		logger.info("initializing");
		// mapTestpackage = testpackageDao.getMapTestpackage();
	}

	public enum fieldName {
		subject, testid, bankKey;
	}
	
	@Override
	public void analysisProcess(AnalysisResponse analysisResponse) {
		try {
			HashMap<String, Hashtable<String, FieldCheckType>> mapCategoryField;
			TDSReport tdsReportUpload = analysisResponse.getTDSReport();
			Test test = tdsReportUpload.getTest();
			if (test != null) {
				mapCategoryField = analysisResponse.getMapCategoryField();
				Hashtable<String, FieldCheckType> tdsFieldResultMap = new Hashtable<String, FieldCheckType>();
				mapCategoryField.put("Test", tdsFieldResultMap);
				validateTest(test, tdsFieldResultMap, analysisResponse);
			}

			Examinee examinee = tdsReportUpload.getExaminee();
			if (examinee != null) {
				mapCategoryField = analysisResponse.getMapCategoryField();
				Hashtable<String, FieldCheckType> tdsFieldResultMap = new Hashtable<String, FieldCheckType>();
				mapCategoryField.put("Examinee", tdsFieldResultMap);
				validateExaminee(examinee, tdsFieldResultMap);
			}

		} catch (Exception e) {
			logger.error("analysisProcess exception: ", e);
		}

	}

	private void validateTest(Test test,
			Hashtable<String, FieldCheckType> tdsFieldResultMap,
			AnalysisResponse analysisResponse) {
		try {
			Testpackage testpackage = null;
			String name = test.getName();
			if (!name.isEmpty()) {
				analysisResponse.setFileName(name);
				FieldCheckType fieldCheckType = new FieldCheckType();
				fieldCheckType.set_fieldCheckType(fieldCheckType
						.get_fieldCheckType().PC);
				fieldCheckType.setCorrectDataType(true);
				fieldCheckType.setFieldEmpty(false);
				tdsFieldResultMap.put("name", fieldCheckType);
				if (StringUtils.isAsciiPrintable(name)) {
					fieldCheckType.setAcceptableValue(true);
				}
				mapTestpackage = testpackageDao.getMapTestpackage();
				String uniqueid = "(SBAC_PT)SBAC-Mathematics-11-Spring-2013-2015";
				testpackage = testpackageDao.getTestpackage2(uniqueid);
				// Testpackage testpackage =
				// testpackageDao.getTestpackage2(name);
				if (testpackage != null) {
					fieldCheckType.setCorrectValue(true);
				}
				System.out.println("fieldCheckType ==>"
						+ fieldCheckType.toString());
			}

			String subject = test.getSubject();
			validateTDSField(subject, tdsFieldResultMap, testpackage, fieldName.subject, "PC");
			
			String testId = test.getTestId();
			validateTDSField(testId, tdsFieldResultMap, testpackage, fieldName.testid, "PC");

		} catch (Exception e) {
			logger.error("validateTest exception: ", e);
		}
	}

	private void validateTDSField(String fieldValue,
			Hashtable<String, FieldCheckType> tdsFieldResultMap,
			Testpackage testpackage,
			fieldName fieldNameIn, 
			String checkType) {
		try {
			if (!fieldValue.isEmpty()) {
				FieldCheckType fieldCheckType = new FieldCheckType();
				if (checkType.equals("PC"))
					fieldCheckType.set_fieldCheckType(fieldCheckType
							.get_fieldCheckType().PC);
				else if (checkType.equals("P"))
					fieldCheckType.set_fieldCheckType(fieldCheckType
							.get_fieldCheckType().P);
				else if (checkType.equals("D"))
					fieldCheckType.set_fieldCheckType(fieldCheckType
							.get_fieldCheckType().D);
				fieldCheckType.setCorrectDataType(true);
				fieldCheckType.setFieldEmpty(false);
				tdsFieldResultMap.put(fieldNameIn.toString(), fieldCheckType);
				if (StringUtils.isAsciiPrintable(fieldValue)) {
					fieldCheckType.setAcceptableValue(true);
				}
				
				if (fieldNameIn == fieldName.subject){
					List<Property> listProperty = testpackage.getProperty();
					for (Property temp : listProperty) {
						String name = temp.getName();
						if(name.toLowerCase().equals("subject")){
							fieldCheckType.setCorrectValue(true);
						}
					}
				}else if (fieldNameIn == fieldName.testid){
					
				}
				
				System.out.println("fieldCheckType ==>" + fieldCheckType.toString());
			}
		} catch (Exception e) {
			logger.error("validateTest exception: ", e);
		}
	}

	private void validateExaminee(Examinee examinee,
			Hashtable<String, FieldCheckType> tdsFieldResultMap) {
		try {

			/*
			 * String name = test.getName(); if (!name.isEmpty()) { if
			 * (StringUtils.isAsciiPrintable(name)){
			 * tdsFieldResultMap.put("name", "valid"); } }
			 * 
			 * System.out.println("b2 b2 ->" +
			 * StringUtils.isAsciiPrintable("Ceki G\u00fclc\u00fc"));
			 * System.out.println("b3 b3 ->" +
			 * StringUtils.isAsciiPrintable("-MATH-7-Fall-2013"));
			 */
		} catch (Exception e) {
			logger.error("validateExaminee exception: ", e);
		}
	}

}
