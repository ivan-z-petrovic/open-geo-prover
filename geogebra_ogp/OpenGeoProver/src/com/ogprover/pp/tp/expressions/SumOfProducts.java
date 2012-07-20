/* 
 * DISCLAIMER PLACEHOLDER 
 */
package com.ogprover.pp.tp.expressions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.ogprover.main.OpenGeoProver;
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
		return new SumOfProducts(terms);
	}

	@Override
	public AMExpression simplifyInOneStep() {
		HashSet<BigProduct> newTerms = new HashSet<BigProduct>(terms);
		for (Iterator<BigProduct> i = terms.iterator() ; i.hasNext() ; ) {
			BigProduct next = i.next();
			if (next.getCoeff().isZero())
				newTerms.remove(next);
		}
		return new SumOfProducts(newTerms);
	}

	@Override
	public AMExpression eliminate(Point pt, AreaMethodProver prover)
			throws UnknownStatementException {
		OpenGeoProver.settings.getLogger().error("Method eliminate should not be called on sum of product instances.");
		return null;
	}

	@Override
	public AMExpression reduceToSingleFraction() {
		return this;
	}

	@Override
	public AMExpression reduceToRightAssociativeFormInOneStep() {
		return this;
	}

	@Override
	public AMExpression toIndependantVariables(AreaMethodProver prover)
			throws UnknownStatementException {
		OpenGeoProver.settings.getLogger().error("Method toIndependantVariables should not be called on sum of product instances.");
		return null;
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
				newProduct.setCoeff((BasicNumber)(new Product(p.getCoeff(), product.getCoeff())).simplify());
				newSet.add(newProduct);
			} else {
				newSet.add(p);
			}
		}
		if (!productFound)
			newSet.add(product);
		this.terms = newSet;
	}

	
	@Override
	public SumOfProducts toSumOfProducts() {
		return this;
	}
}
