package org.adligo.math.shared.huge;

import java.math.BigInteger;
import java.nio.IntBuffer;
import java.util.PrimitiveIterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.stream.IntStream;

import org.adligo.collections.linked.DoublyLinkedList;
import org.adligo.collections.linked.DoublyLinkedListMutant;
import org.adligo.i_collections.shared.linked.I_DoublyLinkedNode;
import org.adligo.i_math.shared.IntType;
import org.adligo.i_math.shared.huge.I_HugeInt;

/**
 * Immutable implementation of I_HugeInt using a linked list of IntArrayLink nodes.
 * Each node contains an array of integers, and the class tracks the number of links.
 * Also this class should be considered experimental, and likely has bugs!
 *
 * 
 * @author scott<br/>
 *         <br/>
 * 
 * <pre><code>
 * ---------------- Apache ICENSE-2.0 --------------------------
 *
 * Copyright 2025 Adligo Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </code><pre>
 */
public class HugeInt implements I_HugeInt {
	public static final String ADDING_OF_NEGATIVE_NUMBERS_IS_NOT_SUPPORTED_YET = "Adding of negative numbers is not supported yet!";
	public static final String THIS_HUGE_INT_DOESN_T_FIT_INTO_A_BIG_INTEGER = "This HugeInt doesn't fit into a BigInteger!";
	public static final String THIS_HUGE_INT_DOESN_T_FIT_INTO_A_LONG = "This HugeInt doesn't fit into a long!";
	public static final String THIS_HUGE_INT_DOESN_T_FIT_INTO_A_SHORT = "This HugeInt doesn't fit into a short!";
	public static final String THIS_HUGE_INT_DOESN_T_FIT_INTO_A_BYTE = "This huge int doesn't fit into a byte!";
	public static final String THIS_HUGE_INT_DOESN_T_FIT_INTO_A_INT = "This HugeInt doesn't fit into a int!";
	public static final HugeInt ZERO = new HugeInt(0);
	public static final HugeInt ONE = new HugeInt(1);
	public static final HugeInt TWO = new HugeInt(2);
    final DoublyLinkedList<HugeIntNode> list;
    final boolean negative;
    final I_HugeInt linkCount;

    public HugeInt(byte value) {
    	throw new RuntimeException("TODO Implement me");
    }
    
    public HugeInt(short value) {
    	throw new RuntimeException("TODO Implement me");
    }
    /**
     * Constructs a HugeInt from a single integer.
     */
    public HugeInt(int value) {
    	HugeIntNode head = new HugeIntNode(new int[]{Math.abs(value)});
    	DoublyLinkedListMutant<HugeIntNode> tlist = new DoublyLinkedListMutant<HugeIntNode>();
    	tlist.add(head);
        this.list = new DoublyLinkedList<HugeIntNode>(tlist);
        if (value < 0) {
        	this.negative = true;
        } else {
        	this.negative = false;
        }
        this.linkCount = ONE;
    }
    
    public HugeInt(long value) {
    	throw new RuntimeException("TODO Implement me");
    }
    
    public HugeInt(BigInteger value) {
    	throw new RuntimeException("TODO Implement me");
    }
    
    /**
     * Constructs a HugeInt from an array of integers.
     * Each array element becomes a node in the linked list.
     * @param negative if the number is negative
     * @param input in little to big ending order all integers are interpreted for their byte values, so the negative sign
     *   as a bit is used for positive or negative bits depending on the negative parameter.
     */
    public HugeInt(boolean negative, IntStream input) {
    	this.negative = negative;
        IntBuffer buf = IntBuffer.allocate(8);
        PrimitiveIterator.OfInt iterator = input.iterator();
        DoublyLinkedListMutant<HugeIntNode> tlist = new DoublyLinkedListMutant<HugeIntNode>();
        
    	
        int counter = 0;
        //I_HugeInt linkCountLocal = null;
        int linkCountLocal = 0;
        boolean first = true;
        while (iterator.hasNext()) {

        	int next = iterator.nextInt();
        	if (counter >= 8) {
        		int [] ints = new int [8];
        		for (int i = 0; i < 8; i++) {
					int n = buf.get(i);
					ints[7-i] = n;
				}
				HugeIntNode nn = new HugeIntNode(ints);
				tlist.add(nn);
				//TODO
				//linkCountLocal = linkCountLocal.add(HugeInt.ONE);
				linkCountLocal++;
        		buf = IntBuffer.allocate(8);
        	}
        	buf.put(next);
        	if (first) {
        		//TODO
        		//linkCountLocal = new HugeInt(1);
        		linkCountLocal++;
        		first = false;
        	}
        	counter++;
        }
        int remaining = buf.position();
        if (remaining != 0) {
	        int [] ints = new int [remaining];
			for (int i = 0; i < remaining; i++) {
				int n = buf.get(i);
				ints[remaining-1-i] = n;
				//linkCountLocal = linkCountLocal.add(HugeInt.ONE);
				//TODO
	    		linkCountLocal++;
			}
	
			tlist.add(new HugeIntNode(ints));
        }
		this.list = new DoublyLinkedList<HugeIntNode>(tlist);
		this.linkCount = new HugeInt(linkCountLocal);
    }
    
    /**
     * Returns the number of IntArrayLink nodes in this HugeInt.
     */
    public I_HugeInt getLinkCount() {
        return linkCount;
    }
    


	@Override
	public String toString() {
		return "HugeInt [list=" + list + ", negative=" + negative + ", linkCount=" + linkCount
				+ "]";
	}

	@Override
	public I_HugeInt add(I_HugeInt other) {
		if (this.negative || !other.isPositive()) {
			throw new IllegalStateException(ADDING_OF_NEGATIVE_NUMBERS_IS_NOT_SUPPORTED_YET);
		}
		AddStream as = new AddStream(toStream().iterator(), other.toStream().iterator());
		return new HugeInt(false, IntStream.generate(as).takeWhile(as.isNotDone()));
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
		I_DoublyLinkedNode<HugeIntNode> head = list.getHead();
		if (head.hasNext()) {
			return false;
		}
		HugeIntNode hin = head.getValue();
		int [] data = hin.getData();
		if (data.length > 1) {
			return false;
		}
		if (data[0] == HugeIntNode.ALL_ONES) {
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
		return !negative;
	}

	@Override
	public byte toByte() {
		I_DoublyLinkedNode<HugeIntNode> head = list.getHead();
		if (head.hasNext()) {
			throw new IllegalStateException(THIS_HUGE_INT_DOESN_T_FIT_INTO_A_BYTE);
		}
		HugeIntNode hin = head.getValue();
		int [] data = hin.getData();
		if (data.length > 1) {
			throw new IllegalStateException(THIS_HUGE_INT_DOESN_T_FIT_INTO_A_BYTE);
		}
		int val = data[0];
		if (val == HugeIntNode.ALL_ONES) {
			throw new IllegalStateException(THIS_HUGE_INT_DOESN_T_FIT_INTO_A_BYTE);
		}
		if (val > Byte.MAX_VALUE || val < Byte.MIN_VALUE) {
			throw new IllegalStateException(THIS_HUGE_INT_DOESN_T_FIT_INTO_A_BYTE);
		}
		return (byte) val;
	}

	@Override
	public short toShort() {
		I_DoublyLinkedNode<HugeIntNode> head = list.getHead();
		if (head.hasNext()) {
			throw new IllegalStateException(THIS_HUGE_INT_DOESN_T_FIT_INTO_A_SHORT);
		}
		HugeIntNode hin = head.getValue();
		int [] data = hin.getData();
		if (data.length > 1) {
			throw new IllegalStateException(THIS_HUGE_INT_DOESN_T_FIT_INTO_A_SHORT);
		}
		int val = data[0];
		if (val == HugeIntNode.ALL_ONES) {
			throw new IllegalStateException(THIS_HUGE_INT_DOESN_T_FIT_INTO_A_SHORT);
		}
		if (val > Short.MAX_VALUE || val < Short.MIN_VALUE) {
			throw new IllegalStateException(THIS_HUGE_INT_DOESN_T_FIT_INTO_A_SHORT);
		}
		return (byte) val;
	}

	@Override
	public int toInt() {
		I_DoublyLinkedNode<HugeIntNode> head = list.getHead();
		if (head.hasNext()) {
			throw new IllegalStateException(THIS_HUGE_INT_DOESN_T_FIT_INTO_A_INT);
		}
		HugeIntNode hin = head.getValue();
		int [] data = hin.getData();
		if (data.length > 1) {
			throw new IllegalStateException(THIS_HUGE_INT_DOESN_T_FIT_INTO_A_INT);
		}
		if (data[0] == HugeIntNode.ALL_ONES) {
			throw new IllegalStateException(THIS_HUGE_INT_DOESN_T_FIT_INTO_A_INT);
		}
		return data[0];
	}

	@Override
	public long toLong() {
		I_DoublyLinkedNode<HugeIntNode> head = list.getHead();
		if (head.hasNext()) {
			throw new IllegalStateException(THIS_HUGE_INT_DOESN_T_FIT_INTO_A_LONG);
		}
		HugeIntNode hin = head.getValue();
		int [] data = hin.getData();
		if (data.length > 2) {
			throw new IllegalStateException(THIS_HUGE_INT_DOESN_T_FIT_INTO_A_LONG);
		}
		if (data.length == 1) {
			return (long) data[0];
		}
		int iz = data[0];
		int i1 = data[1];
		long r = i1;
		r = r << 32;
		r = r | iz;
		return r;
	}

	@Override
	public BigInteger toBig() {
		I_DoublyLinkedNode<HugeIntNode> head = list.getHead();
		if (linkCount.isGreaterThan((long) Integer.MAX_VALUE)) {
			throw new IllegalStateException(THIS_HUGE_INT_DOESN_T_FIT_INTO_A_BIG_INTEGER);
		}
		int mi = Integer.MAX_VALUE;
		if (this.linkCount.isInt()) {
			long lci = (linkCount.toInt() - 1) * 8;
			I_DoublyLinkedNode<HugeIntNode> tail = list.getTail();
			HugeIntNode hin = tail.getValue();
			int [] data = hin.getData();
			lci = lci + data.length;
			if (lci > Integer.MAX_VALUE) {
				throw new IllegalStateException(THIS_HUGE_INT_DOESN_T_FIT_INTO_A_BIG_INTEGER);
			}
		}
		if (head.hasNext()) {
			throw new IllegalStateException(THIS_HUGE_INT_DOESN_T_FIT_INTO_A_BIG_INTEGER);
		}
		HugeIntNode hin = head.getValue();
		int [] data = hin.getData();
		if (data.length > 2) {
			throw new IllegalStateException(THIS_HUGE_INT_DOESN_T_FIT_INTO_A_LONG);
		}
		if (data.length == 1) {
			return BigInteger.valueOf((long) data[0]);
		}
		int iz = data[0];
		int i1 = data[1];
		long r = i1;
		r = r << 32;
		r = r | iz;
		return BigInteger.ZERO;
	}
    
	public boolean isGreaterThan(long i) {
		if (this.isLong()) {
			long m = toLong();
			if (m > i) {
				return true;
			}
		}
		return false;
	}
    // Other I_HugeInt methods would go here...

	@Override
	public IntStream toStream() {
		return toStream(true);
	}

	@Override
	public IntStream toStream(boolean littleToBig) {
		if (littleToBig) {
			LittleToBigIntStream ltbis = new LittleToBigIntStream(this);
			return IntStream.generate(ltbis).takeWhile(ltbis.isNotDone());
		} else {
			//Stream
			return null;
		}
	}

	@Override
	public I_HugeInt toHuge() {
		return this;
	}

	@Override
	public IntType getType() {
		// TODO Auto-generated method stub
		return null;
	}
}
 
class LittleToBigIntStream implements IntSupplier {
	I_DoublyLinkedNode<HugeIntNode> current;
	int [] data;
	int idx = 0;
	AtomicBoolean done = new AtomicBoolean(false);
	AtomicBoolean done2 = new AtomicBoolean(false);
	IntPredicate predicate = new IntPredicate() {
		
		@Override
		public boolean test(int value) {
			if (done2.get()) {
				return false;
			}
			if (done.get()) {
				done2.set(true);
			}
			return true;
		}
	};
	
	public LittleToBigIntStream(HugeInt hi) {
		current = hi.list.getHead();
		data = current.getValue().getData();
	}
	
	@Override
	public int getAsInt() {
		if (idx > data.length - 1) {
			done.set(true);
			return 0;
		}
		int r = data[idx];
		idx++;
		if (idx >= data.length) {
			if (current.hasNext()) {
				current = current.getNext();
				data = current.getValue().getData();
				idx = 0;
			} else {
				done.set(true);
			}
		} 
		return r;
	}
	
	public IntPredicate isNotDone() {
		return predicate;
	}
}

class BigToLittleIntStream implements IntSupplier {
	
	@Override
	public int getAsInt() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public boolean isDone() {
		return false;
	}
}



class AddStream implements IntSupplier {
	long carry = 0;
	AtomicInteger last = new AtomicInteger();
	AtomicBoolean done = new AtomicBoolean(false);
	AtomicBoolean done2 = new AtomicBoolean(false);
	final PrimitiveIterator.OfInt meIt;
	final PrimitiveIterator.OfInt oIt;
	final IntPredicate predicate = new IntPredicate() {

		@Override
		public boolean test(int value) {
			if (done2.get()) {
				return false;
			}
			if (done.get()) {
				done2.set(true);
			}
			return true;
		}
		
	};
	
	public AddStream(PrimitiveIterator.OfInt meIt, PrimitiveIterator.OfInt oIt) {
		this.meIt = meIt;
		this.oIt = oIt;
	}
	
	@Override
	public int getAsInt() {
		long m = 0;
		long o = 0;
		if (meIt.hasNext()) {
			m = meIt.nextInt();
		}
		if (oIt.hasNext()) {
			o = oIt.nextInt();
		}
		long r = m + o + carry;
		if (r >= Integer.MAX_VALUE) {
			carry = r - Integer.MAX_VALUE;
		}
		if (!meIt.hasNext() && !oIt.hasNext() && carry == 0) {
			done.set(true);
		}
		last.set((int) r);
		return (int) r;
	}
	
	public IntPredicate isNotDone() {
		return predicate;
	}
}
