package db.magic.ui.pref;

import java.util.Map;
import java.util.TreeMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import db.magic.dictios.DictAlpha;
import db.magic.dictios.DictArray;
import db.magic.dictios.DictHex;
import db.magic.dictios.DictImple;
import db.magic.dictios.DictIntMap;
import db.magic.dictios.DictNum;
import db.magic.dictios.DictSelect;
import db.magic.dictios.DictStrMap;
import db.magic.dictios.NamedItem;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;
import db.magic.ui.ui.UI;
import db.magic.util.Str;

class DictImpleInputDialog extends ValuesDialog {

	private static final String M_STORE_ID = "DictImpleInputDialog";
	private static final String M_Tit = "Tit";
	private static final String M_Nam = "Nam";
	private static final String M_Typ = "Typ";
	private static final String M_NrSep = "NrSep";
	private static final String M_Key = "Key";
	private static final String M_Def = "Def";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);
	
	private Text dictNameText;
	private CCombo dictTypeCombo;
	private Text nrSepText;
	private CCombo keyTypeCombo;
	private Text dictDefineText;

	private final NamedItem<DictImple> named;

	public DictImpleInputDialog(final Shell parent, final NamedItem<DictImple> named) {
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
		return new Point(800, 400);
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite group = UI.GridLayout((Composite)super.createDialogArea(parent))
				.gridLayout().verticalSpacing(5).apply();
		
		UI.label(group).text(msg.get(M_Nam)).apply();
		dictNameText = UI.text(group, SWT.BORDER | SWT.SINGLE).text(named.getName()).width(350).apply();
		
		final DictImple imple = named.getItem();
		
		UI.label(group).text(msg.get(M_Typ)).apply();
		dictTypeCombo = UI.combo(group, false).add("").add(DictImple.DictTypes)
				.text(imple != null ? imple.dictType : null).width(250).apply();

		UI.label(group).text(msg.get(M_NrSep)).apply();
		nrSepText = UI.text(group, SWT.BORDER + SWT.SINGLE).text(imple != null ? imple.nrSep : null).width(100).apply();

		UI.label(group).text(msg.get(M_Key)).apply();
		keyTypeCombo = UI.combo(group, false).add(DictImple.KeyTypes)
				.text(imple != null ? DictImple.KeyTypes[imple.keyType] : null).width(150).apply();

		UI.label(group).text(msg.get(M_Def)).apply();
		dictDefineText = UI.text(group, SWT.BORDER | SWT.SINGLE)
				.text(packDictImple(imple)).width(750).apply();

		return group;
	}

	private static final String ARRAY_SEP = "|";
	private static final String KEY_SEP = ":";

	private static String packDictArray(final String[] impl) {
		return String.join(ARRAY_SEP, impl);
	}

	private static String[] unpackDictArray(final String s) {
		return s != null ? s.split("\\s*[" + ARRAY_SEP + "]\\s*") : null;
	}

	private static <T> String packDictMap(final Map<T, Object> impl) {
		final StringBuffer sb = new StringBuffer();
		if (impl != null) { 
			impl.forEach((k, v) -> {
				if( sb.length() > 0 ) {
					sb.append(ARRAY_SEP);
				}
				sb.append(k);
				sb.append(KEY_SEP);
				if (v != null) {
					sb.append(v);
				}
			});
		}
		return sb.toString();
	}

	public static String packDictImple(final DictImple imple) {
		if (imple != null) {
			switch (imple.dictType) {
			case DictImple.T_Array:
				return packDictArray(((DictArray)imple).getImpl());
			case DictImple.T_Map:
				if (imple.keyType == DictImple.K_CHAR) {
					return packDictMap(((DictStrMap)imple).getImpl());
				}
				else {
					return packDictMap(((DictIntMap)imple).getImpl());
				}
			case DictImple.T_Select:
				return ((DictSelect)imple).getQuery();
			default:
			}
		}
		return null;
	}

	private static Map<String, Object> unpackDictStrKeyMap(final String s) {
		if (s != null) {
			final String[] array = s.split("\\s*[" + ARRAY_SEP + "]\\s*");
			final Map<String, Object> impl = new TreeMap<>();
			for (final String wk :  array) {
				int i = wk.indexOf(KEY_SEP);
				if( i > 0 ) {
					final String key = wk.substring(0, i).trim();
					final String value = wk.substring(i+1).trim();
					impl.put(key,  value);
				}
			}
			return impl;
		}
		return null;
	}

	private static Map<Integer, Object> unpackDictIntKeyMap(final String s) {
		if (s != null) {
			final String[] array = s.split("\\s*[" + ARRAY_SEP + "]\\s*");
			final Map<Integer, Object> impl = new TreeMap<>();
			for (final String wk : array) {
				int i = wk.indexOf(KEY_SEP);
				if( i > 0 ) {
					final String key = wk.substring(0, i).trim();
					final String value = wk.substring(i+1).trim();
					try {
						impl.put(Integer.parseInt(key),  value);
					}
					catch( Exception e) {
						// pass
					}
				}
			}
			return impl;
		}
		return null;
	}

	private static DictImple newDictImple(
		final String dictType, final String nrSep, final String keyType,
		final String impleStr
	) {
		switch (dictType) {
		case DictImple.T_Num:
			return new DictNum(nrSep);
		case DictImple.T_Alpha:
			return new DictAlpha(nrSep);
		case DictImple.T_Hex:
			return new DictHex(nrSep);
		case DictImple.T_Array:
			return new DictArray(nrSep, unpackDictArray(impleStr));
		case DictImple.T_Map:
			if (Str.eq(keyType, DictImple.K_Char)) {
				return new DictStrMap(nrSep, unpackDictStrKeyMap(impleStr));
			}
			else {
				return new DictIntMap(nrSep, unpackDictIntKeyMap(impleStr));
			}
		case DictImple.T_Select:
			return new DictSelect(keyType, nrSep, impleStr);
		}
		return null;
	}

	@Override
	protected boolean save() {
		named.setName(dictNameText.getText());
		named.setItem(newDictImple(
				dictTypeCombo.getText(),
				nrSepText.getText(),
				keyTypeCombo.getText(),
				dictDefineText.getText()));
		return true;
	}

}