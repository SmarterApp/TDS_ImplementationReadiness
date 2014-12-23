package org.cresst.sb.irp.service;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.dao.StudentResponseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentResponseServiceImpl implements StudentResponseService {
	private static Logger logger = Logger.getLogger(StudentResponseServiceImpl.class);

	@Autowired
	private StudentResponseDao studentResponseDao;
	
}
