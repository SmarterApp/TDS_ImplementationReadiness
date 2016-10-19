package org.cresst.sb.irp.domain.analysis;

import com.google.common.collect.ImmutableList;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class OpportunityCategory extends Category {

    private List<SegmentCategory> segmentCategories = new ArrayList<>();
    private List<AccommodationCategory> accommodationCategories = new ArrayList<>();
    private List<ScoreCategory> scoreCategories = new ArrayList<>();
    private List<GenericVariableCategory> genericVariableCategories = new ArrayList<>();
    private List<ItemCategory> itemCategories = new ArrayList<>();

    private OpItemsData opItemsData = new OpItemsData();
    private TdsReportScoreIrpScoredScore tdsReportScoreIrpScoredScore = new TdsReportScoreIrpScoredScore();

    // Returns true if all categories in list are true
    // Short circuits if false category is encountered
    private boolean allTrue(List<? extends Category> categories) {
        boolean result = true;
        for(Category category : categories) {
             result = result && category.isEveryCellValid();
             if (!result) break;
        }
        return result;
    }

    @Override
    public boolean isEveryCellValid() {
        return allTrue(segmentCategories) && allTrue(scoreCategories) && allTrue(genericVariableCategories) && allTrue(itemCategories);
    }

    public ImmutableList<SegmentCategory> getSegmentCategories() {
        return ImmutableList.copyOf(segmentCategories);
    }

    public void setSegmentCategories(List<SegmentCategory> segmentCategories) {
        this.segmentCategories = segmentCategories;
    }

    public ImmutableList<AccommodationCategory> getAccommodationCategories() {
        return ImmutableList.copyOf(accommodationCategories);
    }

    public void setAccommodationCategories(List<AccommodationCategory> accommodationCategories) {
        this.accommodationCategories = accommodationCategories;
    }

    public ImmutableList<ScoreCategory> getScoreCategories() {
        return ImmutableList.copyOf(scoreCategories);
    }

    public void setScoreCategories(List<ScoreCategory> scoreCategories) {
        this.scoreCategories = scoreCategories;
    }

	public ImmutableList<GenericVariableCategory> getGenericVariableCategories() {
		return ImmutableList.copyOf(genericVariableCategories);
	}

	public void setGenericVariableCategories(List<GenericVariableCategory> genericVariableCategories) {
		this.genericVariableCategories = genericVariableCategories;
	}

    public ImmutableList<ItemCategory> getItemCategories() {
        return ImmutableList.copyOf(itemCategories);
    }

    public void setItemCategories(List<ItemCategory> itemCategories) {
        this.itemCategories = itemCategories;
    }

	public OpItemsData getOpItemsData() {
		return opItemsData;
	}

	public void setOpItemsData(OpItemsData opItemsData) {
		this.opItemsData = opItemsData;
	}

	public TdsReportScoreIrpScoredScore getTdsReportScoreIrpScoredScore() {
		return tdsReportScoreIrpScoredScore;
	}

	public void setTdsReportScoreIrpScoredScore(TdsReportScoreIrpScoredScore tdsReportScoreIrpScoredScore) {
		this.tdsReportScoreIrpScoredScore = tdsReportScoreIrpScoredScore;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("segmentCategories", segmentCategories)
                .append("accommodationCategories", accommodationCategories)
                .append("scoreCategories", scoreCategories)
                .append("genericVariableCategories", genericVariableCategories)
                .append("itemCategories", itemCategories)
                .append("opItemsData", opItemsData)
                .append("tdsReportScoreIrpScoredScore", tdsReportScoreIrpScoredScore)
                .toString();
    }



}
