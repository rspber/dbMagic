package db.magic.dictios;

import db.magic.util.Num;

public final class DictNum extends DictImple {

	public DictNum(final String nrSep) {
		super(T_Num, 0, nrSep);
	}

	@Override
	public String valueOf(final int index, final String[] args) {
		return String.valueOf(index + (args != null ? Num.toInt(args[0]) : 0));
	}

	@Override
	public String toStr(final String index, final int range, final String[] args) {
		return index;
	}

}