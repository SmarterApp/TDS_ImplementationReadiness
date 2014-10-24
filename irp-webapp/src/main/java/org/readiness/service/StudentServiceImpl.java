package org.readiness.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.readiness.dao.StudentDao;
import org.readiness.student.domain.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {
	private static Logger logger = Logger.getLogger(StudentServiceImpl.class);

	@Autowired
	private StudentDao studentDao;

	public StudentServiceImpl(){
		logger.info("initializing");
	}
	
	@Override
	public List<Student> getStudents() {
		logger.info("getStudents");
		return studentDao.getStudents();
	}

	@Override
	public Student getStudentByStudentIdentifier(String studentIdentifier) {
		logger.info("getStudentByStudentIdentifier");
		return studentDao.getStudentByStudentIdentifier(studentIdentifier);
	}

	@Override
	public void createStudent(Student student) {
		studentDao.createStudent(student);
	}
	
}
