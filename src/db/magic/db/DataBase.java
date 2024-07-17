package db.magic.db;

import java.util.List;
import java.util.Properties;

public class DataBase implements Cloneable {

	private String name;
	public final String driverName;
	public final String url;
	public final String user;
	public final String password;
	public final List<String> classPaths;

	public boolean modified_;

	public DataBase(
		final String name,
		final String driverName,
		final String url,
		final String user,
		final String password,
		final List<String> classPaths
	) {
		this.name          = name;          
		this.driverName    = driverName;    
		this.url           = url;           
		this.user          = user;          
		this.password      = password;      
		this.classPaths    = classPaths;     
	}

	public String getName() {
		return name;
	}
	
	public void setName(final String name) {
		modified_ = true;
		this.name = name;
	}

	public String[] getClassPaths() {
		return classPaths.toArray(new String[classPaths.size()]);
	}

	public Properties getProperties() {
		final Properties properties = new Properties();
		properties.setProperty("user", user);
		properties.setProperty("password", password);

		return properties;
	}

	public Object clone() {
		return new DataBase(
			name,
			driverName,
			url,
			user,
			password,
			classPaths
		);
	}

}
