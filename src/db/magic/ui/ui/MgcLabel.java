package db.magic.ui.ui;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Label;

import db.magic.util.Str;

public class MgcLabel extends MgcGridData<Label> {

	public MgcLabel(final Label label) {
		super(label);
	}

	public MgcLabel text(final String text) {
		c.setText(Str.nn(text));
		return this;
	}

	public MgcLabel image(final Image image) {
		c.setImage(image);
		return this;
	}

	public MgcLabel align(final int alignment) {
		c.setAlignment(alignment);
		return this;
	}

}