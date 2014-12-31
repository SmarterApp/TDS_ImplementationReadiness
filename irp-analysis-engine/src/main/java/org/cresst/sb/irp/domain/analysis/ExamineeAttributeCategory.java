package org.cresst.sb.irp.domain.analysis;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

public class ExamineeAttributeCategory {

	private List<CellCategory> cellCategories = new ArrayList<>();

	public List<CellCategory> getCellCategories() {
		return ImmutableList.copyOf(cellCategories);
	}

	public void setCellCategories(List<CellCategory> cellCategories) {
		this.cellCategories = cellCategories;
	}

	@Override
	public String toString() {
		return "ExamineeAttributeCategory [cellCategories=" + cellCategories + "]";
	}
}
