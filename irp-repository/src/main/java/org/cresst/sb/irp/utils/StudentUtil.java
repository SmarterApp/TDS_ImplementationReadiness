package org.cresst.sb.irp.utils;

import java.util.Iterator;
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

	public StudentUtil(){
		logger.info("initializing");
	}
	
	public void getHeaderColumn(Map<Integer, String> headerMap, XSSFSheet sheet) {
		try {
			// get header
			Row headerRow = sheet.getRow(0);
			Iterator<Cell> cellHeaderIterator = headerRow.cellIterator();
			while (cellHeaderIterator.hasNext()) {
				// get cell object
				Cell cell = cellHeaderIterator.next();
				headerMap.put(Integer.valueOf(cell.getColumnIndex()), cell
						.getStringCellValue().trim());
			}
		} catch (Exception e) {
			logger.error("getHeaderColumn(); exception: ", e);
		}
	}

	public void processSheet(List<Student> listStudent,
			Map<Integer, String> headerMap, XSSFSheet sheet) {
		try {
			for (int rowCount = 1; rowCount <= sheet.getLastRowNum(); rowCount++) {
				Row detailRow = sheet.getRow(rowCount);
				if (detailRow != null && !isEmptyRow(detailRow)) {
					Student student = createStudentObject(detailRow, headerMap);
					if (student != null)
						listStudent.add(student);
				}
			}
		} catch (Exception e) {
			logger.error("processSheet(); exception: ", e);
		}

	}

	private boolean isEmptyRow(Row row) {
		boolean bln = true;
		try {
			for (int cellNum = row.getFirstCellNum(); cellNum < row
					.getLastCellNum(); cellNum++) {
				Cell cell = row.getCell(cellNum);
				if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK
						&& StringUtils.isNotBlank(cell.toString())) {
					bln = false;
				}
			}
		} catch (Exception e) {
			logger.error("isEmptyRow(); exception: ", e);
		}
		return bln;
	}

	private Student createStudentObject(Row row, Map<Integer, String> headerMap) {
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
					setStudentFieldData(student, columnName,
							cell.getStringCellValue());
				}
			}

		} catch (Exception e) {
			logger.error("createStudentObject(); exception: ", e);
		}

		return student;
	}

	private void setStudentFieldData(Student student, String columnName,
			String cellStringValue) {
		try {
			switch (columnName) {
			case "StateAbbreviation":
				student.setStateAbbreviation(cellStringValue);
				break;
			case "ResponsibleDistrictIdentifier":
				student.setResponsibleDistrictIdentifier(cellStringValue);
				break;
			case "ResponsibleInstitutionIdentifier": //"ResponsibleSchoolIdentifier":
				student.setResponsibleInstitutionIdentifier(cellStringValue);
				break;
			case "LastOrSurname":
				student.setLastOrSurname(cellStringValue);
				break;
			case "FirstName":
				student.setFirstName(cellStringValue);
				break;
			case "MiddleName":
				student.setMiddleName(cellStringValue);
				break;
			case "Birthdate":
				student.setBirthdate(cellStringValue);
				break;
			case "SSID":
				student.setSSID(Long.parseLong(cellStringValue));
				break;	
			case "AlternateSSID":
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
			case "GradeLevelWhenAssessed":
				student.setGradeLevelWhenAssessed(cellStringValue);
				break;
			case "Sex":
				student.setSex(cellStringValue);
				break;
			case "HispanicOrLatinoEthnicity":
				student.setHispanicOrLatinoEthnicity(cellStringValue);
				break;
			case "AmericanIndianOrAlaskaNative":
				student.setAmericanIndianOrAlaskaNative(cellStringValue);
				break;
			case "Asian":
				student.setAsian(cellStringValue);
				break;
			case "BlackOrAfricanAmerican":
				student.setBlackOrAfricanAmerican(cellStringValue);
				break;
			case "White":
				student.setWhite(cellStringValue);
				break;
			case "NativeHawaiianOrOtherPacificIslander":
				student.setNativeHawaiianOrOtherPacificIslander(cellStringValue);
				break;
			case "DemographicRaceTwoOrMoreRaces":
				student.setDemographicRaceTwoOrMoreRaces(cellStringValue);
				break;
			case "IDEAIndicator":
				student.setIDEAIndicator(cellStringValue);
				break;
			case "LEPStatus":
				student.setLEPStatus(cellStringValue);
				break;
			case "Section504Status":
				student.setSection504Status(cellStringValue);
				break;
			case "EconomicDisadvantageStatus":
				student.setEconomicDisadvantageStatus(cellStringValue);
				break;
			case "LanguageCode":
				student.setLanguageCode(cellStringValue);
				break;
			case "EnglishLanguageProficiencyLevel":
				student.setEnglishLanguageProficiencyLevel(cellStringValue);
				break;
			case "MigrantStatus":
				student.setMigrantStatus(cellStringValue);
				break;
			case "FirstEntryDateIntoUSSchool":
				student.setFirstEntryDateIntoUSSchool(cellStringValue);
				break;
			case "LimitedEnglishProficiencyEntryDate":
				student.setLimitedEnglishProficiencyEntryDate(cellStringValue);
				break;
			case "LEPExitDate":
				student.setLEPExitDate(cellStringValue);
				break;
			case "TitleIIILanguageInstructionProgramType":
				student.setTitleIIILanguageInstructionProgramType(cellStringValue);
				break;
			case "PrimaryDisabilityType":
				student.setPrimaryDisabilityType(cellStringValue);
				break;
			default:
				throw new IllegalArgumentException("Invalid column name: "
						+ columnName);
			}
		} catch (Exception e) {
			logger.error("setStudentFieldData(); exception: ", e);
		}
	}

	public Student getStudentBySSID(List<Student> listStudent, long studentSSID) {

		for (Student _student : listStudent){
			if(_student.getSSID() == studentSSID){
				return _student;
			}
		}

		return null;
	}
	
	/*
	public Student getStudentByStudentIdentifier(List<Student> listStudent,
			String studentIdentifier) {

		Student student = null;
		try {
			for(Student _student: listStudent){
				if(_student.getStudentIdentifier().trim().toLowerCase().equals(studentIdentifier.trim().toLowerCase())){
					return _student;
				}
			}
		} catch (Exception e) {
			logger.info("getStudentByStudentIdentifier exception: " + e);
			System.out.println("StudentUtil.getStudentByStudentIdentifier Exception thrown  :"+ e);
			e.printStackTrace();
		}
		return student;
	}*/

	
	
}


