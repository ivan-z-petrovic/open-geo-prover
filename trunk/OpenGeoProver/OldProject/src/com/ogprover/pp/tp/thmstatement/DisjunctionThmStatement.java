/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.thmstatement;

import com.ogprover.polynomials.XPolynomial;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Abstract class for disjunction of geometry theorem statements</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public abstract class DisjunctionThmStatement extends CompoundThmStatement {
	/*
	 * ======================================================================
	 * ========================== VARIABLES =================================
	 * ======================================================================
	 */
	/**
	 * <i><b>
	 * Version number of class in form xx.yy where
	 * xx is major version/release number and yy is minor
	 * release number.
	 * </b></i>
	 */
	public static final String VERSION_NUM = "1.00"; // this should match the version number from class comment
	
	

	/*
	 * ======================================================================
	 * ========================= ABSTRACT METHODS ===========================
	 * ======================================================================
	 */
	
	
	

	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	
	
	

	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#getAlgebraicForm()
	 */
	public XPolynomial getAlgebraicForm() {
		// For disjunction of theorem statements polynomial is  
		// product of polynomials for single statements
		XPolynomial statementPoly = null;
		
		for (ThmStatement singleStatement : this.particleThmStatements) {
			singleStatement.transformToAlgebraicForm(); // polynomial form of this statement is in CP
			XPolynomial singleStatementPoly = this.consProtocol.getAlgebraicGeoTheorem().getStatement();
			if (statementPoly == null)
				statementPoly = (XPolynomial) singleStatementPoly.clone();
			else
				statementPoly.multiplyByPolynomial(singleStatementPoly);
		}
		
		return statementPoly;
	}
	
}
