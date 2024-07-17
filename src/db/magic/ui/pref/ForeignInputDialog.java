package db.magic.ui.pref;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import db.magic.dictios.Foreign;
import db.magic.dictios.NamedItem;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;
import db.magic.ui.ui.UI;

class ForeignInputDialog extends ValuesDialog {

	private static final String M_STORE_ID = "ForeignInputDialog";
	private static final String M_Tit = "Tit";
	private static final String M_Nam = "Nam";
	private static final String M_Typ = "Typ";
	private static final String M_RefFld = "RefFld";
	private static final String M_RefTab = "RefTab";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);
	
	private Text fieldNameText;
	private CCombo refTypeCombo;
	private Text refFieldText;
	private Text refTableText;

	private final NamedItem<Foreign> named;

	public ForeignInputDialog(final Shell parent, final NamedItem<Foreign> named) {
		super(parent);
		this.named = named;
	}

	@Override
	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(msg.get(M_Tit));
	}

	@Override
	protected Point getInitialSize() {
		return new Point(700, 350);
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite group = UI.GridLayout((Composite)super.createDialogArea(parent))
				.gridLayout().verticalSpacing(5).apply();

		UI.label(group).text(msg.get(M_Nam)).apply();
		fieldNameText = UI.text(group, SWT.BORDER + SWT.SINGLE).text(named.getName()).width(350).apply();

		final Foreign foreign = named.getItem();

		UI.label(group).text(msg.get(M_Typ)).apply();
		refTypeCombo = UI.combo(group, false).add(Foreign.RefTypes)
				.text(foreign != null ? Foreign.RefTypes[foreign.refType] : null).width(150).apply();

		UI.label(group).text(msg.get(M_RefFld)).apply();
		refFieldText = UI.text(group, SWT.BORDER + SWT.SINGLE).text(foreign != null ? foreign.refField : null).width(650).apply();

		UI.label(group).text(msg.get(M_RefTab)).apply();
		refTableText = UI.text(group, SWT.BORDER + SWT.SINGLE).text(foreign != null ? foreign.refTable : null).width(650).apply();

		return group;
	}

	@Override
	protected boolean save() {
		named.setName(fieldNameText.getText());
		named.setItem(new Foreign(
			Foreign.toType(refTypeCombo.getText()),
			refFieldText.getText(),
			refTableText.getText()));
		return true;
	}

}