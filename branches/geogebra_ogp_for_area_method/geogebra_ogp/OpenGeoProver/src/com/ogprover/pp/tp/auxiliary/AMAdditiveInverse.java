/* 
 * DISCLAIMER PLACEHOLDER 
 */
package com.ogprover.pp.tp.auxiliary;

import java.util.HashSet;

import com.ogprover.pp.tp.geoconstruction.Point;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for representing the opposite inverse of an expression.</dd>
 * </dl>
 */
public class AMAdditiveInverse extends AMExpression {
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
	 * The expression to be inversed.
	 */
	protected AMExpression expr;

	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	public AMExpression getExpr() {
		return expr;
	}

	/**
	 * @see com.ogprover.pp.tp.auxiliary.AMExpression#getPoints()
	 */
	public HashSet<Point> getPoints() {
		return expr.getPoints();
	}
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param expr  	Expression
	 */
	public AMAdditiveInverse(AMExpression expr) {
		this.expr = expr;
	}
}