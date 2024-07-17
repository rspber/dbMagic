package db.magic.ui.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import db.magic.dictios.FDictio;

public class DictDialogUtil {

	public static boolean openPopupDialog(final Control referer, final FDictio dictio, final Object value) {
		if (dictio != null) {
			if (dictio.mode == FDictio.MODE_VECTOR) {
				final BitPositions bps = new BitPositions(dictio.implDescrArray(), true);
				bps.setBits(value, dictio.range);
				new MultiCheckBox(referer, 0, bps, 200).open();
			}
			else {
				final IdxLabels lbs = new IdxLabels(dictio.implDescrArray(), true);
				lbs.setIndex(dictio.dictImple().index(value));
				new ElemList(referer, 0, lbs, 200).open();
			}
			return true;
		}
		return false;
	}

	public static void multiCheckBoxArrowDown(final FDictio dictio, final boolean readOnly, final Text vaText) {
		final BitPositions bps = new BitPositions(dictio.implDescrArray(), readOnly);
		bps.setBits(vaText.getText(), dictio.range);
		if (new MultiCheckBox(vaText, 0, bps, 200).open() == IDialogConstants.OK_ID) {
			final long value = bps.getValue();
			vaText.setText(String.valueOf(value));
		}
	}

}