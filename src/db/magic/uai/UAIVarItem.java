package db.magic.uai;

public class UAIVarItem extends UAIItem{

	public final int type;	// 0 - $
	public final String subst;

	public UAIVarItem(final String phrase, final int type, final String name, final String subst) {
		super(phrase, name);	
		this.type    = type;	    
		this.subst   = subst;
	}

}