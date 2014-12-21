package org.cresst.sb.irp.dao;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.OpportunityCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
public class OpportunityAnalysisAction extends AnalysisAction {
	private static Logger logger = Logger.getLogger(OpportunityAnalysisAction.class);

	public enum EnumOpportunityPropertyFieldName {
		server, database, clientName, key, oppId, startDate, status, opportunity, statusDate, dateCompleted, pauseCount, itemCount, ftCount, abnormalStarts, gracePeriodRestarts, taId, taName, sessionId, windowId, windowOpportunity, dateForceCompleted, qaLevel, assessmentParticipantSessionPlatformUserAgent, effectiveDate;
	}

	@Override
	public void analysis(IndividualResponse individualResponse) throws IOException {
		try {
			TDSReport tdsReport = individualResponse.getTDSReport();

			OpportunityCategory opportunityCategory = new OpportunityCategory();
			individualResponse.setOpportunityCategory(opportunityCategory);
			List<CellCategory> listOportunityPropertyCategory = opportunityCategory.getOpportunityPropertyCategories();
			Opportunity opportunity = getOpportunity(tdsReport);

			CellCategory cellCategory;
			FieldCheckType fieldCheckType;

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
			cellCategory.setTdsFieldName(EnumOpportunityPropertyFieldName.clientName.toString());
			cellCategory.setTdsFieldNameValue(opportunity.getClientName());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(opportunity, EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.clientName, fieldCheckType);

			cellCategory = new CellCategory();
			listOportunityPropertyCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumOpportunityPropertyFieldName.key.toString());
			cellCategory.setTdsFieldNameValue(opportunity.getKey());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(opportunity, EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.key, fieldCheckType);

			cellCategory = new CellCategory();
			listOportunityPropertyCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumOpportunityPropertyFieldName.oppId.toString());
			cellCategory.setTdsFieldNameValue(opportunity.getOppId());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(opportunity, EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.oppId, fieldCheckType);

			cellCategory = new CellCategory();
			listOportunityPropertyCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumOpportunityPropertyFieldName.startDate.toString());
			cellCategory.setTdsFieldNameValue(opportunity.getStartDate());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(opportunity, EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.startDate, fieldCheckType);

			cellCategory = new CellCategory();
			listOportunityPropertyCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumOpportunityPropertyFieldName.status.toString());
			cellCategory.setTdsFieldNameValue(opportunity.getStatus());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(opportunity, EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.status, fieldCheckType);
			
			cellCategory = new CellCategory();
			listOportunityPropertyCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumOpportunityPropertyFieldName.opportunity.toString());
			cellCategory.setTdsFieldNameValue(Long.toString(opportunity.getOpportunity()));
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(opportunity, EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.opportunity, fieldCheckType);

			cellCategory = new CellCategory();
			listOportunityPropertyCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumOpportunityPropertyFieldName.statusDate.toString());
			cellCategory.setTdsFieldNameValue(opportunity.getStatusDate().toString());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(opportunity, EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.statusDate, fieldCheckType);

			cellCategory = new CellCategory();
			listOportunityPropertyCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumOpportunityPropertyFieldName.dateCompleted.toString());
			cellCategory.setTdsFieldNameValue(opportunity.getDateCompleted());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.D);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(opportunity, EnumFieldCheckType.D, EnumOpportunityPropertyFieldName.dateCompleted, fieldCheckType);

			cellCategory = new CellCategory();
			listOportunityPropertyCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumOpportunityPropertyFieldName.pauseCount.toString());
			cellCategory.setTdsFieldNameValue(Long.toString(opportunity.getPauseCount()));
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(opportunity, EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.pauseCount, fieldCheckType);

			cellCategory = new CellCategory();
			listOportunityPropertyCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumOpportunityPropertyFieldName.itemCount.toString());
			cellCategory.setTdsFieldNameValue(Long.toString(opportunity.getItemCount()));
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(opportunity, EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.itemCount, fieldCheckType);
			
			cellCategory = new CellCategory();
			listOportunityPropertyCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumOpportunityPropertyFieldName.ftCount.toString());
			cellCategory.setTdsFieldNameValue(Long.toString(opportunity.getFtCount()));
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(opportunity, EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.ftCount, fieldCheckType);
			
			cellCategory = new CellCategory();
			listOportunityPropertyCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumOpportunityPropertyFieldName.abnormalStarts.toString());
			cellCategory.setTdsFieldNameValue(Long.toString(opportunity.getAbnormalStarts()));
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(opportunity, EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.abnormalStarts, fieldCheckType);
			
			cellCategory = new CellCategory();
			listOportunityPropertyCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumOpportunityPropertyFieldName.gracePeriodRestarts.toString());
			cellCategory.setTdsFieldNameValue(Long.toString(opportunity.getGracePeriodRestarts()));
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(opportunity, EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.gracePeriodRestarts, fieldCheckType);
			
			cellCategory = new CellCategory();
			listOportunityPropertyCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumOpportunityPropertyFieldName.taId.toString());
			cellCategory.setTdsFieldNameValue(opportunity.getTaId());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.D);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(opportunity, EnumFieldCheckType.D, EnumOpportunityPropertyFieldName.taId, fieldCheckType);

			cellCategory = new CellCategory();
			listOportunityPropertyCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumOpportunityPropertyFieldName.taName.toString());
			cellCategory.setTdsFieldNameValue(opportunity.getTaName());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.D);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(opportunity, EnumFieldCheckType.D, EnumOpportunityPropertyFieldName.taName, fieldCheckType);
			
			cellCategory = new CellCategory();
			listOportunityPropertyCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumOpportunityPropertyFieldName.sessionId.toString());
			cellCategory.setTdsFieldNameValue(opportunity.getSessionId());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.D);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(opportunity, EnumFieldCheckType.D, EnumOpportunityPropertyFieldName.sessionId, fieldCheckType);
			
			cellCategory = new CellCategory();
			listOportunityPropertyCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumOpportunityPropertyFieldName.windowId.toString());
			cellCategory.setTdsFieldNameValue(opportunity.getWindowId());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(opportunity, EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.windowId, fieldCheckType);
			
			cellCategory = new CellCategory();
			listOportunityPropertyCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumOpportunityPropertyFieldName.windowOpportunity.toString());
			cellCategory.setTdsFieldNameValue(opportunity.getWindowOpportunity());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.D);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(opportunity, EnumFieldCheckType.D, EnumOpportunityPropertyFieldName.windowOpportunity, fieldCheckType);
			
			cellCategory = new CellCategory();
			listOportunityPropertyCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumOpportunityPropertyFieldName.dateForceCompleted.toString());
			cellCategory.setTdsFieldNameValue(opportunity.getDateForceCompleted());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.D);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(opportunity, EnumFieldCheckType.D, EnumOpportunityPropertyFieldName.dateForceCompleted, fieldCheckType);
		
			cellCategory = new CellCategory();
			listOportunityPropertyCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumOpportunityPropertyFieldName.qaLevel.toString());
			cellCategory.setTdsFieldNameValue(opportunity.getQaLevel());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.D);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(opportunity, EnumFieldCheckType.D, EnumOpportunityPropertyFieldName.qaLevel, fieldCheckType);
			
			cellCategory = new CellCategory();
			listOportunityPropertyCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumOpportunityPropertyFieldName.assessmentParticipantSessionPlatformUserAgent.toString());
			cellCategory.setTdsFieldNameValue(opportunity.getAssessmentParticipantSessionPlatformUserAgent());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(opportunity, EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.assessmentParticipantSessionPlatformUserAgent, fieldCheckType);
			
			cellCategory = new CellCategory();
			listOportunityPropertyCategory.add(cellCategory);
			cellCategory.setTdsFieldName(EnumOpportunityPropertyFieldName.effectiveDate.toString());
			cellCategory.setTdsFieldNameValue(opportunity.getEffectiveDate());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			cellCategory.setFieldCheckType(fieldCheckType);
			validateField(opportunity, EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.effectiveDate, fieldCheckType);
			
			
		} catch (Exception e) {
			logger.error("analysis exception: ", e);
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
			logger.error("validateField exception: ", e);
		}
	}

	// Field Check Type (P) --> check that field is not empty, and field value is of correct data type
	// and within acceptable values
	private void checkP(Opportunity opportunity, EnumOpportunityPropertyFieldName enumFieldName, FieldCheckType fieldCheckType) {
		try {
			switch (enumFieldName) {
			case server:
				// <xs:attribute name="server" use="required" />
				processP_PritableASCIIone(opportunity.getServer(), fieldCheckType);
				break;
			case database:
				// <xs:attribute name="database" />
				processP_PritableASCIIzero(opportunity.getDatabase(), fieldCheckType);
				break;
			case clientName:
				//	<xs:attribute name="clientName" use="required" /> 
				processP_PritableASCIIone(opportunity.getClientName(), fieldCheckType);
				break;
			case key:
				// 	<xs:attribute name="key" use="required" />
				processP_PritableASCIIone(opportunity.getKey(), fieldCheckType);
				break;
			case oppId:
				//<xs:attribute name="oppId" use="required" />
				processP_Positive64bit(Long.valueOf(opportunity.getOppId()).longValue(), fieldCheckType);
				break;	
			case startDate:
				//	<xs:attribute name="startDate" type="NullableDateTime" />
				processP(opportunity.getStartDate(), fieldCheckType, true); //required Y
				break;
			case status:
				//<xs:attribute name="status" use="required">
				//<xs:simpleType>
				//<xs:restriction base="xs:token">
				//	<xs:enumeration value="appeal" />
				//	<xs:enumeration value="completed" />
				//	<xs:enumeration value="expired" />
				//	<xs:enumeration value="handscoring" />
				//	<xs:enumeration value="invalidated" />
				//	<xs:enumeration value="paused" />
				//	<xs:enumeration value="reported" />
				//	<xs:enumeration value="reset" />
				//	<xs:enumeration value="scored" />
				//	<xs:enumeration value="submitted" />
				//	<xs:enumeration value="pending" />
				//</xs:restriction>
				//</xs:simpleType>
				processP(opportunity.getStatus(), fieldCheckType, true); //required Y
				break;
			case opportunity:
				//<xs:attribute name="opportunity" use="required" type="xs:unsignedInt" />
				processP_Positive32bit(Long.toString(opportunity.getOpportunity()), fieldCheckType);
				break;
			case statusDate:
				//	<xs:attribute name="statusDate" use="required" type="xs:dateTime" />
				processP(opportunity.getStatusDate().toString(), fieldCheckType, true); //required Y
				break;
			case pauseCount:
				//	<xs:attribute name="pauseCount" use="required" type="xs:unsignedInt" />
				processP(Long.toString(opportunity.getPauseCount()), fieldCheckType, true); //required Y
				break;
			case itemCount:
				//	<xs:attribute name="itemCount" use="required" type="xs:unsignedInt" />
				processP(Long.toString(opportunity.getItemCount()), fieldCheckType, true); //required Y
				break;
			case ftCount:
				//	<xs:attribute name="ftCount" use="required" type="xs:unsignedInt" />
				processP(Long.toString(opportunity.getFtCount()), fieldCheckType, true); //required Y
				break;
			case abnormalStarts:	
				//	<xs:attribute name="abnormalStarts" use="required" type="xs:unsignedInt" />
				processP(Long.toString(opportunity.getAbnormalStarts()), fieldCheckType, true); //required Y
				break;
			case gracePeriodRestarts:	
				//	<xs:attribute name="gracePeriodRestarts" use="required" type="xs:unsignedInt" />
				processP(Long.toString(opportunity.getGracePeriodRestarts()), fieldCheckType, true); //required Y
				break;
			case windowId:
				//	<xs:attribute name="windowId" use="required" />
				processP_PritableASCIIone(opportunity.getWindowId(), fieldCheckType);
				break;
			case assessmentParticipantSessionPlatformUserAgent:
				// <xs:attribute name="assessmentParticipantSessionPlatformUserAgent" />
				processP(opportunity.getAssessmentParticipantSessionPlatformUserAgent(), fieldCheckType, true); //required Y
				break;
			case effectiveDate:
				//<xs:attribute name="effectiveDate" />
				//YYYY-MM-DD, where 2000 <= YYYY <= 9999 01 <= MM <= 12 01 <= DD <= 31
				processDate(opportunity.getEffectiveDate(), fieldCheckType); //required Y
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("checkP exception: ", e);
		}
	}

}
