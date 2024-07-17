package db.magic.ui.views.dbs;

import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;

import db.magic.MgcPlugC;
import db.magic.DBMagicPlugin;
import db.magic.db.DataBase;
import db.magic.ui.views.exec.SQLExecView;
import db.magic.util.Str;

public class UpdateComboSQLRunner implements Runnable {

	private final DataBase db;

	public UpdateComboSQLRunner(final DataBase db) {
		this.db = db;
	}

	@Override
	public void run() {
		SQLExecView view = null;
		final IWorkbenchPage page = DBMagicPlugin.getPage();
		final IViewReference[] references = page.getViewReferences();
		for (final IViewReference reference : references) {

			if (Str.eq(MgcPlugC.SQLExecView, reference.getId())) {
				view = (SQLExecView)reference.getView(true);
				if (view != null) {
					view.updateCombo(db);
				}
			}
		}
		if (view == null) {
			view = (SQLExecView)DBMagicPlugin.showView(MgcPlugC.SQLExecView);
			view.updateCombo(db);
		}
	}

}