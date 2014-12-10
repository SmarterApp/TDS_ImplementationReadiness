package org.cresst.sb.irp.domain.analysis;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class SegmentCategory {
	private static Logger logger = Logger.getLogger(SegmentCategory.class);
	
	private List<CellCategory> listCellCategory;
	
	public SegmentCategory() {
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
		return "SegmentCategory [listCellCategory=" + listCellCategory + "]";
	}
	
}
