/* 
 * DISCLAIMER PLACEHOLDER 
 */
package com.ogprover.pp.tp.auxiliary;

import java.util.HashSet;

import com.ogprover.pp.tp.geoconstruction.Point;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for representing sum of two expressions.</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Damien Desfontaines
 */
public class AMSum extends AMExpression {
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
	public AMSum(AMExpression term1, AMExpression term2) {
		this.term1 = term1;
		this.term2 = term2;
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
		s.append(term1.print());
		s.append("+");
		s.append(term2.print());
		s.append(")");
		return s.toString();
	}
	
	@Override
	public boolean equals(AMExpression expr) {
		if (!(expr instanceof AMSum))
			return false;
		AMSum sum = (AMSum)expr;
		return (term1.equals(sum.getTerm1()) && term2.equals(sum.getTerm2()));
	}
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	@Override
	public boolean containsOnlyFreePoints() {
		return (term1.containsOnlyFreePoints() && term2.containsOnlyFreePoints());
	}
	
	@Override
	public AMExpression uniformize() {
		return new AMSum(term1.uniformize(), term2.uniformize());
	}
	
	@Override
	public AMExpression eliminate(Point pt) {
		return new AMSum(term1.eliminate(pt), term2.eliminate(pt));
	}
	
	@Override
	public AMExpression reduceToSingleFraction() {
		AMExpression expr1 = term1.reduceToSingleFraction();
		AMExpression expr2 = term2.reduceToSingleFraction();
		
		if (expr1 instanceof AMFraction) {
			AMExpression num1 = ((AMFraction)expr1).getNumerator();
			AMExpression den1 = ((AMFraction)expr1).getDenominator();
			
			if (expr2 instanceof AMFraction) {
				AMExpression num2 = ((AMFraction)expr2).getNumerator();
				AMExpression den2 = ((AMFraction)expr2).getDenominator();
				if (den1.equals(den2)) {
					return new AMFraction(new AMSum(num1, num2), den1);
				}
				AMExpression numerator = new AMSum(new AMProduct(num1, den2), new AMProduct(num2, den1));
				AMExpression denominator = new AMProduct(den1, den2);
				return new AMFraction(numerator, denominator);
			}
			
			return new AMFraction(new AMSum(num1, new AMProduct(expr2, den1)), den1);
		}
		
		if (expr2 instanceof AMFraction) {
			AMExpression num2 = ((AMFraction)expr2).getNumerator();
			AMExpression den2 = ((AMFraction)expr2).getDenominator();
			return new AMFraction(new AMSum(new AMProduct(expr1, den2), num2), den2);
		}
		
		return new AMSum(expr1, expr2);
	}
	
	@Override
	public AMExpression reductToRightAssociativeForm() {
		System.out.println("reductToRightAssociativeForm" + this.print());
		AMExpression firstTerm = term1.reductToRightAssociativeForm();
		AMExpression c = term2.reductToRightAssociativeForm();
		if (firstTerm instanceof AMSum) {
			AMExpression a = ((AMSum) firstTerm).getTerm1();
			AMExpression b = ((AMSum) firstTerm).getTerm2();
			return (new AMSum (a, new AMSum(b, c))).reductToRightAssociativeForm();
		}
		return new AMSum(firstTerm, c);
	}
	
	@Override
	public AMExpression toIndependantVariables() {
		return new AMSum(term1.toIndependantVariables(), term2.toIndependantVariables());
	}
}