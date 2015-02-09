package org.cresst.sb.irp.domain.tinytablescoringengine;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

public abstract class TableObject {
	
	public Element toXml() {
		return toXml(new Document());
	}

	public abstract Element toXml(Document doc);

	public static TableObject create(Document doc) {
		Element node = doc.getRootElement();
		switch (node.getName()) {
		case "responseSpec":
			node = node.getChild("responseTable");
			return Table.fromXml(node);
		case "responseTable":
			return Table.fromXml(node);
		case "tr":
			return TableVector.fromXml(node);
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

	protected TableVector getColumn(String name) {
		return null;
	}

	protected TableVector getColumn(int i) {
		return null;
	}

	protected double getValueNumeric(int idx) {
		return Double.NaN;
	}

	protected TableVector getHeaderRow() {
		return null;
	}
}
