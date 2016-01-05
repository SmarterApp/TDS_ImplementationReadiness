package org.cresst.sb.irp.analysis.engine;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.math.NumberUtils;
import org.cresst.sb.irp.domain.analysis.CellCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.ScoreCategory;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.testscoring.ITestScorer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Analyzes how the test score in the TDSReport
 */
@Service
public class TestScoreAnalysisAction extends AnalysisAction<TDSReport.Opportunity.Score, TestScoreAnalysisAction.EnumScoreFieldName, Object> {
    private final static Logger logger = LoggerFactory.getLogger(TestScoreAnalysisAction.class);

    public enum EnumScoreFieldName {
        measureOf, measureLabel, value, standardError
    }

    @Autowired
    private ITestScorer tisService;

    /**
     * Analyze Scores in the TDS Report
     *
     * @param individualResponse Where the TDS Report is obtained and where the result is stored
     */
    @Override
    public void analyze(IndividualResponse individualResponse) {
        try {
            List<TDSReport.Opportunity.Score> scores = individualResponse.getTDSReport().getOpportunity().getScore();

            if (scores.size() > 0) {

                // Analyze all TDSReport.Opportunity.Scores first
                List<ScoreCategory> scoreCategories = new ArrayList<>();
                individualResponse.getOpportunityCategory().setScoreCategories(scoreCategories);

                individualResponse.setHasValidScoring(true);

                for (TDSReport.Opportunity.Score score : scores) {
                    ScoreCategory scoreCategory = new ScoreCategory();
                    scoreCategories.add(scoreCategory);

                    validate(scoreCategory, score, score.getMeasureOf(), FieldCheckType.EnumFieldCheckType.P, EnumScoreFieldName.measureOf, null);
                    validate(scoreCategory, score, score.getMeasureLabel(), FieldCheckType.EnumFieldCheckType.P, EnumScoreFieldName.measureLabel, null);
                    validate(scoreCategory, score, score.getValue(), FieldCheckType.EnumFieldCheckType.P, EnumScoreFieldName.value, null);
                    validate(scoreCategory, score, score.getStandardError(), FieldCheckType.EnumFieldCheckType.P, EnumScoreFieldName.standardError, null);

                    for (CellCategory cellCategory : scoreCategory.getCellCategories()) {
                        if (!cellCategory.getFieldCheckType().isAcceptableValue()) {
                            individualResponse.setHasValidScoring(false);
                        }
                    }
                }

                if (individualResponse.hasValidScoring()) {
                    // If all of them are valid then
                    // Construct a TDSReport without scoring information
                    TDSReport tdsReport = individualResponse.getTDSReport();
                    List<TDSReport.Opportunity.Score> copyOfScores = copyScores(tdsReport.getOpportunity().getScore());
                    tdsReport.getOpportunity().getScore().clear();

                    // Submit the TDSReport without scoring information to TIS and receive the response
                    TDSReport scoredTDSReport = tisService.scoreTDSReport(tdsReport);

                    // Put the scores back
                    tdsReport.getOpportunity().getScore().addAll(copyOfScores);

                    // Verify TIS scores matches TDSReport scores
                    if (scoresMatch(tdsReport.getOpportunity().getScore(), scoredTDSReport.getOpportunity().getScore())) {

                    }
                }
            }
        } catch (Exception e) {
            logger.error("Test Score Analysis Exception", e);
        }
    }

    /**
     * Verifies the scores from the TDS Report match the scores calculated by TIS
     *
     * @param tdsReportScores Scores from the TDS Report
     * @param irpScoredScores Scores from the TIS
     * @return True if the scores match; false otherwise
     */
    private boolean scoresMatch(List<TDSReport.Opportunity.Score> tdsReportScores, List<TDSReport.Opportunity.Score> irpScoredScores) {
        Map<String, TDSReport.Opportunity.Score> irpScoresMap = new HashMap<>();
        for (TDSReport.Opportunity.Score score : irpScoredScores) {
            irpScoresMap.put(score.getMeasureOf() + score.getMeasureLabel(), score);
        }

        for (TDSReport.Opportunity.Score score : tdsReportScores) {
            TDSReport.Opportunity.Score irpScore = irpScoresMap.get(score.getMeasureOf() + score.getMeasureLabel());
            if (irpScore != null) {
                if (!score.getValue().equals(irpScore.getValue()) &&
                        !score.getStandardError().equals(irpScore.getStandardError())) {
                    return false;
                }
            }
        }

        return true;
    }

    private List<TDSReport.Opportunity.Score> copyScores(List<TDSReport.Opportunity.Score> originalScores) {
        List<TDSReport.Opportunity.Score> copyOfScores = new ArrayList<>();

        for (TDSReport.Opportunity.Score score : originalScores) {
            TDSReport.Opportunity.Score scoreCopy = new TDSReport.Opportunity.Score();
            copyOfScores.add(scoreCopy);

            scoreCopy.setMeasureLabel(score.getMeasureLabel());
            scoreCopy.setMeasureOf(score.getMeasureOf());
            scoreCopy.setStandardError(score.getStandardError());
            scoreCopy.setValue(score.getValue());
        }

        return copyOfScores;
    }

    /**
     * Field Check Type (P) --> check that field is not empty and field value is of correct data type and within acceptable
     * values
     *
     * @param score          Score object with fields to check
     * @param enumFieldName  Specifies the field to check
     * @param fieldCheckType Stores the analysis data for the field being analyzed
     */
    @Override
    protected void checkP(TDSReport.Opportunity.Score score, EnumScoreFieldName enumFieldName, FieldCheckType fieldCheckType) {
        switch (enumFieldName) {
            case measureOf:
                processP_PritableASCIIone(score.getMeasureOf(), fieldCheckType);
                break;
            case measureLabel:
                processP_PritableASCIIone(score.getMeasureLabel(), fieldCheckType);
                break;
            case value:
                processP_PritableASCIIone(score.getValue(), fieldCheckType);
                break;
            case standardError:
                processP_FloatAllowNulls(score.getStandardError(), fieldCheckType);
                break;
            default:
                break;
        }
    }

    /**
     * Checks if the field has the correct value
     *
     * @param score          Score object with field to check
     * @param enumFieldName  Specifies the field to check
     * @param fieldCheckType
     * @param comparisonData
     */
    @Override
    protected void checkC(TDSReport.Opportunity.Score score, EnumScoreFieldName enumFieldName, FieldCheckType fieldCheckType, Object comparisonData) {

    }
}
