package org.cresst.sb.irp.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee.ExamineeAttribute;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Examinee.ExamineeRelationship;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Repository;

@Repository
public class TDSReportDaoImpl implements TDSReportDao {
	private static Logger logger = Logger.getLogger(TDSReportDaoImpl.class);
	private TDSReport tdsReport;

	@Value("classpath:TestScoreBatching.xml")
	private Resource tdsReportResource;

	@Autowired
	private Unmarshaller unmarshaller;

	public TDSReportDaoImpl() {
		logger.info("initializing");
	}

	@Override
	public TDSReport.Test getTest() {
		TDSReport.Test test = tdsReport.getTest();
		if (test == null) {
			throw new NotFoundException("Could not find Test for TDSReportDaoImpl");
		}
		return test;
	}

	@Override
	public TDSReport.Examinee getExaminee() {
		Examinee examinee = tdsReport.getExaminee();
		if (examinee == null) {
			throw new NotFoundException("Could not find Examinee for TDSReportDaoImpl");
		}
		return examinee;
	}

	@Override
	public List<ExamineeAttribute> getExamineeAttributes(Examinee examinee) {
		List<ExamineeAttribute> listExamineeAttribute = new ArrayList<ExamineeAttribute>();
		try {
			if (examinee != null) {
				// ExamineeAttributes and ExamineeRelationships
				List<Object> listObject = examinee.getExamineeAttributeOrExamineeRelationship();
				for (Object o : listObject) {
					if (o instanceof ExamineeAttribute) {
						listExamineeAttribute.add((ExamineeAttribute) o);
					}
				}
			}
		} catch (Exception e) {
			logger.error("getExamineeAttributes exception: ", e);
		}
		return listExamineeAttribute;
	}

	@Override
	public List<ExamineeRelationship> getExamineeRelationships(Examinee examinee) {
		List<ExamineeRelationship> listExamineeRelationship = new ArrayList<ExamineeRelationship>();
		try {
			if (examinee != null) {
				// ExamineeAttributes and ExamineeRelationships
				List<Object> listObject = examinee.getExamineeAttributeOrExamineeRelationship();
				for (Object o : listObject) {
					if (o instanceof ExamineeRelationship) {
						listExamineeRelationship.add((ExamineeRelationship) o);
					}
				}
			}
		} catch (Exception e) {
			logger.error("getExamineeRelationships exception: ", e);
		}
		return listExamineeRelationship;
	}
	
	@Override
	public TDSReport.Opportunity getOpportunity() {
		TDSReport.Opportunity opportunity = tdsReport.getOpportunity();
		if (opportunity == null) {
			throw new NotFoundException("Could not find Opportunity for TDSReportDaoImpl");
		}
		return opportunity;
	}

	@Override
	public List<TDSReport.Opportunity.Score> getOpportunityScores() {
		TDSReport.Opportunity opportunity = tdsReport.getOpportunity();
		if (opportunity == null) {
			throw new NotFoundException("Could not find Opportunity for TDSReportDaoImpl");
		}
		List<TDSReport.Opportunity.Score> scoreList = opportunity.getScore();
		if (scoreList == null) {
			throw new NotFoundException("Could not find List<Score> for TDSReportDaoImpl");
		}
		return scoreList;
	}

	@Override
	public List<TDSReport.Opportunity.Item> getOpportunityItems() {
		TDSReport.Opportunity opportunity = tdsReport.getOpportunity();
		if (opportunity == null) {
			throw new NotFoundException("Could not find Opportunity for TDSReportDaoImpl");
		}
		List<TDSReport.Opportunity.Item> itemList = opportunity.getItem();
		if (itemList == null) {
			throw new NotFoundException("Could not find List<Item> for TDSReportDaoImpl");
		}
		return itemList;
	}

	@Override
	public List<TDSReport.Comment> getComments() {
		List<TDSReport.Comment> commentList = tdsReport.getComment();
		if (commentList == null) {
			throw new NotFoundException("Could not find List<Comment> for TDSReportDaoImpl");
		}
		return commentList;
	}

	@Override
	public List<TDSReport.ToolUsage> getToolUsage() {
		List<TDSReport.ToolUsage> toolUsageList = tdsReport.getToolUsage();
		if (toolUsageList == null) {
			throw new NotFoundException("Could not find List<ToolUsage> for TDSReportDaoImpl");
		}
		return toolUsageList;
	}

	@PostConstruct
	public void loadData() throws Exception {
		try {
			tdsReport = (TDSReport) unmarshaller.unmarshal(new StreamSource(tdsReportResource.getInputStream()));
		} catch (Exception e) {
			logger.error("TDSReportDaoImpl exception: ", e);
		}

	}



}
