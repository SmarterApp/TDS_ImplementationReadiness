package org.cresst.sb.irp.domain.analysis;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class SegmentCategory {

	private List<CellCategory> cellCategories = new ArrayList<>();

	public List<CellCategory> getCellCategories() {
		return ImmutableList.copyOf(cellCategories);
	}

	public void setCellCategories(List<CellCategory> cellCategories) {
		this.cellCategories = cellCategories;
	}

	@Override
	public String toString() {
		return "SegmentCategory [cellCategories=" + cellCategories + "]";
	}
}
