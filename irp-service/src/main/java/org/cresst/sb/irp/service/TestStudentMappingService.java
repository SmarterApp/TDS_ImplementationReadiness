package org.cresst.sb.irp.service;

import java.util.List;

import org.cresst.sb.irp.domain.teststudentmapping.TestStudentMapping;
import org.cresst.sb.irp.exceptions.NotFoundException;

public interface TestStudentMappingService {

	List<TestStudentMapping> getTestStudentMappings();
	
	TestStudentMapping getTestStudentMapping(String testName, long studentSSID) throws NotFoundException;
	
}
