package db.magic.ui.views.dbs.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;

import db.magic.MgcJob;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;
import db.magic.ui.views.dbs.DBConnectNodeRunner;
import db.magic.ui.views.dbs.DBNode;
import db.magic.ui.views.dbs.FolderNode;
import db.magic.ui.views.dbs.RefreshFolderTablesRunner;
import db.magic.ui.views.dbs.RefreshSchemaFoldersRunner;
import db.magic.ui.views.dbs.SchemaNode;

public class RefreshAction extends Action {

	private static final String M_STORE_ID = "RefreshAction";
	private static final String M_Tit = "Tit";
	private static final String M_Msg = "Msg";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);


	private final boolean showDialog;
	private final TreeViewer viewer;

	public RefreshAction(final TreeViewer viewer) {
		this.showDialog = true;
		this.viewer = viewer;
		setText(msg.get(M_Tit));
		setToolTipText(msg.get(M_Msg));
		setAccelerator(SWT.F5);
	}

	@Override
	public void run() {
		final Object element = ((StructuredSelection)viewer.getSelection()).getFirstElement();

		if (element instanceof DBNode) {
			final DBNode dbNode = (DBNode)element;
			dbNode.removeChildAll();
			dbNode.setExpanded(false);
			dbNode.setConnected(true);
			MgcJob.run("Action->RefreshConnectDB", false, new DBConnectNodeRunner(viewer, dbNode));
		}
		else if (element instanceof SchemaNode) {
			final SchemaNode schemaNode = (SchemaNode)element;
			schemaNode.setExpanded(true);
			MgcJob.run("Action->RefreshSchemaFolders", showDialog, new RefreshSchemaFoldersRunner(viewer, schemaNode));
		}
		else if (element instanceof FolderNode) {
			final FolderNode folderNode = (FolderNode)element;
			folderNode.setExpanded(true);
			MgcJob.run("Action->RefreshFolderTables", showDialog, new RefreshFolderTablesRunner(viewer, folderNode));
		}
	}

}