/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoconstruction;

import com.ogprover.pp.tp.ndgcondition.AlgebraicNDGCondition;


/**
* <dl>
* <dt><b>Interface description:</b></dt>
* <dd>Interface for special constant angles 
*     (like 30 degrees, or 60, 45, 90, 18, 36, 72 and others that
*     can be constructed)</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public interface SpecialConstantAngle {
	/*
	 * ======================================================================
	 * ========================== VARIABLES =================================
	 * ======================================================================
	 */
	/**
	 * <i><b>
	 * Version number of interface in form xx.yy where
	 * xx is major version/release number and yy is minor
	 * release number.
	 * </b></i>
	 */
	public static final String VERSION_NUM = "1.00"; // this should match the version number from interface comment
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
     * Method that transforms this angle to algebraic form.
     * 
     * @return	Returns SUCCESS if successful or general error otherwise 
     */
    public int transformToAlgebraicForm();
    
    /**
     * Method for processing of polynomial for NDG condition.
     * 
     * @param ndgCond	Polynomial for of NDG condition.
     */
    public void processNDGCondition(AlgebraicNDGCondition ndgCond);
    
    /**
	 * Method that sets the parametric point used in algebraic expression for tangent of this angle.
	 * 
	 * @param point The parametric point to set
	 */
	public void setParametricPoint(Point point);

	/**
	 * Method that retrieves the parametric point used in algebraic expression for tangent of this angle.
	 * 
	 * @return the parametric point
	 */
	public Point getParametricPoint();
}
