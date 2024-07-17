package db.magic.ui.views.sqh;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import db.magic.DBMagicPlugin;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;
import db.magic.tree.IMgcTreeNode;

public class SqHDeleteActionRunner extends Action implements Runnable {

	private static final String M_STORE_ID = "SqHDeleteAction";
	private static final String M_Tit = "Tit";
	private static final String M_Msg = "Msg";
	private static final String M_Conf = "Conf";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);

	private final StructuredViewer viewer;

	public SqHDeleteActionRunner(final StructuredViewer viewer) {
		this.viewer = viewer;
		setText(msg.get(M_Tit));
		setToolTipText(msg.get(M_Msg));
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
	}

	@Override
	public void run() {
		((IStructuredSelection)viewer.getSelection()).forEach( object -> {
			final IMgcTreeNode node = (IMgcTreeNode)object;
			if (DBMagicPlugin.confirmDialog(msg.get(M_Conf) + node.getName())) {
				node.getParent().removeChild(node);
				viewer.refresh(node.getParent());
			}
		});
	}

}