package db.magic.dictios;

import db.magic.util.Str;

public final class DictHex extends DictImple {

	public DictHex(final String nrSep) {
		super(T_Hex, 0, nrSep);
	}

	@Override
	public String valueOf(final int index, final String[] args) {
		return String.format("%x", index);
	}

	@Override
	public String toStr(final String index, final int range, final String[] args) {
		if (Str.is(index) ) {
			try {
				long j = Long.parseLong(index);
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("%x", j));
				int i = sb.length();
				int n = range > 0 ? range < 128 ? range : 128 : 4;
				i %= n;
				if (i > 0) {
					while (++i <= n) {
						sb.insert(0, "0");
					}
				}
				return sb.toString();
			}
			catch( Exception e) {
				return "dictionary translation error";
			}
		}
		return index;
	}

}