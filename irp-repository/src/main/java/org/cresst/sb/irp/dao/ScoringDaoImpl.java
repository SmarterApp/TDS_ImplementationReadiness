package org.cresst.sb.irp.dao;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.scoring.ObjectFactory;
import org.cresst.sb.irp.domain.scoring.TDSReport;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class ScoringDaoImpl implements ScoringDao {
	private static Logger logger = Logger.getLogger(ScoringDaoImpl.class);
	private String rootResourceFolderName = "SampleAssessmentItemPackage";
	private String testScoringFileName = "sampleoutput.xml";
	private TDSReport tDSReport;

	public ScoringDaoImpl() {
		try {
			JAXBContext ctx = JAXBContext
					.newInstance(ObjectFactory.class);
			Unmarshaller unmarshaller = ctx.createUnmarshaller();
			tDSReport = (TDSReport) unmarshaller.unmarshal(new File(
					rootResourceFolderName + "/" + testScoringFileName));
			// Test test = tDSReport.getTest();
		} catch (Exception e) {
			logger.error("ScoringDaoImpl exception: ", e);
		}
	}

	@Override
	public TDSReport.Test getTest() {
		TDSReport.Test test = tDSReport.getTest();
		if (test == null){
			throw new NotFoundException("Could not find Test for ScoringDaoImpl");
		}
		return test;
	}

	@Override
	public TDSReport.Examinee getExaminee() {
		TDSReport.Examinee examinee = tDSReport.getExaminee();
		if (examinee == null){
			throw new NotFoundException("Could not find Examinee for ScoringDaoImpl");
		}
		return examinee;
	}

	@Override
	public TDSReport.Opportunity getOpportunity() {
		TDSReport.Opportunity opportunity = tDSReport.getOpportunity();
		if (opportunity == null){
			throw new NotFoundException("Could not find Opportunity for ScoringDaoImpl");
		}
		return opportunity;
	}
	
	@Override
	public List<TDSReport.Opportunity.Score> getOpportunityScores() {
		TDSReport.Opportunity opportunity = tDSReport.getOpportunity();
		if (opportunity == null){
			throw new NotFoundException("Could not find Opportunity for ScoringDaoImpl");
		}
		List<TDSReport.Opportunity.Score> scoreList = opportunity.getScore();
		if (scoreList == null){
			throw new NotFoundException("Could not find List<Score> for ScoringDaoImpl");
		}
		return scoreList;
	}	
	
	@Override
	public List<TDSReport.Opportunity.Item> getOpportunityItems() {
		TDSReport.Opportunity opportunity = tDSReport.getOpportunity();
		if (opportunity == null){
			throw new NotFoundException("Could not find Opportunity for ScoringDaoImpl");
		}
		List<TDSReport.Opportunity.Item> itemList = opportunity.getItem();
		if (itemList == null){
			throw new NotFoundException("Could not find List<Item> for ScoringDaoImpl");
		}
		return itemList;
	}

	@Override
	public List<TDSReport.Comment> getComments() {
		List<TDSReport.Comment> commentList = tDSReport.getComment();
		if (commentList == null){
			throw new NotFoundException("Could not find List<Comment> for ScoringDaoImpl");
		}
		return commentList;
	}

	@Override
	public List<TDSReport.ToolUsage> getToolUsage() {
		List<TDSReport.ToolUsage> toolUsageList = tDSReport.getToolUsage();
		if (toolUsageList == null){
			throw new NotFoundException("Could not find List<ToolUsage> for ScoringDaoImpl");
		}
		return toolUsageList;
	}



	

}
