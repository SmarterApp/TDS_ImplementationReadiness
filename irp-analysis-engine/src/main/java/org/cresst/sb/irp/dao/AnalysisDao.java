package org.cresst.sb.irp.dao;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.cresst.sb.irp.domain.analysis.AnalysisResponse;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;

public interface AnalysisDao {

	//void analysisProcess(TDSReport tdsReport,  List<HashMap<String, Hashtable<String, String>>> analysisResponseList);
	
	void analysisProcess(AnalysisResponse analysisResponse);
	
	
}
