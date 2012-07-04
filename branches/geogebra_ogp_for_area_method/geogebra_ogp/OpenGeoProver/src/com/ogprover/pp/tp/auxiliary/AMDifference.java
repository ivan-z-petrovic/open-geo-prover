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
 * <dd>Class for representing the difference between two expressions.</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Damien Desfontaines
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
		s.append("-");
		s.append(term2.print());
		s.append(")");
		return s.toString();
	}
	
	@Override
	public boolean equals(AMExpression expr) {
		if (!(expr instanceof AMDifference))
			return false;
		AMDifference diff = (AMDifference)expr;
		return (term1.equals(diff.getTerm1()) && term2.equals(diff.getTerm2()));
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
		return new AMDifference(term1.uniformize(), term2.uniformize());
	}
	
	@Override
	public AMExpression simplifyInOneStep() {
		AMExpression t1 = term1.simplifyInOneStep();
		AMExpression t2 = term2.simplifyInOneStep();
		if (t2.isZero())
			return t1; // a-0 -> a
		if (t1.isZero())
			return new AMAdditiveInverse(t2); // 0-a -> -a
		if (t1 instanceof AMNumber && t2 instanceof AMNumber)
			return new AMNumber(((AMNumber)t1).value() - ((AMNumber)t2).value()); // n-n' -> n-n'
		if (t1.equals(t2))
			return new AMNumber(0); // a-a -> 0
		if (t2 instanceof AMAdditiveInverse)
			return new AMSum(t1, ((AMAdditiveInverse) t2).expr); // a--b -> a+b
		return new AMDifference(t1, t2);
	}
	
	@Override
	public AMExpression eliminate(Point pt, AreaMethodProver prover) {
		return new AMDifference(term1.eliminate(pt, prover), term2.eliminate(pt, prover));
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
					return new AMFraction(new AMDifference(num1, num2), den1);
				}
				AMExpression numerator = new AMDifference(new AMProduct(num1, den2), new AMProduct(num2, den1));
				AMExpression denominator = new AMProduct(den1, den2);
				return new AMFraction(numerator, denominator);
			}
			
			return new AMFraction(new AMDifference(num1, new AMProduct(expr2, den1)), den1);
		}
		
		if (expr2 instanceof AMFraction) {
			AMExpression num2 = ((AMFraction)expr2).getNumerator();
			AMExpression den2 = ((AMFraction)expr2).getDenominator();
			return new AMFraction(new AMDifference(new AMProduct(expr1, den2), num2), den2);
		}
		
		return new AMDifference(expr1, expr2);
	}
	@Override
	public AMExpression reductToRightAssociativeForm() {
		AMExpression firstTerm = term1.reductToRightAssociativeForm();
		AMExpression secondTerm = term2.reductToRightAssociativeForm();
		return (new AMSum(firstTerm, new AMProduct(new AMNumber(-1), secondTerm))).reductToRightAssociativeForm();
	}
	
	@Override
	public AMExpression toIndependantVariables() {
		return new AMDifference(term1.toIndependantVariables(), term2.toIndependantVariables());
	}
	
	@Override
	public int size() {
		return 1 + term1.size() + term2.size();
	}
}