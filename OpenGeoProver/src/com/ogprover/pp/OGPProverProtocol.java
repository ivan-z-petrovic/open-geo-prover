/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Interface for prover protocol</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public interface OGPProverProtocol {
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
	 * <i><b>
	 * Input type of prover protocol
	 * </b></i>
	 */
	public static final int PP_TYPE_INPUT = 0;
	/**
	 * <i><b>
	 * Output type of prover protocol
	 * </b></i>
	 */
	public static final int PP_TYPE_OUTPUT = 1;
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method that retrieves prover protocol type
	 * 
	 * @return	The type of prover protocol
	 */
	public int getProverProtocolType();
	
}
