package db.magic.ui.views.dbs.dialogs;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.Wizard;

import db.magic.DBMagicPlugin;
import db.magic.db.DBManager;
import db.magic.db.DataBase;
import db.magic.db.DriverManager;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;

public class DataBaseWizard extends Wizard {

	private static final String M_STORE_ID = "DBWizard";
	private static final String M_Tit = "Tit";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);

	private final DBManager dbManager = DBManager.getDefault();

	DataBase olddb;
	DataBase newdb;
	private DBWizardPage1 page1;
	private DBWizardPage2 page2;
	private ISelection selection;

	public DataBaseWizard(final ISelection selection) {
		this(selection, null);
	}

	public DataBaseWizard(final ISelection selection, final DataBase olddb) {
		super();
		setWindowTitle(msg.get(M_Tit));
		this.selection = selection;
		this.olddb = olddb;
		setNeedsProgressMonitor(true);
		setHelpAvailable(false);
	}

	public void addPages() {
		page1 = new DBWizardPage1(selection);
		page2 = new DBWizardPage2(selection);
		addPage(page1);
		addPage(page2);
	}

	public boolean performFinish() {
		newdb = createNewDataBase();
		dbManager.remove(olddb);
		dbManager.add(newdb);
		DriverManager.getInstance().removeCach(newdb);
		dbManager.finalization();
		return true;
	}

	public boolean canFinish() {
		if (page1.isPageComplete() && page2.isPageComplete()) {
			return true;
		}
		else {
			return false;
		}
	}

	protected DataBase createNewDataBase() {
		try {
			return new DataBase(
				page1.nameText.getText(),
				page2.driverCombo.getText(),
				page2.urlText.getText(),
				page2.userIdText.getText(),
				page2.passwordText.getText(),
				page1.classPaths
			);
		}
		catch (Exception e) {
			DBMagicPlugin.showError(e);
		}
		return null;
	}

	public DataBase getNewDataBase() {
		return newdb;
	}

}