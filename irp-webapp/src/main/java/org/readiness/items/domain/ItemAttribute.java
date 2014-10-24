package org.readiness.items.domain;

public class ItemAttribute {

	private String format;
	private String version;

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getBankkey() {
		return bankkey;
	}

	public void setBankkey(String bankkey) {
		this.bankkey = bankkey;
	}

	private String bankkey;

}
