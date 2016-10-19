package org.cresst.sb.irp.cat.domain.analysis;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"subject","grade","claim","cell","stim","min","max","weight"})
public class Blueprint {
    private String subject;
    private String grade;
    private String claim;
    private String cell;
    private String stim;
    private String min;
    private String max;
    private String weight;
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
    public String getClaim() {
        return claim;
    }
    public void setClaim(String claim) {
        this.claim = claim;
    }
    public String getCell() {
        return cell;
    }
    public void setCell(String cell) {
        this.cell = cell;
    }
    public String getStim() {
        return stim;
    }
    public void setStim(String stim) {
        this.stim = stim;
    }
    public String getMin() {
        return min;
    }
    public void setMin(String min) {
        this.min = min;
    }
    public String getMax() {
        return max;
    }
    public void setMax(String max) {
        this.max = max;
    }
    public String getWeight() {
        return weight;
    }
    public void setWeight(String weight) {
        this.weight = weight;
    }
}
