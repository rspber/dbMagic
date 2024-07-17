package db.magic.ui.views.dbs.driver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import db.magic.db.DBManager;

public class JDBCDriverDialog extends TitleAreaDialog {

	private TreeViewer viewer;
	private List<String> targetNames;

	private final TreeNode ivisibleRoot = new TreeNode("ivisible");

	public JDBCDriverDialog(final Shell shell) {
		super(shell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}

	@Override
	protected void createButtonsForButtonBar(final Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		setMessage("JDBC Driver");
		final Composite composite = (Composite) super.createDialogArea(parent);

		viewer = new TreeViewer(composite, SWT.BORDER | SWT.MULTI);
		viewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));

		DBManager.getDefault().forEach( db -> {
			final DBTypeNode dbNode = new DBTypeNode(db.driverName);
			if (db.classPaths != null) {
				db.classPaths.forEach( path -> {
					final File file = new File(path);
					if (file.exists()) {
						dbNode.addChild(file.isDirectory() ? new FolderNode(path) : new FileNode(path));
					}
				});
			}
			ivisibleRoot.addChild(dbNode);
		});

		viewer.setContentProvider(new DriverContentProvider());
		viewer.setLabelProvider(new DriverLabelProvider());
		viewer.setInput(ivisibleRoot);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(final SelectionChangedEvent event) {
				selectionChangeHandler(event);
			}
		});
		viewer.expandAll();
		return composite;
	}

	@Override
	protected Control createContents(final Composite parent) {
		final Control control = super.createContents(parent);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
		return control;
	}

	private void selectionChangeHandler(final SelectionChangedEvent event) {
		final StructuredSelection ss = (StructuredSelection)event.getSelection();
		targetNames = new ArrayList<>();
		boolean enabeld = false;
		for (final Object element : ss) {
			if (!(element instanceof DBTypeNode)) {
				enabeld = true;
				final TreeLeaf leaf = (TreeLeaf)element;
				targetNames.add(leaf.getName());
			}
		}
		getButton(IDialogConstants.OK_ID).setEnabled(enabeld);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(480, 450);
	}

	public List<String> getTargetNames() {
		return targetNames;
	}

	@Override
	protected boolean isResizable() {
	    return true;
	}	

}