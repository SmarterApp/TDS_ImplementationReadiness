package org.cresst.sb.irp.domain.analysis;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;

import java.util.ArrayList;
import java.util.List;

public class IndividualResponse {

	private String fileName; // id
	private boolean isValidXMLfile;
	private TDSReport tdsReport; //
	private String status; //No errors, Contains xx errors 

	private TestPropertiesCategory testPropertiesCategory;
	private ExamineeCategory examineeCategory;
	private List<ExamineeAttributeCategory> examineeAttributeCategories = new ArrayList<>();
	private List<ExamineeRelationshipCategory> examineeRelationshipCategories = new ArrayList<>();
	private OpportunityCategory opportunityCategory;
	private List<CommentCategory> commentCategories = new ArrayList<>();
	private List<ToolUsageCategory> toolUsageCategories = new ArrayList<>();

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
	
	public TestPropertiesCategory getTestPropertiesCategory() {
		return testPropertiesCategory;
	}

	public void setTestPropertiesCategory(TestPropertiesCategory testPropertiesCategory) {
		this.testPropertiesCategory = testPropertiesCategory;
	}

	public ExamineeCategory getExamineeCategory() {
		return examineeCategory;
	}

	public void setExamineeCategory(ExamineeCategory examineeCategory) {
		this.examineeCategory = examineeCategory;
	}

	public ImmutableList<ExamineeAttributeCategory> getExamineeAttributeCategories() {
		return ImmutableList.copyOf(examineeAttributeCategories);
	}

	public ImmutableList<ExamineeRelationshipCategory> getExamineeRelationshipCategories() {
		return ImmutableList.copyOf(examineeRelationshipCategories);
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

	public List<ToolUsageCategory> getToolUsageCategories() {
		return toolUsageCategories;
	}

	public void setToolUsageCategories(List<ToolUsageCategory> toolUsageCategories) {
		this.toolUsageCategories = toolUsageCategories;
	}

	public void addExamineeAttributeCategory(ExamineeAttributeCategory examineeAttributeCategory) {
		examineeAttributeCategories.add(examineeAttributeCategory);
	}

	public void addExamineeRelationshipCategory(ExamineeRelationshipCategory examineeRelationshipCategory) {
		examineeRelationshipCategories.add(examineeRelationshipCategory);
	}

	public void addToolUsageCategory(ToolUsageCategory toolUsageCategory) {
		toolUsageCategories.add(toolUsageCategory);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("fileName", fileName)
				.append("isValidXMLfile", isValidXMLfile)
				.append("tdsReport", tdsReport)
				.append("status", status)
				.append("testPropertiesCategory", testPropertiesCategory)
				.append("examineeCategory", examineeCategory)
				.append("examineeAttributeCategories", examineeAttributeCategories)
				.append("examineeRelationshipCategories", examineeRelationshipCategories)
				.append("opportunityCategory", opportunityCategory)
				.append("commentCategories", commentCategories)
				.append("toolUsageCategories", toolUsageCategories)
				.toString();
	}
}
