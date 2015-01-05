package org.cresst.sb.irp.service;

import org.cresst.sb.irp.domain.analysis.AnalysisResponse;

import java.nio.file.Path;

public interface AnalysisService {

	AnalysisResponse analysisProcess(Iterable<Path> tdsReportPaths);

}
