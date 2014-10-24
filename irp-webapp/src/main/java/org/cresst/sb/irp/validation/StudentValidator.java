package org.cresst.sb.irp.validation;

import org.apache.commons.lang3.math.NumberUtils;
import org.cresst.sb.irp.domain.student.Student;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class StudentValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Student.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		Student student = (Student) target;
		String externalSSID = student.getExternalSSID();
		String birthDate = student.getBirthdate();
		String confirmationCode = student.getConfirmationCode();
		
		//. . .
		
		if (!NumberUtils.isNumber(externalSSID)) {
			errors.rejectValue("externalSSID", "error.externalSSID.invalid",
					"enter a valid number");
		} else if (externalSSID.length() < 12) {
			errors.rejectValue("externalSSID", "error.externalSSID.less",
					"must be greater than or equal to 12");
		}
		
		if (confirmationCode == null){
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmationCode",
					"error.confirmationCode.blank", "must not be blank");
		} else if (confirmationCode.contains("abc")){
			errors.rejectValue("confirmationCode", "error.confirmationCode.invalid", "not a valid confirmationcode");
		}
		
		//. . .
	}

}
