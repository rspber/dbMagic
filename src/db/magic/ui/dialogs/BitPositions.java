package db.magic.ui.dialogs;

import java.math.BigInteger;

import db.magic.util.Num;

public class BitPositions extends Labels implements IBitPositions {

	private BigInteger set = BigInteger.ZERO;
	private int range = 8;

	public BitPositions(final String[] labels, final boolean readOnly) {
		super(labels, readOnly);
	}

	@Override
	public boolean testBit(final int index) {
		return set.testBit(index);
	}

	@Override
	public void setBit(final int index, final boolean value) {
		if (index >= 0) {
			set = value ? set.setBit(index) : set.clearBit(index);
		}
	}

	public void setBits(final Object bits, final short range) {
		long n = 0;
		if (bits instanceof String) {
			n = Num.toLong((String)bits);
		}
		else if (bits instanceof Integer) {
			n = (Integer)bits;
		}
		else if (bits instanceof Long) {
			n = (Long)bits;
		}
		this.range = range == 0 ? 8 : range > 128 ? 128 : range; 
		for (int i = 0; i < this.range; ++i) {
			setBit(i, (n & 1) != 0);
			n >>= 1;
		}
	}

	public long getValue() {
		return set.longValue();
	}

}