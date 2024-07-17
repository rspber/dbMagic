package db.magic.ui.views.dbs;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;

import db.magic.MgcJob;
import db.magic.MgcJobRunnable;
import db.magic.db.DataBase;
import db.magic.tree.MgcTreeNodeUtil;

public class RefreshSchemaFoldersRunner implements MgcJobRunnable {

	private final TreeViewer viewer;
	private final SchemaNode schema;

	public RefreshSchemaFoldersRunner(final TreeViewer viewer, final SchemaNode schema) {
		this.viewer = viewer;
		this.schema = schema;
	}

	@Override
	public void run(final MgcJob job) throws Exception {

		schema.removeChildAll();
		Display.getDefault().asyncExec( () -> MgcTreeNodeUtil.refreshTreeNode(viewer, schema) );

		final DataBase db = DBsUtil.getDataBase(schema);

		final String[] tableTypes = { "TABLE", "VIEW" };
		for (final String tableType : tableTypes ) {
			final FolderNode folderNode = new FolderNode(tableType);
			folderNode.addTables(SchemaByTypesTablesSupplyer.getTables(db, schema.getName(), tableType));
			schema.addChild(folderNode);
		}
		schema.setExpanded(true);
		Display.getDefault().asyncExec( () -> MgcTreeNodeUtil.expandTreeNode(viewer, schema) );
	}

}