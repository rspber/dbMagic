package db.magic.descr;

import db.magic.tree.MgcTreeNode;

public class DescrRootNode extends MgcTreeNode {

	public DescrRootNode(final String name) {
		super(name);
	}

	public DescrCategoryNode addCategory(final String name) {
		final DescrCategoryNode category = new DescrCategoryNode(name);
		addChild(category);
		return category;
	}

}