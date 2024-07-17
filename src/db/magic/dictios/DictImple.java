package db.magic.dictios;

import db.magic.util.Num;
import db.magic.util.Str;

public abstract class DictImple {

	public static final String T_Array  = "Array";
	public static final String T_Map    = "Map";
	public static final String T_Select = "Select";
	public static final String T_Num    = "Num";
	public static final String T_Hex    = "Hex";
	public static final String T_Alpha  = "Alpha";

	public static final String K_Int  = "int";
	public static final String K_Char  = "char";

	public static final String[] DictTypes = {T_Array, T_Map, T_Select, T_Num, T_Hex, T_Alpha}; 
	public static final String[] KeyTypes = {K_Int, K_Char}; 

	public static final int K_INT  = 0;
	public static final int K_CHAR = 1;

	public final String dictType;
	public final int keyType;
	public final String nrSep;

	protected final char x = 'x';
	protected final char d = '·';	// middle dot 0xb7  &#183;
	protected final boolean xedreverse = false;

	public DictImple(final String dictType, final int keyType, final String nrSep) {
		this.dictType = dictType;
		this.keyType = keyType;
		this.nrSep = nrSep;
	}

	public String keyStr() {
		return keyType == K_CHAR ? K_Char : K_Int; 
	}

	public abstract String valueOf(final int index, final String args[]);

	protected String sepIt(final String index, final Object out) {
		if (out != null) {
			if (Str.is(nrSep) ) {
				return index + " " + (Str.eq(nrSep, "space") ? "" : nrSep + " ") + out;
			}
			else {
				return out.toString();
			}
		}
		return index;
	}

//	@Virtual
	public String toStr(final String index, final int range, final String[] args) {
		if (Str.is(index) ) {
			try {
				int i = Integer.parseInt(index);
				if (range == 0 || i >= 0 && i < range ) {
					return sepIt(index, valueOf(i, args));
				}
			}
			catch( Exception e) {
			}
		}
		return index;
	}

//	@Virtual
	public int index(final Object value) {
		if (value instanceof String) {
			final String s = (String)value;
			try {
				return Integer.parseInt(s);
			}
			catch( Exception e) {
				return -1;
			}
		}
		if (value instanceof Integer) {
			return (Integer)value;
		}
		return -1;
	}

//	@Virtual
	public String[] descrArray(final int range, final String[] args) {
		final String[] t = new String[range < 128 ? range : 128];
		for( int i = 0; i < t.length; ++i) {
			t[i] = valueOf(i, args);
		}
		return t;
	}

//	@Virtual
	public boolean[] toArray(final int range, final String value) {
		long v = Num.toLong(value);
		final boolean[] t = new boolean[range < 128 ? range : 128];
		for( int i = 0; i < t.length; ++i) {
			t[i] = (v & 1) != 0;
			v >>= 1;
		}
		return t;
	}

	public String xedStr(final int range, long v) {
		final int n = range < 128 ? range : 128;
		final StringBuilder sb = new StringBuilder(n);
		for( int i = 0; i < n; ++i) {
			if (i > 0 && (i & 0x03) == 0) {
				sb.append(" ");
			}
			sb.append( (v & 1) != 0 ? x : d);
			v >>= 1;
		}
		return (xedreverse ? sb.reverse() : sb).toString();
	}

//	@Virtual
	public String xedStr(final int range, final String value) {
		return xedStr(range, Num.toLong(value));
	}

}