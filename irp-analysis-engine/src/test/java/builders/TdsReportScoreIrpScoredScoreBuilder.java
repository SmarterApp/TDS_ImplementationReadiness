package builders;

import java.util.List;
import java.util.Map;

import org.cresst.sb.irp.domain.analysis.TdsReportScoreIrpScoredScore;
import org.cresst.sb.irp.domain.analysis.TdsReportScoreIrpScoredScorePair;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;

public class TdsReportScoreIrpScoredScoreBuilder {

	TdsReportScoreIrpScoredScore tdsReportScoreIrpScoredScore = new TdsReportScoreIrpScoredScore();

	public TdsReportScoreIrpScoredScoreBuilder isScoredTDSReport(boolean isScoredTDSReport) {
		tdsReportScoreIrpScoredScore.setScoredTDSReport(isScoredTDSReport);
		return this;
	}

	public TdsReportScoreIrpScoredScoreBuilder isScoreMatch(boolean isScoreMatch) {
		tdsReportScoreIrpScoredScore.setScoreMatch(isScoreMatch);
		return this;
	}

	public TdsReportScoreIrpScoredScoreBuilder matchedScoresMap(Map<String, TDSReport.Opportunity.Score> matchedScoresMap) {
		tdsReportScoreIrpScoredScore.setMatchedScoresMap(matchedScoresMap);
		return this;
	}

	public TdsReportScoreIrpScoredScoreBuilder extraTdsReportScoreMap(Map<String, TDSReport.Opportunity.Score> extraTdsReportScoreMap) {
		tdsReportScoreIrpScoredScore.setExtraTdsReportScoreMap(extraTdsReportScoreMap);
		return this;
	}
	
	public TdsReportScoreIrpScoredScoreBuilder missedIrpScoredScoreMap(Map<String, TDSReport.Opportunity.Score> missedIrpScoredScoreMap) {
		tdsReportScoreIrpScoredScore.setMissedIrpScoredScoreMap(missedIrpScoredScoreMap);
		return this;
	}
	
	public TdsReportScoreIrpScoredScoreBuilder notMatchPairs(List<TdsReportScoreIrpScoredScorePair> notMatchPairs){
		tdsReportScoreIrpScoredScore.setNotMatchPairs(notMatchPairs);
		return this;
	}

	public TdsReportScoreIrpScoredScore toTdsReportScoreIrpScoredScore() {
		return tdsReportScoreIrpScoredScore;
	}

}
