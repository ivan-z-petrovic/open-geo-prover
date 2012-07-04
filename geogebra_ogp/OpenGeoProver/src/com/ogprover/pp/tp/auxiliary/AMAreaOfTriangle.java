/* 
 * DISCLAIMER PLACEHOLDER 
 */
package com.ogprover.pp.tp.auxiliary;

import java.util.HashSet;

import com.ogprover.pp.tp.geoconstruction.AMFootPoint;
import com.ogprover.pp.tp.geoconstruction.AMIntersectionPoint;
import com.ogprover.pp.tp.geoconstruction.FreePoint;
import com.ogprover.pp.tp.geoconstruction.PRatioPoint;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.pp.tp.geoconstruction.TRatioPoint;
import com.ogprover.thmprover.AreaMethodProver;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for representing the (oriented) area of a triangle for the area method.</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Damien Desfontaines
 */
public class AMAreaOfTriangle extends AMExpression {
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
	 * Points which form the triangle
	 */
	protected Point a,b,c;

	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	public Point getA() {
		return a;
	}
	public Point getB() {
		return b;
	}
	public Point getC() {
		return c;
	}
	
	/**
	 * @see com.ogprover.pp.tp.auxiliary.AMExpression#getPoints()
	 */
	public HashSet<Point> getPoints() {
		HashSet<Point> points = new HashSet<Point>();
		points.add(a);
		points.add(b);
		points.add(c);
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
	 * @param a		Point
	 * @param b		Point
	 * @param c		Point
	 */
	public AMAreaOfTriangle(Point a, Point b, Point c) {
		this.a = a;
		this.b = b;
		this.c = c;
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
		s.append("S_");
		s.append(a.getGeoObjectLabel());
		s.append(b.getGeoObjectLabel());
		s.append(c.getGeoObjectLabel());
		return s.toString();
	}
	
	@Override
	public boolean equals(AMExpression expr) {
		if (!(expr instanceof AMAreaOfTriangle))
			return false;
		AMAreaOfTriangle area = (AMAreaOfTriangle)expr;
		return (a.equals(area.getA()) && b.equals(area.getB()) && c.equals(area.getC()));
	}
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	@Override
	public boolean containsOnlyFreePoints() {
		if (a instanceof FreePoint && b instanceof FreePoint && c instanceof FreePoint) {
			return true;
		}
		return false;
	}
	
	@Override
	public AMExpression uniformize() {
		if (a.compare(b) && a.compare(c)) {
			if (b.compare(c)) {
				return this;
			}
			return new AMAdditiveInverse(new AMAreaOfTriangle(a, c, b));
		}
		if (b.compare(a) && b.compare(c)) {
			if (a.compare(c)) {
				return new AMAdditiveInverse(new AMAreaOfTriangle(b, a, c));
			}
			return new AMAreaOfTriangle(b, c, a);
		}
		if (a.compare(b)) {
			return new AMAreaOfTriangle(c, a, b);
		}
		return new AMAdditiveInverse(new AMAreaOfTriangle(c, b, a));
	}
	
	@Override
	public AMExpression simplifyInOneStep() {
		if (a.equals(b) || b.equals(c) || c.equals(a))
			return new AMNumber(0); // S_ABA -> 0, S_AAB -> 0, S_BAA -> 0
		return this;
	}
	
	/**
	 * See http://hal.inria.fr/hal-00426563/PDF/areaMethodRecapV2.pdf "elimination lemmas"
	 */
	@Override
	public AMExpression eliminate(Point pt, AreaMethodProver prover) {
		Point aa = null;
		Point bb = null;
		
		if (pt.equals(c)) {
			aa = a;
			bb = b;
		}
		else if (pt.equals(b)) {
			aa = c;
			bb = a;
		}
		else if (pt.equals(a)) {
			aa = b;
			bb = c;
		}
		else
			return this;
		
		if (pt instanceof AMIntersectionPoint) {
			Point u = ((AMIntersectionPoint)pt).getU();
			Point v = ((AMIntersectionPoint)pt).getV();
			Point p = ((AMIntersectionPoint)pt).getP();
			Point q = ((AMIntersectionPoint)pt).getQ();
			
			AMExpression supq = new AMAreaOfTriangle(u, p, q);
			AMExpression gv = new AMAreaOfTriangle(aa, bb, v);
			AMExpression term1 = new AMProduct(supq, gv);
			AMExpression svpq = new AMAreaOfTriangle(v, p, q);
			AMExpression gu = new AMAreaOfTriangle(aa, bb, u);
			AMExpression term2 = new AMProduct(svpq, gu);
			AMExpression numerator = new AMDifference(term1, term2);
			AMExpression supv = new AMAreaOfTriangle(u, p, v);
			AMExpression spvq = new AMAreaOfTriangle(p, v, q);
			AMExpression denominator = new AMSum(supv, spvq);
			return new AMFraction(numerator, denominator);
		}
		
		if (pt instanceof AMFootPoint) {
			Point p = ((AMFootPoint)pt).getP();
			Point u = ((AMFootPoint)pt).getU();
			Point v = ((AMFootPoint)pt).getV();
			
			AMExpression ppuv = new AMPythagorasDifference(p, u, v);
			AMExpression gv = new AMAreaOfTriangle(aa, bb, v);
			AMExpression term1 = new AMProduct(ppuv, gv);
			AMExpression ppvu = new AMPythagorasDifference(p, v, u);
			AMExpression gu = new AMAreaOfTriangle(aa, bb, u);
			AMExpression term2 = new AMProduct(ppvu, gu);
			AMExpression numerator = new AMSum(term1, term2);
			AMExpression denominator = new AMPythagorasDifference(u, v, u);
			return new AMFraction(numerator, denominator);
		}
		
		if (pt instanceof PRatioPoint) {
			Point w = ((PRatioPoint)pt).getW();
			Point u = ((PRatioPoint)pt).getU();
			Point v = ((PRatioPoint)pt).getV();
			AMExpression r = ((PRatioPoint)pt).getR();
			
			AMExpression gw = new AMAreaOfTriangle(aa, bb, w);
			AMExpression gu = new AMAreaOfTriangle(aa, bb, u);
			AMExpression gv = new AMAreaOfTriangle(aa, bb, v);
			AMExpression difference = new AMDifference(gv, gu);
			AMExpression product = new AMProduct(r, difference);
			return new AMSum(gw,product);
		}
		
		if (pt instanceof TRatioPoint) {
			Point p = ((TRatioPoint)pt).getU();
			Point q = ((TRatioPoint)pt).getV();
			AMExpression r = ((TRatioPoint)pt).getR();
			
			AMExpression sabp = new AMAreaOfTriangle(aa, bb, p);
			AMExpression ppab = new AMPythagorasDifference(p, aa, bb);
			AMExpression pqab = new AMPythagorasDifference(q, aa, bb);
			AMExpression ppaqb = new AMDifference(ppab, pqab);
			AMExpression coeff = new AMFraction(r, new AMNumber(4));
			AMExpression product = new AMProduct(coeff, ppaqb);
			return new AMDifference(sabp, product);
		}
		
		// Theoretically, pt cannot be a free point
		if (pt instanceof FreePoint) {
			System.out.println("Trying to eliminate the free point" + pt.getGeoObjectLabel());
			return null;
		}
		
		System.out.println("The point " + pt.getGeoObjectLabel() + "has not been generated using the area method");
		return null;
	}
	
	@Override
	public AMExpression reduceToSingleFraction() {
		return this;
	}
	
	@Override
	public AMExpression reductToRightAssociativeForm() {
		return this;
	}
	
	@Override
	public AMExpression toIndependantVariables() {
		AMExpression firstTerm = new AMProduct(new AMDifference(getY(b), getY(c)), getX(a));
		AMExpression secondTerm = new AMProduct(new AMDifference(getY(c), getY(a)), getX(b));
		AMExpression thirdTerm = new AMProduct(new AMDifference(getY(a), getY(b)), getX(c));
		AMExpression numerator = new AMSum(firstTerm, new AMSum(secondTerm, thirdTerm));
		return new AMFraction(numerator, souv);
	}
	
	@Override
	public int size() {
		return 1;
	}
}