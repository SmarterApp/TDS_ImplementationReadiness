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

	public void seIsMinopitems(boolean isMinopitems) {
		this.isMinopitems = isMinopitems;
	}

	public boolean isMaxopitems() {
		return isMaxopitems;
	}

	public void setIsMaxopitems(boolean isMaxopitems) {
		this.isMaxopitems = isMaxopitems;
	}

	@Override
	public String toString() {
		return "OpItemsData [isCombo=" + isCombo + ", elementtype=" + elementtype + ", minopitems=" + minopitems + ", maxopitems="
				+ maxopitems + ", catItems=" + catItems + ", perfItems=" + perfItems + ", isMinopitems=" + isMinopitems
				+ ", isMaxopitems=" + isMaxopitems + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		OpItemsData that = (OpItemsData) o;
		if (isCombo != that.isCombo) return false;
		if (elementtype != that.elementtype) return false;
		if (minopitems != that.minopitems) return false;
		if (maxopitems != that.maxopitems) return false;
		if (catItems != that.catItems) return false;
		if (perfItems != that.perfItems) return false;
		if (isMinopitems != that.isMinopitems) return false;
		if (isMaxopitems != that.isMaxopitems) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = (isCombo ? 1 : 0);
		result = 31 * result + (elementtype != null ? elementtype.hashCode() : 0);
		result = 31 * result + minopitems;
		result = 31 * result + maxopitems;
		result = 31 * result + catItems;
		result = 31 * result + perfItems;
		result = 31 * result + (isMinopitems ? 1 : 0);
		result = 31 * result + (isMaxopitems ? 1 : 0);
		return result;
	}

}
