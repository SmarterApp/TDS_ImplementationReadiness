package org.readiness.dao;

import java.util.List;

import org.readiness.scoring.domain.TDSReport.Comment;
import org.readiness.scoring.domain.TDSReport.Examinee;
import org.readiness.scoring.domain.TDSReport.Opportunity;
import org.readiness.scoring.domain.TDSReport.Test;
import org.readiness.scoring.domain.TDSReport.ToolUsage;
import org.readiness.scoring.domain.TDSReport.Opportunity.Item;
import org.readiness.scoring.domain.TDSReport.Opportunity.Score;

public interface ScoringDao {
	
	Test getTest();
	
	Examinee getExaminee();
	
	Opportunity getOpportunity();
	
	List<Score> getOpportunityScores();
	
	List<Item> getOpportunityItems();
	
	List<Comment> getComments();
	
	List<ToolUsage> getToolUsage();

}
