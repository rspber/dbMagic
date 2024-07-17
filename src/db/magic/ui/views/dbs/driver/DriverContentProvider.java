package db.magic.ui.views.dbs.driver;

import org.eclipse.jface.viewers.ITreeContentProvider;

class DriverContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getElements(final Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object getParent(final Object element) {
		if (element instanceof TreeLeaf) {
			return ((TreeLeaf)element).getParent();
		}
		return null;
	}

	@Override
	public Object[] getChildren(final Object parentElement) {
		if (parentElement instanceof TreeNode) {
			return ((TreeNode)parentElement).getChildrens();
		}
		return new Object[0];
	}

	@Override
	public boolean hasChildren(final Object element) {
		if (element instanceof TreeNode) {
			return ((TreeNode)element).hasChildren();
		}
		return false;
	}

}