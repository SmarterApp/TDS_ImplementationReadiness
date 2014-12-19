package org.cresst.sb.irp.domain.analysis;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class OpportunityCategory {
	private static Logger logger = Logger.getLogger(OpportunityCategory.class);
	
	private List<CellCategory> opportunityPropertyCategories;
	private List<SegmentCategory> segmentCategories;
	private List<AccommodationCategory> accommodationCategories;
	private List<ScoreCategory> scoreCategories;
	private List<ItemCategory> itemCategories;
	
	
	public OpportunityCategory() {
		logger.info("initializing");
		setOpportunityPropertyCategories(new ArrayList<CellCategory>());
		setSegmentCategories(new ArrayList<SegmentCategory>());
		setAccommodationCategories(new ArrayList<AccommodationCategory>());
		setScoreCategories(new ArrayList<ScoreCategory>());
		setItemCategories(new ArrayList<ItemCategory>());
	}

	public List<CellCategory> getOpportunityPropertyCategories() {
		return opportunityPropertyCategories;
	}

	public void setOpportunityPropertyCategories(List<CellCategory> opportunityPropertyCategories) {
		this.opportunityPropertyCategories = opportunityPropertyCategories;
	}

	public List<SegmentCategory> getSegmentCategories() {
		return segmentCategories;
	}

	public void setSegmentCategories(List<SegmentCategory> segmentCategories) {
		this.segmentCategories = segmentCategories;
	}

	public List<AccommodationCategory> getAccommodationCategories() {
		return accommodationCategories;
	}

	public void setAccommodationCategories(List<AccommodationCategory> accommodationCategories) {
		this.accommodationCategories = accommodationCategories;
	}

	public List<ScoreCategory> getScoreCategories() {
		return scoreCategories;
	}

	public void setScoreCategories(List<ScoreCategory> scoreCategories) {
		this.scoreCategories = scoreCategories;
	}

	public List<ItemCategory> getItemCategories() {
		return itemCategories;
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
