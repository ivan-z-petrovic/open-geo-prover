/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.ndgcondition;

import java.util.Vector;

import com.ogprover.pp.tp.geoconstruction.Point;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for the "Those points must be distinct" NDG condition.</dd>
* </dl>
* 
* @version 1.00
* @author Damien Desfontaines
*/
public class DistinctPoints extends SimpleNDGCondition {
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
	 * The points which must not be equal
	 */
	private Point a,b;
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * @param a		The first point
	 * @param b 	The second point
	 */
	public DistinctPoints(Point a, Point b) {
		this.a = a;
		this.b = b;
		points = new Vector<Point>();
		points.add(a);
		points.add(b);
	}
	
	
	/*
	 * ======================================================================
	 * ======================= COMMON OBJECT METHODS ========================
	 * ======================================================================
	 */
	public String print() {
		return "Points " + a.getGeoObjectLabel() + " and " + b.getGeoObjectLabel() + " must not be equal.";
	}
}
