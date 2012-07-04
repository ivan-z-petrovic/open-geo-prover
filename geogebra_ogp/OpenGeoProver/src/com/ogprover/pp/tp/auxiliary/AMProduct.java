/* 
 * DISCLAIMER PLACEHOLDER 
 */
package com.ogprover.pp.tp.auxiliary;

import java.util.HashSet;

import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.thmprover.AreaMethodProver;

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
	public AMExpression simplifyInOneStep() {
		AMExpression f1 = factor1.simplifyInOneStep();
		AMExpression f2 = factor2.simplifyInOneStep();
		if (f1 instanceof AMNumber) {
			if (f2 instanceof AMNumber)
				return new AMNumber(((AMNumber)f1).value() * ((AMNumber)f2).value()); // n.n' -> n*n'
			int value = ((AMNumber)f1).value();
			if (value == 0)
				return new AMNumber(0); // 0.a -> 0
			if (value == 1)
				return f2; // 1.a -> a
			if (value < 0)
				return new AMAdditiveInverse(new AMProduct(new AMNumber(-value), f2)); // (-n).a -> -(n.a)
		}
		if (f2 instanceof AMNumber) {
			int value = ((AMNumber)f2).value();
			if (value == 0)
				return new AMNumber(0); // a.0 -> 0
			if (value == 1)
				return f1; // a.1 -> a
			if (value < 0)
				return new AMAdditiveInverse(new AMProduct(new AMNumber(-value), f1)); // a.(-n) -> -(n.a)
		}
		if (f1 instanceof AMAdditiveInverse) {
			if (f2 instanceof AMAdditiveInverse)
				return new AMProduct(((AMAdditiveInverse)f1).getExpr(), 
									 ((AMAdditiveInverse)f2).getExpr()); // (-a).(-b) -> a.b
			return new AMAdditiveInverse(new AMProduct(((AMAdditiveInverse)f1).getExpr(), f2)); // (-a).b -> -(a.b)
		}
		if (f2 instanceof AMAdditiveInverse)
			return new AMAdditiveInverse(new AMProduct(((AMAdditiveInverse)f2).getExpr(), f1)); // a.(-b) -> -(a.b)))
		if (f1 instanceof AMFraction) {
			AMExpression numerator = ((AMFraction)f1).getNumerator();
			AMExpression denominator = ((AMFraction)f1).getDenominator();
			if (numerator.equals(new AMNumber(1)) && denominator.equals(f2))
				return new AMNumber(1); // a.(1/a) -> 1
		}
		if (f2 instanceof AMFraction) {
			AMExpression numerator = ((AMFraction)f2).getNumerator();
			AMExpression denominator = ((AMFraction)f2).getDenominator();
			if (numerator.equals(new AMNumber(1)) && denominator.equals(f1))
				return new AMNumber(1); // (1/a).a -> 1
		}
		return new AMProduct(f1, f2);
	}
	
	@Override
	public AMExpression eliminate(Point pt, AreaMethodProver prover) {
		return new AMProduct(factor1.eliminate(pt, prover), factor2.eliminate(pt, prover));
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
	
	@Override
	public int size() {
		return 1 + factor1.size() + factor2.size();
	}
}