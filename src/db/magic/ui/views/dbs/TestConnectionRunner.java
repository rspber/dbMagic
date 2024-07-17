package db.magic.ui.views.dbs;

import java.sql.Connection;

import db.magic.db.DB;
import db.magic.db.DataBase;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;

public class TestConnectionRunner implements Runnable {

	private static final String M_STORE_ID = "TestConnection";
	private static final String M_TimOut = "TimOut";
	private static final String M_Succ = "Succ";
	private static final String M_ConErr = "ConErr";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);

	private final DataBase db;

	private boolean isSuccess;

	private String message;
	private Throwable throwable;

	public TestConnectionRunner(final DataBase db) {
		this.isSuccess = false;
		this.db = db;
		this.message = msg.get(M_TimOut);
	}

	@Override
	public void run() {
		Connection con = null;
		try {
			con = DB.getConnection(db);
			isSuccess = true;
			message = msg.get(M_Succ);
		}
		catch (Exception e) {
//			MgcPlugin.log(e);
			message = msg.get(M_ConErr) + "\n" + e.getMessage();
			this.throwable = e;
			isSuccess = false;
		}
		finally {
			DB.close(con);
		}
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public String getMessage() {
		return message;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(final Throwable throwable) {
		this.throwable = throwable;
	}

}