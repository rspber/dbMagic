package db.magic.ui.pref;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import db.magic.dictios.DictiosSupplyer;
import db.magic.dictios.FDictio;
import db.magic.dictios.NamedItem;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;
import db.magic.ui.ui.UI;
import db.magic.util.Str;

class FDictioInputDialog extends ValuesDialog {

	private static final String M_STORE_ID = "DictioInputDialog";
	private static final String M_Tit = "Tit";
	private static final String M_Nam = "Nam";
	private static final String M_Mod = "Mod";
	private static final String M_Dic = "Dic";
	private static final String M_Ran = "Ran";
	private static final String M_Arg = "Arg";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);
	
	private Text fieldNameText;
	private Text modeText;
	private CCombo dictNameCombo;
	private Text rangeText;
	private Text dictArgText;

	private final NamedItem<FDictio> named;

	public FDictioInputDialog(final Shell parent, final NamedItem<FDictio> named) {
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
		return new Point(500, 300);
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite group = UI.GridLayout((Composite)super.createDialogArea(parent))
				.gridLayout(2, false).verticalSpacing(5).apply();

		UI.label(group).text(msg.get(M_Nam)).width(100).apply();
		fieldNameText = UI.text(group, SWT.BORDER + SWT.SINGLE).text(named.getName()).width(350).apply();

		final FDictio dictio = named.getItem();

		UI.label(group).text(msg.get(M_Mod)).width(100).apply();
		modeText = UI.text(group, SWT.BORDER + SWT.SINGLE).text(dictio != null ? dictio.modeStr() : null).width(100).apply();

		UI.label(group).text(msg.get(M_Dic)).width(100).apply();
		dictNameCombo = UI.combo(group, false).width(350).apply();
		DictiosSupplyer.getDefault().forEachItem( (k,v) -> {
			dictNameCombo.add(k);
		});
		dictNameCombo.setText(Str.nn(dictio != null ? dictio.dictImple() != null ? dictio.dict.getName() : null : null) );

		UI.label(group).text(msg.get(M_Ran)).width(100).apply();
		rangeText = UI.text(group, SWT.BORDER + SWT.SINGLE)
				.text(dictio != null && dictio.range > 0 ? String.valueOf(dictio.range) : null).width(50).apply();

		UI.label(group).text(msg.get(M_Arg)).width(100);
		dictArgText = UI.text(group, SWT.BORDER + SWT.SINGLE).text(packArgs(dictio != null ? dictio.args : null)).width(250).apply();

		return group;
	}

	private static final String ARG_SEP = ","; 

	public static String packArgs(final String[] args) {
		if (args != null) {
			final StringBuilder sb = new StringBuilder();
			String sep = "";
			for (final String arg : args) {
				sb.append(sep);
				sb.append(arg);
				sep = ARG_SEP;
			}
			return sb.toString();
		}
		return null;
	}

	private static String[] unpackArgs(final String s) {
		return !Str.isBlank(s) ? s.split("\\s*[" + ARG_SEP + "]\\s*") : null;
	}

	@Override
	protected boolean save() {
		named.setName(fieldNameText.getText());
		named.setItem(new FDictio(
			modeText.getText(),
			dictNameCombo.getText(),
			rangeText.getText(),
			unpackArgs(dictArgText.getText())));
		return true;
	}

}