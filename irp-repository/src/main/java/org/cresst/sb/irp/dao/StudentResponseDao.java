package org.cresst.sb.irp.dao;

import java.util.Map;

import org.cresst.sb.irp.domain.studentresponse.StudentResponse;
import org.cresst.sb.irp.domain.studentresponse.TestItemResponse;

public interface StudentResponseDao {
	
	Map<Integer, String> getHeaderMap();
	
	Map<Long, TestItemResponse> getTestItemStudentResponseMap();
	
	TestItemResponse getTestItemResponseByStudentID(Long studentID);
	
	StudentResponse getStudentResponseByStudentIDandBankKeyID(Long studentID, String bankKey, String id);


	
}
