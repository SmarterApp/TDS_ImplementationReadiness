package org.cresst.sb.irp.dao;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.EnumUtils;
import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.ExamineeRelationshipCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.student.Student;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee.ExamineeRelationship;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class ExamineeRelationshipAnalysisAction extends AnalysisAction {
	private static Logger logger = Logger.getLogger(ExamineeRelationshipAnalysisAction.class);

	public enum EnumExamineeRelationshipFieldName {
		name, value, context, contextDate, entityKey;
	}
	
	//need to double check with the UPDATE document
	public enum EnumExamineeRelationshipAcceptValues {
		DistrictId, DistrictName, SchoolId, SchoolName, StateName, StudentGroupName,
		ResponsibleDistrictIdentifier, OrganizationName, ResponsibleInstitutionIdentifier, NameOfInstitution,
		StateAbbreviation;
	}
	
	@Override
	public void analysis() throws IOException {
		try {
			IndividualResponse individualResponse = getIndividualResponse();
			List<ExamineeRelationshipCategory> listExamineeRelationshipCategory = individualResponse.getExamineeRelationshipCategories();
			TDSReport tdsReport = individualResponse.getTDSReport();
			
			ExamineeRelationshipCategory examineeRelationshipCategory;
			Examinee examinee = tdsReport.getExaminee();
			Student student = getStudent(examinee.getKey());
			List<ExamineeRelationship> listExamineeRelationship = getExamineeRelationships(examinee);
			if (listExamineeRelationship != null) {
				for (ExamineeRelationship er : listExamineeRelationship) {
					System.out.println("relation ...." + er.getName());
					examineeRelationshipCategory = new ExamineeRelationshipCategory();
					listExamineeRelationshipCategory.add(examineeRelationshipCategory);
					analysisEachExamineeRelationship(examineeRelationshipCategory, er, student);
				}
			}
		} catch (Exception e) {
			logger.error("analysis exception: ", e);
		}
	}
	
	private void analysisEachExamineeRelationship(ExamineeRelationshipCategory examineeRelationshipCategory,
			ExamineeRelationship examineeRelationship, Student student) {
		try {
			List<CellCategory> listCellCategory = examineeRelationshipCategory.getListCellCategory();
			CellCategory cellCategory;
			FieldCheckType fieldCheckType;
			
			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumExamineeRelationshipFieldName.name.toString());
			cellCategory.setTdsFieldNameValue(examineeRelationship.getName());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(examineeRelationship, EnumFieldCheckType.P, EnumExamineeRelationshipFieldName.name, fieldCheckType, student);

			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumExamineeRelationshipFieldName.value.toString());
			cellCategory.setTdsFieldNameValue(examineeRelationship.getValue());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(examineeRelationship, EnumFieldCheckType.P, EnumExamineeRelationshipFieldName.value, fieldCheckType, student);

			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumExamineeRelationshipFieldName.context.toString());
			cellCategory.setTdsFieldNameValue(examineeRelationship.getContext().toString());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(examineeRelationship, EnumFieldCheckType.P, EnumExamineeRelationshipFieldName.context, fieldCheckType, student);

			
			
		} catch (Exception e) {
			logger.error("analysisEachExamineeRelationship exception: ", e);
		}
	}

	private void validateField(ExamineeRelationship examineeRelationship, EnumFieldCheckType enumFieldCheckType,
			EnumExamineeRelationshipFieldName enumFieldName, FieldCheckType fieldCheckType, Student student) {
		try {
			switch (enumFieldCheckType) {
			case D:
				break;
			case P:
				checkP(examineeRelationship, enumFieldName, fieldCheckType);
				break;
			case PC:
				checkP(examineeRelationship, enumFieldName, fieldCheckType);
				//checkC(examineeRelationship, enumFieldName, fieldCheckType, student);
				break;
			}
		} catch (Exception e) {
			logger.error("validateField exception: ", e);
		}
	}
	
	private void checkP(ExamineeRelationship examineeRelationship, EnumExamineeRelationshipFieldName enumFieldName,
			FieldCheckType fieldCheckType) {
		try {
			switch (enumFieldName) {
			case name:
				validateToken(examineeRelationship.getName(), fieldCheckType);
				processAcceptableEnum(examineeRelationship.getName(), fieldCheckType, EnumExamineeRelationshipAcceptValues.class);
				break;
			case value:
				validateToken(examineeRelationship.getValue(), fieldCheckType);
				validatePritableASCIIone(examineeRelationship.getValue(), fieldCheckType);
				break;
			case context:
				validateToken(examineeRelationship.getContext().toString(), fieldCheckType);
				processAcceptableContextEnum(examineeRelationship.getContext().toString(), fieldCheckType, EnumExamineeAttributeContextAcceptValues.class);
				break;
			case contextDate:
				// processP(examineeRelationship.getContextDate(), fieldCheckType);
				break;
			case entityKey:
				validateToken(examineeRelationship.getEntityKey().toString(), fieldCheckType);
				//validate accept value
				break;	
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("checkP exception: ", e);
		}
	}

	private void processAcceptableEnum(String fieldValue, FieldCheckType fieldCheckType,
			Class<EnumExamineeRelationshipAcceptValues> class1) {
		try {
			System.out.println("fieldValue ->" + fieldValue);
			if (fieldValue != null && !fieldValue.trim().isEmpty()) {
				if (EnumUtils.isValidEnum(class1, fieldValue)) {
					fieldCheckType.setAcceptableValue(true);
				} 
			}
		} catch (Exception e) {
			logger.error("processAcceptableEnum exception: ", e);
		}
	}
	
}
