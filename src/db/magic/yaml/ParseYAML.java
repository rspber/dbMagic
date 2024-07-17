package db.magic.yaml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ParseYAML extends ParseJSON {

	public ParseYAML(final String name, final List<String> lines) {
		super(name, lines);
	}

	public Object read_text(final int pfx) {
		char c = skip_blanks_();
		switch (c) {
		case '{':
		case '[':
			return read_json();
		}
		if (c == 0 && line(xi-1) == ' ') {
			c = ' ';
		}
		if (c == '|' || c == '>' || c == ' ') {
			if (c == '|' || c == '>') {
				String s = strEOL(xi);	// ???
			}
			eol_();
			List<Object>tt = new ArrayList<>();
			while (true) {
				if (next_line_()) {
					while (next_line_()) {
						tt.add(null);
					}
				}
				skip_blanks_();
				if (xi <= pfx) {
					break;
				}
				tt.add(eol_());
			}
//			tt::oops = c;
			return tt;
		}
		else {
			return eol_();
		}
	}

	public Object read_yaml(final int pfx) {
		if (line(xi) == '{') {
			return read_json();
		}
		final Map<String, Object> map = new TreeMap<>();
		final List<Object> list = new ArrayList<>();
		
		while (true) {
			comment();
			if (next_line_()) {
				continue;
			}
			if (No >= lines.size()) {
				break;
			}
			char m = line(xi);
			if (m == '-') {
				int p1 = xi;
				if (p1 < pfx) {
					break;
				}
				++xi;
				int this_i = xi;
				skip_blanks_();
				String s = get_token__();
				xi = this_i;
				Object u;
				if (s != null && !s.isEmpty()) {
					u = read_yaml(xi);
				}
				else {
					u = read_text(p1);
				}
				if (u != null) {
					if (map.size() > 0) {
						throw new RuntimeException("no indentation: - " + u);
					}
					list.add(u);
				}
			}
			else {
				int p1 = xi;
				if (p1 <= pfx) {
					break;
				}
				final String token = get_token_();
				final char sp = line(xi); 
				comment();
				Object u;
				if (line(xi) == 0 && sp == 0) {
					u = read_yaml(p1);
				}
				else {
					u = read_text(p1);
				}
				map.put(token, u);
			}
		}
		return map.size() > 0 ? map : list.size() > 0 ? list : null;
	}

}