/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.thmstatement;

import java.util.Vector;

import com.ogprover.pp.tp.expressions.AMExpression;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for a theorem statement used by the area method</dd>
* </dl>
* 
* @version 1.00
* @author Damien Desfontaines
*/
public class AreaMethodTheoremStatement {
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
	
	/**
	 * An area method theorem statement is a conjunction of an equality between
	 * rational expressions in geometric quantities. All this equalities can be
	 * described with one expression each, by passing the right side to the left one.
	 * We can so represent a theorem statement by a vector of AMExpressions.
	 */
	protected Vector<AMExpression> statements;
	
	/**
	 * Name of the theorem
	 */
	protected String name;
	

	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @return the statement to verify. Each element of the returned vector must be equal to zero.
	 */
	public Vector<AMExpression> getStatements() {
		return statements;
	}
	
	/**
	 * @return the name of the theorem.
	 */
	public String getName() {
		return name;
	}
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method.
	 */
	public AreaMethodTheoremStatement(String name, Vector<AMExpression> statements) {
		this.name = name;
		this.statements = statements;
	}
}
