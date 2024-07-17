package db.magic.ui.views.dbs;

import db.magic.tree.MgcTreeLeaf;
import db.magic.util.Str;

public class TableNode extends MgcTreeLeaf {

	public TableNode(final String name) {
		super(name);
	}

	public String getLabel() {
		return getName();
	}

	public String getSqlTableName() {
		final String schema = getSchemaName(); 
		if (Str.is(schema)) {
			final StringBuffer sb = new StringBuffer();
			sb.append(schema);
			sb.append(".");
			sb.append(getName());
			return sb.toString();
		}
		else {
			return getName();
		}
	}

	public String getSchemaName() {
		final SchemaNode node = DBsUtil.getSchemaNode(this);
		return  node != null ? node.getName() : null;
	}

}