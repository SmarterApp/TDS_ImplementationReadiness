package org.cresst.sb.irp.utils;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.analysis.ExamineeAttributeCategory;
import org.cresst.sb.irp.domain.analysis.ExamineeCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.student.Student;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Test;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee.ExamineeAttribute;
import org.cresst.sb.irp.domain.testpackage.Testpackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class ProcessAnalysisAction extends AnalysisAction {
	private static Logger logger = Logger.getLogger(ProcessAnalysisAction.class);

	@Override
	public void analysisTest() throws IOException {
		try {
			CellCategory testCategory;
			FieldCheckType fieldCheckType;
			IndividualResponse individualResponse = getIndividualResponse();
			List<CellCategory> listTestCategory = individualResponse.getListTestCategory();
			Test test = getTest();

			testCategory = new CellCategory();
			listTestCategory.add(testCategory);
			testCategory.setTdsFieldName(EnumTestFieldName.name.toString());
			testCategory.setTdsFieldNameValue(test.getName());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			testCategory.setFieldCheckType(fieldCheckType);
			validateField(test, EnumTestFieldName.name, EnumFieldCheckType.PC, fieldCheckType);

			testCategory = new CellCategory();
			listTestCategory.add(testCategory);
			testCategory.setTdsFieldName(EnumTestFieldName.subject.toString());
			testCategory.setTdsFieldNameValue(test.getSubject());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			testCategory.setFieldCheckType(fieldCheckType);
			validateField(test, EnumTestFieldName.subject, EnumFieldCheckType.PC, fieldCheckType);

			testCategory = new CellCategory();
			listTestCategory.add(testCategory);
			testCategory.setTdsFieldName(EnumTestFieldName.testId.toString());
			testCategory.setTdsFieldNameValue(test.getTestId());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			testCategory.setFieldCheckType(fieldCheckType);
			validateField(test, EnumTestFieldName.testId, EnumFieldCheckType.PC, fieldCheckType);

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
			if (key != null) {
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
		try {
			IndividualResponse individualResponse = getIndividualResponse();
			List<ExamineeAttributeCategory> listExamineeAttributeCategory = individualResponse.getListExamineeAttributeCategory();
			ExamineeAttributeCategory examineeAttributeCategory;
			TDSReport tdsReport = getTdsReport();
			Examinee examinee = tdsReport.getExaminee();

			Student student = getStudent(examinee.getKey());
			System.out.println("student first name ->" + student.getFirstName());
			List<ExamineeAttribute> listExamineeAttribute = getExamineeAttributes(examinee);
			if (listExamineeAttribute != null) {
				for (ExamineeAttribute e : listExamineeAttribute) {
					System.out.println("ddddd...." + e.getName());
					examineeAttributeCategory = new ExamineeAttributeCategory();
					listExamineeAttributeCategory.add(examineeAttributeCategory);
					analysisIndividualExamineeAttribute(examineeAttributeCategory, e, student);

				}
			}
		} catch (Exception e) {
			logger.error("analysisExamineeAttribute exception: ", e);
		}
	}

	private void analysisIndividualExamineeAttribute(ExamineeAttributeCategory examineeAttributeCategory, 
			ExamineeAttribute examineeAttribute,
			Student student) {
		try {
			List<CellCategory> listCellCategory = examineeAttributeCategory.getListCellCategory();
			CellCategory cellCategory;
			FieldCheckType fieldCheckType;
			
			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumExamineeAttributeFieldName.name.toString());
			cellCategory.setTdsFieldNameValue(examineeAttribute.getName());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(examineeAttribute, EnumExamineeAttributeFieldName.name, EnumFieldCheckType.PC, fieldCheckType);
			
			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumExamineeAttributeFieldName.value.toString());
			cellCategory.setTdsFieldNameValue(examineeAttribute.getValue());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(examineeAttribute, EnumExamineeAttributeFieldName.value, EnumFieldCheckType.PC, fieldCheckType);
			
			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumExamineeAttributeFieldName.context.toString());
			cellCategory.setTdsFieldNameValue(examineeAttribute.getContext().toString());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(examineeAttribute, EnumExamineeAttributeFieldName.context, EnumFieldCheckType.P, fieldCheckType);
			
			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumExamineeAttributeFieldName.contextDate.toString());
			cellCategory.setTdsFieldNameValue(examineeAttribute.getContextDate().toString());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(examineeAttribute, EnumExamineeAttributeFieldName.contextDate, EnumFieldCheckType.P, fieldCheckType);
			
			
		} catch (Exception e) {
			logger.error("analysisIndividualExamineeAttribute exception: ", e);
		}
	}

	
	
}
