package db.magic.db;

import java.sql.Connection;
import java.sql.SQLException;

import db.magic.util.Str;

public class Transaction {

	private DataBase db;
	private Connection con = null;

	private int count = 0;

	Transaction(final DataBase db) {
		this.db = db;
	}

	public boolean isConnecting() {
		return con != null;
	}

	Transaction checkUserId(final DataBase db) {
		if (this.con == null || !Str.eq(this.db.user, db.user)) {
			DB.close(this.con);
			this.con = null;
			this.db = db;
		}
		return this;
	}

	public Connection getConnection() throws Exception {
		if (con == null) {
			con = DB.getConnection(db);
			con.setAutoCommit(true);
			count = 0;
		}
		return con;
	}

	public void closeConnection() {
		if (con != null) {
			DB.close(con);
			con = null;
		}
	}

	public int getCount() {
		return count;
	}

	public void addCount(final int rowsAffected) {
		count += rowsAffected;
	}

	void clrCount() {
		count = 0;
	}

	public void commit() throws SQLException {
		if (con != null) {
			con.commit();
			count = 0;
		}
	}

	public void rollback() throws SQLException {
		if (con != null) {
			con.rollback();
			count = 0;
		}
	}

}