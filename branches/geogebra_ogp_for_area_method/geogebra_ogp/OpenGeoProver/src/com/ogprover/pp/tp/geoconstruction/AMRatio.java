/* 
 * DISCLAIMER PLACEHOLDER 
 */
package com.ogprover.pp.tp.geoconstruction;

/**
 * Temporary class, which will after discussion be either deleted or written.
 */
public class AMRatio {
	private int r;

	public AMRatio(){
		r = 0;
	}
	
	/**
	 * Constructor method 
	 * 
	 * @param r		Value of the ratio
	 */
	public AMRatio(int r) {
		this.r = r;
	}
	
	/**
	 * Constructor method, the ratio is then [ab]/[cd], where [ab] and [cd] are oriented parallel segments.
	 * @param a		Point
	 * @param b		Point
	 * @param c		Point
	 * @param d		Point
	 */
	public AMRatio(Point a, Point b, Point c, Point d) {
		// TODO
		r = 0;
	}
	
	/**
	 * Constructor method, the ratio is then p/q, where p and q are integers, and p is different from zero.
	 * @param p
	 * @param q
	 */
	public AMRatio(int p, int q) {
		// TODO
		r = 0;
	}
	public int getR() {
		return r;
	}
}
