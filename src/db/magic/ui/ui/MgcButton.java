package db.magic.ui.ui;

import java.util.function.Consumer;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;

import db.magic.util.Str;

public class MgcButton extends MgcGridData<Button> {

	public MgcButton(final Button button) {
		super(button);
	}

	public MgcButton minWidth(final int minWidth) {
		final int width = c.computeSize(-1, -1).x;
		gd().widthHint = width < minWidth ? minWidth : width;
		return this;
	}

	public MgcButton text(final String text) {
		c.setText(Str.nn(text));
		return this;
	}

	public MgcButton image(final Image image) {
		c.setImage(image);
		return this;
	}

	public MgcButton onSelect(final Consumer<SelectionEvent> consumer) {
		c.addSelectionListener(SelectionListener.widgetSelectedAdapter(consumer));
		return this;
	}

}