package org.cresst.sb.irp.domain.analysis;

public class OpItemsData {

	private boolean isCombo;
	private String elementtype;
	private int minopitems;
	private int maxopitems;
	private int catItems;
	private int perfItems;
	private boolean isMinopitems;
	private boolean isMaxopitems;
	
	public boolean isCombo() {
		return isCombo;
	}

	public void setCombo(boolean isCombo) {
		this.isCombo = isCombo;
	}
	
	public String getElementtype() {
		return elementtype;
	}

	public void setElementtype(String elementtype) {
		this.elementtype = elementtype;
	}

	public int getMinopitems() {
		return minopitems;
	}

	public void setMinopitems(int minopitems) {
		this.minopitems = minopitems;
	}

	public int getMaxopitems() {
		return maxopitems;
	}

	public void setMaxopitems(int maxopitems) {
		this.maxopitems = maxopitems;
	}

	public int getCatItems() {
		return catItems;
	}

	public void setCatItems(int catItems) {
		this.catItems = catItems;
	}

	public int getPerfItems() {
		return perfItems;
	}

	public void setPerfItems(int perfItems) {
		this.perfItems = perfItems;
	}

	public boolean isMinopitems() {
		return isMinopitems;
	}

	public void setMinopitems(boolean isMinopitems) {
		this.isMinopitems = isMinopitems;
	}

	public boolean isMaxopitems() {
		return isMaxopitems;
	}

	public void setMaxopitems(boolean isMaxopitems) {
		this.isMaxopitems = isMaxopitems;
	}

	@Override
	public String toString() {
		return "OpItemsData [isCombo=" + isCombo + ", elementtype=" + elementtype + ", minopitems=" + minopitems + ", maxopitems="
				+ maxopitems + ", catItems=" + catItems + ", perfItems=" + perfItems + ", isMinopitems=" + isMinopitems
				+ ", isMaxopitems=" + isMaxopitems + "]";
	}

}
