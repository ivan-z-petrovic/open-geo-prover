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
 * 
 * @version 1.00
 * @author Damien Desfontaines
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
	@Override
	public AMExpression eliminate(Point pt) {
		return new AMProduct(factor1.eliminate(pt), factor2.eliminate(pt));
	}
	@Override
	public AMExpression reduceToSingleFraction() {
		AMExpression expr1 = factor1.reduceToSingleFraction();
		AMExpression expr2 = factor2.reduceToSingleFraction();
		
		if (expr1 instanceof AMFraction) {
			AMExpression num1 = ((AMFraction)expr1).getNumerator();
			AMExpression den1 = ((AMFraction)expr1).getDenominator();
			
			if (expr2 instanceof AMFraction) {
				AMExpression num2 = ((AMFraction)expr2).getNumerator();
				AMExpression den2 = ((AMFraction)expr2).getDenominator();
				return new AMFraction(new AMProduct(num1, num2), new AMProduct(den1, den2));
			}
			
			return new AMFraction(new AMProduct(num1, expr2), den1);
		}
		
		if (expr2 instanceof AMFraction) {
			AMExpression num2 = ((AMFraction)expr2).getNumerator();
			AMExpression den2 = ((AMFraction)expr2).getDenominator();
			return new AMFraction(new AMProduct(num2, expr1), den2);
		}
		
		return new AMProduct(expr1, expr2);
	}
	
	@Override
	public AMExpression reductToRightAssociativeForm() {
		AMExpression firstFactor = factor1.reductToRightAssociativeForm();
		AMExpression secondFactor = factor2.reductToRightAssociativeForm();
		if (secondFactor instanceof AMSum) {
			AMExpression firstTerm = ((AMSum) secondFactor).getTerm1();
			AMExpression secondTerm = ((AMSum) secondFactor).getTerm2();
			return (new AMSum(new AMProduct(firstFactor, firstTerm), new AMProduct(firstFactor, secondTerm))).reductToRightAssociativeForm();
		}
		if (firstFactor instanceof AMSum) {
			AMExpression firstTerm = ((AMSum) firstFactor).getTerm1();
			AMExpression secondTerm = ((AMSum) firstFactor).getTerm2();
			return (new AMSum(new AMProduct(secondFactor, firstTerm), new AMProduct(secondFactor, secondTerm))).reductToRightAssociativeForm();
		}
		if (firstFactor instanceof AMProduct) {
			AMExpression a = ((AMProduct) firstFactor).getFactor1();
			AMExpression b = ((AMProduct) firstFactor).getFactor2();
			return (new AMProduct(a, new AMProduct(b, secondFactor))).reductToRightAssociativeForm();
		}
		if (firstFactor instanceof AMNumber) {
			if (secondFactor instanceof AMNumber)
				return new AMNumber(((AMNumber) firstFactor).value() * ((AMNumber) secondFactor).value());
			if (secondFactor instanceof AMProduct) {
				AMExpression a = ((AMProduct) secondFactor).getFactor1();
				AMExpression b = ((AMProduct) secondFactor).getFactor2();
				if (a instanceof AMNumber) {
					int product = ((AMNumber) firstFactor).value() * ((AMNumber) a).value();
					return new AMProduct(new AMNumber(product), b);
				}
			}
			
		}
		if (secondFactor instanceof AMNumber)
			return (new AMProduct(secondFactor, firstFactor)).reductToRightAssociativeForm();
		if (secondFactor instanceof AMProduct) {
			AMExpression a = ((AMProduct) secondFactor).getFactor1();
			AMExpression b = ((AMProduct) secondFactor).getFactor2();
			if (a instanceof AMNumber)
				return new AMProduct(a, new AMProduct(firstFactor, b));
		}
		return new AMProduct(firstFactor, secondFactor);
	}
	
	@Override
	public AMExpression toIndependantVariables() {
		return new AMProduct(factor1.toIndependantVariables(), factor2.toIndependantVariables());
	}
}