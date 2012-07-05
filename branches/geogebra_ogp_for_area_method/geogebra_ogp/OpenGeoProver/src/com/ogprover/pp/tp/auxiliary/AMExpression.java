/* 
 * DISCLAIMER PLACEHOLDER 
 */
package com.ogprover.pp.tp.auxiliary;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import com.ogprover.pp.tp.geoconstruction.FreePoint;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.thmprover.AreaMethodProver;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for representing expressions used in the area method :
 * 		those expressions can be integers, ratios of two collinear segments,
 * 		area of a triangle, pythagoras difference between three points, or 
 * 		any rational expression formed by those primitives.</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Damien Desfontaines
 */
public abstract class AMExpression {
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
	 * Intermediary points used in the last step of the algorithm.
	 * They are free points, and are supposed not to be collinear, and such as 
	 * none of them is collinear to two other free points.
	 */
	public static Point iO = new FreePoint("iO");
	public static Point iU = new FreePoint("iU");
	public static Point iV = new FreePoint("iV");
	
	/**
	 * Area of the triangle formed by iO, iU and iV
	 */
	public static AMExpression souv = new AMAreaOfTriangle(iO, iU, iV);
	
	
	/*
	 * ======================================================================
	 * ========================= ABSTRACT METHODS ===========================
	 * ======================================================================
	 */
	/**
	 * @return a String corresponding to the expression.
	 */
	public abstract String print();

	/**
	 * @return the set of points used in this expression.
	 */
	public abstract HashSet<Point> getPoints();
	
	/**
	 * @return true iff this expression contains only free points
	 */
	public abstract boolean containsOnlyFreePoints();
	
	/**
	 * @return the number of nodes in the syntaxic tree of the formula
	 */
	public abstract int size();
	
	/**
	 * @return the expression uniformized
	 */
	public abstract AMExpression uniformize();
	
	/**
	 * See http://hal.inria.fr/hal-00426563/PDF/areaMethodRecapV2.pdf for the list of all possible simplifications
	 * @return the expression one-step-simplified
	 */
	public abstract AMExpression simplifyInOneStep();
	
	/**
	 * @return the expression in which one point has been eliminated
	 * @param pt		The point to eliminate
	 * @param prover	The prover which called the function
	 * @throws ExecutionException 
	 */
	public abstract AMExpression eliminate(Point pt, AreaMethodProver prover) throws UnknownStatementException;
	
	/**
	 * @return the expression in the form AMFraction(a,b), where a and b do not contain any AMFraction.
	 */
	public abstract AMExpression reduceToSingleFraction();
	
	/**
	 * An expression containing additions and products is said to be in a 
	 * right associative form if it is of the form 
	 * 		expr = (c1*x1*...*xn_1 + c2*y1*...*yn_2 + ... + cm*z1*...*zn_m)
	 * where ci are constants, and xi, yi and zi are of type AMAreaOfTriangle, 
	 * or AMPythagorasDifference, and where the big sum and all the products 
	 * are in a right associative form.
	 * @return the expression transformed into a right associative form.
	 * /!\ This method is supposed to be called on an object without any fraction left.
	 */
	public abstract AMExpression reduceToRightAssociativeFormInOneStep();
	
	/**
	 * @param prover TODO
	 * @return the expression in which all geometric quantities involved are independant.
	 * @throws UnknownStatementException TODO
	 */
	public abstract AMExpression toIndependantVariables(AreaMethodProver prover) throws UnknownStatementException;
	
	/*
	 * ======================================================================
	 * ======================= COMMON OBJECT METHODS ========================
	 * ======================================================================
	 */
	@Override
	public abstract boolean equals(Object expr);
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * @return true iff this expression is equal to (new AMNumber(0))
	 */
	public boolean isZero() {
		if (this instanceof AMNumber) {
			if (((AMNumber)this).value() == 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return the expression fully simplified
	 */
	public AMExpression simplify() {
		AMExpression last = this; 
		AMExpression current = last.simplifyInOneStep();
		while (!last.equals(current)) {
			last = current;
			current = current.simplifyInOneStep();
		}
		return last;
	}
	
	/**
	 * @return the expression in right associative form
	 */
	public AMExpression reduceToRightAssociativeForm() {
		AMExpression last = this; 
		AMExpression current = last.reduceToRightAssociativeFormInOneStep();
		while (!last.equals(current)) {
			last = current;
			current = current.reduceToRightAssociativeFormInOneStep();
		}
		return last;	
	}
	
	/**
	 * @return the list of the factors of this product
	 * /!\ The expression has to be a product, in right associative form, with (or without) a single constant on the left.
	 */
	public List<AMExpression> productToList() {
		if (this instanceof AMPythagorasDifference || this instanceof AMAreaOfTriangle) {
			List<AMExpression> list = new Vector<AMExpression>(); 
			list.add(this);
			return list;
		}
		
		if (this instanceof AMNumber) {
			return new Vector<AMExpression>();
		}
		
		if (this instanceof AMProduct) {
			AMExpression leftFactor = ((AMProduct)this).getFactor1();
			AMExpression rightFactor = ((AMProduct)this).getFactor2();
			List<AMExpression> list = rightFactor.productToList();
			if (leftFactor instanceof AMPythagorasDifference || leftFactor instanceof AMAreaOfTriangle)
				list.add(leftFactor);
			return list;
		}
		
		System.out.println("The expression is not in the good form : " + this.print());
		return null;
	}
	
	/**
	 * @return true iff the given product is the same as this. 
	 * /!\ The expressions has to be products, in right associative form, with (or without) a single constant on the left
	 */
	public boolean isSameProduct(AMExpression expr) {
		List<AMExpression> factorsOfThis = this.productToList();
		List<AMExpression> factorsOfExpr = expr.productToList();
		AMExpressionComparator comparator = new AMExpressionComparator();
		Collections.sort(factorsOfExpr, comparator);
		Collections.sort(factorsOfThis, comparator);
		
		if (factorsOfThis.size() != factorsOfExpr.size())
			return false;
		for (int i=0 ; i<factorsOfThis.size() ; i++)
			if (!(factorsOfThis.get(i).equals(factorsOfExpr.get(i))))
				return false;
		return true;
		//return factorsOfThis.equals(factorsOfExpr);
	}
	
	/**
	 * If this is a sum of products in right associative form, where all the products contain one single 
	 * constant on the left, add the given expression to the sum, without repetition of products.
	 * For example, if this = 2*x*y + 4*z and expr = -7*x*y, this.addProductToSum(AMExpression expr) will be
	 * equal to -3*x*y + 4*z. This works even if the factors of the products are not in the same order (e.g.
	 * if expr = -7*y*z.
	 */
	public AMExpression addProductToSum(AMExpression expr) {
		//System.out.println("  " + this.print() + ".addProductToSum(" + expr.print() + ")");
		if(this instanceof AMSum) {
			//System.out.println("    case 1 : this[" + this.print() + "] instanceof AMSum");
			AMExpression leftTerm = ((AMSum)this).getTerm1();
			AMExpression restOfSum = ((AMSum)this).getTerm2();
			if (leftTerm.isSameProduct(expr)) {
				int constantOfLeftTerm = ((AMNumber)((AMProduct)leftTerm).getFactor1()).value();
				int constantOfExpr =  ((AMNumber)((AMProduct)expr).getFactor1()).value();
				int sum = constantOfExpr + constantOfLeftTerm;
				AMExpression restOfProduct = ((AMProduct)leftTerm).getFactor2();
				return new AMSum(new AMProduct(new AMNumber(sum), restOfProduct), restOfSum);
			}
			return new AMSum(leftTerm, restOfSum.addProductToSum(expr));
		}
		if (this.isSameProduct(expr)) {
			int constantOfThis = ((AMNumber)((AMProduct)this).getFactor1()).value();
			int constantOfExpr =  ((AMNumber)((AMProduct)expr).getFactor1()).value();
			int sum = constantOfExpr + constantOfThis;
			AMExpression restOfProduct = ((AMProduct)this).getFactor2();
			return new AMProduct(new AMNumber(sum), restOfProduct);
		}
		return new AMSum(this, expr);
	}
	
	/**
	 * If this is a sum of products, groups the terms which are equal up to a constant multiplicative factor.
	 */
	public AMExpression groupSumOfProducts() {
		if (this instanceof AMSum) {
			AMExpression leftTerm = ((AMSum)this).getTerm1();
			AMExpression groupedRest = ((AMSum)this).getTerm2().groupSumOfProducts();
			return groupedRest.addProductToSum(leftTerm);
		}
		return this;
	}
	
	/**
	 * Given a point P, returns the expression representing the area of the triangle iOiUP.
	 * It corresponds to the quantity X_P described in the Julien Narboux' paper.
	 */
	protected static AMExpression getX(Point p) {
		return new AMAreaOfTriangle(iO, iU, p);
	}
	
	/**
	 * Given a point P, returns the expression representing the area of the triangle iOiVP.
	 * It corresponds to the quantity Y_P described in the Julien Narboux' paper.
	 */
	protected static AMExpression getY(Point p) {
		return new AMAreaOfTriangle(iO, iV, p);
	}
}