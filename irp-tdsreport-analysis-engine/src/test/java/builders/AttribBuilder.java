package builders;

public class AttribBuilder {

	private org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib attrib = new org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib();

	public AttribBuilder(String attid){
		attrib.setAttid(attid);
	}
	
	public AttribBuilder name(String name){
		attrib.setName(name);
		return this;
	}
	
	public AttribBuilder val(String val){
		attrib.setVal(val);
		return this;
	}
	
	public AttribBuilder desc(String desc){
		attrib.setDesc(desc);
		return this;
	}
	
	public org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist.Attrib toAttrib(){
		return attrib;
	}
}
