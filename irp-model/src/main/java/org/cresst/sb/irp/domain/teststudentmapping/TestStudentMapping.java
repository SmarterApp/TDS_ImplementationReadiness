package org.cresst.sb.irp.domain.teststudentmapping;

public class TestStudentMapping {

	private String test;
	private String testType;
	private String segmentName;
	private String componentTestName;
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

	public String getSegmentName() {
		return segmentName;
	}

	public void setSegmentName(String segmentName) {
		this.segmentName = segmentName;
	}

	public String getComponentTestName() {
		return componentTestName;
	}

	public void setComponentTestName(String componentTestName) {
		this.componentTestName = componentTestName;
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
		return "TestStudentMapping [test=" + test + ", testType=" + testType + ", segmentName=" + segmentName + ", componentTestName="
				+ componentTestName + ", isCAT=" + isCAT + ", studentSSID=" + studentSSID + ", alternateSSID=" + alternateSSID
				+ ", studentName=" + studentName + "]";
	}

}
