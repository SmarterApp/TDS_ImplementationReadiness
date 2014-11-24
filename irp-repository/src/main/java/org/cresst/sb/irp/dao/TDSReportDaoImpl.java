package org.cresst.sb.irp.dao;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.tdsreport.ObjectFactory;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Test;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.cresst.sb.irp.utils.XMLValidate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TDSReportDaoImpl implements TDSReportDao, InitializingBean {
	private static Logger logger = Logger.getLogger(TDSReportDaoImpl.class);
	private String rootResourceFolderName = "SampleAssessmentItemPackage";
	private String testScoringFileName = "TestScoreBatching.xml"; //"sampleoutput.xml";
	private String testScoringXSDFileName = "TDSReport.xsd"; // "TestScoreBatching.xsd";
	private TDSReport tDSReport;

	@Autowired
	private XMLValidate xMLValidate;
	
	public TDSReportDaoImpl() {
		logger.info("initializing");
	}

	@Override
	public TDSReport.Test getTest() {
		TDSReport.Test test = tDSReport.getTest();
		if (test == null){
			throw new NotFoundException("Could not find Test for TDSReportDaoImpl");
		}
		return test;
	}

	@Override
	public TDSReport.Examinee getExaminee() {
		TDSReport.Examinee examinee = tDSReport.getExaminee();
		if (examinee == null){
			throw new NotFoundException("Could not find Examinee for TDSReportDaoImpl");
		}
		return examinee;
	}

	@Override
	public TDSReport.Opportunity getOpportunity() {
		TDSReport.Opportunity opportunity = tDSReport.getOpportunity();
		if (opportunity == null){
			throw new NotFoundException("Could not find Opportunity for TDSReportDaoImpl");
		}
		return opportunity;
	}
	
	@Override
	public List<TDSReport.Opportunity.Score> getOpportunityScores() {
		TDSReport.Opportunity opportunity = tDSReport.getOpportunity();
		if (opportunity == null){
			throw new NotFoundException("Could not find Opportunity for TDSReportDaoImpl");
		}
		List<TDSReport.Opportunity.Score> scoreList = opportunity.getScore();
		if (scoreList == null){
			throw new NotFoundException("Could not find List<Score> for TDSReportDaoImpl");
		}
		return scoreList;
	}	
	
	@Override
	public List<TDSReport.Opportunity.Item> getOpportunityItems() {
		TDSReport.Opportunity opportunity = tDSReport.getOpportunity();
		if (opportunity == null){
			throw new NotFoundException("Could not find Opportunity for TDSReportDaoImpl");
		}
		List<TDSReport.Opportunity.Item> itemList = opportunity.getItem();
		if (itemList == null){
			throw new NotFoundException("Could not find List<Item> for TDSReportDaoImpl");
		}
		return itemList;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			String xsdPath = rootResourceFolderName + "/" + testScoringXSDFileName;
			String xmlPath = rootResourceFolderName + "/" + testScoringFileName;
			boolean bln = xMLValidate.validateXMLSchema(xsdPath, xmlPath);
			JAXBContext ctx = JAXBContext
					.newInstance(ObjectFactory.class);
			Unmarshaller unmarshaller = ctx.createUnmarshaller();
			tDSReport = (TDSReport) unmarshaller.unmarshal(new File(
					rootResourceFolderName + "/" + testScoringFileName));
			Test test = tDSReport.getTest();
		} catch (Exception e) {
			logger.error("TDSReportDaoImpl exception: ", e);
		}
		
	}
	
	@Override
	public List<TDSReport.Comment> getComments() {
		List<TDSReport.Comment> commentList = tDSReport.getComment();
		if (commentList == null){
			throw new NotFoundException("Could not find List<Comment> for TDSReportDaoImpl");
		}
		return commentList;
	}

	@Override
	public List<TDSReport.ToolUsage> getToolUsage() {
		List<TDSReport.ToolUsage> toolUsageList = tDSReport.getToolUsage();
		if (toolUsageList == null){
			throw new NotFoundException("Could not find List<ToolUsage> for TDSReportDaoImpl");
		}
		return toolUsageList;
	}
	



	

}
