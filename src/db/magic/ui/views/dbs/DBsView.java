package db.magic.ui.views.dbs;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;

import db.magic.db.DBManager;
import db.magic.db.DataBase;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;
import db.magic.tree.MgcTreeContentProvider;
import db.magic.ui.ui.UI;
import db.magic.ui.views.dbs.actions.CollapseAllActionRunner;
import db.magic.ui.views.dbs.actions.DBAddActionRunner;
import db.magic.ui.views.dbs.actions.DBConnectActionRunner;
import db.magic.ui.views.dbs.actions.DBCopyActionRunner;
import db.magic.ui.views.dbs.actions.DBDiscoActionRunner;
import db.magic.ui.views.dbs.actions.DBEditActionRunner;
import db.magic.ui.views.dbs.actions.DBRemoveActionRunner;
import db.magic.ui.views.dbs.actions.RefreshAction;

public class DBsView extends ViewPart {

	private static final String M_STORE_ID = "DBsView";
	private static final String M_Root = "Root";

	private final MsgStore msg = MgcMsg.getMsgStore(M_STORE_ID);

	private final RootNode ivisibleRootNode = new RootNode("ivisible");
	public final RootNode rootNode = new RootNode(msg.get(M_Root));

	private TreeViewer viewer;

	private DBAddActionRunner dbAddAction;
	private DBRemoveActionRunner dbRemoveAction;
	private DBEditActionRunner dbEditAction;
	private DBConnectActionRunner dbConnectAction;
	private DBDiscoActionRunner dbDiscoAction;
	private RefreshAction refreshAction;
	private DBCopyActionRunner dbCopyAction;
	private CollapseAllActionRunner collapseAllAction;

	public void createPartControl(final Composite parent) {
		final Composite main = UI.compositeGrid(parent)
				.gridLayout(1, false).marginWH(0, 2).spacingHV(0, 2)
				.eol().apply();

		final Composite body = UI.compositeGrid(main).client().apply();
		body.setLayout(new FillLayout());
		viewer = new TreeViewer(body, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);

		ivisibleRootNode.addChild(rootNode);

		DBManager.getDefault().forEach( db -> {
			rootNode.addChild(new DBNode(db));
		});

		viewer.setContentProvider(new MgcTreeContentProvider());
		viewer.setLabelProvider(new DBsLabelProvider());
		viewer.setUseHashlookup(true);
		viewer.setInput(ivisibleRootNode);
		viewer.expandToLevel(2);

		getSite().setSelectionProvider((ISelectionProvider)viewer);

		createActions();

		createContextMenu();

		viewer.addDoubleClickListener(new DBsDoubleClickHandler());

		createMenuBar();
	}

	private void createActions() {
		dbAddAction = new DBAddActionRunner(this);
		dbRemoveAction = new DBRemoveActionRunner(this);
		dbRemoveAction.setEnabled(false);
		dbEditAction = new DBEditActionRunner(viewer);
		dbConnectAction = new DBConnectActionRunner(viewer);
		dbDiscoAction = new DBDiscoActionRunner(viewer);
		refreshAction = new RefreshAction(viewer);
		dbCopyAction = new DBCopyActionRunner(this);
		collapseAllAction = new CollapseAllActionRunner(viewer);
	}

	private void createContextMenu() {
		final MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(final IMenuManager manager) {
				fillContextMenu(manager);
			}
		});
		final Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void createMenuBar() {
		final IActionBars bars = getViewSite().getActionBars();
		final IToolBarManager manager = bars.getToolBarManager();
		manager.add(dbAddAction);
		manager.add(new Separator());
		manager.add(collapseAllAction);
	}

	private void fillContextMenu(final IMenuManager manager) {
		final Object obj = ((StructuredSelection)viewer.getSelection()).getFirstElement();

		if (obj instanceof RootNode) {
			dbRemoveAction.setEnabled(false);
			manager.add(dbAddAction);
			manager.add(dbRemoveAction);
			manager.add(new Separator());
		}
		else if (obj instanceof DBNode) {
			final DBNode dbNode = (DBNode)obj;
			dbRemoveAction.setEnabled(true);
			manager.add(dbConnectAction);
			manager.add(dbDiscoAction);
			manager.add(new Separator());

			manager.add(dbAddAction);
			manager.add(dbEditAction);
			manager.add(dbRemoveAction);
			manager.add(dbCopyAction);

			manager.add(new Separator());
			dbConnectAction.setEnabled(!dbNode.isConnected());
			dbDiscoAction.setEnabled(dbNode.isConnected());
		}
		else if (obj instanceof SchemaNode) {
			manager.add(refreshAction);
		}
		else if (obj instanceof FolderNode) {
			manager.add(refreshAction);
		}
		else if (obj instanceof TableNode) {
			manager.add(refreshAction);
		}
	}

	public DBNode addDBNode(final DataBase db) {
		final DBNode dbNode = new DBNode(db);
		rootNode.addChild(dbNode);
		return dbNode;
	}

	public TreeViewer getViewer() {
		return viewer;
	}

	@Override
	public void setFocus() {
	}

}