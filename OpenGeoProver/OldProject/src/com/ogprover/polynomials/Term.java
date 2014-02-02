/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.polynomials;

import java.util.Vector;

import com.ogprover.main.OpenGeoProver;
import com.ogprover.utilities.logger.ILogger;


/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Base abstract class for all terms (products of powers and coefficient)</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public abstract class Term implements Comparable<Term>, Cloneable, RationalAlgebraicExpression {
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
	// Class constants for two types of terms
	/**
	 * Term for u-variables
	 */
	public static final short TERM_TYPE_UTERM = 1;
	/**
	 * Term for x-variables
	 */
	public static final short TERM_TYPE_XTERM = 2;
	/**
	 * Term of symbolic powers
	 */
	public static final short TERM_TYPE_SYMBOLIC = 3;
	
	// Other class members
	/**
	 * List of powers that make term
	 */
	protected Vector<Power> powers; // Very important assumptions:
									// 1. all powers in this vector are of same variable type and it matches the type of term 
									// (TERM_TYPE_XTERM contains powers of VAR_TYPE_X and similar for UTERM/U)
									// 2. this vector is sorted in descending order by powers, as defined by their compare method
									// (e.g. correct order in this vector is [x_9, (x_6)^2, (x_5)^4, x_2] or [(u_4)^3, u_3, (u_1)^7] etc.)
	
	/*
	 * ======================================================================
	 * ========================= ABSTRACT METHODS ===========================
	 * ======================================================================
	 */
	/**
	 * Clone method
	 * 
	 * @see java.lang.Object#clone()
	 */
	public abstract Term clone();
	/**
	 * Method that gives term type
	 * 
	 * @return	Term type (one of TERM_TYPE_xxx values)
	 */
	public abstract int getType();
	/**
	 * Method to merge this term with equal term
	 * 
	 * @param t	Term to merge with this term
	 * @return	This term which is result of operation
	 */
	public abstract Term merge(Term t);
	/**
	 * Method to multiply this term by another term
	 * 
	 * @param t	Term to multiply this term by
	 * @return	This term which is result of operation
	 */
	public abstract Term mul(Term t);
	/**
	 * Method to multiply this term by a real coefficient
	 * 
	 * @param r	A real coefficient to multiply this term by
	 * @return	This term which is result of operation
	 */
	public abstract Term mul(double r);
	/**
	 * Method to divide this term by another
	 * 
	 * @param t	Term to divide this term by
	 * @return	This term which is result of operation
	 */
	public abstract Term divide(Term t);
	/**
	 * Method for inversion of this term (changing the sign)
	 * 
	 * @return	This term which is result of operation
	 */
	public abstract Term invert();
	/**
	 * Method to check whether this term is zero constant by its variable
	 * 
	 * @return	True if term is zero and false otherwise
	 */
	public abstract boolean isZero();
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method that gives collection of powers
	 * 
	 * @return	Powers of this term
	 */
	public Vector<Power> getPowers() {
		return this.powers;
	}
	
	
	
	/*
	 * ======================================================================
	 * ======================= COMMON OBJECT METHODS ========================
	 * ======================================================================
	 */
	/** CompareTo method
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	// Comparison of two terms - vectors of powers are sorted in descending 
	// order in both terms, therefore compare one by one power.
	public int compareTo(Term t) {
		int size = this.powers.size(), 
		tsize = ((t != null) ? t.getPowers().size() : 0);
		int ii = 0;
		ILogger logger = OpenGeoProver.settings.getLogger();

		if (t == null) {
			logger.error("Null term passed in.");
			return -2; // error
		}
	
		// default comparison of terms with different type - theoretical case
		if (this.getType() != t.getType())
			return this.getType() - t.getType();
	
		while (ii < size && ii < tsize) {
			Power p = this.powers.get(ii);
			Power q = t.getPowers().get(ii);
		
			if (p == null || q == null) {
				logger.error("Found null object(s) when expected non-null value");
				return -2;
			}
		
			int cmp = p.compareTo(q);
			if (cmp != 0)
				return cmp;
			ii++;
		}
	
		return ((size < tsize) ? -1 : ((size > tsize) ? 1 : 0));
	}
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Adds power to collection and keeps the sorting order.
	 * 
	 * @param p		Power to be added into collection
	 */
	public void addPower(Power p) {
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		// If power is of another variable type, don't add it into collection
		if ((this.getType() == Term.TERM_TYPE_UTERM && p.getVarType() == Variable.VAR_TYPE_UX_U) ||
			(this.getType() == Term.TERM_TYPE_XTERM && p.getVarType() == Variable.VAR_TYPE_UX_X) ||
			(this.getType() == Term.TERM_TYPE_SYMBOLIC && (p.getVarType() == Variable.VAR_TYPE_SYMB_X || p.getVarType() == Variable.VAR_TYPE_SYMB_Y))) {
			// Since vector of powers is sorted in descending order
			// binary search algorithm is used to find place where to insert new power object.
			int left = 0, right = this.powers.size()-1, middle = 0;
			Power middleP = null;
			long middleIndex = 0;
			long pIndex = p.getIndex();
			int pExp = p.getExponent();
			
			while (left <= right) {
				middle = (left + right) >> 1;
				middleP = this.powers.get(middle);
				
				if (middleP == null) { // error
					logger.error("Found null object in collection, while in binary search algorithm in addPower() method.");
					break;
				}
				middleIndex = middleP.getIndex();
				
				if (middleIndex == pIndex) {
					// power already exists in vector - then merge degrees
					middleP.addToExponent(pExp);
					break;
				}
				else if (middleIndex < pIndex) { // remember: vector is sorted in descending order
					right = middle - 1;
				}
				else {
					left = middle + 1;
				}
			}
			
			if (left > right) {
				// new power is not found and should be inserted at proper position
				this.powers.insertElementAt(p, left); // identical as add(left, p)
			}
		}
		else
			logger.warn("Attempting to add power of another variable type.");
	}
	
	/**
	 * This method merges powers of this term with powers from another
	 * passed in term - result is stored in current term.
	 * Both terms are of same variable type and they are sorted in descending order.
	 * 
	 * @param t		Term to be merged with current term
	 * @param add	Indicator whether new powers are added to existing collection
	 * 				or they are subtracted from existing collection
	 */
	public void mergePowers(Term t, boolean add){
		int size = this.powers.size();
		int tsize = ((t != null) ? t.getPowers().size() : 0);
		int ii = 0, jj = 0, op = ((add == true) ? 1 : -1);
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		// Terms must be of same type
		if (t == null)
			return; // nothing to merge
		if (this.getType() != t.getType()) {
			logger.warn("Attempt to merge two terms of different types.");
			return;
		}
		
		// Pass simultaneously both collections - this is possible
		// because they are both sorted in descending order.
		while (ii < size && jj < tsize){
			Power pi = this.powers.get(ii);
			Power pj = t.getPowers().get(jj);
			
			if (pi == null || pj == null) {
				logger.error("Null object(s) read from collection");
				return;
			}
			
			long piInd = pi.getIndex(), pjInd = pj.getIndex();
			
			// If powers of same variable are found, merge their exponents.
			if (piInd == pjInd) {
				int resultExp = pi.getExponent() + op * pj.getExponent();
				
				// resultExp must be non-negative
				if (resultExp < 0)
					resultExp = 0;
				// if resulting exponent is zero, then this term has to be
				// removed from the current collection
				if (resultExp == 0) {
					this.powers.remove(ii);
					size--;
					jj++; // go on through passed in term
				}
				else { // processing of this variable is completed; then move forward in both collections
					pi.setExponent(resultExp); // updates element at current position
					ii++;
					jj++;
				}	
			}
			else if (piInd < pjInd) {
				// in 'add' mode new power should be added in current collection
				if (add){
					this.powers.add(ii, pj.clone());
					size++;
					ii++;
					jj++;
				}
				else
					jj++; // move forward in second collection
			}
			else
				ii++; // move forward in current collection
		}
		
		// If end of passed in collection is not reached
		// complete the process - in 'add' mode add all missing powers
		while (jj < tsize){
			Power pj = t.getPowers().get(jj);
			// add new powers at the end
			this.powers.addElement(pj.clone());
			jj++;
		}
	}
	
	/**
	 * <b>
	 * Method for examining whether this term is divisible by some other term.
	 * Both terms are sorted in descending order.
	 * </b>
	 * 
	 * @param t		Passed in term to examine whether current term is divisible by this one
	 * @return		True if first term is divisible by passed in term, false otherwise
	 */
	public final boolean isDivisibleByTerm(Term t) {
		boolean result = true;
		boolean readI = true, readJ = true;
		int ii = 0, jj = 0;
		long piIndex = 0, pjIndex = 0;
		int size = this.powers.size(), 
			tsize = ((t != null) ? t.getPowers().size() : 0);
		Power pi = null, pj = null;
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (t == null) {
			logger.error("Null term passed in.");
			return false;
		}
		
		// terms must be of same type
		if (this.getType() != t.getType()) {
			logger.error("Terms must be of same type.");
			return false;
		}
		
		// special cases
		if (tsize == 0)
			return true; // second term represents constant
		if (size == 0)
			return false; // first term represents constant and therefore
						  // it is divisible only by constant term
		// to be divisible by second term, first term must contain
		// at least all variables from second term
		if (tsize > size)
			return false;
		
		// Pass both terms simultaneously - 
		// it is possible because they are both order in descending order
		while (ii < size && jj < tsize) {
			if (readI) {
				pi = this.powers.get(ii);
				if (pi == null) {
					logger.error("Found null object when expected non-null value");
					return false;
				}
				piIndex = pi.getIndex();
				readI = false;
			}
			
			if (readJ) {
				pj = this.powers.get(jj);
				if (pj == null) {
					logger.error("Found null object when expected non-null value");
					return false;
				}
				pjIndex = pj.getIndex();
				readJ = false;
			}
			
			// If current position in both terms is on same variable,
			// power from first term must have greater or equal exponent
			// than the power from second term, in order to be divisible
			if (piIndex == pjIndex) {
				if (pi.getExponent() < pj.getExponent()) {
					// break the loop - result is false
					result = false;
					break;
				}
				
				// first term is divisible by current power from
				// second term, then move forward in both terms
				ii++;
				readI = true;
				jj++;
				readJ = true;
			}
			else if (piIndex > pjIndex) {
				// skip current power in first term and move to another
				ii++;
				readI = true;
			}
			// current power from second term is not found in first term
			// that means first term is not divisible by second term 
			else {
				result = false;
				break;
			}
		}
		
		// If there are some powers left in second term, 
		// first is not divisible by second
		if (jj < tsize)
			result = false;
		
		// if loop is regularly ended result is true
		// else result is false
		return result;
	}
	
	/**
	 * Method that gives index in vector of powers where specified variable is placed
	 * 
	 * @param varIndex		Index of variable that is searched for in vector of powers
	 * @return				Index in vector of powers where specified variable is placed or -1 if not found
	 */
	// E.g. if vector is powers = [(x_9)^4, (x_8)^3, x_4, (x_2)^5] then
	// getVectorIndexOfVarIndex(8) = 1
	// getVectorIndexOfVarIndex(3) = -1
	// getVectorIndexOfVarIndex(2) = 3 etc.
	public int getVectorIndexOfVarIndex(long varIndex) {
		// Binary search algorithm is used since powers are sorted in descending order
		int left = 0, right = this.powers.size()-1, middle = 0;
		long midVarIndex = 0;
		Power midP = null;
		
		while (left <= right) {
			middle = (left + right) >> 1;
			midP = this.powers.get(middle);
			
			if (midP == null) {
				OpenGeoProver.settings.getLogger().error("Found null object");
				return -1;
			}
			
			midVarIndex = midP.getIndex();
			if (midVarIndex == varIndex)
				return middle;
			else if (midVarIndex < varIndex) // vector is sorted in descending order
				right = middle - 1;
			else
				left = middle + 1;
		}
		
		// not found
		return -1;
	}
	
	
	/**
	 * Method that gives power object of specified variable from vector of powers.
	 * 
	 * @param varIndex		Index of variable that is searched for in vector of powers
	 * @return				Power object of specified variable from vector of powers or null if not found
	 */
	// similar as previous method, but returns object instead of index
	public Power getPowerOfVarIndex(long varIndex) {
		// Binary search algorithm is used since powers are sorted in descending order
		int left = 0, right = this.powers.size()-1, middle = 0;
		long midVarIndex = 0;
		Power midP = null;
		
		while (left <= right) {
			middle = (left + right) >> 1;
			midP = this.powers.get(middle);
			
			if (midP == null) {
				OpenGeoProver.settings.getLogger().error("Found null object");
				return null;
			}
			
			midVarIndex = midP.getIndex();
			if (midVarIndex == varIndex)
				return midP;
			else if (midVarIndex < varIndex) // vector is sorted in descending order
				right = middle - 1;
			else
				left = middle + 1;
		}
		
		// not found
		return null;
	}
	
	/**
	 * Increments the exponent of power of specified variable;
	 * if resulting exponent is zero, power is removed from collection.
	 * 
	 * @param varIndex		Index of variable
	 * @param expInc		Exponent increment
	 * @return				This term which is result of operation, or null in case of error
	 */
	public Term changePowerExponent(int varIndex, int expInc) {
		if (expInc != 0) {
			int ii = this.getVectorIndexOfVarIndex(varIndex);
			
			if (ii >= 0) {
				Power pi = this.powers.get(ii);
				if (pi == null) {
					OpenGeoProver.settings.getLogger().error("Null object found when expected non-null value");
					return null;
				}
				pi.addToExponent(expInc); // update current element
				
				// if new exponent is zero, remove power from vector
				if (pi.getExponent() == 0)
					this.powers.remove(ii);
			}
		}
		
		return this;
	}
	
	/**
	 * <b>Method that gives power exponent of specified variable from term</b>
	 * 
	 * @param varIndex		Index of variable
	 * @return				Power exponent of specified variable if found, zero otherwise
	 */
	public final int getVariableExponent(int varIndex){
		Power p = this.getPowerOfVarIndex(varIndex);
		
		if (p == null)
			return 0;
		
		return p.getExponent();
	}
	
	/**
	 * <b>
	 * Method that finds gcd(greatest common divisor) of this and
	 * passed in term; both terms are sorted in descending order
	 * </b>
	 * 
	 * @param t		Passed in term
	 * @return		This term that makes gcd(this, t)
	 */
	// E.g. t1 = [(x_7)^5, (x_6)^4, x_5, (x_3)^8, x_1],
	// t2 = [x_9, x_8, (x_6)^2, x_5, x_3, (x_2)^2]
	// then gcd(t1, t2) = [(x_6)^2, x_5, x_3] etc.
	public final Term gcd(Term t){
		ILogger logger = OpenGeoProver.settings.getLogger();
		Term thisBeforeChange = this.clone();
		this.powers = new Vector<Power>(); // prepare vector of powers to receive powers of gcd
		
		if (t == null) {
			logger.error("Null term passed in");
			return null;
		}
		
		// terms must be of same type
		if (this.getType() != t.getType()) {
			logger.error("Terms must be of same type in gcd() method");
			return null;
		}
		
		int size1 = thisBeforeChange.getPowers().size(),
			size2 = t.getPowers().size();
		int ii = 0, jj = 0;
		boolean readI = true, readJ = true;
		Power pi = null, pj = null;
		long piIndex = 0, pjIndex = 0;
		int piExp = 0, pjExp = 0;
		
		// Terms are sorted in descending order
		// therefore can be simultaneously passed
		while (ii < size1 && jj <size2) {
			if (readI) {
				pi = thisBeforeChange.getPowers().get(ii);
				if (pi == null) {
					logger.error("Found null object when expected non-null value");
					return null;
				}
				piIndex = pi.getIndex();
				piExp = pi.getExponent();
				readI = false;
			}
			
			if (readJ) {
				pj = t.getPowers().get(jj);
				if (pj == null) {
					logger.error("Found null object when expected non-null value");
					return null;
				}
				pjIndex = pj.getIndex();
				pjExp = pj.getExponent();
				readJ = false;
			}
			
			if (piIndex == pjIndex) { // same variable in both terms
				// found one common divisor
				int resExp = ((piExp <= pjExp) ? piExp : pjExp);
				
				if (resExp > 0) {
					Power p = new Power(pi.getVarType(), piIndex, resExp);
				
					// add this common divisor at the end of new vector;
					// since original terms are ordered in descending order
					// then resulting vector of powers will be ordered in same manner
					this.powers.addElement(p);
				}
				
				// go on in both terms
				ii++;
				readI = true;
				jj++;
				readJ = true;
			}
			else if (piIndex < pjIndex) {
				// move forward only in second term
				jj++;
				readJ = true;
			}
			else {
				// move forward only in first term
				ii++;
				readI = true;
			}
		}
		
		// at the end return this term with newly created vector;
		// if it is empty, that means terms have no common divisors
		return this;
	}
	
}