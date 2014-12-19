package org.cresst.sb.irp.domain.analysis;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.items.Itemrelease;

import java.util.ArrayList;
import java.util.List;

public class ItemCategory {

	private List<CellCategory> cellCategories = new ArrayList<>();

	private ResponseCategory responseCategory;
	private ScoreInfoCategory scoreInfoCategory; 

	private String itemBankKeyKey;
	private org.cresst.sb.irp.domain.items.Itemrelease.Item irpItem;
	private Itemrelease.Item.Attriblist attriblist;

	public ImmutableList<CellCategory> getCellCategories() {
		return ImmutableList.copyOf(cellCategories);
	}

	public void addCellCategory(CellCategory cellCategory) {
		cellCategories.add(cellCategory);
	}

	public ResponseCategory getResponseCategory() {
		return responseCategory;
	}

	public void setResponseCategory(ResponseCategory responseCategory) {
		this.responseCategory = responseCategory;
	}

	public ScoreInfoCategory getScoreInfoCategory() {
		return scoreInfoCategory;
	}

	public void setScoreInfoCategory(ScoreInfoCategory scoreInfoCategory) {
		this.scoreInfoCategory = scoreInfoCategory;
	}

	public String getItemBankKeyKey() {
		return itemBankKeyKey;
	}

	public void setItemBankKeyKey(String itemBankKeyKey) {
		this.itemBankKeyKey = itemBankKeyKey;
	}

	public org.cresst.sb.irp.domain.items.Itemrelease.Item getIrpItem() {
		return irpItem;
	}

	public void setIrpItem(org.cresst.sb.irp.domain.items.Itemrelease.Item irpItem) {
		this.irpItem = irpItem;
	}
	
	public Itemrelease.Item.Attriblist getAttriblist() {
		return attriblist;
	}

	public void setAttriblist(Itemrelease.Item.Attriblist attriblist) {
		this.attriblist = attriblist;
	}


	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("cellCategories", cellCategories)
				.append("responseCategory", responseCategory)
				.append("scoreInfoCategory", scoreInfoCategory)
				.append("itemBankKeyKey", itemBankKeyKey)
				.append("irpItem", irpItem)
				.append("attriblist", attriblist)
				.toString();
	}
}
