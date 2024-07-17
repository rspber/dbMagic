package db.magic.dictios;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import db.magic.util.Str;

public final class DictIntMap extends DictImple {

	private final Map<Integer, Object> impl;

	public DictIntMap(final String nrSep, final Map<Integer, Object> impl) {
		super(T_Map, 0, nrSep);
		this.impl = impl != null ? impl : new TreeMap<>();
	}

	public Map<Integer, Object> getImpl() {
		return impl;
	}

	@Override
	public String valueOf(final int index, final String[] args) {
		final Object value = impl.get(index); 
		return value != null ? value.toString() : null;
	}

	@Override
	public String toStr(final String index, final int range, final String[] args) {
		if (Str.is(index) ) {
			return sepIt(index, impl.get(Integer.parseInt(index)));
		}
		return index;
	}

	@Override
	public int index(final Object value) {
		if (value != null) {
			int i = 0;
			for (Map.Entry<Integer, Object> entry : impl.entrySet()) {
				if (String.valueOf(entry.getKey()).equals(value)) {
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
		impl.forEach( (k,v) -> list.add( k + " : " + (v != null ? v : "")));
		return list.toArray(new String[0]);
	}

}