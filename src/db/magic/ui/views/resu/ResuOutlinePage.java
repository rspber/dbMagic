package db.magic.ui.views.resu;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import db.magic.db.ResuTableColumn;
import db.magic.db.ResuTableElement;
import db.magic.descr.DescrColumnNode;
import db.magic.descr.DescrManager;
import db.magic.descr.DescrTableNode;
import db.magic.dictios.FDictio;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;
import db.magic.tree.MgcTreeLeaf;
import db.magic.tree.MgcTreeNode;
import db.magic.tree.IMgcTreeNode;
import db.magic.ui.dialogs.DictDialogUtil;
import db.magic.ui.ui.UI;
import db.magic.util.Str;

public class ResuOutlinePage extends Page implements IContentOutlinePage {

	private static final String M_STORE_ID = "Outline";
	private static final String M_Mne = "Mne";
	private static final String M_Nam = "Nam";
	private static final String M_Hex = "Hex";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);

	private ListenerList<ISelectionChangedListener> selectionChangedListeners = new ListenerList<>();

	private DescrManager descrManager = DescrManager.getDefault();

	private Composite main;
	private Label dbTitle;
	private Button showMnems;
	private Button showNames;
	private Composite first = null;

	private ResuTableElement lastElement = null;

	private ResuTableColumn[] columns;
	private Label[] labels;

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		selectionChangedListeners.add(listener);
	}

	@Override
	public void createControl(final Composite parent) {

		main = UI.compositeGrid(parent).gridLayout(1, false).apply();

		final Composite header = UI.compositeGrid(main).gridLayout(3, true).gridData().height(25).apply();
		dbTitle = UI.label(header).text("No header").apply();
		showMnems = UI.button(header, SWT.CHECK).text(msg.get(M_Mne)).apply();
		showMnems.setSelection(true);
		showMnems.addSelectionListener( new SelectionListener() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				refreshLabels();
			}
			@Override
			public void widgetDefaultSelected(final SelectionEvent event) {
			}
		});
		showNames = UI.button(header, SWT.CHECK).text(msg.get(M_Nam)).apply();

		refresh(null, null);
	}

	public void setInput(final IEditorInput input) {
		int i = 0;
	}

	public void refresh(final String partName, final ResuTableElement element) {

		if (element == lastElement) {
			return;
		}
		lastElement = element;

		if (first != null) {
			first.dispose();
		}
		labels = null;

		dbTitle.setText( partName != null ? partName : "No Name");

		first = UI.compositeFill(main)
				.fillLayout()
				.gridData().eol().height(main.getParent().getClientArea().height).apply();

		final ScrolledComposite scrolled = new ScrolledComposite(first, SWT.V_SCROLL);
		UI.GridLayout(scrolled).gridLayout().client().apply();

		final Composite group = UI.compositeGrid(scrolled).gridLayout(2, false).apply();

		if (element == null) {
			UI.label(group).text("No signal detected...").apply();
		}
		else {
			columns = element.getColumns();

			labels = new Label[columns.length];

			for (int i = 0; i < columns.length; ++i) {
				labels[i] = UI.label(group).text(columns[i].name).apply();

				final FDictio dictio = element.getDictio(i);
				if (dictio != null && dictio.isMgcDictio()) {

					final String value = (String)element.getItem(i);

					final Composite span = UI.compositeFill(group).apply();

					if (dictio.mode == FDictio.MODE_VECTOR) {
						span.setLayout(new GridLayout(5, false));

						final Text explainText = UI.text(span, SWT.BORDER)
								.text((String)element.getPictItem(i)).width(110).apply();

						final Button vaArrow = UI.button(span, SWT.ARROW | SWT.DOWN).apply();

						UI.label(span).text(msg.get(M_Hex)).apply();
						final Text hexText = UI.text(span, SWT.BORDER)
								.text(Str.is(value) ? String.format("%04x", Long.parseLong(value)) : Str.EMPTY)
								.width(60).apply();

						final Text vaText = UI.text(span, SWT.BORDER).text(value).width(60).apply();

						vaArrow.addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(final SelectionEvent e) {
								DictDialogUtil.multiCheckBoxArrowDown(dictio, true, vaText);
							}
						});
					}
					else {
						span.setLayout(new GridLayout(2, false));

						final CCombo explainCombo = UI.combo(span, true).dictio(dictio)
								.select(dictio.getValueIndex(value)).width(250).apply();
						final Text vaText = UI.text(span, SWT.BORDER).text(value).width(60).apply();
					}
				}
				else {
					UI.text(group, SWT.BORDER).text((String)element.getPictItem(i)).eol().apply();
				}
			}
		}

		scrolled.setContent(group);
		scrolled.setExpandHorizontal(true);
		scrolled.setExpandVertical(true);
		scrolled.setMinSize(group.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		main.layout(true, true);
	}

	private void refreshLabels() {
		if (labels != null) {
			final String tableName = dbTitle.getText();
			descrManager.ivisibleRoot.forEach( tableNode -> {
				if (tableNode.getName().equals(tableName)) {
					refreshLabels((DescrTableNode)tableNode);
				}
			});
		}
	}

	private void refreshLabels(final DescrTableNode tableNode) {
		final boolean mnems = showMnems.getSelection();
		final boolean names = showNames.getSelection();
		for (int i = 0; i  < columns.length; ++i) {
			final String mnem = columns[i].name;
			StringBuilder sb = new StringBuilder();
			if (mnems) {
				sb.append(mnem);
			}
			if (names) {
				final DescrColumnNode columnNode = (DescrColumnNode)tableNode.getChild(mnem);
				if (columnNode != null) {
					sb.append(columnNode.getFullName());
				}
			}
		}
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		selectionChangedListeners.remove(listener);
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void setSelection(ISelection selection) {
		int i = 0;
	}

	@Override
	public ISelection getSelection() {
		return null;
	}

	@Override
	public Control getControl() {
		return main;
	}

}