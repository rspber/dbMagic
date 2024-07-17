package db.magic.dictios;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.swt.widgets.Display;

import db.magic.DBMagicPlugin;
import db.magic.db.ResuTableColumn;
import db.magic.db.SelectSQL;
import db.magic.uai.Sweeper;
import db.magic.util.Str;

public class MgcUtil {

	public static DictImple newDictIntMap(final Connection con, final String nrSep, final String query) {
		final Map<Integer, Object> impl = new TreeMap<>();
		new SelectSQL(query) {
			@Override
			public void next(ResultSet rs) throws SQLException {
				impl.put(rs.getInt(1), rs.getString(2));
			}
		}.executeQuery(con);
		return new DictIntMap(nrSep, impl);
	}

	public static DictImple newDictStrMap(final Connection con, final String nrSep, final String query) {
		final Map<String, Object> impl = new TreeMap<>();
		new SelectSQL(query) {
			@Override
			public void next(ResultSet rs) throws SQLException {
				String key = rs.getString(1);
				if (Str.is(key)) {
					impl.put(key, rs.getString(2));
				}
			}
		}.executeQuery(con);
		return new DictStrMap(nrSep, impl);
	}

	private static Map<String, String> argsToMap(final String[] args) {
		if (args != null) {
			final Map<String, String> lib = new TreeMap<>();
			for (int i = 0; i < args.length; ++i) {
				lib.put("arg" + (i + 1), args[i]);
			}
			return lib;
		}
		else {
			return null;
		}
	}

	public static FDictio convertSelectToMap(final Connection con, final FDictio dictio) {
		try {
			final DictSelect disel = (DictSelect)dictio.dictImple();
			final String sqlFinal = Sweeper.getDefault().resolve(disel.getQuery(), argsToMap(dictio.args));
			final DictImple imple;
			if (disel.keyType == DictImple.K_CHAR) {
				imple = newDictStrMap(con, disel.nrSep, sqlFinal);
			}
			else {
				imple = newDictIntMap(con, disel.nrSep, sqlFinal);
			}
			return new FDictio(dictio.mode, new NamedItem<>(null, imple), dictio.range, null);
		}
		catch (Exception e) {
			Display.getDefault().asyncExec( () -> DBMagicPlugin.showError("Dictionary select problem", e));
		}
		return null;
	}

	public static NamedItem<FDictio>[] getDictios(final Connection con, final ResuTableColumn[] columns) {
		if (columns == null) {
			return null;
		}
		final FDictiosSupplyer dictios = FDictiosSupplyer.getDefault(); 
		final NamedItem<FDictio>[] array = new NamedItem[columns.length];
		for (int i = 0; i < columns.length; ++i) {
			NamedItem<FDictio> dic = dictios.deduce(columns[i]);  
			if (dic != null ) {
				final FDictio dictio = dic.getItem(); 
				if (dictio.dictImple() instanceof DictSelect) {
					dic = new NamedItem<>(null, convertSelectToMap(con, dictio));
				}
			}
			array[i] = dic; 
		}
		return array;
	}

	public static NamedItem<Foreign>[] getForeigns(final ResuTableColumn[] columns) {
		if (columns == null) {
			return null;
		}
		final ForeignsSupplyer foreigns = ForeignsSupplyer.getDefault(); 
		final NamedItem<Foreign>[] array = new NamedItem[columns.length];
		for (int i = 0; i < columns.length; ++i) {
			array[i] = foreigns.deduce(columns[i]);  
		}
		return array;
	}

}