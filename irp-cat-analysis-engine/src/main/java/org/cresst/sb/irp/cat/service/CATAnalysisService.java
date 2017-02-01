package org.cresst.sb.irp.cat.service;

import java.io.IOException;

import org.cresst.sb.irp.cat.domain.analysis.CATAnalysisResponse;
import org.cresst.sb.irp.cat.domain.analysis.CATDataModel;

/**
 * Analyzes CAT data from a simulation run. 
 */
public interface CATAnalysisService {
    CATAnalysisResponse analyzeCatResults(CATDataModel catData) throws IOException;
}
