package db.magic.ui.views.dbs.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;

import db.magic.MgcImg;
import db.magic.DBMagicPlugin;
import db.magic.db.DBManager;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;
import db.magic.ui.views.dbs.DBNode;
import db.magic.ui.views.dbs.DBsView;

public class DBRemoveActionRunner extends Action implements Runnable {

	private static final String M_STORE_ID = "DBRemoveAction";
	private static final String M_Tit = "Tit";
	private static final String M_Msg = "Msg";
	private static final String M_Conf = "Conf";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);

	private final DBsView view;

	public DBRemoveActionRunner(final DBsView view) {
		this.view = view;
		setText(msg.get(M_Tit));
		setToolTipText(msg.get(M_Msg));
		setEnabled(true);
		setImageDescriptor(MgcImg.getDefault().getImageDescriptor(MgcImg.IMG_CODE_DB_DELETE));
	}

	@Override
	public void run() {
		((IStructuredSelection)view.getViewer().getSelection()).forEach( element -> {
			if (element instanceof DBNode) {
				proc((DBNode)element);
			}
		});
	}

	private void proc(final DBNode dbNode) {
		if (DBMagicPlugin.confirmDialog(msg.get(M_Conf) + " " + dbNode.getName())) {
			DBManager.getDefault().remove(dbNode.getDataBase());
			dbNode.getParent().removeChild(dbNode);
			view.getViewer().refresh();
		}
	}

}