package db.magic.descr;

import db.magic.tree.MgcTreeNode;

public class DescrCategoryNode extends MgcTreeNode {

	public DescrCategoryNode(final String name) {
		super(name);
	}

	public DescrTableNode addTable(final String name) {
		final DescrTableNode table = new DescrTableNode(name);
		addChild(table);
		return table;
	}

}