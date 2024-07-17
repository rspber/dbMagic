package db.magic.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.magic.dictios.MgcUtil;
import db.magic.dictios.FDictio;
import db.magic.dictios.Foreign;
import db.magic.dictios.NamedItem;

public class Jdbc {

	public static ResuTableElement[] queryForElements(final DataBase db, final String sql) throws Exception {
		final Connection con = TransactionManager.getInstance(db).getConnection();
		return queryForElements(con, sql);
	}

	private static ResuTableColumn getColumn(final ResultSetMetaData meta, final int i) throws SQLException {
		final ResuTableColumn column = new ResuTableColumn(
		meta.getSchemaName(i),
		meta.getTableName(i),
		meta.getColumnName(i),
		(short)meta.getColumnType(i),
		meta.getColumnTypeName(i),
		meta.getColumnDisplaySize(i),
		meta.getScale(i),
		ResultSetMetaData.columnNoNulls == meta.isNullable(i),
		""							// TODO no default value information
		);
		return column;
	}

	private static ResuTableColumn[] getTableColumns(final ResultSetMetaData meta) throws SQLException {
		final int count = meta.getColumnCount();
		final ResuTableColumn[] columns = new ResuTableColumn[count];
		for (int i = 0; i < count; ++i) {
			columns[i] = getColumn(meta, i + 1);
		}
		return columns;
	}

	public static ResuTableElement[] queryForElements(final Connection con, final String sql) throws Exception {
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			final ResultSetMetaData meta = rs.getMetaData();
			final ResuTableColumn[] columns = getTableColumns(meta);
			final NamedItem<FDictio>[] dictios = MgcUtil.getDictios(con, columns);
			final NamedItem<Foreign>[] foreigns = MgcUtil.getForeigns(columns); 

			final List<ResuTableElement> list = new ArrayList<>();
			list.add(new ResuTableElement(0, columns, dictios, foreigns, null));
			int recordNo = 0;

			while (rs.next()) {
				++recordNo;
				final int size = meta.getColumnCount();
				final Object[] items = new Object[size];
				for (int i = 0; i < size; ++i) {
					items[i] = rs.getString(i + 1);
				}
				list.add(new ResuTableElement(recordNo, columns, dictios, foreigns, items));
			}
			return list.toArray(new ResuTableElement[0]);
		}
		catch (Exception e) {
			throw new SQLException(sql, e);
		}
		finally {
			DB.close(rs);
			DB.close(stmt);
		}
	}

}