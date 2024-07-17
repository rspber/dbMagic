package db.magic.ui.views.sqh;

import db.magic.tree.MgcTreeNode;

public class SqHRootNode extends MgcTreeNode {

	public SqHRootNode(final String name) {
		super(name);
	}

	public SqHCategoryNode addCapegory(final String name) {
 		final SqHCategoryNode category = new SqHCategoryNode(name);
	 	addChild(category);
		return category;
	}

}