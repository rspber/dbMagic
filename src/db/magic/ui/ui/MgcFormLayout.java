package db.magic.ui.ui;

import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

public class MgcFormLayout<C extends Composite> extends MgcGridData<C> {

	private FormLayout formLayout;

	@Override
	public C apply() {
		if (formLayout != null) {
			c.setLayout(formLayout);
		}
		return super.apply();
	}

	public MgcFormLayout(final C c) {
		super(c);
	}

	private FormLayout fl() {
		if (formLayout == null) {
			formLayout = new FormLayout();
		}
		return formLayout;
	}

	public MgcFormLayout<C> formLayout() {
		formLayout = new FormLayout();
		return this;
	}

	public MgcFormLayout<C> marginLeft(final int marginLeft) {
		fl().marginLeft = marginLeft;
		return this;
	}

	public MgcFormLayout<C> marginTop(final int marginTop) {
		fl().marginTop = marginTop;
		return this;
	}

	public MgcFormLayout<C> marginWidth(final int marginWidth) {
		fl().marginWidth = marginWidth;
		return this;
	}

	public MgcFormLayout<C> marginHeight(final int marginHeight) {
		fl().marginHeight = marginHeight;
		return this;
	}

}