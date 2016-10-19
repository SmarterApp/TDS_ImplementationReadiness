package builders;

import org.cresst.sb.irp.domain.analysis.TdsReportScoreIrpScoredScorePair;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;

public class TdsReportScoreIrpScoredScorePairBuilder {
	
	TdsReportScoreIrpScoredScorePair tdsReportScoreIrpScoredScorePair = new TdsReportScoreIrpScoredScorePair();
	
	public TdsReportScoreIrpScoredScorePairBuilder tdsReportScore(TDSReport.Opportunity.Score tdsReportScore){
		tdsReportScoreIrpScoredScorePair.setTdsReportScore(tdsReportScore);
		return this;
	}
	
	public TdsReportScoreIrpScoredScorePairBuilder irpScoredScore(TDSReport.Opportunity.Score irpScoredScore){
		tdsReportScoreIrpScoredScorePair.setIrpScoredScore(irpScoredScore);
		return this;
	}
	
	public TdsReportScoreIrpScoredScorePair toTdsReportScoreIrpScoredScorePair(){
		return tdsReportScoreIrpScoredScorePair;
	}

}
