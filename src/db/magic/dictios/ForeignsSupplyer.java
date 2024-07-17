package db.magic.dictios;

import java.util.Map;

import db.magic.util.Obj;
import db.magic.yaml.YamlIn;
import db.magic.yaml.YamlOut;

public class ForeignsSupplyer extends PatternItems<Foreign> {

	private static final String FOREIGNS_YAML = "foreigns.yaml";

	private static final String refType = "refType";
	private static final String refField = "refField";
	private static final String refTable = "refTable";

	private static ForeignsSupplyer fDefault;
	public static ForeignsSupplyer getDefault() {
		if( fDefault == null ) {
			fDefault = new ForeignsSupplyer();
			fDefault.initialization(FOREIGNS_YAML);
		}
		return fDefault;
	}

	@Override
	public void load(final Object yamlObject) {
		YamlIn.loadSections(yamlObject, (fieldName, refMap) -> {
			addItem((String)fieldName, createRef(Obj.ofStringMap(refMap)));
		});
	}

	private Foreign createRef(final Map<String, Object> map) {
		return new Foreign(
			YamlIn.getString(map, refType),
			YamlIn.getString(map, refField),
			YamlIn.getString(map, refTable)
		);
	}

	@Override
	public String toYaml() {
		final StringBuilder sb = new StringBuilder();
		forEachItem( (fieldName, foreign) -> {
	 		YamlOut.yaml(sb, 0, fieldName, null);
			YAMLit(sb, 2, foreign);
			sb.append("\n");
		});
		return sb.toString();
	}

 	public static void YAMLit(final StringBuilder sb, final int pfx, final Foreign foreign) {
		YamlOut.yaml(sb, pfx, refType, String.valueOf(foreign.refType));
		YamlOut.yaml(sb, pfx, refField, foreign.refField);
		YamlOut.yaml(sb, pfx, refTable, foreign.refTable);
	}

}