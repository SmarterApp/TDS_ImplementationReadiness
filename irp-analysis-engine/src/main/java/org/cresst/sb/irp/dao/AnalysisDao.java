package org.cresst.sb.irp.dao;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.cresst.sb.irp.domain.analysis.AnalysisResponse;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;

public interface AnalysisDao {
	
	AnalysisResponse analysisProcess(Iterable<Path> tdsReportPaths);
	
}
