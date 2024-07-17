package db.magic.ui.views.dbs;

import db.magic.db.DataBase;
import db.magic.tree.MgcTreeNode;

public class DBNode extends MgcTreeNode {

	private DataBase db;
	private boolean isConnected;

	public DBNode(final DataBase db) {
		super(db.getName());
		this.db = db;
	}

	public DataBase getDataBase() {
		return db;
	}

	public void setDataBase(final DataBase db) {
		this.db = db;
		setName(db.getName());
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(final boolean isConnected) {
		this.isConnected = isConnected;
	}

	public Object clone() {
		final DBNode inst = new DBNode((DataBase)db.clone());
		inst.isConnected = isConnected;
		return inst;
	}

	void addSchemas(final String[] schemas) {
		for (final String n : schemas) {
			addChild(new SchemaNode(n));
		}
	}

}