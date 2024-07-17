package db.magic.ui.pref.descr;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import db.magic.DBMagicPlugin;
import db.magic.descr.DescrItemData;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;
import db.magic.ui.pref.ValuesDialog;
import db.magic.ui.ui.UI;
import db.magic.util.Str;

public class DescrAddDialog extends ValuesDialog {

	private static final String M_STORE_ID = "DescrAddDialog";
	private static final String M_Tit = "Tit";
	private static final String M_Cat = "Cat";
	private static final String M_Tab = "Tab";
	private static final String M_Col = "Col";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);

	private Text categoryText;
	private Text tableIdText;
	private Text columnIdText;

	private final DescrItemData data;

	public DescrAddDialog(final DescrItemData data) {
		super(DBMagicPlugin.getShell());
		this.data = data;
	}

	@Override
	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(msg.get(M_Tit));
	}

	@Override
	protected Point getInitialSize() {
		return new Point(400, 250);
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite group = (Composite)super.createDialogArea(parent);

		final Composite div = UI.compositeGrid(group).gridLayout(1, false).eol().apply();

		{
			final Composite span = UI.compositeGrid(div).gridLayout(2, false).eol().apply();
			UI.label(span).text(msg.get(M_Cat)).width(100).apply(); 
			categoryText = UI.text(span, SWT.BORDER).text(data != null ? data.category : null).eol().apply();
		}

		{
			final Composite span = UI.compositeGrid(div).gridLayout(2, false).eol().apply();
			UI.label(span).text(msg.get(M_Tab)).width(100).apply();  
			tableIdText = UI.text(span, SWT.BORDER).text(data != null ? data.tableId : null).eol().apply();
		}

		{
			final Composite span = UI.compositeGrid(div).gridLayout(2, false).eol().apply();
			UI.label(span).text(msg.get(M_Col)).width(100).apply();  
			columnIdText = UI.text(span, SWT.BORDER).text(data != null ? data.columnId : null).eol().apply();
		}

		return group;
	}

	@Override
	protected boolean save() {
		data.category = categoryText.getText();
		data.tableId = tableIdText.getText();
		data.columnId = columnIdText.getText();
		return !Str.isBlank(data.category);
	}

}