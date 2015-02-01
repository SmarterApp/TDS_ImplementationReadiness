package org.cresst.sb.irp.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cresst.sb.irp.domain.studentresponse.StudentResponse;
import org.cresst.sb.irp.domain.studentresponse.TestItemResponse;
import org.cresst.sb.irp.utils.StudentResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class StudentResponseDaoImpl implements StudentResponseDao {
	private final static Logger logger = LoggerFactory.getLogger(StudentResponseDaoImpl.class);
	private Map<Integer, String> headerMap = new HashMap<Integer, String>();
	private Map<Long, TestItemResponse> testItemStudentResponseMap = new HashMap<Long, TestItemResponse>();
	
	@Value("classpath:irp-package/PT10thGradeMathItemsWithStudentResponses_1_13_15.xlsx")  //1/13/15 use 1 file only
	private Resource studentResponseResource;
	
	@Autowired
	private StudentResponseUtil studentResponseUtil;
	
	public StudentResponseDaoImpl(){
		logger.info("initializing");
	}
	
	@PostConstruct
	public void loadData() throws Exception {
		XSSFWorkbook workbook = new XSSFWorkbook(studentResponseResource.getInputStream());
		XSSFSheet sheet = workbook.getSheetAt(0);
		studentResponseUtil.getHeaderColumn(headerMap, sheet);
		studentResponseUtil.initialTestItemResponse(headerMap, testItemStudentResponseMap);
		if(testItemStudentResponseMap.size() > 0){
			studentResponseUtil.processSheet(testItemStudentResponseMap, headerMap, sheet);
		}
	}
	
	@Override
	public TestItemResponse getTestItemResponseByStudentID(Long studentID) {
		TestItemResponse testItemResponse = testItemStudentResponseMap.get(studentID);
		if (testItemResponse == null){
			return null;
		}
		return testItemResponse;
	}

	@Override
	public StudentResponse getStudentResponseByStudentIDandBankKeyID(Long studentID, String id) {
		TestItemResponse testItemResponse = testItemStudentResponseMap.get(studentID);
		if (testItemResponse == null){
			return null;
		}
		List<StudentResponse> studentResponses = testItemResponse.getStudentResponses();
		for(StudentResponse sr: studentResponses){
			if (sr.getId().equals(id)){
				return sr;
			}
		}
		return null;
	}

	@Override
	public Map<Integer, String> getHeaderMap() {
		return headerMap;
	}

	@Override
	public Map<Long, TestItemResponse> getTestItemStudentResponseMap() {
		return testItemStudentResponseMap;
	}

}
