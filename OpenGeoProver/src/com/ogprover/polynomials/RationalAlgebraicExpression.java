/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.polynomials;


/**
 * <dl>
 * <dt><b>Interface description:</b></dt>
 * <dd>Interface for all rational algebraic expressions.</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public interface RationalAlgebraicExpression {
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
	 * Method for printing expression in LaTeX format
	 * 
	 * @return	String representing LaTeX format of expression
	 */
	public String printToLaTeX();
	/**
	 * Method for printing expression in XML format
	 * 
	 * @return	String representing XML format of expression
	 */
	public String printToXML();
	/**
	 * Method for printing expression in plain textual format
	 * 
	 * @return	String representing plain textual format of expression
	 */
	public String print();
}