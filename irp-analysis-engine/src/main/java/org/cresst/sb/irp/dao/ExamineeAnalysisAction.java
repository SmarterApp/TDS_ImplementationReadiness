package org.cresst.sb.irp.dao;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.analysis.ExamineeCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.student.Student;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class ExamineeAnalysisAction extends AnalysisAction {
	private static Logger logger = Logger.getLogger(ExamineeAnalysisAction.class);

	@Override
	public void analysis() throws IOException {
		try {
			ExamineeCategory examineeCategory = new ExamineeCategory();
			FieldCheckType fieldCheckType = new FieldCheckType();
			examineeCategory.setFieldCheckType(fieldCheckType);
			IndividualResponse individualResponse = getIndividualResponse();
			individualResponse.setExamineeCategory(examineeCategory);

			TDSReport tdsReport = getTdsReport();
			Examinee examinee = tdsReport.getExaminee();
			Long key = examinee.getKey();
			System.out.println("key========>" + key);
			if (key != null) {
				examineeCategory.setKey(key);
				fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
				validateField(key, EnumFieldCheckType.PC, fieldCheckType);
			}
		} catch (Exception e) {
			logger.error("analysis exception: ", e);
		}

	}

	private void validateField(Long key, EnumFieldCheckType enumFieldCheckType, FieldCheckType fieldCheckType) {
		try {
			switch (enumFieldCheckType) {
			case D:
				break;
			case P:
				processP(key, fieldCheckType);
				break;
			case PC:
				processP(key, fieldCheckType);
				processC(key, fieldCheckType);
				break;
			}
		} catch (Exception e) {
			logger.error("validateField exception: ", e);
		}
	}

	private void processP(Long key, FieldCheckType fieldCheckType) {
		try {
			if (sign(key) > 0) {
				fieldCheckType.setCorrectDataType(true);
				fieldCheckType.setFieldEmpty(false);
				fieldCheckType.setAcceptableValue(true);
			}
		} catch (Exception e) {
			logger.error("processP exception: ", e);
		}
	}
	
	private void processC(Long key, FieldCheckType fieldCheckType) {
		try {
			Long tempKey = 8505L;
			Student student = getStudent(tempKey); //getStudent(key);
			if (student != null) {
				fieldCheckType.setCorrectValue(true);
			}
		} catch (Exception e) {
			logger.error("processC exception: ", e);
		}
	}


}
