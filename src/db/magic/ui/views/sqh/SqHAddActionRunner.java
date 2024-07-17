package db.magic.ui.views.sqh;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;
import db.magic.tree.IMgcTreeNode;

public class SqHAddActionRunner extends Action implements Runnable {

	private static final String M_STORE_ID = "SqHAddAction";
	private static final String M_Tit = "Tit";
	private static final String M_Msg = "Msg";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);

	private final TreeViewer viewer;

	public SqHAddActionRunner(final TreeViewer viewer) {
		this.viewer = viewer;
		setText(msg.get(M_Tit));
		setToolTipText(msg.get(M_Msg));
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
	}

	@Override
	public void run() {

		final SqHAddItemData data = new SqHAddItemData(); 

		final Object first = ((IStructuredSelection)viewer.getSelection()).getFirstElement();
		if (first instanceof IMgcTreeNode) {
			IMgcTreeNode picked = (IMgcTreeNode)first; 

			if (picked instanceof SqHQueryNode) {
				data.sql = picked.getName();
				picked = picked.getParent();
			}
			if (picked instanceof SqHFolderNode) {
				data.folder = picked.getName();
				picked = picked.getParent();
			}
			if (picked instanceof SqHCategoryNode) {
				data.category = picked.getName();
				picked = picked.getParent();
			}
		}

		if (new SqHAddDialog(data).open() == IDialogConstants.OK_ID) {
			SqHManager.getDefault().addItemData(data);
			viewer.refresh();
		}
	}

}