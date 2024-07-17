package db.magic.ui.ui;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Control;

public class MgcGridData<C extends Control> {

	protected final C c;

	private GridData gridData;

	public MgcGridData(final C c) {
		this.c = c;
	}

	public C apply() {
		if (gridData != null) {
			c.setLayoutData(gridData);
		}
		return c;
	}

	protected GridData gd() {
		if (gridData == null) {
			gridData = new GridData();
		}
		return gridData;
	}

	public MgcGridData<C> gridData() {
		gd();
		return this;
	}

	public MgcGridData<C> gridData(final int style) {
		gridData = new GridData(style);
		return this;
	}

	public MgcGridData<C> gridData (
		final int horizontalAlignment, final int verticalAlignment,
		final boolean grabExcessHorizontalSpace, final boolean grabExcessVerticalSpace
	) {
		gridData = new GridData(horizontalAlignment, verticalAlignment,
				grabExcessHorizontalSpace, grabExcessVerticalSpace, 1, 1);
		return this;
	}

	public MgcGridData<C> client() {
		gridData = new GridData(GridData.FILL_BOTH);
		return this;
	}

	public MgcGridData<C> eol() {
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		return this;
	}

	public MgcGridData<C> heightFull() {
		gridData = new GridData(GridData.FILL_VERTICAL);
		return this;
	}

	public MgcGridData<C> width(final int width) {
		gd().widthHint = width;
		return this;
	}

	public MgcGridData<C> height(final int height) {
		gd().heightHint = height;
		return this;
	}

	public MgcGridData<C> horizontalSpan(final int horizontalSpan) {
		gd().horizontalSpan = horizontalSpan;
		return this;
	}

	public MgcGridData<C> verticalSpan(final int verticalSpan) {
		gd().verticalSpan = verticalSpan;
		return this;
	}

}