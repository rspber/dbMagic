package db.magic.dictios;

import db.magic.util.Num;

public final class DictAlpha extends DictImple {

	public DictAlpha(final String nrSep) {
		super(T_Alpha, 0, nrSep);
	}

	@Override
	public String valueOf(final int index, final String[] args) {
		return String.valueOf('a' + index + (args != null ? Num.toInt(args[0]) : 0));
	}

}