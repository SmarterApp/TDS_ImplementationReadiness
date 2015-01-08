package org.cresst.sb.irp.service;

import org.cresst.sb.irp.domain.studentresponse.StudentResponse;
import org.cresst.sb.irp.domain.studentresponse.TestItemResponse;

public interface StudentResponseService {
	
	TestItemResponse getTestItemResponseByStudentID(String studentID);
	
	StudentResponse getStudentResponseByStudentIDandBankKeyID(String studentID, String id);

}
