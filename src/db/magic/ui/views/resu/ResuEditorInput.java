package db.magic.ui.views.resu;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import db.magic.db.DataBase;
import db.magic.util.Str;

public class ResuEditorInput implements IEditorInput {

	private final DataBase db;
	private final String sql;

	public ResuEditorInput(final DataBase db, final String sql) {
		super();
		this.db = db;
		this.sql = sql;
	}

	public DataBase getDataBase() {
		return db;
	}

	@Override
	public String getName() {
		return sql;
	}

	@Override
	public String getToolTipText() {
		return sql;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof ResuEditorInput) {
			final ResuEditorInput input = (ResuEditorInput) o;
			return Str.eq(sql, input.sql);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return sql.hashCode();
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public Object getAdapter(final Class adapter) {
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

}