package org.cresst.sb.irp.dao;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.dao.ExamineeAttributeAnalysisAction.EnumExamineeAttributeFieldName;
import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.ExamineeRelationshipCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.student.Student;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee.ExamineeAttribute;
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
	
	public enum EnumExamineeRelationshipAcceptValues {
		DistrictId, DistrictName, SchoolId, SchoolName, StateName, StudentGroupName;
	}
	
	@Override
	public void analysis() throws IOException {
		try {
			IndividualResponse individualResponse = getIndividualResponse();
			List<ExamineeRelationshipCategory> listExamineeRelationshipCategory = individualResponse.getListExamineeRelationshipCategory();
			ExamineeRelationshipCategory examineeRelationshipCategory;
			TDSReport tdsReport = getTdsReport();
			Examinee examinee = tdsReport.getExaminee();
			Student student = getStudent(examinee.getKey());
			System.out.println("student first name in relationship ->" + student.getFirstName());
			List<ExamineeRelationship> listExamineeRelationship = getExamineeRelationships(examinee);
			if (listExamineeRelationship != null) {
				for (ExamineeRelationship er : listExamineeRelationship) {
					System.out.println("ddddd...." + er.getName());
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
			validateField(examineeRelationship, EnumFieldCheckType.P, EnumExamineeRelationshipFieldName.name, fieldCheckType);

			
		} catch (Exception e) {
			logger.error("analysisEachExamineeRelationship exception: ", e);
		}
	}

	private void validateField(ExamineeRelationship examineeRelationship, EnumFieldCheckType enumFieldCheckType,
			EnumExamineeRelationshipFieldName enumExamineeRelationshipFieldName, FieldCheckType fieldCheckType) {
		try {
			switch (enumFieldCheckType) {
			case D:
				break;
			case P:
				checkP(examineeRelationship, enumExamineeRelationshipFieldName, fieldCheckType);
				break;
			case PC:
				checkP(examineeRelationship, enumExamineeRelationshipFieldName, fieldCheckType);
				//checkC(examineeRelationship, enumExamineeRelationshipFieldName, fieldCheckType);
				break;
			}
		} catch (Exception e) {
			logger.error("validateField exception: ", e);
		}
	}
	
	private void checkP(ExamineeRelationship examineeRelationship, EnumExamineeRelationshipFieldName enumExamineeRelationshipFieldName,
			FieldCheckType fieldCheckType) {
		try {
			switch (enumExamineeRelationshipFieldName) {
			case name:
				////processP(examineeRelationship.getName(), fieldCheckType);
				break;
			case value:
				/////processP(examineeRelationship.getValue(), fieldCheckType);
				break;
			case context:
				// processP(examineeRelationship.getContext(), fieldCheckType);
				break;
			case contextDate:
				// processP(examineeRelationship.getContextDate(), fieldCheckType);
				break;
			case entityKey:
				//processP(examineeRelationship.getEntityKey(), fieldCheckType);
				break;	
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("checkP exception: ", e);
		}
	}

	
	
}

