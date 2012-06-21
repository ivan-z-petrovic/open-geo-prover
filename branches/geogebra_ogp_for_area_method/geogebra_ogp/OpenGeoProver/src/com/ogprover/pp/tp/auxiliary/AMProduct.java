/* 
 * DISCLAIMER PLACEHOLDER 
 */
package com.ogprover.pp.tp.auxiliary;

import java.util.HashSet;

import com.ogprover.pp.tp.geoconstruction.Point;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for representing multiplication of two expressions.</dd>
 * </dl>
 */
public class AMProduct extends AMExpression {
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
	 * The two factors.
	 */
	protected AMExpression factor1,factor2;

	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	public AMExpression getFactor1() {
		return factor1;
	}
	public AMExpression getFactor2() {
		return factor2;
	}
	
	/**
	 * @see com.ogprover.pp.tp.auxiliary.AMExpression#getPoints()
	 */
	public HashSet<Point> getPoints() {
		HashSet<Point> points = new HashSet<Point>();
		points.addAll(factor1.getPoints());
		points.addAll(factor2.getPoints());
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
	 * @param factor1 	Expression
	 * @param factor2	Expression
	 */
	public AMProduct(AMExpression factor1, AMExpression factor2) {
		this.factor1 = factor1;
		this.factor2 = factor2;
	}
	
	
	/*
	 * ======================================================================
	 * ======================= COMMON OBJECT METHODS ========================
	 * ======================================================================
	 */
	/**
	 * @see com.ogprover.pp.tp.auxiliary.AMExpression#toString()
	 */
	@Override
	public String print() {
		StringBuilder s = new StringBuilder();
		s.append("(");
		s.append(factor1.print());
		s.append("×");
		s.append(factor2.print());
		s.append(")");
		return s.toString();
	}
	
	@Override
	public boolean equals(AMExpression expr) {
		if (!(expr instanceof AMProduct))
			return false;
		AMProduct prod = (AMProduct)expr;
		return (factor1.equals(prod.getFactor1()) && factor2.equals(prod.getFactor2())
				|| factor1.equals(prod.getFactor2()) && factor2.equals(prod.getFactor1()));
	}
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	@Override
	public boolean containsOnlyFreePoints() {
		return (factor1.containsOnlyFreePoints() && factor2.containsOnlyFreePoints());
	}
	
	@Override
	public AMExpression uniformize() {
		return new AMProduct(factor1.uniformize(), factor2.uniformize());
	}
}