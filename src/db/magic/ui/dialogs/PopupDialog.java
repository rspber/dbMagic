package db.magic.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import db.magic.DBMagicPlugin;
import db.magic.ui.ui.UI;

public abstract class PopupDialog extends Dialog {

	public static final int MODE_COMBO = 0;
	public static final int MODE_R_U50 = 1;

	private final Control reference;
	private final int mode;	// 0 - down under, 1 - r_u50

	protected final int maxHeight; 

	private final Labels lbs;

	protected PopupDialog(final Control reference, final int mode, final Labels lbs, final int maxHeight) {
		super(DBMagicPlugin.getShell());
		this.reference = reference;
		this.mode = mode;
		this.maxHeight = maxHeight;
		this.lbs = lbs;
		lbs.setState(CANCEL);
	}

	protected abstract void createDialogBody(final Composite body);

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite body = (Composite)super.createDialogArea(parent);
		createDialogBody(body);
		return body;
	}

	protected void initTableViewer(final TableViewer tableViewer) {
		UI.GridData(tableViewer.getTable()).client().height(maxHeight).apply();
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setInput(lbs.getLabels());
	}

	@Override
	protected Point getInitialLocation(final Point defaultPosition) {
		if (reference != null) {
			final Rectangle bounds = reference.getBounds();
			int x, y;
			switch(mode) {
			case MODE_R_U50:
				x = bounds.width; 
				y = bounds.height - 50;
				break;
			case MODE_COMBO:
			default:
				x = 0;
				y = bounds.height;
			}
			return reference.toDisplay(x, y);
		}
		return defaultPosition;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		if (!lbs.isReadOnly() ) {
			createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		}
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	protected abstract void returnValues();

	@Override
	protected void setReturnCode(final int code) {
		if (!lbs.isReadOnly() ) {
			if( code == OK  ) {
				returnValues();
				lbs.setState(OK);
			}
			super.setReturnCode(code);
		}
	}

	@Override
	protected boolean isResizable() {
	    return true;
	}	

}