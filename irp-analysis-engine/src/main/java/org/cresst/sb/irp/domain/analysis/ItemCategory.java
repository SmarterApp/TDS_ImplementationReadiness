package org.cresst.sb.irp.domain.analysis;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.items.Itemrelease;

public class ItemCategory {
	private static Logger logger = Logger.getLogger(ItemCategory.class);

	private List<CellCategory> listItemAttribute;
	private ResponseCategory responseCategory;
	private ScoreInfoCategory scoreInfoCategory; 
	private String itemBankKeyKey;
	private String expectResultFromIRPitem;
	private Itemrelease.Item.Attriblist.Attrib attrib;
	
	public ItemCategory() {
		logger.info("initializing");
		setListItemAttribute(new ArrayList<CellCategory>());
	}

	public List<CellCategory> getListItemAttribute() {
		return listItemAttribute;
	}

	public void setListItemAttribute(List<CellCategory> listItemAttribute) {
		this.listItemAttribute = listItemAttribute;
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

	public String getExpectResultFromIRPitem() {
		return expectResultFromIRPitem;
	}

	public void setExpectResultFromIRPitem(String expectResultFromIRPitem) {
		this.expectResultFromIRPitem = expectResultFromIRPitem;
	}

	public Itemrelease.Item.Attriblist.Attrib getAttrib() {
		return attrib;
	}

	public void setAttrib(Itemrelease.Item.Attriblist.Attrib attrib) {
		this.attrib = attrib;
	}

	@Override
	public String toString() {
		return "ItemCategory [listItemAttribute=" + listItemAttribute + ", responseCategory=" + responseCategory
				+ ", scoreInfoCategory=" + scoreInfoCategory + ", itemBankKeyKey=" + itemBankKeyKey
				+ ", expectResultFromIRPitem=" + expectResultFromIRPitem + ", attrib=" + attrib + "]";
	}


}
