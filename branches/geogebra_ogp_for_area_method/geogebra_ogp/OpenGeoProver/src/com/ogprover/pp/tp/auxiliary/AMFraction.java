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
 * 
 * @version 1.00
 * @author Damien Desfontaines
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
		s.append(numerator.print());
		s.append("/");
		s.append(denominator.print());
		s.append(")");
		return s.toString();
	}
	
	@Override
	public boolean equals(AMExpression expr) {
		if (!(expr instanceof AMFraction))
			return false;
		AMFraction frac = (AMFraction)expr;
		return (numerator.equals(frac.getNumerator()) && denominator.equals(frac.getDenominator()));
	}
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	@Override
	public boolean containsOnlyFreePoints() {
		return (numerator.containsOnlyFreePoints() && denominator.containsOnlyFreePoints());
	}
	
	@Override
	public AMExpression uniformize() {
		return new AMFraction(numerator.uniformize(), denominator.uniformize());
	}
	
	@Override
	public AMExpression simplifyInOneStep() {
		AMExpression n = numerator.simplifyInOneStep();
		AMExpression d = denominator.simplifyInOneStep();
		if (n.isZero())
			return new AMNumber(0); // 0/a -> 0
		if (d.isZero()) {
			System.out.println("Division by zero in expression : " + this.print());
			return null;
		}
		if (n.equals(d))
			return new AMNumber(1); // a/a -> 0
		if (d.equals(new AMNumber(1)))
			return n; // a/1 -> a
		if (n instanceof AMAdditiveInverse) {
			if (d instanceof AMAdditiveInverse)
				return new AMFraction(((AMAdditiveInverse)n).getExpr(), ((AMAdditiveInverse)d).getExpr()); // (-a)/(-b) -> a/b
			return new AMAdditiveInverse(new AMFraction(((AMAdditiveInverse)n).getExpr(), d)); // (-a)/b -> -(a/b)
		}
		if (d instanceof AMAdditiveInverse)
			return new AMAdditiveInverse(new AMFraction(n, ((AMAdditiveInverse)d).getExpr())); // a/(-b) -> -(a/b)
		if (n instanceof AMProduct) {
			AMProduct product = (AMProduct)n;
			AMExpression factor1 = product.getFactor1().simplifyInOneStep();
			AMExpression factor2 = product.getFactor2().simplifyInOneStep();
			if (factor1.equals(d))
				return factor2; // (a*b)/a -> b
			if (factor2.equals(d))
				return factor1; // (b*a)/a -> b
		}
		return new AMFraction(n, d);
	}
	
	@Override
	public AMExpression eliminate(Point pt) {
		return new AMFraction(numerator.eliminate(pt), denominator.eliminate(pt));
	}
	
	@Override
	public AMExpression reduceToSingleFraction() {
		AMExpression expr1 = numerator.reduceToSingleFraction();
		AMExpression expr2 = denominator.reduceToSingleFraction();
		
		if (expr1 instanceof AMFraction) {
			AMExpression num1 = ((AMFraction)expr1).getNumerator();
			AMExpression den1 = ((AMFraction)expr1).getDenominator();
			
			if (expr2 instanceof AMFraction) {
				AMExpression num2 = ((AMFraction)expr2).getNumerator();
				AMExpression den2 = ((AMFraction)expr2).getDenominator();
				if (den1.equals(den2)) {
					return new AMFraction(num1,num2);
				}
				return new AMFraction(new AMProduct(num1, num2), new AMProduct(den1, den2));
			}
			
			return new AMFraction(num1, new AMProduct(den1, expr2));
		}
		
		if (expr2 instanceof AMFraction) {
			AMExpression num2 = ((AMFraction)expr2).getNumerator();
			AMExpression den2 = ((AMFraction)expr2).getDenominator();
			return new AMFraction(new AMProduct(expr1, den2), num2);
		}
		
		return new AMFraction(expr1, expr2);
	}
	
	@Override
	public AMExpression reductToRightAssociativeForm() {
		System.out.println("Calling reductToRightAssociativeForm() on an expression which contains fractions : " + this.print());
		return null;
	}
	@Override
	public AMExpression toIndependantVariables() {
		return new AMFraction(numerator.toIndependantVariables(), denominator.toIndependantVariables());
	}
}