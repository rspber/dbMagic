package db.magic.ui.views.dbs;

public class TableInfo {

	private String name;
	private String tableType;

	public TableInfo(final String name) {
		this.name = name;
	}

	public TableInfo() {
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getTableType() {
		return this.tableType;
	}

	public void setTableType(final String tableType) {
		this.tableType = tableType;
	}

}