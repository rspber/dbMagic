package db.magic.ui.views.resu;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import db.magic.MgcPlugC;
import db.magic.db.ResuTableColumn;
import db.magic.db.ResuTableElement;
import db.magic.dictios.FDictio;
import db.magic.dictios.Foreign;
import db.magic.ui.dialogs.DictDialogUtil;
import db.magic.util.Obj;

public class ResuTableLayer {

	public final ResuView resu;
	protected Table table;
	private TableViewer viewer;
	private TableCursor cursor;
	private ResuTableElement lastSelectedElement;

	public ResuTableLayer(final ResuView resu) {
		this.resu = resu;
	}

	private static int min_column_size = 50;
	private static int max_column_size = 600;

	void createTableLayer(final String partName, final ResuTableElement[] elements) {

		table = new Table(resu.main, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		createTableColumns(elements[0]);

		viewer = new TableViewer(table);
		viewer.setContentProvider(new ResuContentProvider());
		viewer.setLabelProvider(new ResuLabelProvider((element) -> element == lastSelectedElement));
		viewer.setInput(elements);

		cursor = new TableCursor(table, SWT.NONE);

		cursor.addListener(SWT.FocusIn, event -> {
			cursor.setBackground(MgcPlugC.SELECTED_CELL_BACKGROUND);
			resu.contentOutlinePage.refresh(partName, lastSelectedElement);
		});

		cursor.addListener(SWT.FocusOut, event -> {
			cursor.setBackground(MgcPlugC.SELECTED_CELL_NOT_FOCUS_BACKGROUND);
			table.setSelection(new TableItem[0]);	// howk !!!
		});

		cursor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selection(partName);
			}
		});

		cursor.addListener(SWT.MouseDown, event -> {
			if ((event.stateMask & SWT.CTRL) != 0) {
				actOnColumn();
			}
		});

		cursor.setBackground(MgcPlugC.SELECTED_CELL_BACKGROUND);
		cursor.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT));

		cursor.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				switch(e.keyCode) {
				case SWT.SPACE:
					actOnColumn();
					break;
				}
			}

		});
	}

	private void createTableColumns(final ResuTableElement header) {

		final TableColumn rowNr = new TableColumn(table, SWT.RIGHT);
		rowNr.pack();
		final ResuTableColumn[] columns = header.getColumns();
		for (int i = 0; i < columns.length; ++i) { 
			final ResuTableColumn column = columns[i];
			final FDictio dictio = header.getDictio(i);
			final TableColumn col = new TableColumn(table, column.isRightAlign(dictio) ? SWT.RIGHT : SWT.LEFT);
			col.setText(column.name);
			col.pack();
		}
		table.setVisible(false);
		for (final TableColumn c : table.getColumns()) {
			c.pack();
			final int width = c.getWidth();
			if (width != 0) {
				if (width < min_column_size) {
					c.setWidth(min_column_size);
				}
				if (width > max_column_size) {
					c.setWidth(max_column_size);
				}
			}
		}
		table.setVisible(true);
	}

	private void actOnColumn(final TableItem tableItem, final int index) {
		final ResuTableElement element = (ResuTableElement)tableItem.getData();
		{
			final Foreign foreign = element.getForeign(index);
			if (foreign != null && Obj.nn(element.getItem(index))) {
				resu.subTableLayer(ResuTableLayer.this).update(element, index, foreign);
				return;
			}
		}
		{
			final FDictio dictio = element.getDictio(index);
			final Object item = element.getItem(index);
			if (DictDialogUtil.openPopupDialog(cursor, dictio, item)) {
				return;
			}
		}
	}

	private void actOnColumn() {
		final TableItem tableItem = cursor.getRow();
		if (tableItem != null) {
			final int columnIndex = cursor.getColumn();
			if (columnIndex > 0) {
				actOnColumn(tableItem, columnIndex - 1);
			}
		}
	}

	private void selection(final String partName) {
		final TableItem item = cursor.getRow();
		final Object newSelectedElement = item != null ? item.getData() : null;

		if (newSelectedElement != null ) {
			if (newSelectedElement != lastSelectedElement) {
				suppressSelection();
				lastSelectedElement = (ResuTableElement)newSelectedElement;
				viewer.update(newSelectedElement, null);
			}
			resu.contentOutlinePage.refresh(partName, lastSelectedElement);
		}
		else {
			suppressSelection();
		}
	}

	private void suppressSelection() {
		final ResuTableElement tmp = lastSelectedElement;
		lastSelectedElement = null;
		if (tmp != null) {
			viewer.getTable().setSelection(new TableItem[0]);	// howk !!!
			viewer.update(tmp, null);
		}
	}

	protected void resetTable() {
		table.dispose();
		cursor.dispose();
	}

	public Table getTable() {
		return table;
	}

	public TableViewer getViewer() {
		return viewer;
	}

}