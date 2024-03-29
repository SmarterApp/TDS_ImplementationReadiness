package org.cresst.sb.irp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Utilities for handling excel files from the irp-package
 */
@Service
public class ExcelUtil {
	private final static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * 
     * @param headerMap
     *            is populated with <column index, header name> values after
     *            method call
     * @param sheet
     *            the excel spreadsheet to extract headers from
     * @param headerrowIndex
     *            the index of the header row found in `sheet`.
     */
	public void getHeaderColumn(Map<Integer, String> headerMap, XSSFSheet sheet, int headerrowIndex) {
		try {
			// get header
			Row headerRow = sheet.getRow(headerrowIndex);
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
	
    /**
     * 
     * @param row
     *            to test
     * @return boolean representing whether `row` is empty or not
     */
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
	
	// dates are stored as numbers in Excel
	protected String dateString(String cellStringValue){
		Date javaDate = DateUtil.getJavaDate(Double.parseDouble(cellStringValue));
		SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(javaDate);
	}
	
}
