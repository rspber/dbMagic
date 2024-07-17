package db.magic.dictios;

import java.util.Map;

import db.magic.util.Obj;
import db.magic.yaml.YamlIn;
import db.magic.yaml.YamlOut;

public class VariesSupplyer extends NamedItems<Vario> {

	private static final String VARIES_YAML = "variables.yaml";

	private static final String value = "value";

	private static VariesSupplyer fDefault;
	public static VariesSupplyer getDefault() {
		if( fDefault == null ) {
			fDefault = new VariesSupplyer();
			fDefault.initialization(VARIES_YAML);
		}
		return fDefault;
	}

	@Override
	public void load(final Object yamlObject) {
		YamlIn.loadSections(yamlObject, (varName, varMap) -> {
			addItem((String)varName, createVario(Obj.ofStringMap(varMap)));
		});
	}

	private Vario createVario(final Map<String, Object> map) {
		return new Vario(
			YamlIn.getString(map, value)
		);
	}

	@Override
	public String toYaml() {
		final StringBuilder sb = new StringBuilder();
		forEachItem( (varName, vario) -> {
	 		YamlOut.yaml(sb, 0, varName, null);
			YAMLit(sb, 2, vario);
			sb.append("\n");
		});
		return sb.toString();
	}

 	public static void YAMLit(final StringBuilder sb, final int pfx, final Vario vario) {
		YamlOut.yaml(sb, pfx, value, vario.value);
	}

	public Vario getVario(final String name) {
		final NamedItem<Vario> named = getNamedItem(name);
		return named != null ? named.getItem() : null;
	}

}