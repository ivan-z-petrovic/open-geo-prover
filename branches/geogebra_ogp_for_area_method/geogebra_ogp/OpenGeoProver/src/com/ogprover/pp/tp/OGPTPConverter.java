/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp;

/**
 * <dl>
 * <dt><b>Interface description:</b></dt>
 * <dd>OGPCPConverter is interface for all Construction Protocol
 * 	   converters from external dynamic geometry systems toward OpenGeoProver.</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public interface OGPTPConverter {
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
	 * Method that accepts input CP and converts it to internal theorem 
	 * protocol (in OGP format).
	 * 
	 * @param inputCP	CP from external system
	 * @return			OGP CP which is result of conversion of input CP 
	 */
	public OGPTP convertCP(Object inputCP);
}
