package org.cresst.sb.irp.dao;

import org.cresst.sb.irp.domain.student.Student;
import org.cresst.sb.irp.exceptions.NotFoundException;

import java.util.List;

/**
 * Student data repository access to student data in irp-package. Data is loaded
 * from the student data in the excel spreadsheet IRPStudents.xlsx in
 * irp-package. Students can also be created in this repository.
 */
public interface StudentDao {
	
	List<Student> getStudents();
	
	//Student getStudentByStudentIdentifier(String studentIdentifier);
	
	Student getStudentByStudentSSID(String studentSSID) throws NotFoundException;
	
	Student getStudentBySSID(List<Student> students, String studentSSID);
	
	void createStudent(Student student);

}
