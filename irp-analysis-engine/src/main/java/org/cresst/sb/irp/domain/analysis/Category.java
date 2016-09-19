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
    // Keep track of whether all CellCategories are valid
    protected boolean everyCellValid = true;

    public ImmutableList<CellCategory> getCellCategories() {
        return ImmutableList.copyOf(cellCategories);
    }

    // In Nested Categories this should be implemented to check those categories as well
    public boolean isEveryCellValid() {
        return everyCellValid;
    }

    public void addCellCategory(CellCategory cellCategory) {
        // Check status of cell category and set overall status when adding
        if (cellCategory != null) {
            FieldCheckType fieldCheckType = cellCategory.getFieldCheckType();
            if(fieldCheckType != null) {
                FieldCheckType.StatusEnum result = fieldCheckType.getStatusEnum();
                if (result != null && result.equals(FieldCheckType.StatusEnum.ERROR)) {
                    everyCellValid = false;
                }
            }
        }
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
