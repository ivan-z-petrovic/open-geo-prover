/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.auxiliary;

import com.ogprover.polynomials.XPolynomial;

/**
 * <dl>
 * <dt><b>Interface description:</b></dt>
 * <dd>Interface for generalized segments: line segments and products of two line segments</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */

public interface GeneralizedSegment {
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
	 * Method that retrieves instantiated condition for square of segment.
	 * 
	 * @return	XPolynomial object representing the square of this segment.
	 */
	public XPolynomial getInstantiatedConditionForSquareOfSegment();
	/**
	 * Method that retrieves the description of this segment.
	 * 
	 * @return	Textual description of segment.
	 */
	public String getDescription();

}
