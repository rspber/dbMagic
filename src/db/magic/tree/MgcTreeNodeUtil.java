package db.magic.tree;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;

public class MgcTreeNodeUtil {

	public static void expandTreeNode(final TreeViewer viewer, final IMgcTreeNode node) {
		viewer.expandToLevel(node, 1);
		refreshTreeNode(viewer, node);
	}

	public static void refreshTreeNode(final TreeViewer viewer, final IMgcTreeNode node) {
		viewer.refresh(node);
		viewer.getControl().notifyListeners(SWT.Selection, null);
	}

}