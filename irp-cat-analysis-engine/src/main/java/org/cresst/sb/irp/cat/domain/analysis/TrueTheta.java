package org.cresst.sb.irp.cat.domain.analysis;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"sid", "theta"})
public class TrueTheta {
    private String sid;
    private double theta;

    public String getSid() {
        return sid;
    }
    public void setSid(String sid) {
        this.sid = sid;
    }
    public double getTheta() {
        return theta;
    }
    public void setTheta(double theta) {
        this.theta = theta;
    }

    @Override
    public String toString() {
        return "TrueTheta [sid=" + sid + ", theta=" + theta + "]";
    }
}
