package org.cresst.sb.irp.domain.analysis;

import org.cresst.sb.irp.domain.tdsreport.TDSReport;

import java.util.*;

public class TdsReportScoreIrpScoredScore {

	private boolean isScoredTDSReport; // false - tisScorer.scoreTDSReport(tdsReport) return null
	private boolean isScoreMatch; // Verify TIS scores matches TDSReport scores

	private Map<String, TDSReport.Opportunity.Score> matchedScoresMap = new TreeMap<>();
	private Map<String, TDSReport.Opportunity.Score> extraTdsReportScoreMap = new TreeMap<>();
	private Map<String, TDSReport.Opportunity.Score> missedIrpScoredScoreMap = new TreeMap<>();

	private List<TdsReportScoreIrpScoredScorePair> notMatchPairs = new ArrayList<>(); // same key but score not match

	public boolean isScoredTDSReport() {
		return isScoredTDSReport;
	}

	public void setScoredTDSReport(boolean isScoredTDSReport) {
		this.isScoredTDSReport = isScoredTDSReport;
	}

	public boolean isScoreMatch() {
		return isScoreMatch;
	}

	public void setScoreMatch(boolean isScoreMatch) {
		this.isScoreMatch = isScoreMatch;
	}

	public Map<String, TDSReport.Opportunity.Score> getMatchedScoresMap() {
		return matchedScoresMap;
	}

	public void setMatchedScoresMap(Map<String, TDSReport.Opportunity.Score> matchedScoresMap) {
		this.matchedScoresMap = matchedScoresMap;
	}

	public Map<String, TDSReport.Opportunity.Score> getExtraTdsReportScoreMap() {
		return extraTdsReportScoreMap;
	}

	public void setExtraTdsReportScoreMap(Map<String, TDSReport.Opportunity.Score> extraTdsReportScoreMap) {
		this.extraTdsReportScoreMap = extraTdsReportScoreMap;
	}

	public List<TdsReportScoreIrpScoredScorePair> getNotMatchPairs() {
		return notMatchPairs;
	}

	public void setNotMatchPairs(List<TdsReportScoreIrpScoredScorePair> notMatchPairs) {
		this.notMatchPairs = notMatchPairs;
	}

	public Map<String, TDSReport.Opportunity.Score> getMissedIrpScoredScoreMap() {
		return missedIrpScoredScoreMap;
	}

	public void setMissedIrpScoredScoreMap(Map<String, TDSReport.Opportunity.Score> missedIrpScoredScoreMap) {
		this.missedIrpScoredScoreMap = missedIrpScoredScoreMap;
	}
	
	/**
	 * TDSReport.Opportunity.Score has NOT override the euqals and hashcode method,
	 * matchedScoresMap, extraTdsReportScoreMap, missedIrpScoredScoreMap and notMatchPairs
	 * may NOT work properly
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		TdsReportScoreIrpScoredScore that = (TdsReportScoreIrpScoredScore) o;
		return Objects.equals(isScoredTDSReport, that.isScoredTDSReport) &&
				Objects.equals(isScoreMatch, that.isScoreMatch) &&
				Objects.equals(matchedScoresMap, that.matchedScoresMap) &&
				Objects.equals(extraTdsReportScoreMap, that.extraTdsReportScoreMap) &&
				Objects.equals(missedIrpScoredScoreMap, that.missedIrpScoredScoreMap) &&
				Objects.equals(notMatchPairs, that.notMatchPairs);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(isScoredTDSReport, isScoreMatch, matchedScoresMap, extraTdsReportScoreMap, missedIrpScoredScoreMap,
				notMatchPairs);
	}

}
