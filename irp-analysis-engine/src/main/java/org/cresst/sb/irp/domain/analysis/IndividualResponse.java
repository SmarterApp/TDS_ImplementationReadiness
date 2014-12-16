package org.cresst.sb.irp.domain.analysis;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;

public class IndividualResponse {
	private static Logger logger = Logger.getLogger(IndividualResponse.class);

	private String fileName; // id
	private boolean isValidXMLfile;
	private TDSReport tdsReport; //
	private String status; //No errors, Contains xx errors 

	private List<CellCategory> testPropertyCategories;
	private ExamineeCategory examineeCategory;
	private List<ExamineeAttributeCategory> examineeAttributeCategories;
	private List<ExamineeRelationshipCategory> examineeRelationshipCategories;
	private OpportunityCategory opportunityCategory;
	private List<CommentCategory> commentCategories;
	
	
	public IndividualResponse() {
		logger.info("initializing");
		setTestPropertyCategories(new ArrayList<CellCategory>());
		setExamineeAttributeCategories(new ArrayList<ExamineeAttributeCategory>());
		setExamineeRelationshipCategories(new ArrayList<ExamineeRelationshipCategory>());
		setCommentCategories(new ArrayList<CommentCategory>());
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public TDSReport getTDSReport() {
		return tdsReport;
	}

	public void setTDSReport(TDSReport tdsReport) {
		this.tdsReport = tdsReport;
	}

	public boolean isValidXMLfile() {
		return isValidXMLfile;
	}

	public void setValidXMLfile(boolean isValidXMLfile) {
		this.isValidXMLfile = isValidXMLfile;
	}
	
	public List<CellCategory> getTestPropertyCategories() {
		return testPropertyCategories;
	}

	public void setTestPropertyCategories(List<CellCategory> testPropertyCategories) {
		this.testPropertyCategories = testPropertyCategories;
	}

	
	public ExamineeCategory getExamineeCategory() {
		return examineeCategory;
	}

	public void setExamineeCategory(ExamineeCategory examineeCategory) {
		this.examineeCategory = examineeCategory;
	}

	public List<ExamineeAttributeCategory> getExamineeAttributeCategories() {
		return examineeAttributeCategories;
	}

	public void setExamineeAttributeCategories(List<ExamineeAttributeCategory> examineeAttributeCategories) {
		this.examineeAttributeCategories = examineeAttributeCategories;
	}
	
	public List<ExamineeRelationshipCategory> getExamineeRelationshipCategories() {
		return examineeRelationshipCategories;
	}

	public void setExamineeRelationshipCategories(List<ExamineeRelationshipCategory> examineeRelationshipCategories) {
		this.examineeRelationshipCategories = examineeRelationshipCategories;
	}
	
	public OpportunityCategory getOpportunityCategory() {
		return opportunityCategory;
	}

	public void setOpportunityCategory(OpportunityCategory opportunityCategory) {
		this.opportunityCategory = opportunityCategory;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<CommentCategory> getCommentCategories() {
		return commentCategories;
	}

	public void setCommentCategories(List<CommentCategory> commentCategories) {
		this.commentCategories = commentCategories;
	}

	@Override
	public String toString() {
		return "IndividualResponse [fileName=" + fileName + ", isValidXMLfile=" + isValidXMLfile + ", tdsReport=" + tdsReport
				+ ", status=" + status + ", testPropertyCategories=" + testPropertyCategories + ", examineeCategory="
				+ examineeCategory + ", examineeAttributeCategories=" + examineeAttributeCategories
				+ ", examineeRelationshipCategories=" + examineeRelationshipCategories + ", opportunityCategory="
				+ opportunityCategory + ", commentCategories=" + commentCategories + "]";
	}


}
