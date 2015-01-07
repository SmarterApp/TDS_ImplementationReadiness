package org.cresst.sb.irp.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cresst.sb.irp.domain.student.Student;
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
	//private List<StudentResponse> listStudentResponse = new ArrayList<StudentResponse>();

	
	@Value("classpath:irp-package/PT10thGradeMathItemsWithStudentResponses.xlsx")  //12/23/14
	private Resource studentResponseResource;
	
	@Autowired
	private StudentResponseUtil studentResponseUtil;
	
	public StudentResponseDaoImpl(){
		logger.info("initializing");
	}
	
	@PostConstruct
	public void loadData() throws Exception {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(studentResponseResource.getInputStream());
			XSSFSheet sheet = workbook.getSheetAt(0);
			studentResponseUtil.getHeaderColumn(headerMap, sheet);
			studentResponseUtil.initialTestItemResponse(headerMap, testItemStudentResponseMap);
			if(testItemStudentResponseMap.size() > 0){
				studentResponseUtil.processSheet(testItemStudentResponseMap, headerMap, sheet);
			}
		} catch (Exception e) {
			logger.error("afterPropertiesSet exception: ", e);
		}
	}
	
	
}
