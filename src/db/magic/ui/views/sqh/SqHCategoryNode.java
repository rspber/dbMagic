package db.magic.ui.views.sqh;

import db.magic.tree.MgcTreeNode;

public class SqHCategoryNode extends MgcTreeNode {

	public SqHCategoryNode(final String name) {
		super(name);
	}

	public SqHFolderNode addFolder(final String name) {
		final SqHFolderNode folder = new SqHFolderNode(name);
		addChild(folder);
		return folder;
	}

}