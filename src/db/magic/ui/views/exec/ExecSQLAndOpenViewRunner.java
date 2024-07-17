package db.magic.ui.views.exec;

import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;

import db.magic.MgcJob;
import db.magic.MgcJobRunnable;
import db.magic.MgcPlugC;
import db.magic.DBMagicPlugin;
import db.magic.db.DataBase;
import db.magic.db.Jdbc;
import db.magic.db.ResuTableElement;
import db.magic.db.SqlUtil;
import db.magic.db.Transaction;
import db.magic.db.TransactionManager;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;
import db.magic.uai.Sweeper;
import db.magic.ui.views.dbs.actions.DBConfConnActionRunner;
import db.magic.ui.views.resu.OpenResuViewRunner;
import db.magic.ui.views.sqh.SqHManager;
import db.magic.ui.views.sqh.SqHView;

public class ExecSQLAndOpenViewRunner implements MgcJobRunnable {

	private static final String M_STORE_ID = "ExecSQLAndOpen";
	private static final String M_NoCon = "NoCon";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);

	private final DataBase db;
	private final String sqlQuery;
	private final List<String> uaiValues;
	private final int limit;
	private final String secId;

	private static final Pattern LIMIT = Pattern.compile("\\blimit\\b", Pattern.CASE_INSENSITIVE);

	private static void cutLastSemi(final StringBuilder sb) {
		while (sb.length() > 0 && sb.charAt(sb.length() - 1) == ';') {
			sb.setLength(sb.length() - 1);
		}
	}

	private static String finishSql(final String sql, final List<String> uaiValues, final int limit) {

		final StringBuilder sb = new StringBuilder(sql);

		Sweeper.getDefault().resolve(sb, uaiValues);

		if (limit > 0) {
			if (! LIMIT.matcher(sb.toString()).find()) {
				if (sql.toLowerCase().startsWith("select")) {
					cutLastSemi(sb);
					sb.append(" LIMIT ");
					sb.append(limit);
				}
			}
		}

		return sb.toString();
	}

	public ExecSQLAndOpenViewRunner(final DataBase db, final String sqlQuery, final List<String> uaiValues, final int limit, final String secId) {
		this.db = db;
		this.sqlQuery = sqlQuery;
		this.uaiValues = uaiValues;
		this.limit = limit;
		this.secId = secId;
	}

	@Override
	public void run(final MgcJob job) throws Exception {

		final Transaction trans = TransactionManager.getInstance(db);
		if (!trans.isConnecting()) {
			Display.getDefault().syncExec(new DBConfConnActionRunner(db));
			if (!trans.isConnecting()) {
				Display.getDefault().asyncExec( () -> {
					DBMagicPlugin.showWarning(msg.get(M_NoCon));
				});
				job.Cancel();
			}
		}
		job.Over();

		final String finishSql = finishSql(sqlQuery, uaiValues, limit);

		final String partName = SqlUtil.getSQLname(finishSql);
		final ResuTableElement[] elements = Jdbc.queryForElements(trans.getConnection(), finishSql);
		Display.getDefault().asyncExec( new OpenResuViewRunner(db, partName, finishSql, elements, secId) );

		job.Over();
		if (SqHManager.getDefault().addHistory(sqlQuery) ) {

			Display.getDefault().asyncExec( () -> {
				final IViewPart part = DBMagicPlugin.findView(MgcPlugC.SqHView);
				if (part instanceof SqHView) {
					final SqHView hv = (SqHView)part;
					hv.getViewer().refresh();
				}
			});

		}
	}

}