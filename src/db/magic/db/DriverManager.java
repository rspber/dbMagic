package db.magic.db;

import java.sql.Driver;
import java.util.HashMap;
import java.util.List;

public class DriverManager {

	private static DriverManager _instance;

	private HashMap<String, Driver> driverMap = new HashMap<>();

	private DriverManager() {
	}

	private String getKey(final DataBase db) {
		String key = db.driverName;
		return key;
	}

	public void removeCach(final DataBase db) {
		driverMap.remove(getKey(db));
	}

	public static synchronized DriverManager getInstance() {
		if (_instance == null) {
			_instance = new DriverManager();
		}
		else {
		}
		return _instance;

	}

	public Driver getDriver(final DataBase db) throws Exception {
		final String key = getKey(db);

		if (driverMap.containsKey(key)) {
			return driverMap.get(key);
		}
		else {
			final Driver driver = getDriver(db.driverName, db.classPaths);
			driverMap.put(key, driver);
			return driver;
		}
	}

	private Driver getDriver(final String driverName, final List<String> classpaths) throws Exception {
		Class<?> driverClass = null;
		final DriverClassLoader loader = DriverClassLoader.getClassLoader(classpaths, getClass().getClassLoader());
		try {
			driverClass = loader.loadClass(driverName);
		}
		catch (ClassNotFoundException e) {
			try {
				driverClass = DriverClassLoader.getSystemClassLoader().loadClass(driverName);
			}
			catch (ClassNotFoundException ex) {
				throw ex;
			}
		}
		return (Driver)driverClass.newInstance();
	}

}
