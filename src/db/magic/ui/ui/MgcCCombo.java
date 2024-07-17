package db.magic.ui.ui;

import org.eclipse.swt.custom.CCombo;

import db.magic.dictios.FDictio;
import db.magic.util.Str;

public class MgcCCombo extends MgcGridData<CCombo> {

	public MgcCCombo(final CCombo combo) {
		super(combo);
	}

	public MgcCCombo add(final String option) {
		c.add(option);
		return this;
	}

	public MgcCCombo add(final String[] options) {
		for (final String option : options) {
			c.add(option);
		}
		return this;
	}

	public MgcCCombo dictio(final FDictio dictio) {
		for( final String item : dictio.implDescrArray() ) {
			c.add(" " + item);
		}
		return this;
	}

	public MgcCCombo text(final String text) {
		c.setText(Str.nn(text));
		return this;
	}

	public MgcCCombo select(final int selectionIndex) {
		c.select(selectionIndex);
		return this;
	}

}