package org.cresst.sb.irp.utils;

import java.io.IOException;
import java.util.List;

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
	
	//added DOB -  <ExamineeAttribute context="FINAL" name="DOB" 
	public enum EnumExamineeAttributeAcceptValues {
		LastOrSurname, FirstName, MiddleName, Birthdate, DOB, StudentIdentifier, AlternateSSID, GradeLevelWhenAssessed,
		Sex, HispanicOrLatinoEthnicity, AmericanIndianOrAlaskaNative, Asian, BlackOrAfricanAmerican, White,
		NativeHawaiianOrOtherPacificIslander, DemographicRaceTwoOrMoreRaces, IDEAIndicator, LEPStatus, Section504Status,
		EconomicDisadvantageStatus, LanguageCode, EnglishLanguageProficiencyLevel, MigrantStatus, 
		FirstEntryDateIntoUSSchool, LimitedEnglishProficiencyEntryDate, LEPExitDate,
		TitleIIILanguageInstructionProgramType, PrimaryDisabilityType;
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
			validateField(examineeAttribute, EnumFieldCheckType.PC, EnumExamineeAttributeFieldName.name, fieldCheckType);

			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumExamineeAttributeFieldName.value.toString());
			cellCategory.setTdsFieldNameValue(examineeAttribute.getValue());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.PC);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(examineeAttribute, EnumFieldCheckType.PC, EnumExamineeAttributeFieldName.value, fieldCheckType);

			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumExamineeAttributeFieldName.context.toString());
			cellCategory.setTdsFieldNameValue(examineeAttribute.getContext().toString());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(examineeAttribute, EnumFieldCheckType.P, EnumExamineeAttributeFieldName.context, fieldCheckType);

			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumExamineeAttributeFieldName.contextDate.toString());
			cellCategory.setTdsFieldNameValue(examineeAttribute.getContextDate().toString());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(examineeAttribute, EnumFieldCheckType.P, EnumExamineeAttributeFieldName.contextDate, fieldCheckType);

		} catch (Exception e) {
			logger.error("analysisEachExamineeAttribute exception: ", e);
		}
	}

	private void validateField(ExamineeAttribute examineeAttribute, EnumFieldCheckType enumFieldCheckType,
			EnumExamineeAttributeFieldName enumExamineeAttributeFieldName, FieldCheckType fieldCheckType) {
		try {
			switch (enumFieldCheckType) {
			case D:
				break;
			case P:
				checkP(examineeAttribute, enumExamineeAttributeFieldName, fieldCheckType);
				break;
			case PC:
				checkP(examineeAttribute, enumExamineeAttributeFieldName, fieldCheckType);
				checkC(examineeAttribute, enumExamineeAttributeFieldName, fieldCheckType);
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
				processP(examineeAttribute.getName(), fieldCheckType);
				break;
			case value:
				processP(examineeAttribute.getValue(), fieldCheckType);
				break;
			case context:
				// processP(examineeAttribute.getContext(), fieldCheckType);
				break;
			case contextDate:
				// processP(examineeAttribute.getContextDate(), fieldCheckType);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("checkP exception: ", e);
		}
	}

	private void checkC(ExamineeAttribute examineeAttribute, EnumExamineeAttributeFieldName enumExamineeAttributeFieldName,
			FieldCheckType fieldCheckType) {
		try {
			switch (enumExamineeAttributeFieldName) {
			case name:
				processName(examineeAttribute, fieldCheckType);
				break;
			case value:
				//processName(fieldCheckType);
				break;
			case context:
				//processName(fieldCheckType);
				break;	
			case contextDate:
				//processName(fieldCheckType);
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
			/*String testSubject = test.getSubject();
			List<Property> listProperty = testpackage.getProperty();
			String subjectValueFromTestPackage = getSubjectPropertyValueFromListProperty(listProperty);
			if (subjectValueFromTestPackage != null) {
				if (subjectValueFromTestPackage.equals("MATH") && testSubject.equals("MA")
						|| subjectValueFromTestPackage.equals("ELA") && testSubject.equals("ELA")) {
					fieldCheckType.setCorrectValue(true);
				}
			}*/
		} catch (Exception e) {
			logger.error("processName exception: ", e);
		}

	}

	
	private void processExamineeAttributeName(String nameValue, FieldCheckType fieldCheckType){
		try {
			EnumExamineeAttributeAcceptValues ev = EnumValueExist(nameValue);
			if (ev != null){
				fieldCheckType.setCorrectValue(true);
			}
			
			
		} catch (Exception e) {
			logger.error("processExamineeAttributeName exception: ", e);
		}
	}

	//may need to change this function tomorrow
	private EnumExamineeAttributeAcceptValues EnumValueExist(String nameValue){
		for (EnumExamineeAttributeAcceptValues me : EnumExamineeAttributeAcceptValues.values()) {
	        if (me.name().equalsIgnoreCase(nameValue))
	            return me;
	    }
	    return null;
	}
	
}
