package db.magic.db;

import java.util.concurrent.ConcurrentHashMap;

public class TransactionManager {

	private static ConcurrentHashMap<String, Transaction> map = new ConcurrentHashMap<>();

	private static TransactionManager fDefault;
	public static synchronized Transaction getInstance(final DataBase db) {
		if (fDefault == null) {
			fDefault = new TransactionManager();
		}
		return fDefault.create(db);
	}

	private TransactionManager() {
	}

	private Transaction create(final DataBase db) {
		Transaction t = map.get(db.getName());
		if (t != null) {
			return t.checkUserId(db);
		}
		else {
			t =  new Transaction(db);
			map.put(db.getName(), t);
			return t;
		}
	}

	public static synchronized void destroy() {
		map.forEach((k, v) -> v.closeConnection());
	}

}