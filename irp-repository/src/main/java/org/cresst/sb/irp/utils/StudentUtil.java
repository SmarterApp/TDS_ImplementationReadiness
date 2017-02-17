package org.cresst.sb.irp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.cresst.sb.irp.domain.student.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * Student utilities
 */
@Service
public class StudentUtil {
	private final static Logger logger = LoggerFactory.getLogger(StudentUtil.class);
	
	public StudentUtil() {
	}
	
    /**
     * 
     * @param listStudent
     *            results from processing excel sheet
     * @param headerMap
     *            map that contains column index keys to header name values
     * @param sheet
     *            spreadsheet to process
     * @param excelUtil
     *            excel utility service
     * @param startIndex
     *            index to start processing from. Iterates until last row.
     */
	public void processSheet(List<Student> listStudent,
			Map<Integer, String> headerMap, XSSFSheet sheet,
			ExcelUtil excelUtil, 
			int startIndex) {
		try {
			for (int rowCount = startIndex; rowCount <= sheet.getLastRowNum(); rowCount++) {
				Row detailRow = sheet.getRow(rowCount);
				if (detailRow != null && !excelUtil.isEmptyRow(detailRow)) {
					Student student = createObject(detailRow, headerMap, excelUtil);
					if (student != null)
						listStudent.add(student);
				}
			}
		} catch (Exception e) {
			logger.error("processSheet(); exception: ", e);
		}

	}

    /**
     * 
     * @param row
     *            excel row to create accommodation from
     * @param headerMap
     *            <column index, column name> map to process row
     * @param excelUtil
     *            excel utility service
     * @return student object represented by `row`
     */
	private Student createObject(Row row, Map<Integer, String> headerMap, ExcelUtil excelUtil) {
		Student student = null;
		try {
			student = new Student();
			int headerSize = headerMap.size();
			for (int cellCount = 0; cellCount < headerSize; cellCount++) {
				Cell cell = row.getCell(cellCount);
				String columnName = headerMap.get(cellCount);
				if (cell != null) {
					if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						cell.setCellType(Cell.CELL_TYPE_STRING);
					} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
					} 
					
					setFieldData(student, columnName, cell.getStringCellValue(), excelUtil);
				}
			}

		} catch (Exception e) {
			logger.error("createObject(); exception: ", e);
		}

		return student;
	}

    /**
     * 
     * @param student
     *            existing Student that will have a new property set according
     *            to `columnName`, with the value of `cellStringValue`
     * @param columnName
     *            selects the method of setting the value in `cellStringValue`.
     *            The string will make `cellStringValue` to the corresponding
     *            property in `student`.
     * @param cellStringValue
     *            value to set in `student`
     */
	private void setFieldData(Student student, String columnName, String cellStringValue, ExcelUtil excelUtil) {
		try {
			columnName = StringUtils.deleteWhitespace(columnName);
			switch (columnName.toLowerCase()) {
			case "stateabbreviation": 
				student.setStateAbbreviation(cellStringValue);
				break;
			case "responsibledistrictidentifier":
				student.setResponsibleDistrictIdentifier(cellStringValue);
				break;
			case "responsibleinstitutionidentifier": //"ResponsibleSchoolIdentifier":
				student.setResponsibleInstitutionIdentifier(cellStringValue);
				break;
			case "lastorsurname":
				student.setLastOrSurname(cellStringValue);
				break;
			case "firstname":
				student.setFirstName(cellStringValue);
				break;
			case "middlename":
				student.setMiddleName(cellStringValue);
				break;
			case "birthdate":
				student.setBirthdate(excelUtil.dateString(cellStringValue));
				break;
			case "ssid":
				student.setSSID(cellStringValue);
				break;	
			case "alternatessid":
				student.setAlternateSSID(cellStringValue);
				break;		
			/*	
			case "StudentIdentifier":
				student.setStudentIdentifier(cellStringValue);
				break;
			case "ExternalSSID":
				student.setExternalSSID(cellStringValue);
				break;
			case "ConfirmationCode":
				student.setConfirmationCode(cellStringValue);
				break;
			*/
			case "gradelevelwhenassessed":
				student.setGradeLevelWhenAssessed(StringUtils.leftPad(cellStringValue, 2, '0'));
				break;
			case "sex":
				student.setSex(cellStringValue);
				break;
			case "hispanicorlatinoethnicity":
				student.setHispanicOrLatinoEthnicity(cellStringValue);
				break;
			case "americanindianoralaskanative":
				student.setAmericanIndianOrAlaskaNative(cellStringValue);
				break;
			case "asian":
				student.setAsian(cellStringValue);
				break;
			case "blackorafricanamerican":
				student.setBlackOrAfricanAmerican(cellStringValue);
				break;
			case "white":
				student.setWhite(cellStringValue);
				break;
			case "nativehawaiianorotherpacificislander":
				student.setNativeHawaiianOrOtherPacificIslander(cellStringValue);
				break;
			case "demographicracetwoormoreraces":
				student.setDemographicRaceTwoOrMoreRaces(cellStringValue);
				break;
			case "ideaindicator":
				student.setIDEAIndicator(cellStringValue);
				break;
			case "lepstatus":
				student.setLEPStatus(cellStringValue);
				break;
			case "section504status":
				student.setSection504Status(cellStringValue);
				break;
			case "economicdisadvantagestatus":
				student.setEconomicDisadvantageStatus(cellStringValue);
				break;
			case "languagecode":
				student.setLanguageCode(cellStringValue);
				break;
			case "englishlanguageproficiencylevel":
				student.setEnglishLanguageProficiencyLevel(cellStringValue);
				break;
			case "migrantstatus":
				student.setMigrantStatus(cellStringValue);
				break;
			case "firstentrydateintousschool":
				student.setFirstEntryDateIntoUSSchool(excelUtil.dateString(cellStringValue));
				break;
			case "limitedenglishproficiencyentrydate":
				student.setLimitedEnglishProficiencyEntryDate(cellStringValue);
				break;
			case "lepexitdate":
				student.setLEPExitDate(cellStringValue);
				break;
			case "titleiiilanguageinstructionprogramtype":
				student.setTitleIIILanguageInstructionProgramType(cellStringValue);
				break;
			case "primarydisabilitytype":
				student.setPrimaryDisabilityType(cellStringValue);
				break;
			case "delete":
				student.setDelete(cellStringValue);
				break;
			default:
				throw new IllegalArgumentException("Invalid column name: "
						+ columnName);
			}
		} catch (Exception e) {
			logger.error("setFieldData(); exception: ", e);
		}
	}

}


