package org.cresst.sb.irp.domain.analysis;

import java.util.List;

public class CATAnalysisResponse {
    private List<StudentScoreCAT> studentScores;
    private List<ItemResponseCAT> itemResponses;
    private List<PoolItemCAT> poolItems;


    public List<StudentScoreCAT> getStudentScores() {
        return studentScores;
    }
    public void setStudentScores(List<StudentScoreCAT> studentScores) {
        this.studentScores = studentScores;
    }
    public List<ItemResponseCAT> getItemResponses() {
        return itemResponses;
    }
    public void setItemResponses(List<ItemResponseCAT> itemResponses) {
        this.itemResponses = itemResponses;
    }
    public void setPoolItems(List<PoolItemCAT> poolItems) {
        this.poolItems = poolItems;
    }
    public List<PoolItemCAT> getPoolItems() {
        return poolItems;
    }
}
