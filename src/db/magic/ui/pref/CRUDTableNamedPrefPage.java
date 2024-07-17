package db.magic.ui.pref;

import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import db.magic.DBMagicPlugin;
import db.magic.dictios.NamedItem;
import db.magic.dictios.NamedItems;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;
import db.magic.ui.ui.UI;

public abstract class CRUDTableNamedPrefPage<T> extends PreferencePage implements IWorkbenchPreferencePage {

	private static final String M_STORE_ID = "CRUDPrefer";
	private static final String M_Add = "Add";
	private static final String M_Edi = "Edi";
	private static final String M_Del = "Del";
	private static final String M_ConfRm = "ConfRm";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);

	protected TableViewer tableViewer;
	protected Button editButton;
	protected Button deleteButton;
	protected final String[] tableHeader;
	protected final ColumnLayoutData[] columnLayoutDatas;

	protected final NamedItems<T> items; 

	private final static int BUTTON_WIDTH = 80;

	public CRUDTableNamedPrefPage(final NamedItems<T> items, final String[] tableHeader, final ColumnLayoutData[] columnLayoutDatas) {
		super();
		this.tableHeader = tableHeader;
		this.columnLayoutDatas = columnLayoutDatas;
		this.items = items;
	}

	@Override
	public void init(IWorkbench workbench) {
	}

	protected abstract ValuesDialog newInputDialog(final NamedItem<T> named);

	protected abstract LabelProvider getLabelProvider();

	@Override
	public boolean performOk() {
		items.finalization();
		return super.performOk();
	}

	@Override
	protected Control createContents(final Composite parent) {
		noDefaultAndApplyButton();
		final Composite composite = UI.compositeGrid(parent)
				.gridLayout(tableHeader.length, false).marginWH(0, 5).spacingHV(5, 5).apply();
		{
			final Composite tableComposite = UI.compositeGrid(composite)
					.gridLayout(1, false).marginWH(0, 0).spacingHV(0, 0)
					.client().width(400).height(150).apply();

			tableViewer = new TableViewer(tableComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);
			final Table table = tableViewer.getTable();
			final TableLayout tableLayout = new TableLayout();
			table.setLayout(tableLayout);
			table.setHeaderVisible(true);
			table.setLinesVisible(true);
			table.setLayoutData(new GridData(GridData.FILL_BOTH));

			for (int i = 0; i < tableHeader.length; ++i) {
				tableLayout.addColumnData(columnLayoutDatas[i]);
				final TableColumn tc = new TableColumn(table, SWT.NONE, i);
				tc.setResizable(columnLayoutDatas[i].resizable);
				tc.setText(tableHeader[i]);
			}
			tableViewer.setContentProvider(new CRUDContentProvider<T>());
			tableViewer.setLabelProvider(getLabelProvider());
			tableViewer.setInput(items.getNamedItems());
			tableViewer.addSelectionChangedListener( event -> {
				final int size = ((IStructuredSelection)event.getSelection()).size();
				editButton.setEnabled(size == 1);
				deleteButton.setEnabled(size > 0);
			});
		}
		{
			final Composite buttonComposite = UI.compositeGrid(composite)
					.gridLayout(1, false).marginWH(0, 0).spacingHV(0, 10)
					.gridData(GridData.CENTER).apply();
			{
				final Button button = UI.button(buttonComposite, SWT.PUSH).text(msg.get(M_Add))
						.minWidth(BUTTON_WIDTH).apply();
				button.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(final SelectionEvent event) {
						final NamedItem<T> named = new NamedItem<T>("", null);
						final ValuesDialog dialog = newInputDialog(named);
						if (dialog.open() == IDialogConstants.OK_ID) {
							items.addNamedItem(named);
							tableViewer.refresh();
						}
					}
				});
			}
			{
				final Button button = UI.button(buttonComposite, SWT.PUSH).text(msg.get(M_Edi))
						.minWidth(BUTTON_WIDTH).apply();
				button.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(final SelectionEvent event) {
						final IStructuredSelection selection = (IStructuredSelection)tableViewer.getSelection();
						final NamedItem<T> org = (NamedItem)selection.getFirstElement();
						final NamedItem<T> named = new NamedItem<T>(org.getName(), org.getItem());
						final ValuesDialog dialog = newInputDialog(named);
						if (dialog.open() == IDialogConstants.OK_ID) {
							final int index = tableViewer.getTable().getSelectionIndex();
							items.setNamedItem(index, named);
							tableViewer.refresh();
						}
					}
				});
				button.setEnabled(false);
				editButton = button;
			}
			{
				final Button button = UI.button(buttonComposite, SWT.PUSH).text(msg.get(M_Del))
						.minWidth(BUTTON_WIDTH).apply();
				button.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(final SelectionEvent event) {
						final int index = tableViewer.getTable().getSelectionIndex();
						final NamedItem<T> old = items.getNamedItem(index);
						if( DBMagicPlugin.confirmDialog(old.getName() + msg.get(M_ConfRm)) ) {
							items.removeNamedItem(old);
							tableViewer.refresh();
						}
					}
				});
				button.setEnabled(false);
				deleteButton = button;
			}
		}
		return composite;
	}

	private static class CRUDContentProvider<T> implements IStructuredContentProvider {

		@Override
		public Object[] getElements(final Object inputElement) {
			if (inputElement instanceof Map) {
				final Map<String, NamedItem<T>> map = (Map<String, NamedItem<T>>)inputElement;
				return map.values().toArray(); 
			}
			return null;
		}

	}

}