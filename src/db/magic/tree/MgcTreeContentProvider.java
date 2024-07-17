package db.magic.tree;

import org.eclipse.jface.viewers.ITreeContentProvider;

public class MgcTreeContentProvider implements ITreeContentProvider {

	public Object[] getElements(final Object inputElement) {
		
		return getChildren(inputElement);
	}

	public Object getParent(final Object element) {
		if (element instanceof IMgcTreeNode) {
			return ((IMgcTreeNode)element).getParent();
		}
		return null;
	}

	public Object[] getChildren(final Object parentElement) {
		if (parentElement instanceof IMgcTreeNode) {
			return ((IMgcTreeNode)parentElement).getChildrens();
		}
		return new Object[0];
	}

	public boolean hasChildren(final Object element) {
		if (element instanceof IMgcTreeNode)
			return ((IMgcTreeNode) element).hasChildren();
		return false;
	}

}