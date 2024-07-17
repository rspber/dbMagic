package db.magic.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class SelectSQL {

	private final String query;

	public SelectSQL(final String query) {
		this.query = query;
	}

	public abstract void next(final ResultSet rs) throws SQLException;

	public int executeQuery(final Connection con) {
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			int rowNum = 0;
			while( rs.next() ) {
				++rowNum;
				next(rs);
			}
			return rowNum; 
		}
		catch( Exception e ) {
			try {
				con.rollback();
			} catch (SQLException e1) {
			}
			throw new RuntimeException("Problematic SQL: " + query, e);
		}
		finally {
			DB.close(rs);
			DB.close(stmt);
		}
	}

}