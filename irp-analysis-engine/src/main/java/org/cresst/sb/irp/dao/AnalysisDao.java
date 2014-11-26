package org.cresst.sb.irp.dao;

import org.cresst.sb.irp.domain.tdsreport.TDSReport;

public interface AnalysisDao {

	void analysisProcess(TDSReport tdsReport);
	
}
