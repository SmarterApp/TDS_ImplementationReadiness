package org.cresst.sb.irp.domain.studentresponse;

import java.util.ArrayList;
import java.util.List;

public class TestItemResponse {
	
	private String studentID; //11190
	private List<StudentResponse> studentResponses;
	
	public TestItemResponse(){
		studentResponses =  new ArrayList<StudentResponse>();
	}
	
	public String getStudentID() {
		return studentID;
	}
	
	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}
	
	public List<StudentResponse> getStudentResponses() {
		return studentResponses;
	}
	
	public void setStudentResponses(List<StudentResponse> studentResponses) {
		this.studentResponses = studentResponses;
	}

	@Override
	public String toString() {
		return "TestItemResponse [studentID=" + studentID + ", studentResponses=" + studentResponses + "]";
	}

}
