package org.cresst.sb.irp.domain.tinytablescoringengine;

import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

import AIR.Common.Helpers._Ref;
import AIR.Common.Utilities.JavaPrimitiveUtils;
import AIR.Common.xml.XmlElement;

public class TableVector extends TableObject {

	private TableCell[] _elements = null;
	public boolean isHeader = false;

	public TableVector(int size) {
		_elements = new TableCell[size];
	}

	public TableVector(TableCell[] elements) {
		_elements = elements;
	}

	public TableCell getElement(int idx) {
		if (idx < 0 || idx > _elements.length) {
			return new TableCell("");
		}
		return _elements[idx];
	}

	public TableCell[] getElements() {
		return _elements;
	}

	public void setElement(int idx, TableCell value) {
		_elements[idx] = value;
	}

	public int getElementCount() {
		return _elements.length;
	}

	public static TableVector fromXml(Element node) {
		List<Element> nodeList = new XmlElement(node).selectNodes("td|th");
		boolean anyData = false;
		TableCell[] elements = new TableCell[nodeList.size()];
		for (int i = 0; i < nodeList.size(); i++) {
			anyData = false;
			switch (nodeList.get(i).getName()) {
			case "td":
				anyData = true;
				elements[i] = new TableCell(nodeList.get(i).getText());
				break;
			case "th":
				String name = nodeList.get(i).getAttribute("id").getValue();
				String colSpanString = nodeList.get(i).getText();
				elements[i] = new TableHeaderCell(name, colSpanString);
				break;
			default:
				break;

			}
		}
		TableVector t = new TableVector(elements);
		if (!anyData) {
			t.isHeader = true;
		}
		return t;
	}

	public double getValueNumeric(int idx) {
		if ((idx < 0) || (idx >= _elements.length)) {
			return Double.NaN;
		}
		_Ref<Double> d = new _Ref<Double>();
		boolean success = JavaPrimitiveUtils.doubleTryParse(_elements[idx]._content, d);
		if (success) {
			return d.get();
		}
		return Double.NaN;
	}

	@Override
	public Element toXml(Document doc) {
		Element tr = new Element("tr");
		for (TableCell cell : _elements) {
			Element xml = cell.toXml(doc);
			tr.addContent(xml);
		}
		return tr;
	}
	
}
