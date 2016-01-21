package org.cresst.sb.irp.analysis.engine;

import builders.CellCategoryBuilder;
import builders.ScoreBuilder;
import builders.TdsReportScoreIrpScoredScoreBuilder;

import org.cresst.sb.irp.domain.analysis.*;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.testscoring.ITestScorer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Verifies Test Scoring analysis
 */
@RunWith(MockitoJUnitRunner.class)
public class TestScoreAnalysisActionTest {

    @Mock
    private ITestScorer testScorer;

    @InjectMocks
    TestScoreAnalysisAction underTest = new TestScoreAnalysisAction();

    @Test
    public void whenTDSReport_DoesNotHave_Scoring_InvalidTestScoring() {

        final TDSReport.Opportunity opportunity = new TDSReport.Opportunity();
        final TDSReport tdsReport = new TDSReport();
        tdsReport.setOpportunity(opportunity);

        final IndividualResponse individualResponse = new IndividualResponse();
        individualResponse.setTDSReport(tdsReport);

        underTest.analyze(individualResponse);

        assertFalse(individualResponse.hasValidScoring());
    }

    @Test
    public void whenTDSReport_HasValid_Scoring_ValidTestScoring_AndCellCategoryHasPassingValues() {
        final List<TDSReport.Opportunity.Score> scores = new ArrayList<>();
        scores.add(new ScoreBuilder().measureOf("Overall").measureLabel("Attempted").value("Y").standardError("").toScore());
        scores.add(new ScoreBuilder().measureOf("Overall").measureLabel("ThetaScore").value("-0.14785630410943").standardError("0.457791032400169").toScore());
        scores.add(new ScoreBuilder().measureOf("1").measureLabel("ThetaScore").value("1.3335").standardError("0.831048294559457").toScore());
        scores.add(new ScoreBuilder().measureOf("SOCK_2").measureLabel("ThetaScore").value("-1.2370123601497").standardError("0.608838178456256").toScore());

        final TDSReport.Opportunity opportunity = new TDSReport.Opportunity();
        opportunity.getScore().addAll(scores);

        final TDSReport tdsReport = new TDSReport();
        tdsReport.setOpportunity(opportunity);

        final IndividualResponse individualResponse = new IndividualResponse();
        individualResponse.setTDSReport(tdsReport);
        individualResponse.setOpportunityCategory(new OpportunityCategory());

        final TDSReport mockTDSReport = new TDSReport();
        when(testScorer.scoreTDSReport(tdsReport)).thenReturn(mockTDSReport);

        underTest.analyze(individualResponse);

        List<ScoreCategory> scoreCategories = new ArrayList<>();
        ScoreCategory scoreCategory = new ScoreCategory();
        scoreCategories.add(scoreCategory);
        for (CellCategory cellCategory : createValidCellCategories("Overall", "Attempted", "Y", "")) {
            scoreCategory.addCellCategory(cellCategory);
        }

        scoreCategory = new ScoreCategory();
        scoreCategories.add(scoreCategory);
        for (CellCategory cellCategory : createValidCellCategories("Overall", "ThetaScore", "-0.14785630410943", "0.457791032400169")) {
            scoreCategory.addCellCategory(cellCategory);
        }

        scoreCategory = new ScoreCategory();
        scoreCategories.add(scoreCategory);
        for (CellCategory cellCategory : createValidCellCategories("1", "ThetaScore", "1.3335", "0.831048294559457")) {
            scoreCategory.addCellCategory(cellCategory);
        }

        scoreCategory = new ScoreCategory();
        scoreCategories.add(scoreCategory);
        for (CellCategory cellCategory : createValidCellCategories("SOCK_2", "ThetaScore", "-1.2370123601497", "0.608838178456256")) {
            scoreCategory.addCellCategory(cellCategory);
        }

        assertTrue(individualResponse.hasValidScoring());
        assertArrayEquals(scoreCategories.toArray(), individualResponse.getOpportunityCategory().getScoreCategories().toArray());
    }

    @Test
    public void whenTDSReport_HasInvalid_Scoring_InvalidTestScoring_AndCellCategoryHasFailingValues() {
        final List<TDSReport.Opportunity.Score> scores = new ArrayList<>();
        scores.add(new ScoreBuilder().measureOf("Overall").measureLabel("ThetaScore").value("").standardError("fail").toScore());

        final TDSReport.Opportunity opportunity = new TDSReport.Opportunity();
        opportunity.getScore().addAll(scores);

        final TDSReport tdsReport = new TDSReport();
        tdsReport.setOpportunity(opportunity);

        final IndividualResponse individualResponse = new IndividualResponse();
        individualResponse.setTDSReport(tdsReport);
        individualResponse.setOpportunityCategory(new OpportunityCategory());

        underTest.analyze(individualResponse);

        List<ScoreCategory> scoreCategories = new ArrayList<>();
        ScoreCategory scoreCategory = new ScoreCategory();
        scoreCategories.add(scoreCategory);
        for (CellCategory cellCategory : createInvalidCellCategories()) {
            scoreCategory.addCellCategory(cellCategory);
        }

        assertFalse(individualResponse.hasValidScoring());
        assertArrayEquals(scoreCategories.toArray(), individualResponse.getOpportunityCategory().getScoreCategories().toArray());
    }

    @Test
    public void whenTDSReport_HasValid_Scoring_And_ScoresMatch_CellCategoryHasPassingValue_Match() {
        final List<TDSReport.Opportunity.Score> scores = new ArrayList<>();
        scores.add(new ScoreBuilder().measureOf("Overall").measureLabel("Attempted").value("Y").standardError("1.234").toScore());

        final TDSReport.Opportunity opportunity = new TDSReport.Opportunity();
        opportunity.getScore().addAll(scores);

        final TDSReport tdsReport = new TDSReport();
        tdsReport.setOpportunity(opportunity);

        final IndividualResponse individualResponse = new IndividualResponse();
        individualResponse.setTDSReport(tdsReport);
        individualResponse.setOpportunityCategory(new OpportunityCategory());

        // Create TDSReport test double
        final List<TDSReport.Opportunity.Score> mockScores = new ArrayList<>();
        mockScores.add(new ScoreBuilder().measureOf("Overall").measureLabel("Attempted").value("Y").standardError("1.234").toScore());

        final TDSReport.Opportunity mockOpportunity = new TDSReport.Opportunity();
        mockOpportunity.getScore().addAll(mockScores);

        final TDSReport mockTDSReport = new TDSReport();
        mockTDSReport.setOpportunity(mockOpportunity);

        when(testScorer.scoreTDSReport(tdsReport)).thenReturn(mockTDSReport);

        underTest.analyze(individualResponse);
        OpportunityCategory actualOpportunityCategory = individualResponse.getOpportunityCategory();
        TdsReportScoreIrpScoredScore tdsReportScoreIrpScoredScore = actualOpportunityCategory.getTdsReportScoreIrpScoredScore();
        Map<String, TDSReport.Opportunity.Score> tdsMatchedScoreMap = tdsReportScoreIrpScoredScore.getMatchedScoresMap();
        
        Map<String, TDSReport.Opportunity.Score> matchedScoresMap = new HashMap<>();
        matchedScoresMap.put("OverallAttempted", (new ScoreBuilder().measureOf("Overall").measureLabel("Attempted").value("Y").standardError("1.234").toScore()));
        TdsReportScoreIrpScoredScore expectedTdsReportScoreIrpScoredScore = new TdsReportScoreIrpScoredScoreBuilder()
        	.isScoredTDSReport(true)
        	.isScoreMatch(true)
        	.matchedScoresMap(matchedScoresMap)
        	.extraTdsReportScoreMap(new HashMap<String, TDSReport.Opportunity.Score>())
        	.missedIrpScoredScoreMap(new HashMap<String, TDSReport.Opportunity.Score>())
        	.notMatchPairs(new ArrayList<TdsReportScoreIrpScoredScorePair>())
        	.toTdsReportScoreIrpScoredScore();
        assertTrue(individualResponse.hasValidScoring());
        assertTrue(tdsReportScoreIrpScoredScore.isScoredTDSReport());
        assertTrue(tdsReportScoreIrpScoredScore.isScoreMatch());
        assertArrayEquals(tdsMatchedScoreMap.keySet().toArray(), matchedScoresMap.keySet().toArray());
        //assertEquals(expectedTdsReportScoreIrpScoredScore, tdsReportScoreIrpScoredScore);
    }

    private CellCategory[] createValidCellCategories(String measureOf, String measureLabel, String value, String standarError) {
        CellCategory[] cellCategories = new CellCategory[] {
                new CellCategoryBuilder()
                        .correctDataType(true)
                        .acceptableValue(true)
                        .isFieldEmpty(false)
                        .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.P)
                        .tdsFieldName(TestScoreAnalysisAction.EnumScoreFieldName.measureOf.toString())
                        .tdsFieldNameValue(measureOf)
                        .toCellCategory(),

                new CellCategoryBuilder()
                        .correctDataType(true)
                        .acceptableValue(true)
                        .isFieldEmpty(false)
                        .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.P)
                        .tdsFieldName(TestScoreAnalysisAction.EnumScoreFieldName.measureLabel.toString())
                        .tdsFieldNameValue(measureLabel)
                        .toCellCategory(),

                new CellCategoryBuilder()
                        .correctDataType(true)
                        .acceptableValue(true)
                        .isFieldEmpty(false)
                        .enumFieldCheckType(FieldCheckType.EnumFieldCheckType.P)
                        .tdsFieldName(TestScoreAnalysisAction.EnumScoreFieldName.value.toString())
                        .tdsFieldNameValue(value)
                        .toCellCategory(),

                new CellCategoryBuilder()
                        .correctDataType(true)
                        .acceptableValue(true)
                        .isFieldEmpty(false)
						.enumFieldCheckType(FieldCheckType.EnumFieldCheckType.P)
						.tdsFieldName(TestScoreAnalysisAction.EnumScoreFieldName.standardError.toString())
						.tdsFieldNameValue(standarError).toCellCategory() };

		return cellCategories;
	}

	private CellCategory[] createInvalidCellCategories() {
		CellCategory[] cellCategories = new CellCategory[] {
				new CellCategoryBuilder()
					.correctDataType(true)
					.acceptableValue(true)
					.isFieldEmpty(false)
					.enumFieldCheckType(FieldCheckType.EnumFieldCheckType.P)
					.tdsFieldName(TestScoreAnalysisAction.EnumScoreFieldName.measureOf.toString())
					.tdsFieldNameValue("Overall").toCellCategory(),

				new CellCategoryBuilder()
					.correctDataType(true)
					.acceptableValue(true)
					.isFieldEmpty(false)
					.enumFieldCheckType(FieldCheckType.EnumFieldCheckType.P)
					.tdsFieldName(TestScoreAnalysisAction.EnumScoreFieldName.measureLabel.toString())
					.tdsFieldNameValue("ThetaScore").toCellCategory(),

				new CellCategoryBuilder()
					.correctDataType(false)
					.acceptableValue(false)
					.isFieldEmpty(true)
					.enumFieldCheckType(FieldCheckType.EnumFieldCheckType.P)
					.tdsFieldName(TestScoreAnalysisAction.EnumScoreFieldName.value.toString()).tdsFieldNameValue("")
					.toCellCategory(),

				new CellCategoryBuilder()
					.correctDataType(false)
					.acceptableValue(false)
					.isFieldEmpty(false)
					.enumFieldCheckType(FieldCheckType.EnumFieldCheckType.P)
					.tdsFieldName(TestScoreAnalysisAction.EnumScoreFieldName.standardError.toString())
					.tdsFieldNameValue("fail").toCellCategory() };

		return cellCategories;
	}
}
