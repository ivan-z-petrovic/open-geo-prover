/* 
 * DISCLAIMER PLACEHOLDER 
 */
package com.ogprover.pp.tp.auxiliary;

import java.util.HashSet;

import com.ogprover.pp.tp.geoconstruction.Point;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for representing the difference between two expressions.</dd>
 * </dl>
 */
public class AMDifference extends AMExpression {
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
	 * The two terms.
	 */
	protected AMExpression term1,term2;
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	public AMExpression getTerm1() {
		return term1;
	}
	public AMExpression getTerm2() {
		return term2;
	}
	
	/**
	 * @see com.ogprover.pp.tp.auxiliary.AMExpression#getPoints()
	 */
	public HashSet<Point> getPoints() {
		HashSet<Point> points = new HashSet<Point>();
		points.addAll(term1.getPoints());
		points.addAll(term2.getPoints());
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
	 * @param term1 	Expression
	 * @param term2 	Expression
	 */
	public AMDifference(AMExpression term1, AMExpression term2) {
		this.term1 = term1;
		this.term2 = term2;
	}
}