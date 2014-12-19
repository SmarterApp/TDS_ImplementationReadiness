package org.cresst.sb.irp.domain.analysis;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class AccommodationCategory {
	private static Logger logger = Logger.getLogger(AccommodationCategory.class);
	
	private List<CellCategory> cellCategories;
	
	public AccommodationCategory() {
		logger.info("initializing");
		setCellCategories(new ArrayList<CellCategory>());
	}

	public List<CellCategory> getCellCategories() {
		return cellCategories;
	}

	public void setCellCategories(List<CellCategory> cellCategories) {
		this.cellCategories = cellCategories;
	}

	@Override
	public String toString() {
		return "AccommodationCategory [cellCategories=" + cellCategories + "]";
	}
	
	
}
