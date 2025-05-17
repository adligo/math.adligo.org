package org.adligo.math.shared.huge;

import static org.adligo.i_math.shared.huge.HugeConstants.MAX_BYTE;
import static org.adligo.i_math.shared.huge.HugeConstants.MAX_CHUNK;
import static org.adligo.i_math.shared.huge.HugeConstants.MAX_INT;
import static org.adligo.i_math.shared.huge.HugeConstants.MAX_LONG;
import static org.adligo.i_math.shared.huge.HugeConstants.MAX_SHORT;
import static org.adligo.i_math.shared.huge.HugeConstants.MIN_BYTE;
import static org.adligo.i_math.shared.huge.HugeConstants.MIN_INT;
import static org.adligo.i_math.shared.huge.HugeConstants.MIN_LONG;
import static org.adligo.i_math.shared.huge.HugeConstants.MIN_SHORT;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.adligo.i_math.shared.IntType;
import org.adligo.i_math.shared.MathException;
import org.adligo.i_math.shared.huge.HugeConstants;
import org.adligo.i_math.shared.huge.I_HugeInt;
import org.adligo.i_math.shared.huge.I_HugeIntBuffer;

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
	public static final String A_NUMBER_IS_REQUIRED = "A number is required!";
	public static final String ADDING_OF_NEGATIVE_NUMBERS_IS_NOT_SUPPORTED_YET = "Adding of negative numbers is not supported yet!";
	public static final String THIS_HUGE_INT_DOESN_T_FIT_INTO_A_BIG_INTEGER = "This HugeInt doesn't fit into a BigInteger!";
	public static final String THIS_HUGE_INT_DOESN_T_FIT_INTO_A_LONG = "This HugeInt doesn't fit into a long!";
	public static final String THIS_HUGE_INT_DOESN_T_FIT_INTO_A_SHORT = "This HugeInt doesn't fit into a short!";
	public static final String THIS_HUGE_INT_DOESN_T_FIT_INTO_A_BYTE = "This huge int doesn't fit into a byte!";
	public static final String THIS_HUGE_INT_DOESN_T_FIT_INTO_A_INT = "This HugeInt doesn't fit into a int!";
	public static final HugeInt ZERO = new HugeInt((byte) 0);
	public static final HugeInt ONE = new HugeInt((byte) 1);
	public static final HugeInt TWO = new HugeInt((byte) 2);
	public static final HugeInt TEN = new HugeInt((byte) 2);
	
	private static AbstractDelegate getDelegate(BigInteger b) {
		int c = MAX_CHUNK.compareTo(b);
		if (c >= 0) {
			if (b.compareTo(MIN_BYTE) >= 0 && b.compareTo(MAX_BYTE) <= 0) {
				return new ByteDelegate(b.byteValueExact());
			} else if (b.compareTo(MIN_SHORT) >= 0 && b.compareTo(MAX_SHORT) <= 0) {
				return new ShortDelegate(b.shortValueExact());
			} else if (b.compareTo(MIN_INT) >= 0 && b.compareTo(MAX_INT) <= 0) {
				return new IntDelegate(b.intValueExact());
			} else if (b.compareTo(MIN_LONG) >= 0 && b.compareTo(MAX_LONG) <= 0) {
				return new LongDelegate(b.longValueExact());
			} else {
				return new BigIntegerDelegate(b);
			}
		} else {
			byte [] bytes = b.toByteArray();
			//TODO split into chunks bad programmer!
			return new BigIntegerDelegate(b);
			
		}
	}
	private final AbstractDelegate delegate;
	
	public HugeInt(byte b) {
		delegate = new ByteDelegate(b);
	}
	
	public HugeInt(short s) {
		if (s < Byte.MIN_VALUE || s > Byte.MAX_VALUE) {
			delegate = new ShortDelegate(s);
		} else {
			delegate = new ByteDelegate((byte) s);
		}
	}
	
	public HugeInt(int i) {
		if (i >= Byte.MIN_VALUE && i <= Byte.MAX_VALUE) {
			delegate = new ByteDelegate((byte) i);
		} else if (i >= Short.MIN_VALUE && i <= Short.MAX_VALUE) {
			delegate = new ShortDelegate((short) i);
		} else {
			delegate = new IntDelegate(i);
		}
	}
	
	public HugeInt(long l) {
		if (l >= Byte.MIN_VALUE && l <= Byte.MAX_VALUE) {
			delegate = new ByteDelegate((byte) l);
		} else if (l >= Short.MIN_VALUE && l <= Short.MAX_VALUE) {
			delegate = new ShortDelegate((short) l);
		} else if (l >= Integer.MIN_VALUE && l <= Integer.MAX_VALUE) {
			delegate = new IntDelegate((short) l);
		} else {
			delegate = new LongDelegate(l);
		}
	}

	public HugeInt(BigInteger b) {
		delegate = getDelegate(b);
	}
	
	public HugeInt(Stream<BigInteger> s) {
		Iterator<BigInteger> it = s.iterator();
		BigInteger first = it.next();
		if (first != null) {
			if (it.hasNext()) {
				BigInteger second = it.next();
				if (second != null) {
					//TODO 
					delegate = null;
					return;
				}
			} 
			delegate = getDelegate(first);
			return;
		} 
		throw new IllegalArgumentException(A_NUMBER_IS_REQUIRED);
	}
	
	public HugeInt(I_HugeIntBuffer b) {
		//TODO
		delegate = null;
	}
	
	@Override
	public boolean isBig() {
		IntType it = delegate.getType();
		switch (it) {
			case Byte:
			case Short:
			case Int:
			case Long:
				return true;
			case Big:
				if (delegate instanceof BigIntegerDelegate) {
					return true;
				} 
			default:
				return false;
		}
	}
	@Override
	public I_HugeInt toHuge() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isLong() {
		IntType it = delegate.getType();
		switch (it) {
			case Byte:
			case Short:
			case Int:
			case Long:
				return true;
			default:
				return false;
		}
	}
	
	@Override
	public BigInteger toBig() {
		IntType it = delegate.getType();
		switch (it) {
			case Byte:
				return BigInteger.valueOf(((ByteDelegate) delegate).toByte());
			case Short:
				return BigInteger.valueOf(((ShortDelegate) delegate).toShort());
			case Int:
				return BigInteger.valueOf(((IntDelegate) delegate).toInt());
			case Long:
				return BigInteger.valueOf(((LongDelegate) delegate).toLong());
			default:
				if (delegate instanceof BigIntegerDelegate) {
					return ((BigIntegerDelegate) delegate).toBigInteger();
				} else if (delegate instanceof RamBufferedDelegate) {
					throw new MathException("TODO");
				} else {
					throw new MathException("TODO");
				}
		}
	}
	@Override
	public boolean isInt() {
		IntType it = delegate.getType();
		switch (it) {
			case Byte:
			case Short:
			case Int:
				return true;
			default:
				return false;
		}
	}
	@Override
	public long toLong() {
		IntType it = delegate.getType();
		switch (it) {
			case Byte:
				return ((ByteDelegate) delegate).toByte();
			case Short:
				return ((ShortDelegate) delegate).toShort();
			case Int:
				return ((IntDelegate) delegate).toInt();
			case Long:
				return ((LongDelegate) delegate).toLong();
			default:
		}
		throw new IllegalStateException(THIS_HUGE_INT_DOESN_T_FIT_INTO_A_LONG);
	}
	
	@Override
	public boolean isShort() {
		IntType it = delegate.getType();
		switch (it) {
			case Byte:
			case Short:
				return true;
			default:
				return false;
		}
	}
	@Override
	public int toInt() {
		IntType it = delegate.getType();
		switch (it) {
			case Byte:
				return ((ByteDelegate) delegate).toByte();
			case Short:
				return ((ShortDelegate) delegate).toShort();
			case Int:
				return ((IntDelegate) delegate).toInt();
			default:
		}
		throw new IllegalStateException(THIS_HUGE_INT_DOESN_T_FIT_INTO_A_INT);
	}
	@Override
	public boolean isByte() {
		IntType it = delegate.getType();
		switch (it) {
			case Byte:
				return true;
			default:
				return false;
		}
	}
	@Override
	public short toShort() {
		IntType it = delegate.getType();
		switch (it) {
			case Byte:
				return ((ByteDelegate) delegate).toByte();
			case Short:
				return ((ShortDelegate) delegate).toShort();
			default:
		}
		throw new IllegalStateException(THIS_HUGE_INT_DOESN_T_FIT_INTO_A_SHORT);
	}
	
	@Override
	public IntType getType() {
		return delegate.getType();
	}
	@Override
	public byte toByte() {
		IntType it = delegate.getType();
		switch (it) {
			case Byte:
				return ((ByteDelegate) delegate).toByte();
			default:
		}
		throw new IllegalStateException(THIS_HUGE_INT_DOESN_T_FIT_INTO_A_BYTE);
	}
	@Override
	public I_HugeInt add(I_HugeInt other) {
		if (!this.isPositive() || !other.isPositive()) {
			throw new IllegalStateException(ADDING_OF_NEGATIVE_NUMBERS_IS_NOT_SUPPORTED_YET);
		}
		return new HugeInt(new AddStream(this.toStream(), other.toStream()).calc());
	}
	
	@Override
	public I_HugeInt add(I_HugeInt other, I_HugeIntBuffer buffer) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isInRam() {
		return delegate.isInRam();
	}
	@Override
	public boolean isPositive() {
		return !delegate.isNegative();
	}
	@Override
	public boolean isGreaterThan(long i) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Stream<BigInteger> toStream() {
		return toStream(true);
	}
	@Override
	public Stream<BigInteger> toStream(boolean littleToBig) {
		return delegate.toStream(littleToBig);
	}
}


abstract class AbstractDelegate {
	
	public boolean isInRam() {
		return true;
	}
	
	public IntType getType() {
		return IntType.Big;
	}
	
	public boolean isNegative() {
		return false;
	}
	public abstract Stream<BigInteger> toStream(boolean littleToBig);
}

class ByteDelegate extends AbstractDelegate {
	private byte b;
	
	public ByteDelegate(byte b) {
		this.b = b;
	}
	public Stream<BigInteger> toStream(boolean littleToBig) {
		Supplier<BigInteger> sup = new Supplier<BigInteger>() {
			BigInteger big = BigInteger.valueOf((long) Math.abs(b));
			@Override
			public BigInteger get() {
				BigInteger t = big;
				big = null;
				return t;
			}
			
		};
		return Stream.generate(sup);
	}
	
	public IntType getType() {
		return IntType.Byte;
	}
	public boolean isNegative() {
		if (b < 0) {
			return true;
		}
		return false;
	}
	public byte toByte() {
		return b;
	}
}

class ShortDelegate extends AbstractDelegate {
	private short s;
	
	public ShortDelegate(short s) {
		this.s = s;
	}
	public Stream<BigInteger> toStream(boolean littleToBig) {
		Supplier<BigInteger> sup = new Supplier<BigInteger>() {
			BigInteger big = BigInteger.valueOf((long) Math.abs(s));
			@Override
			public BigInteger get() {
				BigInteger t = big;
				big = null;
				return t;
			}
			
		};
		return Stream.generate(sup);
	}
	public IntType getType() {
		return IntType.Short;
	}
	public boolean isNegative() {
		if (s < 0) {
			return true;
		}
		return false;
	}
	public short toShort() {
		return s;
	}
}

class IntDelegate extends AbstractDelegate {
	private int i;
	
	public IntDelegate(int i) {
		this.i = i;
	}
	public Stream<BigInteger> toStream(boolean littleToBig) {
		Supplier<BigInteger> s = new Supplier<BigInteger>() {
			BigInteger big = BigInteger.valueOf(Math.abs(i));
			@Override
			public BigInteger get() {
				BigInteger t = big;
				big = null;
				return t;
			}
			
		};
		return Stream.generate(s);
	}
	public IntType getType() {
		return IntType.Int;
	}
	public boolean isNegative() {
		if (i < 0) {
			return true;
		}
		return false;
	}
	public int toInt() {
		return i;
	}
}


class LongDelegate extends AbstractDelegate {
	private long l;
	
	public LongDelegate(long l) {
		this.l = l;
	}
	public Stream<BigInteger> toStream(boolean littleToBig) {
		Supplier<BigInteger> s = new Supplier<BigInteger>() {
			BigInteger big = BigInteger.valueOf(Math.abs(l));
			@Override
			public BigInteger get() {
				BigInteger t = big;
				big = null;
				return t;
			}
			
		};
		return Stream.generate(s);
	}
	public IntType getType() {
		return IntType.Long;
	}
	public boolean isNegative() {
		if (l < 0) {
			return true;
		}
		return false;
	}
	public long toLong() {
		return l;
	}
}

class BigIntegerDelegate extends AbstractDelegate {
	private BigInteger b;
	
	public BigIntegerDelegate(BigInteger b) {
		this.b = b;
	}
	public Stream<BigInteger> toStream(boolean littleToBig) {
		Supplier<BigInteger> s = new Supplier<BigInteger>() {
			BigInteger big = b.abs();
			@Override
			public BigInteger get() {
				BigInteger t = big;
				big = null;
				return t;
			}
			
		};
		return Stream.generate(s);
	}
	public boolean isNegative() {
		if (b.compareTo(BigInteger.ZERO) <= 0) {
			return true;
		}
		return false;
	}
	public BigInteger toBigInteger() {
		return b;
	}
}

class RamBufferedDelegate extends AbstractDelegate {

	@Override
	public Stream<BigInteger> toStream(boolean littleToBig) {
		// TODO Auto-generated method stub
		return null;
	}


}

class ExternalBufferedDelegate extends AbstractDelegate {

	@Override
	public boolean isInRam() {
		return false;
	}

	@Override
	public Stream<BigInteger> toStream(boolean littleToBig) {
		// TODO Auto-generated method stub
		return null;
	}
}

class AddStream {
	Iterator<BigInteger> l;
	Iterator<BigInteger> r;
	
	public AddStream(Stream<BigInteger> l, Stream<BigInteger> r) {
		this.l = l.iterator();
		this.r = r.iterator();
	}
	
	public Stream<BigInteger> calc() {
		Supplier<BigInteger> sup = new Supplier<BigInteger>() {
			BigInteger nl = null;
			BigInteger nr = null;
			BigInteger carry = null;
			
			@Override
			public BigInteger get() {
				//This logic will get complex
				if (l.hasNext()) {
					nl = l.next();
				}
				if (r.hasNext()) {
					nr = r.next();
				}
				if (nl != null && nr != null) {
					if (carry != null) {
						BigInteger ret = nl.add(nr).add(carry);
						if (ret.compareTo(HugeConstants.MAX_CHUNK) >= 1) {
							throw new MathException("TODO overflow");
						}
						return ret;
					} else {
						BigInteger ret = nl.add(nr);
						if (ret.compareTo(HugeConstants.MAX_CHUNK) >= 1) {
							throw new MathException("TODO overflow");
						}
						return ret;
					}
				}
				return null;
			}
		};
		return Stream.generate(sup);

	}
	
}