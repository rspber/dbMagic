package db.magic.ui.pref;

import db.magic.store.MgcPrefStoreManager;
import db.magic.store.PrefStore;
import db.magic.util.Num;

public class MgcSQLPref {

	private static final String P_PREF_STORE = "DBSql"; 
	
	static final String P_NO_CONFIRM_CONNECT_DB = "NoConfirmConnectDB";
	static final String P_CONNECT_TIMEOUT       = "ConnectTimeout";
	static final String P_TABLE_ROW_LIMIT       = "TableRowLimit";
	static final String P_EXEC_SQL_ROW_LIMIT    = "ExecSqlRowLimit";
//	static final String P_NO_CONFIRM_COMMIT     = "NoConfirmCommit";

	private final PrefStore prefStore = MgcPrefStoreManager.getDefault().getPrefStore(P_PREF_STORE);

	public static PrefStore getPrefStore() {
		return getDefault().prefStore;
	}

	private static MgcSQLPref fDefault;
	public static MgcSQLPref getDefault() {
		if( fDefault == null ) {
			fDefault = new MgcSQLPref();
		}
		return fDefault;
	}

	public boolean NoConfirmConnectDB() {
		return prefStore.getBoolean(P_NO_CONFIRM_CONNECT_DB);
	}

	public void setNoConfirmConnectDB(final boolean no) {
		prefStore.setValue(P_NO_CONFIRM_CONNECT_DB, no);
	}

	public String TableRowLimit() {
		return prefStore.getString(P_TABLE_ROW_LIMIT);
	}

	public String ExecSqlRowLimit() {
		return prefStore.getString(P_EXEC_SQL_ROW_LIMIT);
	}

	public int ConnectTimeout() {
		return Num.toInt(prefStore.getString(P_CONNECT_TIMEOUT));
	}
/*
	public boolean NoConfirmCommit() {
		return prefStore.getBoolean(P_CONNECT_TIMEOUT);
	}
*/
}