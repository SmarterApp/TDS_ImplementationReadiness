package org.cresst.sb.irp.domain.analysis;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

public class OpportunityCategory extends Category {

	private List<CellCategory> opportunityPropertyCategories = new ArrayList<>();
	private List<SegmentCategory> segmentCategories = new ArrayList<>();
	private List<AccommodationCategory> accommodationCategories = new ArrayList<>();
	private List<ScoreCategory> scoreCategories = new ArrayList<>();
	private List<ItemCategory> itemCategories = new ArrayList<>();

	public List<SegmentCategory> getSegmentCategories() {
		return ImmutableList.copyOf(segmentCategories);
	}

	public void setSegmentCategories(List<SegmentCategory> segmentCategories) {
		this.segmentCategories = segmentCategories;
	}

	public List<AccommodationCategory> getAccommodationCategories() {
		return ImmutableList.copyOf(accommodationCategories);
	}

	public void setAccommodationCategories(List<AccommodationCategory> accommodationCategories) {
		this.accommodationCategories = accommodationCategories;
	}

	public List<ScoreCategory> getScoreCategories() {
		return ImmutableList.copyOf(scoreCategories);
	}

	public void setScoreCategories(List<ScoreCategory> scoreCategories) {
		this.scoreCategories = scoreCategories;
	}

	public List<ItemCategory> getItemCategories() {
		return ImmutableList.copyOf(itemCategories);
	}

	public void setItemCategories(List<ItemCategory> itemCategories) {
		this.itemCategories = itemCategories;
	}

	@Override
	public String toString() {
		return "OpportunityCategory [opportunityPropertyCategories=" + opportunityPropertyCategories + ", segmentCategories="
				+ segmentCategories + ", accommodationCategories=" + accommodationCategories + ", scoreCategories="
				+ scoreCategories + ", itemCategories=" + itemCategories + "]";
	}
}
