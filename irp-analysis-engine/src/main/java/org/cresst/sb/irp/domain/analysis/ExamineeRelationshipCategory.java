package org.cresst.sb.irp.domain.analysis;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class ExamineeRelationshipCategory {

	private List<CellCategory> cellCategories = new ArrayList<CellCategory>();

	public List<CellCategory> getCellCategories() {
		return ImmutableList.copyOf(cellCategories);
	}

	public void setCellCategories(List<CellCategory> cellCategories) {
		this.cellCategories = cellCategories;
	}

	@Override
	public String toString() {
		return "ExamineeRelationshipCategory [cellCategories=" + cellCategories + "]";
	}
}
