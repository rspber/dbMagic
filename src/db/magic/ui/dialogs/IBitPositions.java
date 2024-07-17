package db.magic.ui.dialogs;

public interface IBitPositions {

	public boolean testBit(final int index);

	public void setBit(final int index, final boolean value);

	public boolean isReadOnly();

	public void setState(final int state);

	public boolean OK();

}