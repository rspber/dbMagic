package db.magic.db;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import db.magic.uai.Sweeper;
import db.magic.ui.pref.MgcSQLPref;
import db.magic.util.Num;

public class SqlUtil {

	private Sweeper sweeper = Sweeper.getDefault();

	private static SqlUtil fDefault;
	public static SqlUtil getDefault() {
		if( fDefault == null ) {
			fDefault = new SqlUtil();
		}
		return fDefault;
	}

	private final String select_table_values = "{select * from {$table}}{ where {$where}}{ order by {$order_by}}"; 

	public String getTableContentSql(final String table, final String condition, final String orderBy, final int offset, int limit) {

		final Map<String, String> map = new HashMap<>();
		map.put("table", table);
		map.put("where", condition);
		map.put("order_by", orderBy);
		
		final StringBuilder sb = new StringBuilder(select_table_values);
		sweeper.resolve(sb, map);

		if (limit == 0 ) {
			limit = Num.toInt(MgcSQLPref.getDefault().TableRowLimit());
		}
		if (limit > 0) {
			if (offset > 0) {
				sb.append(" offset ");
				sb.append(offset);
			}
			sb.append(" limit ");
			sb.append(limit);
		}
		final String sql = sb.toString();
		return sql;
	}

// --------------------	

	private final String ref_condition = "{$ref_field} = {$key_value}"; 

	private String toSqlValue(final Object value) {
		return value.toString();
	}

	private String refCondition(final String refField, final Object keyValue) {
		final StringBuilder sb = new StringBuilder(ref_condition);
		final Map<String, String> map = new HashMap<>();
		map.put("ref_field", refField);
		map.put("key_value", toSqlValue(keyValue));
		return sweeper.resolve(sb, map) ? sb.toString() : null;
	}

	public String getForeignRefSql(final String refTable, final String refField, final Object keyValue) {
		return getTableContentSql(refTable, refCondition(refField, keyValue), null, 0, 0);
	}

// --------------------	

	private static final Pattern from1 = Pattern.compile("select\\s+.+\\s+from\\s+(\\S+)", Pattern.CASE_INSENSITIVE);
	private static final Pattern from2 = Pattern.compile("select\\s+.+\\s+from\\s+\\\"(\\S+)\\\"", Pattern.CASE_INSENSITIVE);

	public static String getSQLname(final String sql) {
		{
			final Matcher m = from1.matcher(sql);
			if (m.find()) {
				return m.group(1);
			}
		}
		{
			final Matcher m = from2.matcher(sql);
			if (m.find()) {
				return m.group(1);
			}
		}
		return null;
	}

}