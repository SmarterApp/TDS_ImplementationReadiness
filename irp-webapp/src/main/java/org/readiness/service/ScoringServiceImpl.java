package org.readiness.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.readiness.dao.ScoringDao;
import org.readiness.scoring.domain.TDSReport.Comment;
import org.readiness.scoring.domain.TDSReport.Examinee;
import org.readiness.scoring.domain.TDSReport.Opportunity;
import org.readiness.scoring.domain.TDSReport.Opportunity.Item;
import org.readiness.scoring.domain.TDSReport.Opportunity.Score;
import org.readiness.scoring.domain.TDSReport.Test;
import org.readiness.scoring.domain.TDSReport.ToolUsage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoringServiceImpl implements ScoringService {
	private static Logger logger = Logger.getLogger(ScoringServiceImpl.class);

	@Autowired
	private ScoringDao scoringDao;

	public ScoringServiceImpl() {
		logger.info("initializing");
	}

	@Override
	public Test getTest() {
		return scoringDao.getTest();
	}

	@Override
	public Examinee getExaminee() {
		logger.info("getExaminee()");
		return scoringDao.getExaminee();
	}

	@Override
	public Opportunity getOpportunity() {
		logger.info("getOpportunity()");
		return scoringDao.getOpportunity();
	}

	@Override
	public List<Score> getOpportunityScores() {
		logger.info("getOpportunityScores()");
		return scoringDao.getOpportunityScores();
	}
	
	@Override
	public List<Item> getOpportunityItems() {
		logger.info("getOpportunityScores()");
		return scoringDao.getOpportunityItems();
	}
	
	@Override
	public List<Comment> getComments() {
		logger.info("getComments()");
		return scoringDao.getComments();
	}

	@Override
	public List<ToolUsage> getToolUsage() {
		logger.info("getToolUsage()");
		return scoringDao.getToolUsage();
	}



}
