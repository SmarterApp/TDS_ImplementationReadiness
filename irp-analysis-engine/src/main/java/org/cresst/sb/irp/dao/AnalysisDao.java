package org.cresst.sb.irp.dao;

import org.cresst.sb.irp.domain.analysis.AnalysisResponse;

import java.nio.file.Path;

public interface AnalysisDao {

    AnalysisResponse analysisProcess(Iterable<Path> tdsReportPaths);

}
