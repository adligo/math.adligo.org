package org.adligo.math.shared.huge;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.adligo.i_math.shared.huge.HugeConstants;

/**
 * This class just contains logic to split up a BigInteger into a HugeInt stream,
 * or reconstitute a BigInteger from bytes.
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
public class BigIntegerBuilder {
	
	/**
	 * creates a BigInteger from a byte array 
	 * @param bytes matches with the format of {@link BigInteger#toByteArray()}
	 * @return
	 */
	public static BigInteger from(byte []  bytes) {
		return new BigInteger(bytes);
	}
	
	public static Stream<BigInteger> split(BigInteger b) {
		String hexString = b.toString(16);
		List<BigInteger> rl = new ArrayList<>();
		while (hexString.length() / 2 >= HugeConstants.MAX_CHUNK_BYTES) {
			String littleHex = hexString.substring(hexString.length() - (HugeConstants.MAX_CHUNK_BYTES * 2), 
					hexString.length());
			BigInteger n = new BigInteger(littleHex, 16);
			rl.add(n);
			hexString = hexString.substring(0, hexString.length() - (HugeConstants.MAX_CHUNK_BYTES * 2));
		}
		if (hexString.length() >= 1) {
			BigInteger n = new BigInteger(hexString, 16);
			rl.add(n);
		}
		return rl.stream();
	}

	public static String toHexWithPad(BigInteger b) {
		return toHexWithPad(b, HugeConstants.MAX_CHUNK_BYTES * 2);
	}
	
	public static String toHexWithPad(BigInteger b, int size) {
		String hex = b.toString(16);
		if (hex.length() < size) {
			int diff = size - hex.length();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < diff; i++) {
				sb.append('0');
			}
			sb.append(hex);
			return sb.toString();
		}
		return hex;
	}
}
