package db.magic.dictios;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;

import db.magic.util.Str;
import db.magic.yaml.Yamlet;

public abstract class NamedItems<T> extends Yamlet {

	protected final Map<String/*name*/, NamedItem<T>> items = new TreeMap<>();

	private boolean modified_;

	@Override
	public void clear() {
		items.clear();
	}

	@Override
	public void setModified(final boolean value) {
		modified_ = value;
	}

	@Override
	public boolean wasModified() {
		return modified_;
	}

	public Map<String/*name*/, NamedItem<T>> getNamedItems() {
		return items;
	}

	public void forEachItem(final BiConsumer<String, T> step) {
		items.forEach( (k, v) -> step.accept(k, v.getItem()));
	}

	//@Virtual
	public void putNamedItem(final NamedItem<T> itemo) {
		modified_ = true;
		items.put(itemo.getName(), itemo);
	}

	public void addNamedItem(final NamedItem<T> itemo) {
		if (itemo != null ) {
			final NamedItem<T> old = items.get(itemo.getName());
			if (old != null) {
				modified_ = true;
				old.setItem(itemo.getItem());
			}
			else {
				putNamedItem(itemo);
			}
		}
	}

	public void addItem(final String name, final T item) {
		addNamedItem(new NamedItem<T>(name, item));
	}

	public void setNamedItem(final int index, final NamedItem<T> itemo) {
		final NamedItem<T> old = getNamedItem(index);
		if( itemo != null ) {
			if (old != null) {
				modified_ = true;
				old.setItem(itemo.getItem());
				if (!Str.eq(old.getName(), itemo.getName())) {
					removeNamedItem(old);
					old.setName(itemo.getName());
					putNamedItem(old);
				}
			}
			else {
				putNamedItem(itemo);
			}
		}
		else {
			removeNamedItem(old);
		}
	}

	public NamedItem<T> getNamedItem(final String name) {
		return name != null ? items.get(name) : null;
	}

	public NamedItem<T> getNamedItem(final int index) {
		final Collection<NamedItem<T>> list = items.values();
		int i = 0;
		for (Iterator<NamedItem<T>> it = list.iterator(); it.hasNext();) {
			NamedItem<T> p = it.next();
			if (i == index) {
				return p;
			}
			++i;
		}
		return null;
	}

	public NamedItem<T> removeNamedItem(final NamedItem<T> itemo) {
		return itemo != null ? removeNamedItem(itemo.getName()) : null;
	}

	//@Virtual
	public NamedItem<T> removeNamedItem(final String name) {
		modified_ = true;
		return items.remove(name);
	}

	public boolean contains(final T item) {

		for( Map.Entry<String, NamedItem<T>> entry : items.entrySet()) {
			if (entry.getValue().equals(item)) {
				return true;
			}
		}
		return false;
	}

}