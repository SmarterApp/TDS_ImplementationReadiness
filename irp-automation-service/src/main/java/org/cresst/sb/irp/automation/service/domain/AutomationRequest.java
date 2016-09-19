package org.cresst.sb.irp.automation.service.domain;

import java.net.URL;
import java.util.Objects;

public class AutomationRequest {

    private String vendorName;
    private URL adapterUrl;

    public AutomationRequest() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AutomationRequest that = (AutomationRequest) o;
        return Objects.equals(vendorName, that.vendorName) &&
                Objects.equals(adapterUrl, that.adapterUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vendorName, adapterUrl);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AutomationRequest{");
        sb.append("vendorName='").append(vendorName).append('\'');
        sb.append(", adapterUrl=").append(adapterUrl);
        sb.append('}');
        return sb.toString();
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public URL getAdapterUrl() {
        return adapterUrl;
    }

    public void setAdapterUrl(URL adapterUrl) {
        this.adapterUrl = adapterUrl;
    }
}
