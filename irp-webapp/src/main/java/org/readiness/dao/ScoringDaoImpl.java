package org.readiness.dao;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.readiness.exceptions.NotFoundException;
import org.readiness.scoring.domain.TDSReport;
import org.readiness.scoring.domain.TDSReport.Comment;
import org.readiness.scoring.domain.TDSReport.Examinee;
import org.readiness.scoring.domain.TDSReport.Opportunity;
import org.readiness.scoring.domain.TDSReport.Opportunity.Item;
import org.readiness.scoring.domain.TDSReport.Opportunity.Score;
import org.readiness.scoring.domain.TDSReport.Test;
import org.readiness.scoring.domain.TDSReport.ToolUsage;
import org.springframework.stereotype.Repository;

@Repository
public class ScoringDaoImpl implements ScoringDao {
	private static Logger logger = Logger.getLogger(ScoringDaoImpl.class);
	private String rootResourceFolderName = "SampleAssessmentItemPackage";
	private String testScoringFileName = "sampleoutput.xml";
	private TDSReport tDSReport;

	public ScoringDaoImpl() {
		logger.info("initializing");

		try {
			JAXBContext ctx = JAXBContext
					.newInstance(org.readiness.scoring.domain.ObjectFactory.class);
			Unmarshaller unmarshaller = ctx.createUnmarshaller();
			tDSReport = (TDSReport) unmarshaller.unmarshal(new File(
					rootResourceFolderName + "/" + testScoringFileName));
			// Test test = tDSReport.getTest();
		} catch (Exception e) {
			logger.info("ScoringDaoImpl exception: " + e);
			System.out
					.println("ScoringDaoImpl.ScoringDaoImpl() Exception thrown  :"
							+ e);
			e.printStackTrace();
		}
	}

	@Override
	public Test getTest() {
		logger.info("getTest()");  
		Test test = tDSReport.getTest();
		if (test == null){
			throw new NotFoundException("Could not find Test for ScoringDaoImpl");
		}
		return test;
	}

	@Override
	public Examinee getExaminee() {
		logger.info("getExaminee()");  
		Examinee examinee = tDSReport.getExaminee();
		if (examinee == null){
			throw new NotFoundException("Could not find Examinee for ScoringDaoImpl");
		}
		return examinee;
	}

	@Override
	public Opportunity getOpportunity() {
		logger.info("getOpportunity()");  
		Opportunity opportunity = tDSReport.getOpportunity();
		if (opportunity == null){
			throw new NotFoundException("Could not find Opportunity for ScoringDaoImpl");
		}
		return opportunity;
	}
	
	@Override
	public List<Score> getOpportunityScores() {
		logger.info("getOpportunityScores()");  
		Opportunity opportunity = tDSReport.getOpportunity();
		if (opportunity == null){
			throw new NotFoundException("Could not find Opportunity for ScoringDaoImpl");
		}
		List<Score> scoreList = opportunity.getScore();
		if (scoreList == null){
			throw new NotFoundException("Could not find List<Score> for ScoringDaoImpl");
		}
		return scoreList;
	}	
	
	@Override
	public List<Item> getOpportunityItems() {
		logger.info("getOpportunityItems()");  
		Opportunity opportunity = tDSReport.getOpportunity();
		if (opportunity == null){
			throw new NotFoundException("Could not find Opportunity for ScoringDaoImpl");
		}
		List<Item> itemList = opportunity.getItem();
		if (itemList == null){
			throw new NotFoundException("Could not find List<Item> for ScoringDaoImpl");
		}
		return itemList;
	}

	@Override
	public List<Comment> getComments() {
		logger.info("getComments()");  
		List<Comment> commentList = tDSReport.getComment();
		if (commentList == null){
			throw new NotFoundException("Could not find List<Comment> for ScoringDaoImpl");
		}
		return commentList;
	}

	@Override
	public List<ToolUsage> getToolUsage() {
		logger.info("getToolUsage()");  
		List<ToolUsage> toolUsageList = tDSReport.getToolUsage();
		if (toolUsageList == null){
			throw new NotFoundException("Could not find List<ToolUsage> for ScoringDaoImpl");
		}
		return toolUsageList;
	}



	

}
