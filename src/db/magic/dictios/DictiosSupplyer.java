package db.magic.dictios;

import java.util.Map;

import db.magic.util.Obj;
import db.magic.util.Str;
import db.magic.yaml.YamlIn;
import db.magic.yaml.YamlOut;

public class DictiosSupplyer extends NamedItems<DictImple> {

	private static final String DICTIOS_YAML = "dictionaries.yaml";

	private static final String type = "type";
	private static final String key = "key";
	private static final String sep = "sep";
	private static final String def = "def";

	private static DictiosSupplyer fDefault;
	public static DictiosSupplyer getDefault() {
		if( fDefault == null ) {
			fDefault = new DictiosSupplyer();
			fDefault.init_();
		}
		return fDefault;
	}

	private void init_() {
		initialization(DICTIOS_YAML);
		initDefaults();
		setModified(false);
	}
	
	@Override
	public void load(final Object yamlObject) {
		YamlIn.loadSections(yamlObject, (dictId, dictMap) -> {
			addItem((String)dictId, prepDictImple(Obj.ofStringMap(dictMap)));
		});
	}

	private DictImple prepDictImple(final Map<String, Object> map) {
		final String dictType = YamlIn.getString(map, type);
		final String nrSep = YamlIn.getString(map, sep);
		switch (dictType) {
		case DictImple.T_Array:
			return new DictArray(nrSep, YamlIn.getArray(map, def));
		case DictImple.T_Map:
			if (Str.eq(YamlIn.getString(map, key), DictImple.K_Char)) {
				return new DictStrMap(nrSep, YamlIn.getStrKeyMap(map, def));
			}
			else {
				return new DictIntMap(nrSep, YamlIn.getIntKeyMap(map, def));
			}
		case DictImple.T_Select:
			return new DictSelect(YamlIn.getString(map, key), nrSep, YamlIn.getString(map, def));
		case DictImple.T_Num:
			return new DictNum(nrSep);
		case DictImple.T_Hex:
			return new DictHex(nrSep);
		case DictImple.T_Alpha:
			return new DictAlpha(nrSep);
		default:
			return null;
		}
	}

	private void initDefaults() {
		addIfNotExists("num", new DictNum(null));
		addIfNotExists("hex", new DictHex(null));
		addIfNotExists("alpha", new DictAlpha(null));
		addIfNotExists("yesNo", new DictArray("-", new String[] {"No", "Yes"}));
		addIfNotExists("mydict", new DictSelect("-", "int", "select 1 as key, 'descr' as descr from table where type={$arg1}"));
	}

	public void addIfNotExists(final String name, final DictImple imple) {
		final NamedItem<DictImple> tmp = getNamedItem(name);
		if (tmp == null) {
			addItem(name, imple);
		}
	}

	@Override
	public String toYaml() {
		final StringBuilder sb = new StringBuilder();
		forEachItem( (dictName, dictio) -> {
	 		YamlOut.yaml(sb, 0, dictName, null);
			YAMLit(sb, 2, dictio);
			sb.append("\n");
		});
		return sb.toString();
	}

 	public static void YAMLit(final StringBuilder sb, final int pfx, final DictImple imple) {
 		YamlOut.yaml(sb, pfx, type, imple.dictType);
 		if (imple.keyType > 0) {
 			YamlOut.yaml(sb, pfx, key, imple.keyStr());
 		}
 		if (imple.nrSep != null) {
 			YamlOut.yaml(sb, pfx, sep, imple.nrSep);
 		}
		switch (imple.dictType) {
		case DictImple.T_Array:
			YamlOut.yaml(sb, pfx, def, ((DictArray)imple).getImpl());
			break;
		case DictImple.T_Map:
			if (imple.keyType == DictImple.K_CHAR) {
				YamlOut.yaml(sb, pfx, def, ((DictStrMap)imple).getImpl());
			}
			else {
				YamlOut.yaml(sb, pfx, def, ((DictIntMap)imple).getImpl());
			}
			break;
		case DictImple.T_Select:
			YamlOut.yaml(sb, pfx, def, ((DictSelect)imple).getQuery());
			break;
		default:
			;
		}
	}

}