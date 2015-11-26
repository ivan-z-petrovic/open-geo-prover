/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoconstruction;

import java.util.Vector;

import com.ogprover.pp.tp.ndgcondition.AlgebraicNDGCondition;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Abstract for parametric set of points</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public abstract class ParametricSet extends GeoConstruction implements  SetOfPoints {
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
	 * List of all parametric points that represent the set.
     * Parametric points are not real points - they are not points from this
     * set of points. They can be considered as points from real line (set of
     * real numbers) that determine the equation of parametric set.
     * E.g. if equation of parametric set is A*x^2 + B*y^2 = 0, then A and B are
     * real parameters and they determine 2 parametric points which are in plane
     * with coordinates P1(A,0) and P2(B,0).
	 */
	protected Vector<Point> parametricPoints = null;
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method that sets the list of all parametric points
	 * 
	 * @param parametricPoints the parametricPoints to set
	 */
	public void setParametricPoints(Vector<Point> parametricPoints) {
		this.parametricPoints = parametricPoints;
	}
	
	/**
	 * Method that retrieves the list of all parametric points
	 * 
	 * @return the points
	 */
	public Vector<Point> getParametricPoints() {
		return this.parametricPoints;
	}
	
	

	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
     * Method that transforms parametric set of points to algebraic form
     * by instantiating parametric points.
     * 
     * @return	Returns SUCCESS if successful or general error otherwise 
     */
    public abstract int transformToAlgebraicForm();
    
    
    /**
     * Method that processes passed in NDG condition in polynomial form with respect
     * to this parametric set (examines degenerate cases of this parametric set).
     * 
     * @param ndgCond	NDG condition to process
     */
    public abstract void processNDGCondition(AlgebraicNDGCondition ndgCond);
	
}

