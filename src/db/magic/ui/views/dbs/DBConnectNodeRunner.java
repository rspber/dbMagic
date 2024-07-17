package db.magic.ui.views.dbs;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;

import db.magic.MgcJob;
import db.magic.MgcJobRunnable;
import db.magic.DBMagicPlugin;
import db.magic.MgcSync;
import db.magic.db.DB;
import db.magic.db.DataBase;
import db.magic.db.TransactionManager;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;
import db.magic.tree.MgcTreeNodeUtil;
import db.magic.ui.pref.MgcSQLPref;

public class DBConnectNodeRunner implements MgcJobRunnable {

	private static final String M_STORE_ID = "ConnectDB";
	private static final String M_TimOut = "TimOut";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);

	private final TreeViewer viewer;
	private final DBNode dbNode;

	public DBConnectNodeRunner(final TreeViewer viewer, final DBNode dbNode) {
		this.viewer = viewer;
		this.dbNode = dbNode;
	}

	@Override
	public void run(final MgcJob job) throws Exception {
		try {
			synchronized (viewer) {
				final DataBase db = dbNode.getDataBase();
				final TestConnectionRunner test = new TestConnectionRunner(db);
				final Thread th = new Thread(test);
				th.setPriority(Thread.MIN_PRIORITY);
				th.start();
				final int timeout = MgcSQLPref.getDefault().ConnectTimeout();
				MgcSync.sync(th, timeout);
				job.Over();
				job.monitor.worked(1);
				if (!test.isSuccess()) {
					dbNode.setConnected(false);
					dbNode.setExpanded(false);
					if (test.getThrowable() == null) {
						test.setThrowable(new SQLException(msg.get(M_TimOut)));
					}
					Display.getDefault().asyncExec( () -> {
						DBMagicPlugin.showError(test.getMessage(), test.getThrowable());
					});
					job.Cancel();
				}
				job.Over();
				dbNode.removeChildAll();
				Display.getDefault().asyncExec( () -> MgcTreeNodeUtil.refreshTreeNode(viewer, dbNode) );
				dbNode.addSchemas(getSchemaNames(db));
				dbNode.setConnected(true);
				Display.getDefault().asyncExec( () -> MgcTreeNodeUtil.expandTreeNode(viewer, dbNode) );
				job.monitor.worked(1);
			}
		}
		catch (Exception e) {
			dbNode.setConnected(false);
			dbNode.setExpanded(false);
			Display.getDefault().asyncExec( () -> {
				DBMagicPlugin.showError("Error", e);
			});
		}
	}

	private String[] getSchemaNames(final DataBase db) throws Exception {

		final Connection con = TransactionManager.getInstance(db).getConnection();
		final DatabaseMetaData dbMD = con.getMetaData();
		if( !dbMD.supportsSchemasInTableDefinitions()) {
			return new String[0];
		}

		ResultSet rs = null;
		try {
			rs = dbMD.getSchemas();

			List<String> list = new ArrayList<>();
			while (rs.next()) {
				list.add(rs.getString(1));
			}
			return list.toArray(new String[0]);
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			DB.close(rs);
		}
	}

}