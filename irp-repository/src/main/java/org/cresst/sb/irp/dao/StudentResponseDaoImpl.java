package org.cresst.sb.irp.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cresst.sb.irp.domain.studentresponse.StudentResponse;
import org.cresst.sb.irp.domain.studentresponse.TestItemResponse;
import org.cresst.sb.irp.utils.StudentResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class StudentResponseDaoImpl implements StudentResponseDao {
	private static Logger logger = Logger.getLogger(StudentResponseDaoImpl.class);
	private Map<Integer, String> headerMap = new HashMap<Integer, String>();
	private Map<String, TestItemResponse> testItemStudentResponseMap = new HashMap<String, TestItemResponse>();
	
	@Value("classpath:irp-package/PT10thGradeMathItemsWithStudentResponses.xlsx")  //12/23/14
	private Resource studentResponseResource;
	
	@Autowired
	private StudentResponseUtil studentResponseUtil;
	
	public StudentResponseDaoImpl(){
		logger.info("initializing");
	}
	
	@PostConstruct
	public void loadData() throws Exception {
	
		System.out.println("insde of loadData....");
		XSSFWorkbook workbook = new XSSFWorkbook(studentResponseResource.getInputStream());
		XSSFSheet sheet = workbook.getSheetAt(0);
		studentResponseUtil.getHeaderColumn(headerMap, sheet);
		for(Map.Entry<Integer, String> entry: headerMap.entrySet()){
			Integer key = entry.getKey();
			String value = entry.getValue();
			System.out.println("key -->" + key + " value ->" + value);
		}
		studentResponseUtil.initialTestItemResponse(headerMap, testItemStudentResponseMap);
		if(testItemStudentResponseMap.size() > 0){
			System.out.println("1111111111111111111");
			studentResponseUtil.processSheet(testItemStudentResponseMap, headerMap, sheet);
		}else
			System.out.println("22222222222222222222");
	}
	
	@Override
	public TestItemResponse getTestItemResponseByStudentID(String studentID) {
		TestItemResponse testItemResponse = testItemStudentResponseMap.get(studentID);
		if (testItemResponse == null){
			return null;
		}
		return testItemResponse;
	}

	@Override
	public StudentResponse getStudentResponseByStudentIDandBankKeyID(String studentID, String id) {
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


	
	
}
