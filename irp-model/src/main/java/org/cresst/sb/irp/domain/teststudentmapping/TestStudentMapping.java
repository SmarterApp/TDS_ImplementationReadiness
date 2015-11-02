package org.cresst.sb.irp.domain.teststudentmapping;

public class TestStudentMapping {
	
	private String Test;
	private String StudentSSID;
	
	public String getTest() {
		return Test;
	}
	public void setTest(String test) {
		Test = test;
	}
	public String getStudentSSID() {
		return StudentSSID;
	}
	public void setStudentSSID(String studentSSID) {
		StudentSSID = studentSSID;
	}
	@Override
	public String toString() {
		return "TestStudentMapping [Test=" + Test + ", StudentSSID=" + StudentSSID + "]";
	} 
	

}
