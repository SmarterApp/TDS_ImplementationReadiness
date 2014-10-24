package org.cresst.sb.irp.dao;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cresst.sb.irp.domain.student.Student;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.cresst.sb.irp.utils.StudentUtil;
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

	}

	@Override
	public void afterPropertiesSet() throws Exception {
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
			logger.error("afterPropertiesSet exception: ", e);
		}
	}

	public List<Student> getListStudent() {
		return listStudent;
	}

	@Override
	public List<Student> getStudents() {
		return listStudent;
	}

	@Override
	public Student getStudentByStudentIdentifier(String studentIdentifier) {
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
