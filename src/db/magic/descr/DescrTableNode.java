package db.magic.descr;

import db.magic.tree.MgcTreeNode;
import db.magic.util.Str;

public class DescrTableNode extends MgcTreeNode {

	private String fullName;
	private String description;

	public DescrTableNode(final String tableId) {
		super(tableId);
	}

	public void setFullName(final String fullName) {
		modified_ = true;
		this.fullName = fullName;
	}

	public void setDescription(final String description) {
		modified_ = true;
		this.description = description;
	}

	public String getFullName() {
		return fullName;
	}

	public String getDescription() {
		return description;
	}

	public DescrColumnNode addColumn(final String name) {
		final DescrColumnNode column = new DescrColumnNode(name);
		addChild(column);
		return column;
	}

	@Override
	public String toString() {
		return getName() + (!Str.isBlank(fullName) ? " -- " + fullName : "");
	}

}