package db.magic.ui.views.dbs.dialogs;

import java.io.IOException;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipInputStream;

public class JDBCDriverSupplyerRunner implements Runnable {

	private final ClassLoader loader;
	private final List<String> classpaths;
	private List<String> driverNames;

	public JDBCDriverSupplyerRunner(final ClassLoader loader, final List<String> classpaths) {
		this.loader = loader;
		this.classpaths = classpaths;
	}

	@Override
	public void run() {
		driverNames = searchDriver();
	}

	public List<String> searchDriver() {
		final List<String> driverList = new ArrayList<>();
		final ZipInputStream in = null;
		try {
			if (classpaths != null) {
				classpaths.forEach( classpath -> {
					try {
						if (classpath.endsWith(".class")) {
							addDriverList(driverList, classpath);
						}
						else {
							final JarFile jarFile = new JarFile(classpath);
							final Enumeration<JarEntry> e = jarFile.entries();
							while (e.hasMoreElements()) {
								final JarEntry entry = e.nextElement();
								this.addDriverList(driverList, entry.getName());
							}
						}
					}
					catch (IOException e) {
					}
				});
			}
		}
		finally {
			try {
				if (in != null) {
					in.close();
				}
			}
			catch (Exception e) {
			}
		}
		return driverList;
	}

	private void addDriverList(final List<String> driverList, final String name) {
		if (name.endsWith(".class")) {
			final String cname = name.replaceFirst(".class", "").replaceAll("/", ".");
			try {
				Class clazz = loader.loadClass(cname);
				if (Driver.class.isAssignableFrom(clazz) && !Driver.class.equals(clazz)) {
					driverList.add(cname);
				}
			else {
					return;
				}
			}
			catch (NoClassDefFoundError | ClassNotFoundException | SecurityException e) {
				// MgcPlugin.log(ex);
			}
		}
	}

	public List<String> getDriverNames() {
		return driverNames;
	}

}