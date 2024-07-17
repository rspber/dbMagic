package db.magic.uai;

public class UAIFieldItem extends UAIItem {

	public static final int EXPR_TYPE = 1;	// {&...	
	public static final int VALUE_TYPE = 0;	// {?...	

	public final int type;
	public final String fieldTypeRefer;
	public final String title;
	public final String defualt;

	public UAIFieldItem(final String phrase, final int type, final String name,
			final String fieldTypeRefer, final String title, final String defualt
	) {
		super(phrase, name);	
		this.type    = type;
		this.fieldTypeRefer = fieldTypeRefer;
		this.title   = title;
		this.defualt = defualt;
	}

}