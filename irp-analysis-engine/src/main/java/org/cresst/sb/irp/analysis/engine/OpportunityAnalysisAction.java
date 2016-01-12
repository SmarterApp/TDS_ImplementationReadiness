package org.cresst.sb.irp.analysis.engine;

import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.OpportunityCategory;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OpportunityAnalysisAction extends AnalysisAction<Opportunity, OpportunityAnalysisAction.EnumOpportunityPropertyFieldName, Object> {
    private final static Logger logger = LoggerFactory.getLogger(OpportunityAnalysisAction.class);

    static public enum EnumOpportunityPropertyFieldName {
        server, database, clientName, key, oppId, startDate, status, opportunity, statusDate, dateCompleted, pauseCount, itemCount, ftCount, abnormalStarts, gracePeriodRestarts, taId, taName, sessionId, windowId, windowOpportunity, dateForceCompleted, qaLevel, assessmentParticipantSessionPlatformUserAgent, effectiveDate
    }

    @Override
    public void analyze(IndividualResponse individualResponse) {
        TDSReport tdsReport = individualResponse.getTDSReport();
        Opportunity opportunity = tdsReport.getOpportunity(); //<xs:element name="Opportunity" minOccurs="1" maxOccurs="1">

        OpportunityCategory opportunityCategory = new OpportunityCategory();
        individualResponse.setOpportunityCategory(opportunityCategory);

        validate(opportunityCategory, opportunity, opportunity.getServer(), EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.server, null);
        validate(opportunityCategory, opportunity, opportunity.getDatabase(), EnumFieldCheckType.D, EnumOpportunityPropertyFieldName.database, null);
        validate(opportunityCategory, opportunity, opportunity.getKey(), EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.key, null);
        validate(opportunityCategory, opportunity, opportunity.getOppId(), EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.oppId, null);
        validate(opportunityCategory, opportunity, opportunity.getStartDate(), EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.startDate, null);
        validate(opportunityCategory, opportunity, opportunity.getStatus(), EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.status, null);
        validate(opportunityCategory, opportunity, opportunity.getOpportunity(), EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.opportunity, null);
        validate(opportunityCategory, opportunity, opportunity.getStatusDate(), EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.statusDate, null);
        validate(opportunityCategory, opportunity, opportunity.getDateCompleted(), EnumFieldCheckType.D, EnumOpportunityPropertyFieldName.dateCompleted, null);
        validate(opportunityCategory, opportunity, opportunity.getPauseCount(), EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.pauseCount, null);
        validate(opportunityCategory, opportunity, opportunity.getItemCount(), EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.itemCount, null);
        validate(opportunityCategory, opportunity, opportunity.getFtCount(), EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.ftCount, null);
        validate(opportunityCategory, opportunity, opportunity.getAbnormalStarts(), EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.abnormalStarts, null);
        validate(opportunityCategory, opportunity, opportunity.getGracePeriodRestarts(), EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.gracePeriodRestarts, null);
        validate(opportunityCategory, opportunity, opportunity.getTaId(), EnumFieldCheckType.D, EnumOpportunityPropertyFieldName.taId, null);
        validate(opportunityCategory, opportunity, opportunity.getTaName(), EnumFieldCheckType.D, EnumOpportunityPropertyFieldName.taName, null);
        validate(opportunityCategory, opportunity, opportunity.getSessionId(), EnumFieldCheckType.D, EnumOpportunityPropertyFieldName.sessionId, null);
        validate(opportunityCategory, opportunity, opportunity.getWindowId(), EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.windowId, null);
        validate(opportunityCategory, opportunity, opportunity.getWindowOpportunity(), EnumFieldCheckType.D, EnumOpportunityPropertyFieldName.windowOpportunity, null);
        validate(opportunityCategory, opportunity, opportunity.getDateForceCompleted(), EnumFieldCheckType.D, EnumOpportunityPropertyFieldName.dateForceCompleted, null);
        validate(opportunityCategory, opportunity, opportunity.getClientName(), EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.clientName, null);
        validate(opportunityCategory, opportunity, opportunity.getAssessmentParticipantSessionPlatformUserAgent(), EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.assessmentParticipantSessionPlatformUserAgent, null);
        if ( opportunity.getEffectiveDate() != null)
        	validate(opportunityCategory, opportunity, opportunity.getEffectiveDate(), EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.effectiveDate, null);
        else
        	fieldNameNotExist(opportunityCategory, EnumOpportunityPropertyFieldName.effectiveDate, EnumFieldCheckType.P);
        validate(opportunityCategory, opportunity, opportunity.getQaLevel(), EnumFieldCheckType.D, EnumOpportunityPropertyFieldName.qaLevel, null);
    
    }
    
    /**
     * Field Check Type (P) --> check that field is not empty, and field value is of correct data type
     * and within acceptable values
     *
     * @param opportunity    Opportunity with fields to check
     * @param enumFieldName  Specifies the field to check
     * @param fieldCheckType This is where the results are stored
     */
    @Override
    protected void checkP(Opportunity opportunity, EnumOpportunityPropertyFieldName enumFieldName, FieldCheckType fieldCheckType) {
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
                    processP_Positive64bit(Long.valueOf(opportunity.getOppId()), fieldCheckType);
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

    /**
     * Checks if the field has correct value
     *
     * @param checkObj       Object with fields to check
     * @param enumFieldName  Specifies the field to check
     * @param fieldCheckType This is where the results are stored
     * @param unused         Unused parameter
     */
    @Override
    protected void checkC(Opportunity checkObj, EnumOpportunityPropertyFieldName enumFieldName, FieldCheckType fieldCheckType, Object unused) {
    }
    
    /**
     * To avoid NullPointerException for tds fieldName not existed. for those xml attribute element not required only
     * @param opportunityCategory
     * @param enumOpportunityPropertyFieldName
     * @param enumFieldCheckType
     */
    private void fieldNameNotExist(OpportunityCategory opportunityCategory, EnumOpportunityPropertyFieldName enumOpportunityPropertyFieldName, EnumFieldCheckType enumFieldCheckType){
  	  final FieldCheckType fieldCheckType = new FieldCheckType();
        fieldCheckType.setEnumfieldCheckType(enumFieldCheckType);
        fieldCheckType.setFieldEmpty(true);
        fieldCheckType.setCorrectDataType(false);

        CellCategory cellCategory = new CellCategory();
        cellCategory.setTdsFieldName(enumOpportunityPropertyFieldName.name());
        cellCategory.setTdsFieldNameValue("");
        cellCategory.setFieldCheckType(fieldCheckType);
        
        opportunityCategory.addCellCategory(cellCategory);
  }
}
