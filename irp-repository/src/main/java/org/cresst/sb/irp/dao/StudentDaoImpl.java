package org.cresst.sb.irp.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cresst.sb.irp.domain.student.Student;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.cresst.sb.irp.utils.StudentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@Repository
public class StudentDaoImpl implements StudentDao {
	private final static Logger logger = LoggerFactory.getLogger(StudentDaoImpl.class);
	private Map<Integer, String> headerMap = new HashMap<Integer, String>();
	private List<Student> listStudent = new ArrayList<Student>();

	@Value("classpath:irp-package/HI Students1_3.xlsx")  //AK Students.xlsx")
	private Resource studentResource;

	@Autowired
	private StudentUtil studentUtil;
	
	public StudentDaoImpl() {

	}

	@PostConstruct
	public void loadData() throws Exception {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(studentResource.getInputStream());
			XSSFSheet sheet = workbook.getSheetAt(0);
			studentUtil.getHeaderColumn(headerMap, sheet);
			studentUtil.processSheet(listStudent, headerMap, sheet);
		} catch (Exception e) {
			logger.error("afterPropertiesSet exception: ", e);
		}
	}

	@Override
	public List<Student> getStudents() {
		return listStudent;
	}

	/*@Override
	public Student getStudentByStudentIdentifier(String studentIdentifier) {
		Student student = studentUtil.getStudentByStudentIdentifier(listStudent, studentIdentifier);
		if (student == null){
			throw new NotFoundException("Could not find student " + studentIdentifier);
		}
		return student;
	}*/
	
	@Override
	public Student getStudentByStudentSSID(long studentSSID) throws NotFoundException {
		Student student = studentUtil.getStudentBySSID(listStudent, studentSSID);
		if (student == null){
			throw new NotFoundException("Could not find student " + studentSSID);
		}
		return student;
	}

	@Override
	public void createStudent(Student student) {
		listStudent.add(student);
	}



}
