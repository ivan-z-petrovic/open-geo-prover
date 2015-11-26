/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.thmprover;

import com.ogprover.polynomials.GeoTheorem;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Abstract class for algebraic based provers</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public abstract class AlgebraicMethodProver implements TheoremProver {
	/**
	 * theorem to be proved, set in polynomial representation
	 */
	protected GeoTheorem theorem;
	
	
	/**
	 * @return the theorem
	 */
	public GeoTheorem getTheorem() {
		return theorem;
	}
	/**
	 * @param theorem the theorem to set
	 */
	public void setTheorem(GeoTheorem theorem) {
		this.theorem = theorem;
	}
}