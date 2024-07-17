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
import db.magic.ui.views.dbs.DBsView;
import db.magic.ui.views.dbs.dialogs.DataBaseWizard;

public class DBAddActionRunner extends Action implements Runnable {

	private static final String M_STORE_ID = "DBAddAction";
	private static final String M_Tit = "Tit";
	private static final String M_Msg = "Msg";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);

	private final DBsView view;

	public DBAddActionRunner(final DBsView view) {
		this.view = view;
		setText(msg.get(M_Tit));
		setToolTipText(msg.get(M_Msg));
		setImageDescriptor(MgcImg.getDefault().getImageDescriptor(MgcImg.IMG_CODE_DB_ADD));
	}

	@Override
	public void run() {
		final TreeViewer viewer = view.getViewer();
		final Shell shell = DBMagicPlugin.getShell();
		final DataBaseWizard wizard = new DataBaseWizard(viewer.getSelection());
		final WizardDialog dialog = new WizardDialog(shell, wizard);
		if (dialog.open() == IDialogConstants.OK_ID) {
			final DataBase newdb = wizard.getNewDataBase();
			final DBNode addDB = view.addDBNode(newdb);
			viewer.refresh();
			viewer.setSelection(new StructuredSelection(addDB), true);
			viewer.getControl().notifyListeners(SWT.Selection, null);
		}
	}

}