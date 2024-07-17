package db.magic.dictios;

import java.util.ArrayList;
import java.util.List;

import db.magic.db.ResuTableColumn;
import db.magic.util.Str;

public abstract class PatternItems<T> extends NamedItems<T> {

	// all patterns: (eg. *_id) are redundant to items
	private final List<PatternItem<T>> patternList = new ArrayList<>();

	@Override
	public void putNamedItem(final NamedItem<T> itemo) {
		super.putNamedItem(itemo);
		final String name = itemo.getName();
		if (name.contains("*") || name.contains("?")) {
			patternList.add(new PatternItem<T>(itemo));
		}
	}

	@Override
	public NamedItem<T> removeNamedItem(final String name) {
		return removePattern(super.removeNamedItem(name));
	}

	private NamedItem<T> removePattern(final NamedItem<T> old) {
		if (old != null ) {
			for( int i = patternList.size(); --i >= 0; ) {
				final PatternItem<T> p = patternList.get(i);
				if( p.itemo == old) {
					patternList.remove(i);
					return old;
				}
			}
		}
		return null;
	}

	public NamedItem<T> deduce(final String name) {
		final NamedItem<T> itemo = items.get(name);
		if (itemo != null) {
			return itemo;
		}
		for( final PatternItem<T> fpattern : patternList) {
			if (fpattern.pattern.matcher(name).matches() ) {
				return fpattern.itemo;
			}
		}
		return null;
	}

	public NamedItem<T> deduce(final ResuTableColumn column) {
		int i = column.name.indexOf(".");
		if (i >= 0 ) {
			String s  = column.name;
			{
				final NamedItem<T> itemo = deduce(s);
				if (itemo != null) {
					return itemo;
				}
			}
			while (i >= 0) {
				s = s.substring(i + 1);
				final NamedItem<T> itemo = deduce(s);
				if (itemo != null) {
					return itemo;
				}
			}
			return null;
		}
		{
			final NamedItem<T> itemo = deduce((Str.is(column.schemaName) ? column.schemaName : "public") + "." + column.tableName + "." + column.name);
			if (itemo != null) {
				return itemo;
			}
		}
		{
			final NamedItem<T> itemo = deduce(column.tableName + "." + column.name);
			if (itemo != null) {
				return itemo;
			}
		}
		{
			final NamedItem<T> itemo = deduce(column.name);
			if (itemo != null) {
				return itemo;
			}
		}
		return null;
	}

}