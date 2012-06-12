/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.area_method;

import java.util.Vector;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for construction of a T-ratio point in an area method construction</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Damien Desfontaines
 */
public class AMTratioPoint extends AMPoint {
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
	protected AMPoint y,u,v;
	/**
	 * Ratio used to construct this point
	 */
	protected AMRatio r;

	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * Returns the point with a given label, constructed as TRATIO(y,u,v,r)
	 * See http://hal.inria.fr/hal-00426563/PDF/areaMethodRecapV2.pdf
	 * 
	 * @param label 	Label of point
	 * @param u			Previously constructed point
	 * @param v 		Previously constructed point
	 * @param r			Ratio
	 */
	public AMTratioPoint(String label,AMPoint u, AMPoint v, AMRatio r) {
		type = 2;
		this.label = label;
		this.u = u;
		this.v = v;
		dependantPoints = new Vector<AMPoint>();
		dependantPoints.add(u);
		dependantPoints.add(v);
	}
	
	/**
	 * Constructor method
	 * Returns the point with an automatically generated label, constructed as TRATIO(y,u,v,r)
	 * See http://hal.inria.fr/hal-00426563/PDF/areaMethodRecapV2.pdf
	 * 
	 * @param u		Previously constructed point
	 * @param v 	Previously constructed point
	 * @param r		Ratio
	 */
	public AMTratioPoint(AMPoint u, AMPoint v, AMRatio r) {
		type = 2;
		this.label = nextAvailableName();
		this.u = u;
		this.v = v;
		dependantPoints = new Vector<AMPoint>();
		dependantPoints.add(u);
		dependantPoints.add(v);
	}
}