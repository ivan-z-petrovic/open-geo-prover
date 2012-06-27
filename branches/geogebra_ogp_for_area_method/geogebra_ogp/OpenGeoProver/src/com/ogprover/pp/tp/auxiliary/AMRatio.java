/* 
 * DISCLAIMER PLACEHOLDER 
 */
package com.ogprover.pp.tp.auxiliary;

import java.util.HashSet;

import com.ogprover.pp.tp.geoconstruction.FreePoint;
import com.ogprover.pp.tp.geoconstruction.Point;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for representing the ration between two collinear segments,
 * 		for the area method algorithm.</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Damien Desfontaines
 */
public class AMRatio extends AMExpression {
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
	 * Four Point variables A, B, C and D, to represent AB/CD.
	 */
	protected Point a,b,c,d; 

	
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
	public Point getD() {
		return d;
	}
	
	
	/**
	 * @see com.ogprover.pp.tp.auxiliary.AMExpression#getPoints()
	 */
	public HashSet<Point> getPoints() {
		HashSet<Point> points = new HashSet<Point>();
		points.add(a);
		points.add(b);
		points.add(c);
		points.add(d);
		return points;
	}
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method, the ratio is then [ab]/[cd], where [ab] and [cd] are oriented parallel segments.
	 * @param a		Point
	 * @param b		Point
	 * @param c		Point
	 * @param d		Point
	 */
	public AMRatio(Point a, Point b, Point c, Point d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
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
		s.append(a.getGeoObjectLabel());
		s.append(b.getGeoObjectLabel());
		s.append("/");
		s.append(c.getGeoObjectLabel());
		s.append(d.getGeoObjectLabel());
		return s.toString();
	}
	
	@Override
	public boolean equals(AMExpression expr) {
		if (!(expr instanceof AMRatio))
			return false;
		AMRatio ratio = (AMRatio)expr;
		return (a.equals(ratio.getA()) && b.equals(ratio.getB()) && c.equals(ratio.getC()) && d.equals(ratio.getD()));
	}
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	@Override
	public boolean containsOnlyFreePoints() {
		if (a instanceof FreePoint && b instanceof FreePoint && c instanceof FreePoint && d instanceof FreePoint) {
			return true;
		}
		return false;
	}
	
	@Override
	public AMExpression uniformize() {
		if (a.compare(b)) {
			if (c.compare(d)) {
				return this;
			}
			return new AMAdditiveInverse(new AMRatio(a,b,d,c));
		}
		if (c.compare(d)) {
			return new AMAdditiveInverse(new AMRatio(b,a,c,d));
		}
		return new AMRatio(b,a,d,c);
	}
	
	@Override
	public AMExpression eliminate(Point pt) {
		// TODO write the elimination lemmas for the ratios
		System.out.println("Not yet implemented");
		return null;
	}
	
	@Override
	public AMExpression reduceToSingleFraction() {
		return this;
	}
	@Override
	public AMExpression reductToRightAssociativeForm() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public AMExpression toIndependantVariables() {
		// TODO write the transformation to independant variables for the ratios
		System.out.println("Not yet implemented");
		return null;
	}
}
