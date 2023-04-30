package org.adligo.math.shared;

import java.math.BigInteger;

import org.adligo.i_math.shared.I_EgcdResult;
import org.adligo.i_math.shared.I_MathCtx;

/**
 * A implementation of @see I_MathCtx <br/>
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
public class MathCtx implements I_MathCtx {

  /**
   * @see I_MathCtx#egcd(BigInteger, BigInteger)
   * Also note I have used the wikipedia example for my variable names
   * {@link https://en.wikipedia.org/wiki/Extended_Euclidean_algorithm#Example}
   * Also note the regular ecd was removed from this class because it's in BigInteger itself,
   * and BigInteger correctly reversed the terms when backwards.
   */
  public I_EgcdResult egcd(BigInteger a, BigInteger b) {
    BigInteger lsi = BigInteger.ONE;
    BigInteger lti = BigInteger.ZERO;
    BigInteger si = BigInteger.ZERO;
    BigInteger ti = BigInteger.ONE;
    BigInteger lb = BigInteger.ONE;
    
    if (a.compareTo(b) <= -1) {
      b = a;
    }
    while (BigInteger.ZERO != b) {
      lb = b;
      //System.out.println("a is  " + a.toString(10) + " b is " + b.toString(10));
      BigInteger[] dnr = a.divideAndRemainder(b);
      a = b;
      b = dnr[1];
      BigInteger d = dnr[0];
      
      BigInteger t = si;
      si = lsi.subtract(d.multiply(si));
      lsi = t;
      
      t = ti;
      ti = lti.subtract(d.multiply(ti));
      lti = t;
      //System.out.println("quotent is  " + dnr[0].toString(10) + " remainder is " + dnr[1].toString(10));
      //System.out.println("si is  " + si.toString(10) + " ti is " + ti.toString(10));
    }
    return new EgcdResult(lsi, lti, lb);
  }

}
