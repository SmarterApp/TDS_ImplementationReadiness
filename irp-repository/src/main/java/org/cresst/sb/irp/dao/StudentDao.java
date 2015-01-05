package org.cresst.sb.irp.dao;

import org.cresst.sb.irp.domain.student.Student;
import org.cresst.sb.irp.exceptions.NotFoundException;

import java.util.List;


public interface StudentDao {
	
	List<Student> getStudents();
	
	//Student getStudentByStudentIdentifier(String studentIdentifier);
	
	Student getStudentByStudentSSID(long studentSSID) throws NotFoundException;
	
	void createStudent(Student student);

}
