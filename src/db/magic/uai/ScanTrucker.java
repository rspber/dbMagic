package db.magic.uai;

public class ScanTrucker {

	final StringBuilder sb;
	int ib;
	int uainr;

	public ScanTrucker(final StringBuilder sb) {
		this.sb = sb;
		ib = 0;
	}

	private void blanks() {
		while (ib < sb.length() ) {
			switch (sb.charAt(ib)) {
			case ' ':
			case '\t':
			case '\r':
			case '\n':
				++ib;
				continue;
			default:
				;
			}
			break;
		}
	}

	public boolean skip_to(final String patt) {
		final int k = sb.indexOf(patt, ib);
		if (k >= 0) {
			ib = k + patt.length();
			return true;
		}
		ib = sb.length();
		return false;
	}

	public boolean skip(final String patt) {
		blanks();
		for (int i = 0; i < patt.length(); ++i) {
			if (sb.charAt(ib + i) != patt.charAt(i)) {
				return false;
			}
		}
		ib += patt.length();
		return true;
	}

	public String skip_word() {
		blanks();
		int i = ib;
		if (i < sb.length() ) {
			final char c = sb.charAt(i);
			if (c == '\"' || c == '\'') {
				while (++i < sb.length() ) {
					if (sb.charAt(i) == c) {
						final String ret = ib + 1 < i ? sb.substring(ib + 1, i) : null;
						ib = i + 1;
						return ret;
					}
				}
				return null;
			}
		}
		while (i < sb.length() ) {
			final char c = sb.charAt(i);
			if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c == '_' || c >= '0' && c <= '9') {
				++i;
				continue;
			}
			break;
		}
		final String ret = i > ib ? sb.substring(ib, i) : null;
		ib = i;
		return ret;
	}

}