package db.magic.util;

public class Chr {

	public static boolean isAlpha(final char c) {
		return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '_';
	}

	public static boolean isDigit(final char c) {
		return c >= '0' && c <= '9';
	}

	public static boolean isAlphaNum(final char c) {
		return isAlpha(c) || isDigit(c);
	}

}