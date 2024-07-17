package db.magic.store;

import java.util.Map;
import java.util.TreeMap;

import db.magic.yaml.YamlIn;
import db.magic.yaml.YamlOut;
import db.magic.yaml.Yamlet;

public class MgcPrefStoreManager extends Yamlet {

	private static final String PREFERENCES = "preferences.yaml";

	private final Map<String, PrefStore> prefStores = new TreeMap<>();	// <PrefId, PrefStore>

	private boolean modified_;

	private static MgcPrefStoreManager fDefault;
	public static MgcPrefStoreManager getDefault() {
		if( fDefault == null ) {
			fDefault = new MgcPrefStoreManager();
			fDefault.initialization(PREFERENCES);
		}
		return fDefault;
	}

	@Override
	public void clear() {
		prefStores.clear();
	}

	@Override
	public void setModified(final boolean value) {
		modified_ = value;
	}

	@Override
	public boolean wasModified() {
		return modified_;
	}

	public PrefStore getPrefStore(final String prefId) {
		PrefStore pref = prefStores.get(prefId);
		if (pref == null) {
			pref = new PrefStore();
			prefStores.put(prefId,  pref);
		}
		return pref;
	}

	@Override
	public void load(final Object yamlObject) {
		YamlIn.loadSections(yamlObject, (prefId, partsMap) -> {
			final PrefStore prefStore = getPrefStore((String)prefId);
			partsMap.forEach( (partId, value) -> {
				prefStore.setValue( partId, value );
			});
		});
	}

	@Override
	public String toYaml() {
		final StringBuilder sb = new StringBuilder();
		prefStores.forEach( (prefId, prefStore) -> {
			YamlOut.yaml(sb, 0, prefId, null);
			prefStore.forEach( (name, value) -> {
				YamlOut.yaml(sb, 2, name, value);
			});
		});
		return sb.toString();
	}

}