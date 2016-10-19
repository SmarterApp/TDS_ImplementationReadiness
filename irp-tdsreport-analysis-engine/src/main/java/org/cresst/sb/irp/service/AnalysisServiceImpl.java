package org.cresst.sb.irp.service;

import org.cresst.sb.irp.analysis.engine.AnalysisEngine;
import org.cresst.sb.irp.domain.analysis.AnalysisResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class AnalysisServiceImpl implements AnalysisService {

    @Autowired
    private AnalysisEngine tdsReportAnalysisEngine;

    @Override
    public AnalysisResponse analysisProcess(Iterable<Path> tdsReportPaths) {
        return tdsReportAnalysisEngine.analyze(tdsReportPaths);
    }
}
