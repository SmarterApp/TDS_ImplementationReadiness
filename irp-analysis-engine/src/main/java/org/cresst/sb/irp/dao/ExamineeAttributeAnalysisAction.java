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
	public void analysis(IndividualResponse individualResponse) throws IOException {
		try {
			List<ExamineeAttributeCategory> listExamineeAttributeCategory = individualResponse.getExamineeAttributeCategories();
			TDSReport tdsReport = individualResponse.getTDSReport();

			ExamineeAttributeCategory examineeAttributeCategory;

			Examinee examinee = tdsReport.getExaminee();
			Student student = getStudent(examinee.getKey());
			List<ExamineeAttribute> listExamineeAttribute = getExamineeAttributes(examinee);
			if (listExamineeAttribute != null) {
				for (ExamineeAttribute ex : listExamineeAttribute) {
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
			List<CellCategory> listCellCategory = examineeAttributeCategory.getCellCategories();
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
			validateField(examineeAttribute, EnumFieldCheckType.P, EnumExamineeAttributeFieldName.context, fieldCheckType,
					student);

			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumExamineeAttributeFieldName.contextDate.toString());
			cellCategory.setTdsFieldNameValue(examineeAttribute.getContextDate().toString());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(examineeAttribute, EnumFieldCheckType.P, EnumExamineeAttributeFieldName.contextDate, fieldCheckType,
					student);

		} catch (Exception e) {
			logger.error("analysisEachExamineeAttribute exception: ", e);
		}
	}

	private void validateField(ExamineeAttribute examineeAttribute, EnumFieldCheckType enumFieldCheckType,
			EnumExamineeAttributeFieldName enumFieldName, FieldCheckType fieldCheckType, Student student) {
		try {
			switch (enumFieldCheckType) {
			case D:
				break;
			case P:
				checkP(examineeAttribute, enumFieldName, fieldCheckType);
				break;
			case PC:
				checkP(examineeAttribute, enumFieldName, fieldCheckType);
				checkC(examineeAttribute, enumFieldName, fieldCheckType, student);
				break;
			}
		} catch (Exception e) {
			logger.error("validateField exception: ", e);
		}
	}

	private void checkP(ExamineeAttribute examineeAttribute, EnumExamineeAttributeFieldName enumFieldName,
			FieldCheckType fieldCheckType) {
		try {
			switch (enumFieldName) {
			case name:
				//	<xs:attribute name="name" use="required" />
				processAcceptableEnum(examineeAttribute.getName(), fieldCheckType, EnumExamineeAttributeAcceptValues.class);
				break;
			case value:
				// <xs:attribute name="value" />
				processP(examineeAttribute.getValue(), fieldCheckType, true); //last param ->required Y
				break;
			case context:
				// <xs:simpleType name="Context">
				// <xs:restriction base="xs:token">
				// <xs:enumeration value="INITIAL" />
				// <xs:enumeration value="FINAL" />
				// </xs:restriction>
				// </xs:simpleType>
				processP(examineeAttribute.getContext().toString(), fieldCheckType, true);  //last param ->required Y
				break;
			case contextDate:
				// <xs:attribute name="contextDate" use="required" type="xs:dateTime" />
				processP(examineeAttribute.getContextDate().toString(), fieldCheckType, true); 
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("checkP exception: ", e);
		}
	}

	private void checkC(ExamineeAttribute examineeAttribute, EnumExamineeAttributeFieldName enumFieldName,
			FieldCheckType fieldCheckType, Student student) {
		try {
			switch (enumFieldName) {
			case name:
				// processName(examineeAttribute, fieldCheckType);
				break;
			case value:
				processFieldNameValue(examineeAttribute, fieldCheckType, student);
				break;
			case context:
				break;
			case contextDate:
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
			String value = examineeAttribute.getValue();
			String studentValue = getStudentValueByName(fieldName, EnumExamineeAttributeAcceptValues.class, student);
			if (studentValue != null && value != null) {
				if (studentValue.equals(value)) {
					fieldCheckType.setCorrectValue(true);
				}
			}
		} catch (Exception e) {
			logger.error("processFieldNameValue exception: ", e);
		}

	}

	private void processAcceptableEnum(String fieldValue, FieldCheckType fieldCheckType,
			Class<EnumExamineeAttributeAcceptValues> class1) {
		try {
			if (fieldValue != null && !fieldValue.trim().isEmpty()) {
				if (EnumUtils.isValidEnum(class1, fieldValue)) {
					setPcorrect(fieldCheckType);
				}
			}
		} catch (Exception e) {
			logger.error("processAcceptableEnum exception: ", e);
		}
	}
	
	public String getStudentValueByName(String fieldNameValue, Class<EnumExamineeAttributeAcceptValues> class1, Student student) {
		String str = null;
		try {
			if (EnumUtils.isValidEnum(class1, fieldNameValue)) {
				for (EnumExamineeAttributeAcceptValues e : EnumExamineeAttributeAcceptValues.values()) {
					if (e.name().equals(fieldNameValue)
							&& e.name().equals(EnumExamineeAttributeAcceptValues.FirstName.toString())) {
						str = student.getFirstName();
					} else if (e.name().equals(fieldNameValue)
							&& e.name().equals(EnumExamineeAttributeAcceptValues.LastOrSurname.toString())) {
						str = student.getLastOrSurname();
					} else if (e.name().equals(fieldNameValue)
							&& e.name().equals(EnumExamineeAttributeAcceptValues.StudentIdentifier.toString())) {
						str = student.getSSID();
					} else if (e.name().equals(fieldNameValue)
							&& e.name().equals(EnumExamineeAttributeAcceptValues.DOB.toString())) {
						str = student.getBirthdate();
					} else if (e.name().equals(fieldNameValue)
							&& e.name().equals(EnumExamineeAttributeAcceptValues.BlackOrAfricanAmerican.toString())) {
						str = student.getBlackOrAfricanAmerican();
					}// ......
				}
			}
		} catch (Exception e) {
			logger.error("getStudentValueByName exception: ", e);
		}
		return str;
	}

}
