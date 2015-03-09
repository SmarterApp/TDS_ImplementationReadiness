package builders;

public class ItemreleaseItemBuilder {
	
    org.cresst.sb.irp.domain.items.Itemrelease.Item item = new org.cresst.sb.irp.domain.items.Itemrelease.Item();
    
	public ItemreleaseItemBuilder(String format, String id, String version, String bankkey) {
		item.setFormat(format);
		item.setId(id);
		item.setVersion(version);
		item.setBankkey(bankkey);
	}
	
	public org.cresst.sb.irp.domain.items.Itemrelease.Item toItem(){
		return item;
	}

}
