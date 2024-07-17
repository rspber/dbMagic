package db.magic.ui.views.dbs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;

import db.magic.MgcJob;
import db.magic.MgcJobRunnable;
import db.magic.db.DataBase;
import db.magic.tree.MgcTreeNodeUtil;
import db.magic.tree.IMgcTreeNode;

public class RefreshFolderTablesRunner implements MgcJobRunnable {

	final TreeViewer viewer;
	final FolderNode folderNode;

	public RefreshFolderTablesRunner(final TreeViewer viewer, final FolderNode folderNode) {
		this.viewer     = viewer;
		this.folderNode = folderNode;
	}

	@Override
	public void run(final MgcJob job) throws Exception {

		final DataBase db = DBsUtil.getDataBase(folderNode);
		final String schemaName = DBsUtil.getSchemaName(folderNode);

		final TableInfo[] tables = SchemaByTypesTablesSupplyer.getTables(db, schemaName, folderNode.getName());

		final List<String> newTableList = new ArrayList<>();

		for (final TableInfo tableInfo : tables) {

			final TableNode newTableNode = new TableNode(tableInfo.getName());
			newTableList.add(tableInfo.getName());

			final IMgcTreeNode tableNode = folderNode.getChild(tableInfo.getName());
			if (tableNode == null) {
				folderNode.addChild(newTableNode);
				Display.getDefault().asyncExec( () -> MgcTreeNodeUtil.refreshTreeNode(viewer, newTableNode) );
			}
			else {
				Display.getDefault().asyncExec( () -> MgcTreeNodeUtil.refreshTreeNode(viewer, tableNode) );
			}
		}

		folderNode.forEachReverse( node -> {
			if (!newTableList.contains(node.getName())) {
				folderNode.removeChild(node);
			}
		});

		Display.getDefault().asyncExec( () -> MgcTreeNodeUtil.refreshTreeNode(viewer, folderNode) );
	}

}