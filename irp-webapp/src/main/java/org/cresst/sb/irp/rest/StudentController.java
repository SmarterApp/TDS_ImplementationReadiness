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
import org.jsondoc.core.annotation.*;
import org.jsondoc.core.pojo.ApiParamType;
import org.jsondoc.core.pojo.ApiVerb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

@Api(name = "Student API", description = "REST API for Student data")
@Controller
public class StudentController {
	private static Logger logger = Logger.getLogger(StudentController.class);

	@Autowired
	private StudentService studentService;

	@ApiMethod(path = "/students", description = "Returns a list of Students", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value="/students", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject List<Student> getStudents(){
		return studentService.getStudents();
		
	}

	@ApiMethod(path = "/students/{studentSSID}", description = "Returns a Student", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value="/students/{studentSSID}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject Student getStudentByStudentSSID(
			@ApiParam(name = "studentSSID", description = "The Student ID", paramType = ApiParamType.PATH)
			@PathVariable("studentSSID") String studentSSID){
		if (!NumberUtils.isNumber(studentSSID)) {
			logger.info("getStudentByStudentIdentifier error studentIdentifier is not a number");
		} else {
			return studentService.getStudentByStudentSSID(studentSSID); //getStudentByStudentIdentifier(studentIdentifier);
		}
		return null;
		
	}

	@ApiMethod(path = "/students/addstudent", description = "Adds a Student", verb = ApiVerb.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value="/students/addstudent", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public @ApiResponseObject Student createStudent(
			@ApiBodyObject
			@RequestBody Student student, BindingResult bindingResult){
		new StudentValidator().validate(student, bindingResult);
		if (bindingResult.hasErrors()){
			logger.info("createStudent() method: Validation errors occurred");
			throw new ValidationException("Validation errors occurred");
		} else {
			studentService.createStudent(student);
			logger.info("createStudent() method: new student successfully saved.");
			return student;
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
