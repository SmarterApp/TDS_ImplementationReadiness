package org.cresst.sb.irp.domain.tinytablescoringengine;

import org.jdom2.Document;
import org.jdom2.Element;

public class TableCell2 extends TableObject2 {

	public boolean _isHeader = false;
	protected String _content = "";

	public TableCell2(String content) {
		_content = content;
	}

	public boolean getIsHeader() {
		return _isHeader;
	}

	public String getName() {
		return "";
	}
	
	public String getContext(){
		return _content;
	}

	@Override
	public String toString() {
		return "TableCell2 [_isHeader=" + _isHeader + ", _content=" + _content + "]";
	}

	@Override
	public Element toXml(Document arg0) {
		Element td = new Element("td");
		td.addContent(_content);
		return td;
	}

}
