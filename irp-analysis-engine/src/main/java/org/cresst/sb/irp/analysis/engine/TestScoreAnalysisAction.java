package org.cresst.sb.irp.analysis.engine;

import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.ScoreCategory;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Analyzes how the test score in the TDSReport
 */
@Service
public class TestScoreAnalysisAction extends AnalysisAction<TDSReport.Opportunity.Score, TestScoreAnalysisAction.EnumScoreFieldName, Object> {
    private final static Logger logger = LoggerFactory.getLogger(TestScoreAnalysisAction.class);

    public enum EnumScoreFieldName {
        measureOf, measureLabel, value, standardError
    }

    /**
     * Analyze Scores in the TDS Report
     *
     * @param individualResponse Where the TDS Report is obtained and where the result is stored
     */
    @Override
    public void analyze(IndividualResponse individualResponse) {
        try {
            List<ScoreCategory> scoreCategories = individualResponse.getOpportunityCategory().getScoreCategories();

            List<TDSReport.Opportunity.Score> scores = individualResponse.getTDSReport().getOpportunity().getScore();

            if (scores.size() > 0) {

                // Analyze all TDSReport.Opportunity.Scores first

                // If all of them are valid then
                // Construct a TDSReport without scoring information
                // Submit the TDSReport without scoring information to TIS
                // Receive the response
                // Verify TIS scores matches TDSReport scores
            }
        } catch (Exception e) {
            logger.error("Test Score Analysis Exception", e);
        }
    }

    /**
     * Field Check Type (P) --> check that field is not empty, and field value is of correct data type and within acceptable
     * values
     *
     * @param checkObj       Object with fields to check
     * @param enumFieldName  Specifies the field to check
     * @param fieldCheckType
     */
    @Override
    protected void checkP(TDSReport.Opportunity.Score checkObj, EnumScoreFieldName enumFieldName, FieldCheckType fieldCheckType) {

    }

    /**
     * Checks if the field has the correct value
     *
     * @param checkObj       Object with field to check
     * @param enumFieldName  Specifies the field to check
     * @param fieldCheckType
     * @param comparisonData
     */
    @Override
    protected void checkC(TDSReport.Opportunity.Score checkObj, EnumScoreFieldName enumFieldName, FieldCheckType fieldCheckType, Object comparisonData) {

    }
}
