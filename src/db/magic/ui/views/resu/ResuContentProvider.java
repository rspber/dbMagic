package db.magic.ui.views.resu;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ResuContentProvider implements IStructuredContentProvider {

	Object[] contents;

	public ResuContentProvider() {}

	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
		if (newInput instanceof Object[]) {
			contents = (Object[])newInput;
		}
		else {
			contents = null;
		}
	}

	public Object[] getElements(final Object inputElement) {
		final Object[] out = new Object[contents.length - 1];
		System.arraycopy(contents, 1, out, 0, out.length);
		return out;
	}

	public void dispose() {
		contents = null;
	}

	public Object[] getContents() {
		return contents;
	}

}