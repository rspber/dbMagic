package db.magic.ui.ui;

import org.eclipse.swt.widgets.Group;

public class MgcGroupFillLayout extends MgcFillLayout<Group> {

	public MgcGroupFillLayout(final Group c) {
		super(c);
	}

	public MgcGroupFillLayout text(final String text) {
		((Group)c).setText(text);;
		return this;
	}

}