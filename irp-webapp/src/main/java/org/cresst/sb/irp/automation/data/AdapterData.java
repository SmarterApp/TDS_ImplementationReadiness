package org.cresst.sb.irp.automation.data;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

public class AdapterData {
    @NotNull @NotBlank
    private String vendorName;
    @NotEmpty
    private List<URI> tdsReportLinks;

    public AdapterData() {}

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public List<URI> getTdsReportLinks() {
        return tdsReportLinks;
    }

    public void setTdsReportLinks(List<URI> tdsReportLinks) {
        this.tdsReportLinks = tdsReportLinks;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AdapterData{");
        sb.append("vendorName='").append(vendorName).append('\'');
        sb.append(", tdsReportLinks=").append(tdsReportLinks);
        sb.append('}');
        return sb.toString();
    }
}
