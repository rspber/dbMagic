package db.magic.ui.dialogs;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TableItem;

public class MultiCheckBox extends PopupDialog {

	private CheckboxTableViewer tableViewer;
	private final BitPositions bps;

	public MultiCheckBox(final Control referer, final int mode,  final BitPositions bps, final int maxHeight) {
		super(referer, mode, bps, maxHeight);
		this.bps = bps;
	}

	public void initValues() {
		final TableItem[] items = tableViewer.getTable().getItems();
		for( int i = 0; i < items.length; ++i ) {
			items[i].setChecked(bps.testBit(i));
		}
	}

	@Override
	protected void createDialogBody(final Composite body) {
		tableViewer = CheckboxTableViewer.newCheckList(body, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION | (bps.isReadOnly() ? SWT.READ_ONLY : 0));
		initTableViewer(tableViewer);
		initValues();
	}

	@Override
	public void returnValues() {
		final TableItem[] items = tableViewer.getTable().getItems();
		for( int i = 0; i < items.length; ++i ) {
			bps.setBit(i, items[i].getChecked() );
		}
	}

}