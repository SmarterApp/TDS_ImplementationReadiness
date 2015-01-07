package org.cresst.sb.irp.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.cresst.sb.irp.domain.student.Student;
import org.cresst.sb.irp.domain.studentresponse.StudentResponse;
import org.cresst.sb.irp.domain.studentresponse.TestItemResponse;
import org.springframework.stereotype.Service;

@Service
public class StudentResponseUtil {
	private static Logger logger = Logger.getLogger(StudentResponseUtil.class);

	public StudentResponseUtil() {
		logger.info("initializing");
	}

	public void getHeaderColumn(Map<Integer, String> headerMap, XSSFSheet sheet) {
		try {
			// get header
			Row headerRow = sheet.getRow(1); // 0 Test Packages , Student Responses (Alternate ID)
			Iterator<Cell> cellHeaderIterator = headerRow.cellIterator();
			while (cellHeaderIterator.hasNext()) {
				// get cell object
				Cell cell = cellHeaderIterator.next();
				headerMap.put(Integer.valueOf(cell.getColumnIndex()), cell.getStringCellValue().trim());
			}
		} catch (Exception e) {
			logger.error("getHeaderColumn(); exception: ", e);
		}
	}

	public void processSheet(Map<String, TestItemResponse> testItemStudentResponseMap, Map<Integer, String> headerMap,
			XSSFSheet sheet) {
		try {
			for (int rowCount = 2; rowCount <= sheet.getLastRowNum(); rowCount++) {
				Row detailRow = sheet.getRow(rowCount);
				if (detailRow != null && !isEmptyRow(detailRow)) {
					List<StudentResponse> currStudentResponses = createStudentResponses(testItemStudentResponseMap);
					createStudentResponseObject(detailRow, headerMap, currStudentResponses);
				}
			}
		} catch (Exception e) {
			logger.error("processSheet(); exception: ", e);
		}
	}

	private List<StudentResponse> createStudentResponses(Map<String, TestItemResponse> testItemStudentResponseMap) {
		List<StudentResponse> listStudentResponse = null;
		try {
			listStudentResponse = new ArrayList<StudentResponse>();
			for (Map.Entry<String, TestItemResponse> entry : testItemStudentResponseMap.entrySet()) {
				String key = entry.getKey();
				TestItemResponse testItemResponse = entry.getValue();
				StudentResponse studentResponse = new StudentResponse();
				studentResponse.setStudentID(key);
				listStudentResponse.add(studentResponse);
				List<StudentResponse> tmpStudentResponse = testItemResponse.getStudentResponses();
				tmpStudentResponse.add(studentResponse);
			}
		} catch (Exception e) {
			logger.error("createStudentResponses(); exception: ", e);
		}
		return listStudentResponse;
	}

	private void createStudentResponseObject(Row row, Map<Integer, String> headerMap, List<StudentResponse> studentResponses) {
		try {
			boolean blnStart = false;
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
					for (StudentResponse sr : studentResponses) {
						if (!blnStart) {
							setStudentResponseFieldData(sr, columnName, cell.getStringCellValue());
						}
						else {
							if (sr.getStudentID().equals(columnName))
								sr.setResponseContent(cell.getStringCellValue());
						}
					}
				}
				if (columnName.trim().equals("Training Test Item"))
					blnStart = true;
			}
		} catch (Exception e) {
			logger.error("createStudentResponseObject(); exception: ", e);
		}
	}

	private boolean isEmptyRow(Row row) {
		boolean bln = true;
		try {
			for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
				Cell cell = row.getCell(cellNum);
				if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && StringUtils.isNotBlank(cell.toString())) {
					bln = false;
				}
			}
		} catch (Exception e) {
			logger.error("isEmptyRow(); exception: ", e);
		}
		return bln;
	}

	public void initialTestItemResponse(Map<Integer, String> map, Map<String, TestItemResponse> testItemStudentResponseMap) {
		try {
			boolean blnStart = false;
			for (Map.Entry<Integer, String> entry : map.entrySet()) {
				int key = entry.getKey();
				String columnName = entry.getValue();
				if (blnStart) {
					TestItemResponse testItemResponse = new TestItemResponse();
					testItemResponse.setStudentID(columnName);
					testItemStudentResponseMap.put(columnName, testItemResponse);
				}
				if (columnName.trim().equals("Training Test Item"))
					blnStart = true;
			}
		} catch (Exception e) {
			logger.error("initialTestItemResponse(); exception: ", e);
		}
	}

	private void setStudentResponseFieldData(StudentResponse studentResponse, String columnName, String cellStringValue) {
		try {
			switch (columnName) {
			case "Test":
				studentResponse.setTestFileName(cellStringValue);
				break;
			case "Type":
				studentResponse.setType(cellStringValue);
				break;
			case "Filename":
				studentResponse.setItemName(cellStringValue);
				break;
			case "Bank Key":
				studentResponse.setBankKey(cellStringValue);
				break;
			case "ID":
				studentResponse.setId(cellStringValue);
				break;
			case "Source":
				studentResponse.setSource(cellStringValue);
				break;
			case "Subject":
				studentResponse.setSubject(cellStringValue);
				break;
			case "Item Type":
				studentResponse.setItemType(cellStringValue);
				break;
			case "Lookup":
				studentResponse.setLookup(cellStringValue);
				break;
			case "Training Test Item":
				studentResponse.setTraningTestItem(cellStringValue);
				break;
			default:
				throw new IllegalArgumentException("Invalid column name: " + columnName);
			}
		} catch (Exception e) {
			logger.error("setStudentResponseFieldData(); exception: ", e);
		}
	}

}
