package org.cresst.sb.irp.domain.analysis;

import tds.itemscoringengine.ItemScoreInfo;

public class ResponseCategory extends Category {

	public boolean isResponseValid;
	private ItemScoreInfo itemScoreInfo; //	itemScoreInfo.getStatus()), itemScoreInfo.getPoints()), itemScoreInfo.getRationale().getMsg());

	public ItemScoreInfo getItemScoreInfo() {
		return itemScoreInfo;
	}

	public void setItemScoreInfo(ItemScoreInfo itemScoreInfo) {
		this.itemScoreInfo = itemScoreInfo;
	}

	public boolean isResponseValid() {
		return isResponseValid;
	}

	public void setIsResponseValid(boolean isResponseValid) {
		this.isResponseValid = isResponseValid;
	}

	@Override
	public String toString() {
		return "ResponseCategory [isResponseValid=" + isResponseValid + ", itemScoreInfo=" + itemScoreInfo + "]";
	}

	

}
