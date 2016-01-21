package org.cresst.sb.irp.domain.analysis;

import org.cresst.sb.irp.domain.tdsreport.TDSReport;

public class TdsReportScoreIrpScoredScorePair {

	private TDSReport.Opportunity.Score tdsReportScore;
	private TDSReport.Opportunity.Score irpScoredScore;

	public TDSReport.Opportunity.Score getTdsReportScore() {
		return tdsReportScore;
	}

	public void setTdsReportScore(TDSReport.Opportunity.Score tdsReportScore) {
		this.tdsReportScore = tdsReportScore;
	}

	public TDSReport.Opportunity.Score getIrpScoredScore() {
		return irpScoredScore;
	}

	public void setIrpScoredScore(TDSReport.Opportunity.Score irpScoredScore) {
		this.irpScoredScore = irpScoredScore;
	}
	
	public void set(TDSReport.Opportunity.Score tdsReportScore, TDSReport.Opportunity.Score irpScoredScore){
		setTdsReportScore(tdsReportScore);
		setIrpScoredScore(irpScoredScore);
	}

}
