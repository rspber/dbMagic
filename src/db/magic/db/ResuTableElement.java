package db.magic.db;

import db.magic.dictios.FDictio;
import db.magic.dictios.Foreign;
import db.magic.dictios.NamedItem;

public class ResuTableElement {

	public final int recordNo;
	private final ResuTableColumn[] columns;
	private final NamedItem<FDictio>[] dictios;
	private final NamedItem<Foreign>[] foreigns;
	private final Object[] items;

	public ResuTableElement(
			final int recordNo,
			final ResuTableColumn[] columns,
			final NamedItem<FDictio>[] dictios,
			final NamedItem<Foreign>[] foreigns,
			final Object[] items
	) {
		this.recordNo = recordNo;
		this.columns = columns;
		this.dictios = dictios;
		this.foreigns = foreigns;
		this.items = items;
	}

	public Object getItem(final int index) {
		return items[index];
	}

	public Object getPictItem(final int index) {
		if (dictios != null && dictios[index] != null) {
			return dictios[index].getItem().translate(items[index]);
		}
		return items[index];
	}

	public ResuTableColumn[] getColumns() {
		return columns;
	}

	public FDictio getDictio(final int index) {
		return dictios[index] != null ? dictios[index].getItem() : null;
	}

	public Foreign getForeign(final int index) {
		return foreigns[index] != null ? foreigns[index].getItem() : null;
	}

}