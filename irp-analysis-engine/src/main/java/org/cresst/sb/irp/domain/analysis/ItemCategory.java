package org.cresst.sb.irp.domain.analysis;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.items.Itemrelease;

public class ItemCategory {
	private static Logger logger = Logger.getLogger(ItemCategory.class);

	private Long position;
	private String segmentId;
	private Long bankKey;
	private Long key;
	private Short operational;
	private Short isSelected;
	private String format;
	private String score;
	private String scoreStatus;
	private String adminDate;
	private Long numberVisits;
	private String mimeType;
	private String clientId;
	private String strand;
	private String contentLevel;
	private Long pageNumber;
	private Long pageVisits;
	private int pageTime;
	private Short dropped;
	
	private FieldCheckType positionFieldCheckType;
	private FieldCheckType segmentIdFieldCheckType;
	private FieldCheckType bankKeyFieldCheckType;
	private FieldCheckType keyFieldCheckType;
	private FieldCheckType operationalFieldCheckType;
	private FieldCheckType isSelectedFieldCheckType;
	private FieldCheckType formatFieldCheckType;
	private FieldCheckType scoreFieldCheckType;
	private FieldCheckType scoreStatusFieldCheckType;
	private FieldCheckType adminDateFieldCheckType;
	private FieldCheckType numberVisitsFieldCheckType;
	private FieldCheckType mimeTypeFieldCheckType;
	private FieldCheckType clientIdFieldCheckType;
	private FieldCheckType strandFieldCheckType;
	private FieldCheckType contentLevelFieldCheckType;
	private FieldCheckType pageNumberFieldCheckType;
	private FieldCheckType pageVisitsFieldCheckType;
	private FieldCheckType pageTimeFieldCheckType;
	private FieldCheckType droppedFieldCheckType;
	
	private ResponseCategory responseCategory;
	private ScoreInfoCategory scoreInfoCategory; 
	private String itemBankKeyKey;
	private Itemrelease.Item.Attriblist attriblist;
	
	public ItemCategory() {
		logger.info("initializing");
	}
	
	public Long getPosition() {
		return position;
	}
	public void setPosition(Long position) {
		this.position = position;
	}
	
	public String getSegmentId() {
		return segmentId;
	}
	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}
	public Long getBankKey() {
		return bankKey;
	}
	public void setBankKey(Long bankKey) {
		this.bankKey = bankKey;
	}
	public Long  getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	public Short getOperational() {
		return operational;
	}
	public void setOperational(Short operational) {
		this.operational = operational;
	}
	public Short getIsSelected() {
		return isSelected;
	}
	public void setIsSelected(Short isSelected) {
		this.isSelected = isSelected;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getScoreStatus() {
		return scoreStatus;
	}
	public void setScoreStatus(String scoreStatus) {
		this.scoreStatus = scoreStatus;
	}
	public String getAdminDate() {
		return adminDate;
	}
	public void setAdminDate(String adminDate) {
		this.adminDate = adminDate;
	}
	public Long getNumberVisits() {
		return numberVisits;
	}
	public void setNumberVisits(Long numberVisits) {
		this.numberVisits = numberVisits;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getStrand() {
		return strand;
	}
	public void setStrand(String strand) {
		this.strand = strand;
	}
	public String getContentLevel() {
		return contentLevel;
	}
	public void setContentLevel(String contentLevel) {
		this.contentLevel = contentLevel;
	}
	public Long getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(Long pageNumber) {
		this.pageNumber = pageNumber;
	}
	public Long getPageVisits() {
		return pageVisits;
	}
	public void setPageVisits(Long pageVisits) {
		this.pageVisits = pageVisits;
	}
	public int getPageTime() {
		return pageTime;
	}
	public void setPageTime(int pageTime) {
		this.pageTime = pageTime;
	}
	public Short getDropped() {
		return dropped;
	}
	public void setDropped(Short dropped) {
		this.dropped = dropped;
	}
	
	public FieldCheckType getPositionFieldCheckType() {
		return positionFieldCheckType;
	}

	public void setPositionFieldCheckType(FieldCheckType positionFieldCheckType) {
		this.positionFieldCheckType = positionFieldCheckType;
	}

	public FieldCheckType getSegmentIdFieldCheckType() {
		return segmentIdFieldCheckType;
	}

	public void setSegmentIdFieldCheckType(FieldCheckType segmentIdFieldCheckType) {
		this.segmentIdFieldCheckType = segmentIdFieldCheckType;
	}

	public FieldCheckType getBankKeyFieldCheckType() {
		return bankKeyFieldCheckType;
	}

	public void setBankKeyFieldCheckType(FieldCheckType bankKeyFieldCheckType) {
		this.bankKeyFieldCheckType = bankKeyFieldCheckType;
	}

	public FieldCheckType getKeyFieldCheckType() {
		return keyFieldCheckType;
	}

	public void setKeyFieldCheckType(FieldCheckType keyFieldCheckType) {
		this.keyFieldCheckType = keyFieldCheckType;
	}

	public FieldCheckType getOperationalFieldCheckType() {
		return operationalFieldCheckType;
	}

	public void setOperationalFieldCheckType(FieldCheckType operationalFieldCheckType) {
		this.operationalFieldCheckType = operationalFieldCheckType;
	}

	public FieldCheckType getIsSelectedFieldCheckType() {
		return isSelectedFieldCheckType;
	}

	public void setIsSelectedFieldCheckType(FieldCheckType isSelectedFieldCheckType) {
		this.isSelectedFieldCheckType = isSelectedFieldCheckType;
	}

	public FieldCheckType getFormatFieldCheckType() {
		return formatFieldCheckType;
	}

	public void setFormatFieldCheckType(FieldCheckType formatFieldCheckType) {
		this.formatFieldCheckType = formatFieldCheckType;
	}

	public FieldCheckType getScoreFieldCheckType() {
		return scoreFieldCheckType;
	}

	public void setScoreFieldCheckType(FieldCheckType scoreFieldCheckType) {
		this.scoreFieldCheckType = scoreFieldCheckType;
	}

	public FieldCheckType getScoreStatusFieldCheckType() {
		return scoreStatusFieldCheckType;
	}

	public void setScoreStatusFieldCheckType(FieldCheckType scoreStatusFieldCheckType) {
		this.scoreStatusFieldCheckType = scoreStatusFieldCheckType;
	}

	public FieldCheckType getAdminDateFieldCheckType() {
		return adminDateFieldCheckType;
	}

	public void setAdminDateFieldCheckType(FieldCheckType adminDateFieldCheckType) {
		this.adminDateFieldCheckType = adminDateFieldCheckType;
	}

	public FieldCheckType getNumberVisitsFieldCheckType() {
		return numberVisitsFieldCheckType;
	}

	public void setNumberVisitsFieldCheckType(FieldCheckType numberVisitsFieldCheckType) {
		this.numberVisitsFieldCheckType = numberVisitsFieldCheckType;
	}

	public FieldCheckType getMimeTypeFieldCheckType() {
		return mimeTypeFieldCheckType;
	}

	public void setMimeTypeFieldCheckType(FieldCheckType mimeTypeFieldCheckType) {
		this.mimeTypeFieldCheckType = mimeTypeFieldCheckType;
	}

	public FieldCheckType getClientIdFieldCheckType() {
		return clientIdFieldCheckType;
	}

	public void setClientIdFieldCheckType(FieldCheckType clientIdFieldCheckType) {
		this.clientIdFieldCheckType = clientIdFieldCheckType;
	}

	public FieldCheckType getStrandFieldCheckType() {
		return strandFieldCheckType;
	}

	public void setStrandFieldCheckType(FieldCheckType strandFieldCheckType) {
		this.strandFieldCheckType = strandFieldCheckType;
	}

	public FieldCheckType getContentLevelFieldCheckType() {
		return contentLevelFieldCheckType;
	}

	public void setContentLevelFieldCheckType(FieldCheckType contentLevelFieldCheckType) {
		this.contentLevelFieldCheckType = contentLevelFieldCheckType;
	}

	public FieldCheckType getPageNumberFieldCheckType() {
		return pageNumberFieldCheckType;
	}

	public void setPageNumberFieldCheckType(FieldCheckType pageNumberFieldCheckType) {
		this.pageNumberFieldCheckType = pageNumberFieldCheckType;
	}

	public FieldCheckType getPageVisitsFieldCheckType() {
		return pageVisitsFieldCheckType;
	}

	public void setPageVisitsFieldCheckType(FieldCheckType pageVisitsFieldCheckType) {
		this.pageVisitsFieldCheckType = pageVisitsFieldCheckType;
	}

	public FieldCheckType getPageTimeFieldCheckType() {
		return pageTimeFieldCheckType;
	}

	public void setPageTimeFieldCheckType(FieldCheckType pageTimeFieldCheckType) {
		this.pageTimeFieldCheckType = pageTimeFieldCheckType;
	}

	public FieldCheckType getDroppedFieldCheckType() {
		return droppedFieldCheckType;
	}

	public void setDroppedFieldCheckType(FieldCheckType droppedFieldCheckType) {
		this.droppedFieldCheckType = droppedFieldCheckType;
	}
	
	public ResponseCategory getResponseCategory() {
		return responseCategory;
	}

	public void setResponseCategory(ResponseCategory responseCategory) {
		this.responseCategory = responseCategory;
	}

	public ScoreInfoCategory getScoreInfoCategory() {
		return scoreInfoCategory;
	}

	public void setScoreInfoCategory(ScoreInfoCategory scoreInfoCategory) {
		this.scoreInfoCategory = scoreInfoCategory;
	}

	public String getItemBankKeyKey() {
		return itemBankKeyKey;
	}

	public void setItemBankKeyKey(String itemBankKeyKey) {
		this.itemBankKeyKey = itemBankKeyKey;
	}

	public Itemrelease.Item.Attriblist getAttriblist() {
		return attriblist;
	}

	public void setAttriblist(Itemrelease.Item.Attriblist attriblist) {
		this.attriblist = attriblist;
	}

	@Override
	public String toString() {
		return "ItemCategory [position=" + position + ", segmentId=" + segmentId + ", bankKey=" + bankKey + ", key=" + key
				+ ", operational=" + operational + ", isSelected=" + isSelected + ", format=" + format + ", score=" + score
				+ ", scoreStatus=" + scoreStatus + ", adminDate=" + adminDate + ", numberVisits=" + numberVisits + ", mimeType="
				+ mimeType + ", clientId=" + clientId + ", strand=" + strand + ", contentLevel=" + contentLevel + ", pageNumber="
				+ pageNumber + ", pageVisits=" + pageVisits + ", pageTime=" + pageTime + ", dropped=" + dropped
				+ ", positionFieldCheckType=" + positionFieldCheckType + ", segmentIdFieldCheckType=" + segmentIdFieldCheckType
				+ ", bankKeyFieldCheckType=" + bankKeyFieldCheckType + ", keyFieldCheckType=" + keyFieldCheckType
				+ ", operationalFieldCheckType=" + operationalFieldCheckType + ", isSelectedFieldCheckType="
				+ isSelectedFieldCheckType + ", formatFieldCheckType=" + formatFieldCheckType + ", scoreFieldCheckType="
				+ scoreFieldCheckType + ", scoreStatusFieldCheckType=" + scoreStatusFieldCheckType + ", adminDateFieldCheckType="
				+ adminDateFieldCheckType + ", numberVisitsFieldCheckType=" + numberVisitsFieldCheckType
				+ ", mimeTypeFieldCheckType=" + mimeTypeFieldCheckType + ", clientIdFieldCheckType=" + clientIdFieldCheckType
				+ ", strandFieldCheckType=" + strandFieldCheckType + ", contentLevelFieldCheckType=" + contentLevelFieldCheckType
				+ ", pageNumberFieldCheckType=" + pageNumberFieldCheckType + ", pageVisitsFieldCheckType="
				+ pageVisitsFieldCheckType + ", pageTimeFieldCheckType=" + pageTimeFieldCheckType + ", droppedFieldCheckType="
				+ droppedFieldCheckType + ", responseCategory=" + responseCategory + ", scoreInfoCategory=" + scoreInfoCategory
				+ ", itemBankKeyKey=" + itemBankKeyKey + ", attriblist=" + attriblist + "]";
	}


}
