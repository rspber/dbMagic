package db.magic.ui.views.sqh;

import db.magic.tree.MgcTreeNode;

public class SqHFolderNode extends MgcTreeNode {

	public SqHFolderNode(final String name) {
		super(name);
	}

	public SqHQueryNode addQuery(final String sqlQuery) {
		final SqHQueryNode query = new SqHQueryNode(sqlQuery);
		addChild(query);
		return query;
	}

}