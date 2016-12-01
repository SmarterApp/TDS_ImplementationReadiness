package org.cresst.sb.irp.cat.domain.analysis;

public interface StudentScoreCAT extends Score {
    double getOverallScore();
    
    double getOverallSEM();
    
    double getClaim1Score();
    
    double getClaim3Score();
    
    double getClaim1SEM();
    
    double getClaim3SEM();
}
