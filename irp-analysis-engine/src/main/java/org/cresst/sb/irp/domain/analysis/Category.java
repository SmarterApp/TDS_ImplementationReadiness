package org.cresst.sb.irp.domain.analysis;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for Categories
 */
public abstract class Category {
	protected List<CellCategory> cellCategories = new ArrayList<>();

	public ImmutableList<CellCategory> getCellCategories() {
		return ImmutableList.copyOf(cellCategories);
	}

	public void addCellCategory(CellCategory cellCategory) {
		cellCategories.add(cellCategory);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Category category = (Category) o;

		if (!cellCategories.equals(category.cellCategories)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return cellCategories.hashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("cellCategories", cellCategories)
				.toString();
	}
}
