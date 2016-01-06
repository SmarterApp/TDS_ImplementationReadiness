package org.cresst.sb.irp.domain.teststudentmapping;

public class TestStudentMapping {

	private String test;
	private String testType;
	private String segment;
	private boolean isCAT;
	private String studentSSID;
	private String alternateSSID;
	private String studentName;

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public String getSegment() {
		return segment;
	}

	public void setSegment(String segment) {
		this.segment = segment;
	}

	public boolean isCAT() {
		return isCAT;
	}

	public void setCAT(boolean isCAT) {
		this.isCAT = isCAT;
	}

	public String getStudentSSID() {
		return studentSSID;
	}

	public void setStudentSSID(String studentSSID) {
		this.studentSSID = studentSSID;
	}

	public String getAlternateSSID() {
		return alternateSSID;
	}

	public void setAlternateSSID(String alternateSSID) {
		this.alternateSSID = alternateSSID;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	@Override
	public String toString() {
		return "TestStudentMapping [test=" + test + ", testType=" + testType + ", segment=" + segment + ", isCAT=" + isCAT
				+ ", studentSSID=" + studentSSID + ", alternateSSID=" + alternateSSID + ", studentName=" + studentName + "]";
	}



}
