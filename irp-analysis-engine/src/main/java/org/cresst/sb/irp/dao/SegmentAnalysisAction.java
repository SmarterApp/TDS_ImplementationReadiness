package org.cresst.sb.irp.dao;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.OpportunityCategory;
import org.cresst.sb.irp.domain.analysis.SegmentCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Segment;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class SegmentAnalysisAction extends AnalysisAction {
	private static Logger logger = Logger.getLogger(SegmentAnalysisAction.class);

	public enum EnumSegmentFieldName {
		id, position, formId, formKey, algorithm, algorithmVersion;
	}
	
	@Override
	public void analysis() throws IOException {
		try {
			IndividualResponse individualResponse = getIndividualResponse();
			TDSReport tdsReport = individualResponse.getTDSReport();
			OpportunityCategory opportunityCategory = individualResponse.getOpportunityCategory();
			List<SegmentCategory> listSegmentCategory = opportunityCategory.getSegmentCategories();

			SegmentCategory segmentCategory;
			Opportunity opportunity = getOpportunity(tdsReport);
			List<Segment> listSegment = opportunity.getSegment();
			for (Segment s : listSegment) {
				segmentCategory = new SegmentCategory();
				listSegmentCategory.add(segmentCategory);
				analysisEachSegment(segmentCategory, s);
			}
		} catch (Exception e) {
			logger.error("analysis exception: ", e);
		}
	}
	
	private void analysisEachSegment(SegmentCategory segmentCategory, Segment segment){
		try {
			List<CellCategory> listCellCategory = segmentCategory.getCellCategories();
			CellCategory cellCategory;
			FieldCheckType fieldCheckType;

			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumSegmentFieldName.id.toString());
			cellCategory.setTdsFieldNameValue(segment.getId());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(segment, EnumFieldCheckType.P, EnumSegmentFieldName.id, fieldCheckType);
			
			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumSegmentFieldName.position.toString());
			cellCategory.setTdsFieldNameValue(Short.toString(segment.getPosition()));
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(segment, EnumFieldCheckType.P, EnumSegmentFieldName.position, fieldCheckType);

			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumSegmentFieldName.formId.toString());
			cellCategory.setTdsFieldNameValue(segment.getFormId());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.D);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(segment, EnumFieldCheckType.D, EnumSegmentFieldName.formId, fieldCheckType);
			
			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumSegmentFieldName.formKey.toString());
			cellCategory.setTdsFieldNameValue(segment.getFormKey());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.D);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(segment, EnumFieldCheckType.D, EnumSegmentFieldName.formKey, fieldCheckType);
			
			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumSegmentFieldName.algorithm.toString());
			cellCategory.setTdsFieldNameValue(segment.getAlgorithm());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(segment, EnumFieldCheckType.P, EnumSegmentFieldName.algorithm, fieldCheckType);
			
			cellCategory = new CellCategory();
			listCellCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumSegmentFieldName.algorithmVersion.toString());
			cellCategory.setTdsFieldNameValue(segment.getAlgorithmVersion());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(segment, EnumFieldCheckType.P, EnumSegmentFieldName.algorithmVersion, fieldCheckType);
			
		} catch (Exception e) {
			logger.error("analysisEachSegment exception: ", e);
		}
		
	}

	private void validateField(Segment segment, EnumFieldCheckType enumFieldCheckType,
			EnumSegmentFieldName enumFieldName, FieldCheckType fieldCheckType) {
		try {
			switch (enumFieldCheckType) {
			case D:
				break;
			case P:
				checkP(segment, enumFieldName, fieldCheckType);
				break;
			case PC:
				checkP(segment, enumFieldName, fieldCheckType);
				//checkC(segment, enumFieldName, fieldCheckType);
				break;
			}
		} catch (Exception e) {
			logger.error("validateField exception: ", e);
		}
	}
	
	// Field Check Type (P) --> check that field is not empty, and field value is of correct data type
	// and within acceptable values
	private void checkP(Segment segment, EnumSegmentFieldName enumFieldName, FieldCheckType fieldCheckType) {
		try {
			switch (enumFieldName) {
			case id:
				//	<xs:attribute name="id" use="required" />
				processP_PritableASCIIone(segment.getId(), fieldCheckType);
				break;
			case position:
				//	<xs:attribute name="position" use="required">
				//<xs:simpleType>
				//<xs:restriction base="xs:unsignedByte">
				//	<xs:minInclusive value="1" />
				//</xs:restriction>
				//</xs:simpleType>
				//</xs:attribute>
				processP_Positive32bit(Short.toString(segment.getPosition()), fieldCheckType);
				break;
			case formKey:
				break;
			case formId:
				break;
			case algorithm:
				//<xs:attribute name="algorithm" use="required" />
				processP_PritableASCIIone(segment.getAlgorithm(), fieldCheckType);
				break;
			case algorithmVersion:
				//   <xs:attribute name="algorithmVersion" />
				processP_PritableASCIIone(segment.getAlgorithmVersion(), fieldCheckType);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("checkP exception: ", e);
		}
	}
	

}
