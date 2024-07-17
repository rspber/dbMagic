package db.magic.ui.views.dbs.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;

import db.magic.MgcImg;
import db.magic.MgcJob;
import db.magic.DBMagicPlugin;
import db.magic.db.DataBase;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;
import db.magic.ui.views.dbs.DBDiscoRunner;
import db.magic.ui.views.dbs.DBNode;
import db.magic.ui.views.resu.ResuEditorInput;
import db.magic.util.Str;

public class DBDiscoActionRunner extends Action implements Runnable {

	private static final String M_STORE_ID = "DBDiscoAction";
	private static final String M_Tit = "Tit";
	private static final String M_Msg = "Msg";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);

	private final TreeViewer viewer;

	public DBDiscoActionRunner(final TreeViewer viewer) {
		this.viewer = viewer;
		setText(msg.get(M_Tit));
		setToolTipText(msg.get(M_Msg));
		setEnabled(false);
		setImageDescriptor(MgcImg.getDefault().getImageDescriptor(MgcImg.IMG_CODE_DB_DELETE));
	}

	@Override
	public void run() {
		((IStructuredSelection)viewer.getSelection()).forEach( element -> {
			if (element instanceof DBNode) {
				proc((DBNode)element);
			}
		});
	}

	private void proc(final DBNode dbNode) {
		final List<IEditorReference> target = new ArrayList<>();
		final IEditorReference[] references = DBMagicPlugin.getPage().getEditorReferences();
		for (final IEditorReference reference : references) {
			final IEditorInput input;
			try {
				input = reference.getEditorInput();
			}
			catch (Exception e) {
				DBMagicPlugin.showError(e);
				continue;
			}
			if (input instanceof ResuEditorInput) {
				final DataBase db = ((ResuEditorInput)input).getDataBase();
				if (Str.eq(db.getName(), dbNode.getName())) {
					target.add(reference);
				}
			}
		}
		if (target.size() > 0) {
			DBMagicPlugin.getCloseEditors(target.toArray(new IEditorReference[0]));
		}
		MgcJob.run("Action->DisconnectDB", false, new DBDiscoRunner(viewer, dbNode));
	}

}