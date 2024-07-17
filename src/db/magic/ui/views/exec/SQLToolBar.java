package db.magic.ui.views.exec;

import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;

public class SQLToolBar {

	private CoolBar coolBar;

	private final GlobalAction allExecAction;
	private final GlobalAction nextSqlAction;
	private final GlobalAction prevSqlAction;

	public final ComboSelectDB comboSelectDB = new ComboSelectDB();
	public final SQLLimitControl sqlLimit = new SQLLimitControl();

	public SQLToolBar(final SQLExecView sqlExecView) {

		allExecAction = new GlobalAction(sqlExecView, GlobalAction.EXEC_SQL);
		nextSqlAction = new GlobalAction(sqlExecView, GlobalAction.NEXT_SQL);
		prevSqlAction = new GlobalAction(sqlExecView, GlobalAction.PREV_SQL);
	}

	public void createPartControl(final Composite parent) {
		coolBar = new CoolBar(parent, SWT.FLAT);

		final FormData data = new FormData();
		data.top = new FormAttachment(0, 0);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		coolBar.setLayoutData(data);
		final CoolBarManager coolBarMgr = new CoolBarManager(coolBar);

		{
			final ToolBarManager toolBarMgr = new ToolBarManager(SWT.FLAT);
			toolBarMgr.add(allExecAction);
			coolBarMgr.add(new ToolBarContributionItem(toolBarMgr));
		}
		{
			final ToolBarManager toolBarMgr = new ToolBarManager(SWT.FLAT);
			toolBarMgr.add(prevSqlAction);
			toolBarMgr.add(nextSqlAction);
			coolBarMgr.add(new ToolBarContributionItem(toolBarMgr));
		}
		{
			final ToolBarManager toolBarMgr = new ToolBarManager(SWT.FLAT);
			toolBarMgr.add(comboSelectDB);
			coolBarMgr.add(new ToolBarContributionItem(toolBarMgr));
		}
		{
			final ToolBarManager toolBarMgr = new ToolBarManager(SWT.FLAT);
			toolBarMgr.add(sqlLimit);
			coolBarMgr.add(new ToolBarContributionItem(toolBarMgr));
		}

		coolBarMgr.update(true);

		coolBar.addControlListener(new ControlListener() {

			public void controlMoved(final ControlEvent e) {
			}

			public void controlResized(final ControlEvent e) {
				parent.getParent().layout(true);
				parent.layout(true);
			}
		});
	}

	public CoolBar getCoolBar() {
		return coolBar;
	}

}
