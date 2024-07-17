package db.magic.ui.views.dbs.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.PartInitException;

import db.magic.MgcJob;
import db.magic.MgcPlugC;
import db.magic.DBMagicPlugin;
import db.magic.MgcSync;
import db.magic.db.DataBase;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;
import db.magic.tree.IMgcTreeNode;
import db.magic.ui.pref.MgcSQLPref;
import db.magic.ui.views.dbs.DBConnectNodeRunner;
import db.magic.ui.views.dbs.DBNode;
import db.magic.ui.views.dbs.DBsView;

public class DBConfConnActionRunner extends Action implements Runnable {

	private static final String M_STORE_ID = "DBConfConnAction";
	private static final String M_NotCon = "NotCon";
	private static final String M_WilNot = "WilNot";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);

	private final static int YES = 2;

	private final DataBase db;

	public DBConfConnActionRunner(final DataBase db) {
		this.db = db;
	}

	@Override
	public void run() {
		try {
			final boolean b = MgcSQLPref.getDefault().NoConfirmConnectDB();
			if (b) {
				connect(db);
			}
			else {
				final MessageDialogWithToggle dialog = DBMagicPlugin.confirmDialogWithToggle(
					msg.get(M_NotCon), msg.get(M_WilNot), false);
				if (dialog.getReturnCode() == YES) {
					MgcSQLPref.getDefault().setNoConfirmConnectDB(dialog.getToggleState());
					connect(db);
				}
			}
		}
		catch (PartInitException e) {
			DBMagicPlugin.showError(e);
		}
	}

	private void connect(final DataBase db) throws PartInitException {
		DBsView view = (DBsView)DBMagicPlugin.findView(MgcPlugC.DBsView);
		if (view == null) {
			view = (DBsView)DBMagicPlugin.showView(MgcPlugC.DBsView);
		}
		final TreeViewer viewer = view.getViewer();
		final IMgcTreeNode node = view.rootNode.getChild(db.getName());
		if (node instanceof DBNode) {
			final DBNode dbNode = (DBNode)node;
			dbNode.setConnected(true);
			MgcSync.sync(MgcJob.run("Action->ConfirmConnectDB", false, new DBConnectNodeRunner(viewer, dbNode)));
		}
	}

}