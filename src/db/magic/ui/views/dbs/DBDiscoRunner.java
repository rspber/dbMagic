package db.magic.ui.views.dbs;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;

import db.magic.MgcJob;
import db.magic.MgcJobRunnable;
import db.magic.db.Transaction;
import db.magic.db.TransactionManager;
import db.magic.tree.MgcTreeNodeUtil;

public class DBDiscoRunner implements MgcJobRunnable {

	private final TreeViewer viewer;
	private final DBNode dbNode;

	public DBDiscoRunner(final TreeViewer viewer, final DBNode dbNode) {
		this.viewer = viewer;
		this.dbNode = dbNode;
	}

	@Override
	public void run(final MgcJob job) throws Exception {
		final Transaction trans = TransactionManager.getInstance(dbNode.getDataBase());
		dbNode.setConnected(false);
		trans.closeConnection();
		dbNode.removeChildAll();
		dbNode.setExpanded(false);
		Display.getDefault().asyncExec( () -> MgcTreeNodeUtil.refreshTreeNode(viewer, dbNode) );
	}

}