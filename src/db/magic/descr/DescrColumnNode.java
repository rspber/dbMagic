package db.magic.descr;

import db.magic.tree.MgcTreeLeaf;
import db.magic.util.Str;

public class DescrColumnNode extends MgcTreeLeaf {

	private String fullName;
	private String description;

	public DescrColumnNode(final String columnId) {
		super(columnId);
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

	@Override
	public String toString() {
		return getName() + (!Str.isBlank(fullName) ? " -- " + fullName : "");
	}

}