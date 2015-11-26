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
 * <dd>Class for representing the sum of products of any number of geometric quantities.</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Damien Desfontaines
 */
public class SumOfProducts extends AMExpression {
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
	 * Products appearing in the sum.
	 */
	protected HashSet<BigProduct> terms = new HashSet<BigProduct>();
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 */
	public SumOfProducts() {
	}
		
	/**
	 * Constructor method
	 * @param term	 	First term of the expression
	 */
	public SumOfProducts(BigProduct term) {
		this.terms.add(term);
	}
	
	/**
	 * Constructor method
	 * @param terms	 	Terms of the sum
	 */
	public SumOfProducts(HashSet<BigProduct> terms) {
		this.terms.addAll(terms);
	}
	
	/**
	 * Constructor method
	 * @param terms	 	Terms of the sum
	 * @param term	 	Term to add to the sum
	 */
	public SumOfProducts(HashSet<BigProduct> terms, BigProduct term) {
		this.terms.addAll(terms);
		this.addTerm(term);
	}
	
	/**
	 * Constructor method
	 * @param terms1 	Terms of the first sum
	 * @param terms2 	Terms of the second sum
	 */
	public SumOfProducts(HashSet<BigProduct> terms1, HashSet<BigProduct> terms2) {
		if (terms1.size() < terms2.size()) {
			HashSet<BigProduct> copy = terms1;
			terms1 = terms2;
			terms2 = copy;
		}
		this.terms.addAll(terms1);
		for (BigProduct term : terms2)
			this.addTerm(term);
	}
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	public HashSet<BigProduct> getTerms() {
		return terms;
	}
	
	
	/*
	 * ======================================================================
	 * ======================= COMMON OBJECT METHODS ========================
	 * ======================================================================
	 */
	@Override
	public String print() {
		if (terms.isEmpty())
			return "0";
		StringBuilder s = new StringBuilder();
		boolean premier = true;
		s.append("(");
		for (BigProduct p : terms) {
			if (!premier)
				s.append("+");
			else
				premier = false;
			s.append(p.print());
		}
		s.append(")");
		return s.toString();
	}

	@Override
	public boolean equals(Object expr) {
		if (!(expr instanceof SumOfProducts))
			return false;
		SumOfProducts sum = (SumOfProducts) expr;
		return terms.equals(sum.getTerms());
	}

	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	@Override
	public HashSet<Point> getPoints() {
		HashSet<Point> points = new HashSet<Point>();
		for (BigProduct p : terms)
			points.addAll(p.getPoints());
		return points;
	}

	@Override
	public boolean containsOnlyFreePoints() {
		for (BigProduct p : terms)
			if (!p.containsOnlyFreePoints())
				return false;
		return true;
	}

	@Override
	public int size() {
		int sum = 0;
		for (BigProduct p : terms)
			sum += p.size();
		return sum;
	}

	@Override
	public AMExpression uniformize(HashSet<HashSet<Point>> knownCollinearPoints) {
		HashSet<BigProduct> newTerms = new HashSet<BigProduct>();
		for (BigProduct p : terms)
			newTerms.add((BigProduct) p.uniformize(knownCollinearPoints));
		return new SumOfProducts(newTerms);
	}

	@Override
	public AMExpression simplifyInOneStep() {
		BasicNumber d = new BasicNumber(0);
		for (BigProduct p : terms)
			d = d.gcd(p.getCoeff());
		// We remove all the zeros
		HashSet<BigProduct> newTerms = new HashSet<BigProduct>();
		for (BigProduct p : terms) {
			BasicNumber coeff = p.getCoeff();
			if (!(coeff.isZero())) {
				p.setCoeff(coeff.divide(d));
				//p.setCoeff(coeff);
				newTerms.add(p);
			}
		}
		AMExpression r = new SumOfProducts(newTerms);
		return r;
	}

	@Override
	public AMExpression eliminate(Point pt, Vector<Boolean> isLemmaUsed, AreaMethodProver prover)
			throws UnknownStatementException {
		AMExpression sum = new BasicNumber(0);
		for (BigProduct p : terms)
			sum = new Sum(p.eliminate(pt, isLemmaUsed, prover),sum);
		return sum;
	}

	@Override
	public AMExpression reduceToSingleFraction() {
		return this;
	}

	@Override
	public AMExpression toIndependantVariables(AreaMethodProver prover)
			throws UnknownStatementException {
		AMExpression sum = new BasicNumber(0);
		for (BigProduct p : terms)
			sum = new Sum(p.toIndependantVariables(prover),sum);
		return sum;
	}

	@Override
	public AMExpression replace(HashMap<Point, Point> replacementMap) {
		HashSet<BigProduct> newTerms = new HashSet<BigProduct>();
		for (BigProduct p : terms)
			newTerms.add((BigProduct) p.replace(replacementMap));
		return new SumOfProducts(newTerms);
	}
	
	
	public void addTerm(BigProduct product) {
		boolean productFound = false;
		HashSet<BigProduct> newSet = new HashSet<BigProduct>();
		for (BigProduct p : terms) {
			if (p.hasSameFactors(product)) {
				productFound = true;
				BigProduct newProduct = new BigProduct(product.getFactors());
				newProduct.setCoeff((BasicNumber)(new Sum(p.getCoeff(), product.getCoeff())).simplify());
				newSet.add(newProduct);
			} else {
				newSet.add(p);
			}
		}
		if (!productFound) {
			newSet.add(product);
		}
		this.terms = newSet;
	}

	@Override
	public AMExpression toSumOfProducts() {
		return this;
	}

	@Override
	public double testValue(HashMap<String, FloatCoordinates> coords) {
		double sum = 0;
		for (BigProduct p : terms) {
			sum += p.testValue(coords);
		}
		return sum;
	}
}
