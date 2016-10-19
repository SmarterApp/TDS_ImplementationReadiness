package builders;

import org.cresst.sb.irp.domain.teststudentmapping.TestStudentMapping;

/**
 * Builder to create IRP TestStudentMapping objects to support unit tests
 */
public class TestStudentMappingBuilder {
	
	private TestStudentMapping testStudentMapping = new TestStudentMapping();
	
	public TestStudentMappingBuilder test(String test){
		testStudentMapping.setTest(test);
		return this;
	}
	
	public TestStudentMappingBuilder testType(String testType){
		testStudentMapping.setTestType(testType);
		return this;
	}
	
	public TestStudentMappingBuilder segmentName(String segmentName){
		testStudentMapping.setSegmentName(segmentName);
		return this;
	}
	
	public TestStudentMappingBuilder componentTestName(String componentTestName){
		testStudentMapping.setComponentTestName(componentTestName);
		return this;
	}
	
	public TestStudentMappingBuilder isCAT(boolean isCAT){
		testStudentMapping.setCAT(isCAT);
		return this;
	}
	
	public TestStudentMappingBuilder studentSSID(String studentSSID){
		testStudentMapping.setStudentSSID(studentSSID);
		return this;
	}
	
	public TestStudentMappingBuilder alternateSSID(String alternateSSID){
		testStudentMapping.setAlternateSSID(alternateSSID);
		return this;
	}
	
	public TestStudentMappingBuilder studentName(String studentName){
		testStudentMapping.setStudentName(studentName);
		return this;
	}
	
	public TestStudentMapping toTestStudentMapping(){
		return testStudentMapping;
	}
	
}
