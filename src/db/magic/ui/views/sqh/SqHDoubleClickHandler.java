package db.magic.ui.views.sqh;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

import db.magic.MgcPlugC;
import db.magic.DBMagicPlugin;
import db.magic.tree.IMgcTreeNode;
import db.magic.ui.views.exec.SQLExecView;

public class SqHDoubleClickHandler implements IDoubleClickListener {

	public void doubleClick(final DoubleClickEvent event) {

		final Viewer view = event.getViewer();
		final ISelection selection = event.getSelection();

		if (view instanceof TreeViewer && selection instanceof StructuredSelection) {
			final TreeViewer viewer = (TreeViewer)view;
			final Object element = ((StructuredSelection)selection).getFirstElement();
			if (element instanceof SqHQueryNode) {
				final SqHQueryNode sqHQueryNode = (SqHQueryNode)element;
				final SQLExecView sqlExecView = (SQLExecView)DBMagicPlugin.showView(MgcPlugC.SQLExecView);
				if (sqlExecView != null) {
					sqlExecView.setSqlText(sqHQueryNode.getName());
//					SqHSupplyer.getDefault().modifyCurrentPosition(sqhItem);
					sqlExecView.updateSqHQueryButtons();
				}
			}
			else if (element instanceof IMgcTreeNode) {
				if (!viewer.getExpandedState(element)) {
					viewer.expandToLevel(element, 1);
				}
				else {
					viewer.collapseToLevel(element, 1);
				}
			}
		}
	}

}