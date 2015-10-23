package org.cresst.sb.irp.analysis.engine;

import org.apache.commons.lang3.StringUtils;
import org.cresst.sb.irp.domain.accommodation.Accommodation;
import org.cresst.sb.irp.domain.analysis.Category;
import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.items.Itemrelease;
import org.cresst.sb.irp.domain.student.Student;
import org.cresst.sb.irp.domain.testpackage.Testspecification;
import org.cresst.sb.irp.domain.teststudentmapping.TestStudentMapping;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.cresst.sb.irp.service.AccommodationService;
import org.cresst.sb.irp.service.ItemService;
import org.cresst.sb.irp.service.StudentService;
import org.cresst.sb.irp.service.TDSReportService;
import org.cresst.sb.irp.service.TestPackageService;
import org.cresst.sb.irp.service.TestStudentMappingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.ImmutableList;

import java.text.Collator;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Base class for various TDS Report XML analyzers.
 *
 * @param <T>
 *            Any object that contains the fields being analyzed.
 * @param <E>
 *            An enum to represent the field that is being analyzed.
 * @param <O>
 *            Any object that needs to be passed through to help validate a field.
 */
public abstract class AnalysisAction<T, E extends Enum, O> {
	private final static Logger logger = LoggerFactory.getLogger(AnalysisAction.class);
	
	@Autowired
	public AccommodationService accommodationService;
	
	@Autowired
	public TestPackageService testPackageService;

	@Autowired
	public StudentService studentService;
	
	@Autowired
	public TDSReportService tdsReportService;

	@Autowired
	public ItemService itemService;

	@Autowired
	public TestStudentMappingService testStudentMappingService;
	

	/**
	 * Analyze the TDS Report
	 *
	 * @param individualResponse
	 *            Where the TDS Report is obtained and where the result is stored
	 */
	abstract public void analyze(IndividualResponse individualResponse);

	/**
	 * Field Check Type (P) --> check that field is not empty, and field value is of correct data type and within acceptable
	 * values
	 *
	 * @param checkObj
	 *            Object with fields to check
	 * @param enumFieldName
	 *            Specifies the field to check
	 * @param fieldCheckType
	 *            This is where the results are stored
	 */
	abstract protected void checkP(T checkObj, E enumFieldName, FieldCheckType fieldCheckType);

	/**
	 * Checks if the field has the correct value
	 *
	 * @param checkObj
	 *            Object with field to check
	 * @param enumFieldName
	 *            Specifies the field to check
	 * @param fieldCheckType
	 *            This is where the results are stored
	 */
	abstract protected void checkC(T checkObj, E enumFieldName, FieldCheckType fieldCheckType, O comparisonData);

	/**
	 * Gets the expected value that the field being analyzed should contain. The default implementation returns a null String.
	 * AnalysisAction classes should override this method if they are analyzing data with expected values.
	 *
	 * @param comparisonData
	 *            The IRP object (e.g. Student, Testpackage, etc) containing the field with the expected value
	 * @param enumFieldName
	 *            Specifies the field to check
	 * @return
	 */
	protected String expectedValue(O comparisonData, E enumFieldName) {
		return null;
	}

	/**
	 * Validates the checkObj's field
	 *
	 * @param category
	 *            The category to store the result
	 * @param checkObj
	 *            The object containing the field to check
	 * @param value
	 *            The value of the field that is being checked (it's simply converted to a String)
	 * @param enumFieldCheckType
	 *            Specifies the type of field check to perform
	 * @param enumFieldName
	 *            The name of the field being analyzed as Category specific Enum
	 * @param <U>
	 *            Since checkObj has many different types of values this generic parameter was introduced to account for the
	 *            differences.
	 */
	protected <U> void validate(Category category, T checkObj, U value,
			FieldCheckType.EnumFieldCheckType enumFieldCheckType, E enumFieldName, O comparisonData) {
		final FieldCheckType fieldCheckType = new FieldCheckType();
		fieldCheckType.setEnumfieldCheckType(enumFieldCheckType);

		final CellCategory cellCategory = new CellCategory();
		cellCategory.setTdsFieldName(enumFieldName.toString());
		cellCategory.setTdsFieldNameValue(Objects.toString(value, ""));
		cellCategory.setTdsExpectedValue(expectedValue(comparisonData, enumFieldName));
		cellCategory.setFieldCheckType(fieldCheckType);

		category.addCellCategory(cellCategory);

		checkField(checkObj, enumFieldCheckType, enumFieldName, fieldCheckType, comparisonData);
		System.out.println("cellCategory ==>" + cellCategory.toString());
	}

	/**
	 * 
	 * @param cellCategories
	 *            The cellCategories store the list CellCategory
	 * @param key
	 *            The tdsFieldName used to retrieve tdsFieldValue in CellCategory
	 * @return the tdsFieldNameValue in CellCategory object
	 */
	protected String getTdsFieldNameValueByFieldName(ImmutableList<CellCategory> cellCategories, String key) {
		for (CellCategory cellCategory : cellCategories) {
			if (cellCategory.getTdsFieldName().equals(key))
				return cellCategory.getTdsFieldNameValue();
		}
		return null;
	}

	/**
	 * Checks the field
	 *
	 * @param checkObj
	 *            The object containing the field to check
	 * @param enumFieldCheckType
	 *            Specifies the type of field check to perform
	 * @param enumFieldName
	 *            The name of the field being analyzed as Category specific Enum
	 * @param fieldCheckType
	 *            This is where the result is stored
	 */
	protected void checkField(T checkObj, FieldCheckType.EnumFieldCheckType enumFieldCheckType, E enumFieldName,
			FieldCheckType fieldCheckType, O comparisonData) {

		switch (enumFieldCheckType) {
		case D:
			break;
		case P:
			checkP(checkObj, enumFieldName, fieldCheckType);
			break;
		case PC:
			checkP(checkObj, enumFieldName, fieldCheckType);
			checkC(checkObj, enumFieldName, fieldCheckType, comparisonData);
			break;
		}
	}

	public Testspecification getTestpackageByIdentifierUniqueid(String uniqueid) {
		return testPackageService.getTestpackageByIdentifierUniqueid(uniqueid);
	}

	public Map<String, Testspecification> getMapTestpackage() {
		return testPackageService.getMapTestpackage();
	}

	public Accommodation getAccommodation(long key) throws NotFoundException {
		return accommodationService.getAccommodationByStudentIdentifier(key);
	}
	
	public Student getStudent(long key) throws NotFoundException {
		return studentService.getStudentByStudentSSID(key);
	}

	public TestStudentMapping getTestStudentMapping(String testName, long studentSSID) throws NotFoundException {
		return testStudentMappingService.getTestStudentMapping(testName, studentSSID);
	}
	
	public org.cresst.sb.irp.domain.items.Itemrelease.Item getItemByIdentifier(String identifier) {
		return itemService.getItemByIdentifier(identifier);
	}

	public Itemrelease.Item.Attriblist getItemAttriblistFromIRPitem(
			org.cresst.sb.irp.domain.items.Itemrelease.Item irpItem) {
		return itemService.getItemAttriblistFromIRPitem(irpItem);
	}

	public Itemrelease.Item.Attriblist.Attrib getItemAttribValueFromIRPitemAttriblist(
			Itemrelease.Item.Attriblist attriblist, String attid) {
		return itemService.getItemAttribValueFromIRPitemAttriblist(attriblist, attid);
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
		if (!required || StringUtils.isNotEmpty(str)) {
			setPcorrect(fieldCheckType);
		}
	}

	public boolean isCorrectValue(String v1, String v2) {
		return StringUtils.equalsIgnoreCase(v1.trim(), v2.trim());
	}

	public <U> boolean compare(List<U> list1, List<U> list2) {
		if (list1 == null && list2 == null)
			return true;
		if ((list1 != null && list2 == null) || (list1 == null && list2 != null) || (list1.size() != list2.size()))
			return false;
		Collections.sort(list1, Collator.getInstance());
		Collections.sort(list2, Collator.getInstance());
		return list1.equals(list2);
	}

	public void processP_Positive32bit(String inputValue, FieldCheckType fieldCheckType) {
		try {
			if (inputValue != null) {
				int num = Integer.parseInt(inputValue);
				if (num >= 0) {
					setPcorrect(fieldCheckType);
				}
			}
		} catch (NumberFormatException e) {
			logger.info("Number not an integer: " + inputValue);
		}
	}

	public void processP_Positive64bit(Long inputValue, FieldCheckType fieldCheckType) {
		if (inputValue != null && inputValue >= 0) {
			setPcorrect(fieldCheckType);
		}
	}

	public void processP_PritableASCIIone(String inputValue, FieldCheckType fieldCheckType) {
		if (StringUtils.isNotBlank(inputValue) && StringUtils.isAsciiPrintable(inputValue)) {
			setPcorrect(fieldCheckType);
		}
	}

	public void processP_PritableASCIIzero(String inputValue, FieldCheckType fieldCheckType) {
		if (inputValue != null && StringUtils.isAsciiPrintable(inputValue)) {
			setPcorrect(fieldCheckType);
		}
	}

	public void processP_AcceptValue(int inputValue, FieldCheckType fieldCheckType, int firstValue, int secondValue) {
		if (inputValue == firstValue || inputValue == secondValue) {
			setPcorrect(fieldCheckType);
		}
	}

	public void processP_Year(Long year, FieldCheckType fieldCheckType) {
		if (year != null && isValidYear(year)) {
			setPcorrect(fieldCheckType);
		}
	}

	public boolean isValidYear(Long year) {
		return 1900 <= year && year <= 9999;
	}

	public boolean isValidMonth(Long month) {
		return 1 <= month && month <= 12;
	}

	public boolean isValidDate(Long date) {
		return 0 < date && date <= 31;
	}

	public void validateUnsignedFloat(String inputValue, FieldCheckType fieldCheckType, int allowedValue) {
		try {
			if (inputValue != null) {
				float fValue = Float.parseFloat(inputValue.trim());
				if (fValue >= 0 || fValue == allowedValue) {
					setPcorrect(fieldCheckType);
				}
			}
		} catch (NumberFormatException e) {
			logger.info("Not a float: " + inputValue);
		}
	}

	public void processDate(String date, FieldCheckType fieldCheckType) { // effectiveDate="2014-07-07"
		String[] array = date.split("-");
		if (array.length == 3) {
			try {
				boolean blnYear = isValidYear(Long.parseLong(array[0]));
				boolean blnMonth = isValidMonth(Long.parseLong(array[1]));
				boolean blnDate = isValidDate(Long.parseLong(array[2]));
				if (blnYear && blnMonth && blnDate) {
					setPcorrect(fieldCheckType);
				}
			} catch (NumberFormatException e) {
				logger.info("Could not parse date: " + date);
			}
		}
	}

}
