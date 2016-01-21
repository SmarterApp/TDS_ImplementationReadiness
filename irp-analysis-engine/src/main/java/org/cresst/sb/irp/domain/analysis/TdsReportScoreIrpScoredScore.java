package org.cresst.sb.irp.domain.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cresst.sb.irp.domain.tdsreport.TDSReport;

public class TdsReportScoreIrpScoredScore {

	private boolean isScoredTDSReport; // false - tisScorer.scoreTDSReport(tdsReport) return null
	private boolean isScoreMatch; // Verify TIS scores matches TDSReport scores

	private Map<String, TDSReport.Opportunity.Score> matchedScoresMap = new HashMap<>();
	private Map<String, TDSReport.Opportunity.Score> extraTdsReportScoreMap = new HashMap<>();
	private Map<String, TDSReport.Opportunity.Score> missedIrpScoredScoreMap = new HashMap<>();

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

}
