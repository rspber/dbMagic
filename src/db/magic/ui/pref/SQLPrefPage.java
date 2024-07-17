package db.magic.ui.pref;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;
import db.magic.ui.ui.UI;

public class SQLPrefPage extends MapedFieldPrefPage {

	private static final String M_STORE_ID = "SQLPrefer";
	private static final String M_Tit = "Tit";
	private static final String M_NConDB= "NConDB";
	private static final String M_ConTim = "ConTim";
	private static final String M_Sec = "Sec";
	private static final String M_0Lim = "0Lim";
	private static final String M_TabLim = "TabLim";
	private static final String M_ExeLim = "ExeLim";
//	private static final String M_NCComi = "NCComi";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);

	public SQLPrefPage() {
		super(MgcSQLPref.getPrefStore());
		setDescription(msg.get(M_Tit));
		MgcSQLPref.getDefault();
	}

	@Override
	public void createFieldEditors() {
		final Composite div = UI.GridLayout(getFieldEditorParent()).gridLayout(1, false).apply();

		{
			final Composite span = UI.compositeGrid(div).gridLayout(2, false).apply();
			UI.label(span).text(msg.get(M_NConDB)).width(150).apply();
			addField(MgcSQLPref.P_NO_CONFIRM_CONNECT_DB, UI.button(span, SWT.CHECK).apply());
		}
		{
			final Composite span = UI.compositeGrid(div).gridLayout(4, false).apply();
			UI.label(span).text(msg.get(M_ConTim)).width(150).apply();
			addField(MgcSQLPref.P_CONNECT_TIMEOUT, UI.text(span, SWT.BORDER).intOrNull().width(100).apply());
			UI.label(span).text(msg.get(M_Sec)).apply();
			UI.label(span).text(msg.get(M_0Lim)).apply();
		}
		{
			final Composite span = UI.compositeGrid(div).gridLayout(3, false).apply();
			UI.label(span).text(msg.get(M_TabLim)).width(150).apply();
			addField(MgcSQLPref.P_TABLE_ROW_LIMIT, UI.text(span, SWT.BORDER).width(100).apply());
			UI.label(span).text(msg.get(M_0Lim)).apply();
		}
		{
			final Composite span = UI.compositeGrid(div).gridLayout(3, false).apply();
			UI.label(span).text(msg.get(M_ExeLim)).width(150).apply();
			addField(MgcSQLPref.P_EXEC_SQL_ROW_LIMIT, UI.text(span, SWT.BORDER).width(100).apply());
			UI.label(span).text(msg.get(M_0Lim)).apply();
		}
/*		{
			final Composite span = WK.compositeGrid(div).layout(2, false).apply();
			WK.label(span).text(msg.get(M_NCComi)).width(150).apply();
			addField(P_NO_CONFIRM_COMMIT, WK.button(span, SWT.CHECK).apply());
		}*/
	}

}