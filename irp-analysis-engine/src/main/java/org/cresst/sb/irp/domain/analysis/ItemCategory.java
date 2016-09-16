package org.cresst.sb.irp.domain.analysis;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cresst.sb.irp.domain.items.Itemrelease;


public class ItemCategory extends Category {

    private ResponseCategory responseCategory;
    private ScoreInfoCategory scoreInfoCategory;

    private String itemBankKeyKey;
    private org.cresst.sb.irp.domain.items.Itemrelease.Item irpItem;
    private Itemrelease.Item.Attriblist attriblist;

    public enum ItemStatusEnum{ FOUND, MISSING, EXTRA, NOTUSED }

    private boolean itemFormatCorrect;

    public boolean isItemFormatCorrect() {
		return itemFormatCorrect;
	}

	public void setItemFormatCorrect(boolean itemFormatCorrect) {
		this.itemFormatCorrect = itemFormatCorrect;
	}

	private ItemStatusEnum status;

    public ItemStatusEnum getStatus() {
		return status;
	}

	public void setStatus(ItemStatusEnum status) {
		this.status = status;
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
    public Boolean isEveryCellValid() {
        return this.everyCellValid && this.scoreInfoCategory.isEveryCellValid() && this.responseCategory.isEveryCellValid();
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
                .append("itemFormatCorrect", itemFormatCorrect)
                .append("status", status)
                .toString();
    }
}
