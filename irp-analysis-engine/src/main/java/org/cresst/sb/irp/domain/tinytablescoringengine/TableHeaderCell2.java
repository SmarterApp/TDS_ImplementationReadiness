package org.cresst.sb.irp.domain.tinytablescoringengine;

import org.apache.commons.lang3.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;

public class TableHeaderCell2 extends TableCell2 {

	private String _name;

	public TableHeaderCell2(String name, String content) {
		super(content);
		_isHeader = true;
		_name = name;
	}

	public String getName() {
		return _name;
	}

	public Element toXml(Document doc) {
		Element th = new Element("th");
		if (!StringUtils.isEmpty(_name)) {
			th.setAttribute("id", _name);
		}
		if (!StringUtils.isEmpty(_content))
			th.setText(_content);
		return th;
	}

}
