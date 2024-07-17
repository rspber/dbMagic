package db.magic.ui.views.dbs.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import db.magic.MgcImg;
import db.magic.DBMagicPlugin;
import db.magic.db.DBManager;
import db.magic.db.DataBase;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;
import db.magic.ui.views.dbs.DBNode;
import db.magic.ui.views.dbs.DBsView;
import db.magic.util.Str;

public class DBCopyActionRunner extends Action implements Runnable {

	private static final String M_STORE_ID = "DBCopyAction";
	private static final String M_Tit = "Tit";
	private static final String M_Msg = "Msg";
	private static final String M_DupExt = "DupExt";
	private static final String M_NewTit = "NewTit";
	private static final String M_NewMsg = "NewMsg";
	private static final String M_IsEmp = "IsEmp";
	private static final String M_Exi = "Exi";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);

	private final DBsView view;

	public DBCopyActionRunner(final DBsView view) {
		this.view = view;
		setText(msg.get(M_Tit));
		setToolTipText(msg.get(M_Msg));
		setImageDescriptor(MgcImg.getDefault().getImageDescriptor(MgcImg.IMG_CODE_DB_COPY));
	}

	@Override
	public void run() {
		final TreeViewer viewer = view.getViewer();
		final Object element = ((StructuredSelection)viewer.getSelection()).getFirstElement();
		if (element instanceof DBNode) {
			final DBNode dbNode = (DBNode)element;
			final DataBase db = dbNode.getDataBase();
			final DataBase newdb = (DataBase)db.clone();
			final Shell shell = DBMagicPlugin.getShell();
			final String newName = db.getName() + msg.get(M_DupExt);
			final InputDialog dialog = new InputDialog(shell, msg.get(M_NewTit), msg.get(M_NewMsg), newName,
				name -> {
					if (Str.isBlank(name)) {
						return name + msg.get(M_IsEmp);
					}
					if (DBManager.getDefault().hasDataBase(name)) {
						return name + msg.get(M_Exi);
					}
					return null;
				});
			if (dialog.open() == InputDialog.OK) {
				newdb.setName(dialog.getValue());
				final DBNode addDBNode = view.addDBNode(newdb);
				viewer.refresh();
				viewer.setSelection(new StructuredSelection(addDBNode), true);
				viewer.getControl().notifyListeners(SWT.Selection, null);
				DBManager.getDefault().add(newdb);
			}
		}
	}

}