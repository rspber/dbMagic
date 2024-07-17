package db.magic.yaml;

import java.util.List;
import java.util.Map;

import db.magic.tree.IMgcTreeNode;
import db.magic.util.Chr;
import db.magic.util.Obj;
import db.magic.util.Str;

public class YamlOut {

	private static String esc(final String s) {
		if (s != null && !s.isBlank()) {
			final StringBuilder sb = new StringBuilder();
			boolean esc = !Chr.isAlpha(s.charAt(0)); 
			for (int i = 0; i < s.length(); ++i) {
				final char c = s.charAt(i);
				if (c == '\'' || c == '"' || c == '\\') {
					sb.append('\\');
					esc = true;
				}
				if (!Chr.isAlphaNum(c)) {
					esc = true;
				}
				sb.append(c);
			}
			if (esc) {
				sb.insert(0, '"');
				sb.append('"');
			}
			return sb.toString();
		}
		return s;
	}

 	private static void it_value(final StringBuilder sb, final String value) {
 		if (!Str.isBlank(value)) {
 			sb.append(": ");
 			sb.append(esc(value));
 			sb.append("\n");
 		}
 		else {
 			sb.append(":\n");
 		}
 	}

 	private static void spaces(final StringBuilder sb, int count) {
 		while (--count >= 0) {
 			sb.append(' ');
 		}
 	}
 
	public static void liyaml(final StringBuilder sb, final int pfx, final String name, final String value) {
 		spaces(sb, pfx - 2);
 		sb.append("- ");
 		sb.append(esc(name));
 		it_value(sb, value);
 	}

	private static void liitem(final StringBuilder sb, final int pfx, final String value) {
 		spaces(sb, pfx - 2);
 		sb.append("- ");
		sb.append(esc(value));
		sb.append("\n");
 	}

 	public static void text(final StringBuilder sb, final int pfx, final String name, final String value) {
 		if (value != null) {
 	 		spaces(sb, pfx);
	 		sb.append(esc(name));
 			sb.append(": |\n");
 			final String[] lines = value.split("\\r*\\n");
 			for (final String line : lines) {
 				if (line != null) {
 					spaces(sb, pfx + 2);
 					sb.append(line);
 				}
 	 	 		sb.append('\n');
 			}
 		}
 	}

 	public static void yaml(final StringBuilder sb, final int pfx, final String name, final Object value) {
 		spaces(sb, pfx);
 		sb.append(esc(name));
		if (value == null) {
			sb.append(":\n");
			return;
		}
 		if (value instanceof List) {
			sb.append(":\n");
 			Obj.ofList(value).forEach( s -> liitem(sb, pfx + 2, (String)s) );
 			return;
 		}
 		if (value instanceof String[]) {
			sb.append(":\n");
 			for (final String v : (String[])value) {
 				liitem(sb, pfx + 2, v);
 			}
 			return;
 		}
 		if (value instanceof Map) {
			sb.append(":\n");
			Obj.ofMap(value).forEach((k, v) -> {
 				yaml(sb, pfx + 2, k.toString(), v);
 			});
 			return;
 		}
 		it_value(sb, value.toString());
 	}

 	public static void yamlTreeNode(final StringBuilder sb, final int pfx, final IMgcTreeNode node) {
		node.forEach( v -> {
			if (v.isLeaf()) {
				liitem(sb, pfx, v.toString());
			}
			else {
				yaml(sb, pfx, v.toString(), null);
				yamlTreeNode(sb, pfx + 2, v);
			}
		});
	}

}
