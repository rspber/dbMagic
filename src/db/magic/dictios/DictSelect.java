package db.magic.dictios;

import db.magic.util.Str;

// Proxy
public final class DictSelect extends DictImple {

	private final String query;

	public DictSelect(final int keyType, final String nrSep, final String query) {
		super(T_Select, keyType, nrSep);
		this.query = query;
	}

	public DictSelect(final String keyType, final String nrSep, final String query) {
		this(Str.eq(keyType, K_Char) ? 1 : 0, nrSep, query);
	}

	public String getQuery() {
		return query;
	}

	@Override
	public String valueOf(final int index, final String[] args) {
		return null;
	}

}