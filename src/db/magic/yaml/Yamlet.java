package db.magic.yaml;

import db.magic.DBMagicPlugin;

public abstract class Yamlet {

	private Yamloader loader;

	public abstract void load(final Object yamlObject);
	public abstract void clear();
	public abstract boolean wasModified();
	public abstract void setModified(final boolean value);
	public abstract String toYaml();

	public void initialization(final String yamlFile) {
		loader = new Yamloader(yamlFile);
		try {
			load(loader.loadPureObject());
		}
		catch (Exception e) {
			DBMagicPlugin.showError(e);
			clear();
		}
		setModified(false);
	}

	public void finalization() {
		if (wasModified()) {
			loader.save(toYaml());
		}
	}

}