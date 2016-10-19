package builders;

import org.cresst.sb.irp.domain.analysis.OpItemsData;

public class OpItemsDataBuilder {

	private OpItemsData opItemsData = new OpItemsData();

	public OpItemsDataBuilder isCombo(boolean isCombo) {
		opItemsData.setCombo(isCombo);
		return this;
	}

	public OpItemsDataBuilder elementtype(String elementtype) {
		opItemsData.setElementtype(elementtype);
		return this;
	}

	public OpItemsDataBuilder minopitems(int minopitems) {
		opItemsData.setMinopitems(minopitems);
		return this;
	}

	public OpItemsDataBuilder maxopitems(int maxopitems) {
		opItemsData.setMaxopitems(maxopitems);
		return this;
	}

	public OpItemsDataBuilder catItems(int catItems) {
		opItemsData.setCatItems(catItems);
		return this;
	}

	public OpItemsDataBuilder perfItems(int perfItems) {
		opItemsData.setPerfItems(perfItems);
		return this;
	}

	public OpItemsDataBuilder isMinopitems(boolean isMinopitems) {
		opItemsData.seIsMinopitems(isMinopitems);
		return this;
	}

	public OpItemsDataBuilder isMaxopitems(boolean isMaxopitems) {
		opItemsData.setIsMaxopitems(isMaxopitems);
		return this;
	}

	public OpItemsData toOpItemsData() {
		return opItemsData;
	}

}
