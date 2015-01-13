package org.cresst.sb.irp.service;

import java.util.Map;

import org.cresst.sb.irp.domain.studentresponse.StudentResponse;
import org.cresst.sb.irp.domain.studentresponse.TestItemResponse;

public interface StudentResponseService {
	
	Map<Integer, String> getHeaderMap();
	
	Map<String, TestItemResponse> getTestItemStudentResponseMap();
	
	TestItemResponse getTestItemResponseByStudentID(String studentID);
	
	StudentResponse getStudentResponseByStudentIDandBankKeyID(String studentID, String id);
	

}
