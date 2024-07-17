package db.magic.db;

import java.sql.Types;

import db.magic.dictios.FDictio;
import db.magic.util.Str;

public class ResuTableColumn {

	public final String schemaName;
	public final String tableName;
	public final String name;
	public final short dataType;
	public final String typeName;
	public final int size;
	public final int dec;
	public final boolean notNull;
	public final String defaultValue;

	public ResuTableColumn(
		final String schemaName,
		final String tableName,
		final String name,
		final short dataType,
		final String typeName,
		final int size,
		final int dec,
		final boolean notNull,
		final String defaultValue)
	{
		this.schemaName = schemaName;
		this.tableName  = tableName; 
		this.name       = name;      
		this.dataType   = dataType;  
		this.typeName   = typeName;  
		this.size       = size;      
		this.dec        = dec;       
		this.notNull    = notNull;   
		this.defaultValue     = defaultValue;    
	}

	public boolean isRightAlign(final FDictio dictio) {
		if (dictio != null ) {
			return dictio.isRightAlign();
		}
		switch(dataType) {
		case Types.BIGINT:
		case Types.DECIMAL:
		case Types.INTEGER:
		case Types.NUMERIC:
		case Types.SMALLINT:
		case Types.TINYINT:
			return true;
		default:
			return false;
		}
	}

	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (o.getClass() != getClass()) {
			return false;
		}
		ResuTableColumn co = (ResuTableColumn) o;
		return
				Str.eq(name, co.name)
			&& dataType == co.dataType
			&& Str.eq(typeName, co.typeName)
			&& size == co.size
			&& dec == co.dec
			&& notNull == co.notNull
			&& Str.eq(defaultValue, co.defaultValue);
	}

}