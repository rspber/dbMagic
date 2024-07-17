package db.magic.util;

import java.util.List;

public class Str {

	public static final String EMPTY = "";
	public static final String COMMA_EXPR = "\\s*,\\s*";

	public static boolean eq(final String s1, final String s2)
	{
		return s1 != null ? s2 != null ? s1.equals(s2) : false : s2 == null;
	}

	public static boolean eqFine(final String s1, final String s2)
	{
		return (s1 != null ? s1 : EMPTY).equals(s2 != null ? s2 : EMPTY);
	}

	public static boolean is(final String s) {
		return s != null && !s.isEmpty(); 
	}

	public static String nn(final String s) {
		return s != null ? s : EMPTY; 
	}

	public static boolean isBlank(final String s)
	{
		return s == null || s.isBlank();
	}

	public static String[] comma_split(String s)
	{
		if( s != null ) {
			s = s.trim();
		}
		return s != null && !s.isEmpty() ? s.split(COMMA_EXPR) : null;
	}

	public static boolean eq(final List<String> l1, final List<String> l2) {
		if (l1 == l2) {
			return true;
		}
		if (l1 == null || l2 == null) {
			return false;
		}
		if (l1.size() != l2.size()) {
			return false;
		}
		for( int i = l1.size(); --i >= 0; ) {
			if (!Str.eq(l1.get(i), l2.get(i))) {
				return false;
			}
		}
		return true;
	}

	public static String spaces(int n) {
		final StringBuilder sb = new StringBuilder(n);
		while (--n >= 0) {
			sb.append(' ');
		}
		return sb.toString();
	}

	public static String escAmp(final String s) {
		return s != null ? s.replaceAll("\\&", "&&") : null;
	}
	
}