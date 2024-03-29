package org.cresst.sb.irp.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cresst.sb.irp.domain.teststudentmapping.TestStudentMapping;
import org.cresst.sb.irp.utils.ExcelUtil;
import org.cresst.sb.irp.utils.TestStudentMappingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class TestStudentMappingDaoImpl implements TestStudentMappingDao {
	private final static Logger logger = LoggerFactory.getLogger(TestStudentMappingDaoImpl.class);

	private Map<Integer, String> headerMap = new HashMap<Integer, String>();
	private List<TestStudentMapping> testStudentMappings = new ArrayList<TestStudentMapping>();
	
    @Value("${irp.package.location}/IRPTestStudentMapping.xlsx")
	private Resource testStudentMappingResource;
	
	@Autowired
	private ExcelUtil excelUtil;
	
	@Autowired
	private TestStudentMappingUtil testStudentMappingUtil;
	
	@PostConstruct
	public void loadData() throws Exception {
		XSSFWorkbook workbook = new XSSFWorkbook(testStudentMappingResource.getInputStream());
		XSSFSheet sheet = workbook.getSheetAt(0);
		excelUtil.getHeaderColumn(headerMap, sheet, 1);
		testStudentMappingUtil.processSheet(testStudentMappings, headerMap, sheet, excelUtil, 2);
	}

	@Override
	public List<TestStudentMapping> getTestStudentMappings() {
		return testStudentMappings;
	}

	@Override
	public TestStudentMapping getTestStudentMapping(String testName, String studentSSID) {
		for(TestStudentMapping tsm : testStudentMappings){
			if (StringUtils.equalsIgnoreCase(tsm.getTest(), testName) && studentSSID != null && studentSSID.equalsIgnoreCase(tsm.getStudentSSID()))
				return tsm;
		}
		
		return null;
	}

	@Override
	public List<TestStudentMapping> getTestStudentMappingsByTestName(String testName) {
		List<TestStudentMapping> tStudentMappings = new ArrayList<TestStudentMapping>();
		for(TestStudentMapping tsm : testStudentMappings){
			if (StringUtils.equalsIgnoreCase(tsm.getTest(), testName))
				tStudentMappings.add(tsm);
		}
		if(tStudentMappings.size() > 0)
			return tStudentMappings;
		return null;
	}
	
	
}
