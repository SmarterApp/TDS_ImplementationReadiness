package org.cresst.sb.irp.cat.domain.analysis;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Threshold levels from Smarter Balanced Scoring Specification
 */
@JsonPropertyOrder({"subject","grade","theta_1_2","ss_1_2", "theta_2_3", "ss_2_3", "theta_3_4", "ss_3_4"})
public class ThresholdLevels {
    private String subject;
    private String grade;
    private double theta_1_2;
    private double ss_1_2;
    private double theta_2_3;
    private double ss_2_3;
    private double theta_3_4;
    private double ss_3_4;
    
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getGrade() {
        return grade;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }
    public double getTheta_1_2() {
        return theta_1_2;
    }
    public void setTheta_1_2(double theta_1_2) {
        this.theta_1_2 = theta_1_2;
    }
    public double getSs_1_2() {
        return ss_1_2;
    }
    public void setSs_1_2(double ss_1_2) {
        this.ss_1_2 = ss_1_2;
    }
    public double getTheta_2_3() {
        return theta_2_3;
    }
    public void setTheta_2_3(double theta_2_3) {
        this.theta_2_3 = theta_2_3;
    }
    public double getSs_2_3() {
        return ss_2_3;
    }
    public void setSs_2_3(double ss_2_3) {
        this.ss_2_3 = ss_2_3;
    }
    public double getTheta_3_4() {
        return theta_3_4;
    }
    public void setTheta_3_4(double theta_3_4) {
        this.theta_3_4 = theta_3_4;
    }
    public double getSs_3_4() {
        return ss_3_4;
    }
    public void setSs_3_4(double ss_3_4) {
        this.ss_3_4 = ss_3_4;
    }
}
