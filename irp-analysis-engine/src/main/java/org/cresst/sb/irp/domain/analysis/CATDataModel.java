package org.cresst.sb.irp.domain.analysis;

import java.util.List;

public class CATDataModel {
    private List<StudentScoreCAT> studentScores;
    private List<ItemResponseCAT> itemResponses;
    private List<PoolItemCAT> poolItems;
    private List<TrueTheta> trueThetas;
    private List<Blueprint> blueprints;

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
    public List<TrueTheta> getTrueThetas() {
        return trueThetas;
    }
    public void setTrueThetas(List<TrueTheta> trueThetas) {
        this.trueThetas = trueThetas;
    }
    public List<Blueprint> getBlueprints() {
        return blueprints;
    }
    public void setBlueprints(List<Blueprint> blueprints) {
        this.blueprints = blueprints;
    }

}
