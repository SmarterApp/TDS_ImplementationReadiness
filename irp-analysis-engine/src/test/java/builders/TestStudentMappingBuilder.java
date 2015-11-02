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
	
	public TestStudentMappingBuilder studentSSID(String studentSSID){
		testStudentMapping.setStudentSSID(studentSSID);
		return this;
	}
	
	public TestStudentMapping toTestStudentMapping(){
		return testStudentMapping;
	}
	
}
