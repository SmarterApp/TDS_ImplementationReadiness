package org.cresst.sb.irp.cat.domain.analysis;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
    "sid", "overallScore","overallSEM","claim1Score","claim1SEM",
    "claim2Score","claim2SEM","claim3Score","claim3SEM","claim4Score","claim4SEM"
    })
public class StudentScoreCAT implements Score {
    private String sid;
    private double overallScore;
    private double overallSEM;
    private double claim1Score;
    private double claim1SEM;
    private double claim2Score;
    private double claim2SEM;
    private double claim3Score;
    private double claim3SEM;
    private double claim4Score;
    private double claim4SEM;

    @Override
    public String getSid() {
        return sid;
    }

    public void setSid(String sId) {
        this.sid = sId;
    }

    @Override
    public double getScore() {
        return overallScore;
    }

    public double getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(double overallScore) {
        this.overallScore = overallScore;
    }

    public double getOverallSEM() {
        return overallSEM;
    }

    public void setOverallSEM(double overallSEM) {
        this.overallSEM = overallSEM;
    }

    public double getClaim1Score() {
        return claim1Score;
    }

    public void setClaim1Score(double claim1Score) {
        this.claim1Score = claim1Score;
    }

    public double getClaim1SEM() {
        return claim1SEM;
    }

    public void setClaim1SEM(double claim1sem) {
        claim1SEM = claim1sem;
    }

    public double getClaim2Score() {
        return claim2Score;
    }

    public void setClaim2Score(double claim2Score) {
        this.claim2Score = claim2Score;
    }

    public double getClaim2SEM() {
        return claim2SEM;
    }

    public void setClaim2SEM(double claim2sem) {
        claim2SEM = claim2sem;
    }

    public double getClaim3Score() {
        return claim3Score;
    }

    public void setClaim3Score(double claim3Score) {
        this.claim3Score = claim3Score;
    }

    public double getClaim3SEM() {
        return claim3SEM;
    }

    public void setClaim3SEM(double claim3sem) {
        claim3SEM = claim3sem;
    }

    public double getClaim4Score() {
        return claim4Score;
    }

    public void setClaim4Score(double claim4Score) {
        this.claim4Score = claim4Score;
    }

    public double getClaim4SEM() {
        return claim4SEM;
    }

    public void setClaim4SEM(double claim4sem) {
        claim4SEM = claim4sem;
    }

    @Override
    public String toString() {
        return "StudentScoreCAT [sid=" + sid + ", overallScore=" + overallScore + ", overallSEM=" + overallSEM
                + ", claim1Score=" + claim1Score + ", claim1SEM=" + claim1SEM + ", claim2Score=" + claim2Score
                + ", claim2SEM=" + claim2SEM + ", claim3Score=" + claim3Score + ", claim3SEM=" + claim3SEM
                + ", claim4Score=" + claim4Score + ", claim4SEM=" + claim4SEM + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(sid, overallScore, overallSEM,
                                claim1Score, claim1SEM,
                                claim2Score, claim2SEM,
                                claim3Score, claim3SEM,
                                claim4Score, claim4SEM
                                );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StudentScoreCAT other = (StudentScoreCAT) obj;
        if (Double.doubleToLongBits(claim1SEM) != Double.doubleToLongBits(other.claim1SEM))
            return false;
        if (Double.doubleToLongBits(claim1Score) != Double.doubleToLongBits(other.claim1Score))
            return false;
        if (Double.doubleToLongBits(claim2SEM) != Double.doubleToLongBits(other.claim2SEM))
            return false;
        if (Double.doubleToLongBits(claim2Score) != Double.doubleToLongBits(other.claim2Score))
            return false;
        if (Double.doubleToLongBits(claim3SEM) != Double.doubleToLongBits(other.claim3SEM))
            return false;
        if (Double.doubleToLongBits(claim3Score) != Double.doubleToLongBits(other.claim3Score))
            return false;
        if (Double.doubleToLongBits(claim4SEM) != Double.doubleToLongBits(other.claim4SEM))
            return false;
        if (Double.doubleToLongBits(claim4Score) != Double.doubleToLongBits(other.claim4Score))
            return false;
        if (Double.doubleToLongBits(overallSEM) != Double.doubleToLongBits(other.overallSEM))
            return false;
        if (Double.doubleToLongBits(overallScore) != Double.doubleToLongBits(other.overallScore))
            return false;
        if (sid == null) {
            if (other.sid != null)
                return false;
        } else if (!sid.equals(other.sid))
            return false;
        return true;
    }


}
