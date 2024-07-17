package db.magic.ui.views.dbs;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import db.magic.db.DB;
import db.magic.db.DataBase;
import db.magic.db.TransactionManager;

public class SchemaByTypesTablesSupplyer {

	public static TableInfo[] getTables(final DataBase db, final String schemaPattern, final String type) throws Exception {

		final Connection con = TransactionManager.getInstance(db).getConnection();

		ResultSet rs = null;

		try {
			final DatabaseMetaData dbMD = con.getMetaData();

			rs = dbMD.getTables(null, dbMD.supportsSchemasInTableDefinitions() ? schemaPattern : "%", "%", new String[] { type });

			final List<TableInfo> list = new ArrayList<>();
			while (rs.next()) {
				final String tableName = rs.getString("TABLE_NAME");
				final TableInfo tableInfo = new TableInfo();
				tableInfo.setName(tableName);
				tableInfo.setTableType(rs.getString("TABLE_TYPE"));

				list.add(tableInfo);
			}
			return list.toArray(new TableInfo[0]);
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			DB.close(rs);
		}
	}

}