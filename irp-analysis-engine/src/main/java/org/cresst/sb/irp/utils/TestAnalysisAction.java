package org.cresst.sb.irp.utils;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.analysis.ExamineeCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.TestCategory;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Test;
import org.springframework.stereotype.Service;

@Service
public class TestAnalysisAction extends AnalysisAction {
	private static Logger logger = Logger.getLogger(TestAnalysisAction.class);

	@Override
	public void analysisTest() throws IOException {
		try {
			TestCategory testCategory;
			FieldCheckType fieldCheckType;
			IndividualResponse individualResponse = getIndividualResponse();
			List<TestCategory> listTestCategory = individualResponse.getListTestCategory();
			Test test = getTest();

			testCategory = new TestCategory();
			listTestCategory.add(testCategory);
			testCategory.setTdsFieldName("name");
			testCategory.setTdsFieldNameValue(test.getName());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			testCategory.setFieldCheckType(fieldCheckType);
			validateField(test, EnumFieldName.TestName, EnumFieldCheckType.PC, fieldCheckType);

			testCategory = new TestCategory();
			listTestCategory.add(testCategory);
			testCategory.setTdsFieldName("subject");
			testCategory.setTdsFieldNameValue(test.getSubject());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			testCategory.setFieldCheckType(fieldCheckType);
			validateField(test, EnumFieldName.TestSubject, EnumFieldCheckType.PC, fieldCheckType);
	
			testCategory = new TestCategory();
			listTestCategory.add(testCategory);
			testCategory.setTdsFieldName("testId");
			testCategory.setTdsFieldNameValue(test.getTestId());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			testCategory.setFieldCheckType(fieldCheckType);
			validateField(test, EnumFieldName.TestTestId, EnumFieldCheckType.PC, fieldCheckType);

		} catch (Exception e) {
			logger.error("analysisTest exception: ", e);
		}
	}

	@Override
	public void analysisExaminee() throws IOException {
		try {
			ExamineeCategory examineeCategory = new ExamineeCategory();
			FieldCheckType fieldCheckType = new FieldCheckType();
			examineeCategory.setFieldCheckType(fieldCheckType);
			IndividualResponse individualResponse = getIndividualResponse();
			individualResponse.setExamineeCategory(examineeCategory);
			
			TDSReport tdsReport = getTdsReport();
			Examinee examinee = tdsReport.getExaminee();
			Long key = examinee.getKey();
			if (key != null){
				examineeCategory.setKey(key);
				fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
				validateField(key, EnumFieldCheckType.PC, fieldCheckType);
			}
			
		} catch (Exception e) {
			logger.error("analysisExaminee exception: ", e);
		}
	}

	@Override
	public void analysisExamineeAttribute() throws IOException {
		// TODO Auto-generated method stub
		
	}

}
