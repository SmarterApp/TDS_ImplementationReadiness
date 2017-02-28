package org.cresst.sb.irp.analysis.engine;

import com.google.common.primitives.Floats;
import org.apache.commons.lang3.StringUtils;
import org.cresst.sb.irp.domain.analysis.*;
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
public class TestScoreAnalysisAction extends
		AnalysisAction<TDSReport.Opportunity.Score, TestScoreAnalysisAction.EnumScoreFieldName, Object> {
	private final static Logger logger = LoggerFactory.getLogger(TestScoreAnalysisAction.class);

	public enum EnumScoreFieldName {
		measureOf, measureLabel, value, standardError
	}

	@Autowired
	private ITestScorer tisScorer;

	/**
	 * Analyze Scores in the TDS Report
	 *
	 * @param individualResponse
	 *            Where the TDS Report is obtained and where the result is stored
	 */
	@Override
	public void analyze(IndividualResponse individualResponse) {
		try {
			List<TDSReport.Opportunity.Score> scores = individualResponse.getTDSReport().getOpportunity().getScore();

			if (scores.size() > 0) {

				// Analyze all TDSReport.Opportunity.Scores first
				List<ScoreCategory> scoreCategories = new ArrayList<>();
				OpportunityCategory opportunityCategory = individualResponse.getOpportunityCategory();
				TdsReportScoreIrpScoredScore tdsReportScoreIrpScoredScore = opportunityCategory.getTdsReportScoreIrpScoredScore();
				opportunityCategory.setScoreCategories(scoreCategories);

				individualResponse.setValidScoring(true);

				for (TDSReport.Opportunity.Score score : scores) {
					ScoreCategory scoreCategory = new ScoreCategory();
					scoreCategories.add(scoreCategory);

					validate(scoreCategory, score, score.getMeasureOf(), FieldCheckType.EnumFieldCheckType.P,
							EnumScoreFieldName.measureOf, null);
					validate(scoreCategory, score, score.getMeasureLabel(), FieldCheckType.EnumFieldCheckType.P,
							EnumScoreFieldName.measureLabel, null);
					validate(scoreCategory, score, score.getValue(), FieldCheckType.EnumFieldCheckType.P, EnumScoreFieldName.value,
							null);
					validate(scoreCategory, score, score.getStandardError(), FieldCheckType.EnumFieldCheckType.P,
							EnumScoreFieldName.standardError, null);

					for (CellCategory cellCategory : scoreCategory.getCellCategories()) {
					    FieldCheckType fieldCheckType = cellCategory.getFieldCheckType();
						if (!fieldCheckType.isOptionalValue() && !fieldCheckType.isAcceptableValue()) {
							individualResponse.setValidScoring(false);
						}
					}
				}

				// Construct a TDSReport without scoring information
				TDSReport tdsReport = individualResponse.getTDSReport();
				List<TDSReport.Opportunity.Score> copyOfScores = copyScores(tdsReport.getOpportunity().getScore());
				tdsReport.getOpportunity().getScore().clear();

				// Submit the TDSReport without scoring information to TIS and receive the response
				TDSReport scoredTDSReport = tisScorer.scoreTDSReport(tdsReport);

				// Put the scores back
				tdsReport.getOpportunity().getScore().addAll(copyOfScores);

				if (scoredTDSReport != null) {
					logger.info("Received scored TDSReport");
					tdsReportScoreIrpScoredScore.setScoredTDSReport(true);

					if (scoredTDSReport.getOpportunity() != null)
						// Verify TIS scores matches TDSReport scores
						scoresMatch(tdsReport.getOpportunity().getScore(), scoredTDSReport.getOpportunity().getScore(),
							tdsReportScoreIrpScoredScore);
				}
			}
		} catch (Exception e) {
			logger.error("Test Score Analysis Exception", e);
		}
	}

	/**
	 * Verifies the scores from the TDS Report match the scores calculated by TIS
	 *
	 * @param tdsReportScores
	 *            Scores from the TDS Report
	 * @param irpScoredScores
	 *            Scores from the TIS
	 * @param tdsReportScoreIrpScoredScore
	 * 			  Store tds report scores information and tis scored scores information
	 */
	private void scoresMatch(List<TDSReport.Opportunity.Score> tdsReportScores, List<TDSReport.Opportunity.Score> irpScoredScores,
			TdsReportScoreIrpScoredScore tdsReportScoreIrpScoredScore) {

		tdsReportScoreIrpScoredScore.setScoreMatch(true);
		Map<String, TDSReport.Opportunity.Score> matchedScoresMap = tdsReportScoreIrpScoredScore.getMatchedScoresMap();
		Map<String, TDSReport.Opportunity.Score> extraTdsReportScoreMap = tdsReportScoreIrpScoredScore.getExtraTdsReportScoreMap();
		List<TdsReportScoreIrpScoredScorePair> notMatchScorePairs = tdsReportScoreIrpScoredScore.getNotMatchPairs();
		Map<String, TDSReport.Opportunity.Score> missedIrpScoredMap = tdsReportScoreIrpScoredScore.getMissedIrpScoredScoreMap();

		Map<String, TDSReport.Opportunity.Score> irpScoresMap = new HashMap<>();
		for (TDSReport.Opportunity.Score score : irpScoredScores) {
			irpScoresMap.put(score.getMeasureOf() + score.getMeasureLabel(), score);
		}

		for (TDSReport.Opportunity.Score score : tdsReportScores) {
			TDSReport.Opportunity.Score irpScore = irpScoresMap.get(score.getMeasureOf() + score.getMeasureLabel());
			if (irpScore != null) {

				final boolean isScaleScore = "ScaleScore".equalsIgnoreCase(score.getMeasureLabel());
				final boolean isThetaScore = "ThetaScore".equalsIgnoreCase(score.getMeasureLabel());

				final boolean isScoreEqual = (isScaleScore || isThetaScore)
						&& decimalScoresMatch(isScaleScore, isThetaScore,
								score.getValue(), irpScore.getValue(),
								score.getStandardError(), irpScore.getStandardError());

				final boolean isValueEqual = !StringUtils.isEmpty(score.getValue())
						&& score.getValue().equals(irpScore.getValue());

				if (!isScoreEqual && !isValueEqual) {
					TdsReportScoreIrpScoredScorePair notMatchPair = new TdsReportScoreIrpScoredScorePair();
					notMatchPair.set(score, irpScore);
					notMatchScorePairs.add(notMatchPair);
					tdsReportScoreIrpScoredScore.setScoreMatch(false);
				} else {
					matchedScoresMap.put(score.getMeasureOf() + score.getMeasureLabel(), score);
				}

				irpScoresMap.remove(score.getMeasureOf() + score.getMeasureLabel());
			} else {
				extraTdsReportScoreMap.put(score.getMeasureOf() + score.getMeasureLabel(), score);
				tdsReportScoreIrpScoredScore.setScoreMatch(false);
			}
		}

		for (Map.Entry<String, TDSReport.Opportunity.Score> entry : irpScoresMap.entrySet()) {
			missedIrpScoredMap.put(entry.getKey(), entry.getValue());
			tdsReportScoreIrpScoredScore.setScoreMatch(false);
		}
	}

	/**
	 * Compares ScaleScore and ThetaScore values as floating point numbers
	 *
	 * @param isScaleScore Indicates if the score is a ScaleScore
	 * @param isThetaScore Indicates if the score is a ThetaScore
	 * @param tdsScoreString The vendor's score value
	 * @param irpScoreString IRP's score value
	 * @param tdsStandardErrorString The vendor's standard error
	 * @param irpStandardErrorString IRP's standard error
     * @return True if the scores and standard errors are nearly equal; false otherwise.
     */
	private static boolean decimalScoresMatch(boolean isScaleScore, boolean isThetaScore,
											  String tdsScoreString, String irpScoreString,
											  String tdsStandardErrorString, String irpStandardErrorString) {

		final float scaleScoreEpsilon = 0.1f;
		final float thetaScoreEpsilon = 0.01f;
		final float standardErrorEpsilon = 0.01f;

		final Float tdsScoreValue = Floats.tryParse(tdsScoreString);
		final Float irpScoreValue = Floats.tryParse(irpScoreString);

		final Float tdsStandardError = Floats.tryParse(tdsStandardErrorString);
		final Float irpStandardError = Floats.tryParse(irpStandardErrorString);

		return (isScaleScore && nearlyEqual(tdsScoreValue, irpScoreValue, scaleScoreEpsilon))
				|| (isThetaScore && nearlyEqual(tdsScoreValue, irpScoreValue, thetaScoreEpsilon))
				&& nearlyEqual(tdsStandardError, irpStandardError, standardErrorEpsilon);
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
	 * Field Check Type (P) --> check that field is not empty and field value is of correct data type and within acceptable values
	 *
	 * @param score
	 *            Score object with fields to check
	 * @param enumFieldName
	 *            Specifies the field to check
	 * @param fieldCheckType
	 *            Stores the analysis data for the field being analyzed
	 */
	@Override
	protected void checkP(TDSReport.Opportunity.Score score, EnumScoreFieldName enumFieldName, FieldCheckType fieldCheckType) {
		switch (enumFieldName) {
		case measureOf:
			processP_PrintableASCIIone(score.getMeasureOf(), fieldCheckType);
			break;
		case measureLabel:
			processP_PrintableASCIIone(score.getMeasureLabel(), fieldCheckType);
			break;
		case value:
			processP_PrintableASCIIone(score.getValue(), fieldCheckType);

			if (fieldCheckType.isCorrectDataType()
					&& fieldCheckType.isAcceptableValue()
					&& "Overall".equalsIgnoreCase(score.getMeasureOf())
					&& "Attempted".equalsIgnoreCase(score.getMeasureLabel())) {

				String value = score.getValue();
				fieldCheckType.setAcceptableValue("Y".equalsIgnoreCase(value) || "N".equalsIgnoreCase(value));
			}

			// These measures are floating point numbers
			if (fieldCheckType.isCorrectDataType()
					&& fieldCheckType.isAcceptableValue()
					&& ("ScaleScore".equalsIgnoreCase(score.getMeasureLabel())
						|| "ThetaScore".equalsIgnoreCase(score.getMeasureLabel()))) {

				if (StringUtils.isBlank(score.getValue())
						|| Floats.tryParse(score.getValue()) == null) {
					fieldCheckType.setCorrectDataType(false);
					fieldCheckType.setAcceptableValue(false);
				}
			}
			break;
		case standardError:
			processP_FloatAllowNulls(score.getStandardError(), fieldCheckType);
            fieldCheckType.setOptionalValue(true);

			if (fieldCheckType.isCorrectDataType()
				&& fieldCheckType.isAcceptableValue()
				&& ("ScaleScore".equalsIgnoreCase(score.getMeasureLabel())
					|| "ThetaScore".equalsIgnoreCase(score.getMeasureLabel()))
				&& StringUtils.isBlank(score.getStandardError())) {

				fieldCheckType.setAcceptableValue(false);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Checks if the field has the correct value
	 *
	 * @param score
	 *            Score object with field to check
	 * @param enumFieldName
	 *            Specifies the field to check
	 * @param fieldCheckType
	 * @param comparisonData
	 */
	@Override
	protected void checkC(TDSReport.Opportunity.Score score, EnumScoreFieldName enumFieldName, FieldCheckType fieldCheckType,
			Object comparisonData) {
	}

	/**
	 * Compare two floats
	 * http://floating-point-gui.de/errors/comparison/
	 *
	 * @param a Float value to compare
	 * @param b Float value to compare
	 * @param epsilon Tolerance
     * @return Returns true if a falls within [b - epsilon, b + epsilon]
     */
	public static boolean nearlyEqual(Float a, Float b, float epsilon) {

		if (a == null || b == null) {
			return false;
		}

		if (a == b) { // shortcut
			return true;
		}

		return b - epsilon <= a && a <= b + epsilon;
	}
}
