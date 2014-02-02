/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.polynomials;

import java.util.Vector;
import com.ogprover.main.OGPConstants;
import com.ogprover.utilities.OGPUtilities;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for term of symbolic variables</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
//This is class for generic terms, e.g. 3.5*(x_M)^5*x_C*(x_B)^4
public class SymbolicTerm extends Term {
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
	 * Coefficient of term
	 */
	private double coeff;
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method to set the coefficient of term
	 * 
	 * @param coeff	The coefficient to set
	 */
	public void setCoeff(double coeff) {
		this.coeff = coeff;
	}

	/**
	 * Method to get the coefficient from term
	 * 
	 * @return	The coefficient of term
	 */
	public double getCoeff() {
		return coeff;
	}
	
	/**
	 * Method that retrieves type of polynomial
	 * 
	 * @see com.ogprover.polynomials.Term#getType()
	 */
	@Override
	public int getType() {
		return Term.TERM_TYPE_SYMBOLIC;
	}
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param coeff		Coefficient of generic term
	 */
	public SymbolicTerm (double coeff) {
		this.coeff = coeff;
		this.powers = new Vector<Power>(); // empty vector of powers
	}

	
	
	/*
	 * ======================================================================
	 * ======================= COMMON OBJECT METHODS ========================
	 * ======================================================================
	 */
	/**
	 * Clone method
	 * 
	 * @see com.ogprover.polynomials.Term#clone()
	 */
	@Override
	public Term clone() {
		Term c = new SymbolicTerm(this.coeff);
		int ii = 0, size = this.powers.size();
		
		while (ii < size) {
			// add element at the end - this will keep order of powers from original term
			c.getPowers().addElement(this.powers.get(ii).clone());
			ii++;
		}
		
		return c;
	}
	
	/**
	 * Method toString
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int ii = 0, size = this.powers.size();
		long wholeCoeffPart = 0;
		double diff = 0;
		
		// zero term
		if (this.coeff > -OGPConstants.EPSILON && this.coeff < OGPConstants.EPSILON)
			return "[SymbTerm object: coefficient = 0]";
		
		// printing coefficient
		sb.append("[SymbTerm object: coefficient = ");
		wholeCoeffPart = Math.round(this.coeff);
		diff = this.coeff - wholeCoeffPart;
		if (diff > -OGPConstants.EPSILON && diff < OGPConstants.EPSILON)
			sb.append(wholeCoeffPart + ""); // printing integer coefficient
		else // printing double coefficient
			sb.append(this.coeff + "");
		sb.append(" vector of powers = [\n");
		while (ii < size) {
			sb.append("\t"+this.powers.get(ii).toString());
			sb.append("\n");
			ii++;
		}
		sb.append("]]");
		
		return sb.toString();
	}
	
	/**
	 * Method equals
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof SymbolicTerm))
			return false;
		SymbolicTerm st = (SymbolicTerm)obj;
		return (this.compareTo(st) == 0);
	}

	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method for merging this term with another equal term.
	 * Terms are considered equals if have same powers.
	 * Result of merging is term with same powers and
	 * coefficient equals to sum of coefficients of input terms.
	 * 
	 * @param t		Term equals to this term
	 * @return		This term which is result of operation
	 * 
	 * @see com.ogprover.polynomials.Term#merge(com.ogprover.polynomials.Term)
	 */
	@Override
	public Term merge(Term t) {
		this.coeff += ((t != null) ? ((SymbolicTerm)t).getCoeff() : 0);
		return this;
	}

	/**
	 * Multiplication of this term with another term
	 * 
	 * @param t		Passed in term
	 * @return		This term which is result of operation
	 * 
	 * @see com.ogprover.polynomials.Term#mul(com.ogprover.polynomials.Term)
	 */
	@Override
	public Term mul(Term t) {
		this.coeff *= ((t != null) ? ((SymbolicTerm)t).getCoeff() : 0);
		this.mergePowers(t, true);
		return this;
	}

	/**
	 * Multiplication of this term with real coefficient
	 * 
	 * @param r		Real coefficient multiplier
	 * @return		This term which is result of operation
	 * 
	 * @see com.ogprover.polynomials.Term#mul(double)
	 */
	@Override
	public Term mul(double r) {
		this.coeff *= r;
		return this;
	}

	/**
	 * Division of this term by another term
	 * 
	 * @param t		Passed in term
	 * @return		This term which is result of operation
	 * 
	 * @see com.ogprover.polynomials.Term#divide(com.ogprover.polynomials.Term)
	 */
	@Override
	public Term divide(Term t) {
		double d = ((t != null) ? ((SymbolicTerm)t).getCoeff() : 0);
		
		if (d < OGPConstants.EPSILON && d > -OGPConstants.EPSILON)
			return null; // division by zero
		this.coeff /= d;
		this.mergePowers(t, false);
		return this;
	}

	/**
	 * Inverting term - changing the sign of coefficient
	 * 
	 * @return		This term which is result of operation
	 * 
	 * @see com.ogprover.polynomials.Term#invert()
	 */
	@Override
	public Term invert() {
		this.coeff *= -1;
		return this;
	}
	
	/**
	 * Check whether term is zero constant by its variable
	 * 
	 * @return		True if term is zero, false otherwise
	 * 
	 * @see com.ogprover.polynomials.Term#isZero()
	 */
	@Override
	public boolean isZero() {
		return (this.coeff > -OGPConstants.EPSILON && this.coeff < OGPConstants.EPSILON);
	}
	
	/**
	 * @see com.ogprover.polynomials.RationalAlgebraicExpression#printToLaTeX()
	 */
	public String printToLaTeX() {
		int size = this.powers.size(), ii = 0;
		StringBuilder sb = new StringBuilder();
		double coeffSqr = this.coeff * this.coeff, 
			   decrCoeffSqr = coeffSqr - 1,
			   incrCoeff = this.coeff + 1;
		long wholeCoeffPart = 0;
		double diff = 0;
		
		// don't print zero term
		if (this.coeff > -OGPConstants.EPSILON && this.coeff < OGPConstants.EPSILON)
			return "";
		
		// if term is constant or coefficient is not 1 and -1, add coefficient
		if (size == 0 || decrCoeffSqr <= -OGPConstants.EPSILON || decrCoeffSqr >= OGPConstants.EPSILON) {
			// special case - printing integer coefficient
			wholeCoeffPart = Math.round(this.coeff);
			diff = this.coeff - wholeCoeffPart;
			if (diff > -OGPConstants.EPSILON && diff < OGPConstants.EPSILON)
				sb.append(wholeCoeffPart + "");
			else // print double coefficient
				sb.append(OGPUtilities.roundUpToPrecision(this.coeff) + "");
		}
		// if term is not constant and coefficient is -1, just write sign
		else if (incrCoeff > -OGPConstants.EPSILON && incrCoeff < OGPConstants.EPSILON)
			sb.append("-");
		
		// print all powers one by one in descending order - they are already sorted
		while (ii < size) {
			sb.append(this.powers.get(ii).printToLaTeX());
			ii++;
		}
		
		String latexTerm = sb.toString();
		
		if (latexTerm.length() > OGPConstants.MAX_OUTPUT_POLY_CHUNK_SIZE)
			return "??"; // term is too long for output
		
		return latexTerm;
	}
	
	/**
	 * @see com.ogprover.polynomials.RationalAlgebraicExpression#printToXML()
	 */
	public String printToXML() {
		int size = this.powers.size(), ii = 0;
		StringBuilder sb = new StringBuilder();
		double coeffSqr = this.coeff * this.coeff, 
			   decrCoeffSqr = coeffSqr - 1;
		long wholeCoeffPart = 0;
		double diff = 0;
		
		// don't print zero term
		if (this.coeff > -OGPConstants.EPSILON && this.coeff < OGPConstants.EPSILON)
			return "";
		
		sb.append("<proof_sterm>");
		
		if (this.coeff < 0)
			sb.append("<proof_sign> - </proof_sign>");
		else
			sb.append("<proof_sign> + </proof_sign>");
		
		// if term is constant or coefficient is not 1 and -1, add coefficient
		if (size == 0 || decrCoeffSqr <= -OGPConstants.EPSILON || decrCoeffSqr >= OGPConstants.EPSILON) {
			sb.append("<proof_coeff>");
			
			double absCoeff = Math.abs(this.coeff);
			
			// special case - printing integer coefficient
			wholeCoeffPart = Math.round(absCoeff);
			diff = absCoeff - wholeCoeffPart;
			
			if (diff > -OGPConstants.EPSILON && diff < OGPConstants.EPSILON)
				sb.append(wholeCoeffPart + "");
			else // print double coefficient
				sb.append(OGPUtilities.roundUpToPrecision(absCoeff) + "");
			
			sb.append("</proof_coeff>");
		}
		
		// print all powers one by one in descending order - they are already sorted
		while (ii < size) {
			sb.append(this.powers.get(ii).printToXML());
			ii++;
		}
		
		sb.append("</proof_sterm>");
		
		String xmlTerm = sb.toString();
		
		if (xmlTerm.length() > OGPConstants.MAX_XML_OUTPUT_POLY_CHARS_NUM)
			return "??"; // term is too long for output
		
		return xmlTerm;
	}
	
	/**
	 * @see com.ogprover.polynomials.RationalAlgebraicExpression#print()
	 */
	public String print() {
		int size = this.powers.size(), ii = 0;
		StringBuilder sb = new StringBuilder();
		double coeffSqr = this.coeff * this.coeff, 
			   decrCoeffSqr = coeffSqr - 1,
			   incrCoeff = this.coeff + 1;
		long wholeCoeffPart = 0;
		double diff = 0;
		
		// don't print zero term
		if (this.coeff > -OGPConstants.EPSILON && this.coeff < OGPConstants.EPSILON)
			return "";
		
		// if term is constant or coefficient is not 1 and -1, add coefficient
		if (size == 0 || decrCoeffSqr <= -OGPConstants.EPSILON || decrCoeffSqr >= OGPConstants.EPSILON) {
			// special case - printing integer coefficient
			wholeCoeffPart = Math.round(this.coeff);
			diff = this.coeff - wholeCoeffPart;
			if (diff > -OGPConstants.EPSILON && diff < OGPConstants.EPSILON)
				sb.append(wholeCoeffPart);
			else // print double coefficient
				sb.append(OGPUtilities.roundUpToPrecision(this.coeff));
		}
		// if term is not constant and coefficient is -1, just write sign
		else if (incrCoeff > -OGPConstants.EPSILON && incrCoeff < OGPConstants.EPSILON)
			sb.append("-");
		
		// print all powers one by one in descending order - they are already sorted
		while (ii < size) {
			sb.append(this.powers.get(ii).print());
			ii++;
		}
		
		String stringTerm = sb.toString();
		
		if (stringTerm.length() > OGPConstants.MAX_OUTPUT_POLY_CHARS_NUM)
			return "..."; // term is too long for output
		
		return stringTerm;
	}
	
	// Overriding unnecessary methods from Term class - no Javadoc comments for these
	public int getVectorIndexOfVarIndex(int varIndex) {
		return -1;
	}
	
	public Power getPowerOfVarIndex(int varIndex) {
		return null;
	}
	
	public Term changePowerExponent(int varIndex, int expInc) {
		return null;
	}
	
}