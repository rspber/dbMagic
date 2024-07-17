package db.magic.ui.views.dbs.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import db.magic.MgcJob;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;
import db.magic.ui.views.dbs.DBConnectNodeRunner;
import db.magic.ui.views.dbs.DBNode;

public class DBConnectActionRunner extends Action implements Runnable {

	private static final String M_STORE_ID = "DBConnectAction";
	private static final String M_Tit = "Tit";
	private static final String M_Msg = "Msg";

	private final MsgStore msg = MgcMsg.getDefault().getMsgStore(M_STORE_ID);
	
	private final TreeViewer viewer;

	public DBConnectActionRunner(final TreeViewer viewer) {
		this.viewer = viewer;
		setText(msg.get(M_Tit));
		setToolTipText(msg.get(M_Msg));
		setEnabled(true);
	}

	@Override
	public void run() {
		((IStructuredSelection)viewer.getSelection()).forEach( (element) -> {
			if (element instanceof DBNode) {
				proc((DBNode)element);
			}
		});
	}

	private void proc(final DBNode dbNode) {
		if (!dbNode.isConnected()) {
			MgcJob.run("Action->ConnectDB", false, new DBConnectNodeRunner(viewer, dbNode));
		}
	}

}