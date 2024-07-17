package db.magic.ui.views.dbs;

import db.magic.tree.MgcTreeNode;

public class FolderNode extends MgcTreeNode {

	public FolderNode(final String name) {
		super(name);
	}

	public void addTables(final TableInfo[] tables) {
		for (final TableInfo tableInfo : tables) {
			addChild(new TableNode(tableInfo.getName()));
		}
	}

}