package db.magic.ui.pref;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;

public abstract class ValuesDialog extends Dialog {

	public ValuesDialog(final Shell parent) {
		super(parent);
	}

	protected abstract boolean save();

	@Override
	protected void okPressed() {
		if (save()) {
			super.okPressed();
		}
	}

	@Override
	protected boolean isResizable() {
	    return true;
	}	

}