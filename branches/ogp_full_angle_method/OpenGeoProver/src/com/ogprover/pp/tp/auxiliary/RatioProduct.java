/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.auxiliary;

import java.util.Vector;

import com.ogprover.polynomials.XPolynomial;
import com.ogprover.polynomials.XTerm;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for product of n segments' ratios (n >= 0)</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class RatioProduct {
	/*
	 * Segments of these ratio are collinear i.e. they are on same line
	 * or on parallel lines. If there are no ratios, default product is 1.
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
	
	/**
	 * Collection of ratios that make this product
	 */
	private Vector<RatioOfTwoCollinearSegments> ratios = null;
	
	// Instantiated Polynomials
	private XPolynomial numerator = null;
	private XPolynomial denominator = null;
	/**
	 * Flag to determine whether numerator and denominator polynomials 
	 * have been already instantiated.
	 */
	private boolean instantiated = false;
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @param ratios the ratios to set
	 */
	public void setRatios(Vector<RatioOfTwoCollinearSegments> ratios) {
		this.ratios = ratios;
	}

	/**
	 * @return the ratios
	 */
	public Vector<RatioOfTwoCollinearSegments> getRatios() {
		return ratios;
	}

	/**
	 * @param numerator the numerator to set
	 */
	public void setNumerator(XPolynomial numerator) {
		this.numerator = numerator;
	}

	/**
	 * @return the numerator
	 */
	public XPolynomial getNumerator() {
		return numerator;
	}

	/**
	 * @param denominator the denominator to set
	 */
	public void setDenominator(XPolynomial denominator) {
		this.denominator = denominator;
	}

	/**
	 * @return the denominator
	 */
	public XPolynomial getDenominator() {
		return denominator;
	}
	
	/**
	 * @param instantiated the instantiated to set
	 */
	public void setInstantiated(boolean instantiated) {
		this.instantiated = instantiated;
	}

	/**
	 * @return the instantiated
	 */
	public boolean isInstantiated() {
		return instantiated;
	}
	
	
	

	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param ratios	Vector of ratio objects
	 */
	public RatioProduct(Vector<RatioOfTwoCollinearSegments> ratios) {
		this.ratios = ratios;
		// default ratio polynomials are 1s
		this.numerator = new XPolynomial();
		this.numerator.addTerm(new XTerm(1));
		this.denominator = new XPolynomial();
		this.denominator.addTerm(new XTerm(1));
		this.instantiated = false;
	}
	
	/**
	 * Default constructor method
	 */
	public RatioProduct() {
		this(null);
	}
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method that transforms this ratio product to algebraic form - it populates
	 * its numerator and denominator with X-polynomials that represent the algebraic
	 * condition for this ratio product.
	 */
	public void transformToAlgebraicForm() {
		if (this.instantiated)
			return;
		
		if (this.ratios != null) {
			// Note: do not reduce numerator and denominator by UTerm division here !!!
			for (RatioOfTwoCollinearSegments ratio : this.ratios) {
				ratio.transformToAlgebraicForm();
				this.numerator.multiplyByPolynomial(ratio.getNumerator());
				this.denominator.multiplyByPolynomial(ratio.getDenominator());
			}
		}
		
		this.instantiated = true;
	}
	
}


