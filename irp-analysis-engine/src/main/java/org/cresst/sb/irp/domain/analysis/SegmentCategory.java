package org.cresst.sb.irp.domain.analysis;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class SegmentCategory {
	private static Logger logger = Logger.getLogger(SegmentCategory.class);
	
	private List<CellCategory> cellCategories;
	
	public SegmentCategory() {
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
		return "SegmentCategory [cellCategories=" + cellCategories + "]";
	}
	
}
