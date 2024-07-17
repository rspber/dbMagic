package db.magic.yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.eclipse.ui.internal.util.BundleUtility;

import db.magic.DBMagicPlugin;

public class Yamloader {

	private final String fileName;
	private final String ospath;

	public Yamloader(final String fileName) {
		this.fileName = fileName;
		ospath = DBMagicPlugin.getDefault().getStateLocation().append(fileName).toOSString();
	}

	public Object loadPureObject() {
		final List<String> list = readFile();
		if (list != null) {
			final ParseYAML parse = new ParseYAML(fileName, list);
			final Object object = parse.read_yaml(-1);
			parse.finito();
			return object;
		}
		return null;
	}

	private List<String> readFile() {
		final File file = new File(ospath);
		try {
			if (file.exists()) {
				return readFile( new InputStreamReader( new FileInputStream(file), StandardCharsets.UTF_8));
			}
			else {
				final URL dsURL = BundleUtility.find(DBMagicPlugin.getDefault().getBundle(), fileName);
				if (dsURL == null) {
					return null;
				}
				InputStream is = null;
				try {
					is = dsURL.openStream();
					return readFile(new InputStreamReader(is, StandardCharsets.UTF_8));
				}
				finally {
					try {
						if (is != null) {
							is.close();
						}
					}
					catch (IOException e) {
						// do nothing
					}
				}
			}
		}
		catch( Exception e ) {
			DBMagicPlugin.showError("Read File Error", e);
		}
		return null;
	}

	private List<String> readFile(final Reader reader) {
		final Scanner scan = new Scanner(reader);
		scan.useDelimiter("\\r*\\n");
		final List<String> list = new ArrayList<String>();
		while (scan.hasNext()){
		    list.add(scan.next());
		}
		scan.close();
		return list;
	}

	public void save(final String output) {
		try {
			final OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(ospath), StandardCharsets.UTF_8);
			writer.write(output);
			writer.close();
		}
		catch (IOException e) {
			DBMagicPlugin.showError(e);
		}
	}

}