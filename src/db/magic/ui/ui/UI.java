package db.magic.ui.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import db.magic.MgcPlugC;

public class UI {

	/**
	 * Overlay to existing Composite
	 */
	public static MgcGridLayout<Composite> GridLayout(final Composite composite) {
		return new MgcGridLayout<Composite>(composite);
	}

	/**
	 * Overlay to existing Composite
	 */
	public static MgcGridData<Control> GridData(final Control control) {
		return new MgcGridData<Control>(control);
	}

	/**
	 * Create a new Composite with parent
	 */
	public static MgcFormLayout<Composite> compositeForm(final Composite parent) {
		return new MgcFormLayout<Composite>(new Composite(parent, SWT.NONE));
	}

	/**
	 * Create a new Composite with parent
	 */
	public static MgcFillLayout<Composite> compositeFill(final Composite parent) {
		return new MgcFillLayout<Composite>(new Composite(parent, SWT.NONE));
	}

	/**
	 * Create a new Composite with parent
	 */
	public static MgcGridLayout<Composite> compositeGrid(final Composite parent) {
		return new MgcGridLayout<Composite>(new Composite(parent, SWT.NONE));
	}

	/**
	 * Create a new Group with parent
	 */
	public static MgcGroupGridLayout groupGrid(final Composite parent) {
		return new MgcGroupGridLayout(new Group(parent, SWT.NONE));
	}

	/**
	 * Create a new Group with parent
	 */
	public static MgcGroupFillLayout groupFill(final Composite parent) {
		return new MgcGroupFillLayout(new Group(parent, SWT.NONE));
	}

	/**
	 * Create a new Label with parent
	 */
	public static MgcLabel label(final Composite parent) {
		return new MgcLabel(new Label(parent, SWT.NONE));
	}

	/**
	 * Create a new Label with parent
	 */
	public static MgcLabel label(final Composite parent, final int style) {
		return new MgcLabel(new Label(parent, style));
	}

	/**
	 * Create a new Text with parent
	 */
	public static MgcText text(final Composite parent, final int style) {
		return new MgcText(new Text(parent, style));
	}

	/**
	 * Create a new Button with parent
	 */
	public static MgcButton button(final Composite parent, final int style) {
		return new MgcButton(new Button(parent, style));
	}

	/**
	 * Create a new CCombo with parent
	 */
	public static MgcCCombo combo(final Composite parent, final int style) {
		final CCombo c = new CCombo(parent, style);
		c.setVisibleItemCount(10);
		return new MgcCCombo(c);
	}

	/**
	 * Create a new CCombo with parent
	 */
	public static MgcCCombo combo(final Composite parent, final boolean readOnly) {
		final CCombo c = new CCombo(parent, readOnly ? SWT.READ_ONLY : SWT.NONE);
		c.setVisibleItemCount(10);
		c.setBackground(MgcPlugC.ComboColor(readOnly));
		return new MgcCCombo(c);
	}

}