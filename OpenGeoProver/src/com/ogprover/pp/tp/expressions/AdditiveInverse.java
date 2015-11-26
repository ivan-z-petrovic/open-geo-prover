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
 * <dd>Class for representing the opposite inverse of an expression.</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Damien Desfontaines
 */
public class AdditiveInverse extends AMExpression {
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
	 * The expression to be inversed.
	 */
	protected AMExpression expr;

	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	public AMExpression getExpr() {
		return expr;
	}

	/**
	 * @see com.ogprover.pp.tp.expressions.AMExpression#getPoints()
	 */
	public HashSet<Point> getPoints() {
		return expr.getPoints();
	}
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param expr  	Expression
	 */
	public AdditiveInverse(AMExpression expr) {
		this.expr = expr;
	}

	
	/*
	 * ======================================================================
	 * ======================= COMMON OBJECT METHODS ========================
	 * ======================================================================
	 */
	@Override
	public String print() {
		return ("-" + expr.print());
	}

	@Override
	public boolean equals(Object expr) {
		if (!(expr instanceof AdditiveInverse))
			return false;
		AdditiveInverse inv = (AdditiveInverse)expr;
		return this.getExpr().equals(inv.getExpr());
	}
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	@Override
	public boolean containsOnlyFreePoints() {
		return this.expr.containsOnlyFreePoints();
	}
	
	@Override
	public AMExpression uniformize(HashSet<HashSet<Point>> knownCollinearPoints) {
		return new AdditiveInverse(expr.uniformize(knownCollinearPoints));
	}
	
	@Override
	public AMExpression simplifyInOneStep() {
		AMExpression e = expr.simplifyInOneStep();
		if (e instanceof AdditiveInverse)
			return ((AdditiveInverse)e).getExpr(); // --a -> a
		if (e.isZero())
			return new BasicNumber(0); // -0 -> 0
		return new AdditiveInverse(e);
	}
	
	@Override
	public AMExpression eliminate(Point pt, Vector<Boolean> isLemmaUsed, AreaMethodProver prover) throws UnknownStatementException {
		return new AdditiveInverse(expr.eliminate(pt, isLemmaUsed, prover));
	}

	@Override
	public AMExpression reduceToSingleFraction() {
		AMExpression term = expr.reduceToSingleFraction();
		if (term instanceof Fraction) {
			AMExpression numerator = ((Fraction)term).getNumerator();
			AMExpression denominator = ((Fraction)term).getDenominator();
			return new Fraction(new AdditiveInverse(numerator), denominator);
		}
		return new AdditiveInverse(term);
	}

	@Override
	public AMExpression toIndependantVariables(AreaMethodProver prover) throws UnknownStatementException {
		return new AdditiveInverse(expr.toIndependantVariables(prover));
	}

	@Override
	public int size() {
		return 1 + expr.size();
	}
	
	@Override
	public AMExpression replace(HashMap<Point, Point> replacementMap) {
		return new AdditiveInverse(expr.replace(replacementMap));
	}

	@Override
	public AMExpression toSumOfProducts() {
		return (new Product(new BasicNumber(-1), expr)).toSumOfProducts();
	}

	@Override
	public double testValue(HashMap<String, FloatCoordinates> coords) {
		return (0 - expr.testValue(coords));
	}
}