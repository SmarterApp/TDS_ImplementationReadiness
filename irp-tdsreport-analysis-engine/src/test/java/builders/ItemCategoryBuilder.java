package builders;

import org.cresst.sb.irp.domain.analysis.ItemCategory;
import org.cresst.sb.irp.domain.analysis.ItemCategory.ItemStatusEnum;

public class ItemCategoryBuilder {
	ItemCategory itemCategory = new ItemCategory();
	
	public ItemCategoryBuilder itemBankKeyKey(String itemBankKeyKey){
		itemCategory.setItemBankKeyKey(itemBankKeyKey);
		return this;
	}
	
	public ItemCategoryBuilder status(ItemStatusEnum status){
		itemCategory.setStatus(status);
		return this;
	}
	
	public ItemCategoryBuilder itemFormatCorrect(boolean itemFormatCorrect){
		itemCategory.setItemFormatCorrect(itemFormatCorrect);
		return this;
	}
	
	
	public ItemCategory toItemCategory(){
		return itemCategory;
	}
}
