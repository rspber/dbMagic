package db.magic.dictios;

import db.magic.util.Num;
import db.magic.util.Str;

public class FDictio {

	public static final short MODE_VALUE = 0;	// ElemList specific
	public static final short MODE_VECTOR = 1;	// MultiCheckBox will be used

	public final short mode;
	public final NamedItem<DictImple> dict;
	public final short range;
	public final String[] args;

	public FDictio(
			final String mode,
			final String dict,
			final String range,
			final String[] args
	) {
		this(
			!Str.isBlank(mode) ? FDictio.MODE_VECTOR : FDictio.MODE_VALUE,
			DictiosSupplyer.getDefault().getNamedItem(dict),
			Num.toShort(range),
			args
		);
	}

	public FDictio(
			final short mode,
			final NamedItem<DictImple> dict,
			final short range,
			final String[] args
	) {
		this.mode = mode >= 0 ? mode : 0;
		this.dict = dict;
		this.range = range >= 0 ? range : 0;
		this.args = args;
	}

	public String modeStr() {
		switch (mode) {
		case MODE_VECTOR: return "xxx";
		case MODE_VALUE:
		default:
			return "";
		}
	}

	public int getValueIndex(final Object value) {
		return dict != null ? dict.getItem().index(value) : -1;
	}

	public boolean isMgcDictio() {
		final DictImple imple = dictImple();
		return !(
			imple instanceof DictHex ||
			imple instanceof DictNum ||
			imple instanceof DictAlpha
		);
	}

	public boolean isRightAlign() {
		final DictImple imple = dictImple();
		return
			imple instanceof DictHex ||
			imple instanceof DictNum;
	}

	public DictImple dictImple() {
		return dict != null ? dict.getItem() : null;
	}

	public String translateVector(final long value ) {
		final DictImple imple = dictImple(); 
		return imple != null ? imple.xedStr(range, value) : null;
	}

	public String translate(final String value ) {
		final DictImple imple = dictImple(); 
		switch (mode) {
		case MODE_VECTOR:
			return imple != null ? imple.xedStr(range, value) : null;
		case MODE_VALUE:
		default:
			return imple != null ? imple.toStr(value, range, args) : null;
		}
	}

	public Object translate(final Object object ) {
		if( object instanceof String ) {
			return translate((String)object);
		}
		return object;
	}

	public String[] implDescrArray() {
		final DictImple imple = dictImple(); 
		return imple != null ? imple.descrArray(range, args) : null;
	}

}