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
public class AreaOfTriangle extends GeometricQuantity {
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
	 * @see com.ogprover.pp.tp.expressions.AMExpression#getPoints()
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
	public AreaOfTriangle(Point a, Point b, Point c) {
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
	 * @see com.ogprover.pp.tp.expressions.AMExpression#toString()
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
	public boolean equals(Object expr) {
		if (!(expr instanceof AreaOfTriangle))
			return false;
		AreaOfTriangle area = (AreaOfTriangle)expr;
		//return (a.equals(area.getA()) && b.equals(area.getB()) && c.equals(area.getC()));
		return this.print().equals(area.print());
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
	public AMExpression uniformize(HashSet<HashSet<Point>> knownCollinearPoints) {
		if (a.equals(b) || b.equals(c) || c.equals(a))
			return new BasicNumber(0); // S_ABA -> 0, S_AAB -> 0, S_BAA -> 0
		if (AreaMethodProver.optimizeAreaOfCollinearPoints) {
			HashSet<Point> set = new HashSet<Point>();
			set.add(a);
			set.add(b);
			set.add(c);
			if (knownCollinearPoints.contains(set)) {
				OpenGeoProver.settings.getLogger().debug("Koukou : " + this.print());
				return new BasicNumber(0);
			}
		}
		if (a.compare(b) && a.compare(c)) {
			if (b.compare(c)) {
				return this;
			}
			return new AdditiveInverse(new AreaOfTriangle(a, c, b));
		}
		if (b.compare(a) && b.compare(c)) {
			if (a.compare(c)) {
				return new AdditiveInverse(new AreaOfTriangle(b, a, c));
			}
			return new AreaOfTriangle(b, c, a);
		}
		if (a.compare(b)) {
			return new AreaOfTriangle(c, a, b);
		}
		return new AdditiveInverse(new AreaOfTriangle(c, b, a));
	}
	
	@Override
	public AMExpression simplifyInOneStep() {
		// We assume that the verifications that the expression is non-zero have already been done.
		return this;
	}
	
	/**
	 * See http://hal.inria.fr/hal-00426563/PDF/areaMethodRecapV2.pdf "elimination lemmas"
	 */
	@Override
	public AMExpression eliminate(Point pt, Vector<Boolean> isLemmaUsed, AreaMethodProver prover) {
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
			isLemmaUsed.set(5, true);
			Point u = ((AMIntersectionPoint)pt).getU();
			Point v = ((AMIntersectionPoint)pt).getV();
			Point p = ((AMIntersectionPoint)pt).getP();
			Point q = ((AMIntersectionPoint)pt).getQ();
			
			AMExpression supq = new AreaOfTriangle(u, p, q);
			AMExpression gv = new AreaOfTriangle(aa, bb, v);
			AMExpression term1 = new Product(supq, gv);
			AMExpression svpq = new AreaOfTriangle(v, p, q);
			AMExpression gu = new AreaOfTriangle(aa, bb, u);
			AMExpression term2 = new Product(svpq, gu);
			AMExpression numerator = new Difference(term1, term2);
			AMExpression supv = new AreaOfTriangle(u, p, v);
			AMExpression suvq = new AreaOfTriangle(u, v, q);
			AMExpression supvq = new Sum(supv, suvq);
			return new Fraction(numerator, supvq);
		}
		
		if (pt instanceof AMFootPoint) {
			isLemmaUsed.set(6, true);
			Point p = ((AMFootPoint)pt).getP();
			Point u = ((AMFootPoint)pt).getU();
			Point v = ((AMFootPoint)pt).getV();
			
			AMExpression ppuv = new PythagorasDifference(p, u, v);
			AMExpression gv = new AreaOfTriangle(aa, bb, v);
			AMExpression term1 = new Product(ppuv, gv);
			AMExpression ppvu = new PythagorasDifference(p, v, u);
			AMExpression gu = new AreaOfTriangle(aa, bb, u);
			AMExpression term2 = new Product(ppvu, gu);
			AMExpression numerator = new Sum(term1, term2);
			AMExpression denominator = new PythagorasDifference(u, v, u);
			return new Fraction(numerator, denominator);
		}
		
		if (pt instanceof PRatioPoint) {
			isLemmaUsed.set(7, true);
			Point w = ((PRatioPoint)pt).getW();
			Point u = ((PRatioPoint)pt).getU();
			Point v = ((PRatioPoint)pt).getV();
			AMExpression r = ((PRatioPoint)pt).getR();
			
			AMExpression gw = new AreaOfTriangle(aa, bb, w);
			AMExpression gu = new AreaOfTriangle(aa, bb, u);
			AMExpression gv = new AreaOfTriangle(aa, bb, v);
			AMExpression difference = new Difference(gv, gu);
			AMExpression product = new Product(r, difference);
			return new Sum(gw, product);
		}
		
		if (pt instanceof TRatioPoint) {
			isLemmaUsed.set(8, true);
			Point p = ((TRatioPoint)pt).getU();
			Point q = ((TRatioPoint)pt).getV();
			AMExpression r = ((TRatioPoint)pt).getR();
			
			AMExpression sabp = new AreaOfTriangle(aa, bb, p);
			AMExpression ppab = new PythagorasDifference(p, aa, bb);
			AMExpression pqab = new PythagorasDifference(q, aa, bb);
			AMExpression ppaqb = new Difference(ppab, pqab);
			AMExpression coeff = new Fraction(r, new BasicNumber(4));
			AMExpression product = new Product(coeff, ppaqb);
			return new Difference(sabp, product);
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
	public AMExpression toIndependantVariables(AreaMethodProver prover) throws UnknownStatementException {
		AMExpression firstTerm = new Product(new Difference(getY(b), getY(c)), getX(a));
		AMExpression secondTerm = new Product(new Difference(getY(c), getY(a)), getX(b));
		AMExpression thirdTerm = new Product(new Difference(getY(a), getY(b)), getX(c));
		AMExpression numerator = new Sum(firstTerm, new Sum(secondTerm, thirdTerm));
		return new Fraction(numerator, souv);
	}
	
	@Override
	public int size() {
		return 1;
	}
	
	@Override
	public AMExpression replace(HashMap<Point, Point> replacementMap) {
		if (replacementMap.containsKey(a))
			return new AreaOfTriangle(replacementMap.get(a), b, c).replace(replacementMap);
		if (replacementMap.containsKey(b))
			return new AreaOfTriangle(a, replacementMap.get(b), c).replace(replacementMap);
		if (replacementMap.containsKey(c))
			return new AreaOfTriangle(a, b, replacementMap.get(c)).replace(replacementMap);
		return this;
	}
	
	@Override
	public AMExpression toSumOfProducts() {
		return new SumOfProducts(new BigProduct(this));
	}
	
	@Override
	public double testValue(HashMap<String, FloatCoordinates> coords) {
		FloatCoordinates coordsA = coords.get(a.getGeoObjectLabel());
		FloatCoordinates coordsB = coords.get(b.getGeoObjectLabel());
		FloatCoordinates coordsC = coords.get(c.getGeoObjectLabel());
		double xa = coordsA.x;
		double xb = coordsB.x;
		double xc = coordsC.x;
		double ya = coordsA.y;
		double yb = coordsB.y;
		double yc = coordsC.y;
		return ((xb-xa)*(yc-ya) - (yb-ya)*(xc-xa))/2;
	}
}