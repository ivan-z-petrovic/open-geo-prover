/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.ndgcondition;

import java.util.Vector;

import com.ogprover.pp.tp.geoconstruction.Point;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for the "Those lines must not be parallel" NDG condition.</dd>
* </dl>
* 
* @version 1.00
* @author Damien Desfontaines
*/
public class NonParallelLines extends SimpleNDGCondition {
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
	 * The two lines, represented by two points each
	 */
	private Point a,b,c,d;
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * @param a		The first point of the first line
	 * @param b 	The second point of the first line 
	 * @param c		The first point of the second line
	 * @param d 	The second point of the second line 
	 */
	public NonParallelLines(Point a, Point b, Point c, Point d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		points = new Vector<Point>();
		points.add(a);
		points.add(b);
		points.add(c);
		points.add(d);
	}
	
	
	/*
	 * ======================================================================
	 * ======================= COMMON OBJECT METHODS ========================
	 * ======================================================================
	 */
	public String print() {
		return "Lines (" + a.getGeoObjectLabel() + b.getGeoObjectLabel() 
				+ ") and ("+  c.getGeoObjectLabel() + d.getGeoObjectLabel() + ") must not be parallel.";
	}
}
