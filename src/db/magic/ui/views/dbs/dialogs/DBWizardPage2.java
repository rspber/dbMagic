package db.magic.ui.views.dbs.dialogs;

import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import db.magic.DBMagicPlugin;
import db.magic.MgcSync;
import db.magic.db.DataBase;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;
import db.magic.ui.pref.MgcSQLPref;
import db.magic.ui.ui.UI;
import db.magic.ui.views.dbs.TestConnectionRunner;
import db.magic.util.Str;

public class DBWizardPage2 extends WizardPage {

	private static final String M_STORE_ID = "DBWizardPage2";
	private static final String M_Tit = "Tit";
	private static final String M_JDCNam = "JDCNam";
	private static final String M_DBCon  = "DBCon";
	private static final String M_Usr    = "Usr";
	private static final String M_Pwd    = "Pwd";
	private static final String M_TesCon = "TesCon";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);

	private static final int BUTTON_WIDTH = 100;

	CCombo driverCombo;
	Text urlText;
	Text userIdText;
	Text passwordText;

	Button testBtn;
	Text test1Text, test2Text, test3Text;

	boolean searchDriverFlag = true;

	public DBWizardPage2(final ISelection selection) {
		super("page.2");
		setTitle(msg.get(M_Tit));
		setDescription("...");
		setPageComplete(true);
	}

	@Override
	public void createControl(final Composite parent) {
		final Composite container = UI.compositeGrid(parent)
				.gridLayout(1, false).marginWH(5, 5).spacingHV(0, 10)
				.client().apply();

		final Group group = UI.groupGrid(container)
				.gridLayout(2, false).marginWH(5, 15).spacingHV(0, 10)
				.client().apply();

		UI.label(group).text(msg.get(M_JDCNam)).apply();
		driverCombo = UI.combo(group, true).eol().apply();

		UI.label(group).text(msg.get(M_DBCon)).apply();
		urlText = UI.text(group, SWT.SINGLE | SWT.BORDER).eol().apply();

		UI.label(group).text(msg.get(M_Usr)).apply();
		userIdText = UI.text(group, SWT.SINGLE | SWT.BORDER).eol().apply();

		UI.label(group).text(msg.get(M_Pwd)).apply();
		passwordText = UI.text(group, SWT.SINGLE | SWT.BORDER).eol().apply();
		passwordText.setEchoChar('*');

		final DataBase olddb = ((DataBaseWizard)getWizard()).olddb;
		if (olddb != null ) {
			driverCombo.add(Str.nn(olddb.driverName));
			driverCombo.select(0);
			urlText.setText(Str.nn(olddb.url));
			userIdText.setText(Str.nn(olddb.user));
			passwordText.setText(Str.nn(olddb.password));
		}

		testBtn = UI.button(container, SWT.PUSH).text(msg.get(M_TesCon))
				.minWidth(BUTTON_WIDTH).apply();
		testBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(final SelectionEvent e) {
				pressTestBtn();
			}
		});
		test1Text = UI.text(container, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY).eol().apply();
		test2Text = UI.text(container, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY).eol().apply();
		test3Text = UI.text(container, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY).eol().apply();

		setControl(container);
	}

	private void pressTestBtn() {
		final DataBase db = ((DataBaseWizard)getWizard()).createNewDataBase();
		final TestConnectionRunner test = new TestConnectionRunner(db);
		final Thread th = new Thread(test);
		th.start();
		final int timeout = MgcSQLPref.getDefault().ConnectTimeout();
		MgcSync.sync(th, timeout);
		final String msg = test.getMessage();
		test1Text.setText(msg);
		test2Text.setText(msg);
		test3Text.setText(msg);
	}

	private void updateComboBox(final List<String> classpathList) {
		driverCombo.removeAll();
		try {
			final String[] classpaths = classpathList.toArray(new String[0]);
			final String[] drivers = JDBCDriverUtil.searchDriver(getShell(), classpaths, getClass());

			if (drivers == null || drivers.length == 0) {
				setPageComplete(false);
			}
			else {
				for (int i = 0; i < drivers.length; i++) {
					final String string = drivers[i];
					driverCombo.add(string);
					final DataBase olddb = ((DataBaseWizard)getWizard()).olddb;
					if (olddb == null) {
						driverCombo.select(0);
					}
					else {
						if (Str.eq(string, olddb.driverName)) {
							driverCombo.select(i);
						}
					}
				}
			}
		}
		catch (InterruptedException e) {
			DBMagicPlugin.showError(e);
		}
		catch (Exception e) {
			DBMagicPlugin.showError(e);
		}
	}

	public void setVisible(final boolean visible) {
		super.setVisible(visible);

		if (visible) {
			if (searchDriverFlag) {
				final DataBaseWizard wiz = (DataBaseWizard)getWizard();
				final DBWizardPage1 page = (DBWizardPage1)wiz.getPreviousPage(this);
				updateComboBox(page.classPaths);
			}
		}
	}

}