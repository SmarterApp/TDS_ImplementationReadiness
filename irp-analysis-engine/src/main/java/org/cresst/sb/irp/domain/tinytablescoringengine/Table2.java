package org.cresst.sb.irp.domain.tinytablescoringengine;

import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

import AIR.Common.xml.XmlElement;

public class Table2 extends TableObject2 {

	private TableVector2[] _rows;
	private int _rowCount = 0;
	private int _columnCount = 0;
	private int _headerRows = 0;

	public Table2(TableVector2[] rows) {
		_rowCount = rows.length;
		_rows = rows;
		for (int i = 0; i < _rowCount; i++) {
			if (_rows[i].isHeader) {
				_headerRows++;
			}
			_columnCount = _rows[i].getElementCount();
		}
	}

	@Override
	public TableVector2 getColumn(String colName) {
		int colIdx = getColumnIndex(colName);
		if (colIdx == -1) {
			return null;
		}
		return getColumn(colIdx);

	}

	@Override
	public TableVector2 getColumn(int i) {
		int size = _rowCount - _headerRows;
		TableVector2 column = new TableVector2(size);
		for (int j = 0; j < size; j++) {
			column.setElement(j, _rows[_headerRows + j].getElement(i));
		}

		return column;
	}

	public TableVector2 getHeaderRow() {
		return (_rowCount > 0) ? _rows[0] : null;
	}

	public TableVector2 getRowIndex(int idx) {
		return _rows[idx];
	}

	public int getColumnIndex(String colName) {
		if (_headerRows > 0) {
			for (int i = 0; i < _columnCount; i++) {
				if (colName.equals(_rows[_headerRows - 1].getElement(i).getName())) {
					return i;
				}
			}
		}
		return -1;
	}

	public static Table2 fromXml(Element node) // tales a responseTable node as
	// input
	{
		List<Element> rowNodes = new XmlElement(node).selectNodes("tr");
		int rowCount = rowNodes.size();
		TableVector2[] rows = new TableVector2[rowCount];
		for (int i = 0; i < rowCount; i++) {
			rows[i] = TableVector2.fromXml(rowNodes.get(i));
		}
		return new Table2(rows);
	}

	@Override
	public Element toXml(Document doc) {
		Element table = new Element("responseTable");
		for (TableVector2 v : _rows) {
			Element row = v.toXml(doc);
			table.addContent(row);
		}
		return table;
	}
	
	public int getRowCount() {
		return _rowCount;
	}
	
	public int getColumnCount() {
		return _columnCount;
	}

}
