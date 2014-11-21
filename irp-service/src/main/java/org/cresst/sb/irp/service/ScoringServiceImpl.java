package org.cresst.sb.irp.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.dao.ScoringDao;
import org.cresst.sb.irp.domain.scoring.TDSReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoringServiceImpl implements ScoringService {
	private static Logger logger = Logger.getLogger(ScoringServiceImpl.class);

	@Autowired
	private ScoringDao scoringDao;

	public ScoringServiceImpl() {

	}

	@Override
	public TDSReport.Test getTest() {
		return scoringDao.getTest();
	}

	@Override
	public TDSReport.Examinee getExaminee() {
		return scoringDao.getExaminee();
	}

	@Override
	public TDSReport.Opportunity getOpportunity() {
		return scoringDao.getOpportunity();
	}

	@Override
	public List<TDSReport.Opportunity.Score> getOpportunityScores() {
		return scoringDao.getOpportunityScores();
	}
	
	@Override
	public List<TDSReport.Opportunity.Item> getOpportunityItems() {
		return scoringDao.getOpportunityItems();
	}
	
	@Override
	public List<TDSReport.Comment> getComments() {
		return scoringDao.getComments();
	}
	

	@Override
	public List<TDSReport.ToolUsage> getToolUsage() {
		return scoringDao.getToolUsage();
	}
	
}
