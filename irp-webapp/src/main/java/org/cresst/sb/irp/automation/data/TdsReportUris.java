package org.cresst.sb.irp.automation.data;

import java.net.URI;
import java.util.List;

public class TdsReportUris {
    private String vendorName;
    private List<URI> tdsReportUris;

    public TdsReportUris() {}

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public List<URI> getTdsReportUris() {
        return tdsReportUris;
    }

    public void setTdsReportUris(List<URI> tdsReportUris) {
        this.tdsReportUris = tdsReportUris;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TdsReportUris{");
        sb.append("vendorName='").append(vendorName).append('\'');
        sb.append(", tdsReportUris=").append(tdsReportUris);
        sb.append('}');
        return sb.toString();
    }
}
