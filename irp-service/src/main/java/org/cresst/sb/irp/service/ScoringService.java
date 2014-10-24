package org.cresst.sb.irp.service;

import org.cresst.sb.irp.domain.scoring.TDSReport;

import java.util.List;


public interface ScoringService {
	
	TDSReport.Test getTest();
	
	TDSReport.Examinee getExaminee();
	
	TDSReport.Opportunity getOpportunity();
	
	List<TDSReport.Opportunity.Score> getOpportunityScores();
	
	List<TDSReport.Opportunity.Item> getOpportunityItems();
	
	List<TDSReport.Comment> getComments();
	
	List<TDSReport.ToolUsage> getToolUsage();

}
