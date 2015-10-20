package org.cresst.sb.irp.utils;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.cresst.sb.irp.domain.student.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class StudentUtil {
	private final static Logger logger = LoggerFactory.getLogger(StudentUtil.class);

	public StudentUtil() {
	}
	
	public void processSheet(List<Student> listStudent,
			Map<Integer, String> headerMap, XSSFSheet sheet,
			ExcelUtil excelUtil, 
			int startIndex) {
		try {
			for (int rowCount = startIndex; rowCount <= sheet.getLastRowNum(); rowCount++) {
				Row detailRow = sheet.getRow(rowCount);
				if (detailRow != null && !excelUtil.isEmptyRow(detailRow)) {
					Student student = createObject(detailRow, headerMap);
					if (student != null)
						listStudent.add(student);
				}
			}
		} catch (Exception e) {
			logger.error("processSheet(); exception: ", e);
		}

	}

	private Student createObject(Row row, Map<Integer, String> headerMap) {
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
					setFieldData(student, columnName,
							cell.getStringCellValue());
				}
			}

		} catch (Exception e) {
			logger.error("createObject(); exception: ", e);
		}

		return student;
	}

	private void setFieldData(Student student, String columnName,
			String cellStringValue) {
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
				student.setBirthdate(cellStringValue);
				break;
			case "ssid":
				student.setSSID(Long.parseLong(cellStringValue));
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
				student.setGradeLevelWhenAssessed(cellStringValue);
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
				student.setFirstEntryDateIntoUSSchool(cellStringValue);
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


