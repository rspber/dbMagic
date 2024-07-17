package db.magic.ui.views.dbs;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

import db.magic.MgcJob;

public class DBsDoubleClickHandler implements IDoubleClickListener {

	private boolean showDialog = false;

	public DBsDoubleClickHandler() {
	}

	public void doubleClick(final DoubleClickEvent event) {

		final Viewer view = event.getViewer();
		final ISelection selection = event.getSelection();

		if (view instanceof TreeViewer && selection instanceof StructuredSelection) {
			final TreeViewer viewer = (TreeViewer)view;
			final Object element = ((StructuredSelection)selection).getFirstElement();

			if (element instanceof DBNode) {
				if (!viewer.getExpandedState(element)) {
					viewer.expandToLevel(element, 1);
					final DBNode dbNode = (DBNode)element;
					if (!dbNode.isExpanded()) {
						dbNode.setConnected(true);
						dbNode.setExpanded(true);
						MgcJob.run("Tree->ConnectDB", false, new DBConnectNodeRunner(viewer, dbNode));
					}
				}
				else {
					viewer.collapseToLevel(element, 1);
				}
			}
			else if (element instanceof SchemaNode) {

				if (!viewer.getExpandedState(element)) {
					viewer.expandToLevel(element, 1);

					final SchemaNode schemaNode = (SchemaNode)element;
					if (!schemaNode.isExpanded()) {
						schemaNode.setExpanded(true);
						MgcJob.run("Tree->RefreshSchema", showDialog, new RefreshSchemaFoldersRunner(viewer, schemaNode));
					}
				}
				else {
					viewer.collapseToLevel(element, 1);
				}
			}
			else if (element instanceof FolderNode) {
				if (!viewer.getExpandedState(element)) {
					viewer.expandToLevel(element, 1);

					final FolderNode folderNode = (FolderNode)element;
					if (!folderNode.isExpanded()) {
						folderNode.setExpanded(true);
	
						final SchemaNode schema = DBsUtil.getSchemaNode(folderNode);
						if (schema != null) {
							MgcJob.run("Tree->RefreshFolder", showDialog, new RefreshFolderTablesRunner(viewer, folderNode));
						}
					}
				}
				else {
					viewer.collapseToLevel(element, 1);
				}
			}
			else if (element instanceof TableNode) {
				MgcJob.run("Tree->OpenDBTableResultView", showDialog, new DBOpenTableResultViewRunner((TableNode)element));
			}
		}
	}

}