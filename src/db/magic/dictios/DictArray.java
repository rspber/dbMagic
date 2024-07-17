package db.magic.dictios;

import db.magic.util.Num;

public final class DictArray extends DictImple {

	private final String[] impl;

	public DictArray(final String nrSep, final String[] impl) {
		super(T_Array, 0, nrSep);
		this.impl = impl != null ? impl : new String[0];
	}

	public String[] getImpl() {
		return impl;
	}

	@Override
	public String toStr(final String index, final int range, final String[] args) {
		return super.toStr(index, range > 0 ? range : impl.length, args);
	}

	@Override
	public String valueOf(final int index, final String[] args) {
		final int i = index + (args != null ? Num.toInt(args[0]) : 0);
		if (i >= 0 && i < impl.length ) {
			return impl[i];
		}
		else {
			return null;
		}
	}

	@Override
	public String[] descrArray(int range, final String[] args) {
		if (range == 0 ) {
			range = impl.length;
		}
		if (range == impl.length ) {
			return impl;
		}
		else {
			return super.descrArray(range, args);
		}
	}

	@Override
	public boolean[] toArray(final int range, final String value) {
		return super.toArray(range > 0 ? range : impl.length, value);
	}

	@Override
	public String xedStr(final int range, final String value) {
		return super.xedStr(range > 0 ? range : impl.length, value);
	}

}