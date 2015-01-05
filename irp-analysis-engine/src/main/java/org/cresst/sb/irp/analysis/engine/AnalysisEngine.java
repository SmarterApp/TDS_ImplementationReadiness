package org.cresst.sb.irp.analysis.engine;

import org.cresst.sb.irp.domain.analysis.AnalysisResponse;

import java.nio.file.Path;

public interface AnalysisEngine {

    AnalysisResponse analyze(Iterable<Path> tdsReportPaths);

}
