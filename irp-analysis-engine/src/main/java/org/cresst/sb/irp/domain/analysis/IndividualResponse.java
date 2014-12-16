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

	private List<CellCategory> listTestPropertyCategory;
	private ExamineeCategory examineeCategory;
	private List<ExamineeAttributeCategory> listExamineeAttributeCategory;
	private List<ExamineeRelationshipCategory> listExamineeRelationshipCategory;
	private OpportunityCategory opportunityCategory;
	private List<CommentCategory> coomentCategories;
	private List<ToolUsageCategory> toolUsageCategories;
	
	public IndividualResponse() {
		logger.info("initializing");
		setListTestPropertyCategory(new ArrayList<CellCategory>());
		setListExamineeAttributeCategory(new ArrayList<ExamineeAttributeCategory>());
		setListExamineeRelationshipCategory(new ArrayList<ExamineeRelationshipCategory>());
		setCoomentCategories(new ArrayList<CommentCategory>());
		setToolUsageCategories(new ArrayList<ToolUsageCategory>());
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
	
	public List<CellCategory> getListTestPropertyCategory() {
		return listTestPropertyCategory;
	}

	public void setListTestPropertyCategory(List<CellCategory> listTestPropertyCategory) {
		this.listTestPropertyCategory = listTestPropertyCategory;
	}

	
	public ExamineeCategory getExamineeCategory() {
		return examineeCategory;
	}

	public void setExamineeCategory(ExamineeCategory examineeCategory) {
		this.examineeCategory = examineeCategory;
	}

	public List<ExamineeAttributeCategory> getListExamineeAttributeCategory() {
		return listExamineeAttributeCategory;
	}

	public void setListExamineeAttributeCategory(List<ExamineeAttributeCategory> listExamineeAttributeCategory) {
		this.listExamineeAttributeCategory = listExamineeAttributeCategory;
	}
	
	public List<ExamineeRelationshipCategory> getListExamineeRelationshipCategory() {
		return listExamineeRelationshipCategory;
	}

	public void setListExamineeRelationshipCategory(List<ExamineeRelationshipCategory> listExamineeRelationshipCategory) {
		this.listExamineeRelationshipCategory = listExamineeRelationshipCategory;
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

	public List<CommentCategory> getCoomentCategories() {
		return coomentCategories;
	}

	public void setCoomentCategories(List<CommentCategory> coomentCategories) {
		this.coomentCategories = coomentCategories;
	}

	public List<ToolUsageCategory> getToolUsageCategories() {
		return toolUsageCategories;
	}

	public void setToolUsageCategories(List<ToolUsageCategory> toolUsageCategories) {
		this.toolUsageCategories = toolUsageCategories;
	}

	@Override
	public String toString() {
		return "IndividualResponse [fileName=" + fileName + ", isValidXMLfile=" + isValidXMLfile + ", tdsReport=" + tdsReport
				+ ", status=" + status + ", listTestPropertyCategory=" + listTestPropertyCategory + ", examineeCategory="
				+ examineeCategory + ", listExamineeAttributeCategory=" + listExamineeAttributeCategory
				+ ", listExamineeRelationshipCategory=" + listExamineeRelationshipCategory + ", opportunityCategory="
				+ opportunityCategory + ", coomentCategories=" + coomentCategories + ", toolUsageCategories="
				+ toolUsageCategories + "]";
	}


}
