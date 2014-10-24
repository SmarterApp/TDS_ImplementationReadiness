package org.cresst.sb.irp.dao;

import org.cresst.sb.irp.domain.items.Itemrelease;
import org.cresst.sb.irp.domain.scoring.TDSReport;

import java.util.List;


public interface ScoringDao {
	
	TDSReport.Test getTest();
	
	TDSReport.Examinee getExaminee();
	
	TDSReport.Opportunity getOpportunity();
	
	List<TDSReport.Opportunity.Score> getOpportunityScores();
	
	List<TDSReport.Opportunity.Item> getOpportunityItems();
	
	List<TDSReport.Comment> getComments();
	
	List<TDSReport.ToolUsage> getToolUsage();

}
