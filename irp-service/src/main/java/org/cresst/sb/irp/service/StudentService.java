package org.cresst.sb.irp.service;

import org.cresst.sb.irp.domain.student.Student;

import java.util.List;


public interface StudentService {
	
	List<Student> getStudents();
	
	//Student getStudentByStudentIdentifier(String studentIdentifier); 
	
	Student getStudentByStudentSSID(String studentSSID);
	
	void createStudent(Student student);

}
