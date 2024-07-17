package db.magic.ui.views.resu;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;

import db.magic.MgcJob;
import db.magic.db.Jdbc;
import db.magic.db.ResuTableElement;
import db.magic.db.SqlUtil;
import db.magic.dictios.Foreign;

public class ResuTableSubLayer extends ResuTableLayer {

	private final SqlUtil sqU = SqlUtil.getDefault(); 

	private ResuTableElement lastLead;
	private int lastIndex;
	private Foreign lastForeign;

	public ResuTableSubLayer(final ResuView resu) {
		super(resu);
	}

	public void update(final ResuTableElement lead, final int index, final Foreign foreign) {

		final String sql = sqU.getForeignRefSql(foreign.refTable, foreign.refField, lead.getItem(index));

		final String sqlName = SqlUtil.getSQLname(sql);

		if (table == null) {
			loadElements(sqlName, sql);
		}
		else {
			if (lead == lastLead && foreign == lastForeign && index == lastIndex) {
				setSubTableHeight(!table.getVisible());
			}
			else {
				resetTable();
				loadElements(sqlName, sql);
			}
		}

		lastLead = lead;
		lastIndex = index;
		lastForeign = foreign;

	}

	private void loadElements(final String partName, final String sql) {
		final ResuEditorInput reedi = (ResuEditorInput)resu.getEditorInput();
		MgcJob.run("Foreign ref", false, job -> {
			final ResuTableElement[] elements = Jdbc.queryForElements(reedi.getDataBase(), sql);
			Display.getDefault().asyncExec( () -> {
				createTableLayer(partName, elements);
				setSubTableHeight(true);
			});
		});
	}

	private static int tableHeight(final Foreign foreign) {
		return foreign != null &&
			(foreign.refType == Foreign.ONE_TO_MANY || foreign.refType == Foreign.MANY_TO_MANY) ? 200 : 100;
	}

	private void setSubTableHeight(final boolean show) {

		final GridData gridData = (GridData)table.getLayoutData();
		gridData.heightHint = show ? tableHeight(lastForeign) : 0;
		table.setVisible(show);
		resu.resizeLayers();
	}

}