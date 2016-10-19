package builders;

import org.cresst.sb.irp.domain.tdsreport.TDSReport;

public class ScoreBuilder {

    private TDSReport.Opportunity.Score score = new TDSReport.Opportunity.Score();

    public ScoreBuilder measureLabel(String measureLabel) {
        score.setMeasureLabel(measureLabel);
        return this;
    }

    public ScoreBuilder measureOf(String measureOf) {
        score.setMeasureOf(measureOf);
        return this;
    }

    public ScoreBuilder standardError(String standardError) {
        score.setStandardError(standardError);
        return this;
    }

    public ScoreBuilder value(String value) {
        score.setValue(value);
        return this;
    }

    public TDSReport.Opportunity.Score toScore() {
        return score;
    }
}
