package db.magic.ui.views.resu;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.ide.IDE;

import db.magic.MgcPlugC;
import db.magic.DBMagicPlugin;
import db.magic.db.DataBase;
import db.magic.db.ResuTableElement;

public class OpenResuViewRunner implements Runnable {

	private final DataBase db;
	private final String partName;
	private final String finishSql;
	private final ResuTableElement[] elements;
	private final String secondaryId;

	public OpenResuViewRunner(final DataBase db, final String partName, final String finishSql,
			final ResuTableElement[] elements, final String secondaryId) {
		this.db = db;
		this.partName = partName;
		this.finishSql = finishSql;
		this.elements = elements;
		this.secondaryId = secondaryId;
	}

	@Override
	public void run() {
		try {
			final IWorkbenchPage page = DBMagicPlugin.getPage();
			final ResuEditorInput input = new ResuEditorInput(db, finishSql);
			final IEditorPart editorPart = IDE.openEditor(page, input, MgcPlugC.ResuView, false);
			if (editorPart instanceof ResuView) {
				final ResuView sqresu = (ResuView)editorPart;
				sqresu.createMainPage(partName, elements);
				DBMagicPlugin.showView(MgcPlugC.SQLExecView, secondaryId);
			}
		}
		catch (Exception e) {
			DBMagicPlugin.showError(e);
		}
	}

}