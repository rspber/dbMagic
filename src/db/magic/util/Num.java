package db.magic.util;

public class Num {

	public static int toInt(final String s) {
		if (s != null && !s.isBlank() ) {
			try {
				return Integer.parseInt(s);
			}
			catch (Exception e) {
			}
		}
		return 0;
	}

	public static short toShort(final String s) {
		if (s != null && !s.isBlank() ) {
			try {
				return Short.parseShort(s);
			}
			catch (Exception e) {
			}
		}
		return 0;
	}

	public static long toLong(final String s) {
		if (s != null && !s.isBlank() ) {
			try {
				return Long.parseLong(s);
			}
			catch (Exception e) {
			}
		}
		return 0;
	}

}