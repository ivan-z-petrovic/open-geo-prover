/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.ndgcondition;

import java.util.Vector;

import com.ogprover.pp.tp.expressions.AMExpression;
import com.ogprover.pp.tp.geoconstruction.Point;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for the "This expression must not be equal to zero" NDG condition.</dd>
* </dl>
* 
* @version 1.00
* @author Damien Desfontaines
*/
public class NonZeroExpression extends SimpleNDGCondition {
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
	 * The expression
	 */
	private AMExpression expr = null;
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * @param expr 	The expression which must be non-zero
	 */
	public NonZeroExpression(AMExpression expr) {
		this.expr = expr;
		points = new Vector<Point>(expr.getPoints());
	}
	
	
	/*
	 * ======================================================================
	 * ======================= COMMON OBJECT METHODS ========================
	 * ======================================================================
	 */
	public String print() {
		return "The expression \"" + expr.print() + "\" must be non-zero";
	}
}