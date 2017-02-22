package org.cresst.sb.irp.dao;

import java.util.List;

import org.cresst.sb.irp.domain.teststudentmapping.TestStudentMapping;

/**
 * Data repository for the Test Student Mapping data from
 * IRPTestStudentMapping.xlsx
 */
public interface TestStudentMappingDao {
	
	List<TestStudentMapping> getTestStudentMappings();
	
	TestStudentMapping getTestStudentMapping(String testName, String studentSSID);
	
	List<TestStudentMapping> getTestStudentMappingsByTestName(String testName);

}
