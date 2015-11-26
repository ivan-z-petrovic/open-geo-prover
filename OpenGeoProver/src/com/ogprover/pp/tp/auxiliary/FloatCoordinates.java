/* 
 * DISCLAIMER PLACEHOLDER 
 */
package com.ogprover.pp.tp.auxiliary;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Abstract class for point</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class FloatCoordinates {
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
	 * Coordinates of the point
	 */
	public double x,y;
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * param x	X coordinate
	 * param y	Y coordinate
	 */
	public FloatCoordinates(double x, double y) {
		this.x = x;
		this.y = y;
	}
}
