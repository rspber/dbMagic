package db.magic.db;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import db.magic.DBMagicPlugin;

public class DriverClassLoader extends URLClassLoader {

	private DriverClassLoader(final URL[] urls, final ClassLoader parent) {
		super(urls, parent);
	}

	public static DriverClassLoader getClassLoader(final List<String> classpaths, final ClassLoader parent) {
		return new DriverClassLoader(urlize(classpaths), parent);
	}

	private static URL[] urlize(final List<String> classpaths) {
		final List<URL> tmp = new ArrayList<>();
		if (classpaths != null) {
			classpaths.forEach( (c) -> {
				try {
					tmp.add(new File(c).toURL());
				}
				catch (Exception e) {
					DBMagicPlugin.showError(e);
				}
			});
		}
		return tmp.toArray(new URL[tmp.size()]);
	}

}