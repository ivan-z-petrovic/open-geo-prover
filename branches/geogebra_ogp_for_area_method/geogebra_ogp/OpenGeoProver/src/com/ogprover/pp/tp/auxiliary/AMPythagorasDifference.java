/* 
 * DISCLAIMER PLACEHOLDER 
 */
package com.ogprover.pp.tp.auxiliary;

import java.util.HashSet;

import com.ogprover.pp.tp.geoconstruction.Point;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for representing the Pythagoras difference between three points 
 * 		for the area method. If the three points are A, B and C, then this 
 * 		difference equals AB² + BC² - AC². Then, its value is 0 iff ABC is a
 * 		right angle.</dd>
 * </dl>
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
}