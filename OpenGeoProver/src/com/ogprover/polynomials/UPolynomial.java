/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.polynomials;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

import com.ogprover.main.OGPConstants;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for u-polynomials</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public class UPolynomial extends Polynomial {
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
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method that retrieves type of polynomial
	 * 
	 * @see com.ogprover.polynomials.Polynomial#getType()
	 */
	@Override
	public int getType() {
		return Polynomial.POLY_TYPE_UPOLY;
	}
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Default constructor method
	 */
	public UPolynomial(){
		this.terms = new TreeMap<Term, Term>(); // empty tree map collection of terms
	}
	/**
	 * Constructor method (constructs polynomial from real constant)
	 * 
	 * @param cf	real constant that represents constant polynomial
	 */
	public UPolynomial(double cf) {
		Term singleTerm = new UTerm(cf);
		this.terms = new TreeMap<Term, Term>();
		this.terms.put(singleTerm, singleTerm);
	}
	
	/*
	 * ======================================================================
	 * ======================= COMMON OBJECT METHODS ========================
	 * ======================================================================
	 */
	/**
	 * Clone method
	 * 
	 * @see com.ogprover.polynomials.Polynomial#clone()
	 */
	@Override
	public Polynomial clone() {
		Collection<Term> col = this.terms.values(); // all values from tree in ascending order
		Iterator<Term> termIT = col.iterator();
		Polynomial c = new UPolynomial();
		
		while(termIT.hasNext()) {
			Term ct = termIT.next().clone();
			c.getTerms().put(ct, ct); // this will keep the same order of terms as in original tree
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
		ArrayList<Term> list = this.getTermsAsDescList();
		int ii = 0, size = list.size();
		
		if (size == 0)
			sb.append("[UPolynomial object: zero polynomial]");
		else {
			sb.append("[UPolynomial object: <terms in descending order>\n");
			while (ii < size) {
				sb.append("\t");
				sb.append(this.getTermsAsDescList().get(ii++).toString());
				sb.append("\n");
			}
			sb.append("]");
		}
		
		return sb.toString();
	}
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method for division of this polynomial by one term of same type.
	 * <p>
	 * Assumption is that this polynomial is divisible by passed in term i.e.
	 * each its term is divisible by passed in term.
	 * </p>
	 * 
	 * @param ut	Term to divide this polynomial by
	 * @return		This polynomial which is result of operation or null in case of error
	 */
	public UPolynomial divideByTerm(UTerm ut) {
		for (Term t : this.getTermsAsDescList()) {
			if (((UTerm)t).divide(ut) == null)
				return null;
		}
		
		return this;
	}
	
	/**
	 * @see com.ogprover.polynomials.RationalAlgebraicExpression#printToLaTeX()
	 */
	public String printToLaTeX() {
		StringBuilder sb = new StringBuilder();
		boolean firstTerm = true;
		ArrayList<Term> list = this.getTermsAsDescList();
		int ii = 0, size = list.size(), chunkSize = 0, newChunkSize = 0;
		
		if (list.size() > OGPConstants.MAX_OUTPUT_POLY_TERMS_NUM)
			return "??"; // polynomial is too long for output
		
		while (ii < size) {
			Term t = list.get(ii);
			String latexTerm = t.printToLaTeX();
			int len = latexTerm.length();
			newChunkSize = chunkSize + len;
			
			if (latexTerm.startsWith("??"))
				return "??"; // term is too long for output
			
			if (!firstTerm && ((UTerm)t).getCoeff() > 0)
				sb.append("+");
			
			if (newChunkSize > OGPConstants.MAX_OUTPUT_POLY_CHUNK_SIZE){
				if (!firstTerm)
					sb.append("$$"); // chunk separator
				sb.append(latexTerm);
				chunkSize = len;
			}
			else {
				sb.append(latexTerm);
				chunkSize = newChunkSize;
			}
			
			if (firstTerm)
				firstTerm = false;
			
			ii++;
		}
		
		return sb.toString();
	}
	
	/**
	 * @see com.ogprover.polynomials.RationalAlgebraicExpression#printToXML()
	 */
	public String printToXML() {
		StringBuilder sb = new StringBuilder();
		ArrayList<Term> list = this.getTermsAsDescList();
		
		if (list.size() > OGPConstants.MAX_OUTPUT_POLY_TERMS_NUM)
			return "??"; // polynomial is too long for output
		
		sb.append("<proof_upoly>");
		
		boolean firstTerm = true;
		
		for (int ii = 0, jj = list.size(); ii < jj; ii++) {
			UTerm ut = (UTerm)list.get(ii);
			String xmlTerm = ut.printToXML();
			
			if (xmlTerm.startsWith("??")) // polynomial is too long for output
				return "??";
			
			if (firstTerm) {
				// If coefficient of first term is non-negative, remove the + sign
				if (ut.getCoeff() >= 0) {
					// skip the sign
					sb.append(xmlTerm.substring(0, xmlTerm.indexOf("<proof_usign>")));
					sb.append(xmlTerm.substring(xmlTerm.indexOf("</proof_usign>") + 14));
				}
				else
					sb.append(xmlTerm);
				firstTerm = false;
			}
			else
				sb.append(xmlTerm);
		}
		sb.append("</proof_upoly>");
		
		String xmlText = sb.toString();
		
		if (xmlText.length() > OGPConstants.MAX_XML_OUTPUT_POLY_CHARS_NUM)
			return "??"; // polynomial is too long for output
		
		return xmlText;
	}
	
	public boolean isSingleNegativeTerm() {
		if (this.getTerms().size() == 1) {
			UTerm ut = (UTerm)this.getTerms().get(this.getTerms().firstKey());
			return (ut.getCoeff() < 0);
		}
		
		return false;
	}

	/**
	 * @see com.ogprover.polynomials.RationalAlgebraicExpression#print()
	 */
	public String print() {
		StringBuilder sb = new StringBuilder();
		boolean firstTerm = true;
		ArrayList<Term> list = this.getTermsAsDescList();
		
		for (int ii = 0, size = list.size(); ii < size; ii++) {
			Term t = list.get(ii);
			String stringTerm = t.print();
			
			if (stringTerm.startsWith("..."))
				return "..."; // term is too long for output
			
			if (!firstTerm && ((UTerm)t).getCoeff() > 0)
				sb.append("+");
			
			sb.append(stringTerm);
			
			if (firstTerm)
				firstTerm = false;
		}
		
		String stringPoly = sb.toString();
		if (stringPoly.length() > OGPConstants.MAX_OUTPUT_POLY_CHARS_NUM)
			return "...";
		return stringPoly;
	}
	
}