package db.magic.ui.views.exec;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.text.CursorLinePainter;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextViewerExtension2;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.text.source.MatchingCharacterPainter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.texteditor.ITextEditorExtension2;

import db.magic.db.DataBase;
import db.magic.store.MgcMsg;
import db.magic.store.MsgStore;
import db.magic.ui.ui.UI;
import db.magic.util.Num;

public class SQLExecView extends ViewPart implements ITextEditorExtension2 {

	public static final String EXEC_SQL_ACTION  = "db.magic.actions.SQLExecAction"; 
	public static final String NEXT_SQL_ACTION = "db.magic.actions.SQLNextAction"; 
	public static final String PREV_SQL_ACTION = "db.magic.actions.SQLPrevAction"; 

	private SQLSourceViewer sqlViewer;

	private SQLToolBar toolBar;

	private Composite header;

	private Action undoAction;
	private Action redoAction;
	private Action copyAction;
	private Action pasteAction;
	private Action cutAction;
	private Action deleteAction;
	private Action clearAction;
	private Action selAllAction;
	private Action execSqlAction;
	private Action nextSqlAction;
	private Action prevSqlAction;

	public final List<Object[]> uais = new ArrayList<>();	// name, label, operator, value

	public SQLExecView() {
		undoAction    =  new GlobalAction(this, ITextOperationTarget.UNDO);
		redoAction    =  new GlobalAction(this, ITextOperationTarget.REDO);
		copyAction    =  new GlobalAction(this, ITextOperationTarget.COPY);
		pasteAction   =  new GlobalAction(this, ITextOperationTarget.PASTE);
		cutAction     =  new GlobalAction(this, ITextOperationTarget.CUT);
		deleteAction  =  new GlobalAction(this, ITextOperationTarget.DELETE);
		clearAction   =  new GlobalAction(this, GlobalAction.CLEAR);
		selAllAction  =  new GlobalAction(this, ITextOperationTarget.SELECT_ALL);
		execSqlAction =  new GlobalAction(this, GlobalAction.EXEC_SQL);
		prevSqlAction =  new GlobalAction(this, GlobalAction.PREV_SQL);
		nextSqlAction =  new GlobalAction(this, GlobalAction.NEXT_SQL);
	}

	public void setSqlText(final String sql) {
		if (sqlViewer != null) {
			sqlViewer.getDocument().set(sql);
		}
	}

	public SQLSourceViewer getSqlViewer() {
		return sqlViewer;
	}

	public Composite getHeader() {
		return header;
	}

	public void createContextMenu() {
		final MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(final IMenuManager manager) {
				fillContextMenu(manager);
			}
		});
		final Menu menu = menuMgr.createContextMenu(sqlViewer.getTextWidget());
		sqlViewer.getTextWidget().setMenu(menu);
		getSite().registerContextMenu(menuMgr, sqlViewer);
	}

	void fillContextMenu(final IMenuManager manager) {
		manager.add(undoAction);
		manager.add(redoAction);
		manager.add(new Separator());
		manager.add(copyAction);
		manager.add(pasteAction);
		manager.add(new Separator());
		manager.add(cutAction);
		manager.add(deleteAction);
		manager.add(clearAction);
		manager.add(selAllAction);
		manager.add(new Separator());
		manager.add(execSqlAction);
		manager.add(new Separator());
		manager.add(prevSqlAction);
		manager.add(nextSqlAction);
		manager.add(new Separator());
	}

	@Override
	public void createPartControl(final Composite parent) {
		final Composite form = UI.compositeForm(parent).formLayout().apply();

		toolBar = new SQLToolBar(this);
		toolBar.createPartControl(form);

		header = UI.compositeFill(form).equalRows().marginWidth(2).apply();

		final FormData formData = new FormData();
		formData.top = new FormAttachment(toolBar.getCoolBar(), 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		// formData.bottom = new FormAttachment(footerComposite, -2);
		formData.bottom = new FormAttachment(95, 0);
		header.setLayoutData(formData);

		createRuler();

		sqlViewer = new SQLSourceViewer(this, header, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
//		((StyledText)sqlViewer.getControl()).setMargins(5, 5, 15, 15);
		getSite().setSelectionProvider(sqlViewer);

		createActionBars();

		createContextMenu();
	}

	private void createRuler() {
		final  LineNumberRulerColumn rulerColumn = new LineNumberRulerColumn();
		final CompositeRuler ruler = new CompositeRuler();
		ruler.addDecorator(0, rulerColumn);
	}

	protected void createActionBars() {
		final IActionBars actionBars = getViewSite().getActionBars();

		actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), undoAction);
		actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), redoAction);
		actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(), copyAction);
		actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), pasteAction);
		actionBars.setGlobalActionHandler(ActionFactory.CUT.getId(), cutAction);
		actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteAction);
		actionBars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), selAllAction);
		actionBars.setGlobalActionHandler(EXEC_SQL_ACTION, execSqlAction);
		actionBars.setGlobalActionHandler(PREV_SQL_ACTION, prevSqlAction);
		actionBars.setGlobalActionHandler(NEXT_SQL_ACTION, nextSqlAction);

		actionBars.updateActionBars();
	}

	// ITextEditorExtension2
	@Override
	public boolean isEditorInputModifiable() {
		return true;
	}

	// ITextEditorExtension2
	@Override
	public boolean validateEditorInputState() {
		return true;
	}

	public DataBase getDataBase() throws Exception {
		return toolBar.comboSelectDB.getDataBase();
	}

	public void updateCombo(final DataBase newdb) {
		toolBar.comboSelectDB.updateCombo(newdb);
	}

	public void updateSqHQueryButtons() {
		// TODO Auto-generated method stub
	}

	public int sqlLimit() {
		return Num.toInt(toolBar.sqlLimit.sqlLimit.getText());
	}

	@Override
	public void setFocus() {
		if (sqlViewer != null) {
			sqlViewer.getControl().setFocus();
		}
	}

	@Override
	public void dispose() {
//		undoAction.dispose();
//		redoAction.dispose();
//		copyAction.dispose();
//		pasteAction.dispose();
//		cutAction.dispose();
//		deleteAction.dispose();
//		clearAction.dispose();
//		selAllAction.dispose();
//		updAIAction.dispose();
//		execSqlAction.dispose();
//		nextSqlAction.dispose();
//		prevSqlAction.dispose();
		super.dispose();
	}

}