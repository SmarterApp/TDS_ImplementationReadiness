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
import org.springframework.stereotype.Service;

@Service
public class ExamineeAnalysisAction extends AnalysisAction<Examinee, ExamineeAnalysisAction.EnumExamineeFieldName> {
	private static Logger logger = Logger.getLogger(ExamineeAnalysisAction.class);

	static public enum EnumExamineeFieldName {
		key, isDemo
	}
	
	@Override
	public void analyze(IndividualResponse individualResponse) throws IOException {
		TDSReport tdsReport = individualResponse.getTDSReport();
		Examinee examinee = tdsReport.getExaminee();
		ExamineeCategory examineeCategory = new ExamineeCategory();
		individualResponse.setExamineeCategory(examineeCategory);

		validate(examineeCategory, examinee, examinee.getKey(), EnumFieldCheckType.P, EnumExamineeFieldName.key);
		validate(examineeCategory, examinee, examinee.getIsDemo(), EnumFieldCheckType.D, EnumExamineeFieldName.isDemo);
	}

	@Override
	protected void checkP(Examinee examinee, EnumExamineeFieldName enumFieldName, FieldCheckType fieldCheckType){
		try {
			switch (enumFieldName) {
			case key:
				// <xs:attribute name="key" type="xs:long" />
				processP_Positive64bit(examinee.getKey(), fieldCheckType);
				break;
			case isDemo:
				//  <xs:attribute name="isDemo" type="Bit" />
				//	<xs:simpleType name="Bit">
				//<xs:restriction base="xs:unsignedByte">
				//<xs:minInclusive value="0" />
				//<xs:maxInclusive value="1" />
				//</xs:restriction>
				//</xs:simpleType>
				processP(Short.toString(examinee.getIsDemo()), fieldCheckType, false); //last param ->required N
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("checkP exception: ", e);
		}
	}
	
	@Override
	protected void checkC(Examinee examinee, EnumExamineeFieldName enumFieldName, FieldCheckType fieldCheckType){
		try {
			switch (enumFieldName) {
			case key:
				processC(examinee, fieldCheckType);
				break;
			case isDemo:
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("checkC exception: ", e);
		}
	}
	
	
	private void processC(Examinee examinee, FieldCheckType fieldCheckType) {
		try {
			Student student = getStudent(examinee.getKey()); //Long tempKey = 8505L;
			if (student != null) {
				fieldCheckType.setCorrectValue(true);
			}
		} catch (Exception e) {
			logger.error("processC exception: ", e);
		}
	}

}
