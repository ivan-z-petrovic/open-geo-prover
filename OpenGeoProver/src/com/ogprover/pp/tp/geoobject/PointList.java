/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoobject;

import java.util.Vector;

import com.ogprover.pp.tp.geoconstruction.Point;

/**
 * <dl>
 * <dt><b>Interface description:</b></dt>
 * <dd>Interface for all sets of points like lines, circles and ellipses</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */

public interface PointList extends GeoObject {
	/*
	 * ======================================================================
	 * ========================== VARIABLES =================================
	 * ======================================================================
	 */
	/**
	 * <i><b>
	 * Version number of interface in form xx.yy where
	 * xx is major version/release number and yy is minor
	 * release number.
	 * </b></i>
	 */
	public static final String VERSION_NUM = "1.00"; // this should match the version number from interface comment
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method that retrieves the collection of points from
	 * this point list.
	 * 
	 * @return The collection of all points from this point list
	 */
	public abstract Vector<Point> getPoints();
}