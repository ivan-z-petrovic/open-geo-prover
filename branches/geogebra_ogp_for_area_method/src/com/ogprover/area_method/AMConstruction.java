/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.area_method;

import java.util.Vector;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Internal representation of a geometric construction used by the Area Method</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Damien Desfontaines
 */
public class AMConstruction {
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
	 * ========================== VARIABLES =================================
	 * ======================================================================
	 */
	/**
	 * Vector containing the points of the construction, in the right order. 
	 */
	protected Vector<AMPoint> points;
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @return the current number of points in the construction.
	 */
	public int numberOfPoints() {
		return points.size();
	}
	
	/**
	 * Adds a new point to the construction
	 * @param p		a point
	 */
	public void addPoint(AMPoint p) {
		for (int i=0 ; i<p.dependantPoints.size() ; i++) {
			if (!points.contains(p.dependantPoints.get(i))) {
				// TODO throw an exception
				return;
			}
		}
		points.add(p);
	}
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * @return An empty geometrical construction.
	 */
	public AMConstruction() {
		this.points = new Vector<AMPoint>();
	}
	
	/**
	 * @return A geometrical construction containing just one free point.
	 */
	public AMConstruction(AMPoint p) {
		this.points = new Vector<AMPoint>();
	}
	
	/*
	 * ======================================================================
	 * ======================= COMMON OBJECT METHODS ========================
	 * ======================================================================
	 */
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * @param p		a point
	 * @return 		a boolean : true iff p already exist in the construction
	 */
	public boolean exists(AMPoint p) {
		return this.points.contains(p);
	}
}