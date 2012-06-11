/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.prover_protocol;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for output prover protocol</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public class OGPOutputProverProtocol extends OGPProverProtocol {
	/*
	 * TODO
	 * 
	 * Output prover protocol should contain following information:
	 * 
	 * 1. Brief prover result - whether theorem has been proved or
	 * 		disproved or couldn't be proved; with time and space
	 * 		measures of execution.
	 * 2. Generated output reports and path of log file.
	 * 3. Collection of all NDG condition in form derived from algebraic
	 * 		form so that caller could understand them. The best way for 
	 * 		doing this is to pass collection of statements that represent
	 * 		degenerative conditions. E.g. if some NDG condition means that
	 * 		two points must not be identical, then collection will have
	 * 		the statement about two identical points. 
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
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	
	
	

	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */

	
}
