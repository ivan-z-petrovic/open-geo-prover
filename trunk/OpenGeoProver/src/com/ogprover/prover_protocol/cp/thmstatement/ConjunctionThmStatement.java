/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.prover_protocol.cp.thmstatement;

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
	 * @see com.ogprover.prover_protocol.cp.thmstatement.ThmStatement#getAlgebraicForm()
	 */
	public XPolynomial getAlgebraicForm() {
		// For conjunction of theorem statements polynomial is  
		// sum of squares of polynomials for single statements
		XPolynomial statementPoly = new XPolynomial();
		
		for (ThmStatement singleStatement : this.particleThmStatements) {
			singleStatement.transformToAlgebraicForm(); // polynomial form of this statement is in CP
			XPolynomial singleStatementPoly = this.consProtocol.getAlgebraicGeoTheorem().getStatement();
			statementPoly.addPolynomial(singleStatementPoly.multiplyByPolynomial(singleStatementPoly));
		}
		
		return statementPoly;
	}
	
}
