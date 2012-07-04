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
 * <dd>Class for representing the Pythagoras difference between three points 
 * 		for the area method. If the three points are A, B and C, then this 
 * 		difference equals AB² + BC² - AC². Then, its value is 0 iff ABC is a
 * 		right angle.</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Damien Desfontaines
 */
public class AMPythagorasDifference extends AMExpression {
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
	public AMPythagorasDifference(Point a, Point b, Point c) {
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
		s.append("P_");
		s.append(a.getGeoObjectLabel());
		s.append(b.getGeoObjectLabel());
		s.append(c.getGeoObjectLabel());
		return s.toString();
	}
	
	@Override
	public boolean equals(Object expr) {
		if (!(expr instanceof AMPythagorasDifference))
			return false;
		AMPythagorasDifference diff = (AMPythagorasDifference)expr;
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
	public AMExpression uniformize() {
		if (c.compare(b) && b.compare(a)) {
			return new AMPythagorasDifference(c, b, a);
		}
		if (c.equals(a) && b.compare(a)) {
			return new AMPythagorasDifference(b, a, b);
		}
		return this;
	}
	
	@Override
	public AMExpression simplifyInOneStep() {
		if (a.equals(b) || b.equals(c))
			return new AMNumber(0);
		return this;
	}
	
	/**
	 * See http://hal.inria.fr/hal-00426563/PDF/areaMethodRecapV2.pdf "elimination lemmas"
	 */
	@Override
	public AMExpression eliminate(Point pt, AreaMethodProver prover) {
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
				Point u = ((AMIntersectionPoint)pt).getU();
				Point v = ((AMIntersectionPoint)pt).getV();
				Point p = ((AMIntersectionPoint)pt).getP();
				Point q = ((AMIntersectionPoint)pt).getQ();
				
				AMExpression supq = new AMPythagorasDifference(u, p, q);
				AMExpression gv = new AMPythagorasDifference(aa, bb, v);
				AMExpression term1 = new AMProduct(supq, gv);
				AMExpression svpq = new AMPythagorasDifference(v, p, q);
				AMExpression gu = new AMPythagorasDifference(aa, bb, u);
				AMExpression term2 = new AMProduct(svpq, gu);
				AMExpression numerator = new AMDifference(term1, term2);
				AMExpression supv = new AMPythagorasDifference(u, p, v);
				AMExpression spvq = new AMPythagorasDifference(p, v, q);
				AMExpression denominator = new AMSum(supv, spvq);
				return new AMFraction(numerator, denominator);
			}
			
			if (pt instanceof AMFootPoint) {
				Point p = ((AMFootPoint)pt).getP();
				Point u = ((AMFootPoint)pt).getU();
				Point v = ((AMFootPoint)pt).getV();
				
				AMExpression ppuv = new AMPythagorasDifference(p, u, v);
				AMExpression gv = new AMPythagorasDifference(aa, bb, v);
				AMExpression term1 = new AMProduct(ppuv, gv);
				AMExpression ppvu = new AMPythagorasDifference(p, v, u);
				AMExpression gu = new AMPythagorasDifference(aa, bb, u);
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
				
				AMExpression gw = new AMPythagorasDifference(aa, bb, w);
				AMExpression gu = new AMPythagorasDifference(aa, bb, u);
				AMExpression gv = new AMPythagorasDifference(aa, bb, v);
				AMExpression difference = new AMDifference(gv, gu);
				AMExpression product = new AMProduct(r, difference);
				return new AMSum(gw,product);
			}
			
			if (pt instanceof TRatioPoint) {
				Point p = ((TRatioPoint)pt).getU();
				Point q = ((TRatioPoint)pt).getV();
				AMExpression r = ((TRatioPoint)pt).getR();
				
				AMExpression pabp = new AMPythagorasDifference(aa, bb, p);
				AMExpression spaq = new AMPythagorasDifference(p, aa, q);
				AMExpression saqb = new AMPythagorasDifference(aa, q, bb);
				AMExpression spaqb = new AMSum(spaq, saqb);
				AMExpression coeff = new AMProduct(r, new AMNumber(4));
				AMExpression product = new AMProduct(coeff, spaqb);
				return new AMDifference(pabp, product);
			}
		} else {
			if (pt instanceof AMIntersectionPoint) {
				Point u = ((AMIntersectionPoint)pt).getU();
				Point v = ((AMIntersectionPoint)pt).getV();
				Point p = ((AMIntersectionPoint)pt).getP();
				Point q = ((AMIntersectionPoint)pt).getQ();
				
				AMExpression spuv = new AMPythagorasDifference(p, u, v);
				AMExpression spuq = new AMPythagorasDifference(p, u, q);
				AMExpression suqv = new AMPythagorasDifference(u, q, v);
				AMExpression spuqv = new AMSum(spuq, suqv);
				AMExpression paqb = new AMPythagorasDifference(aa, q, bb);
				AMExpression sqvu = new AMPythagorasDifference(q, v, u);
				AMExpression papb = new AMPythagorasDifference(aa, p, bb);
				AMExpression ppqp = new AMPythagorasDifference(p, q, p);
				AMExpression frac1 = new AMFraction(spuv, spuqv);
				AMExpression frac2 = new AMFraction(sqvu, spuqv);
				AMExpression term1 = new AMProduct(frac1, paqb);
				AMExpression term2 = new AMProduct(frac2, papb);
				AMExpression term3 = new AMProduct(new AMProduct(frac1, frac2), ppqp);
				return new AMDifference(new AMSum(term1, term2), term3);
				
			}
			
			if (pt instanceof AMFootPoint) {
				Point p = ((AMFootPoint)pt).getP();
				Point u = ((AMFootPoint)pt).getU();
				Point v = ((AMFootPoint)pt).getV();
				
				AMExpression ppuv = new AMPythagorasDifference(p, u, v);
				AMExpression puvu = new AMPythagorasDifference(u, v, u);
				AMExpression ppvu = new AMPythagorasDifference(p, v, u);
				AMExpression pavb = new AMPythagorasDifference(aa, v, bb);
				AMExpression paub = new AMPythagorasDifference(aa, u, bb);
				AMExpression term1 = new AMProduct(new AMFraction(ppuv, puvu), pavb);
				AMExpression term2 = new AMProduct(new AMFraction(ppvu, puvu), paub);
				AMExpression term3 = new AMFraction(new AMProduct(ppuv, ppvu), puvu);
				return new AMSum(term1, new AMDifference(term2, term3));
			}
			
			if (pt instanceof PRatioPoint) {
				Point w = ((PRatioPoint)pt).getW();
				Point u = ((PRatioPoint)pt).getU();
				Point v = ((PRatioPoint)pt).getV();
				AMExpression r = ((PRatioPoint)pt).getR();
				
				AMExpression pawb = new AMPythagorasDifference(aa, w, bb);
				AMExpression pavb = new AMPythagorasDifference(aa, v, bb);
				AMExpression paub = new AMPythagorasDifference(aa, u, bb);
				AMExpression pwuv = new AMPythagorasDifference(w, u, v);
				AMExpression puvu = new AMPythagorasDifference(u, v, u);
				AMExpression bloc1 = new AMSum(new AMDifference(pavb, paub), new AMProduct(new AMNumber(2), pwuv));
				AMExpression term1 = new AMProduct(r, bloc1);
				AMExpression coeff = new AMProduct(r, new AMDifference(new AMNumber(1), r));
				AMExpression term2 = new AMProduct(coeff, puvu);
				return new AMSum(pawb, new AMDifference(term1, term2));
			}
			
			if (pt instanceof TRatioPoint) {
				Point p = ((TRatioPoint)pt).getU();
				Point q = ((TRatioPoint)pt).getV();
				AMExpression r = ((TRatioPoint)pt).getR();
				
				AMExpression papb = new AMPythagorasDifference(aa, p, bb);
				AMExpression ppqp = new AMPythagorasDifference(p, q, p);
				AMExpression sapq = new AMAreaOfTriangle(aa, p, q);
				AMExpression sbpq = new AMAreaOfTriangle(bb, p, q);
				AMExpression term1 = new AMProduct(new AMProduct(r, r), ppqp);
				AMExpression term2 = new AMProduct(new AMProduct(new AMNumber(4), r), new AMSum(sapq, sbpq));
				return new AMSum(papb, new AMDifference(term1, term2));
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
	public AMExpression reductToRightAssociativeForm() {
		return this;
	}
	
	@Override
	public AMExpression toIndependantVariables(AreaMethodProver prover) throws UnknownStatementException {
		AMExpression term1 = new AMProduct(getY(a), getY(c));
		AMExpression term2 = new AMProduct(new AMNumber(-1), new AMProduct(getY(a), getY(b)));
		AMExpression term3 = new AMProduct(getY(b), getY(b));
		AMExpression term4 = new AMProduct(new AMNumber(-1), new AMProduct(getY(b), getY(c)));
		AMExpression term5 = new AMProduct(new AMNumber(-1), new AMProduct(getX(a), getX(b)));
		AMExpression term6 = new AMProduct(getX(a), getX(c));
		AMExpression term7 = new AMProduct(getX(b), getX(b));
		AMExpression term8 = new AMProduct(new AMNumber(-1), new AMProduct(getX(b), getX(c)));
		AMExpression firstPart = new AMSum(term1, new AMSum(term2, new AMSum(term3, term4)));
		AMExpression secondPart = new AMSum(term5, new AMSum(term6, new AMSum(term7, term8)));
		AMExpression numerator = new AMSum(firstPart, secondPart);
		AMExpression denominator = new AMProduct(souv, souv);
		return new AMProduct(new AMNumber(4), new AMFraction(numerator, denominator));
	}
	
	@Override
	public int size() {
		return 1;
	}
}