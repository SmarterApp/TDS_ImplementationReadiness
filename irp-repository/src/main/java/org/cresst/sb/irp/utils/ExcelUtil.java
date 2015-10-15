package org.cresst.sb.irp.utils;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ExcelUtil {
	private final static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

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
	
	public boolean isEmptyRow(Row row) {
		try {
			for (int cellNum = row.getFirstCellNum(); cellNum < row
					.getLastCellNum(); cellNum++) {
				Cell cell = row.getCell(cellNum);
				if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK
						&& StringUtils.isNotBlank(cell.toString())) {
					return false;
				}
			}
		} catch (Exception e) {
			logger.error("isEmptyRow(); exception: ", e);
		}
		return true;
	}
	
}
