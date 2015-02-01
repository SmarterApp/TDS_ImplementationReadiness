package org.cresst.sb.irp.service;

import java.util.Map;

import org.cresst.sb.irp.dao.StudentResponseDao;
import org.cresst.sb.irp.domain.studentresponse.StudentResponse;
import org.cresst.sb.irp.domain.studentresponse.TestItemResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentResponseServiceImpl implements StudentResponseService {
	private final static Logger logger = LoggerFactory.getLogger(StudentResponseServiceImpl.class);

	@Autowired
	private StudentResponseDao studentResponseDao;

	public StudentResponseServiceImpl(){
		logger.info("initializing");
	}
	
	@Override
	public Map<Integer, String> getHeaderMap() {
		return studentResponseDao.getHeaderMap();
	}

	@Override
	public Map<Long, TestItemResponse> getTestItemStudentResponseMap() {
		return studentResponseDao.getTestItemStudentResponseMap();
	}
	
	@Override
	public TestItemResponse getTestItemResponseByStudentID(Long studentID) {
		return studentResponseDao.getTestItemResponseByStudentID(studentID);
	}

	@Override
	public StudentResponse getStudentResponseByStudentIDandBankKeyID(Long studentID, String id) {
		return studentResponseDao.getStudentResponseByStudentIDandBankKeyID(studentID, id);
	}


	
}
