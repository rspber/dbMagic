package db.magic.db;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

import db.magic.util.Obj;
import db.magic.util.Str;
import db.magic.yaml.YamlIn;
import db.magic.yaml.YamlOut;
import db.magic.yaml.Yamlet;

public class DBManager extends Yamlet {

	private static final String DATA_BASES_YAML = "data_bases.yaml";

	private static final String driverName = "driverName";
	private static final String url = "url";
	private static final String user = "user";
	private static final String password = "password";
	private static final String classPaths = "classPaths";

	private Map<String, DataBase> dbs = new TreeMap<>();

	private boolean wasModified;

	private static DBManager fDefault;
	public static DBManager getDefault() {
		if (fDefault == null) {
			fDefault = new DBManager();
			fDefault.initialization(DATA_BASES_YAML);
		}
		return fDefault;
	}

	@Override
	public void clear() {
		dbs.clear();
	}

	@Override
	public void setModified(final boolean value) {
		dbs.forEach( (k,v) -> v.modified_ = value );
		wasModified = value;
	}

	@Override
	public boolean wasModified() {
		for (Map.Entry<String, DataBase> entry : dbs.entrySet()) {
			if (entry.getValue().modified_) {
				return true;
			}
		}
		return wasModified;
	}

	@Override
	public void load(final Object yamlObject) {
		YamlIn.loadSections(yamlObject, (dbName, dbMap) -> {
			dbs.put((String)dbName, createDataBase((String)dbName, Obj.ofStringMap(dbMap)));
		});
	}

 	private DataBase createDataBase(final String dbName, final Map<String, Object> map) {
		return new DataBase(
			dbName,
			YamlIn.getString(map, driverName),
			YamlIn.getString(map, url),
			YamlIn.getString(map, user),
			YamlIn.getString(map, password),
			YamlIn.getStringList(map, classPaths)
		);
	}

 	@Override
 	public String toYaml() {
		final StringBuilder sb = new StringBuilder();
		dbs.forEach( (dbName, db) -> {
	 		YamlOut.yaml(sb, 0, dbName, null);
			YAMLit(sb, 2, db);
			sb.append("\n");
		});
		return sb.toString();
	}

 	public void YAMLit(final StringBuilder sb, final int pfx, final DataBase db) {
 		YamlOut.yaml(sb, pfx, driverName, db.driverName);
 		YamlOut.yaml(sb, pfx, url, db.url);
 		YamlOut.yaml(sb, pfx, user, db.user);
 		YamlOut.yaml(sb, pfx, password, db.password);
 		YamlOut.yaml(sb, pfx, classPaths, db.classPaths);
	}

 	public void forEach(final Consumer<DataBase> c) {
		dbs.forEach( (dbName, db) -> {
			c.accept(db);
		});
 	}
/* 
	public DataBase[] getDataBases() {
		final DataBase[] array = new DataBase[dbs.size()];
		int i = 0;
		for (Map.Entry<String, DataBase> entry : dbs.entrySet()) {
			array[i++] = entry.getValue();
		}
		return array;
	}
*/
	public DataBase getDataBase(final int index) {
		if (index >= 0 ) {
			int i = -1;
			for (Map.Entry<String, DataBase> entry : dbs.entrySet()) {
				if (++i == index) {
					return entry.getValue();
				}
			}
		}
		return null;
	}

	public int getDataBaseIndex(final String dbName) {
		int i = 0;
		for (Map.Entry<String, DataBase> entry : dbs.entrySet()) {
			if (Str.eq(dbName, entry.getValue().getName())) {
				return i;
			}
			++i;
		}
		return -1;
	}

	public int getDataBaseIndex(final DataBase db) {
		return db != null ? getDataBaseIndex(db.getName()) : -1;
	}

	public boolean hasDataBase(final String dbName) {
		return dbs.get(dbName) != null;
	}

	public void add(final DataBase db) {
		if (db != null) {
			wasModified = true;
			dbs.put(db.getName(), db);
		}
	}

	public void remove(final DataBase db) {
		if (db != null) {
			wasModified= true;
			dbs.remove(db.getName());
		}
	}

	public DataBase getDataBase(final String dbName) {
		return dbs.get(dbName);
	}

}