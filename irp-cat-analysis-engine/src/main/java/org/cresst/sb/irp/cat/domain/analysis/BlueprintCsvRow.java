package org.cresst.sb.irp.cat.domain.analysis;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

// Represent the blueprint csv
@JsonPropertyOrder({ "description", "subject", "grade", "claim", "min", "max", "target", "dok", "dokGte", "passage",
        "shortAnswer", "stim", "weight" })
public class BlueprintCsvRow {
    private String description;
    private String subject;
    private int grade;
    private int claim;
    private int min;
    private int max;
    // Can have multiple targets, handle as String
    private String target;
    private String dok;
    // dok>= some value
    private int dokGte;
    private String passage;
    private int shortAnswer;
    private String stim;
    private String weight;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getClaim() {
        return claim;
    }

    public void setClaim(int claim) {
        this.claim = claim;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getDok() {
        return dok;
    }

    public void setDok(String dok) {
        this.dok = dok;
    }

    public int getDokGte() {
        return dokGte;
    }

    public void setDokGte(int dokGte) {
        this.dokGte = dokGte;
    }

    public String getPassage() {
        return passage;
    }

    public void setPassage(String passage) {
        this.passage = passage;
    }

    public int getShortAnswer() {
        return shortAnswer;
    }

    public void setShortAnswer(int shortAnswer) {
        this.shortAnswer = shortAnswer;
    }

    public String getStim() {
        return stim;
    }

    public void setStim(String stim) {
        this.stim = stim;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
