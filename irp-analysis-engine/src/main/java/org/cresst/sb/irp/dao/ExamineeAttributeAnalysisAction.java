package org.cresst.sb.irp.dao;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.EnumUtils;
import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.ExamineeAttributeCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.student.Student;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee.ExamineeAttribute;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class ExamineeAttributeAnalysisAction extends AnalysisAction {
	private static Logger logger = Logger.getLogger(ExamineeAttributeAnalysisAction.class);

	public enum EnumExamineeAttributeFieldName {
		name, value, context, contextDate;
	}

	// added DOB - <ExamineeAttribute context="FINAL" name="DOB"
	public enum EnumExamineeAttributeAcceptValues {
		LastOrSurname, FirstName, MiddleName, Birthdate, DOB, StudentIdentifier, AlternateSSID, GradeLevelWhenAssessed, Sex, HispanicOrLatino, Ethnicity, AmericanIndianOrAlaskaNative, Asian, BlackOrAfricanAmerican, White, NativeHawaiianOrOtherPacificIslander, DemographicRaceTwoOrMoreRaces, IDEAIndicator, LEPStatus, Section504Status, EconomicDisadvantageStatus, LanguageCode, EnglishLanguageProficiencyLevel, MigrantStatus, FirstEntryDateIntoUSSchool, LimitedEnglishProficiencyEntryDate, LEPExitDate, TitleIIILanguageInstructionProgramType, PrimaryDisabilityType;
	}

	@Override
	public void analysis() throws IOException {
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
				for (ExamineeAttribute ex : listExamineeAttribute) {
					System.out.println("ddddd...." + ex.getName());
					examineeAttributeCategory = new ExamineeAttributeCategory();
					listExamineeAttributeCategory.add(examineeAttributeCategory);
					analysisEachExamineeAttribute(examineeAttributeCategory, ex, student);

				}
			}
		} catch (Exception e) {
			logger.error("analysis exception: ", e);
		}
	}

	private void analysisEachExamineeAttribute(ExamineeAttributeCategory examineeAttributeCategory,
			ExamineeAttribute examineeAttribute, Student student) {
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
			validateField(examineeAttribute, EnumFieldCheckType.P, EnumExamineeAttributeFieldName.name, fieldCheckType, student);

			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumExamineeAttributeFieldName.value.toString());
			cellCategory.setTdsFieldNameValue(examineeAttribute.getValue());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(examineeAttribute, EnumFieldCheckType.PC, EnumExamineeAttributeFieldName.value, fieldCheckType, student);

			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumExamineeAttributeFieldName.context.toString());
			cellCategory.setTdsFieldNameValue(examineeAttribute.getContext().toString());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(examineeAttribute, EnumFieldCheckType.P, EnumExamineeAttributeFieldName.context, fieldCheckType, student);

			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumExamineeAttributeFieldName.contextDate.toString());
			cellCategory.setTdsFieldNameValue(examineeAttribute.getContextDate().toString());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(examineeAttribute, EnumFieldCheckType.P, EnumExamineeAttributeFieldName.contextDate, fieldCheckType, student);

		} catch (Exception e) {
			logger.error("analysisEachExamineeAttribute exception: ", e);
		}
	}

	private void validateField(ExamineeAttribute examineeAttribute, EnumFieldCheckType enumFieldCheckType,
			EnumExamineeAttributeFieldName enumExamineeAttributeFieldName, FieldCheckType fieldCheckType, Student student) {
		try {
			switch (enumFieldCheckType) {
			case D:
				break;
			case P:
				checkP(examineeAttribute, enumExamineeAttributeFieldName, fieldCheckType);
				break;
			case PC:
				checkP(examineeAttribute, enumExamineeAttributeFieldName, fieldCheckType);
				checkC(examineeAttribute, enumExamineeAttributeFieldName, fieldCheckType, student);
				break;
			}
		} catch (Exception e) {
			logger.error("validateField exception: ", e);
		}
	}

	private void checkP(ExamineeAttribute examineeAttribute, EnumExamineeAttributeFieldName enumExamineeAttributeFieldName,
			FieldCheckType fieldCheckType) {
		try {
			switch (enumExamineeAttributeFieldName) {
			case name:
				System.out.println("xxxxxxxxxxxxxxxxx");
				validateToken(examineeAttribute.getName(), fieldCheckType);
				processAcceptableEnum(examineeAttribute.getName(), fieldCheckType, EnumExamineeAttributeAcceptValues.class);
				break;
			case value:
				validateToken(examineeAttribute.getValue(), fieldCheckType);
				break;
			case context:
				// validateToken(examineeAttribute.getContext(), fieldCheckType);
				break;
			case contextDate:
				// validateToken(examineeAttribute.getContextDate(), fieldCheckType);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("checkP exception: ", e);
		}
	}

	private void checkC(ExamineeAttribute examineeAttribute, EnumExamineeAttributeFieldName enumExamineeAttributeFieldName,
			FieldCheckType fieldCheckType, Student student) {
		try {
			switch (enumExamineeAttributeFieldName) {
			case name:
				//processName(examineeAttribute, fieldCheckType);
				break;
			case value:
				System.out.println("VVVVVVVVVVVVVVVV");
				processFieldNameValue(examineeAttribute,  fieldCheckType, student);
				break;
			case context:
				// processName(fieldCheckType);
				break;
			case contextDate:
				// processName(fieldCheckType);
				break;
			default:
				break;
			}

		} catch (Exception e) {
			logger.error("checkC exception: ", e);
		}
	}

	private void processName(ExamineeAttribute examineeAttribute, FieldCheckType fieldCheckType) {
		try {
			
		} catch (Exception e) {
			logger.error("processName exception: ", e);
		}

	}

	private void processFieldNameValue(ExamineeAttribute examineeAttribute, FieldCheckType fieldCheckType, Student student) {
		try {
			String fieldName = examineeAttribute.getName();
			System.out.println("fieldName ------>" +  fieldName);
			String value = examineeAttribute.getValue();
			System.out.println("value ------>" +  value);
			String studentValue = getStudentValueByName(examineeAttribute.getName(), EnumExamineeAttributeAcceptValues.class, student);
			if (studentValue != null && value != null) {
				if (studentValue.equals(value)){
					fieldCheckType.setCorrectValue(true);
				}
			}
		} catch (Exception e) {
			logger.error("processFieldNameValue exception: ", e);
		}

	}
	
	
	

}
