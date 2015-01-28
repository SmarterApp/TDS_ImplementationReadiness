package org.cresst.sb.irp.domain.studentresponse;

public class StudentResponse {

	private String testFileName; // (SBAC_PT)SBAC Math HS-MATH-10-Spring-2014-2015
	private String type; // item
	private String itemName; // item-187-174.xml
	private String bankKey; // 187
	private String id; // 174
	private String source; // 4867
	private String subject;
	private String itemType; // ER, MI
	private String lookup; // 174
	private String traningTestItem; // 2
	private long studentID; // 8599
	private String responseContent; // YES for 2 and -7; NO for -2 and 7
	private String tdsResponseContent; // corresponding actual response content in tds report xml file
	private Boolean status; // result of comparing responseContent with tdsResponseContent

	public StudentResponse() {

	}

	public String getTdsResponseContent() {
		return tdsResponseContent;
	}

	public void setTdsResponseContent(String tdsResponseContent) {
		this.tdsResponseContent = tdsResponseContent;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getTestFileName() {
		return testFileName;
	}

	public void setTestFileName(String testFileName) {
		this.testFileName = testFileName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getBankKey() {
		return bankKey;
	}

	public void setBankKey(String bankKey) {
		this.bankKey = bankKey;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getLookup() {
		return lookup;
	}

	public void setLookup(String lookup) {
		this.lookup = lookup;
	}

	public String getTraningTestItem() {
		return traningTestItem;
	}

	public void setTraningTestItem(String traningTestItem) {
		this.traningTestItem = traningTestItem;
	}

	public Long getStudentID() {
		return studentID;
	}

	public void setStudentID(Long studentID) {
		this.studentID = studentID;
	}

	public String getResponseContent() {
		return responseContent;
	}

	public void setResponseContent(String responseContent) {
		this.responseContent = responseContent;
	}

	@Override
	public String toString() {
		return "StudentResponse [testFileName=" + testFileName + ", type=" + type + ", itemName=" + itemName + ", bankKey="
				+ bankKey + ", id=" + id + ", source=" + source + ", subject=" + subject + ", itemType=" + itemType + ", lookup="
				+ lookup + ", traningTestItem=" + traningTestItem + ", studentID=" + studentID + ", responseContent="
				+ responseContent + ", tdsResponseContent=" + tdsResponseContent + ", status=" + status + "]";
	}

}
