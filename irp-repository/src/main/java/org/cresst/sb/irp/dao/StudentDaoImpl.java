package org.cresst.sb.irp.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cresst.sb.irp.domain.student.Student;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.cresst.sb.irp.utils.ExcelUtil;
import org.cresst.sb.irp.utils.StudentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class StudentDaoImpl implements StudentDao {
	private final static Logger logger = LoggerFactory.getLogger(StudentDaoImpl.class);
	private Map<Integer, String> headerMap = new HashMap<Integer, String>();
	private List<Student> students = new ArrayList<Student>();

    @Value("${irp.package.location}/IRPStudents.xlsx")
	private Resource studentResource;

	@Autowired
	private ExcelUtil excelUtil;
	
	@Autowired
	private StudentUtil studentUtil;
	
	public StudentDaoImpl() {

	}

	@PostConstruct
	public void loadData() throws Exception {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(studentResource.getInputStream());
			XSSFSheet sheet = workbook.getSheetAt(0);
			excelUtil.getHeaderColumn(headerMap, sheet, 0); 
			studentUtil.processSheet(students, headerMap, sheet, excelUtil, 1);
		} catch (Exception e) {
			logger.error("afterPropertiesSet exception: ", e);
		}
	}

	@Override
	public List<Student> getStudents() {
		return students;
	}

	@Override
	public Student getStudentByStudentSSID(String studentSSID) throws NotFoundException {
		Student student = getStudentBySSID(students, studentSSID);
		if (student == null) {
			throw new NotFoundException("Could not find student " + studentSSID);
		}
		return student;
	}

	@Override
	public void createStudent(Student student) {
		students.add(student);
	}

	@Override
	public Student getStudentBySSID(List<Student> students, String studentSSID) {
		for (Student student : students) {
			if(studentSSID != null && studentSSID.equalsIgnoreCase(student.getSSID())) {
				return student;
			}
		}

		return null;
	}

	


}
