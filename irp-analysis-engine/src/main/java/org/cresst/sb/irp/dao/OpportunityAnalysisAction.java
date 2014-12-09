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
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Segment;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class OpportunityAnalysisAction extends AnalysisAction {
	private static Logger logger = Logger.getLogger(OpportunityAnalysisAction.class);

	public enum EnumOpportunityPropertyFieldName {
		server, database, clientName, key, oppId, startDate, status, opportunity, statusDate, dateCompleted, pauseCount, itemCount, ftCount, abnormalStarts, gracePeriodRestarts, taId, taName, sessionId, windowId, windowOpportunity, dateForceCompleted, qaLevel, assessmentParticipantSessionPlatformUserAgent, effectiveDate;
	}

	@Override
	public void analysis() throws IOException {
		// TODO Auto-generated method stub
		IndividualResponse individualResponse = getIndividualResponse();
		OpportunityCategory opportunityCategory = new OpportunityCategory();
		individualResponse.setOpportunityCategory(opportunityCategory);
		Opportunity opportunity = getOpportunity();

		analysisOpportunityProperty(opportunity, opportunityCategory);
		analysisSegments(opportunity, opportunityCategory);
		// List<ExamineeAttributeCategory> listExamineeAttributeCategory = individualResponse.;

	}

	private void analysisOpportunityProperty(Opportunity opportunity, OpportunityCategory opportunityCategory) {
		try {
			CellCategory cellCategory;
			FieldCheckType fieldCheckType;
			List<CellCategory> listOportunityPropertyCategory = opportunityCategory.getListOportunityPropertyCategory();

			cellCategory = new CellCategory();
			listOportunityPropertyCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumOpportunityPropertyFieldName.server.toString());
			cellCategory.setTdsFieldNameValue(opportunity.getServer());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(opportunity, EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.server, fieldCheckType);

			cellCategory = new CellCategory();
			listOportunityPropertyCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumOpportunityPropertyFieldName.database.toString());
			cellCategory.setTdsFieldNameValue(opportunity.getDatabase());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(opportunity, EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.database, fieldCheckType);

			cellCategory = new CellCategory();
			listOportunityPropertyCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumOpportunityPropertyFieldName.key.toString());
			cellCategory.setTdsFieldNameValue(opportunity.getKey());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(opportunity, EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.key, fieldCheckType);

			// TODO Auto-generated method stub
			
		} catch (Exception e) {
			logger.error("analysisOpportunityProperty exception: ", e);
		}
	}

	private void analysisSegments(Opportunity opportunity, OpportunityCategory opportunityCategory) {
		try {
			SegmentCategory segmentCategory;
			List<SegmentCategory> listSegmentCategory = opportunityCategory.getListSegmentCategory();
			
			List<Segment> listSegment = opportunity.getSegment();
			if (listSegment != null) {
				for (Segment s : listSegment) {
					System.out.println("segment...." + s.getId());
					segmentCategory = new SegmentCategory();
					listSegmentCategory.add(segmentCategory);
					analysisSegment(segmentCategory, s);
				}
			}
		} catch (Exception e) {
			logger.error("analysisSegments exception: ", e);
		}
	}

	private void analysisSegment(SegmentCategory segmentCategory,Segment s) {
		try {
			// TODO Auto-generated method stub
		} catch (Exception e) {
			logger.error("analysisSegment exception: ", e);
		}
	}
	
	private void validateField(Opportunity opportunity, EnumFieldCheckType enumFieldCheckType,
			EnumOpportunityPropertyFieldName enumFieldName, FieldCheckType fieldCheckType) {
		try {
			switch (enumFieldCheckType) {
			case D:
				break;
			case P:
				checkP(opportunity, enumFieldName, fieldCheckType);
				break;
			case PC:
				checkP(opportunity, enumFieldName, fieldCheckType);
				// checkC(opportunity, enumFieldName, fieldCheckType);
				break;
			}
		} catch (Exception e) {
			logger.error("checkP exception: ", e);
		}
	}

	// Field Check Type (P) --> check that field is not empty, and field value is of correct data type
	// and within acceptable values
	private void checkP(Opportunity opportunity, EnumOpportunityPropertyFieldName enumFieldName, FieldCheckType fieldCheckType) {
		try {
			switch (enumFieldName) {
			case server:
				validateToken(opportunity.getServer(), fieldCheckType);
				validatePritableASCII(opportunity.getServer(), fieldCheckType);
				break;
			case database:
				validateToken(opportunity.getDatabase(), fieldCheckType);
				validatePritableASCII(opportunity.getDatabase(), fieldCheckType);
				break;
			case key:
				validateToken(opportunity.getKey(), fieldCheckType);
				validatePritableASCII(opportunity.getKey(), fieldCheckType);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("checkP exception: ", e);
		}
	}

}
