package org.cresst.sb.irp.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.dao.StudentDao;
import org.cresst.sb.irp.domain.student.Student;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {
	private static Logger logger = Logger.getLogger(StudentServiceImpl.class);

	@Autowired
	private StudentDao studentDao;

	public StudentServiceImpl(){

	}
	
	@Override
	public List<Student> getStudents() {
		return studentDao.getStudents();
	}

	/*
	@Override
	public Student getStudentByStudentIdentifier(String studentIdentifier) {
		return studentDao.getStudentByStudentIdentifier(studentIdentifier);
	}*/
	
	@Override
	public Student getStudentByStudentSSID(long studentSSID) throws NotFoundException {
		return studentDao.getStudentByStudentSSID(studentSSID);
	}

	@Override
	public void createStudent(Student student) {
		studentDao.createStudent(student);
	}

	
	
}
