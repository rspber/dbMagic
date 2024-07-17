package db.magic.ui.views.dbs;

import org.eclipse.swt.widgets.Display;

import db.magic.MgcJob;
import db.magic.MgcJobRunnable;
import db.magic.db.DataBase;
import db.magic.db.Jdbc;
import db.magic.db.ResuTableElement;
import db.magic.db.SqlUtil;
import db.magic.ui.views.resu.OpenResuViewRunner;

public class DBOpenTableResultViewRunner implements MgcJobRunnable {

	private final TableNode tableNode;

	public DBOpenTableResultViewRunner(final TableNode tableNode) {
		this.tableNode = tableNode;
	}

	@Override
	public void run(final MgcJob job) throws Exception {
		final DataBase db = DBsUtil.getDataBase(tableNode);
		Display.getDefault().asyncExec( new UpdateComboSQLRunner(db));
		final String finishSql = SqlUtil.getDefault().getTableContentSql(tableNode.getSqlTableName(), null, null, 0, 0);
		final ResuTableElement[] elements = Jdbc.queryForElements(db, finishSql);
		Display.getDefault().asyncExec( new OpenResuViewRunner(db, tableNode.getName(), finishSql, elements, null) );
	}

}