package org.cresst.sb.irp.utils;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.cresst.sb.irp.domain.teststudentmapping.TestStudentMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Utilities for the processing of TestStudentMappings from excel files.
 */
@Service
public class TestStudentMappingUtil {
	private final static Logger logger = LoggerFactory.getLogger(TestStudentMappingUtil.class);

    /**
     * 
     * @param list
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
	@SuppressWarnings("unchecked")
	public <T> void processSheet(List<T> list, Map<Integer, String> headerMap, XSSFSheet sheet, ExcelUtil excelUtil, int startIndex) {
		try {
			for (int rowCount = startIndex; rowCount <= sheet.getLastRowNum(); rowCount++) {
				Row detailRow = sheet.getRow(rowCount);
				if (detailRow != null && !excelUtil.isEmptyRow(detailRow)) {
					T testStudentMapping = (T) createObject(detailRow, headerMap);
					if (testStudentMapping != null)
						list.add(testStudentMapping);
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
     * @return
     */
	private TestStudentMapping createObject(Row row, Map<Integer, String> headerMap) {
		TestStudentMapping testStudentMapping = null;
		try {
			testStudentMapping = new TestStudentMapping();
			int headerSize = headerMap.size();
			for (int cellCount = 0; cellCount < headerSize; cellCount++) {
				Cell cell = row.getCell(cellCount);
				String columnName = headerMap.get(cellCount);
				if (cell != null) {
					if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						cell.setCellType(Cell.CELL_TYPE_STRING);
					} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
					} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN)
						cell.setCellType(Cell.CELL_TYPE_STRING);
					setFieldData(testStudentMapping, columnName, cell.getStringCellValue());
				}
			}

		} catch (Exception e) {
			logger.error("createObject(); exception: ", e);
		}

		return testStudentMapping;
	}

    /**
     * 
     * @param accommodation
     *            existing TestStudentMapping that will have a new property set
     *            according to `columnName`, with the value of `cellStringValue`
     * @param columnName
     *            selects the method of setting the value in `cellStringValue`.
     *            The string will make `cellStringValue` to the corresponding
     *            property in `testStudentMapping`.
     * @param cellStringValue
     *            value to set in `testStudentMapping`
     */
	private void setFieldData(TestStudentMapping testStudentMapping, String columnName, String cellStringValue) {
		try {
			columnName = StringUtils.deleteWhitespace(columnName);
			switch (columnName.toLowerCase()) {
			case "test":
				testStudentMapping.setTest(cellStringValue);
				break;
			case "testtype":
				testStudentMapping.setTestType(cellStringValue);
				break;
			case "segmentname":
				testStudentMapping.setSegmentName(cellStringValue);
				break;	
			case "componenttestname":
				testStudentMapping.setComponentTestName(cellStringValue);
				break;		
			case "cat":
				testStudentMapping.setCAT(Boolean.parseBoolean(cellStringValue));
			case "studentssid":
				testStudentMapping.setStudentSSID(cellStringValue);
				break;
			case "alternatessid":
				testStudentMapping.setAlternateSSID(cellStringValue);
				break;
			case "studentname":
				testStudentMapping.setStudentName(cellStringValue);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("setFieldData(); exception: ", e);
		}
	}
	
}
