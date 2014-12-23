package org.cresst.sb.irp.utils;

import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.stereotype.Service;

@Service
public class StudentResponseUtil {
	private static Logger logger = Logger.getLogger(StudentResponseUtil.class);

	public StudentResponseUtil(){
		logger.info("initializing");
	}
	
	public void getHeaderColumn(Map<Integer, String> headerMap, XSSFSheet sheet) {
		try {
			// get header
			Row headerRow = sheet.getRow(1); //0 Student Responses (Alternate ID)
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
	
}
