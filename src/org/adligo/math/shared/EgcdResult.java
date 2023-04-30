package org.adligo.math.shared;


import java.math.BigInteger;

import org.adligo.i_math.shared.I_EgcdResult;

/**
 * A implementation of @see I_EgcdResult <br/>
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
public class EgcdResult implements I_EgcdResult {
  private final BigInteger bca;
  private final BigInteger bcb;
  private final BigInteger result;
  
  public EgcdResult(BigInteger bca, BigInteger bcb, BigInteger result) {
    this.bca = bca;
    this.bcb = bcb;
    this.result = result;
  }

  /**
   * @see I_EgcdResult#getBca()
   */
  public BigInteger getBca() {
    return bca;
  }

  /**
   * @see I_EgcdResult#getBcb()
   */
  public BigInteger getBcb() {
    return bcb;
  }

  /**
   * @see I_EgcdResult#getResult()
   */
  public BigInteger getResult() {
    return result;
  }

}
