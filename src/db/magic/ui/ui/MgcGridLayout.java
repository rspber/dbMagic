package db.magic.ui.ui;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class MgcGridLayout<C extends Composite> extends MgcGridData<C> {
	
	private GridLayout gridLayout;
	
	@Override
	public C apply() {
		if (gridLayout != null) {
			c.setLayout(gridLayout);
		}
		return super.apply();
	}
	
	public MgcGridLayout(final C c) {
		super(c);
	}
	
	private GridLayout gl() {
		if (gridLayout == null) {
			gridLayout = new GridLayout();
		}
		return gridLayout;
	}
	
	public MgcGridLayout<C> gridLayout() {
		gl();
		return this;
	}
	
	private GridLayout gl(final int numColumns, final boolean makeColumnsEqualWidth) {
		if (gridLayout == null) {
			gridLayout = new GridLayout(numColumns, makeColumnsEqualWidth);
		}
		else {
			gridLayout.numColumns = numColumns;
			gridLayout.makeColumnsEqualWidth = makeColumnsEqualWidth;
		}
		return gridLayout;
	}
	
	public MgcGridLayout<C> gridLayout(final int numColumns, final boolean makeColumnsEqualWidth) {
		gl(numColumns, makeColumnsEqualWidth);
		return this;
	}
	
	public MgcGridLayout<C> marginLeft(final int marginLeft) {
		gl().marginLeft = marginLeft;
		return this;
	}
	
	public MgcGridLayout<C> marginTop(final int marginTop) {
		gl().marginTop = marginTop;
		return this;
	}
	
	public MgcGridLayout<C> marginRight(final int marginRight) {
		gl().marginRight = marginRight;
		return this;
	}
	
	public MgcGridLayout<C> marginBottom(final int marginBottom) {
		gl().marginBottom = marginBottom;
		return this;
	}
	
	public MgcGridLayout<C> marginLT(final int marginLeft, final int marginTop) {
		gl().marginLeft = marginLeft;
		gl().marginTop = marginTop;
		return this;
	}
	
	public MgcGridLayout<C> marginWidth(final int marginWidth) {
		gl().marginWidth = marginWidth;
		return this;
	}
	
	public MgcGridLayout<C> marginHeight(final int marginHeight) {
		gl().marginHeight = marginHeight;
		return this;
	}
	
	public MgcGridLayout<C> marginWH(final int marginWidth, final int marginHeight) {
		gl().marginWidth = marginWidth;
		gl().marginHeight = marginHeight;
		return this;
	}
	
	public MgcGridLayout<C> horizontalSpacing(final int horizontalSpacing) {
		gl().horizontalSpacing = horizontalSpacing;
		return this;
	}
	
	public MgcGridLayout<C> verticalSpacing(final int verticalSpacing) {
		gl().verticalSpacing = verticalSpacing;
		return this;
	}

	public MgcGridLayout<C> spacingHV(final int horizontalSpacing, final int verticalSpacing) {
		gl().horizontalSpacing = horizontalSpacing;
		gl().verticalSpacing = verticalSpacing;
		return this;
	}

}