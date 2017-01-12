package org.cresst.sb.irp.analysis.engine;

import org.apache.commons.lang3.StringUtils;
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

    public enum EnumOpportunityPropertyFieldName {
        server(128, false),
        database(128, false),
        key(36),
        oppId(16),
        startDate(23),
        status(50, false),
        validity(7),
        completeness(8),
        opportunity(8, false),
        statusDate(23, false),
        dateCompleted(23),
        pauseCount(8, false),
        itemCount(8),
        ftCount(8, false),
        abnormalStarts(8, false),
        gracePeriodRestarts(8, false),
        TAID(128, false),
        TAName(128, false),
        sessionId(128, false),
        windowId(50),
        windowOpportunity(8, false),
        completeStatus(8),
        administrationCondition(20),
        dateForceCompleted(8, false),
        clientName(255, false),
        assessmentParticipantSessionPlatformUserAgent(512),
        effectiveDate(10),
        testReason(255, false);

        private int maxWidth;
        private boolean isRequired;

        EnumOpportunityPropertyFieldName(int maxWidth) {
            this.maxWidth = maxWidth;
            this.isRequired = true;
        }

        EnumOpportunityPropertyFieldName(int maxWidth, boolean isRequired) {
            this.maxWidth = maxWidth;
            this.isRequired = isRequired;
        }

        public int getMaxWidth() {
            return maxWidth;
        }

        public boolean isRequired() {
            return isRequired;
        }
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
        validate(opportunityCategory, opportunity, opportunity.getValidity(), EnumFieldCheckType.D, EnumOpportunityPropertyFieldName.validity, null);
        validate(opportunityCategory, opportunity, opportunity.getCompleteness(), EnumFieldCheckType.D, EnumOpportunityPropertyFieldName.completeness, null);
        validate(opportunityCategory, opportunity, opportunity.getOpportunity(), EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.opportunity, null);
        validate(opportunityCategory, opportunity, opportunity.getStatusDate(), EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.statusDate, null);
        validate(opportunityCategory, opportunity, opportunity.getDateCompleted(), EnumFieldCheckType.D, EnumOpportunityPropertyFieldName.dateCompleted, null);
        validate(opportunityCategory, opportunity, opportunity.getPauseCount(), EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.pauseCount, null);
        validate(opportunityCategory, opportunity, opportunity.getItemCount(), EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.itemCount, null);
        validate(opportunityCategory, opportunity, opportunity.getFtCount(), EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.ftCount, null);
        validate(opportunityCategory, opportunity, opportunity.getAbnormalStarts(), EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.abnormalStarts, null);
        validate(opportunityCategory, opportunity, opportunity.getGracePeriodRestarts(), EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.gracePeriodRestarts, null);
        validate(opportunityCategory, opportunity, opportunity.getTaId(), EnumFieldCheckType.D, EnumOpportunityPropertyFieldName.TAID, null);
        validate(opportunityCategory, opportunity, opportunity.getTaName(), EnumFieldCheckType.D, EnumOpportunityPropertyFieldName.TAName, null);
        validate(opportunityCategory, opportunity, opportunity.getSessionId(), EnumFieldCheckType.D, EnumOpportunityPropertyFieldName.sessionId, null);
        validate(opportunityCategory, opportunity, opportunity.getWindowId(), EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.windowId, null);
        validate(opportunityCategory, opportunity, opportunity.getWindowOpportunity(), EnumFieldCheckType.D, EnumOpportunityPropertyFieldName.windowOpportunity, null);
        validate(opportunityCategory, opportunity, opportunity.getWindowOpportunity(), EnumFieldCheckType.D, EnumOpportunityPropertyFieldName.completeStatus, null);
        validate(opportunityCategory, opportunity, opportunity.getWindowOpportunity(), EnumFieldCheckType.D, EnumOpportunityPropertyFieldName.administrationCondition, null);
        validate(opportunityCategory, opportunity, opportunity.getDateForceCompleted(), EnumFieldCheckType.D, EnumOpportunityPropertyFieldName.dateForceCompleted, null);
        validate(opportunityCategory, opportunity, opportunity.getClientName(), EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.clientName, null);
        validate(opportunityCategory, opportunity, opportunity.getAssessmentParticipantSessionPlatformUserAgent(), EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.assessmentParticipantSessionPlatformUserAgent, null);
        if (opportunity.getEffectiveDate() != null)
        	validate(opportunityCategory, opportunity, opportunity.getEffectiveDate(), EnumFieldCheckType.P, EnumOpportunityPropertyFieldName.effectiveDate, null);
        else
        	fieldNameNotExist(opportunityCategory, EnumOpportunityPropertyFieldName.effectiveDate, EnumFieldCheckType.P);
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
                    processP_PrintableASCIIoneMaxWidth(opportunity.getServer(), fieldCheckType, enumFieldName.getMaxWidth());
                    break;
                case database:
                    // <xs:attribute name="database" />
                    processP_PrintableASCIIzeroMaxWidth(opportunity.getDatabase(), fieldCheckType, enumFieldName.getMaxWidth());
                    break;
                case clientName:
                    //	<xs:attribute name="clientName" use="required" />
                    processP_PrintableASCIIoneMaxWidth(opportunity.getClientName(), fieldCheckType, enumFieldName.getMaxWidth());
                    break;
                case key:
                    // 	<xs:attribute name="key" use="required" />
                    processP_PrintableASCIIoneMaxWidth(opportunity.getKey(), fieldCheckType, enumFieldName.getMaxWidth());
                    fieldCheckType.setRequiredFieldMissing(enumFieldName.isRequired() && StringUtils.isBlank(opportunity.getKey()));
                    break;
                case oppId:
                    //<xs:attribute name="oppId" use="required" />
                    Long oppIdValue = Long.valueOf(opportunity.getOppId());
                    processP_Positive64bit(oppIdValue, fieldCheckType);
                    fieldCheckType.setCorrectWidth(oppIdValue != null);
                    fieldCheckType.setRequiredFieldMissing(enumFieldName.isRequired() && StringUtils.isBlank(opportunity.getOppId()));
                    break;
                case startDate:
                    //	<xs:attribute name="startDate" type="NullableDateTime" />
                    String startDate = opportunity.getStartDate();
                    processP(startDate, fieldCheckType, true); //required Y
                    fieldCheckType.setCorrectWidth(startDate == null || startDate.length() <= enumFieldName.getMaxWidth());
                    fieldCheckType.setRequiredFieldMissing(enumFieldName.isRequired() && StringUtils.isBlank(startDate));
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
                    String status = opportunity.getStatus();
                    processP(status, fieldCheckType, true); //required Y
                    fieldCheckType.setCorrectWidth(status != null && status.length() <= enumFieldName.getMaxWidth());
                    fieldCheckType.setRequiredFieldMissing(enumFieldName.isRequired() && StringUtils.isBlank(status));
                    break;
                case opportunity:
                    //<xs:attribute name="opportunity" use="required" type="xs:unsignedInt" />
                    processP_Positive32bitMaxWidth(Long.toString(opportunity.getOpportunity()), fieldCheckType, enumFieldName.getMaxWidth());
                    break;
                case statusDate:
                    //	<xs:attribute name="statusDate" use="required" type="xs:dateTime" />
                    processP(opportunity.getStatusDate().toString(), fieldCheckType, true); //required Y
                    fieldCheckType.setCorrectWidth(true);
                    break;
                case pauseCount:
                    //	<xs:attribute name="pauseCount" use="required" type="xs:unsignedInt" />
                    String pauseCount = Long.toString(opportunity.getPauseCount());
                    processP(pauseCount, fieldCheckType, true); //required Y
                    fieldCheckType.setCorrectWidth(pauseCount != null && pauseCount.length() <= enumFieldName.getMaxWidth());
                    fieldCheckType.setRequiredFieldMissing(enumFieldName.isRequired() && StringUtils.isBlank(pauseCount));
                    break;
                case itemCount:
                    //	<xs:attribute name="itemCount" use="required" type="xs:unsignedInt" />
                    String itemCount = Long.toString(opportunity.getItemCount());
                    processP(itemCount, fieldCheckType, true); //required Y
                    fieldCheckType.setCorrectWidth(itemCount != null && itemCount.length() <= enumFieldName.getMaxWidth());
                    fieldCheckType.setRequiredFieldMissing(enumFieldName.isRequired() && StringUtils.isBlank(itemCount));
                    break;
                case ftCount:
                    //	<xs:attribute name="ftCount" use="required" type="xs:unsignedInt" />
                    String ftCount = Long.toString(opportunity.getFtCount());
                    processP(ftCount, fieldCheckType, true); //required Y
                    fieldCheckType.setCorrectWidth(ftCount != null && ftCount.length() <= enumFieldName.getMaxWidth());
                    fieldCheckType.setRequiredFieldMissing(enumFieldName.isRequired() && StringUtils.isBlank(ftCount));
                    break;
                case abnormalStarts:
                    //	<xs:attribute name="abnormalStarts" use="required" type="xs:unsignedInt" />
                    String abnormalStarts = Long.toString(opportunity.getAbnormalStarts());
                    processP(abnormalStarts, fieldCheckType, true); //required Y
                    fieldCheckType.setCorrectWidth(abnormalStarts != null && abnormalStarts.length() <= enumFieldName.getMaxWidth());
                    fieldCheckType.setRequiredFieldMissing(enumFieldName.isRequired() && StringUtils.isBlank(abnormalStarts));
                    break;
                case gracePeriodRestarts:
                    //	<xs:attribute name="gracePeriodRestarts" use="required" type="xs:unsignedInt" />
                    String gracePeriodRestartds = Long.toString(opportunity.getGracePeriodRestarts());
                    processP(gracePeriodRestartds, fieldCheckType, true); //required Y
                    fieldCheckType.setCorrectWidth(gracePeriodRestartds != null && gracePeriodRestartds.length() <= enumFieldName.getMaxWidth());
                    fieldCheckType.setRequiredFieldMissing(enumFieldName.isRequired() && StringUtils.isBlank(gracePeriodRestartds));
                    break;
                case windowId:
                    //	<xs:attribute name="windowId" use="required" />
                    processP_PrintableASCIIoneMaxWidth(opportunity.getWindowId(), fieldCheckType, enumFieldName.getMaxWidth());
                    fieldCheckType.setRequiredFieldMissing(enumFieldName.isRequired() && StringUtils.isBlank(opportunity.getWindowId()));
                    break;
                case assessmentParticipantSessionPlatformUserAgent:
                    // <xs:attribute name="assessmentParticipantSessionPlatformUserAgent" />
                    String assessmentParticipantSessionPlatformUserAgent = opportunity.getAssessmentParticipantSessionPlatformUserAgent();
                    processP(assessmentParticipantSessionPlatformUserAgent, fieldCheckType, true); //required Y
                    fieldCheckType.setCorrectWidth(assessmentParticipantSessionPlatformUserAgent != null && assessmentParticipantSessionPlatformUserAgent.length() <= enumFieldName.getMaxWidth());
                    fieldCheckType.setRequiredFieldMissing(enumFieldName.isRequired() && StringUtils.isBlank(assessmentParticipantSessionPlatformUserAgent));
                    break;
                case effectiveDate:
                    //<xs:attribute name="effectiveDate" />
                    //YYYY-MM-DD, where 2000 <= YYYY <= 9999 01 <= MM <= 12 01 <= DD <= 31
                    processDate(opportunity.getEffectiveDate(), fieldCheckType); //required Y
                    fieldCheckType.setRequiredFieldMissing(enumFieldName.isRequired() && StringUtils.isBlank(opportunity.getEffectiveDate()));
                    break;
                default:
                    break;
            }

            fieldCheckType.setOptionalValue(!enumFieldName.isRequired());
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
        fieldCheckType.setFieldValueEmpty(true);
        fieldCheckType.setCorrectDataType(false);
        fieldCheckType.setRequiredFieldMissing(enumOpportunityPropertyFieldName.isRequired());

        CellCategory cellCategory = new CellCategory();
        cellCategory.setTdsFieldName(enumOpportunityPropertyFieldName.name());
        cellCategory.setTdsFieldNameValue("");
        cellCategory.setFieldCheckType(fieldCheckType);
        
        opportunityCategory.addCellCategory(cellCategory);
  }
}
