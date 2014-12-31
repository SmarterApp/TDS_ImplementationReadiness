package org.cresst.sb.irp.domain.analysis;

public class ScoreInfoCategory {

	private String scorePoint;
	private String maxScore;
	private String scoreDimension;
	private String scoreStatus;
	private String confLevel;
	
	private FieldCheckType scorePointFieldCheckType;
	private FieldCheckType maxScoreFieldCheckType;
	private FieldCheckType scoreDimensionFieldCheckType;
	private FieldCheckType scoreStatusFieldCheckType;
	private FieldCheckType confLevelFieldCheckType;
	
	private ScoreRationaleCategory scoreRationaleCategory;
	
	public ScoreRationaleCategory getScoreRationaleCategory() {
		return scoreRationaleCategory;
	}

	public void setScoreRationaleCategory(ScoreRationaleCategory scoreRationaleCategory) {
		this.scoreRationaleCategory = scoreRationaleCategory;
	}

	public String getScorePoint() {
		return scorePoint;
	}

	public void setScorePoint(String scorePoint) {
		this.scorePoint = scorePoint;
	}

	public String getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(String maxScore) {
		this.maxScore = maxScore;
	}

	public String getScoreDimension() {
		return scoreDimension;
	}

	public void setScoreDimension(String scoreDimension) {
		this.scoreDimension = scoreDimension;
	}

	public String getScoreStatus() {
		return scoreStatus;
	}

	public void setScoreStatus(String scoreStatus) {
		this.scoreStatus = scoreStatus;
	}

	public String getConfLevel() {
		return confLevel;
	}

	public void setConfLevel(String confLevel) {
		this.confLevel = confLevel;
	}

	public FieldCheckType getScorePointFieldCheckType() {
		return scorePointFieldCheckType;
	}

	public void setScorePointFieldCheckType(FieldCheckType scorePointFieldCheckType) {
		this.scorePointFieldCheckType = scorePointFieldCheckType;
	}

	public FieldCheckType getMaxScoreFieldCheckType() {
		return maxScoreFieldCheckType;
	}

	public void setMaxScoreFieldCheckType(FieldCheckType maxScoreFieldCheckType) {
		this.maxScoreFieldCheckType = maxScoreFieldCheckType;
	}

	public FieldCheckType getScoreDimensionFieldCheckType() {
		return scoreDimensionFieldCheckType;
	}

	public void setScoreDimensionFieldCheckType(FieldCheckType scoreDimensionFieldCheckType) {
		this.scoreDimensionFieldCheckType = scoreDimensionFieldCheckType;
	}

	public FieldCheckType getScoreStatusFieldCheckType() {
		return scoreStatusFieldCheckType;
	}

	public void setScoreStatusFieldCheckType(FieldCheckType scoreStatusFieldCheckType) {
		this.scoreStatusFieldCheckType = scoreStatusFieldCheckType;
	}

	public FieldCheckType getConfLevelFieldCheckType() {
		return confLevelFieldCheckType;
	}

	public void setConfLevelFieldCheckType(FieldCheckType confLevelFieldCheckType) {
		this.confLevelFieldCheckType = confLevelFieldCheckType;
	}

	@Override
	public String toString() {
		return "ScoreInfoCategory [scorePoint=" + scorePoint + ", maxScore=" + maxScore + ", scoreDimension=" + scoreDimension
				+ ", scoreStatus=" + scoreStatus + ", confLevel=" + confLevel + ", scorePointFieldCheckType="
				+ scorePointFieldCheckType + ", maxScoreFieldCheckType=" + maxScoreFieldCheckType
				+ ", scoreDimensionFieldCheckType=" + scoreDimensionFieldCheckType + ", scoreStatusFieldCheckType="
				+ scoreStatusFieldCheckType + ", confLevelFieldCheckType=" + confLevelFieldCheckType
				+ ", scoreRationaleCategory=" + scoreRationaleCategory + "]";
	}
}
