package org.cresst.sb.irp.domain.analysis;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ScoreInfoCategory extends Category {

    private ScoreRationaleCategory scoreRationaleCategory;
	private String tdsFormat; // TDSReport <Item format="xx">
	private int itemScore; //IRP item score (MC, MS) or score from itemScoreInfo.getPoints()

	public String getTdsFormat() {
		return tdsFormat;
	}

	public void setTdsFormat(String tdsFormat) {
		this.tdsFormat = tdsFormat;
	}
	
	public int getItemScore() {
		return itemScore;
	}

	public void setItemScore(int itemScore) {
		this.itemScore = itemScore;
	}

    public ScoreRationaleCategory getScoreRationaleCategory() {
        return scoreRationaleCategory;
    }

    public void setScoreRationaleCategory(ScoreRationaleCategory scoreRationaleCategory) {
        this.scoreRationaleCategory = scoreRationaleCategory;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
        	.append("tdsFieldName", tdsFormat)
        	.append("tdsFieldNameValue", itemScore)
        	.append("category", cellCategories.toArray())
            .toString();
    }

}
