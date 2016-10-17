package org.cresst.sb.irp.domain.analysis;

import java.util.List;

public class CATAnalysisResponse {
    private List<StudentScoreCAT> studentScores;
    private List<ItemResponseCAT> itemResponses;

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
}
