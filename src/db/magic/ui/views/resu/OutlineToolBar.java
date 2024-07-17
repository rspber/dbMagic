package db.magic.ui.views.resu;

import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;

public class OutlineToolBar {

	private CoolBar coolBar;

	private final ResuOutlinePage outline;
	
	public OutlineToolBar(final ResuOutlinePage outline) {
		this.outline = outline;
	}

	public void createPartControl(final Composite parent) {
		coolBar = new CoolBar(parent, SWT.FLAT);

		final FormData data = new FormData();
		data.top = new FormAttachment(0, 0);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		coolBar.setLayoutData(data);
		final CoolBarManager coolBarMgr = new CoolBarManager(coolBar);
	}

	public CoolBar getCoolBar() {
		return coolBar;
	}

}