package org.adligo.math.shared.huge;

import java.math.BigInteger;

/**
 * This class contains constants that are small enough to not overflow users memory, but large enough to make points.
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
public class HugeConstants {
	/**
	 * This is double the number of bits in a long, or 16 bytes
	 */
	public static final BigInteger MAX_CHUNK = BigInteger.TWO.pow(128);
	
	public static void main(String [] args) {
		System.out.println(String.format("MAX_CHUNK is %,d",MAX_CHUNK));
	}
}
