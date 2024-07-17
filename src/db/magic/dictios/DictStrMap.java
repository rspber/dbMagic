package db.magic.dictios;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import db.magic.util.Str;

public final class DictStrMap extends DictImple {

	private final Map<String, Object> impl;

	public DictStrMap(final String nrSep, final Map<String, Object> impl) {
		super(T_Map, 1, nrSep);
		this.impl = impl != null ? impl : new TreeMap<>();
	}

	public Map<String, Object> getImpl() {
		return impl;
	}

	@Override
	public String valueOf(final int index, final String[] args) {
		return null;
	}

	@Override
	public String toStr(final String index, final int range, final String[] args) {
		if (Str.is(index) ) {
			return sepIt(index, impl.get(index));
		}
		return index;
	}

	@Override
	public int index(final Object value) {
		if (value != null) {
			int i = 0;
			for (Map.Entry<String, Object> entry : impl.entrySet()) {
				if (entry.getKey().equals(value)) {
					return i;
				}
				++i;
			}
		}
		return -1;
	}

	@Override
	public String[] descrArray(final int range, final String[] args) {
		final List<String> list = new ArrayList<>();
		impl.forEach( (k,v) -> list.add( k + " : " + v));
		return list.toArray(new String[0]);
	}

	@Override
	public boolean[] toArray(final int range, final String value) {
		return null;
	}

	public String xedStr(final int range, final String value) {
		return value;
	}

}