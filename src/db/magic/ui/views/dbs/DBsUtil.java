package db.magic.ui.views.dbs;

import db.magic.db.DataBase;
import db.magic.tree.IMgcTreeNode;

public class DBsUtil {

	public static DBNode getDBNode(final IMgcTreeNode node) {
		if (node != null) {
			if (node instanceof DBNode) {
				return (DBNode)node;
			}
			return getDBNode(node.getParent());
		}
		return null;
	}

	public static DataBase getDataBase(final IMgcTreeNode node) {
		final DBNode dbNode = getDBNode(node);
		return dbNode != null ? dbNode.getDataBase() : null;
	}

	public static SchemaNode getSchemaNode(final IMgcTreeNode node) {
		if (node != null) {
			if (node instanceof SchemaNode) {
				return (SchemaNode)node;
			}
			return getSchemaNode(node.getParent());
		}
		return null;
	}

	public static String getSchemaName(final IMgcTreeNode node) {
		final SchemaNode schemaNode = getSchemaNode(node);
		return schemaNode != null ? schemaNode.getName() : null;
	}

	public static TableNode getTableNode(final IMgcTreeNode node) {
		if (node != null) {
			if (node instanceof TableNode) {
				return (TableNode)node;
			}
			return getTableNode(node.getParent());
		}
		return null;
	}

}