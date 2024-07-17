package db.magic.dictios;

import db.magic.util.Num;
import db.magic.util.Obj;

public class Foreign {

	public static final String T_Many_To_One  = "Many To One";
	public static final String T_One_To_One   = "One  To One";
	public static final String T_One_To_Many  = "One  To Many";
	public static final String T_Many_To_Many = "Many To Many";

	public static final String[] RefTypes = {T_Many_To_One, T_One_To_One, T_One_To_Many, T_Many_To_Many}; 

	public static final int MANY_TO_ONE  = 0;
	public static final int ONE_TO_ONE   = 1;
	public static final int ONE_TO_MANY  = 2;
	public static final int MANY_TO_MANY = 3;
	
	public final int refType;
	public final String refField;
	public final String refTable;

	public Foreign(final int type, final String refField, final String refTable) {
		this.refType = type;
		this.refField = refField;
		this.refTable = refTable;
	}

	public Foreign(final String type, final String refField, final String refTable) {
		this(Num.toInt(type), refField, refTable);
	}

	public String getRefTypeStr() {
		return RefTypes[refType];
	}

	public static int toType(final String text) {
		return Obj.indexOf(RefTypes, text);
	}

}