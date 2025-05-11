// i_math.adligo.org/src/org/adligo/i_math/shared/HugeInt.java
package org.adligo.math.shared;

import java.math.BigInteger;
import java.nio.IntBuffer;
import java.util.function.Supplier;

import org.adligo.i_math.shared.I_HugeInt;

/**
 * Immutable implementation of I_HugeInt using a linked list of IntArrayLink nodes.
 * Each node contains an array of integers, and the class tracks the number of links.
 */
public class HugeInt implements I_HugeInt {
	public static final String THIS_HUGE_INT_DOESN_T_FIT_INTO_A_INT = "This HugeInt doesn't fit into a int!";
	public static final HugeInt ZERO = new HugeInt(0);
	public static final HugeInt ONE = new HugeInt(1);
	public static final HugeInt TWO = new HugeInt(2);
	
	/**
	 * This rebuilds the doubly linked list so that everything (next, previous) is correct.
	 * @param head
	 * @param current
	 * @param right the right most new data 
	 * @return
	 */
	public static final IntArrayLink[] rebuild(IntArrayLink head, IntArrayLink current, IntArrayLink right) {
		IntArrayLink c = current;
		IntArrayLink r = right;
		while (c.hasPrevious()) {
			c = new IntArrayLink(c.getData(), c.getPrevious(), right);
			r = c;
			c = c.getPrevious();
		}
		return new IntArrayLink[] {c, right};
	}
	
    private final IntArrayLink head;
    private final IntArrayLink right;
    private final boolean negative;
    private final I_HugeInt linkCount;

    /**
     * Constructs a HugeInt from a single integer.
     */
    public HugeInt(int value) {
        this.head = new IntArrayLink(new int[]{Math.abs(value)});
        this.right = this.head;
        if (value < 0) {
        	this.negative = true;
        } else {
        	this.negative = false;
        }
        this.linkCount = ONE;
    }
    
    /**
     * Constructs a HugeInt from an array of integers.
     * Each array element becomes a node in the linked list.
     * @param negative if the number is negative
     * @param input in little to big ending order all integers are interpreted for their byte values, so the negative sign
     *   as a bit is used for positive or negative bits depending on the negative parameter.
     */
    public HugeInt(boolean negative, Supplier<Integer> input) {
    	this.negative = negative;
        IntBuffer buf = IntBuffer.allocate(8);
        Integer next = input.get();
        if (next == null) {
        	this.head = new IntArrayLink(new int[]{0});
        	this.right = this.head;
            this.linkCount = new HugeInt(1);
        	
        } else {
        	buf.put(next);
	        int counter = 1;
	        I_HugeInt linkCountLocal = new HugeInt(1);
	        IntArrayLink headLocal = null;
	        IntArrayLink rightLocal = null;
	        while (next != null) {
	        	if (counter >= 8) {
	        		int [] ints = new int [8];
	        		for (int i = 0; i < 8; i++) {
						int n = buf.get(i);
						ints[7-i] = n;
						if (headLocal == null) {
							headLocal = new IntArrayLink(ints);
							rightLocal = headLocal;
						} else if (headLocal == rightLocal) {
							rightLocal = new IntArrayLink(ints);
							headLocal = new IntArrayLink(headLocal, rightLocal);
						} else {
							IntArrayLink nextRightLocal = new IntArrayLink(ints, rightLocal);
							IntArrayLink prev = rightLocal.getPrevious();
							IntArrayLink replacement = new IntArrayLink(rightLocal, prev, nextRightLocal);
							rightLocal = new IntArrayLink(ints);
							IntArrayLink[] vals = rebuild(headLocal, rightLocal, nextRightLocal);
							headLocal = vals[0];
							rightLocal = vals[1];
						}
						linkCountLocal = linkCountLocal.add(HugeInt.ONE);
					}
	        		buf = IntBuffer.allocate(8);
	        	}
	        	buf.put(next);
	        	counter++;
	        }
	        int remaining = buf.position();
	        int [] ints = new int [remaining];
			for (int i = 0; i < remaining; i++) {
				int n = buf.get(i);
				ints[remaining-1-i] = n;
				if (headLocal == null) {
					headLocal = new IntArrayLink(ints);
					rightLocal = headLocal;
				} else if (headLocal == rightLocal) {
					rightLocal = new IntArrayLink(ints);
					headLocal = new IntArrayLink(headLocal, rightLocal);
				} else {
					IntArrayLink nextRightLocal = new IntArrayLink(ints, rightLocal);
					IntArrayLink prev = rightLocal.getPrevious();
					IntArrayLink replacement = new IntArrayLink(rightLocal, prev, nextRightLocal);
					rightLocal = new IntArrayLink(ints);
					IntArrayLink[] vals = rebuild(headLocal, rightLocal, nextRightLocal);
					headLocal = vals[0];
					rightLocal = vals[1];
				}
				linkCountLocal = linkCountLocal.add(HugeInt.ONE);
			}
			this.head = headLocal;
			this.right = rightLocal;
			this.linkCount = linkCountLocal;
        }
    }
    
    /**
     * Returns the number of IntArrayLink nodes in this HugeInt.
     */
    public I_HugeInt getLinkCount() {
        return linkCount;
    }
    


	@Override
	public String toString() {
		return "HugeInt [head=" + head + ", right=" + right + ", negative=" + negative + ", linkCount=" + linkCount
				+ "]";
	}

	@Override
	public I_HugeInt add(I_HugeInt other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isByte() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isShort() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInt() {
		if (head.hasNext()) {
			return false;
		}
		int [] data = head.getData();
		if (data.length > 1) {
			return false;
		}
		if (data[0] == IntArrayLink.ALL_ONES) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isLong() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isBig() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPositive() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public byte toByte() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public short toShort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int toInt() {
		if (head.hasNext()) {
			throw new IllegalStateException(THIS_HUGE_INT_DOESN_T_FIT_INTO_A_INT);
		}
		int [] data = head.getData();
		if (data.length > 1) {
			throw new IllegalStateException(THIS_HUGE_INT_DOESN_T_FIT_INTO_A_INT);
		}
		if (data[0] == IntArrayLink.ALL_ONES) {
			throw new IllegalStateException(THIS_HUGE_INT_DOESN_T_FIT_INTO_A_INT);
		}
		return data[0];
	}

	@Override
	public long toLong() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BigInteger toBig() {
		// TODO Auto-generated method stub
		return null;
	}
    
    // Other I_HugeInt methods would go here...
}
