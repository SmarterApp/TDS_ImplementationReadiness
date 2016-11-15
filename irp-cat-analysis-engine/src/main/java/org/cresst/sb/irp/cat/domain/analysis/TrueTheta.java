package org.cresst.sb.irp.cat.domain.analysis;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"sid", "score"})
public class TrueTheta implements Score {
    private String sid;
    private double score;

    @Override
    public String getSid() {
        return sid;
    }
    public void setSid(String sid) {
        this.sid = sid;
    }
    @Override
    public double getScore() {
        return score;
    }
    public void setScore(double theta) {
        this.score = theta;
    }

    @Override
    public String toString() {
        return "TrueTheta [sid=" + sid + ", score=" + score + "]";
    }
}
