package db.magic.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import db.magic.DBMagicPlugin;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;

public class DB {

	private static final String M_STORE_ID = "DB";
	private static final String M_NoDB = "NoDB";

	public static int run(final DataBase db, final SelectSQL select) throws Exception {

		Connection con = TransactionManager.getInstance(db).getConnection();
		return select.executeQuery(con);
	}

	public static Connection getConnection(final DataBase db) throws Exception {

		if (db == null) {
			final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);
			throw new IllegalStateException(msg.get(M_NoDB));
		}

		final DriverManager manager = DriverManager.getInstance();
		final Driver driver = manager.getDriver(db);

		if (driver != null) {
			return driver.connect(db.url, db.getProperties());
		}
		return null;
	}

	public static void close(final Connection con) {
		if (con != null) {
			try {
				con.close();
			}
			catch (SQLException e) {
				DBMagicPlugin.showError(e);
			}
		}
	}

	public static final void close(final ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			}
			catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static final void close(final Statement st) {
		if (st != null) {
			try {
				st.close();
			}
			catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

}