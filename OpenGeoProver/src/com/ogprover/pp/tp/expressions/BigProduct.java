/* 
 * DISCLAIMER PLACEHOLDER 
 */
package com.ogprover.pp.tp.expressions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.pp.tp.auxiliary.FloatCoordinates;
import com.ogprover.pp.tp.auxiliary.UnknownStatementException;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.thmprover.AreaMethodProver;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for representing the product of any number of geometric quantities.</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Damien Desfontaines
 */
public class BigProduct extends AMExpression {
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
	 * Coefficient in front of the product, by default, it is of course 1.
	 */
	protected BasicNumber coeff = new BasicNumber(1);
	
	/**
	 * Geometric quantities appearing in the product
	 * It is represented as an HashMap, associating with every geometric quantity
	 * 	the number of times where it appears.
	 */
	protected HashMap<GeometricQuantity, Integer> factors;
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	public BasicNumber getCoeff() {
		return coeff;
	}
	
	public HashMap<GeometricQuantity, Integer> getFactors() {
		return factors;
	}
	
	public void setCoeff(BasicNumber coeff) {
		this.coeff = coeff;
	}
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * @param factor 	First factor of the expression
	 */
	public BigProduct(GeometricQuantity factor) {
		factors = new HashMap<GeometricQuantity, Integer>();
		factors.put(factor, new Integer(1));
	}
	
	
	/**
	 * Constructor method
	 * @param coeff 	Value of the product
	 */
	public BigProduct(BasicNumber coeff) {
		factors = new HashMap<GeometricQuantity, Integer>();
		this.coeff = coeff;
	}
	
	/**
	 * Constructor method
	 * @param product 	Previous BigProduct
	 * @param factor	New factor to multiply to the product
	 */
	public BigProduct(BigProduct product, GeometricQuantity factor) {
		coeff = product.getCoeff();
		factors = new HashMap<GeometricQuantity, Integer>(product.getFactors());
		if (factors.containsKey(factor))
			factors.put(factor, new Integer(factors.get(factor).intValue() + 1));
		else
			factors.put(factor, new Integer(1));
	}
	
	/**
	 * Constructor method
	 * @param product 	Previous BigProduct
	 * @param factor	New factor to multiply to the product
	 * @param exponent	Exponent of the new factor
	 */
	public BigProduct(BigProduct product, GeometricQuantity factor, int exponent) {
		coeff = product.getCoeff();
		factors = new HashMap<GeometricQuantity, Integer>(product.getFactors());
		if (factors.containsKey(factor))
			factors.put(factor, new Integer(factors.get(factor).intValue() + exponent));
		else
			factors.put(factor, new Integer(exponent));
	}
	/**
	 * Constructor method
	 * @param product	Previous BigProduct
	 * @param coeff 	Coefficient by which multiply the product
	 */
	public BigProduct(BigProduct product, BasicNumber coeff) {
		factors = new HashMap<GeometricQuantity, Integer>(product.getFactors());
		this.coeff = (BasicNumber) (new Product(product.getCoeff(), coeff)).simplify();
	}
	
	/**
	 * Constructor method
	 * @param terms		Terms to put in the product
	 */
	public BigProduct(HashMap<GeometricQuantity, Integer> terms) {
		factors = new HashMap<GeometricQuantity, Integer>(terms);
	}
	
	
	/**
	 * Constructor method - returns the product of its two arguments
	 * @param product1	The first product 
	 * @param product2	The second product
	 */
	public BigProduct(BigProduct product1, BigProduct product2) {
		coeff = (BasicNumber) (new Product(product1.getCoeff(), product2.getCoeff())).simplify();
		factors = new HashMap<GeometricQuantity, Integer>(product1.getFactors());
		Set<Entry<GeometricQuantity, Integer>> entries = product2.getFactors().entrySet();
		for (Entry<GeometricQuantity, Integer> e : entries) {
			GeometricQuantity factor = e.getKey();
			int exponent = e.getValue().intValue();
			if (factors.containsKey(factor)) {
				int previousExponent = factors.get(factor).intValue();
				factors.put(factor, new Integer(previousExponent + exponent));
			}
			else {
				factors.put(factor, new Integer(exponent));
			}
		}
	}

	/*
	 * ======================================================================
	 * ======================= COMMON OBJECT METHODS ========================
	 * ======================================================================
	 */
	@Override
	public String print() {
		StringBuilder s = new StringBuilder();
		s.append("(");
		s.append(coeff.print());
		Set<Entry<GeometricQuantity, Integer>> entries = factors.entrySet();
		for (Entry<GeometricQuantity, Integer> e : entries) {
			s.append("×" + e.getKey().print());
			int value = e.getValue().intValue();
			if (value != 1)
				s.append("^" + value);
		}
		s.append(")");
		return s.toString();
	}

	@Override
	public boolean equals(Object expr) {
		if (!(expr instanceof BigProduct))
			return false;
		BigProduct product = (BigProduct) expr;
		return coeff.equals(product.getCoeff()) && factors.equals(product.getFactors());
	}

	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	@Override
	public HashSet<Point> getPoints() {
		Set<Entry<GeometricQuantity, Integer>> entries = factors.entrySet();
		HashSet<Point> points = new HashSet<Point>();
		for (Entry<GeometricQuantity, Integer> e : entries) {
			points.addAll(e.getKey().getPoints());
		}
		return points;
	}

	@Override
	public boolean containsOnlyFreePoints() {
		Set<Entry<GeometricQuantity, Integer>> entries = factors.entrySet();
		for (Entry<GeometricQuantity, Integer> e : entries)
			if (!e.getKey().containsOnlyFreePoints())
				return false;
		return true;
	}

	@Override
	public int size() {
		return factors.size();
	}

	@Override
	public AMExpression uniformize(HashSet<HashSet<Point>> knownCollinearPoints) {
		Set<Entry<GeometricQuantity, Integer>> entries = factors.entrySet();
		HashMap<GeometricQuantity, Integer> newMap = new HashMap<GeometricQuantity, Integer>();
		for (Entry<GeometricQuantity, Integer> e : entries) 
			newMap.put((GeometricQuantity) e.getKey().uniformize(knownCollinearPoints), e.getValue());
		return new BigProduct(new BigProduct(newMap), coeff);
	}

	@Override
	public AMExpression simplifyInOneStep() {
		if (coeff.isZero())
			return new BasicNumber(0);
		return this;
	}

	@Override
	public AMExpression eliminate(Point pt, Vector<Boolean> isLemmaUsed, AreaMethodProver prover)
			throws UnknownStatementException {
		AMExpression product = coeff;
		Set<Entry<GeometricQuantity, Integer>> entries = factors.entrySet();
		for (Entry<GeometricQuantity, Integer> e : entries) {
			int power = e.getValue().intValue();
			GeometricQuantity factor = e.getKey();
			for (int i = 0 ; i < power ; i++)
				product = new Product(factor.eliminate(pt, isLemmaUsed, prover), product);
		}
		return product;
	}

	@Override
	public AMExpression reduceToSingleFraction() {
		return this;
	}

	@Override
	public AMExpression toIndependantVariables(AreaMethodProver prover)
			throws UnknownStatementException {
		AMExpression product = coeff;
		Set<Entry<GeometricQuantity, Integer>> entries = factors.entrySet();
		for (Entry<GeometricQuantity, Integer> e : entries) {
			int power = e.getValue().intValue();
			GeometricQuantity factor = e.getKey();
			for (int i = 0 ; i < power ; i++)
				product = new Product(factor.toIndependantVariables(prover), product);
		}
		return product;
	}

	@Override
	public AMExpression replace(HashMap<Point, Point> replacementMap) {
		Set<Entry<GeometricQuantity, Integer>> entries = factors.entrySet();
		HashMap<GeometricQuantity, Integer> newMap = new HashMap<GeometricQuantity, Integer>();
		for (Entry<GeometricQuantity, Integer> e : entries) 
			newMap.put((GeometricQuantity) e.getKey().replace(replacementMap), e.getValue());
		return new BigProduct(new BigProduct(newMap), coeff);
	}
	
	/**
	 * Returns true if the given product has the same factors, ignoring the constant
	 * @param product		A product of geometric quantities
	 */
	public boolean hasSameFactors(BigProduct product) {
		/*
		 *  Note to the people who will maybe read this code : Here, I want to compare
		 *  two HashMaps. The original code was :
		 *  
		 *  return this.getFactors().equals(product.getFactors());
		 *  
		 *  But it seems that equals() method does not work, and I am totally unable to 
		 *  figure out why. I have tried several things, verified ten times that the 
		 *  equals() method was overriden for GeometricQuantity, but the only workaround 
		 *  which seems to work is to put everything in Sets and compare elements one by
		 *  one. If, instead of comparing them one by one, I do simply :
		 *  
		 *  if (!entries2.contains(e))
		 *  	return false;
		 *  
		 *  for example, it does not work. I do not know why.
		 */
		Set<Entry<GeometricQuantity, Integer>> entries1 = product.getFactors().entrySet();
		Set<Entry<GeometricQuantity, Integer>> entries2 = factors.entrySet();
		for (Entry<GeometricQuantity, Integer> e : entries1) {
			boolean found = false;
			for (Entry<GeometricQuantity, Integer> e2 : entries2)
				if (e2.equals(e))
					found = true;
			if (!found)
				return false;
		}
		for (Entry<GeometricQuantity, Integer> e2 : entries2) {
			boolean found = false;
			for (Entry<GeometricQuantity, Integer> e : entries1)
				if (e.equals(e2))
					found = true;
			if (!found)
				return false;
		}
		return true;
	}

	@Override
	public AMExpression toSumOfProducts() {
		return new SumOfProducts(this);
	}

	@Override
	public double testValue(HashMap<String, FloatCoordinates> coords) {
		Set<Entry<GeometricQuantity, Integer>> entries = factors.entrySet();
		double product = coeff.testValue(coords); 
		for (Entry<GeometricQuantity, Integer> e : entries) {
			int power = e.getValue().intValue();
			GeometricQuantity factor = e.getKey();
			for (int i = 0 ; i < power ; i++)
				product *= factor.testValue(coords);
		}
		return product;
	}
}
