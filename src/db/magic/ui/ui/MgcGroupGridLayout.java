package db.magic.ui.ui;

import org.eclipse.swt.widgets.Group;

public class MgcGroupGridLayout extends MgcGridLayout<Group> {

	public MgcGroupGridLayout(final Group c) {
		super(c);
	}

	public MgcGroupGridLayout text(final String text) {
		((Group)c).setText(text);;
		return this;
	}

}