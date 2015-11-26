/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.thmstatement;

import java.util.Vector;

import com.ogprover.polynomials.UFraction;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.polynomials.XTerm;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.expressions.AMExpression;
import com.ogprover.pp.tp.expressions.BasicNumber;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for statement that is always false</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class False extends ElementaryThmStatement {
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
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method.
	 * 
	 * @param cp 	Theorem protocol assigned to this statement
	 */
	public False(OGPTP cp) {
		this.geoObjects = cp.getConstructionSteps();
		this.consProtocol = cp;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#getAlgebraicForm()
	 */
	@Override
	public XPolynomial getAlgebraicForm() {
		// set as statement polynomial that has only 1 - it is always false (i.e. it's never equals to zero)
		XPolynomial statementPolynomial = new XPolynomial();
		statementPolynomial.addTerm(new XTerm(new UFraction(1)));
		return statementPolynomial;
	}

	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#getStatementDesc()
	 */
	@Override
	public String getStatementDesc() {
		return "False";
	}
	
	/**
	 * @see com.ogprover.pp.tp.thmstatement.ThmStatement#isValid()
	 */
	@Override
	public boolean isValid() {
		return this.consProtocol != null;
	}



	@Override
	public AreaMethodTheoremStatement getAreaMethodStatement() {
		// Same method as in getAlgebraicForm()
		Vector<AMExpression> statements = new Vector<AMExpression>();
		statements.add(new BasicNumber(1));
		return new AreaMethodTheoremStatement(getStatementDesc(), statements);
	}
}
