package org.cresst.sb.irp.analysis.engine;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.OpportunityCategory;
import org.cresst.sb.irp.domain.analysis.SegmentCategory;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.Segment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SegmentAnalysisAction extends AnalysisAction<Segment, SegmentAnalysisAction.EnumSegmentFieldName, Object> {
    private static Logger logger = Logger.getLogger(SegmentAnalysisAction.class);

    static public enum EnumSegmentFieldName {
        id, position, formId, formKey, algorithm, algorithmVersion
    }

    @Override
    public void analyze(IndividualResponse individualResponse) {
        try {
            TDSReport tdsReport = individualResponse.getTDSReport();

            Opportunity opportunity = tdsReport.getOpportunity();
            List<Segment> listSegment = opportunity.getSegment();

            List<SegmentCategory> listSegmentCategory = new ArrayList<>();
            for (Segment segment : listSegment) {
                SegmentCategory segmentCategory = new SegmentCategory();
                listSegmentCategory.add(segmentCategory);
                analysisEachSegment(segmentCategory, segment);
            }

            OpportunityCategory opportunityCategory = individualResponse.getOpportunityCategory();
            opportunityCategory.setSegmentCategories(listSegmentCategory);
        } catch (Exception e) {
            logger.error("Analyze exception", e);
        }
    }

    private void analysisEachSegment(SegmentCategory segmentCategory, Segment segment) {
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
