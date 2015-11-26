/* 
 * DISCLAIMER PLACEHOLDER 
 */
package com.ogprover.pp.tp.expressions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

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
 * <dd>Class for representing the Pythagoras difference between three points 
 * 		for the area method. If the three points are A, B and C, then this 
 * 		difference equals AB² + BC² - AC². Then, its value is 0 iff ABC is a
 * 		right angle.</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Damien Desfontaines
 */
public class PythagorasDifference extends GeometricQuantity {
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
	public PythagorasDifference(Point a, Point b, Point c) {
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
		s.append("P_");
		s.append(a.getGeoObjectLabel());
		s.append(b.getGeoObjectLabel());
		s.append(c.getGeoObjectLabel());
		return s.toString();
	}
	
	@Override
	public boolean equals(Object expr) {
		if (!(expr instanceof PythagorasDifference))
			return false;
		PythagorasDifference diff = (PythagorasDifference)expr;
		return (a.equals(diff.getA()) && b.equals(diff.getB()) && c.equals(diff.getC()));
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
		if (a.equals(b) || b.equals(c))
			return new BasicNumber(0);
		if (c.compare(a)) {
			return new PythagorasDifference(c, b, a);
		}
		if (c.equals(a) && b.compare(a)) {
			return new PythagorasDifference(b, a, b);
		}
		return this;
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
		boolean islinear = true;
		
		if (pt.equals(c)) {
			aa = a;
			bb = b;
		}
		else if (pt.equals(b)) {
			aa = a;
			bb = c;
			islinear = false;
		}
		else if (pt.equals(a)) {
			aa = c;
			bb = b;
		}
		else
			return this;
		
		if (islinear) {
			if (pt instanceof AMIntersectionPoint) {
				isLemmaUsed.set(14, true);
				Point u = ((AMIntersectionPoint)pt).getU();
				Point v = ((AMIntersectionPoint)pt).getV();
				Point p = ((AMIntersectionPoint)pt).getP();
				Point q = ((AMIntersectionPoint)pt).getQ();
				
				AMExpression supq = new AreaOfTriangle(u, p, q);
				AMExpression gv = new PythagorasDifference(aa, bb, v);
				AMExpression term1 = new Product(supq, gv);
				AMExpression svpq = new AreaOfTriangle(v, p, q);
				AMExpression gu = new PythagorasDifference(aa, bb, u);
				AMExpression term2 = new Product(svpq, gu);
				AMExpression numerator = new Difference(term1, term2);
				AMExpression supv = new AreaOfTriangle(u, p, v);
				AMExpression suvq = new AreaOfTriangle(u, v, q);
				AMExpression supvq = new Sum(supv, suvq);
				return new Fraction(numerator, supvq);
			}
			
			if (pt instanceof AMFootPoint) {
				isLemmaUsed.set(15, true);
				Point p = ((AMFootPoint)pt).getP();
				Point u = ((AMFootPoint)pt).getU();
				Point v = ((AMFootPoint)pt).getV();
				
				AMExpression ppuv = new PythagorasDifference(p, u, v);
				AMExpression gv = new PythagorasDifference(aa, bb, v);
				AMExpression term1 = new Product(ppuv, gv);
				AMExpression ppvu = new PythagorasDifference(p, v, u);
				AMExpression gu = new PythagorasDifference(aa, bb, u);
				AMExpression term2 = new Product(ppvu, gu);
				AMExpression numerator = new Sum(term1, term2);
				AMExpression denominator = new PythagorasDifference(u, v, u);
				return new Fraction(numerator, denominator);
			}
			
			if (pt instanceof PRatioPoint) {
				isLemmaUsed.set(16, true);
				Point w = ((PRatioPoint)pt).getW();
				Point u = ((PRatioPoint)pt).getU();
				Point v = ((PRatioPoint)pt).getV();
				AMExpression r = ((PRatioPoint)pt).getR();
				
				AMExpression gw = new PythagorasDifference(aa, bb, w);
				AMExpression gu = new PythagorasDifference(aa, bb, u);
				AMExpression gv = new PythagorasDifference(aa, bb, v);
				AMExpression difference = new Difference(gv, gu);
				AMExpression product = new Product(r, difference);
				return new Sum(gw, product);
			}
			
			if (pt instanceof TRatioPoint) {
				isLemmaUsed.set(9, true);
				Point p = ((TRatioPoint)pt).getU();
				Point q = ((TRatioPoint)pt).getV();
				AMExpression r = ((TRatioPoint)pt).getR();
				
				AMExpression pabp = new PythagorasDifference(aa, bb, p);
				AMExpression spaq = new AreaOfTriangle(p, aa, q);
				AMExpression spqb = new AreaOfTriangle(p, q, bb);
				AMExpression spaqb = new Sum(spaq, spqb);
				AMExpression coeff = new Product(r, new BasicNumber(4));
				AMExpression product = new Product(coeff, spaqb);
				return new Difference(pabp, product);
			}
		} else {
			if (pt instanceof AMIntersectionPoint) {
				isLemmaUsed.set(10, true);
				Point u = ((AMIntersectionPoint)pt).getU();
				Point v = ((AMIntersectionPoint)pt).getV();
				Point p = ((AMIntersectionPoint)pt).getP();
				Point q = ((AMIntersectionPoint)pt).getQ();
				
				/*
				AMExpression spuv = new AreaOfTriangle(p, u, v);
				AMExpression spuq = new AreaOfTriangle(p, u, q);
				AMExpression spqv = new AreaOfTriangle(p, q, v);
				AMExpression spuqv = new Sum(spuq, spqv);
				AMExpression paqb = new PythagorasDifference(aa, q, bb);
				AMExpression sqvu = new AreaOfTriangle(q, v, u);
				AMExpression papb = new PythagorasDifference(aa, p, bb);
				AMExpression ppqp = new PythagorasDifference(p, q, p);
				AMExpression frac1 = new Fraction(spuv, spuqv);
				AMExpression frac2 = new Fraction(sqvu, spuqv);
				AMExpression term1 = new Product(frac1, paqb);
				AMExpression term2 = new Product(frac2, papb);
				AMExpression term3 = new Product(new Product(frac1, frac2), ppqp);
				return new Difference(new Sum(term1, term2), term3);
				*/
				AMExpression supv = new AreaOfTriangle(u, p, v);
				AMExpression suvq = new AreaOfTriangle(u, v, q);
				AMExpression supvq = new Sum(supv, suvq);
				AMExpression supq = new AreaOfTriangle(u, p, q);
				AMExpression fraction1 = new Fraction(supq, supvq);
				AMExpression svpq = new AreaOfTriangle(v, p, q);
				AMExpression fraction2 = new Fraction(svpq, supvq);
				AMExpression gv = new PythagorasDifference(a, v, b);
				AMExpression term1 = new Product(fraction1, gv);
				AMExpression gu = new PythagorasDifference(a, u, b);
				AMExpression term2 = new Product(fraction2, gu);
				AMExpression puvu = new PythagorasDifference(u, v, u);
				AMExpression term3 = new Product(fraction1, new Product(fraction2, puvu));
				return new Difference(new Sum(term1, term2), term3);
			}
			if (pt instanceof AMFootPoint) {
				isLemmaUsed.set(11, true);
				Point p = ((AMFootPoint)pt).getP();
				Point u = ((AMFootPoint)pt).getU();
				Point v = ((AMFootPoint)pt).getV();
				
				AMExpression ppuv = new PythagorasDifference(p, u, v);
				AMExpression puvu = new PythagorasDifference(u, v, u);
				AMExpression ppvu = new PythagorasDifference(p, v, u);
				AMExpression pavb = new PythagorasDifference(aa, v, bb);
				AMExpression paub = new PythagorasDifference(aa, u, bb);
				AMExpression term1 = new Product(new Fraction(ppuv, puvu), pavb);
				AMExpression term2 = new Product(new Fraction(ppvu, puvu), paub);
				AMExpression term3 = new Fraction(new Product(ppuv, ppvu), puvu);
				return new Sum(term1, new Difference(term2, term3));
			}
			if (pt instanceof PRatioPoint) {
				isLemmaUsed.set(12, true);
				Point w = ((PRatioPoint)pt).getW();
				Point u = ((PRatioPoint)pt).getU();
				Point v = ((PRatioPoint)pt).getV();
				AMExpression r = ((PRatioPoint)pt).getR();
				
				AMExpression pawb = new PythagorasDifference(aa, w, bb);
				AMExpression pavb = new PythagorasDifference(aa, v, bb);
				AMExpression paub = new PythagorasDifference(aa, u, bb);
				AMExpression pwuv = new PythagorasDifference(w, u, v);
				AMExpression puvu = new PythagorasDifference(u, v, u);
				AMExpression bloc1 = new Sum(new Difference(pavb, paub), new Product(new BasicNumber(2), pwuv));
				AMExpression term1 = new Product(r, bloc1);
				AMExpression coeff = new Product(r, new Difference(new BasicNumber(1), r));
				AMExpression term2 = new Product(coeff, puvu);
				return new Sum(pawb, new Difference(term1, term2));
			}
			if (pt instanceof TRatioPoint) {
				isLemmaUsed.set(13, true);
				Point p = ((TRatioPoint)pt).getU();
				Point q = ((TRatioPoint)pt).getV();
				AMExpression r = ((TRatioPoint)pt).getR();
				
				AMExpression papb = new PythagorasDifference(aa, p, bb);
				AMExpression ppqp = new PythagorasDifference(p, q, p);
				AMExpression sapq = new AreaOfTriangle(aa, p, q);
				AMExpression sbpq = new AreaOfTriangle(bb, p, q);
				AMExpression term1 = new Product(new Product(r, r), ppqp);
				AMExpression term2 = new Product(new Product(new BasicNumber(4), r), new Sum(sapq, sbpq));
				return new Sum(papb, new Difference(term1, term2));
			}
		}
		
		// Theoretically, pt cannot be a free point
		if (pt instanceof FreePoint) {
			System.out.println("Trying to eliminate the free point" + pt.getGeoObjectLabel());
			return null;
		}
		
		System.out.println("Th point " + pt.getGeoObjectLabel() + "has not been generated using the area method");
		return null;
	}
	
	@Override
	public AMExpression reduceToSingleFraction() {
		return this;
	}
	
	@Override
	public AMExpression toIndependantVariables(AreaMethodProver prover) throws UnknownStatementException {
		AMExpression term1 = new Product(getY(a), getY(c));
		AMExpression term2 = new Product(new BasicNumber(-1), new Product(getY(a), getY(b)));
		AMExpression term3 = new Product(getY(b), getY(b));
		AMExpression term4 = new Product(new BasicNumber(-1), new Product(getY(b), getY(c)));
		AMExpression term5 = new Product(new BasicNumber(-1), new Product(getX(a), getX(b)));
		AMExpression term6 = new Product(getX(a), getX(c));
		AMExpression term7 = new Product(getX(b), getX(b));
		AMExpression term8 = new Product(new BasicNumber(-1), new Product(getX(b), getX(c)));
		AMExpression firstPart = new Sum(term1, new Sum(term2, new Sum(term3, term4)));
		AMExpression secondPart = new Sum(term5, new Sum(term6, new Sum(term7, term8)));
		AMExpression numerator = new Sum(firstPart, secondPart);
		AMExpression denominator = new Product(souv, souv);
		return new Product(new BasicNumber(4), new Fraction(numerator, denominator));
	}
	
	@Override
	public int size() {
		return 1;
	}
	
	@Override
	public AMExpression replace(HashMap<Point, Point> replacementMap) {
		if (replacementMap.containsKey(a))
			return new PythagorasDifference(replacementMap.get(a), b, c).replace(replacementMap);
		if (replacementMap.containsKey(b))
			return new PythagorasDifference(a, replacementMap.get(b), c).replace(replacementMap);
		if (replacementMap.containsKey(c))
			return new PythagorasDifference(a, b, replacementMap.get(c)).replace(replacementMap);
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
		return (xb-xa)*(xb-xa) + (yb-ya)*(yb-ya) + (xb-xc)*(xb-xc) + (yb-yc)*(yb-yc) - ((xa-xc)*(xa-xc) + (ya-yc)*(ya-yc));
	}
}