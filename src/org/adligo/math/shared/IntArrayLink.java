package org.adligo.math.shared;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * A immutable implementation of the links in the linked list in <{@link HugeInt}>.
 * Note: that both the array of ints and the ints themselves are LittleEnding binary (big numbers at the left [idx 0] and 
 * small numbers at the end [idx 7]). <br/>
 * <br/>
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

public class IntArrayLink {
	public static final String INT_ARRAY_LINK_DATA_ARRAYS_MUST_BE_1_8_IN_LENGTH = "IntArrayLink data arrays must be 1-8 in length!";
	public static final BigInteger ALL_ONES_INT = new BigInteger("4294967295");
	public static final String EIGHT_FS = "ffffffff";
	public static final int ALL_ONES = ~0;

	public static int[] from(BigInteger i) {
		//little ending hex
		String hexString = i.toString(16);
		//System.out.println("hexString is " + hexString + ", len " + hexString.length());
		int counter = 0;
		int [] l = new int [8];
		int hexCounter = hexString.length();
		while (counter < 8 && hexCounter >= 1) {
			int left = 0;
			if (hexCounter - 8 > 0) {
				left = hexCounter - 8;
			}
			String sub = hexString.substring(left, hexCounter);
			int ni = 0;
			if (EIGHT_FS.equals(sub)) {
				ni = ALL_ONES;
			} else {
				ni = Integer.parseInt(sub, 16);
			}
			l[counter] = ni;
			//System.out.println("sub is " + sub + " ni is " + ni + ", left, right = " + left +"," + hexCounter);
			counter++;
			if (hexCounter - 8 > 0) {
				hexCounter = hexCounter - 8;
			} else {
				hexCounter--;
			}
			
		}
		return l;
	}
	
	public static byte[] intToByteArray(int number) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(number);
        byte[] r = buffer.array();
        return r;
    }
	
	private static int[] verifyAndCloneData(int[] data) {
		int len = data.length;
		if (len > 8 || len < 1) {
			throw new IllegalArgumentException(INT_ARRAY_LINK_DATA_ARRAYS_MUST_BE_1_8_IN_LENGTH);
		}
		return data.clone();
	}
	private final int[] data;
    private final IntArrayLink next;
    private final IntArrayLink previous;
    
    public IntArrayLink(int[] data) {
        this.data = verifyAndCloneData(data);
        this.next = null;
        this.previous = null;
    }
    
    public IntArrayLink(BigInteger i) {
        this.data = verifyAndCloneData(from(i));
        this.next = null;
        this.previous = null;
    }
    
    public IntArrayLink(int[] data, IntArrayLink previous) {
        this.data = verifyAndCloneData(data);
        this.previous = previous;
        this.next = null;
    }
    
    public IntArrayLink(BigInteger i, IntArrayLink previous) {
        this.data = verifyAndCloneData(from(i));
        this.next = null;
        this.previous = previous;
    }

    public IntArrayLink(IntArrayLink head, IntArrayLink next) {
        this.data = verifyAndCloneData(head.data);
        this.next = next;
        this.previous = null;
    }
    
    public IntArrayLink(int[] data, IntArrayLink previous, IntArrayLink next) {
        this.data = verifyAndCloneData(data);
        this.next = next;
        this.previous = previous;
    }
    
    public IntArrayLink(IntArrayLink data, IntArrayLink previous, IntArrayLink next) {
        this.data = verifyAndCloneData(data.data);
        this.next = next;
        this.previous = previous;
    }
    
    public IntArrayLink(BigInteger i, IntArrayLink previous, IntArrayLink next) {
        this.data = verifyAndCloneData(from(i));
        this.next = next;
        this.previous = previous;
    }
    
    public BigInteger add(IntArrayLink other) {
    	BigInteger o = other.toBig();
    	BigInteger m = toBig();
    	return o.add(m);
    }
    
    public boolean hasNext() {
    	if (next == null) {
    		return false;
    	}
    	return true;
    }
    
    public IntArrayLink getNext() {
    	return next;
    }
    
    public IntArrayLink getPrevious() {
    	return previous;
    }
    
    public int[] getData() {
    	return data.clone();
    }
    
    public boolean hasPrevious() {
    	if (previous == null) {
    		return false;
    	}
    	return true;
    }
    
    public BigInteger toBig() {
    	BigInteger r = BigInteger.ZERO;
    	for (int i = data.length - 1; i >= 0; i--) {
			int ni = data[i];
			if (i !=  data.length - 1) {
				r = r.shiftLeft(32);
			}
			//System.out.println("i is " + i + " ni is " + Integer.toHexString(ni) + " , " + r.bitLength());
			String nr = Integer.toHexString(ni);
			BigInteger nbi = ALL_ONES_INT;
			if (ALL_ONES != ni) {
				nbi = BigInteger.valueOf(ni);
			}
			r = r.or(nbi);
			//System.out.println("nbi is " + nbi.longValue() + " , r is " + r.bitLength());
		}
    	return r;
    }
}
