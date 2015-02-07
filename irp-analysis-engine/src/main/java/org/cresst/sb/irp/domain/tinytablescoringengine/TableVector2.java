package org.cresst.sb.irp.domain.tinytablescoringengine;

import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

import AIR.Common.Helpers._Ref;
import AIR.Common.Utilities.JavaPrimitiveUtils;
import AIR.Common.xml.XmlElement;

public class TableVector2 extends TableObject2 {

	private TableCell2[] _elements = null;
	public boolean isHeader = false;

	public TableVector2(int size) {
		_elements = new TableCell2[size];
	}

	public TableVector2(TableCell2[] elements) {
		_elements = elements;
	}

	public TableCell2 getElement(int idx) {
		if (idx < 0 || idx > _elements.length) {
			return new TableCell2("");
		}
		return _elements[idx];
	}

	public TableCell2[] getElements() {
		return _elements;
	}

	public void setElement(int idx, TableCell2 value) {
		_elements[idx] = value;
	}

	public int getElementCount() {
		return _elements.length;
	}

	public static TableVector2 fromXml(Element node) {
		List<Element> nodeList = new XmlElement(node).selectNodes("td|th");
		boolean anyData = false;
		TableCell2[] elements = new TableCell2[nodeList.size()];
		for (int i = 0; i < nodeList.size(); i++) {
			anyData = false;
			switch (nodeList.get(i).getName()) {
			case "td":
				anyData = true;
				elements[i] = new TableCell2(nodeList.get(i).getText());
				break;
			case "th":
				String name = nodeList.get(i).getAttribute("id").getValue();
				String colSpanString = nodeList.get(i).getText();
				elements[i] = new TableHeaderCell2(name, colSpanString);
				break;
			default:
				break;

			}
		}
		TableVector2 t = new TableVector2(elements);
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
		for (TableCell2 cell : _elements) {
			Element xml = cell.toXml(doc);
			tr.addContent(xml);
		}
		return tr;
	}
	
}
