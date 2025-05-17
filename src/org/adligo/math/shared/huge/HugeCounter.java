package org.adligo.math.shared.huge;


import java.math.BigInteger;

import org.adligo.i_math.shared.IntType;
import org.adligo.i_math.shared.huge.HugeConstants;
import org.adligo.i_math.shared.huge.I_HugeCounter;
import org.adligo.i_math.shared.huge.I_HugeInt;


/**
 * An implementation of I_HugeCounter that uses the delegation pattern to swap
 * between different counter implementations based on the current value's IntType.
 * This helps avoid stack overflow issues by using the most efficient counter type
 * for the current value.
 * <br/>
 * 
 * @author scott<br/>
 *         <br/>
 * 
 * <pre><code>
 * ---------------- Apache ICENSE-2.0 --------------------------
 *
 * Copyright 2022 Adligo Inc
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
public class HugeCounter implements I_HugeCounter {

    public static final String THE_CURRENT_VALUE_IS_TO_BIG_TO_FIT_INTO_A_BIG_INTEGER = "The current value is to big to fit into a BigInteger!";
	public static final String THE_CURRENT_VALUE_IS_TO_LARGE_TO_FIT_INTO_A_LONG = "The current value is to large to fit into a long!";
	public static final String THE_CURRENT_VALUE_IS_TO_BIG_TO_FIT_INTO_A_SHORT = "The current value is to big to fit into a short!";
	private static final String THE_CURRENT_VALUE_IS_TO_BIG_TO_FIT_INTO_A_BYTE = "The current value is to big to fit into a byte!";
	public static final String THE_CURRENT_VALUE_IS_TO_LARGE_TO_FIT_INTO_A_BIG_INTEGER = "The current value is to large to fit into a BigInteger!";
	public static final String HUGE_COUNTERS_MUST_BE_INITIALIZED_WITH_A_ZERO_OR_POSITIVE_NUMBERS = "HugeCounters MUST be initialized with a zero or positive numbers.";
	private I_HugeCounter delegate;
    
    /**
     * Creates a HugeCounter starting at zero
     */
    public HugeCounter() {
        this.delegate = new ByteCounter((byte) 0);
    }
    
    /**
     * Creates a HugeCounter with the specified initial byte value
     */
    public HugeCounter(byte initialValue) {
    	if (initialValue < 0) {
    		throw new IllegalArgumentException(HUGE_COUNTERS_MUST_BE_INITIALIZED_WITH_A_ZERO_OR_POSITIVE_NUMBERS);
    	}
        this.delegate = new ByteCounter(initialValue);
    }
    
    /**
     * Creates a HugeCounter with the specified initial short value
     */
    public HugeCounter(short initialValue) {
    	if (initialValue < 0) {
    		throw new IllegalArgumentException(HUGE_COUNTERS_MUST_BE_INITIALIZED_WITH_A_ZERO_OR_POSITIVE_NUMBERS);
    	}
    	if (initialValue <= Byte.MAX_VALUE - 4) {
            this.delegate = new ByteCounter((byte) initialValue);
        } else {
            this.delegate = new ShortCounter((short) initialValue);
        }
    }
    
    /**
     * Creates a HugeCounter with the specified initial int value
     */
    public HugeCounter(int initialValue) {
    	if (initialValue < 0) {
    		throw new IllegalArgumentException(HUGE_COUNTERS_MUST_BE_INITIALIZED_WITH_A_ZERO_OR_POSITIVE_NUMBERS);
    	}
        if (initialValue <= Byte.MAX_VALUE - 4) {
            this.delegate = new ByteCounter((byte) initialValue);
        } else if (initialValue <= Short.MAX_VALUE - 8 ) {
            this.delegate = new ShortCounter((short) initialValue);
        } else {
            this.delegate = new IntCounter(initialValue);
        }
    }
    
    /**
     * Creates a HugeCounter with the specified initial long value
     */
    public HugeCounter(long initialValue) {
    	if (initialValue < 0) {
    		throw new IllegalArgumentException(HUGE_COUNTERS_MUST_BE_INITIALIZED_WITH_A_ZERO_OR_POSITIVE_NUMBERS);
    	}
    	if (initialValue <= Byte.MAX_VALUE -4 ) {
            this.delegate = new ByteCounter((byte) initialValue);
        } else if (initialValue <= Short.MAX_VALUE - 8) {
            this.delegate = new ShortCounter((short) initialValue);
        } else if (initialValue <= Integer.MAX_VALUE - 16) {
            this.delegate = new IntCounter((int) initialValue);
        } else {
            this.delegate = new LongCounter(initialValue);
        }
    }
    
    /**
     * Creates a HugeCounter with the specified initial BigInteger value
     */
    public HugeCounter(BigInteger initialValue) {
    	if (initialValue.signum() < 0) {
    		throw new IllegalArgumentException(HUGE_COUNTERS_MUST_BE_INITIALIZED_WITH_A_ZERO_OR_POSITIVE_NUMBERS);
    	}
    	int bitLength = initialValue.bitLength();
    	if (bitLength <= 6) {
            this.delegate = new ByteCounter(initialValue.byteValue());
        } else if (bitLength <= 14) {
            this.delegate = new ShortCounter(initialValue.shortValue());
        } else if (bitLength <= 30) {
            this.delegate = new IntCounter(initialValue.intValue());
        } else {
        	BigInteger maxLong = BigInteger.valueOf(Long.MAX_VALUE).subtract(BigInteger.valueOf(100000));
        	BigInteger sub = initialValue.subtract(maxLong);
        	if (sub.signum() < 0) {
        		this.delegate = new LongCounter(initialValue.longValue());
        	} else {
        		 this.delegate = new BigCounter(initialValue);
        	}
        }
    }
    
    
    /**
     * Creates a HugeCounter with the specified initial value
     * @param initialValue the initial value for the counter
     */
    public HugeCounter(I_HugeInt initialValue) {
    	if (!initialValue.isPositive()) {
    		throw new IllegalArgumentException(HUGE_COUNTERS_MUST_BE_INITIALIZED_WITH_A_ZERO_OR_POSITIVE_NUMBERS);
    	}
        if (initialValue.isByte()) {
            this.delegate = new ByteCounter(initialValue.toByte());
        } else if (initialValue.isShort()) {
            this.delegate = new ShortCounter(initialValue.toShort());
        } else if (initialValue.isInt()) {
            this.delegate = new IntCounter(initialValue.toInt());
        } else if (initialValue.isLong()) {
            this.delegate = new LongCounter(initialValue.toLong());
        } else if (initialValue.isBig()) {
            this.delegate = new BigCounter(initialValue.toBig());
        } else {
            this.delegate = new HugeIntCounter(initialValue);
        }
    }
    
    @Override
    public void increment() {
    	switch (delegate.getType()) {
    		case IntType.Byte:
    			ByteCounter bc = (ByteCounter) delegate;
                if (bc.isFull()) {
                    delegate = new ShortCounter(bc.toByte());
                }
                break;
    		case IntType.Short:
    			ShortCounter sc = (ShortCounter) delegate;
                if (sc.isFull()) {
                    delegate = new IntCounter(sc.toShort());
                }
                break;
    		case IntType.Int:
    			IntCounter ic = (IntCounter) delegate;
                if (ic.isFull()) {
                    delegate = new LongCounter(ic.toInt());
                }
                break;
    		case IntType.Long:
    			LongCounter lc = (LongCounter) delegate;
                if (lc.isFull()) {
                    delegate = new BigCounter(BigInteger.valueOf(lc.toLong()));
                }
                break;
    		case IntType.Big:
    			BigCounter bigC = (BigCounter) delegate;
                if (bigC.isFull()) {
                    delegate = new HugeIntCounter(bigC.toHuge());
                }
                break;
            default:
            	//do nothing
             
    	}
        delegate.increment();
    }
    
    @Override
    public boolean isBig() {
        return delegate.isBig();
    }
    
    @Override
    public I_HugeInt toHuge() {
        return delegate.toHuge();
    }

	@Override
	public boolean isLong() {
		if (delegate.getType() == IntType.Long) {
			return true;
		}
		return false;
	}

	@Override
	public BigInteger toBig() {
		IntType type = delegate.getType();
		switch (type) {
			case IntType.Byte: 
				return BigInteger.valueOf(((ByteCounter) delegate).toByte());
			case IntType.Short: 
				return BigInteger.valueOf(((ShortCounter) delegate).toShort());
			case IntType.Int:
				return BigInteger.valueOf(((IntCounter) delegate).toInt());
			case IntType.Long:
				return BigInteger.valueOf(((LongCounter) delegate).toLong());
			case IntType.Big:
				return ((BigCounter) delegate).toBig();
			default: 
				throw new IllegalStateException(THE_CURRENT_VALUE_IS_TO_BIG_TO_FIT_INTO_A_BIG_INTEGER);
		}
	}

	@Override
	public boolean isInt() {
		IntType type = delegate.getType();
		switch (type) {
			case IntType.Byte: 
			case IntType.Short: 
			case IntType.Int: 
				return true;
			default: 
				return false;
		}
	}

	@Override
	public long toLong() {
		IntType type = delegate.getType();
		switch (type) {
			case IntType.Byte: 
				return ((ByteCounter) delegate).toByte();
			case IntType.Short: 
				return ((ShortCounter) delegate).toShort();
			case IntType.Int:
				return ((IntCounter) delegate).toInt();
			case IntType.Long:
				return ((LongCounter) delegate).toLong();
			default: 
				throw new IllegalStateException(THE_CURRENT_VALUE_IS_TO_LARGE_TO_FIT_INTO_A_LONG);
		}
	}

	@Override
	public boolean isShort() {
		IntType type = delegate.getType();
		switch (type) {
			case IntType.Byte: 
			case IntType.Short: return true;
			default: 
				return false;
		}
	}

	@Override
	public int toInt() {
		IntType type = delegate.getType();
		switch (type) {
			case IntType.Byte: 
				return ((ByteCounter) delegate).toByte();
			case IntType.Short: 
				return ((ShortCounter) delegate).toShort();
			case IntType.Int:
				return ((IntCounter) delegate).toInt();
			default: 
				throw new IllegalStateException(THE_CURRENT_VALUE_IS_TO_BIG_TO_FIT_INTO_A_SHORT);
		}
	}

	@Override
	public boolean isByte() {
		IntType type = delegate.getType();
		switch (type) {
			case IntType.Byte: return true;
			default: 
				return false;
		}
	}

	@Override
	public short toShort() {
		IntType type = delegate.getType();
		switch (type) {
			case IntType.Byte: 
				return ((ByteCounter) delegate).toByte();
			case IntType.Short: 
				return ((ShortCounter) delegate).toShort();
			default: 
				throw new IllegalStateException(THE_CURRENT_VALUE_IS_TO_BIG_TO_FIT_INTO_A_SHORT);
		}
	}

	@Override
	public IntType getType() {
		IntType type = delegate.getType();
		switch (type) {
			case IntType.Byte: return IntType.Byte;
			case IntType.Short: return IntType.Short;
			case IntType.Int: return IntType.Int;
			case IntType.Long: return IntType.Long;
			case IntType.Big: return IntType.Big;
			default: return IntType.Huge;
		}
	}

	@Override
	public byte toByte() {
		IntType type = delegate.getType();
		switch (type) {
			case IntType.Byte: return ((ByteCounter) delegate).toByte();
			default: 
				throw new IllegalStateException(THE_CURRENT_VALUE_IS_TO_BIG_TO_FIT_INTO_A_BYTE);
		}
	}
	
	protected I_HugeCounter getDelegate() {
		return this.delegate;
	}
    
}

abstract class AbstractCounter implements I_HugeCounter {

    public static final String THE_CURRENT_VALUE_DOES_NOT_FIT_INTO_A_BIG_INTEGER = "The current value does NOT fit into a BigInteger!";
	public static final String THE_CURRENT_VALUE_DOES_NOT_FIT_INTO_A_LONG = "The current value does NOT fit into a long!";
	public static final String THE_CURRENT_VALUE_DOES_NOT_FIT_INTO_AN_INT = "The current value does NOT fit into an int!";
	public static final String THE_CURRENT_VALUE_DOES_NOT_FIT_INTO_A_SHORT = "The current value does NOT fit into a short!";
	public static final String THE_CURRENT_VALUE_DOES_NOT_FIT_INTO_A_BYTE = "The current value does NOT fit into a byte!";

	@Override
    public boolean isBig() {
        return true;
    }

	@Override
	public boolean isLong() {
		return true;
	}

	@Override
	public BigInteger toBig() {
		throw new IllegalStateException(THE_CURRENT_VALUE_DOES_NOT_FIT_INTO_A_BIG_INTEGER);
	}

	@Override
	public boolean isInt() {
		return true;
	}

	@Override
	public long toLong() {
		throw new IllegalStateException(THE_CURRENT_VALUE_DOES_NOT_FIT_INTO_A_LONG);
	}

	@Override
	public boolean isShort() {
		return true;
	}

	@Override
	public int toInt() {
		throw new IllegalStateException(THE_CURRENT_VALUE_DOES_NOT_FIT_INTO_AN_INT);
	}

	@Override
	public boolean isByte() {
		return true;
	}

	@Override
	public short toShort() {
		throw new IllegalStateException(THE_CURRENT_VALUE_DOES_NOT_FIT_INTO_A_SHORT);
	}

	@Override
	public byte toByte() {
		throw new IllegalStateException(THE_CURRENT_VALUE_DOES_NOT_FIT_INTO_A_BYTE);
	}
}
/**
 * Counter implementation for byte values
 */
class ByteCounter extends AbstractCounter implements I_HugeCounter {
    private byte value;

    public ByteCounter() {
        this.value = 0;
    }
    
    public ByteCounter(byte initialValue) {
        this.value = initialValue;
    }
    
    @Override
    public void increment() {
        value++;
    }
    
    @Override
    public I_HugeInt toHuge() {
        return new HugeInt(value);
    }

	@Override
	public IntType getType() {
		return IntType.Byte;
	}
    
	@Override
	public byte toByte() {
		return value;
	}
	
	@Override
	public short toShort() {
		return value;
	}

	@Override
	public int toInt() {
		return value;
	}
	
	@Override
	public long toLong() {
		return value;
	}
	
	@Override
	public BigInteger toBig() {
		return BigInteger.valueOf(value);
	}
	
	public boolean isFull() {
		if (value >= Byte.MAX_VALUE - 4) {
			return true;
		}
		return false;
	}
}

/**
 * Counter implementation for short values
 */
class ShortCounter extends AbstractCounter implements I_HugeCounter {
    private short value;
    
    public ShortCounter(short initialValue) {
        this.value = initialValue;
    }
    
    @Override
    public void increment() {
        value++;
    }

    @Override
    public I_HugeInt toHuge() {
        return new HugeInt(value);
    }
    
    @Override
    public boolean isByte() {
    	return false;
    }
    
    @Override
    public boolean isShort() {
    	return true;
    }
    
    @Override
    public short toShort() {
    	return value;
    }

    @Override
    public int toInt() {
    	return value;
    }

    @Override
    public long toLong() {
    	return value;
    }

    @Override
    public BigInteger toBig() {
    	return BigInteger.valueOf(value);
    }
    
	@Override
	public IntType getType() {
		return IntType.Short;
	}
	
	public boolean isFull() {
		if (value >= Short.MAX_VALUE - 8) {
			return true;
		}
		return false;
	}
}

/**
 * Counter implementation for int values
 */
class IntCounter extends AbstractCounter implements I_HugeCounter {
    private int value;
    
    public IntCounter(int initialValue) {
        this.value = initialValue;
    }
    
    @Override
    public void increment() {
        value++;
    }
    
    @Override
    public boolean isByte() {
    	return false;
    }

    @Override
    public boolean isShort() {
    	return false;
    }
    
    @Override 
    public int toInt() {
    	return value;
    }

    @Override 
    public long toLong() {
    	return value;
    }

    @Override 
    public BigInteger toBig() {
    	return BigInteger.valueOf(value);
    }

    @Override 
    public I_HugeInt toHuge() {
    	return new HugeInt(value);
    }
    
	@Override
	public IntType getType() {
		return IntType.Int;
	}
	
	public boolean isFull() {
		if (value >= Integer.MAX_VALUE - 16) {
			return true;
		}
		return false;
	}
}

/**
 * Counter implementation for long values
 */
class LongCounter extends AbstractCounter implements I_HugeCounter {
    private long value;
    
    public LongCounter(long initialValue) {
        this.value = initialValue;
    }
    
    @Override
    public void increment() {
        value++;
    }
    
    @Override
    public boolean isByte() {
    	return false;
    }

    @Override
    public boolean isShort() {
    	return false;
    }
    
    @Override
    public boolean isInt() {
    	return false;
    }
    
    @Override
    public I_HugeInt toHuge() {
        return new HugeInt(value);
    }

	@Override
	public IntType getType() {
		return IntType.Long;
	}
	
	@Override
	public long toLong() {
		return value;
	}

	@Override
	public BigInteger toBig() {
		return BigInteger.valueOf(value);
	}
	
	public boolean isFull() {
		if (value >= Long.MAX_VALUE - 32) {
			return true;
		}
		return false;
	}
}

/**
 * Counter implementation for BigInteger values
 */
class BigCounter extends AbstractCounter implements I_HugeCounter {
    private java.math.BigInteger value;
    
    public BigCounter(java.math.BigInteger initialValue) {
        this.value = initialValue;
    }
    
    @Override
    public void increment() {
        value = value.add(java.math.BigInteger.ONE);
    }
    
    
    @Override
    public boolean isByte() {
    	return false;
    }

    @Override
    public boolean isShort() {
    	return false;
    }
    
    @Override
    public boolean isInt() {
    	return false;
    }

    @Override
    public boolean isLong() {
    	return false;
    }
    
    @Override
    public BigInteger toBig() {
        return value;
    }

    @Override
    public I_HugeInt toHuge() {
        return new HugeInt(value);
    }
    
	@Override
	public IntType getType() {
		return IntType.Big;
	}
	
	public boolean isFull() {
		if (value.compareTo(HugeConstants.MAX_CHUNK) >= 1) {
			return true;
		}
		return false;
	}
}

/**
 * Counter implementation for HugeInt values
 */
class HugeIntCounter extends AbstractCounter implements I_HugeCounter {
    private I_HugeInt value;
    
    public HugeIntCounter(I_HugeInt initialValue) {
        this.value = initialValue;
    }
    
    @Override
    public void increment() {
        value = value.add(HugeInt.ONE);
    }
    
    @Override
    public boolean isBig() {
        return value.isBig();
    }
    
    @Override
    public I_HugeInt toHuge() {
        return value;
    }

	@Override
	public IntType getType() {
		return IntType.Huge;
	}

}