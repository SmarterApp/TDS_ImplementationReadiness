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

	public enum EnumExamineeAttributeContextAcceptValues {
		INITIAL, FINAL;
	}

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
		Student student = new Student();
		try {
			student = studentService.getStudentByStudentSSID(key.toString());
			if (student != null) {
				return student;
			}
		} catch (Exception e) {
			logger.error("getStudent exception: ", e);
		}
		return null;
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

	public boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
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

	abstract public void analysis() throws IOException;

	public void validateToken(String fieldValue, FieldCheckType fieldCheckType) {
		try {
			if (fieldValue != null && !fieldValue.trim().isEmpty()) {
				fieldCheckType.setCorrectDataType(true);
				fieldCheckType.setFieldEmpty(false);
			}
		} catch (Exception e) {
			logger.error("validateToken exception: ", e);
		}
	}

	// One or more printable ASCII characters
	public void validatePritableASCIIone(String fieldValue, FieldCheckType fieldCheckType) {
		try {
			if (fieldValue != null && !fieldValue.trim().isEmpty() && StringUtils.isAsciiPrintable(fieldValue)) {
				fieldCheckType.setAcceptableValue(true);
			}
		} catch (Exception e) {
			logger.error("validatePritableASCIIone exception: ", e);
		}
	}

	// Zero or more printable ASCII characters
	public void validatePritableASCIIzero(String fieldValue, FieldCheckType fieldCheckType) {
		try {
			if (fieldValue != null && StringUtils.isAsciiPrintable(fieldValue)) {
				fieldCheckType.setAcceptableValue(true);
			}
		} catch (Exception e) {
			logger.error("validatePritableASCIIzero exception: ", e);
		}
	}

	public void validateUnsignedIntPositive32bit(String inputValue, FieldCheckType fieldCheckType) {
		try {
			if (isInteger(inputValue)) {
				int num = getNumberOfBits(Integer.parseInt(inputValue));
				if (num > 0 && num <= 32) {
					fieldCheckType.setCorrectDataType(true);
					fieldCheckType.setFieldEmpty(false);
					fieldCheckType.setAcceptableValue(true);
				}
			}
		} catch (Exception e) {
			logger.error("validateUnsignedIntPositive32bit exception: ", e);
		}
	}

	public void validateUnsignedLongPositive64bit(long number, FieldCheckType fieldCheckType) {
		try {
			long num = sign(number);
			if (num > 0) {
				fieldCheckType.setCorrectDataType(true);
				fieldCheckType.setFieldEmpty(false);
				fieldCheckType.setAcceptableValue(true);
			}
		} catch (Exception e) {
			logger.error("validateUnsignedLongPositive64bit exception: ", e);
		}
	}

	public void validateUnsignedInt(String inputValue, FieldCheckType fieldCheckType, int firstValue, int secondValue) {
		try {
			if (isInteger(inputValue)) {
				if (Integer.parseInt(inputValue) == firstValue || Integer.parseInt(inputValue) == secondValue) {
					fieldCheckType.setCorrectDataType(true);
					fieldCheckType.setFieldEmpty(false);
					fieldCheckType.setAcceptableValue(true);
				}
			}

		} catch (Exception e) {
			logger.error("validateUnsignedInt exception: ", e);
		}
	}



	public void processAcceptableContextEnum(String fieldValue, FieldCheckType fieldCheckType,
			Class<EnumExamineeAttributeContextAcceptValues> class1) {
		try {
			System.out.println("fieldValue ->" + fieldValue);
			if (fieldValue != null && !fieldValue.trim().isEmpty()) {
				if (EnumUtils.isValidEnum(class1, fieldValue)) {
					fieldCheckType.setAcceptableValue(true);
				}
			}
		} catch (Exception e) {
			logger.error("processAcceptableContextEnum exception: ", e);
		}
	}

	public String getStudentValueByName(String fieldNameValue, Class<EnumExamineeAttributeAcceptValues> class1, Student student) {
		String str = null;
		try {
			if (EnumUtils.isValidEnum(class1, fieldNameValue)) {
				for (EnumExamineeAttributeAcceptValues e : EnumExamineeAttributeAcceptValues.values()) {
					if (e.name().equals(fieldNameValue)
							&& e.name().equals(EnumExamineeAttributeAcceptValues.FirstName.toString())) {
						str = student.getFirstName();
					} else if (e.name().equals(fieldNameValue)
							&& e.name().equals(EnumExamineeAttributeAcceptValues.LastOrSurname.toString())) {
						str = student.getLastOrSurname();
					} else if (e.name().equals(fieldNameValue)
							&& e.name().equals(EnumExamineeAttributeAcceptValues.StudentIdentifier.toString())) {
						str = student.getSSID();
					} else if (e.name().equals(fieldNameValue)
							&& e.name().equals(EnumExamineeAttributeAcceptValues.DOB.toString())) {
						str = student.getBirthdate();
					} else if (e.name().equals(fieldNameValue)
							&& e.name().equals(EnumExamineeAttributeAcceptValues.BlackOrAfricanAmerican.toString())) {
						str = student.getBlackOrAfricanAmerican();
					}// ......
				}
			}
		} catch (Exception e) {
			logger.error("getStudentValueByName exception: ", e);
		}
		return str;
	}

	// //////////////////////////////

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

	public void processP(String str, FieldCheckType fieldCheckType) {
		if (str != null && str.length() > 0) {
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
	
	public void processP_AcceptValue(int inputValue, FieldCheckType fieldCheckType, int firstValue, int secondValue){
		if (inputValue == firstValue || inputValue == secondValue) {
			setPcorrect(fieldCheckType);
		}
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

}
