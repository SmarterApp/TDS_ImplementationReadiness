package org.cresst.sb.irp.domain.analysis;

<<<<<<< HEAD
import com.google.common.collect.ImmutableList;

=======
>>>>>>> d5ef8f6176b580ab1660e2cc99ce6c6d02efcadf
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cresst.sb.irp.domain.items.Itemrelease;


<<<<<<< HEAD
public class ItemCategory {
	private static Logger logger = Logger.getLogger(ItemCategory.class);
=======
public class ItemCategory extends Category {
>>>>>>> d5ef8f6176b580ab1660e2cc99ce6c6d02efcadf

    private ResponseCategory responseCategory;
    private ScoreInfoCategory scoreInfoCategory;

    private String itemBankKeyKey;
    private org.cresst.sb.irp.domain.items.Itemrelease.Item irpItem;
    private Itemrelease.Item.Attriblist attriblist;

    public ResponseCategory getResponseCategory() {
        return responseCategory;
    }

<<<<<<< HEAD
	public ItemCategory(){
		logger.info("initializing");
	}
	
	public ImmutableList<CellCategory> getCellCategories() {
		return ImmutableList.copyOf(cellCategories);
	}
=======
    public void setResponseCategory(ResponseCategory responseCategory) {
        this.responseCategory = responseCategory;
    }
>>>>>>> d5ef8f6176b580ab1660e2cc99ce6c6d02efcadf

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
