package org.cresst.sb.irp.dao;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.analysis.ExamineeCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.student.Student;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ExamineeAnalysisAction extends AnalysisAction<Examinee, ExamineeAnalysisAction.EnumExamineeFieldName, Student> {
	private final Logger logger = Logger.getLogger(ExamineeAnalysisAction.class);

	static public enum EnumExamineeFieldName {
		key
	}
	
	@Override
	public void analyze(IndividualResponse individualResponse) {
		TDSReport tdsReport = individualResponse.getTDSReport();
		Examinee examinee = tdsReport.getExaminee();

		ExamineeCategory examineeCategory = new ExamineeCategory();
		individualResponse.setExamineeCategory(examineeCategory);

		// Validate only when the TDS Report contains an Examinee
		if (examinee != null) {
			Long examineeKey = examinee.getKey();
			Student student = null;
			if (examineeKey != null) {
				try {
					student = getStudent(examineeKey);
				} catch (NotFoundException ex) {
					logger.info(String.format("TDS Report contains an Examinee Key (%d) that does not match an IRP Student", examineeKey));
				}
			}
			validate(examineeCategory, examinee, examinee.getKey(), EnumFieldCheckType.PC, EnumExamineeFieldName.key, student);
		}
	}

	@Override
	protected void checkP(Examinee examinee, EnumExamineeFieldName enumFieldName, FieldCheckType fieldCheckType){
		switch (enumFieldName) {
			case key:
				// <xs:attribute name="key" type="xs:long" />
				processP_Positive64bit(examinee.getKey(), fieldCheckType);
				break;
			default:
				break;
		}
	}

	/**
	 * Checks if the field has the correct value
	 *
	 * @param examinee       Examinee fields are checked
	 * @param enumFieldName  Specifies the field to check
	 * @param fieldCheckType This is where the results are stored
	 * @param student        Examinee will be compared to Student record
	 */
	@Override
	protected void checkC(Examinee examinee, EnumExamineeFieldName enumFieldName, FieldCheckType fieldCheckType, Student student){
		switch (enumFieldName) {
			case key:
				if (student != null && student.getSSID() == examinee.getKey()) {
					setCcorrect(fieldCheckType);
				}
				break;
			default:
				break;
		}
	}

	/**
	 * Uses the IRP Student object to populate the expected value of the field being analyzed
	 *
	 * @param student         IRP Student with the expected values
	 * @param enumFieldName   Specifies the field to check
	 * @return The value of the Student object that is expected for the given EnumExamineeFieldName
	 */
	@Override
	protected String expectedValue(Student student, EnumExamineeFieldName enumFieldName) {

		String expectedValue = null;

		switch (enumFieldName) {
			case key:
				if (student != null) {
					expectedValue = String.valueOf(student.getSSID());
				}
				break;
			default:
				break;
		}

		return expectedValue;
	}
}
