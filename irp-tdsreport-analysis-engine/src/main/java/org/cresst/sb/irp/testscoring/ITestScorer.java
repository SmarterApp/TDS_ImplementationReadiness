package org.cresst.sb.irp.testscoring;

import org.cresst.sb.irp.domain.tdsreport.TDSReport;

/**
 * Interface for test scorer implementation
 */
public interface ITestScorer {
    /**
     * Scores the TDSReport
     * @param tdsReport    A TDSReport without scoring
     * @return A TDSReport with scoring
     */
    TDSReport scoreTDSReport(TDSReport tdsReport);
}
