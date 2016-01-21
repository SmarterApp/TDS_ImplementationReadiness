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

	public void set(TDSReport.Opportunity.Score tdsReportScore, TDSReport.Opportunity.Score irpScoredScore) {
		setTdsReportScore(tdsReportScore);
		setIrpScoredScore(irpScoredScore);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		TdsReportScoreIrpScoredScorePair that = (TdsReportScoreIrpScoredScorePair) o;
		
		if (tdsReportScore != null ? !tdsReportScore.equals(that.tdsReportScore) : that.tdsReportScore != null)
			return false;
		if (irpScoredScore != null ? !irpScoredScore.equals(that.irpScoredScore) : that.irpScoredScore != null)
			return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
	    int result = tdsReportScore != null ? tdsReportScore.hashCode() : 0;
        result = 31 * result + (irpScoredScore != null ? irpScoredScore.hashCode() : 0);
        return result;
	}

}
