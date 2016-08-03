package org.cresst.sb.irp.auto.tsb;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

class TestSpecBankData {
    private String id;
    private String specificationXml;
    private String tenantId;
    private String itemBank;
    private String category;
    private String subjectName;
    private String subjectAbbreviation;
    private String name;
    private List<String> grade;
    private String label;
    private String purpose;
    private String type;
    private String version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpecificationXml() {
        return specificationXml;
    }

    public void setSpecificationXml(String specificationXml) {
        this.specificationXml = specificationXml;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getItemBank() {
        return itemBank;
    }

    public void setItemBank(String itemBank) {
        this.itemBank = itemBank;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectAbbreviation() {
        return subjectAbbreviation;
    }

    public void setSubjectAbbreviation(String subjectAbbreviation) {
        this.subjectAbbreviation = subjectAbbreviation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getGrade() {
        return grade;
    }

    public void setGrade(List<String> grade) {
        this.grade = grade;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("specificationXml", specificationXml)
                .append("tenantId", tenantId)
                .append("itemBank", itemBank)
                .append("category", category)
                .append("subjectName", subjectName)
                .append("subjectAbbreviation", subjectAbbreviation)
                .append("name", name)
                .append("grade", grade)
                .append("label", label)
                .append("purpose", purpose)
                .append("type", type)
                .append("version", version)
                .toString();
    }
}