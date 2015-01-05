package org.cresst.sb.irp.domain.analysis;

import com.google.common.collect.ImmutableList;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class AnalysisResponse {
    // Each IndividualResponse represents each TDSReport xml file and it includes all analyze results
    private List<IndividualResponse> individualResponses = new ArrayList<>();

    public ImmutableList<IndividualResponse> getIndividualResponses() {
        return ImmutableList.copyOf(individualResponses);
    }

    public void addListIndividualResponse(IndividualResponse individualResponse) {
        this.individualResponses.add(individualResponse);
    }
}
