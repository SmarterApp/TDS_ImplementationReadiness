package org.cresst.sb.irp.domain.analysis;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class OpportunityCategory {
	private static Logger logger = Logger.getLogger(OpportunityCategory.class);
	
	private List<CellCategory> listOportunityPropertyCategory;
	private List<SegmentCategory> listSegmentCategory;
	private List<AccommodationCategory> listAccommodationCategory;
	private List<ScoreCategory> listScoreCategory;
	private List<ItemCategory> listItemCategory;
	
	
	public OpportunityCategory() {
		logger.info("initializing");
		setListOportunityPropertyCategory(new ArrayList<CellCategory>());
		setListSegmentCategory(new ArrayList<SegmentCategory>());
		setListAccommodationCategory(new ArrayList<AccommodationCategory>());
		setListScoreCategory(new ArrayList<ScoreCategory>());
		setListItemCategory(new ArrayList<ItemCategory>());
	}

	public List<CellCategory> getListOportunityPropertyCategory() {
		return listOportunityPropertyCategory;
	}

	public void setListOportunityPropertyCategory(List<CellCategory> listOportunityPropertyCategory) {
		this.listOportunityPropertyCategory = listOportunityPropertyCategory;
	}

	public List<SegmentCategory> getListSegmentCategory() {
		return listSegmentCategory;
	}

	public void setListSegmentCategory(List<SegmentCategory> listSegmentCategory) {
		this.listSegmentCategory = listSegmentCategory;
	}

	public List<AccommodationCategory> getListAccommodationCategory() {
		return listAccommodationCategory;
	}

	public void setListAccommodationCategory(List<AccommodationCategory> listAccommodationCategory) {
		this.listAccommodationCategory = listAccommodationCategory;
	}

	public List<ScoreCategory> getListScoreCategory() {
		return listScoreCategory;
	}

	public void setListScoreCategory(List<ScoreCategory> listScoreCategory) {
		this.listScoreCategory = listScoreCategory;
	}

	public List<ItemCategory> getListItemCategory() {
		return listItemCategory;
	}

	public void setListItemCategory(List<ItemCategory> listItemCategory) {
		this.listItemCategory = listItemCategory;
	}
	
	
	
	
	
	
	
}
