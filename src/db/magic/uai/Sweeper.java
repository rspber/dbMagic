package db.magic.uai;

import java.util.List;
import java.util.Map;

import db.magic.dictios.VariesSupplyer;
import db.magic.dictios.Vario;
import db.magic.util.Str;

public class Sweeper {

	private final static int MAX_REF_DEPTH = 10;

	private final static String START_SEQ = "{";
	private final static String END_SEQ = "}";
	private final static String VAR_SEQ = "$";
	private final static String UAI_AND_SEQ = "&";
	private final static String UAI_VAL_SEQ = "?";
	private final static String UAI_TYPE_REF_SEQ = ":";
	private final static String UAI_TITLE_SEQ = ":";
	private final static String UAI_EQ_SEQ = "=";

	private VariesSupplyer varies = VariesSupplyer.getDefault();

	private static Sweeper fDefault;
	public static Sweeper getDefault() {
		if( fDefault == null ) {
			fDefault = new Sweeper();
		}
		return fDefault;
	}

	public String resolve(final String any, final Map<String, String> library) {
		if (any != null) {
			final StringBuilder sb = new StringBuilder(any);
			return resolve(sb, library) ? sb.toString() : null;
		}
		return null;
	}

	public boolean resolve(final StringBuilder sb, final Map<String, String> library) {
		return new Resolver(library, null, null).resolve(sb);
	}

	public boolean resolve(final String any, final List<UAIItem> resolved) {
		return new Resolver(null, null, resolved).resolve(new StringBuilder(any));
	}

	public boolean resolve(final StringBuilder sb, final List<String> uaiValues) {
		return new Resolver(null, uaiValues, null).resolve(sb);
	}

	private class Resolver {

		final Map<String, String> library;
		final List<String> uaiValues;
		final List<UAIItem> resolved;		
		int depth;

		public Resolver(final Map<String, String> library, final List<String> uaiValues, final List<UAIItem> resolved) {
			this.library = library;
			this.uaiValues = uaiValues;
			this.resolved = resolved;
		}		

		private boolean parse(final ScanTrucker ce, final boolean embed) {

			while (true) {
				int ib = ce.ib;

				boolean ok = ce.skip_to(START_SEQ);
				int k = ce.ib;

				if (embed) {
					ce.ib = ib;
					if (ce.skip_to(END_SEQ)) {
						if (ce.ib - END_SEQ.length() < k - START_SEQ.length()) {
							return true;
						}
					}
					ce.ib = k;
				}

				if (!ok) {
					return true;
				}

				ib = k - START_SEQ.length();

				final boolean and = ce.skip(UAI_AND_SEQ);
				final boolean ask = !and ? ce.skip(UAI_VAL_SEQ) : false;

				if (and || ask) {

					final String name = ce.skip_word();
					if (name == null) {
						ce.ib = ib + 1;
						continue;
					}

					final String fieldTypeRefer = ce.skip(UAI_TYPE_REF_SEQ) ? ce.skip_word() : null;
					final String title = ce.skip(UAI_TITLE_SEQ) ? ce.skip_word() : null;
					final String defualt = ce.skip(UAI_EQ_SEQ) ? ce.skip_word() : null;

					if (ce.skip(END_SEQ)) {

						if (resolved != null) {
							final String phrase = ce.sb.substring(ib, ce.ib);
							resolved.add(new UAIFieldItem(phrase, and ? 1 : 0, name, fieldTypeRefer, title, defualt));
						}

						if (uaiValues != null && ce.uainr < uaiValues.size() ) {
							final String subst = uaiValues.get(ce.uainr++);
							ce.sb.delete(ib, ce.ib);
							ce.ib = ib;
							if (!Str.isBlank(subst)) {
								ce.sb.insert(ib, subst); 
							}
							else {
								if (embed) {
									return false;
								}
							}
						}
					}
					else {
						ce.ib = ib + 1;
					}
					continue;
				}

				if (ce.skip(VAR_SEQ)) {
					final String name = ce.skip_word();
					if (name == null) {
						ce.ib = ib + 1;
						continue;
					}
					if (ce.skip(END_SEQ)) {

						String val = null;
						if (library != null) {
							val = library.get(name);
						}
						if (val == null) {
							final Vario vario = varies.getVario(name);
							val = vario != null ? vario.value : null;
						}

						final String subst;
						if (val != null) {
							final StringBuilder sb = new StringBuilder(val);
							subst = resolve(sb) ? sb.toString() : null;
						}
						else {
							subst = null;
						}

						if (resolved != null) {
							final String phrase = ce.sb.substring(ib, ce.ib);
							resolved.add(new UAIVarItem(phrase, 0, name, subst));
						}

						if (val != null) {
							ce.sb.delete(ib, ce.ib);
							if (subst != null ) {
								ce.sb.insert(ib, subst);
							}
							ce.ib = ib + (subst != null ? subst.length() : 0);
						}
						else {
							if (embed) {
								return false;
							}
						}
					}
					else {
						ce.ib = ib + 1;
					}
					continue;
				}

				if (!parse(ce, true)) {
					ce.sb.delete(ib, ce.ib + START_SEQ.length());
					ce.ib = ib;
				}
				else {
					ce.ib -= END_SEQ.length();
					ce.sb.delete(ce.ib, ce.ib + END_SEQ.length());
					ce.sb.delete(ib, ib + START_SEQ.length());
					ce.ib -= START_SEQ.length();
				}
			}
		}

		public boolean resolve(final StringBuilder sb) {

			if (++depth > MAX_REF_DEPTH) {
				throw new RuntimeException("Circular reference occured during sql phrases resolving\n\n" + sb.toString());
			}

			parse(new ScanTrucker(sb), false);
			return true;
		}

	}

}