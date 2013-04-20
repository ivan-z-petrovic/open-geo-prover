/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.multithread;

import java.util.Collection;
import java.util.Iterator;

import com.ogprover.polynomials.Polynomial;
import com.ogprover.polynomials.Term;


/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for critical section for a polynomial that is 
 *     result of concurrent multiplying of two polynomials</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public class SyncProduct {
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
	 * Lock for this object
	 */
	private boolean occupied;
	/**
	 * Resulting polynomial
	 */
	private Polynomial productPoly;
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param p	Resulting polynomial
	 */
	public SyncProduct(Polynomial p){
		this.productPoly = p;
		this.occupied = false;
	}
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Synchronous adding of a term to resulting polynomial.
	 * 
	 * @param t		Term to be added to resulting polynomial
	 */
	public synchronized void addTerm(Term t){
		while (this.occupied){
			try {
				this.wait(); // release lock and wait until notified by other thread
			}
			catch (InterruptedException e) {}
		}
		this.occupied = true;
		this.productPoly.addTerm(t);
		this.occupied = false;
		this.notifyAll();
	}
	
	/**
	 * Synchronous merging of a polynomial with resulting polynomial.
	 * 
	 * @param p		Polynomial to be merged with resulting polynomial
	 */
	public synchronized void mergePolynomial(Polynomial p){
		if (p == null)
			return;
		
		Collection<Term> col = p.getTerms().values();
		Iterator<Term> it = col.iterator();
		
		while (this.occupied){
			try {
				this.wait(); // release lock and wait until notified by other thread
			}
			catch (InterruptedException e) {}
		}
		this.occupied = true;
		
		while (it.hasNext())
			this.productPoly.addTerm(it.next());
		this.occupied = false;
		this.notifyAll();
	}
}