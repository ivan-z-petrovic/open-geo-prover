/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoconstruction;

import com.ogprover.pp.tp.geoobject.GeoObject;


/**
* <dl>
* <dt><b>Interface description:</b></dt>
* <dd>Interface for general conic section</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public interface ConicSection extends GeoObject {
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
	
	/**
	 * <i><b>Symbolic label for generic point from conic section</b></i>
	 */
	public static final String M0Label = "0";
	
	/**
	 * <i><b>Symbolic labels for parameters of conic section</b></i>
	 */
	public static final String ALabel = "A";
	public static final String BLabel = "B";
	public static final String CLabel = "C";
	public static final String DLabel = "D";
	public static final String ELabel = "E";
	public static final String FLabel = "F";
	
}
