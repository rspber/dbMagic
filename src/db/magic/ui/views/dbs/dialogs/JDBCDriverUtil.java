package db.magic.ui.views.dbs.dialogs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Shell;

import db.magic.db.DriverClassLoader;

public class JDBCDriverUtil {

	private static void createClassPathList(final List<String> list, final String folderName, final File dir) {
		if (dir.isDirectory()) {
			final File[] files = dir.listFiles();
			for (int i = 0; i < files.length; ++i) {
				final File file = files[i];
				if (file.isDirectory()) {
					createClassPathList(list, folderName, file);
				}
				else if (file.isFile()) {
					String path = file.getPath();
					path = path.replace('\\', '/');
					path = path.replaceAll(folderName.replace('\\', '/') + "/", "");
					list.add(path);
				}
			}
		}
	}

	private static File[] toFiles(final String[] classpaths) {
		final File[] files = new File[classpaths.length];
		for (int i = 0; i < classpaths.length; ++i) {
			files[i] = new File(classpaths[i]);
		}
		return files;
	}

	static String[] searchDriver(final Shell shell, final String[] classpaths, final Class<?> clazz) throws InterruptedException {
		final File[] files = toFiles(classpaths);
		final List<String> driverList = new ArrayList<>();
		for (int j = 0; j < files.length; ++j) {
			final File f = files[j];
			if (f.isDirectory()) {
				createClassPathList(driverList, f.getPath(), f);
			}
			else {
				driverList.add(f.getPath());
			}
		}
		final ClassLoader loader = DriverClassLoader.getClassLoader(driverList, clazz.getClassLoader());

		final JDBCDriverSupplyerRunner thread = new JDBCDriverSupplyerRunner(loader, driverList);
		thread.run();
		final List<String> list = thread.getDriverNames();
		return list.toArray(new String[0]);
	}

}