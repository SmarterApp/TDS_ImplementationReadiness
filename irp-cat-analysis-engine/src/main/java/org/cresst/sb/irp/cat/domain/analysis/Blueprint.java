package org.cresst.sb.irp.cat.domain.analysis;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"grade","claim","cell","notes","stim","selectStatement","min","max","weight","comment"})
public class Blueprint {
    private String grade;
    private String claim;
    private String cell;
    private String notes;
    private String stim;
    private String selectStatement;
    private String min;
    private String max;
    private String weight;
    private String comment;
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
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public String getStim() {
        return stim;
    }
    public void setStim(String stim) {
        this.stim = stim;
    }
    public String getSelectStatement() {
        return selectStatement;
    }
    public void setSelectStatement(String selectStatement) {
        this.selectStatement = selectStatement;
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
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    @Override
    public String toString() {
        return "Blueprint [grade=" + grade + ", claim=" + claim + ", cell=" + cell + ", notes=" + notes + ", stim="
                + stim + ", selectStatement=" + selectStatement + ", min=" + min + ", max=" + max + ", weight=" + weight
                + ", comment=" + comment + "]";
    }
    @Override
    public int hashCode() {
        return Objects.hash(grade, claim, cell, notes, stim, selectStatement, min, max, weight, comment);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Blueprint other = (Blueprint) obj;
        if (cell == null) {
            if (other.cell != null)
                return false;
        } else if (!cell.equals(other.cell))
            return false;
        if (claim == null) {
            if (other.claim != null)
                return false;
        } else if (!claim.equals(other.claim))
            return false;
        if (comment == null) {
            if (other.comment != null)
                return false;
        } else if (!comment.equals(other.comment))
            return false;
        if (grade == null) {
            if (other.grade != null)
                return false;
        } else if (!grade.equals(other.grade))
            return false;
        if (max == null) {
            if (other.max != null)
                return false;
        } else if (!max.equals(other.max))
            return false;
        if (min == null) {
            if (other.min != null)
                return false;
        } else if (!min.equals(other.min))
            return false;
        if (notes == null) {
            if (other.notes != null)
                return false;
        } else if (!notes.equals(other.notes))
            return false;
        if (selectStatement == null) {
            if (other.selectStatement != null)
                return false;
        } else if (!selectStatement.equals(other.selectStatement))
            return false;
        if (stim == null) {
            if (other.stim != null)
                return false;
        } else if (!stim.equals(other.stim))
            return false;
        if (weight == null) {
            if (other.weight != null)
                return false;
        } else if (!weight.equals(other.weight))
            return false;
        return true;
    }

}
