/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.area_method;

import java.util.Vector;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for construction of an intersection point in an area method construction</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Damien Desfontaines
 */
public class AMIntersectionPoint extends AMPoint {
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
	 * Points used to construct this point
	 */
	protected AMPoint u,v,p,q;

	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * Returns the point with a given label, intersection between the lines (uv) and (pq)
	 * 
	 * @param label 	Label of point
	 * @param u			Previously constructed point
	 * @param v 		Previously constructed point
	 * @param p			Previously constructed point
	 * @param q			Previously constructed point
	 */
	public AMIntersectionPoint(String label,AMPoint u, AMPoint v, AMPoint p, AMPoint q) {
		type = 2;
		this.label = label;
		this.u = u;
		this.v = v;
		this.p = p;
		this.q = q;
		dependantPoints = new Vector<AMPoint>();
		dependantPoints.add(u);
		dependantPoints.add(v);
		dependantPoints.add(p);
		dependantPoints.add(q);
	}
	
	/**
	 * Constructor method
	 * Returns the point with an automatically generated label, intersection between the lines (uv) and (pq)
	 * 
	 * @param u				Previously constructed point
	 * @param v 			Previously constructed point
	 * @param p				Previously constructed point
	 * @param q				Previously constructed point
	 */
	public AMIntersectionPoint(AMPoint u, AMPoint v, AMPoint p, AMPoint q) {
		type = 2;
		label = nextAvailableName();
		this.u = u;
		this.v = v;
		this.p = p;
		this.q = q;
		dependantPoints = new Vector<AMPoint>();
		dependantPoints.add(u);
		dependantPoints.add(v);
		dependantPoints.add(p);
		dependantPoints.add(q);
	}
}