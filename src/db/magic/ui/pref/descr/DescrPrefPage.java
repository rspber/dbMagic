package db.magic.ui.pref.descr;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import db.magic.DBMagicPlugin;
import db.magic.descr.DescrCategoryNode;
import db.magic.descr.DescrColumnNode;
import db.magic.descr.DescrItemData;
import db.magic.descr.DescrManager;
import db.magic.descr.DescrTableNode;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;
import db.magic.tree.MgcTreeContentProvider;
import db.magic.tree.IMgcTreeNode;
import db.magic.ui.pref.ValuesDialog;
import db.magic.ui.ui.UI;
import db.magic.util.Str;

public class DescrPrefPage extends PreferencePage implements IWorkbenchPreferencePage {

	private static final String M_STORE_ID = "DescrPrefer";
	private static final String M_ConfRm = "ConfRm";
	private static final String M_Add = "Add";
	private static final String M_Edi = "Edi";
	private static final String M_Del = "Del";
	private static final String M_Nam = "Nam";
	private static final String M_Des = "Des";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);

	private final static int BUTTON_WIDTH = 80;

	private final DescrManager descrManager = DescrManager.getDefault();

	private TreeViewer viewer;
	private Button editButton;
	private Button deleteButton;

	private DescrTableNode deTableNode;
	private DescrColumnNode deColumnNode;
	private Label deOb;
	private Text deId;
	private Text deName;
	private StyledText descro;

	@Override
	public void init(IWorkbench workbench) {
		
	}

	@Override
	public boolean performOk() {
		descrManager.finalization();
		return super.performOk();
	}

	@Override
	protected Control createContents(final Composite parent) {
		noDefaultAndApplyButton();

		final Composite main = UI.compositeGrid(parent)
				.gridLayout(2, false).marginWH(0, 0).spacingHV(0, 0)
				.client().width(400).height(150).apply();

		final Composite left = UI.compositeFill(main).fillLayout().heightFull().width(300).apply();

		viewer = new TreeViewer(left, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new MgcTreeContentProvider());
		viewer.setLabelProvider(new DescrLabelProvider());
		viewer.setInput(descrManager.ivisibleRoot);
		viewer.expandToLevel(2);
		viewer.addSelectionChangedListener( event -> {
			deTableNode = null;
			deColumnNode = null;
			IMgcTreeNode picked = getSelectedNode();
			if (picked instanceof DescrTableNode) {
				deTableNode = (DescrTableNode)picked;
				deOb.setText("table:");
				deId.setText(deTableNode.getName());
				deName.setText(Str.nn(deTableNode.getFullName()));
				deName.setEnabled(true);
				descro.setText(Str.nn(deTableNode.getDescription()));
				descro.setEnabled(true);
			}
			else
			if (picked instanceof DescrColumnNode) {
				deColumnNode = (DescrColumnNode)picked;
				deOb.setText("column:");
				deId.setText(deColumnNode.getName());
				deName.setText(Str.nn(deColumnNode.getFullName()));
				deName.setEnabled(true);
				descro.setText(Str.nn(deColumnNode.getDescription()));
				descro.setEnabled(true);
			}
			else {
				deOb.setText("");
				deId.setText("");
				deName.setEnabled(false);
				descro.setText("");
				descro.setEnabled(false);
			}
		});
		viewer.addDoubleClickListener(new DescrDoubleClickHandler());

		final Composite right = UI.compositeGrid(main)
				.gridLayout(1, false).marginWH(0, 0).spacingHV(0, 0)
				.client().apply();

		final Composite operate = UI.compositeGrid(right)
				.gridLayout(2, false).marginWH(0, 0).spacingHV(0, 0)
				.eol().apply();

		final Composite buttonComposite = UI.compositeGrid(operate)
				.gridLayout(1, false).marginWH(0, 0).spacingHV(0, 10)
				.gridData(GridData.CENTER).apply();
		{
			final Button button = UI.button(buttonComposite, SWT.PUSH).text(msg.get(M_Add))
					.minWidth(BUTTON_WIDTH).apply();

			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent event) {
					final DescrItemData data = getSelectedItemData();
					final ValuesDialog dialog = new DescrAddDialog(data);
					if (dialog.open() == IDialogConstants.OK_ID) {
						descrManager.addItemData(data);
						viewer.refresh();
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
					final DescrItemData old = getSelectedItemData();
					final DescrItemData data = new DescrItemData(old);
					final ValuesDialog dialog = new DescrAddDialog(data);
					if (dialog.open() == IDialogConstants.OK_ID) {
						descrManager.updItemData(old, data);
						viewer.refresh();
					}
				}
			});

//				button.setEnabled(false);
			editButton = button;
		}
		{
			final Button button = UI.button(buttonComposite, SWT.PUSH).text(msg.get(M_Del))
					.minWidth(BUTTON_WIDTH).apply();

			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent event) {
					final IMgcTreeNode old = getSelectedNode();
					if( DBMagicPlugin.confirmDialog(msg.get(M_ConfRm) + old.getName()) ) {
						old.getParent().removeChild(old);
						viewer.refresh();
					}

				}
			});

//				button.setEnabled(false);
			deleteButton = button;
		}

		final Composite descr = UI.compositeGrid(operate)
				.gridLayout(2, false).marginLT(100, 50).marginBottom(20)
				.client().apply();

		deOb = UI.label(descr).text("________:").apply();
		deId = UI.text(descr, SWT.NONE).apply();
		deId.setEnabled(false);
		UI.label(descr).text(msg.get(M_Nam)).apply();
		deName = UI.text(descr, SWT.BORDER).eol().apply();
		deName.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (deTableNode != null) {
					deTableNode.setFullName(deName.getText());
					viewer.refresh();
				}
				if (deColumnNode != null) {
					deColumnNode.setFullName(deName.getText());
					viewer.refresh();
				}
			}
		});
		deName.setEnabled(false);

		UI.label(right).text(msg.get(M_Des)).apply();
		descro = new StyledText(right, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		descro.setMargins(5, 5, 15, 15);
		descro.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (deTableNode != null) {
					deTableNode.setDescription(descro.getText());
				}
				if (deColumnNode != null) {
					deColumnNode.setDescription(descro.getText());
				}
			}
		});
		UI.GridLayout(descro).client().apply();
		descro.setEnabled(false);

		return main;
	}

	private IMgcTreeNode getSelectedNode() {
		final IStructuredSelection selection = (IStructuredSelection)viewer.getSelection();
		return (IMgcTreeNode)selection.getFirstElement();
	}

	private DescrItemData getSelectedItemData() {

		final DescrItemData data = new DescrItemData();

		IMgcTreeNode picked = getSelectedNode();
		boolean ok = true;
		if (picked instanceof DescrColumnNode) {
			data.columnId = picked.getName();
			ok = false;
			picked = picked.getParent();
		}
		if (picked instanceof DescrTableNode) {
			data.tableId = picked.getName();
			if (ok) {
			}
			picked = picked.getParent();
		}
		if (picked instanceof DescrCategoryNode) {
			data.category = picked.getName();
			picked = picked.getParent();
		}
		return data;
	}

}