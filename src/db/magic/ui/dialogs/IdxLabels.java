package db.magic.ui.dialogs;

public class IdxLabels extends Labels {

	private int index; 

	public IdxLabels(final String[] labels, final boolean readOnly) {
		super(labels, readOnly);
	}

	public void setIndex(final int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

}