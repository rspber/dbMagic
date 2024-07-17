package db.magic.dictios;

import java.util.Map;

import db.magic.util.Obj;
import db.magic.yaml.YamlIn;
import db.magic.yaml.YamlOut;

public class FDictiosSupplyer extends PatternItems<FDictio> {

	private static final String FIELDS_YAML = "field_dictios.yaml";

	private static final String dict = "dict";
	private static final String mode = "mode";
	private static final String range = "range";
	private static final String arg = "arg";

	private static FDictiosSupplyer fDefault;
	public static FDictiosSupplyer getDefault() {
		if( fDefault == null ) {
			fDefault = new FDictiosSupplyer();
			fDefault.initialization(FIELDS_YAML);
		}
		return fDefault;
	}

	@Override
	public void load(final Object yamlObject) {
		YamlIn.loadSections(yamlObject, (fieldName, dictMap) -> {
			addItem((String)fieldName, createDictio(Obj.ofStringMap(dictMap)));
		});
	}

	private FDictio createDictio(final Map<String, Object> map) {
		return new FDictio(
			YamlIn.getString(map, mode),
			YamlIn.getString(map, dict),
			YamlIn.getString(map, range),
			YamlIn.getStringArray(map, arg)
		);
	}

	@Override
	public String toYaml() {
		final StringBuilder sb = new StringBuilder();
		forEachItem( (fieldName, dictio) -> {
	 		YamlOut.yaml(sb, 0, fieldName, null);
			YAMLit(sb, 2, dictio);
			sb.append("\n");
		});
		return sb.toString();
	}

 	public static void YAMLit(final StringBuilder sb, final int pfx, final FDictio dictio) {
 		if (dictio.mode > 0) {
 			YamlOut.yaml(sb, pfx, mode, String.valueOf(dictio.mode));
 		}
		YamlOut.yaml(sb, pfx, dict, dictio.dict.getName());
 		if (dictio.range > 0) {
 			YamlOut.yaml(sb, pfx, range, String.valueOf(dictio.range));
 		}
 		if (dictio.args != null) {
			YamlOut.yaml(sb, pfx, arg, dictio.args.length > 1 ? dictio.args : dictio.args[0]);
 		}
	}

}