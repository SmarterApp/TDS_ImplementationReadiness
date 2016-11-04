package org.cresst.sb.irp.cat.service;

import java.util.List;

import org.cresst.sb.irp.cat.domain.analysis.BlueprintStatement;
import org.cresst.sb.irp.cat.domain.analysis.CATAnalysisResponse;
import org.cresst.sb.irp.cat.domain.analysis.CATDataModel;

public interface CATAnalysisService {
    CATAnalysisResponse analyzeCatResults(CATDataModel catData);

    void calculateBlueprintViolations(CATDataModel catData, CATAnalysisResponse response,
            List<BlueprintStatement> blueprintStatements);
}
