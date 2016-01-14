package org.cresst.sb.irp.service;

import java.util.List;

import org.cresst.sb.irp.dao.TestStudentMappingDao;
import org.cresst.sb.irp.domain.teststudentmapping.TestStudentMapping;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestStudentMappingServiceImpl implements TestStudentMappingService {
	private final static Logger logger = LoggerFactory.getLogger(TestStudentMappingServiceImpl.class);

	@Autowired
	private TestStudentMappingDao testStudentMappingDao;
	
	@Override
	public List<TestStudentMapping> getTestStudentMappings() {
		return testStudentMappingDao.getTestStudentMappings();
	}

	@Override
	public TestStudentMapping getTestStudentMapping(String testName, String studentSSID) {
		return testStudentMappingDao.getTestStudentMapping(testName, studentSSID);
	}

	@Override
	public List<TestStudentMapping> getTestStudentMappingsByTestName(String testName) {
		return testStudentMappingDao.getTestStudentMappingsByTestName(testName);
	}


}
