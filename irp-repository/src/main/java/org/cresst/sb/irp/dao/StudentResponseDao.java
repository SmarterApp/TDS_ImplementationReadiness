package org.cresst.sb.irp.dao;

import org.cresst.sb.irp.domain.studentresponse.StudentResponse;
import org.cresst.sb.irp.domain.studentresponse.TestItemResponse;

public interface StudentResponseDao {
	
	TestItemResponse getTestItemResponseByStudentID(String studentID);
	
	StudentResponse getStudentResponseByStudentIDandBankKeyID(String studentID, String id);

}
