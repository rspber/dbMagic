package db.magic.ui.views.dbs.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import db.magic.MgcImg;
import db.magic.DBMagicPlugin;
import db.magic.db.DataBase;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;
import db.magic.ui.views.dbs.DBNode;
import db.magic.ui.views.dbs.dialogs.DataBaseWizard;

public class DBEditActionRunner extends Action implements Runnable {

	private static final String M_STORE_ID = "DBEditAction";
	private static final String M_Tit = "Tit";
	private static final String M_Msg = "Msg";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);

	private final TreeViewer viewer;

	public DBEditActionRunner(final TreeViewer viewer) {
		this.viewer = viewer;
		setText(msg.get(M_Tit));
		setToolTipText(msg.get(M_Msg));
		setEnabled(true);
		setImageDescriptor(MgcImg.getDefault().getImageDescriptor(MgcImg.IMG_CODE_DB_EDIT));
	}

	@Override
	public void run() {
		final Object element = ((StructuredSelection)viewer.getSelection()).getFirstElement();
		if (element instanceof DBNode) {
			final DBNode dbNode = (DBNode)element;
			final Shell shell = DBMagicPlugin.getShell();
			final DataBaseWizard wizard = new DataBaseWizard(viewer.getSelection(), dbNode.getDataBase());
			final WizardDialog dialog = new WizardDialog(shell, wizard);
			if (dialog.open() == IDialogConstants.OK_ID) {
				final DataBase newdb = wizard.getNewDataBase();
				dbNode.setDataBase(newdb);
				viewer.refresh(dbNode);
				viewer.getControl().notifyListeners(SWT.Selection, null);
			}
		}
	}

}