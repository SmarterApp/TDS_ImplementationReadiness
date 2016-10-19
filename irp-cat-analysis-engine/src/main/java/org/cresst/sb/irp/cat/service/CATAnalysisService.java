package org.cresst.sb.irp.cat.service;

import org.cresst.sb.irp.cat.domain.analysis.CATAnalysisResponse;
import org.cresst.sb.irp.cat.domain.analysis.CATDataModel;

public interface CATAnalysisService {
    CATAnalysisResponse analyzeCatResults(CATDataModel catData);
}
