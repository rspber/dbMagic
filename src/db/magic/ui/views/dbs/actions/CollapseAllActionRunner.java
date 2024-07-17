package db.magic.ui.views.dbs.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;

import db.magic.MgcImg;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;

public class CollapseAllActionRunner extends Action implements Runnable {

	private static final String M_STORE_ID = "CollapseAll";
	private static final String M_Tit = "Tit";
	private static final String M_Msg = "Msg";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);

	private final TreeViewer viewer;

	public CollapseAllActionRunner(final TreeViewer viewer) {
		this.viewer = viewer;
		setText(msg.get(M_Tit));
		setToolTipText(msg.get(M_Msg));
		setEnabled(true);
		setImageDescriptor(MgcImg.getDefault().getImageDescriptor(MgcImg.IMG_CODE_COLLAPSE_ALL));
	}

	@Override
	public void run() {
		viewer.collapseAll();
	}

}