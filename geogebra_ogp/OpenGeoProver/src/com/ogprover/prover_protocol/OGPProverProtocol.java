/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.prover_protocol;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Abstract class for prover protocol</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public abstract class OGPProverProtocol {
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
	 * ========================= ABSTRACT METHODS ===========================
	 * ======================================================================
	 */
	/**
	 * Method that retrieves prover protocol type
	 * 
	 * @return	The type of prover protocol
	 */
	public abstract int getProverProtocolType();
	
	

	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	
	
	

	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */

	
}
