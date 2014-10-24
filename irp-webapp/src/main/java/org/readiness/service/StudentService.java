package org.readiness.service;

import java.util.List;

import org.readiness.student.domain.Student;

public interface StudentService {
	
	List<Student> getStudents();
	
	Student getStudentByStudentIdentifier(String studentIdentifier); 
	
	void createStudent(Student student);

}
