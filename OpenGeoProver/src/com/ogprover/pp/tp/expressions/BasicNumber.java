/* 
 * DISCLAIMER PLACEHOLDER 
 */
package com.ogprover.pp.tp.expressions;

import java.math.BigInteger;
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
 * <dd>Class for representing basic numbers in the area method. For the moment,
 * 		they are only integers - not floating numbers, to avoid approximation errors.</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Damien Desfontaines
 */
public class BasicNumber extends AMExpression {
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
	 * Value of the number.
	 */
	protected BigInteger n;

	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	public BigInteger value() {
		return n;
	}
	
	/**
	 * @see com.ogprover.pp.tp.expressions.AMExpression#getPoints()
	 */
	public HashSet<Point> getPoints() {
		return new HashSet<Point>();
	}
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param n		Number
	 */
	public BasicNumber(BigInteger n) {
		this.n = n;
	}

	public BasicNumber(int n) {
		this.n = new BigInteger(Integer.toString(n));
	}
	
	/*
	 * ======================================================================
	 * ======================= COMMON OBJECT METHODS ========================
	 * ======================================================================
	 */
	@Override
	public String print() {
		return n.toString();
	}
	
	@Override
	public boolean equals(Object expr) {
		if (!(expr instanceof BasicNumber))
			return false;
		BasicNumber number = (BasicNumber)expr;
		return (n == number.value());
	}

	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	public BasicNumber add(BasicNumber num) {
		return new BasicNumber(n.add(num.value()));
	}
	
	public BasicNumber subtract(BasicNumber num) {
		return new BasicNumber(n.subtract(num.value()));
	}
	
	public BasicNumber multiply(BasicNumber num) {
		return new BasicNumber(n.multiply(num.value()));
	}
	
	public BasicNumber divide(BasicNumber num) {
		return new BasicNumber(n.divide(num.value()));
	}
	
	public BasicNumber negate() {
		return new BasicNumber(n.negate());
	}
	
	public boolean isNegative() {
		return n.compareTo(BigInteger.ZERO) < 0;
	}
	
	public BasicNumber gcd(BasicNumber num) {
		return new BasicNumber(n.gcd(num.value()));
	}
	
	@Override
	public boolean isZero() {
		return n.equals(BigInteger.ZERO);
	}
	
	@Override
	public boolean containsOnlyFreePoints() {
		return true;
	}
	
	@Override
	public AMExpression uniformize(HashSet<HashSet<Point>> knownCollinearPoints) {
		return this;
	}
	
	@Override
	public AMExpression simplifyInOneStep() {
		return this;
	}
	
	@Override
	public AMExpression eliminate(Point pt, Vector<Boolean> isLemmaUsed, AreaMethodProver prover) {
		return this;
	}

	@Override
	public AMExpression reduceToSingleFraction() {
		return this;
	}

	@Override
	public AMExpression toIndependantVariables(AreaMethodProver prover) throws UnknownStatementException {
		return this;
	}

	@Override
	public int size() {
		return 1;
	}
	
	@Override
	public AMExpression replace(HashMap<Point, Point> replacementMap) {
		return this;
	}

	@Override
	public AMExpression toSumOfProducts() {
		return new SumOfProducts(new BigProduct(this));
	}

	@Override
	public double testValue(HashMap<String, FloatCoordinates> coords) {
		return n.floatValue();
	}
}