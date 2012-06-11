/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.area_method;

import java.util.Vector;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for construction of a foot point in an area method construction</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Damien Desfontaines
 */
public class AMFootPoint extends AMPoint {
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
	protected final AMPoint p,u,v;

	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param pointLabel	Label of point
	 * @param p				Previously constructed point
	 * @param u				Previously constructed point
	 * @param v 			Previously constructed point
	 * @return The point of label pointLabel, foot from the point p on the line (uv).
	 */
	public AMFootPoint(String label,AMPoint p, AMPoint u, AMPoint v) {
		type = 2;
		this.label = label;
		this.p = p;
		this.u = u;
		this.v = v;
		dependantPoints = new Vector<AMPoint>();
		dependantPoints.add(p);
		dependantPoints.add(u);
		dependantPoints.add(v);
	}
	
	/**
	 * Constructor method
	 * 
	 * @param p				Previously constructed point
	 * @param u				Previously constructed point
	 * @param v 			Previously constructed point
	 * @return The point of label automatically generated, foot from the point p on the line (uv).
	 */
	public AMFootPoint(AMPoint p, AMPoint u, AMPoint v) {
		type = 2;
		label = nextAvailableName();
		this.p = p;
		this.u = u;
		this.v = v;
		dependantPoints = new Vector<AMPoint>();
		dependantPoints.add(p);
		dependantPoints.add(u);
		dependantPoints.add(v);
	}
}
