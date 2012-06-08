/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.polynomials;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

import com.ogprover.main.OGPConstants;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.utilities.logger.ILogger;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Abstract class for polynomials of all types</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public abstract class Polynomial implements Cloneable, RationalAlgebraicExpression {
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
	// Class constants for two types of polynomials
	/**
	 * <i><b>Polynomial by u-variables</b></i>
	 */
	public static final short POLY_TYPE_UPOLY = 1;
	/**
	 * <i><b>Polynomial by x-variables</b></i>
	 */
	public static final short POLY_TYPE_XPOLY = 2;
	/**
	 * <i><b>Symbolic polynomial</b></i>
	 */
	public static final short POLY_TYPE_SYMBOLIC = 3;
	
	// Other class data members
	/**
	 * Collection of terms of same type that make this polynomial.
	 */
	protected TreeMap<Term, Term> terms; // TreeMap is used since it is implementation of red-black tree
	 									 // (a sort of self-balancing binary search tree that guarantees that
	 									 // main operations like containsKey, get, put and remove have 
										 // logarithmic time complexity).
	 									 // There are no two equals terms in this collection.
	 									 // Each term also represents itself's key - 
	 									 // therefore Term's compareTo() method is used for natural keys order.
	
	/*
	 * ======================================================================
	 * ========================= ABSTRACT METHODS ===========================
	 * ======================================================================
	 */
	/**
	 * Method that gives polynomial type
	 * 
	 * @return	Polynomial type (one of POLY_TYPE_xxx values)
	 */
	public abstract int getType();
	/**
	 * Method that creates copy of this polynomial
	 * 
	 * @see java.lang.Object#clone()
	 */
	public abstract Polynomial clone();
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method that gives collection of terms
	 * 
	 * @return	Collection of terms of this polynomial
	 */
	public TreeMap<Term, Term> getTerms() {
		return terms;
	}
	
	/**
	 * Check whether polynomial is zero constant.
	 * 
	 * @return	True if polynomial is zero, false otherwise.
	 */
	public final boolean isZero() {
		return this.terms.isEmpty(); // attention: zero terms will not be kept in tree of terms and that is why this condition is sufficient.
									 // please see addTerm() method from this class.
	}
	
	/**
	 * Method that gives all terms from polynomial in descending order.
	 * 
	 * @return		List of all terms in descending order.
	 * 				List contains references to objects from original tree of terms.
	 */
	public ArrayList<Term> getTermsAsDescList(){
		Collection<Term> col = this.terms.values(); // all values from tree in ascending order
		Iterator<Term> termIT = col.iterator();
		ArrayList<Term> list = new ArrayList<Term>();
		
		while (termIT.hasNext())
			list.add(0, termIT.next()); // add at the beginning of list

		return list;
	}
	
	/**
	 * Method that retrieves the greatest degree of all terms of this polynomial.
	 * It is called the polynomial degree.
	 * E.g. degree of polynomial x5 + x4^2*x2^2 + x3*x1 + 7 is equals to 4 and comes from 
	 * term x4^2*x2^2.
	 * 
	 * @return	Polynomial degree
	 */
	public int getPolynomialDegree() {
		int maxDegree = 0;
		
		for (Term t : this.getTermsAsDescList()) {
			int localDegree = 0;
			
			for (Power p : t.getPowers())
				localDegree += p.getExponent();
			
			if (maxDegree < localDegree)
				maxDegree = localDegree;
		}
		
		return maxDegree;
	}
	

	
	/*
	 * ======================================================================
	 * ======================= COMMON OBJECT METHODS ========================
	 * ======================================================================
	 */
	/**
	 * Method equals
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof Polynomial))
			return false;
		
		return this.clone().subtractPolynomial((Polynomial)obj).isZero();
	}
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	// Arithmetics operations with polynomials
	/**
	 * <b>[final method]</b><br>
	 * Method for adding new term to polynomial object.
	 * 
	 * @param t		Term to be added to polynomial.
	 * @return		This polynomial, which is result of operation
	 */
	public final Polynomial addTerm(Term t) {
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (t == null) {
			logger.error("Attempt to add null term.");
			return null;
		}
		
		if ((this.getType() == Polynomial.POLY_TYPE_UPOLY && t.getType() != Term.TERM_TYPE_UTERM) ||
			(this.getType() == Polynomial.POLY_TYPE_XPOLY && t.getType() != Term.TERM_TYPE_XTERM) ||
			(this.getType() == Polynomial.POLY_TYPE_SYMBOLIC && t.getType() != Term.TERM_TYPE_SYMBOLIC)) {
			logger.error("Attempt to add term of another type.");
			return null;
		}
		
		// don't add zero term to collection
		if (t.isZero() == true) {
			logger.warn("Zero term is not added to collection.");
			return this;
		}
		
		Term tFromTree = this.terms.get(t);
		
		if (tFromTree == null) { // this is a brand new term
			this.terms.put(t, t);
		}
		else {
			tFromTree.merge(t); // merging existing term with new term
			// if became zero, remove from collection
			if (tFromTree.isZero() == true)
				this.terms.remove(tFromTree);
		}
		
		return this;
	}
	
	/**
	 * <b>[final method]</b><br>
	 * Addition of two polynomials
	 * 
	 * @param p		Polynomial which is added to current polynomial
	 * @return		This polynomial, which is result of operation
	 */
	public final Polynomial addPolynomial(Polynomial p) {
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (p == null) {
			logger.error("Attempt to add null polynomial.");
			return null;
		}
		
		if (this.getType() != p.getType()) {
			logger.error("Attempt to add polynomial of another type.");
			return null;
		}
		
		// addition of zero polynomial - nothing is changed
		if (p.isZero())
			return this;
		
		Collection<Term> col = p.getTerms().values(); // all values from tree in ascending order
		Iterator<Term> termIT = col.iterator();
		
		while (termIT.hasNext())
			this.addTerm(termIT.next().clone());
		
		return this;
	}
	
	/**
	 * <b>[final method]</b><br>
	 * Subtraction of two polynomials
	 * 
	 * @param p		Polynomial which is subtracted from current polynomial
	 * @return		This polynomial, which is result of operation
	 */
	public final Polynomial subtractPolynomial(Polynomial p) {
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (p == null) {
			logger.error("Attempt to subtract null polynomial.");
			return null;
		}
		
		if (this.getType() != p.getType()) {
			logger.error("Attempt to subtract polynomial of another type.");
			return null;
		}
		
		Collection<Term> col = p.getTerms().values(); // all values from tree in ascending order
		Iterator<Term> termIT = col.iterator();
		
		while (termIT.hasNext())
			this.addTerm(termIT.next().clone().invert());
		
		return this;
	}
	
	/**
	 * <b>[final method]</b><br>
	 * Method for multiplication of this polynomial by real constant.
	 * 
	 * @param d		Real constant - factor
	 * @return		Product of this polynomial and real factor
	 */
	public final Polynomial multiplyByRealConstant(double d){
		// if this polynomial is zero - nothing is changed
		if (this.isZero())
			return this;
		
		if (d > -OGPConstants.EPSILON && d < OGPConstants.EPSILON) {
			// multiplication by zero - therefore new polynomial is zero
			this.terms = new TreeMap<Term, Term>(); // new empty tree of terms
		}
		else {
			// multiply each term by real constant
			Collection<Term> col = this.terms.values(); // all values from tree in ascending order
			Iterator<Term> termIT = col.iterator();
		
			while (termIT.hasNext()) {
				// update this term in tree
				termIT.next().mul(d);
			}
		}
		
		return this;
	}
	
	/**
	 * <b>[final method]</b><br>
	 * Method for multiplication of this polynomial by term.
	 * 
	 * @param t		Term - factor
	 * @return		Product of this polynomial and term
	 */
	public final Polynomial multiplyByTerm(Term t){
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (t == null) {
			logger.error("Attempt to multiply by null term.");
			return null;
		}
		
		if ((this.getType() == Polynomial.POLY_TYPE_UPOLY && t.getType() != Term.TERM_TYPE_UTERM) ||
			(this.getType() == Polynomial.POLY_TYPE_XPOLY && t.getType() != Term.TERM_TYPE_XTERM) ||
			(this.getType() == Polynomial.POLY_TYPE_SYMBOLIC && t.getType() != Term.TERM_TYPE_SYMBOLIC)) {
			logger.error("Attempt to multiply by term of another type.");
			return null;
		}
		
		// if this polynomial is zero - nothing is changed
		if (this.isZero())
			return this;
		
		Collection<Term> col = this.terms.values(); // all values from tree in ascending order
		Iterator<Term> termIT = col.iterator();
		
		// if term is zero constant - result is zero polynomial
		if (t.isZero() == true)
			this.terms = new TreeMap<Term, Term>(); // new empty tree of terms
		else {
			// multiply each term by passed in term - order will be same
			while (termIT.hasNext()) {
				termIT.next().mul(t);
			}
		}
		
		return this;
	}
	
	/**
	 * <b>[final method]</b><br>
	 * Method for multiplication of this polynomial by another polynomial.
	 * 
	 * @param p		Polynomial - factor
	 * @return		Product of this polynomial and passed in polynomial
	 */
	public final Polynomial multiplyByPolynomial(Polynomial p){
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (p == null) {
			logger.error("Attempt to multiply by null polynomial.");
			return null;
		}
		
		if (this.getType() != p.getType()) {
			logger.error("Attempt to multiply by polynomial of another type.");
			return null;
		}
		
		// if this polynomial is zero - nothing is changed
		if (this.isZero())
			return this;
		
		Collection<Term> col = this.terms.values(); // all values from tree in ascending order
		Iterator<Term> termIT = col.iterator();
		Collection<Term> colP = p.getTerms().values(); // all values from tree in ascending order
		Iterator<Term> termITP = colP.iterator();
		
		//long numIterations = 0; // used for better memory management
		
		// if passed in polynomial is zero constant - result is zero polynomial
		this.terms = new TreeMap<Term, Term>(); // new empty tree of terms
		if (p.isZero() == false) {
			while (termIT.hasNext()) {
				Term curr = termIT.next();

				while (termITP.hasNext()){
					//numIterations++; // used for better memory management
					
					Term currP = termITP.next().clone();
					currP.mul(curr);
					this.addTerm(currP); // must use addTerm() method instead of put
					 					 // because some other two factors can produce
					 					 // same product so these terms must be merged
					/*
					currP = null;
					
					if (numIterations > 1000) {
						Runtime.getRuntime().gc();
						numIterations = 0;
					}
					*/ // used for better memory management
				}
				termITP = colP.iterator();
			}
		}
		
		return this;
	}
	
	/**
	 * <b>[final method]</b><br>
	 * Inversion of polynomial (changing of sign) - each term is inverted.
	 * 
	 * @return		This polynomial, which is result of operation
	 */
	public final Polynomial invert() {
		Collection<Term> col = this.terms.values(); // all values from tree in ascending order
		Iterator<Term> termIT = col.iterator();

		while (termIT.hasNext()) { 
			termIT.next().invert(); // updates original term with its inverted version
		}
		
		return this;
	}
	
	/**
	 * <b>[final method]</b><br>
	 * Method that gives leading term of this polynomial.
	 * 
	 * @return		Leading (highest) term of this polynomial.
	 */
	public final Term getLeadingTerm(){
		return this.terms.get(this.terms.lastKey());
	}
}