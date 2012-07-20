/* 
 * DISCLAIMER PLACEHOLDER 
 */
package com.ogprover.pp.tp.expressions;

import java.util.HashMap;
import java.util.HashSet;

import com.ogprover.pp.tp.auxiliary.UnknownStatementException;
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
public class Product extends AMExpression {
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
	 * @see com.ogprover.pp.tp.expressions.AMExpression#getPoints()
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
	public Product(AMExpression factor1, AMExpression factor2) {
		this.factor1 = factor1;
		this.factor2 = factor2;
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
		s.append(factor1.print());
		s.append("×");
		s.append(factor2.print());
		s.append(")");
		return s.toString();
	}
	
	@Override
	public boolean equals(Object expr) {
		if (!(expr instanceof Product))
			return false;
		Product prod = (Product)expr;
		return (factor1.equals(prod.getFactor1()) && factor2.equals(prod.getFactor2()));
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
	public AMExpression uniformize(HashSet<HashSet<Point>> knownCollinearPoints) {
		return new Product(factor1.uniformize(knownCollinearPoints), factor2.uniformize(knownCollinearPoints));
	}
	
	@Override
	public AMExpression simplifyInOneStep() {
		AMExpression f1 = factor1.simplifyInOneStep();
		AMExpression f2 = factor2.simplifyInOneStep();
		if (f1 instanceof BasicNumber) {
			if (f2 instanceof BasicNumber)
				return new BasicNumber(((BasicNumber)f1).value() * ((BasicNumber)f2).value()); // n.n' -> n*n'
			int value = ((BasicNumber)f1).value();
			if (value == 0)
				return new BasicNumber(0); // 0.a -> 0
			if (value == 1)
				return f2; // 1.a -> a
			if (value < 0)
				return new AdditiveInverse(new Product(new BasicNumber(-value), f2)); // (-n).a -> -(n.a)
		}
		if (f2 instanceof BasicNumber) {
			int value = ((BasicNumber)f2).value();
			if (value == 0)
				return new BasicNumber(0); // a.0 -> 0
			if (value == 1)
				return f1; // a.1 -> a
			if (value < 0)
				return new AdditiveInverse(new Product(new BasicNumber(-value), f1)); // a.(-n) -> -(n.a)
		}
		if (f1 instanceof AdditiveInverse) {
			if (f2 instanceof AdditiveInverse)
				return new Product(((AdditiveInverse)f1).getExpr(), 
									 ((AdditiveInverse)f2).getExpr()); // (-a).(-b) -> a.b
			return new AdditiveInverse(new Product(((AdditiveInverse)f1).getExpr(), f2)); // (-a).b -> -(a.b)
		}
		if (f2 instanceof AdditiveInverse)
			return new AdditiveInverse(new Product(((AdditiveInverse)f2).getExpr(), f1)); // a.(-b) -> -(a.b)))
		if (f1 instanceof Fraction) {
			AMExpression numerator = ((Fraction)f1).getNumerator();
			AMExpression denominator = ((Fraction)f1).getDenominator();
			if (numerator.equals(new BasicNumber(1)) && denominator.equals(f2))
				return new BasicNumber(1); // a.(1/a) -> 1
		}
		if (f2 instanceof Fraction) {
			AMExpression numerator = ((Fraction)f2).getNumerator();
			AMExpression denominator = ((Fraction)f2).getDenominator();
			if (numerator.equals(new BasicNumber(1)) && denominator.equals(f1))
				return new BasicNumber(1); // (1/a).a -> 1
		}
		return new Product(f1, f2);
	}
	
	@Override
	public AMExpression eliminate(Point pt, AreaMethodProver prover) throws UnknownStatementException {
		return new Product(factor1.eliminate(pt, prover), factor2.eliminate(pt, prover));
	}
	
	@Override
	public AMExpression reduceToSingleFraction() {
		AMExpression expr1 = factor1.reduceToSingleFraction();
		AMExpression expr2 = factor2.reduceToSingleFraction();
		
		if (expr1 instanceof Fraction) {
			AMExpression num1 = ((Fraction)expr1).getNumerator();
			AMExpression den1 = ((Fraction)expr1).getDenominator();
			
			if (expr2 instanceof Fraction) {
				AMExpression num2 = ((Fraction)expr2).getNumerator();
				AMExpression den2 = ((Fraction)expr2).getDenominator();
				return new Fraction(new Product(num1, num2), new Product(den1, den2));
			}
			
			return new Fraction(new Product(num1, expr2), den1);
		}
		
		if (expr2 instanceof Fraction) {
			AMExpression num2 = ((Fraction)expr2).getNumerator();
			AMExpression den2 = ((Fraction)expr2).getDenominator();
			return new Fraction(new Product(num2, expr1), den2);
		}
		
		return new Product(expr1, expr2);
	}
	
	@Override
	public AMExpression reduceToRightAssociativeFormInOneStep() {
		AMExpression firstFactor = factor1.reduceToRightAssociativeFormInOneStep();
		AMExpression secondFactor = factor2.reduceToRightAssociativeFormInOneStep();
		if (secondFactor instanceof Sum) {
			AMExpression firstTerm = ((Sum) secondFactor).getTerm1();
			AMExpression secondTerm = ((Sum) secondFactor).getTerm2();
			return new Sum(new Product(firstFactor, firstTerm), new Product(firstFactor, secondTerm));
		}
		if (firstFactor instanceof Sum) {
			AMExpression firstTerm = ((Sum) firstFactor).getTerm1();
			AMExpression secondTerm = ((Sum) firstFactor).getTerm2();
			return new Sum(new Product(secondFactor, firstTerm), new Product(secondFactor, secondTerm));
		}
		if (firstFactor instanceof Product) {
			AMExpression a = ((Product) firstFactor).getFactor1();
			AMExpression b = ((Product) firstFactor).getFactor2();
			return new Product(a, new Product(b, secondFactor));
		}
		if (firstFactor instanceof BasicNumber) {
			if (secondFactor instanceof BasicNumber)
				return new BasicNumber(((BasicNumber) firstFactor).value() * ((BasicNumber) secondFactor).value());
			if (secondFactor instanceof Product) {
				AMExpression a = ((Product) secondFactor).getFactor1();
				AMExpression b = ((Product) secondFactor).getFactor2();
				if (a instanceof BasicNumber) {
					int product = ((BasicNumber) firstFactor).value() * ((BasicNumber) a).value();
					return new Product(new BasicNumber(product), b);
				}
			}
			
		}
		if (secondFactor instanceof BasicNumber)
			return new Product(secondFactor, firstFactor);
		if (secondFactor instanceof Product) {
			AMExpression a = ((Product) secondFactor).getFactor1();
			AMExpression b = ((Product) secondFactor).getFactor2();
			if (a instanceof BasicNumber)
				return new Product(a, new Product(firstFactor, b));
		}
		return new Product(firstFactor, secondFactor);
	}
	
	@Override
	public AMExpression toIndependantVariables(AreaMethodProver prover) throws UnknownStatementException {
		return new Product(factor1.toIndependantVariables(prover), factor2.toIndependantVariables(prover));
	}
	
	@Override
	public int size() {
		return 1 + factor1.size() + factor2.size();
	}
	
	@Override
	public AMExpression replace(HashMap<Point, Point> replacementMap) {
		return new Product(factor1.replace(replacementMap), factor2.replace(replacementMap));
	}
	
	@Override
	public SumOfProducts toSumOfProducts() {
		SumOfProducts sum1 = factor1.toSumOfProducts();
		SumOfProducts sum2 = factor2.toSumOfProducts();
		SumOfProducts sumToReturn = new SumOfProducts();
		HashSet<BigProduct> factors1 = sum1.getTerms();
		HashSet<BigProduct> factors2 = sum2.getTerms();
		for (BigProduct product1 : factors1)
			for (BigProduct product2 : factors2)
				sumToReturn.addTerm(new BigProduct(product1, product2));
		return sumToReturn;
	}
}