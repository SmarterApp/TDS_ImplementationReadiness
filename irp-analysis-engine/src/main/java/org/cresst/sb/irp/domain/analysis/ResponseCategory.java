package org.cresst.sb.irp.domain.analysis;

import tds.itemscoringengine.ItemScoreInfo;
import tds.itemscoringengine.ScoringStatus;

/**
 * Item scoring engine can give a score which is stored in CellCategory 
 * 		tdsExpectedValue [tdsFieldName=content, tdsFieldNameValue= <?xml version="1.0" . . . 
 * 		tdsExpectedValue=0, fieldCheckType=FieldCheckType [isFieldEmpty=false, isCorrectDataType=true,
 * 		isAcceptableValue=true, isCorrectValue=true, enumfieldCheckType=PC]]
 */
public class ResponseCategory extends Category {

	public boolean isResponseValid;
	private boolean isRubricMissing; // IRP v2 Development Schedule 13.1
	private ItemScoreInfo itemScoreInfo; // .getStatus()), .getPoints()), .getRationale().getMsg());
	private ScoringStatus scoringStatus;
	private int tdsItemScore; // TDSReport <Item score="xx">
	private String tdsFormat; // TDSReport <Item format="xx">
	private String machineRubricFileName;
	private String rubric;
	private int itemScore; //IRP item score (MC, MS) or score from itemScoreInfo.getPoints()

	public boolean isResponseValid() {
		return isResponseValid;
	}

	public void setIsResponseValid(boolean isResponseValid) {
		this.isResponseValid = isResponseValid;
	}

	public boolean isRubricMissing() {
		return isRubricMissing;
	}

	public void setRubricMissing(boolean isRubricMissing) {
		this.isRubricMissing = isRubricMissing;
	}
	
	public ItemScoreInfo getItemScoreInfo() {
		return itemScoreInfo;
	}

	public void setItemScoreInfo(ItemScoreInfo itemScoreInfo) {
		this.itemScoreInfo = itemScoreInfo;
	}

	public ScoringStatus getScoringStatus() {
		return scoringStatus;
	}

	public void setScoringStatus(ScoringStatus scoringStatus) {
		this.scoringStatus = scoringStatus;
	}

	public String getTdsFormat() {
		return tdsFormat;
	}

	public void setTdsFormat(String tdsFormat) {
		this.tdsFormat = tdsFormat;
	}

	public int getTdsItemScore() {
		return tdsItemScore;
	}

	public void setTdsItemScore(int tdsItemScore) {
		this.tdsItemScore = tdsItemScore;
	}

	public String getMachineRubricFileName() {
		return machineRubricFileName;
	}

	public void setMachineRubricFileName(String machineRubricFileName) {
		this.machineRubricFileName = machineRubricFileName;
	}

	public String getRubric() {
		return rubric;
	}

	public void setRubric(String rubric) {
		this.rubric = rubric;
	}
	
	public int getItemScore() {
		return itemScore;
	}

	public void setItemScore(int itemScore) {
		this.itemScore = itemScore;
	}

	@Override
	public String toString() {
		return "ResponseCategory [isResponseValid=" + isResponseValid + ", isRubricMissing=" + isRubricMissing + ", itemScoreInfo="
				+ itemScoreInfo + ", scoringStatus=" + scoringStatus + ", tdsItemScore=" + tdsItemScore + ", tdsFormat=" + tdsFormat
				+ ", machineRubricFileName=" + machineRubricFileName + ", rubric=" + rubric + ", itemScore=" + itemScore + "]";
	}


}
