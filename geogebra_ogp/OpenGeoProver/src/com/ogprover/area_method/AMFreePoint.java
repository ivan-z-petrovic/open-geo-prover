/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.area_method;

import java.util.Vector;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for construction of a free point in an area method construction</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Damien Desfontaines
 */
public class AMFreePoint extends AMPoint {
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

	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * Returns a free point with a given label
	 * 
	 * @param label 	Label of point
	 */
	public AMFreePoint(String label) {
		type = 0;
		this.label = label;
		dependantPoints = new Vector<AMPoint>();
	}
	
	/**
	 * Constructor method
	 * Returns a free point with an automatically generated label
	 */
	public AMFreePoint() {
		type = 0;
		label = nextAvailableName();
		dependantPoints = new Vector<AMPoint>();
	}
}