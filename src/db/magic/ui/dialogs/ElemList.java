package db.magic.ui.dialogs;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class ElemList extends PopupDialog {

	private TableViewer tableViewer;
	private final IdxLabels lbs;
	private StructuredSelection selection;

	public ElemList(final Control referer, final int mode,  final IdxLabels lbs, final int maxHeight) {
		super(referer, mode, lbs, maxHeight);
		this.lbs = lbs;
	}

	@Override
	protected void createDialogBody(final Composite body) {
		tableViewer = new TableViewer(body, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION | (lbs.isReadOnly() ? SWT.READ_ONLY : 0));

        tableViewer.getTable().addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event e) {
                if (!selection.equals(tableViewer.getSelection())) {
                    tableViewer.setSelection(selection);
                }
            }
        });		
		initTableViewer(tableViewer);

		selection = StructuredSelection.EMPTY;
		final int index = lbs.getIndex();
		if (index >= 0) {
			final Object element = tableViewer.getElementAt(index);
			if (element != null) {
				selection = new StructuredSelection(element);
			}
		}
		tableViewer.setSelection(selection, true);
	}

	@Override
	protected void returnValues() {
	}

}