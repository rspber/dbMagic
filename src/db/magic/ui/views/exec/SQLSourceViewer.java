package db.magic.ui.views.exec;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import db.magic.DBMagicPlugin;
import db.magic.MgcJob;
import db.magic.db.DataBase;
import db.magic.dictios.FDictiosSupplyer;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;
import db.magic.ui.views.sqh.SqHManager;
import db.magic.ui.views.sqh.SqHQueryNode;
import db.magic.util.Str;

public class SQLSourceViewer extends ProjectionViewer {

	private static final String M_STORE_ID = "SQLSourceViewer";
	private static final String M_NoSQL = "NoSQL";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);

	private FDictiosSupplyer dictios = FDictiosSupplyer.getDefault();

	private final SqHManager mgr = SqHManager.getDefault();

	private final SQLExecView sqlExecView;

	public SQLSourceViewer(final SQLExecView sqlExecView, final Composite parent, final int styles) {
		super(parent, new CompositeRuler(), null, false, styles);
		this.sqlExecView = sqlExecView;
		setDocument(new Document());
	}

	public boolean canDoOperation(final int operation) {
		switch (operation) {
		case GlobalAction.EXEC_SQL:
		case GlobalAction.NEXT_SQL:
		case GlobalAction.PREV_SQL:
		case GlobalAction.CLEAR:
			return isEditable();
		default:
			return super.canDoOperation(operation);
		}
	}

	public void doOperation(final int operation) {
		switch (operation) {
		case GlobalAction.EXEC_SQL:
			doExecSQL();
			return;
		case GlobalAction.NEXT_SQL:
			doSqHQueryNode(mgr.nextItem());
			return;
		case GlobalAction.PREV_SQL:
			doSqHQueryNode(mgr.prevItem());
			return;
		case GlobalAction.CLEAR:
			getDocument().set(Str.EMPTY);
			return;

		default:
			super.doOperation(operation);
		}
	}

	// SelectSQL

	private void doExecSQL() {

		try {
			final List<String> uaiValues = new ArrayList<>();
			for( final Object[] cs : sqlExecView.uais) {
				if (cs.length >= 2) {
					uaiValues.add(((Text)cs[1]).getText());
				}
			}
			final String sqlQuery = getDocument().get();
			final DataBase db = sqlExecView.getDataBase();
			final int limit = sqlExecView.sqlLimit();
			final String secId = sqlExecView.getViewSite().getSecondaryId();

			if (!Str.isBlank(sqlQuery)) {
				MgcJob.run("Action->ExecSQL", false, new ExecSQLAndOpenViewRunner(db, sqlQuery, uaiValues, limit, secId));
			}
			else {
				DBMagicPlugin.showInfo(msg.get(M_NoSQL));
			}
		}
		catch( Exception e) {
			DBMagicPlugin.showError(e);
		}
	}

	//SqH

	private void doSqHQueryNode(final SqHQueryNode sqHQueryNode) {
		if (sqHQueryNode != null) {
			getDocument().set(sqHQueryNode.getName());
			setSelectedRange(sqHQueryNode.getName().length(), 0);
			invalidateTextPresentation();
		}
		else {
			getDocument().set(Str.EMPTY);
		}
		sqlExecView.updateSqHQueryButtons();
	}

}