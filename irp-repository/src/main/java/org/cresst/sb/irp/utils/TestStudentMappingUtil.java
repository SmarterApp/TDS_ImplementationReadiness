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

@Service
public class TestStudentMappingUtil {
	private final static Logger logger = LoggerFactory.getLogger(TestStudentMappingUtil.class);
	
	@SuppressWarnings("unchecked")
	public <T> void processSheet(List<T> list,
			Map<Integer, String> headerMap, XSSFSheet sheet,
			ExcelUtil excelUtil,
			int startIndex) {
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
	
	private void setFieldData(TestStudentMapping testStudentMapping, String columnName,
			String cellStringValue) {
		try {
			columnName = StringUtils.deleteWhitespace(columnName);
			switch (columnName.toLowerCase()) {
				case "test":
					testStudentMapping.setTest(cellStringValue);
					break;
				case "studentssid":
					testStudentMapping.setStudentSSID(cellStringValue);
					break;
				case "cat":
					testStudentMapping.setCAT(Boolean.parseBoolean(cellStringValue));
				default:
					break;
			}
		} catch (Exception e) {
			logger.error("setFieldData(); exception: ", e);
		}
	}	

}
