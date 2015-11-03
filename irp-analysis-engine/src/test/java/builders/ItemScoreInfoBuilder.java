package builders;

import org.cresst.sb.irp.domain.tdsreport.ScoreInfoType;

public class ItemScoreInfoBuilder {
	
	private ScoreInfoType scoreInfoType = new ScoreInfoType();
	
	public ItemScoreInfoBuilder scorePoint(String scorePoint){
		scoreInfoType.setScorePoint(scorePoint);
		return this;
	}
	
	public ItemScoreInfoBuilder maxScore(String maxScore){
		scoreInfoType.setMaxScore(maxScore);
		return this;
	}
	
	public ItemScoreInfoBuilder scoreDimension(String scoreDimension){
		scoreInfoType.setScoreDimension(scoreDimension);
		return this;
	}
	
	public ItemScoreInfoBuilder scoreStatus(String scoreStatus){
		scoreInfoType.setScoreStatus(scoreStatus);
		return this;
	}
	
	public ScoreInfoType toScoreInfoType(){
		return scoreInfoType;
	}

}
