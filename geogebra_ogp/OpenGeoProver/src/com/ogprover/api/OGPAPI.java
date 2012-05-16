/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.api;

/**
 * <dl>
 * <dt><b>Interface description:</b></dt>
 * <dd>OGPAPI is interface for all APIs from external dynamic 
 *     geometry systems toward OpenGeoProver.</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public interface OGPAPI {
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
	 * Method that accepts input parameters and construction protocol
	 * and calls specific prover method.
	 * 
	 * @param proverInput	Input object that contains construction protocol
	 *                      (with construction steps and statement to be proved)
	 *                      and other input parameters necessary for prover execution
	 * @return	Output object that contains names of report and log files and
	 *          other necessary data like non-degenerative conditions and result
	 *          of prover (whether theorem has been proved or disproved or could not
	 *          be proved)
	 */
	public Object prove(Object proverInput);
}
