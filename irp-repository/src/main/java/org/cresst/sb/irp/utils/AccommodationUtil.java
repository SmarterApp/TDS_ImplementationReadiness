package org.cresst.sb.irp.utils;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.cresst.sb.irp.domain.accommodation.Accommodation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AccommodationUtil {
	private final static Logger logger = LoggerFactory.getLogger(AccommodationUtil.class);

	public void processSheet(List<Accommodation> list,
			Map<Integer, String> headerMap, XSSFSheet sheet,
			ExcelUtil excelUtil,
			int startIndex) {
		try {
			for (int rowCount = startIndex; rowCount <= sheet.getLastRowNum(); rowCount++) {
				Row detailRow = sheet.getRow(rowCount);
				if (detailRow != null && !excelUtil.isEmptyRow(detailRow)) {
					Accommodation accommodation = createObject(detailRow, headerMap);
					if (accommodation != null)
						list.add(accommodation);
				}
			}
		} catch (Exception e) {
			logger.error("processSheet(); exception: ", e);
		}
	}
	
	private Accommodation createObject(Row row, Map<Integer, String> headerMap) {
		Accommodation accommodation = null;
		try {
			accommodation = new Accommodation();
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
					setFieldData(accommodation, columnName, cell.getStringCellValue());
				}
			}

		} catch (Exception e) {
			logger.error("createObject(); exception: ", e);
		}

		return accommodation;
	}
	
	private void setFieldData(Accommodation accommodation, String columnName,
			String cellStringValue) {
		try {
			columnName = StringUtils.deleteWhitespace(columnName);
			switch (columnName.toLowerCase()) {
			case "studentidentifier": 
				accommodation.setStudentIdentifier(Long.parseLong(cellStringValue));
				break;
			case "stateabbreviation":
				accommodation.setStateAbbreviation(cellStringValue);
				break;
			case "subject": 
				accommodation.setSubject(cellStringValue);
				break;
			case "americansignlanguage":
				accommodation.setAmericanSignLanguage(cellStringValue);
				break;
			case "colorcontrast": 
				accommodation.setColorContrast(cellStringValue);
				break;
			case "closedcaptioning":
				accommodation.setClosedCaptioning(cellStringValue);
				break;
			case "language": 
				accommodation.setLanguage(cellStringValue);
				break;
			case "masking":
				accommodation.setMasking(cellStringValue);
				break;
			case "permissivemode": 
				accommodation.setPermissiveMode(cellStringValue);
				break;
			case "printondemand":
				accommodation.setPrintOnDemand(cellStringValue);
				break;
			case "zoom": 
				accommodation.setZoom(cellStringValue);
				break;
			case "streamlinedinterface":
				accommodation.setStreamlinedInterface(cellStringValue);
				break;
			case "texttospeech": 
				accommodation.setTexttoSpeech(cellStringValue);
				break;
			case "translation":
				accommodation.setTranslation(cellStringValue);
				break;
			case "nonembeddeddesignatedsupports": 
				accommodation.setNonEmbeddedDesignatedSupports(cellStringValue);
				break;
			case "nonembeddedaccommodations":
				accommodation.setNonEmbeddedAccommodations(cellStringValue);
				break;
			case "other": 
				accommodation.setOther(cellStringValue);
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
