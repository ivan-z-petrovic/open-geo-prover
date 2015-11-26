/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoobject;

/**
 * <dl>
 * <dt><b>Interface description:</b></dt>
 * <dd>Interface for all geometry objects (constructions or some auxiliary objects
 *     used in constructions)</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */

public interface GeoObject {
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
	 * Method that retrieves the label of geometric object
	 * 
	 * @return The label of geometric object
	 */
	public String getGeoObjectLabel();
}