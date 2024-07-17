package db.magic.ui.pref.descr;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

import db.magic.tree.IMgcTreeNode;

public class DescrDoubleClickHandler implements IDoubleClickListener {

	public void doubleClick(final DoubleClickEvent event) {

		final Viewer view = event.getViewer();
		final ISelection selection = event.getSelection();

		if (view instanceof TreeViewer && selection instanceof StructuredSelection) {
			final TreeViewer viewer = (TreeViewer)view;
			final Object element = ((StructuredSelection)selection).getFirstElement();
			if (element instanceof IMgcTreeNode) {
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