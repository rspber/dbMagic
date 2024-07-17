package db.magic.dictios;

import java.util.regex.Pattern;

public class PatternItem<T> {

	public final Pattern pattern;
	public final NamedItem<T> itemo;

	public PatternItem(final NamedItem<T> itemo) {
		this.itemo = itemo;
		pattern = Pattern.compile(itemo.getName().replaceAll("\\*", "\\\\w*").replaceAll("\\?", "\\\\w"));
	}

}