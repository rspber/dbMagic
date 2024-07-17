package db.magic.ui.dialogs;

public class Labels {

	protected final String[] labels;
	protected final boolean readOnly;
	protected int state = 1; // CANCEL  

	public Labels(final String[] labels, final boolean readOnly) {
		this.labels = labels;
		this.readOnly = readOnly;
	}

	public String[] getLabels() {
		return labels;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setState(final int state) {
		this.state = state;
	}	

	public boolean OK() {
		return state == 0;  // OK
	}

}