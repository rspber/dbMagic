package db.magic.descr;

public class DescrItemData {

	public String category;
	public String tableId;
	public String columnId;

	public DescrItemData() {
	}

	public DescrItemData(final DescrItemData old) {
		category = old.category;
		tableId = old.tableId;
		columnId = old.columnId;
	}

}