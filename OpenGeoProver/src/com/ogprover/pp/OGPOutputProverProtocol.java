/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Abstract class for output prover protocol</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public abstract class OGPOutputProverProtocol implements OGPProverProtocol {
	/*
	 * Output prover protocol is used to pass prover results from OGP
	 * to some external system. It should contain following information:
	 * 
	 * 1. Brief prover result - whether theorem has been proved or
	 * 		disproved or couldn't be proved; with time and space
	 * 		measures of execution.
	 * 2. Generated output reports and path of log file.
	 * 3. Collection of all NDG conditions in user readable form. 
	 */
	
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
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method that retrieves prover protocol type
	 * 
	 * @return	The type of prover protocol
	 */
	public int getProverProtocolType() {
		return OGPProverProtocol.PP_TYPE_OUTPUT;
	}
	
	

	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */	
}
