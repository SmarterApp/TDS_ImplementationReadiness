package org.cresst.sb.irp.analysis.engine;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.OpportunityCategory;
import org.cresst.sb.irp.domain.analysis.SegmentCategory;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Segment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SegmentAnalysisAction extends AnalysisAction<Segment, SegmentAnalysisAction.EnumSegmentFieldName, Object> {
    private final static Logger logger = LoggerFactory.getLogger(SegmentAnalysisAction.class);

    public enum EnumSegmentFieldName {
        id(250, false),
        position(8, false),
        formKey(100, false),
        formId(150, false),
        algorithm(50, false),
        algorithmVersion(50, false);

        private int maxWidth;
        private boolean isRequired;

        EnumSegmentFieldName(int maxWidth) {
            this.maxWidth = maxWidth;
            this.isRequired = true;
        }

        EnumSegmentFieldName(int maxWidth, boolean isRequired) {
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
        try {
            TDSReport tdsReport = individualResponse.getTDSReport();

            Opportunity opportunity = tdsReport.getOpportunity();
            List<Segment> tdsSegments = opportunity.getSegment(); //<xs:element name="Segment" minOccurs="0" maxOccurs="unbounded">

            List<SegmentCategory> segmentCategories = new ArrayList<>();
            for (Segment segment : tdsSegments) {
                SegmentCategory segmentCategory = new SegmentCategory();
                segmentCategories.add(segmentCategory);
                analyzeSegment(segmentCategory, segment);
            }

            OpportunityCategory opportunityCategory = individualResponse.getOpportunityCategory();
            opportunityCategory.setSegmentCategories(segmentCategories);
        } catch (Exception e) {
            logger.error("Analyze exception", e);
        }
    }

    private void analyzeSegment(SegmentCategory segmentCategory, Segment segment) {
        validate(segmentCategory, segment, segment.getId(), EnumFieldCheckType.P, EnumSegmentFieldName.id, null);
        validate(segmentCategory, segment, segment.getPosition(), EnumFieldCheckType.P, EnumSegmentFieldName.position, null);
        validate(segmentCategory, segment, segment.getFormId(), EnumFieldCheckType.D, EnumSegmentFieldName.formId, null);
        validate(segmentCategory, segment, segment.getFormKey(), EnumFieldCheckType.D, EnumSegmentFieldName.formKey, null);
        validate(segmentCategory, segment, segment.getAlgorithm(), EnumFieldCheckType.P, EnumSegmentFieldName.algorithm, null);
        validate(segmentCategory, segment, segment.getAlgorithmVersion(), EnumFieldCheckType.P, EnumSegmentFieldName.algorithmVersion, null);
    }

    /**
     * Field Check Type (P) --> check that field is not empty, and field value is of correct data type
     * and within acceptable values
     *
     * @param segment        Object with fields to check
     * @param enumFieldName  Specifies the field to check
     * @param fieldCheckType This is where the results are stored
     */
    @Override
    protected void checkP(Segment segment, EnumSegmentFieldName enumFieldName, FieldCheckType fieldCheckType) {
        try {
            switch (enumFieldName) {
                case id:
                    //	<xs:attribute name="id" use="required" />
                    processP_PrintableASCIIoneMaxWidth(segment.getId(), fieldCheckType, enumFieldName.getMaxWidth());
                    fieldCheckType.setRequiredFieldMissing(enumFieldName.isRequired() && StringUtils.isBlank(segment.getId()));
                    break;
                case position:
                    //	<xs:attribute name="position" use="required">
                    //<xs:simpleType>
                    //<xs:restriction base="xs:unsignedByte">
                    //	<xs:minInclusive value="1" />
                    //</xs:restriction>
                    //</xs:simpleType>
                    //</xs:attribute>
                    processP_Positive32bitMaxWidth(Short.toString(segment.getPosition()), fieldCheckType, enumFieldName.getMaxWidth());
                    fieldCheckType.setRequiredFieldMissing(enumFieldName.isRequired() && StringUtils.isBlank(Short.toString(segment.getPosition())));
                    break;
                case formKey:
                    break;
                case formId:
                    break;
                case algorithm:
                    //<xs:attribute name="algorithm" use="required" />
                    processP_PrintableASCIIoneMaxWidth(segment.getAlgorithm(), fieldCheckType, enumFieldName.getMaxWidth());
                    fieldCheckType.setRequiredFieldMissing(enumFieldName.isRequired() && StringUtils.isBlank(segment.getAlgorithm()));
                    break;
                case algorithmVersion:
                    //   <xs:attribute name="algorithmVersion" />
                    processP_PrintableASCIIoneMaxWidth(segment.getAlgorithmVersion(), fieldCheckType, enumFieldName.getMaxWidth());
                    fieldCheckType.setRequiredFieldMissing(enumFieldName.isRequired() && StringUtils.isBlank(segment.getAlgorithmVersion()));
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
     * Noop for now
     *
     * @param segment        Object with field to check
     * @param enumFieldName  Specifies the field to check
     * @param fieldCheckType This is where the results are stored
     * @param unused         Unused parameter
     */
    @Override
    protected void checkC(Segment segment, EnumSegmentFieldName enumFieldName, FieldCheckType fieldCheckType, Object unused) {

    }
}
