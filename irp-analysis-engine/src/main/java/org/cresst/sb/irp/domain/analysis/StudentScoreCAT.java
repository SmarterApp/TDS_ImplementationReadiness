package org.cresst.sb.irp.domain.analysis;

public class StudentScoreCAT {
    private String sId;
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

    public StudentScoreCAT(String sId, double overallScore, double overallSEM, double claim1Score, double claim1sem,
            double claim2Score, double claim2sem, double claim3Score, double claim3sem, double claim4Score,
            double claim4sem) {
        this.sId = sId;
        this.overallScore = overallScore;
        this.overallSEM = overallSEM;
        this.claim1Score = claim1Score;
        claim1SEM = claim1sem;
        this.claim2Score = claim2Score;
        claim2SEM = claim2sem;
        this.claim3Score = claim3Score;
        claim3SEM = claim3sem;
        this.claim4Score = claim4Score;
        claim4SEM = claim4sem;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
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
}
