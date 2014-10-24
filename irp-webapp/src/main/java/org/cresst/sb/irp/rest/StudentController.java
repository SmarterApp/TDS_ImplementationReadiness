package org.cresst.sb.irp.rest;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.student.Student;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.cresst.sb.irp.exceptions.ValidationException;
import org.cresst.sb.irp.service.StudentService;
import org.cresst.sb.irp.validation.StudentValidator;
import org.cresst.sb.irp.validation.StudentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@Controller
public class StudentController {
	private static Logger logger = Logger.getLogger(StudentController.class);

	@Autowired
	private StudentService studentService;
	
	public StudentController(){

	}
	
	@RequestMapping(value="/students", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<Student> getStudents(){
		return studentService.getStudents();
		
	}
	
	@RequestMapping(value="/students/{studentIdentifier}", 
			method = RequestMethod.GET, 
			produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Student getStudentByStudentIdentifier(@PathVariable("studentIdentifier") String studentIdentifier){
		if (!NumberUtils.isNumber(studentIdentifier)) {
			logger.info("getStudentByStudentIdentifier error studentIdentifier is not a number");
		} else {
			return studentService.getStudentByStudentIdentifier(studentIdentifier);
		}
		return null;
		
	}
	
	@RequestMapping(value="/students/addstudent", method = RequestMethod.POST)
	public ResponseEntity<Student> createStudent(@RequestBody Student student, BindingResult bindingResult){
		new StudentValidator().validate(student, bindingResult);
		if (bindingResult.hasErrors()){
			logger.info("createStudent() method: Validation errors occurred");
			throw new ValidationException("Validation errors occurred");
		} else {
			studentService.createStudent(student);
			logger.info("createStudent() method: new student successfully saved.");
			return new ResponseEntity<Student>(student,
					HttpStatus.CREATED);
		}
	}
	
	
	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
	/* . . . . . . . . . . . . . EXCEPTION HANDLERS . . . . . . . . . . . . .. */
	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	@ExceptionHandler(NotFoundException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Not Found")
	public String handleNotFoundException(NotFoundException ex) {
		logger.info("NotFoundException: " + ex.getMessage());
		return ex.getMessage();
	}
	
}
