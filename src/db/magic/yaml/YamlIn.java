package db.magic.yaml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;

import db.magic.DBMagicPlugin;
import db.magic.util.Obj;

public class YamlIn {

	public static <T> void loadSections(final Object obj, final BiConsumer<T, Map<String, Object>> cMap) {
		if (obj instanceof Map) {
			try {
				Obj.ofMap(obj).forEach( (sectId, sectMap) -> {
					if (sectMap != null) {
						cMap.accept((T)sectId, Obj.ofStringMap(sectMap));
					}
				});
			}
			catch( Exception e) {
				DBMagicPlugin.showError(e);
			}
		}
	}

	public static <T> void loadSectionsList(final Object obj, final BiConsumer<T, List<Object>> cList) {
		if (obj instanceof Map) {
			try {
				Obj.ofMap(obj).forEach( (sectId, sectList) -> {
					if (sectList != null) {
						cList.accept((T)sectId, Obj.ofList(sectList));
					}
				});
			}
			catch( Exception e) {
				DBMagicPlugin.showError(e);
			}
		}
	}

	public static String getString(final Map<String, Object> map, final String name) {
		if (map != null) {
			final Object obj = map.get(name);
			if (obj != null) {
				if (obj instanceof String) {
					return (String)obj;
				}
			}
		}
		return null;
	}

	public static List<String> getStringList(final Map<String, Object> map, final String name) {
		return map != null ? toList(map.get(name)) : null;
	}

	public static List<String> toList(final Object obj) {
		final List<String> list = new ArrayList<>();
		if (obj != null) {
			if (obj instanceof List) {
				Obj.ofList(obj).forEach( item -> {
					if (item instanceof String) {
						list.add((String)item);
					}
				});
			}
			else {
				if (obj instanceof String) {
					list.add((String)obj);
				}
 			}
		}
		return list.size() > 0 ? list : null;
	}

	public static String[] getStringArray(final Map<String, Object> map, final String name) {
		if (map != null) {
			final List<String> list = toList(map.get(name));
			return list != null ? list.toArray(new String[list.size()]) : null;
		}
		return null;
	}

	public static String[] getArray(final Map<String, Object> map, final String name) {
		return map != null ? toArray(map.get(name)) : null;
	}

	public static String[] toArray(final Object obj) {
		final List<String> list = toList(obj);
		return list != null ? list.toArray(new String[list.size()]) : null;
	}

	public static Map<String, Object> getStrKeyMap(final Map<String, Object> map, final String name) {
		return map != null ? Obj.ofStringMap(map.get(name)) : null;
	}

	public static Map<Integer, Object> getIntKeyMap(final Map<String, Object> map, final String name) {
		return map != null ? toIntKeyMap(map.get(name)) : null;
	}

	public static Map<Integer, Object> toIntKeyMap(final Object obj) {
		final Map<Integer, Object> map = new TreeMap<>();
		if (obj instanceof Map) {
			Obj.ofStringMap(obj).forEach( (key, value) -> {
				map.put(Integer.parseInt(key), value);
			});
		}
		return map.size() > 0 ? map : null;
	}

}