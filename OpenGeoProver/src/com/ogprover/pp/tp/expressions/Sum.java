/* 
 * DISCLAIMER PLACEHOLDER 
 */
package com.ogprover.pp.tp.expressions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import com.ogprover.pp.tp.auxiliary.FloatCoordinates;
import com.ogprover.pp.tp.auxiliary.UnknownStatementException;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.thmprover.AreaMethodProver;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for representing sum of two expressions.</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Damien Desfontaines
 */
public class Sum extends AMExpression {
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
	 * @see com.ogprover.pp.tp.expressions.AMExpression#getPoints()
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
	public Sum(AMExpression term1, AMExpression term2) {
		this.term1 = term1;
		this.term2 = term2;
	}
	
	/*
	 * ======================================================================
	 * ======================= COMMON OBJECT METHODS ========================
	 * ======================================================================
	 */
	/**
	 * @see com.ogprover.pp.tp.expressions.AMExpression#toString()
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
	public boolean equals(Object expr) {
		if (!(expr instanceof Sum))
			return false;
		Sum sum = (Sum)expr;
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
	public AMExpression uniformize(HashSet<HashSet<Point>> knownCollinearPoints) {
		return new Sum(term1.uniformize(knownCollinearPoints), term2.uniformize(knownCollinearPoints));
	}
	
	@Override
	public AMExpression simplifyInOneStep() {
		AMExpression t1 = term1.simplifyInOneStep();
		AMExpression t2 = term2.simplifyInOneStep();
		if (t1.isZero())
			return t2; // 0+a -> a
		if (t2.isZero())
			return t1; // a+0 -> a
		if (t2 instanceof AdditiveInverse)
			return new Difference(t1, ((AdditiveInverse)t2).getExpr()); // a+(-b) -> a-b
		if (t1 instanceof AdditiveInverse)
			return new Difference(t2, ((AdditiveInverse)t1).getExpr()); // (-a)+b -> b-a
		if (t1 instanceof BasicNumber && t2 instanceof BasicNumber)
			return ((BasicNumber)t1).add((BasicNumber) t2); // n+n' -> n+n'
		return new Sum(t1, t2);
	}
	
	@Override
	public AMExpression eliminate(Point pt, Vector<Boolean> isLemmaUsed, AreaMethodProver prover) throws UnknownStatementException {
		return new Sum(term1.eliminate(pt, isLemmaUsed, prover), term2.eliminate(pt, isLemmaUsed, prover));
	}
	
	@Override
	public AMExpression reduceToSingleFraction() {
		AMExpression expr1 = term1.reduceToSingleFraction();
		AMExpression expr2 = term2.reduceToSingleFraction();
		
		if (expr1 instanceof Fraction) {
			AMExpression num1 = ((Fraction)expr1).getNumerator();
			AMExpression den1 = ((Fraction)expr1).getDenominator();
			
			if (expr2 instanceof Fraction) {
				AMExpression num2 = ((Fraction)expr2).getNumerator();
				AMExpression den2 = ((Fraction)expr2).getDenominator();
				if (den1.equals(den2)) {
					return new Fraction(new Sum(num1, num2), den1);
				}
				AMExpression numerator = new Sum(new Product(num1, den2), new Product(num2, den1));
				AMExpression denominator = new Product(den1, den2);
				return new Fraction(numerator, denominator);
			}
			
			return new Fraction(new Sum(num1, new Product(expr2, den1)), den1);
		}
		
		if (expr2 instanceof Fraction) {
			AMExpression num2 = ((Fraction)expr2).getNumerator();
			AMExpression den2 = ((Fraction)expr2).getDenominator();
			return new Fraction(new Sum(new Product(expr1, den2), num2), den2);
		}
		
		return new Sum(expr1, expr2);
	}
	
	@Override
	public AMExpression toIndependantVariables(AreaMethodProver prover) throws UnknownStatementException {
		return new Sum(term1.toIndependantVariables(prover), term2.toIndependantVariables(prover));
	}
	
	@Override
	public int size() {
		return 1 + term1.size() + term2.size();
	}
	
	@Override
	public AMExpression replace(HashMap<Point, Point> replacementMap) {
		return new Sum(term1.replace(replacementMap), term2.replace(replacementMap));
	}
	
	@Override
	public AMExpression toSumOfProducts() {
		return new SumOfProducts(((SumOfProducts) term1.toSumOfProducts()).getTerms(), ((SumOfProducts) term2.toSumOfProducts()).getTerms());
	}
	@Override
	public double testValue(HashMap<String, FloatCoordinates> coords) {
		return (term1.testValue(coords) + term2.testValue(coords));
	}
}