package org.cresst.sb.irp.cat.domain.analysis;

import java.util.List;

public class CATDataModel {
    private List<? extends StudentScoreCAT> studentScores;
    private List<ItemResponseCAT> itemResponses;
    private List<PoolItem> poolItems;
    private List<TrueTheta> trueThetas;
    private int grade;
    private String subject;
    
    public List<? extends StudentScoreCAT> getStudentScores() {
        return studentScores;
    }
    public void setStudentScores(List<? extends StudentScoreCAT> studentScores2) {
        this.studentScores = studentScores2;
    }
    public List<ItemResponseCAT> getItemResponses() {
        return itemResponses;
    }
    public void setItemResponses(List<ItemResponseCAT> itemResponses) {
        this.itemResponses = itemResponses;
    }
    public void setPoolItems(List<PoolItem> poolItems) {
        this.poolItems = poolItems;
    }
    public List<PoolItem> getPoolItems() {
        return poolItems;
    }
    public List<TrueTheta> getTrueThetas() {
        return trueThetas;
    }
    public void setTrueThetas(List<TrueTheta> trueThetas) {
        this.trueThetas = trueThetas;
    }
    public int getGrade() {
        return grade;
    }
    public void setGrade(int grade) {
        this.grade = grade;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
}
