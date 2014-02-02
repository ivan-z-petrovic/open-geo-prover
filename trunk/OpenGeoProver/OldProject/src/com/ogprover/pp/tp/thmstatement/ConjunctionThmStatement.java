/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.thmstatement;

import com.ogprover.polynomials.XPolynomial;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Abstract class for conjunction of geometry theorem statements</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public abstract class ConjunctionThmStatement extends CompoundThmStatement {
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
		// For conjunction of theorem statements polynomial is  
		// sum of squares of polynomials for single statements.
		// Poly1=0 and Poly2=0 <=> Poly1^2+Poly2^2 = 0.
		// Note: polynomials for single statements must express the exact
		// condition, not weaker condition. E.g. polynomial for segments
		// of equal lengths expresses the weaker condition since it is
		// obtained by squaring the main condition (equality of square roots).
		// Therefore, it is not correct to use it in conjunction statements.
		XPolynomial statementPoly = new XPolynomial();
		
		for (ThmStatement singleStatement : this.particleThmStatements) {
			singleStatement.transformToAlgebraicForm(); // polynomial form of this statement is in CP
			XPolynomial singleStatementPoly = this.consProtocol.getAlgebraicGeoTheorem().getStatement();
			statementPoly.addPolynomial(singleStatementPoly.multiplyByPolynomial(singleStatementPoly));
		}
		
		return statementPoly;
	}
	
}
