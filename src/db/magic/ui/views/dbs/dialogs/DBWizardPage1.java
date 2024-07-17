package db.magic.ui.views.dbs.dialogs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;

import db.magic.MgcImg;
import db.magic.db.DataBase;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;
import db.magic.ui.ui.UI;
import db.magic.ui.views.dbs.driver.JDBCDriverDialog;
import db.magic.util.Str;

public class DBWizardPage1 extends WizardPage {

	private static final String M_STORE_ID = "DBWizardPage1";
	private static final String M_Tit = "Tit";
	private static final String M_DBNam = "DBNam";
	private static final String M_JDCDri = "JDCDri";
	private static final String M_AddFil = "AddFil";
	private static final String M_AddFol = "AddFol";
	private static final String M_RmDri = "RmDri";
	private static final String M_RegDri = "RegDri";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);

	private static final int BUTTON_WIDTH = 100;

	List<String> classPaths = null;
	Text nameText;
	TableViewer tableViewer;
	Table table;
	Button addFileBtn;
	Button addDirBtn;
	Button removeBtn;

	public DBWizardPage1(final ISelection selection) {
		super("page.1");
		setTitle(msg.get(M_Tit));
		setDescription("...");
		setPageComplete(false);
	}

	private void modified() {
		final String str = nameText.getText().trim();
		setPageComplete(Str.is(str) && classPaths.size() > 0);
	}

	@Override
	public void createControl(final Composite parent) {
		final Composite panel = UI.compositeGrid(parent)
				.gridLayout(1, false).marginWH(5, 5).spacingHV(0, 10)
				.client().apply();

		final DataBase olddb = ((DataBaseWizard)getWizard()).olddb;
		{
			final Group groupName = UI.groupGrid(panel).text(msg.get(M_DBNam))
					.gridLayout(1, false)
					.eol().apply();

			nameText = UI.text(groupName, SWT.SINGLE | SWT.BORDER).eol().apply();

			if (olddb != null) {
				nameText.setText(olddb.getName());
			}
			nameText.addModifyListener(new ModifyListener() {

				public void modifyText(final ModifyEvent e) {
					modified();
				}
			});
		}
		{
			final Group groupDriver = UI.groupGrid(panel).text(msg.get(M_JDCDri))
					.gridLayout(1, false)
					.client().apply();

			table = new Table(groupDriver, 0);
			table.setHeaderVisible(false);

			tableViewer = new TableViewer(table);

			table.setHeaderVisible(true);
			table.setLinesVisible(false);

			tableViewer.setContentProvider(new TableContentProvider());
			tableViewer.setLabelProvider(new TableLabelProvider());
			tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
				@Override
				public void selectionChanged(final SelectionChangedEvent event) {
					final ISelection select = event.getSelection();
					removeBtn.setEnabled(select != null);
				}

			});
			{	
				tableViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
				if (olddb != null) {
					classPaths = olddb.classPaths;
				}
				else {
					classPaths = new ArrayList<>();
				}
				tableViewer.setInput(classPaths);
			}

			final Composite buttonComp = UI.compositeGrid(groupDriver).gridLayout(5, false).eol().apply();

			addFileBtn = UI.button(buttonComp, SWT.PUSH).text(msg.get(M_AddFil))
					.minWidth(BUTTON_WIDTH).apply();
			addFileBtn.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					final FileDialog openDialog = new FileDialog(table.getShell(), SWT.OPEN);
					final String openFile = openDialog.open();
					if (openFile != null) {
						if (!classPaths.contains(openFile)) {
							classPaths.add(openFile);
							tableViewer.setInput(classPaths);
						}
					}
				}
			});

			addDirBtn = UI.button(buttonComp, SWT.PUSH).text(msg.get(M_AddFol))
					.minWidth(BUTTON_WIDTH).apply();
			addDirBtn.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					final DirectoryDialog openDialog = new DirectoryDialog(table.getShell(), SWT.OPEN);
					final String openFile = openDialog.open();
					if (openFile != null) {
						if (!classPaths.contains(openFile)) {
							classPaths.add(openFile);
							tableViewer.setInput(classPaths);
						}
					}
				}
			});

			removeBtn = UI.button(buttonComp, SWT.PUSH).text(msg.get(M_RmDri))
					.minWidth(BUTTON_WIDTH).apply();
			removeBtn.setEnabled(false);
			removeBtn.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					final int selectionIndex = table.getSelectionIndex();
					if (selectionIndex >= 0) {
						table.remove(selectionIndex);
						classPaths.remove(selectionIndex);
						tableViewer.setInput(classPaths);
					}
				}
			});

			final Button registedBtn = UI.button(buttonComp, SWT.PUSH).text(msg.get(M_RegDri))
					.minWidth(BUTTON_WIDTH + 50).apply();
			registedBtn.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					final JDBCDriverDialog dialog = new JDBCDriverDialog(registedBtn.getShell());
					if (dialog.open() == JDBCDriverDialog.OK) {
						classPaths.addAll(dialog.getTargetNames());
						tableViewer.setInput(classPaths);
					}
				}
			});
		}
		setControl(panel);
	}

	public void setVisible(final boolean visible) {
		super.setVisible(visible);

		if (visible) {
			final DataBaseWizard wiz = (DataBaseWizard)getWizard();
			final Object page = wiz.getNextPage(this);
			if (page instanceof DBWizardPage2) {
				((DBWizardPage2)page).searchDriverFlag = true;
			}
		}
	}

	private class TableLabelProvider extends LabelProvider implements ITableLabelProvider {

		final MgcImg img = MgcImg.getDefault();

		@Override
		public String getColumnText(final Object element, final int columnIndex) {
			switch (columnIndex) {
			case 0:
				return (String)element;
			default:
				break;
			}
			return "";
		}

		@Override
		public Image getColumnImage(final Object element, final int columnIndex) {
			if (element instanceof String) {
				final File file = new File((String) element);
				if (file.exists()) {
					if (file.isDirectory()) {
						return img.getImage(ISharedImages.IMG_OBJ_FOLDER);
					} else {
						return img.getImage(ISharedImages.IMG_OBJ_FILE);
					}
				} else {
					return img.getImage(MgcImg.IMG_CODE_WARNING);
				}
			}
			return null;
		}

		@Override
		public Image getImage(final Object obj) {
			return null;
		}
	}

	private class TableContentProvider implements IStructuredContentProvider {

		@Override
		public Object[] getElements(final Object inputElement) {
			if (inputElement instanceof List) {
				return ((List)inputElement).toArray();
			}
			return null;
		}

		@Override
		public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
			if (newInput != null) {
				modified();
			}
		}

	}

}