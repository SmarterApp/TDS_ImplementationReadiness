package org.readiness.dao;

import java.util.List;

import org.readiness.student.domain.Student;

public interface StudentDao {
	
	List<Student> getStudents();
	
	Student getStudentByStudentIdentifier(String studentIdentifier);
	
	void createStudent(Student student);

}
