package db.magic.ui.pref;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import db.magic.dictios.NamedItem;
import db.magic.dictios.Vario;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;
import db.magic.ui.ui.UI;

class VarioInputDialog extends ValuesDialog {

	private static final String M_STORE_ID = "VarioInputDialog";
	private static final String M_Tit = "Tit";
	private static final String M_Nam = "Nam";
//	private static final String M_Typ = "Typ";
	private static final String M_Val = "Val";
//	private static final String M_Arg = "Arg";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);

	private Text varNameText;
//	private CCombo varTypeCombo;
	private Text valueText;
//	private Text argsText;

	private final NamedItem<Vario> named;

	public VarioInputDialog(final Shell parent, final NamedItem<Vario> named) {
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
		return new Point(700, 300);
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite group = UI.GridLayout((Composite)super.createDialogArea(parent))
				.gridLayout().verticalSpacing(5).apply();

		UI.label(group).text(msg.get(M_Nam)).apply();
		varNameText = UI.text(group, SWT.BORDER + SWT.SINGLE).text(named.getName()).width(350).apply();

		final Vario vario = named.getItem();
/*		
		WK.label(group).text(msg.get(M_Typ)).apply();
		varTypeCombo = WK.combo(group, false).width(150).apply();
		for( final String type : Vario.VarTypes) {
			varTypeCombo.add(type);
		}
		varTypeCombo.setText(vario != null ? Vario.VarTypes[vario.varType] : null);
*/
		UI.label(group).text(msg.get(M_Val)).apply();
		valueText = UI.text(group, SWT.BORDER + SWT.SINGLE).text(vario != null ? vario.value : null).width(650).apply();
/*		WK.label(group).text(msg.get(M_Arg)).apply();
		argsText = WK.text(group, SWT.BORDER + SWT.SINGLE).text(vario != null ? vario.args : null).width(650).apply();
*/
		return group;
	}

	@Override
	protected boolean save() {
		named.setName(varNameText.getText());
		named.setItem(new Vario(
//			Vario.toType(varTypeCombo.getText()),
			valueText.getText()));
//			argsText.getText()));
		return true;
	}

}