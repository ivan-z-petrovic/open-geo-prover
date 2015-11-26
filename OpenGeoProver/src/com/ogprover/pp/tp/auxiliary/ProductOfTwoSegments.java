/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.auxiliary;

import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.geoobject.Segment;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for product of two segments</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/

public class ProductOfTwoSegments implements GeneralizedSegment{
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
	 * First segment of product
	 */
	private Segment firstSegment = null;
	/**
	 * Second segment of product
	 */
	private Segment secondSegment = null;
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @param firstSegment the firstSegment to set
	 */
	public void setFirstSegment(Segment firstSegment) {
		this.firstSegment = firstSegment;
	}

	/**
	 * @return the firstSegment
	 */
	public Segment getFirstSegment() {
		return firstSegment;
	}

	/**
	 * @param secondSegment the secondSegment to set
	 */
	public void setSecondSegment(Segment secondSegment) {
		this.secondSegment = secondSegment;
	}

	/**
	 * @return the secondSegment
	 */
	public Segment getSecondSegment() {
		return secondSegment;
	}
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param firstSeg	First segment
	 * @param secondSeg	Second segment
	 */
	public ProductOfTwoSegments(Segment firstSeg, Segment secondSeg) {
		this.firstSegment = firstSeg;
		this.secondSegment = secondSeg;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method that retrieves polynomial for square of this segment
	 * 
	 * @return	XPolynomial object that represents the polynomial for 
	 * 			square of this segment
	 */
	public XPolynomial getInstantiatedConditionForSquareOfSegment() {
		return (XPolynomial) this.firstSegment.getInstantiatedConditionForSquareOfSegment().multiplyByPolynomial(this.secondSegment.getInstantiatedConditionForSquareOfSegment());
	}

	/**
	 * @see com.ogprover.pp.tp.auxiliary.GeneralizedSegment#getDescription()
	 */
	public String getDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.firstSegment.getDescription());
		sb.append("*");
		sb.append(this.secondSegment.getDescription());
		return sb.toString();
	}
	
}


