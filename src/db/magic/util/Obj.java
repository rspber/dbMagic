package db.magic.util;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Obj {

	public static <T> int indexOf(final T[] array, final T value) {
		if( array != null ) {
			for( int i = 0; i < array.length; ++i ) {
				if( Objects.equals(array[i], value) ) {
					return i;
				}
			}
		}
		return -1;
	}

	public static <T> boolean contains(final T[] list, final T value) {
		return indexOf(list, value) >= 0;
	}

	public static boolean nn(final Object obj) {
		if (obj != null) {
			if (obj instanceof String) {
				return Str.is((String)obj);
			}
			return true;
		}
		return false;
	}

	public static List<Object> ofList(final Object list) {
		return (List<Object>)list;
	}

	public static Map<String, Object> ofStringMap(final Object map) {
		return (Map<String, Object>)map;
	}

	public static String ofString(final Object string) {
		if (string == null) {
			return null;
		}
		if (string instanceof String) {
			return (String)string;
		}
		if (string instanceof List) {
			return String.join("\n", (List)string);
		}
		return string.toString();
	}

	public static<T> Map<T, Object> ofMap(final Object map) {
		return (Map)map;
	}

	public static int ofInt(final Object value) {
		if (value == null) {
			return 0;
		}
		if (value instanceof String) {
			return Integer.parseInt((String)value);
		}
		return (int)value;
	}

}