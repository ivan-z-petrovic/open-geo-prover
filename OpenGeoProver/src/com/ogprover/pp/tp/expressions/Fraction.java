/* 
 * DISCLAIMER PLACEHOLDER 
 */
package com.ogprover.pp.tp.expressions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.auxiliary.FloatCoordinates;
import com.ogprover.pp.tp.auxiliary.UnknownStatementException;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.thmprover.AreaMethodProver;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for representing the fraction of two expressions.</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Damien Desfontaines
 */
public class Fraction extends AMExpression {
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
	 * @see com.ogprover.pp.tp.expressions.AMExpression#getPoints()
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
	public Fraction(AMExpression numerator, AMExpression denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
	}
	
	/**
	 * Constructor method - shortcut to describe a rational number
	 * 
	 * @param numerator 	Expression
	 * @param denominator	Expression
	 */
	public Fraction(int numerator, int denominator) {
		this.numerator = new BasicNumber(numerator);
		this.denominator = new BasicNumber(denominator);
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
		s.append(numerator.print());
		s.append("/");
		s.append(denominator.print());
		s.append(")");
		return s.toString();
	}
	
	@Override
	public boolean equals(Object expr) {
		if (!(expr instanceof Fraction))
			return false;
		Fraction frac = (Fraction)expr;
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
	public AMExpression uniformize(HashSet<HashSet<Point>> knownCollinearPoints) {
		return new Fraction(numerator.uniformize(knownCollinearPoints), denominator.uniformize(knownCollinearPoints));
	}
	
	@Override
	public AMExpression simplifyInOneStep() {
		AMExpression n = numerator.simplifyInOneStep();
		AMExpression d = denominator.simplifyInOneStep();
		if (n.isZero())
			return new BasicNumber(0); // 0/a -> 0
		if (d.isZero()) {
			System.out.println("Division by zero in expression : " + this.print());
			return null;
		}
		if (n.equals(d))
			return new BasicNumber(1); // a/a -> 0
		if (d.equals(new BasicNumber(1)))
			return n; // a/1 -> a
		if (n instanceof AdditiveInverse) {
			if (d instanceof AdditiveInverse)
				return new Fraction(((AdditiveInverse)n).getExpr(), ((AdditiveInverse)d).getExpr()); // (-a)/(-b) -> a/b
			return new AdditiveInverse(new Fraction(((AdditiveInverse)n).getExpr(), d)); // (-a)/b -> -(a/b)
		}
		if (d instanceof AdditiveInverse)
			return new AdditiveInverse(new Fraction(n, ((AdditiveInverse)d).getExpr())); // a/(-b) -> -(a/b)
		if (n instanceof Product) {
			Product product = (Product)n;
			AMExpression factor1 = product.getFactor1().simplifyInOneStep();
			AMExpression factor2 = product.getFactor2().simplifyInOneStep();
			if (factor1.equals(d))
				return factor2; // (a*b)/a -> b
			if (factor2.equals(d))
				return factor1; // (b*a)/a -> b
		}
		return new Fraction(n, d);
	}
	
	@Override
	public AMExpression eliminate(Point pt, Vector<Boolean> isLemmaUsed, AreaMethodProver prover) throws UnknownStatementException {
		return new Fraction(numerator.eliminate(pt, isLemmaUsed, prover), denominator.eliminate(pt, isLemmaUsed, prover));
	}
	
	@Override
	public AMExpression reduceToSingleFraction() {
		AMExpression expr1 = numerator.reduceToSingleFraction();
		AMExpression expr2 = denominator.reduceToSingleFraction();
		
		if (expr1 instanceof Fraction) {
			AMExpression num1 = ((Fraction)expr1).getNumerator();
			AMExpression den1 = ((Fraction)expr1).getDenominator();
			
			if (expr2 instanceof Fraction) {
				AMExpression num2 = ((Fraction)expr2).getNumerator();
				AMExpression den2 = ((Fraction)expr2).getDenominator();
				if (den1.equals(den2)) {
					return new Fraction(num1,num2);
				}
				return new Fraction(new Product(num1, num2), new Product(den1, den2));
			}
			
			return new Fraction(num1, new Product(den1, expr2));
		}
		
		if (expr2 instanceof Fraction) {
			AMExpression num2 = ((Fraction)expr2).getNumerator();
			AMExpression den2 = ((Fraction)expr2).getDenominator();
			return new Fraction(new Product(expr1, den2), num2);
		}
		
		return new Fraction(expr1, expr2);
	}
	
	@Override
	public AMExpression toIndependantVariables(AreaMethodProver prover) throws UnknownStatementException {
		return new Fraction(numerator.toIndependantVariables(prover), denominator.toIndependantVariables(prover));
	}
	
	@Override
	public int size() {
		return 1 + numerator.size() + denominator.size();
	}
	
	@Override
	public AMExpression replace(HashMap<Point, Point> replacementMap) {
		return new Fraction(numerator.replace(replacementMap), denominator.replace(replacementMap));
	}
	
	@Override
	public AMExpression toSumOfProducts() {
		//return new Fraction(numerator.toSumOfProducts(), denominator);
		OpenGeoProver.settings.getLogger().error("Calling toSumOfProduct on an instance of Fraction : " + this.print());
		return null;
	}
	
	@Override
	public double testValue(HashMap<String, FloatCoordinates> coords) {
		return (numerator.testValue(coords) / denominator.testValue(coords));
	}
}