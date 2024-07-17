package db.magic.ui.views.exec;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import db.magic.db.DBManager;
import db.magic.db.DataBase;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;
import db.magic.ui.ui.UI;

public class ComboSelectDB extends ControlContribution {

	private static final String M_STORE_ID = "ComboSelectDB";
	private static final String M_NoDB = "NoDB";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);

	private CCombo selectCombo;

	public ComboSelectDB() {
		super("SelectDataBase");
	}

	@Override
	protected Control createControl(final Composite parent) {
		selectCombo = UI.combo(parent, true).width(200).apply();
		initializeSelectCombo();
		return selectCombo;
	}

	private void initializeSelectCombo() {
		final DataBase olddb = selectedDataBase();
		selectCombo.removeAll();
		DBManager.getDefault().forEach( db -> {
			selectCombo.add(db.getName());
		});
		updateCombo(olddb);
	}

	void updateCombo(final DataBase newdb) {
		selectCombo.select(DBManager.getDefault().getDataBaseIndex(newdb));
	}

	private DataBase selectedDataBase() {
		return DBManager.getDefault().getDataBase(selectCombo.getSelectionIndex());
	}

	public DataBase getDataBase() throws Exception {
		final DataBase db = selectedDataBase();
		if (db == null) {
			throw new Exception(msg.get(M_NoDB));
		}
		return db;
	}

}