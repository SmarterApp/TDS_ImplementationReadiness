package org.cresst.sb.irp.domain.tinytablescoringengine;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

public abstract class TableObject2 {
	
	public Element toXml() {
		return toXml(new Document());
	}

	public abstract Element toXml(Document doc);

	public static TableObject2 create(Document doc) {
		Element node = doc.getRootElement();
		switch (node.getName()) {
		case "responseSpec":
			node = node.getChild("responseTable");
			return Table2.fromXml(node);
		case "responseTable":
			return Table2.fromXml(node);
		case "tr":
			return TableVector2.fromXml(node);
		default:
			return null;
		}
	}

	protected final String getXmlString() {
		Element tr = toXml();
		XMLOutputter writer = new XMLOutputter();
		writer.outputString(tr);
		return tr.toString();
	}

	protected TableVector2 getColumn(String name) {
		return null;
	}

	protected TableVector2 getColumn(int i) {
		return null;
	}

	protected double getValueNumeric(int idx) {
		return Double.NaN;
	}

	protected TableVector2 getHeaderRow() {
		return null;
	}
}
