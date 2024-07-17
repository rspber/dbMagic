package db.magic.ui.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

public class MgcFillLayout<C extends Composite> extends MgcGridData<C> {

	private FillLayout fillLayout;

	@Override
	public C apply() {
		if (fillLayout != null) {
			c.setLayout(fillLayout);
		}
		return super.apply();
	}

	public MgcFillLayout(final C c) {
		super(c);
	}

	private FillLayout fl() {
		if (fillLayout == null) {
			fillLayout = new FillLayout();
		}
		return fillLayout;
	}

	public MgcFillLayout<C> fillLayout() {
		fillLayout = new FillLayout();
		return this;
	}

	public MgcFillLayout<C> fillLayout(final int style) {
		fillLayout = new FillLayout(style);
		return this;
	}

	public MgcFillLayout<C> equalRows() {
		fillLayout = new FillLayout(SWT.VERTICAL);
		return this;
	}

	public MgcFillLayout<C> marginWidth(final int marginWidth) {
		fl().marginWidth = marginWidth;
		return this;
	}

	public MgcFillLayout<C> marginHeight(final int marginHeight) {
		fl().marginHeight = marginHeight;
		return this;
	}

	public MgcFillLayout<C> marginWH(final int marginWidth, final int marginHeight) {
		fl().marginWidth = marginWidth;
		fl().marginHeight = marginHeight;
		return this;
	}

}