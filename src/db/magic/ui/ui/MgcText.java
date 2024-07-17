package db.magic.ui.ui;

import java.util.function.Consumer;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Text;

import db.magic.util.Str;

public class MgcText extends MgcGridData<Text> {

	public MgcText(final Text text) {
		super(text);
	}

	public MgcText text(final String text) {
		this.c.setText(Str.nn(text));
		return this;
	}

	public MgcText message(final String message) {
		this.c.setMessage(Str.nn(message));
		return this;
	}

	public MgcText intOrNull() {
		c.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				textIntValueCheck(e);
			}

			private void textIntValueCheck(KeyEvent event) {
				final Text t = (Text)event.getSource();
				try {
					String s = t.getText();
					if (Str.is(s) ) {
						Integer.parseInt(s);
					}
					t.setData(s);
				}
				catch (Exception e) {
					final Object s = t.getData();
					t.setText(Str.nn(s instanceof String ? (String)s : null));
				}
			}

		});
		return this;
	}

	public MgcText limitTo(final int limit) {
		c.setTextLimit(limit);
		return this;
	}

	public MgcText onSelect(final Consumer<SelectionEvent> consumer) {
		c.addSelectionListener(SelectionListener.widgetSelectedAdapter(consumer));
		return this;
	}

	public MgcText onModify(final ModifyListener listener) {
		c.addModifyListener(listener);
		return this;
	}

	public MgcText onVerify(final VerifyListener listener) {
		c.addVerifyListener(listener);
		return this;
	}

}