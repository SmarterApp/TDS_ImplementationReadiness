package org.cresst.sb.irp.domain.analysis;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IndividualResponse {

    private String fileName; // id
    private boolean isValidXMLfile;
    private boolean isValidTestName; //<Test name="xxx" > contains the name does not match an IRP Test Package in irp-package/TestPackages
    private boolean isValidExaminee; //combination of <Test name="xxx" and <Esaminee key="xxxx">
    private boolean isCAT; // is CAT test or not
    private boolean hasValidScoring;
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

	public boolean isValidTestName() {
		return isValidTestName;
	}

	public void setValidTestName(boolean isValidTestName) {
		this.isValidTestName = isValidTestName;
	}
    
	public boolean isValidExaminee() {
		return isValidExaminee;
	}

	public void setValidExaminee(boolean isValidExaminee) {
		this.isValidExaminee = isValidExaminee;
	}

    public boolean hasValidScoring() {
        return hasValidScoring;
    }

    public void setHasValidScoring(boolean hasValidScoring) {
        this.hasValidScoring = hasValidScoring;
    }

    public boolean isCAT() {
		return isCAT;
	}

	public void setCAT(boolean isCAT) {
		this.isCAT = isCAT;
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
                .append("isValidTestName", isValidTestName)
                .append("isValidExaminee", isValidExaminee)
                .append("hasValidScoring", hasValidScoring)
                .append("isCAT", isCAT)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndividualResponse that = (IndividualResponse) o;
        return Objects.equals(isValidXMLfile, that.isValidXMLfile) &&
        		Objects.equals(isValidTestName, that.isValidTestName) &&
        		Objects.equals(isValidExaminee, that.isValidExaminee) &&
                Objects.equals(hasValidScoring, that.hasValidScoring) &&
        		Objects.equals(isCAT,  this.isCAT) &&
                Objects.equals(fileName, that.fileName) &&
                Objects.equals(tdsReport, that.tdsReport) &&
                Objects.equals(status, that.status) &&
                Objects.equals(testPropertiesCategory, that.testPropertiesCategory) &&
                Objects.equals(examineeCategory, that.examineeCategory) &&
                Objects.equals(examineeAttributeCategories, that.examineeAttributeCategories) &&
                Objects.equals(examineeRelationshipCategories, that.examineeRelationshipCategories) &&
                Objects.equals(opportunityCategory, that.opportunityCategory) &&
                Objects.equals(commentCategories, that.commentCategories) &&
                Objects.equals(toolUsageCategories, that.toolUsageCategories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName,
                isValidXMLfile,
                isValidTestName,
                isValidExaminee,
                hasValidScoring,
                isCAT,
                tdsReport,
                status,
                testPropertiesCategory,
                examineeCategory,
                examineeAttributeCategories,
                examineeRelationshipCategories,
                opportunityCategory,
                commentCategories,
                toolUsageCategories);
    }



}
