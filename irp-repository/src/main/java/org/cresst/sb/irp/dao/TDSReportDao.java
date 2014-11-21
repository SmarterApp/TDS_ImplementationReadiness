package org.cresst.sb.irp.dao;

import org.cresst.sb.irp.domain.tdsreport.TDSReport;

import java.util.List;


public interface TDSReportDao {
	
	TDSReport.Test getTest();
	
	TDSReport.Examinee getExaminee();
	
	TDSReport.Opportunity getOpportunity();
	
	List<TDSReport.Opportunity.Score> getOpportunityScores();
	
	List<TDSReport.Opportunity.Item> getOpportunityItems();
	
	List<TDSReport.Comment> getComments();
	
	List<TDSReport.ToolUsage> getToolUsage();

}
