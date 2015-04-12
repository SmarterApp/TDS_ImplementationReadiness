package org.cresst.sb.irp.domain.analysis;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AnalysisResponse {
    // Each IndividualResponse represents each TDSReport xml file and it includes all analyze results
    private List<IndividualResponse> individualResponses = new ArrayList<>();

    public ImmutableList<IndividualResponse> getIndividualResponses() {
        return ImmutableList.copyOf(individualResponses);
    }

    public void addListIndividualResponse(IndividualResponse individualResponse) {
        this.individualResponses.add(individualResponse);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnalysisResponse that = (AnalysisResponse) o;
        return Objects.equals(individualResponses, that.individualResponses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(individualResponses);
    }
}
