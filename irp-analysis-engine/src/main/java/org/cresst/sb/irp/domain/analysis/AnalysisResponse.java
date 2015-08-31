package org.cresst.sb.irp.domain.analysis;

import com.google.common.collect.ImmutableList;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AnalysisResponse {
    // Each IndividualResponse represents each TDSReport xml file and it includes all analyze results
    private List<IndividualResponse> individualResponses = new ArrayList<>();
    private String vendorName;
    private String irpVersion;
    private String dateTimeAnalyzed;

    public ImmutableList<IndividualResponse> getIndividualResponses() {
        return ImmutableList.copyOf(individualResponses);
    }

    public void addListIndividualResponse(IndividualResponse individualResponse) {
        this.individualResponses.add(individualResponse);
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getIrpVersion() {
        return irpVersion;
    }

    public void setIrpVersion(String irpVersion) {
        this.irpVersion = irpVersion;
    }

    public String getDateTimeAnalyzed() {
        return dateTimeAnalyzed;
    }

    public void setDateTimeAnalyzed(String dateTimeAnalyzed) {
        this.dateTimeAnalyzed = dateTimeAnalyzed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnalysisResponse that = (AnalysisResponse) o;
        return Objects.equals(individualResponses, that.individualResponses) &&
                Objects.equals(vendorName, that.vendorName) &&
                Objects.equals(irpVersion, that.irpVersion) &&
                Objects.equals(dateTimeAnalyzed, that.dateTimeAnalyzed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(individualResponses, vendorName, irpVersion, dateTimeAnalyzed);
    }
}
