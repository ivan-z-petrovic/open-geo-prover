/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.area_method;

import java.util.Vector;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Abstract class for a point in an area method construction</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Damien Desfontaines
 */
public abstract class AMPoint {
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
	 * Next available variable number, for automatically genereted points
	 */
	public static int nextAvailableNumber = 0;
	
	// ======= Points' constructions =======
	/**
	 * Free points
	 */
	public static final int AMPOINT_FREE_POINT = 1;
	/**
	 * Intersection between two lines
	 */
	public static final int AMPOINT_INTERSECTION_TWO_LINES = 2;
	/**
	 * Foot from a point to a line
	 */
	public static final int AMPOINT_FOOT = 3;
	/**
	 * Given a ratio r and three points U, Y and W, point V such as rUV = YW, and UV // YW
	 */
	public static final int AMPOINT_PRATIO = 4;
	/**
	 * Tratio - see http://hal.inria.fr/hal-00426563/PDF/areaMethodRecapV2.pdf
	 */
	public static final int AMPOINT_TRATIO = 5;
	
	/**
	 * Label/name of the point
	 */
	protected String label;
	/**
	 * Type of the point
	 */
	protected int type;
	/**
	 * Previous points used to construct this point
	 */
	protected Vector<AMPoint> dependantPoints;
	
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
	/**
	 * @return the type of the point
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * @return the label of the point
	 */
	public String getLabel() {
		return label;
	}
	
	/*
	 * ======================================================================
	 * ======================= COMMON OBJECT METHODS ========================
	 * ======================================================================
	 */
	public String toString() {
		return this.getLabel();
	}
	
	/**
	 * Equality between two points
	 * BEWARE : Returns false if two geometrically identic points were constructed differently
	 */
	public boolean equals(AMPoint p) {
		return this.getLabel() == p.getLabel();
	}
	
	public int hashCode() {
		return this.getLabel().hashCode();
	}
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Computes the next available name for an automatically generated construction point
	 */
	protected String nextAvailableName() {
		nextAvailableNumber++;
		return "v".concat(String.valueOf(nextAvailableNumber));
	}
}