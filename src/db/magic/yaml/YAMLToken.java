package db.magic.yaml;

import java.util.List;

import db.magic.util.Chr;

public abstract class YAMLToken {

	protected List<String> lines;
	protected int No;

	private char[] xline;
	protected int xi;

	public YAMLToken(final String name, final List<String> lines) {
		this.lines = lines;
		xline = lines != null && lines.size() > 0 ? lines.get(0).toCharArray() : null;
		xi = 0;
		No = 0;
	}

	public String get_token_() {
		comment();
		final String s = get_token__();
		if (s == null) {
			err("NO TOKEN");
		}
		return s;
	}

	public char line(final int i) {
		return xline != null && i >= 0 && i < xline.length ? xline[i] : 0;
	}

	public String str(final int from, final int to) {
		if (xline != null && from >= 0 && from <= to && to >= 0 && to <= xline.length) {
			final StringBuilder sb = new StringBuilder();
			int i = from;
			while (i < to) {
				char c = xline[i];
				if (c == '\\') {
					c = xline[++i];
				}
				sb.append(c);
				++i;
			}
			return sb.toString();
		}
		return null;
	}

	public String strEOL(final int from) {
		return str(from, xline != null ? xline.length : 0);
	}

	public String pull(int to) {
		if (xline != null && to >= xi) {
			if (to > xline.length) {
				to = xline.length;
			}
			final String s = new String(xline, xi, to - xi);
			xi = to;
			return s;
		}
		return null;
	}

	public int i_quoted__() {
		int i = this.xi;
		char c = line(i);
		if (c == '\'' || c == '"') {
			char d;
			while ((d = line(++i)) != 0) {
				if (d == '\\') {
					++i;
					continue;
				}
				if (d == c) {
					char e = line(i + 1);
					if (e == c) {
						++i;
					}
					else {
						break;
					}
				}
			}
			if (line(i) != c) {
				err( "(" + c + ") EXPECTED");
			}
			++i;
			return i;
		}
		return -1;
	}

	public String quoted__() {
		return pull(i_quoted__()); 
	}

	public String json_const__() {
		int i = this.xi;
		if (Chr.isAlpha(line(i))) {
			while (Chr.isAlphaNum(line(++i)));
		}
		return pull(i);
	}

	public String get_token__() {
		int i = i_quoted__();
		final String s;
		if (i >= 0) {
			s = str(xi + 1, i - 1);
		}
		else {
			i = xi;
			if (Chr.isAlpha(line(i))) {
				while (Chr.isAlphaNum(line(++i)));
			}
			s = str(xi, i);
		}
		while (line(i) == ' ') ++i;
		if (line(i) == ':') {
			xi = i + 1;
			return s;
		}
		return null;
	}

	public String eol_() {
		comment();
		return eol__();
	}

	public String eol__() {
		int i = i_quoted__();
		String s;
		if (i >= 0) {
			s = str(xi + 1, i - 1);
			xi = i;
		}
		else {
			i = xi;
			char b = ' ';
			char c;
			while ((c = line(i)) != 0) {
				if (c == '#' && b == ' ') {
					break;
				}
				b = c;
				++i;
			}
			while (i > xi) {
				c = line(i - 1);
				if (c == ' ' || c == '\t') {
					--i;
				}
				else {
					break;
				}
			}
			s = pull(i);
		}
		comment();
		return s;
	}

	public boolean next_line_() {
		if (line(xi) == 0) {
			xi = 0;
			if (++No < lines.size()) {
				xline = lines.get(No).toCharArray();
				return true;
			}
			else {
				xline = null;
			}
		}
		return false;
	}

	public char skip_blanks_() {
		char c;
		while ((c = line(xi)) != 0) {
			if (c == '\t' || c == ' ') {
				++xi;
			}
			else {
				return c;
			}
		}
		return 0;
	}

	public char next_char_() {
		while (true) {
			char c = skip_blanks_();
			if (c != 0) {
				return c;
			}
			if (!next_line_()) {
				return 0;
			}
		}
	}

	public void comment() {
		if (skip_blanks_() == '#') {
			xi = xline.length;
		}
	}

	public void finito() {
		if (line(xi) != 0 || No < lines.size()) {
			err ("code after end");
		}
	}

	public void err(final String msg) {
		throw new RuntimeException(msg + "\n" +
				No + ":" + str(0, xi) + " --> " + strEOL(xi) + "\n" +
				No + ":" + (No < lines.size() ? lines.get(No) : null), null);
	}

}