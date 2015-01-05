package org.cresst.sb.irp.domain.analysis;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class ToolUsageCategory extends Category {

	private List<ToolPageCategory> toolPageCategories = new ArrayList<>();

	public ImmutableList<ToolPageCategory> getToolPageCategories() {
		return ImmutableList.copyOf(toolPageCategories);
	}

	public void setToolPageCategories(List<ToolPageCategory> toolPageCategories) {
		this.toolPageCategories = toolPageCategories;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("toolPageCategories", toolPageCategories)
				.toString();
	}
}
