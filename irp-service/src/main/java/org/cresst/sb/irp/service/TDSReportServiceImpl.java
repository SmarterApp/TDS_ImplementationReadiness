package org.cresst.sb.irp.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.dao.TDSReportDao;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TDSReportServiceImpl implements TDSReportService {
	private static Logger logger = Logger.getLogger(TDSReportServiceImpl.class);

	@Autowired
	private TDSReportDao tDSReportDao;

	public TDSReportServiceImpl() {
		logger.info("initializing");
	}

	@Override
	public TDSReport.Test getTest() {
		return tDSReportDao.getTest();
	}

	@Override
	public TDSReport.Examinee getExaminee() {
		return tDSReportDao.getExaminee();
	}

	@Override
	public TDSReport.Opportunity getOpportunity() {
		return tDSReportDao.getOpportunity();
	}

	@Override
	public List<TDSReport.Opportunity.Score> getOpportunityScores() {
		return tDSReportDao.getOpportunityScores();
	}

	@Override
	public List<TDSReport.Opportunity.Item> getOpportunityItems() {
		return tDSReportDao.getOpportunityItems();
	}

	@Override
	public List<TDSReport.Comment> getComments() {
		return tDSReportDao.getComments();
	}

	@Override
	public List<TDSReport.ToolUsage> getToolUsage() {
		return tDSReportDao.getToolUsage();
	}

}
