package db.magic.ui.views.sqh;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;

import db.magic.tree.MgcTreeContentProvider;
import db.magic.tree.IMgcTreeNode;
import db.magic.ui.ui.UI;

public class SqHView extends ViewPart {

	private SqHAddActionRunner sqhAddAction;
	private SqHDeleteActionRunner sqhDeleteAction;

	private TreeViewer viewer;

	public void createPartControl(final Composite parent) {

		final Composite main = UI.compositeGrid(parent)
				.gridLayout(1, false).marginWH(0, 2).spacingHV(2, 0)
				.eol().apply();

		final Composite body = UI.compositeFill(main).fillLayout().client().apply();

		viewer = new TreeViewer(body, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new MgcTreeContentProvider());
		viewer.setLabelProvider(new SqHLabelProvider());
		viewer.setInput(SqHManager.getDefault().ivisibleRoot);
		viewer.expandToLevel(2);

		viewer.addDoubleClickListener(new SqHDoubleClickHandler());

		final CompositeRuler ruler = new CompositeRuler();
		final LineNumberRulerColumn rulerColumn = new LineNumberRulerColumn();
		ruler.addDecorator(0, rulerColumn);

		createContextMenu();
	}

	private void createContextMenu() {
		sqhAddAction = new SqHAddActionRunner(viewer);
		sqhDeleteAction = new SqHDeleteActionRunner(viewer);

		final MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(final IMenuManager manager) {
				manager.add(sqhAddAction);
				final Object obj = ((StructuredSelection)viewer.getSelection()).getFirstElement();
				if (obj instanceof IMgcTreeNode) {
					manager.add(sqhDeleteAction);
				}
			}
		});
		final Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	public TreeViewer getViewer() {
		return viewer;
	}

	@Override
	public void setFocus() {
		viewer.getControl().notifyListeners(SWT.Selection, null);
	}

}