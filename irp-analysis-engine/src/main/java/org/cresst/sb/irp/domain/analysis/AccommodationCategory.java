package org.cresst.sb.irp.domain.analysis;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class AccommodationCategory {
	private static Logger logger = Logger.getLogger(AccommodationCategory.class);
	
	private List<CellCategory> listCellCategory;
	
	public AccommodationCategory() {
		logger.info("initializing");
		setListCellCategory(new ArrayList<CellCategory>());
	}

	public List<CellCategory> getListCellCategory() {
		return listCellCategory;
	}

	public void setListCellCategory(List<CellCategory> listCellCategory) {
		this.listCellCategory = listCellCategory;
	}

	@Override
	public String toString() {
		return "AccommodationCategory [listCellCategory=" + listCellCategory + "]";
	}
	
	
}
