package db.magic.ui.views.exec;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import db.magic.ui.pref.MgcSQLPref;
import db.magic.ui.ui.UI;

public class SQLLimitControl extends ControlContribution {

	public Text sqlLimit;

	protected SQLLimitControl() {
		super("SQLLimit");
	}

	@Override
	protected Control createControl(final Composite parent) {
		final Composite group = UI.compositeGrid(parent).gridLayout(2, false).apply();
		UI.label(group).text("limit").apply();
		sqlLimit = UI.text(group, SWT.NONE).text(MgcSQLPref.getDefault().ExecSqlRowLimit()).width(60).apply();
		return group;
	}

}