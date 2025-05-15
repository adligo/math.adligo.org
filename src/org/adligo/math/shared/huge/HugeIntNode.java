package org.adligo.math.shared.huge;

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

public class HugeIntNode {
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
    
    public HugeIntNode(int[] data) {
        this.data = verifyAndCloneData(data);
    }
    
    public HugeIntNode(BigInteger i) {
        this.data = verifyAndCloneData(from(i));
    }
    
    public BigInteger add(HugeIntNode other) {
    	BigInteger o = other.toBig();
    	BigInteger m = toBig();
    	return o.add(m);
    }
 
    /**
     * Note this is somewhat expensive as the data is cloned on each call!
     * @return
     */
    public int[] getData() {
    	return data.clone();
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
