package org.cresst.sb.irp.dao;

import org.cresst.sb.irp.domain.student.Student;

import java.util.List;


public interface StudentDao {
	
	List<Student> getStudents();
	
	Student getStudentByStudentIdentifier(String studentIdentifier);
	
	void createStudent(Student student);

}
