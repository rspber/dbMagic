package db.magic.yaml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class ParseJSON extends YAMLToken {

	public ParseJSON(final String name, final List<String> lines) {
		super(name, lines);
	}

	public Object read_json() {
		char c = next_char_();
		switch (c) {
		case '{': {
			++xi;
			Map<String, Object> o = new TreeMap<>();
			while (true) {
				c = next_char_();
				if (c == '}') {
					++xi;
					return o;
				}
				if (c == '"' || c == '\'') {
					String n = quoted__();
					c = skip_blanks_();
					if (c != ':') {
						throw new RuntimeException(": expented");
					}
					++xi;
					o.put(n, read_json());
				}
				else {
					throw new RuntimeException("\" expented");
				}
				c = next_char_();
				++xi;
				if (c == '}') {
					return o;
				}
				if (c != ',') {
					throw new RuntimeException("no } bracket");
				}
			}
		}
		case '[': {
			++xi;
			List<Object> o = new ArrayList<>();
			while (true) {
				c = next_char_();
				if (c == ']') {
					++xi;
					return o;
				}
				o.add(read_json());
				c = next_char_();
				++xi;
				if (c == ']') {
					return o;
				}
				if (c != ',') {
					throw new RuntimeException("no ] bracket");
				}
			}
		}
		default:
			if (c == '"' || c == '\'') {
				return quoted__();
			}
			else {
				return json_const__();
			}
		}
	}

}