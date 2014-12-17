package org.cresst.sb.irp.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.cresst.sb.irp.dao.ExamineeAttributeAnalysisAction.EnumExamineeAttributeAcceptValues;
import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.items.Itemrelease;
import org.cresst.sb.irp.domain.student.Student;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Test;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee.ExamineeAttribute;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee.ExamineeRelationship;
import org.cresst.sb.irp.domain.testpackage.Property;
import org.cresst.sb.irp.domain.testpackage.Testpackage;
import org.cresst.sb.irp.service.ItemService;
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

	@Autowired
	public ItemService itemService;

	private IndividualResponse individualResponse;

	public AnalysisAction() {
		logger.info("initializing");
	}

	abstract public void analysis() throws IOException;
	
	public IndividualResponse getIndividualResponse() {
		return individualResponse;
	}

	public void setIndividualResponse(IndividualResponse individualResponse) {
		this.individualResponse = individualResponse;
	}

	public Test getTest(TDSReport tdsReport) {
		Test test = null;
		try {
			test = tdsReport.getTest();
			if (test != null) {
				return test;
			}
		} catch (Exception e) {
			logger.error("getTest exception: ", e);
		}
		return null;
	}

	public Opportunity getOpportunity(TDSReport tdsReport) {
		Opportunity opportunity = new Opportunity();
		try {
			opportunity = tdsReport.getOpportunity();
			if (opportunity != null) {
				return opportunity;
			}
		} catch (Exception e) {
			logger.error("getOpportunity exception: ", e);
		}
		return null;
	}

	public Testpackage getTestpackageByIdentifierUniqueid(String uniqueid) {
		return testPackageService.getTestpackageByIdentifierUniqueid(uniqueid);
	}

	public String getSubjectPropertyValueFromListProperty(List<Property> listProperty) {
		return testPackageService.getSubjectPropertyValueFromListProperty(listProperty);
	}

	public String getGradePropertyValueFromListProperty(List<Property> listProperty) {
		return testPackageService.getGradePropertyValueFromListProperty(listProperty);
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

	public List<ExamineeRelationship> getExamineeRelationships(Examinee examinee) {
		List<ExamineeRelationship> listExamineeRelationship = new ArrayList<ExamineeRelationship>();
		try {
			listExamineeRelationship = tdsReportService.getExamineeRelationships(examinee);
			if (listExamineeRelationship != null) {
				return listExamineeRelationship;
			}
		} catch (Exception e) {
			logger.error("getExamineeRelationships exception: ", e);
		}
		return null;
	}

	public Student getStudent(Long key) {
			return studentService.getStudentByStudentSSID(key.toString());
	}

	public org.cresst.sb.irp.domain.items.Itemrelease.Item getItemByIdentifier(String identifier) {
		return itemService.getItemByIdentifier(identifier);
	}

	public Itemrelease.Item.Attriblist getItemAttriblistFromIRPitem(org.cresst.sb.irp.domain.items.Itemrelease.Item irpItem) {
		return itemService.getItemAttriblistFromIRPitem(irpItem);
	}

	public Itemrelease.Item.Attriblist.Attrib getItemAttribValueFromIRPitemAttriblist(Itemrelease.Item.Attriblist attriblist,
			String attid) {
		return itemService.getItemAttribValueFromIRPitemAttriblist(attriblist, attid);
	}

	public Itemrelease.Item.Attriblist.Attrib getItemAttribValueFromIRPitem(
			org.cresst.sb.irp.domain.items.Itemrelease.Item irpItem, String attid) {
		return itemService.getItemAttribFromIRPitem(irpItem, attid);
	}

	private int sign(long i) {
		if (i == 0)
			return 0;
		if (i >> 63 != 0)
			return -1;
		return +1;
	}

	public int getNumberOfBits(int N) {
		int bits = 0;
		while (Math.pow(2, bits) <= N) {
			bits++;
		}
		return bits;
	}

	public boolean isFloat(String str) {
		try {
			Float.parseFloat(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public boolean isItemFormatByValue(List<CellCategory> listItemAttribute, String value) {
		boolean bln = false;
		try {
			for (CellCategory c : listItemAttribute) {
				if (c.getTdsFieldName().equals("format") && c.getTdsFieldNameValue().equals(value)) {
					bln = true;
				}
			}
		} catch (Exception e) {
			logger.error("isItemFormatByValue exception: ", e);
		}
		return bln;
	}

	public String getItemBankKeyKeyFromItemAttribute(List<CellCategory> listItemAttribute) {
		String str = "";
		try {
			for (CellCategory c : listItemAttribute) {
				if (c.getTdsFieldName().equals("bankKey") || c.getTdsFieldName().equals("key")) {
					str = str.concat(c.getTdsFieldNameValue()).concat("-");
				}
			}
			if (str.endsWith("-"))
				str = str.substring(0, str.length() - 1);
		} catch (Exception e) {
			logger.error("validateToken exception: ", e);
		}
		return str;
	}

	public void setAllCorrectFieldCheckType(FieldCheckType fieldCheckType) {
		fieldCheckType.setCorrectDataType(true);
		fieldCheckType.setFieldEmpty(false);
		fieldCheckType.setAcceptableValue(true);
		fieldCheckType.setCorrectValue(true);
	}

	public void setPcorrect(FieldCheckType fieldCheckType) {
		fieldCheckType.setCorrectDataType(true);
		fieldCheckType.setFieldEmpty(false);
		fieldCheckType.setAcceptableValue(true);
	}

	public void setCcorrect(FieldCheckType fieldCheckType) {
		fieldCheckType.setCorrectValue(true);
	}

	public void processP(String str, FieldCheckType fieldCheckType, boolean required) {
		if ((!required) || (str != null && str.length() > 0)) {
			setPcorrect(fieldCheckType);
		}
	}

	public boolean isCorrectValue(String v1, String v2) {
		if (v1.trim().toLowerCase().equals(v2.trim().toLowerCase()))
			return true;
		else
			return false;
	}

	public void processP_Positive32bit(String inputValue, FieldCheckType fieldCheckType) {
		try {
			int num = getNumberOfBits(Integer.parseInt(inputValue));
			if (num > 0 && num <= 32) {
				setPcorrect(fieldCheckType);
			}
		} catch (Exception e) {
			logger.error("processP_Positive32bit exception: ", e);
		}
	}
	
	public void processP_Positive64bit(Long inputValue, FieldCheckType fieldCheckType) {
		try {
			long num = sign(inputValue);
			if (num > 0) {
				setPcorrect(fieldCheckType);
			}
		} catch (Exception e) {
			logger.error("processP_Positive64bit exception: ", e);
		}
	}
	
	public void processP_PritableASCIIone(String inputValue, FieldCheckType fieldCheckType){
		if (inputValue.length() > 0 && StringUtils.isAsciiPrintable(inputValue)){
			setPcorrect(fieldCheckType);
		}
	}
	
	public void processP_PritableASCIIzero(String inputValue, FieldCheckType fieldCheckType){
		if (inputValue != null && StringUtils.isAsciiPrintable(inputValue)){
			setPcorrect(fieldCheckType);
		}
	}
	
	
	public void processP_AcceptValue(int inputValue, FieldCheckType fieldCheckType, int firstValue, int secondValue){
		if (inputValue == firstValue || inputValue == secondValue) {
			setPcorrect(fieldCheckType);
		}
	}
	
	public void processP_Year(Long year, FieldCheckType fieldCheckType){
		if (year != null){
			if (1900 <= year && year <=9999){
				setPcorrect(fieldCheckType);
			}
		}
	}
	
	public boolean isValidYear(Long year)
	{
		if (1900 <= year && year <=9999)
			return true;
		else
			return false;
	}
	
	public boolean isValidMonth(Long month)
	{
		if (01 <= month && month <=12)
			return true;
		else
			return false;
	}
	
	public boolean isValidDate(Long date)
	{
		if (0 < date && date <=31)
			return true;
		else
			return false;
	}
	
	public void validateUnsignedFloat(String inputValue, FieldCheckType fieldCheckType, int allowedValue) {
		try {
			if (isFloat(inputValue)) {
				float fValue = Float.parseFloat(inputValue);
				if (fValue >= 0 || fValue == allowedValue) {
					setPcorrect(fieldCheckType);
				}
			}
		} catch (Exception e) {
			logger.error("validateUnsignedFloat exception: ", e);
		}
	}
	
	
	public void processAcceptValue(String value, FieldCheckType fieldCheckType, List<String> listGradeAcceptValues){
		if (value.length() > 0){
			for(String listItem: listGradeAcceptValues){
				if (listItem.equals(value))
					fieldCheckType.setCorrectValue(true);
			}
		}
	}
	
	public void processDate(String date, FieldCheckType fieldCheckType){ // effectiveDate="2014-07-07"
		String[] array = date.split("-");
		if (array.length == 3){
			boolean blnYear = isValidYear(Long.parseLong(array[0]));
			boolean blnMonth = isValidMonth(Long.parseLong(array[1]));
			boolean blnDate = isValidDate(Long.parseLong(array[2]));
			if (blnYear && blnMonth && blnDate)
				setPcorrect(fieldCheckType);
		}
	}

}
