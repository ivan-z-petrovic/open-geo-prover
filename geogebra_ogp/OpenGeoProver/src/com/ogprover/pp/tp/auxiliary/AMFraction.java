/* 
 * DISCLAIMER PLACEHOLDER 
 */
package com.ogprover.pp.tp.auxiliary;

import java.util.HashSet;

import com.ogprover.pp.tp.geoconstruction.Point;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for representing the fraction of two expressions.</dd>
 * </dl>
 */
public class AMFraction extends AMExpression {
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
	 * Numerator of the fraction
	 */
	protected AMExpression numerator;
	/**
	 * Denominator of the fraction
	 */
	protected AMExpression denominator;

	

	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	public AMExpression getNumerator() {
		return numerator;
	}
	public AMExpression getDenominator() {
		return denominator;
	}
	
	/**
	 * @see com.ogprover.pp.tp.auxiliary.AMExpression#getPoints()
	 */
	public HashSet<Point> getPoints() {
		HashSet<Point> points = new HashSet<Point>();
		points.addAll(numerator.getPoints());
		points.addAll(denominator.getPoints());
		return points;
	}
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param numerator 	Expression
	 * @param denominator	Expression
	 */
	public AMFraction(AMExpression numerator, AMExpression denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
	}
	
	/**
	 * Constructor method - shortcut to describe a rational number
	 * 
	 * @param numerator 	Expression
	 * @param denominator	Expression
	 */
	public AMFraction(int numerator, int denominator) {
		this.numerator = new AMNumber(numerator);
		this.denominator = new AMNumber(denominator);
	}
}