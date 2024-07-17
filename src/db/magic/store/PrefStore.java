package db.magic.store;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;

import db.magic.util.Obj;

public class PrefStore {

	private final Map<String, Object> values;

	public PrefStore() {
		values = new TreeMap<>();
	}

	public PrefStore(final Map<String, Object> values) {
		this.values = values;
	}

	public void forEach(final BiConsumer<String, Object> get) {
		values.forEach( (name, value) -> {
			get.accept(name, value);
		});
	}

	public Object getValue(final String name) {
		return name != null ? values.get(name) : null;
	}

	public void setValue(final String name, final Object value) {
		if (value != null) {
			if (value instanceof Map
				|| value instanceof String
				|| value instanceof Integer
				|| value instanceof Boolean
			) {
				values.put(name, value);
			}
			else {
				throw new RuntimeException("Invalid preference value " + name + " : " +value.toString()); 
			}
		}
		else {
			values.remove(name);
		}
		MgcPrefStoreManager.getDefault().setModified(true);
	}

	public String getString(final String name) {
		final Object value = getValue(name);
		return value != null ? value.toString() : null;
	}

	public boolean getBoolean(final String name) {
		final String s = getString(name);
		return s != null && s.equals("true");
	}

	public Map<String, Object> getMapValue(final String name) {
		Object value = getValue(name);
		if (value == null) {
			value = new TreeMap<String, Object>();
			values.put(name, value);
		}
		if (value instanceof Map) {
			return Obj.ofStringMap(value);
		}
		throw new RuntimeException("PrefStore map value conflict: " + name + " : " + value);
	}
}