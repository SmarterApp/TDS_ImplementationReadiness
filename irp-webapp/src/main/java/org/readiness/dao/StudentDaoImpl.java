package org.readiness.dao;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.readiness.exceptions.NotFoundException;
import org.readiness.student.domain.Student;
import org.readiness.utils.StudentUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class StudentDaoImpl implements StudentDao, InitializingBean {
	private static Logger logger = Logger.getLogger(StudentDaoImpl.class);
	private String rootResourceFolderName = "SampleAssessmentItemPackage";
	private String studentUploadFileName = "AK Students.xlsx";
	private Map<Integer, String> headerMap = new HashMap<Integer, String>();
	private List<Student> listStudent = new ArrayList<Student>();
	
	@Autowired
	private StudentUtil studentUtil;
	
	public StudentDaoImpl() {
		logger.info("StudentDaoImpl()");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("afterPropertiesSet()");
		try {
			FileInputStream file = new FileInputStream(new File(
					rootResourceFolderName + "/" + studentUploadFileName));
			try {
				XSSFWorkbook workbook = new XSSFWorkbook(file);
				XSSFSheet sheet = workbook.getSheetAt(0);
				studentUtil.getHeaderColumn(headerMap, sheet);
				studentUtil.processSheet(listStudent, headerMap, sheet);
			} finally {
				file.close();
			}
		} catch (Exception e) {
			logger.info("afterPropertiesSet exception: " + e);
			System.out.println("afterPropertiesSet() Exception thrown  :" + e);
			e.printStackTrace();
		}
	}

	public List<Student> getListStudent() {
		return listStudent;
	}

	@Override
	public List<Student> getStudents() {
		logger.info("getStudents()");
		return listStudent;
	}

	@Override
	public Student getStudentByStudentIdentifier(String studentIdentifier) {
		logger.info("getStudentByStudentIdentifier(int)");
		Student student = studentUtil.getStudentByStudentIdentifier(listStudent, studentIdentifier);
		if (student == null){
			throw new NotFoundException("Could not find student " + studentIdentifier);
		}
		return student;
	}

	@Override
	public void createStudent(Student student) {
		listStudent.add(student);
	}

}
