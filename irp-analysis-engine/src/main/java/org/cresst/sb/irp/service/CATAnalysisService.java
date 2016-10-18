package org.cresst.sb.irp.service;

import org.cresst.sb.irp.domain.analysis.CATAnalysisResponse;
import org.cresst.sb.irp.domain.analysis.CATDataModel;

public interface CATAnalysisService {
    CATAnalysisResponse analyzeCatResults(CATDataModel catData);
}
