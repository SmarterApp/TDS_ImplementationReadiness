package org.cresst.sb.irp.cat.domain.analysis;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Data model for the student scores from running a CAT simulation. Data is expected to be in order listed in JsonPropertyOrder
 */
@JsonPropertyOrder({
    "sid", "overallScore","overallSEM","claim1Score","claim1SEM",
    "claim2_4Score","claim2_4SEM","claim3Score","claim3SEM"
    })
public class MathStudentScoreCAT implements Score, StudentScoreCAT {
    private String sid;
    private double overallScore;
    private double overallSEM;
    private double claim1Score;
    private double claim1SEM;
    private double claim2_4Score;
    private double claim2_4SEM;
    private double claim3Score;
    private double claim3SEM;

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

    public double getClaim2_4Score() {
        return claim2_4Score;
    }

    public void setClaim2_4Score(double claim2_4Score) {
        this.claim2_4Score = claim2_4Score;
    }

    public double getClaim2_4SEM() {
        return claim2_4SEM;
    }

    public void setClaim2_4SEM(double claim2_4sem) {
        claim2_4SEM = claim2_4sem;
    }
    
    @Override
    public String toString() {
        return "MathStudentScoreCAT [sid=" + sid + ", overallScore=" + overallScore + ", overallSEM=" + overallSEM
                + ", claim1Score=" + claim1Score + ", claim1SEM=" + claim1SEM + ", claim2_4Score=" + claim2_4Score
                + ", claim2_4SEM=" + claim2_4SEM + ", claim3Score=" + claim3Score + ", claim3SEM=" + claim3SEM + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(sid, overallScore, overallSEM,
                                claim1Score, claim1SEM,
                                claim2_4Score, claim2_4SEM,
                                claim3Score, claim3SEM
                                );
    }
}
