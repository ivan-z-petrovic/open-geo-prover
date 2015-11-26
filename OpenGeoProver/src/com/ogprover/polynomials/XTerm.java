/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.polynomials;

import java.util.Vector;

import com.ogprover.main.OGPConstants;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.utilities.logger.ILogger;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for x-term</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
//This is class for x-terms, e.g. (3.5*(u_2)^2(u_1)^5 + 6(u_1) + 7)/(2(u_2)^8 - 7) * (x_10)^5(x_8)^4(x_3)(x_1)^4
public class XTerm extends Term {
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
	 * Rational algebraic expression by u variables - since they are parametric values
	 * this is treated as coefficient by x variables
	 */
	private UFraction uCoeff;
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method that retrieves coefficient
	 * 
	 * @return The u-coefficient
	 */
	public UFraction getUCoeff() {
		return uCoeff;
	}

	/**
	 * Method that sets coefficient
	 * 
	 * @param uCoeff The u-coefficient to set
	 */
	public void setUCoeff(UFraction uCoeff) {
		this.uCoeff = uCoeff;
	}
	
	/**
	 * Method that retrieves the type of polynomial
	 * 
	 * @see com.ogprover.polynomials.Term#getType()
	 */
	@Override
	public int getType() {
		return Term.TERM_TYPE_XTERM;
	}
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param uf	Fraction by u variable to be set as coefficient.
	 */
	public XTerm(UFraction uf) {
		this.uCoeff = uf;
		this.powers = new Vector<Power>(); // empty vector of powers
	}
	
	/**
	 * Constructor method
	 * 
	 * @param d		Real coefficient.
	 */
	public XTerm(double d) {
		this.uCoeff = new UFraction(d);
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
		Term c = new XTerm(this.uCoeff.clone());
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
		
		// zero term
		if (this.uCoeff.isZero())
			return "[XTerm object: coefficient = 0]";
		
		// printing coefficient
		sb.append("[XTerm object:\n coefficient = ");
		sb.append(this.uCoeff.toString());
		sb.append("\n vector of powers = [\n");
		while (ii < size) {
			sb.append("\t" + this.powers.get(ii).toString());
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
		if (!(obj instanceof XTerm))
			return false;
		XTerm x = (XTerm)obj;
		return (this.compareTo(x) == 0);
	}

	
	

	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method that reduces the coefficient of term
	 * 
	 * @return	This term which is result of operation
	 */
	public Term reduce(){
		this.uCoeff.reduce();
		return this;
	}
	
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
		if (t == null) {
			OpenGeoProver.settings.getLogger().error("Null term passed in.");
			return null;
		}
		
		this.uCoeff.add(((XTerm)t).getUCoeff());
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
		if (t == null) {
			OpenGeoProver.settings.getLogger().error("Null term passed in.");
			return null;
		}
		
		this.uCoeff.mul(((XTerm)t).getUCoeff());
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
		this.uCoeff.mul(r);
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
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (t == null) {
			logger.error("Null term passed in.");
			return null;
		}
		
		if (t.isZero()) {
			logger.error("Attempt to divide by zero term.");
			return null;
		}
		
		this.uCoeff.mul(((XTerm)t).getUCoeff().invertFraction());
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
		this.uCoeff.invertSign();
		return this;
	}
	
	/**
	 * Check whether term is zero constant by its variable
	 * 
	 * @return		true if term is zero, false otherwise
	 * 
	 * @see com.ogprover.polynomials.Term#isZero()
	 */
	@Override
	public boolean isZero() {
		return this.uCoeff.isZero();
	}
	
	/**
	 * @see com.ogprover.polynomials.RationalAlgebraicExpression#printToLaTeX()
	 */
	public String printToLaTeX() {
		int size = this.powers.size(), ii = 0;
		StringBuilder sb = new StringBuilder();
		
		// don't print zero term
		if (this.uCoeff.isZero())
			return "";
		
		// print coefficient
		String latexUCoeff = this.uCoeff.printToLaTeX();
		int len = 0, lenUCoeff = latexUCoeff.length();
		
		if (lenUCoeff == 0) {
			// if uCoeff is equals to 1 or -1 print it if there are no powers in x-term
			double uCoeff = ((UTerm)this.uCoeff.getNumerator().getTermsAsDescList().get(0)).getCoeff(); // coefficient of single u-term from u-fraction
			double shiftedUCoeff = (uCoeff > 0) ? uCoeff - 1 : uCoeff + 1;
			
			if (shiftedUCoeff > -OGPConstants.EPSILON && shiftedUCoeff < OGPConstants.EPSILON) {
				if (this.powers.size() == 0)
					return (uCoeff > 0) ? "1" : "-1";
			}
		}
		
		if (latexUCoeff.startsWith("??")) // this means that coefficient is longer than the output chunk and can not be divided
			return "??";

		sb.append(latexUCoeff); // if this is longer than one chunk it is already divided in lines
		if (lenUCoeff > OGPConstants.MAX_OUTPUT_POLY_CHUNK_SIZE)
			sb.append("$$");    // chunk separator
		else
			len = lenUCoeff;
		
		// print all powers one by one in descending order - they are already sorted
		StringBuilder tempSb = new StringBuilder();
		while (ii < size) {
			tempSb.append(this.powers.get(ii).printToLaTeX());
			ii++;
		}
		
		String latexPowers = tempSb.toString();
		int lenPowers = latexPowers.length();
		
		if (len + lenPowers > OGPConstants.MAX_OUTPUT_POLY_CHUNK_SIZE) {
			if (lenPowers > OGPConstants.MAX_OUTPUT_POLY_CHUNK_SIZE) // cannot divide powers in lines
				return "??";
			sb.append("$$"); // chunk separator
			sb.append(latexPowers);
		}
		else
			sb.append(latexPowers);
		
		return sb.toString();
	}
	
	/**
	 * @see com.ogprover.polynomials.RationalAlgebraicExpression#printToXML()
	 */
	public String printToXML() {
		StringBuilder sb = new StringBuilder();
		
		// don't print zero term
		if (this.uCoeff.isZero())
			return "";
		
		String xmlUCoeff = this.uCoeff.printToXML();
		
		if (xmlUCoeff.startsWith("??")) // this means that coefficient is too long for output
			return "??";
		
		sb.append("<proof_xterm>");
		
		/*
		 * Print + sign if:
		 * 	1. there is denominator in u-coefficient
		 * 	2. there is only numerator in u-coefficient but in brackets
		 * 	3. there is numerator in u-coefficient with single term but without sign
		 */
		if (xmlUCoeff.indexOf("<proof_ufrac_den>") >= 0 ||
			xmlUCoeff.indexOf("<proof_lbrac>") >= 0 ||
			xmlUCoeff.indexOf("<proof_usign>") < 0)
			sb.append("<proof_xsign> + </proof_xsign>");
		
		/*
		 * Printing coefficient:
		 * Remove coefficient -1 or 1 if there are powers in this x-term
		 */
		if (xmlUCoeff.indexOf("<proof_ufrac_den>") < 0 && 
			xmlUCoeff.indexOf("<proof_lbrac>") < 0) {
			// u-coefficient consists of only single term numerator
			UTerm singleUTerm = (UTerm)this.uCoeff.getNumerator().getTermsAsDescList().get(0);
			double uCoeff = singleUTerm.getCoeff(); // coefficient of single u-term from u-fraction
			double shiftedUCoeff = (uCoeff > 0) ? uCoeff - 1 : uCoeff + 1;
			
			if (singleUTerm.getPowers().size() == 0 && shiftedUCoeff > -OGPConstants.EPSILON && shiftedUCoeff < OGPConstants.EPSILON) {
				// coefficient is -1 or 1 - remove it if there are powers in x-term
				if (this.getPowers().size() > 0) {
					sb.append(xmlUCoeff.substring(0, xmlUCoeff.indexOf("<proof_coeff>")));
					sb.append(xmlUCoeff.substring(xmlUCoeff.indexOf("</proof_coeff>") + 14));
				}
				else
					sb.append(xmlUCoeff);
			}
			else
				sb.append(xmlUCoeff);
		}
		else
			sb.append(xmlUCoeff);
		
		// print all powers one by one in descending order - they are already sorted
		for (int ii = 0, size = this.powers.size(); ii < size; ii++)
			sb.append(this.powers.get(ii).printToXML());
		
		sb.append("</proof_xterm>");
		
		String xmlText = sb.toString();
		
		if (xmlText.length() > OGPConstants.MAX_XML_OUTPUT_POLY_CHARS_NUM)
			return "??"; // too long term for output
		
		return xmlText;
	}

	/**
	 * @see com.ogprover.polynomials.RationalAlgebraicExpression#print()
	 */
	public String print() {
		int size = this.powers.size(), ii = 0;
		StringBuilder sb = new StringBuilder();
		
		// don't print zero term
		if (this.uCoeff.isZero())
			return "";
		
		// print coefficient
		String stringUCoeff = this.uCoeff.print();
		int len = 0, lenUCoeff = stringUCoeff.length();
		
		if (lenUCoeff == 0) {
			// if uCoeff is equals to 1 or -1 print it if there are no powers in x-term
			double uCoeff = ((UTerm)this.uCoeff.getNumerator().getTermsAsDescList().get(0)).getCoeff(); // coefficient of single u-term from u-fraction
			double shiftedUCoeff = (uCoeff > 0) ? uCoeff - 1 : uCoeff + 1;
			
			if (shiftedUCoeff > -OGPConstants.EPSILON && shiftedUCoeff < OGPConstants.EPSILON) {
				if (this.powers.size() == 0)
					return (uCoeff > 0) ? "1" : "-1";
			}
		}
		
		if (stringUCoeff.startsWith("...")) // this means that coefficient is too long
			return "...";

		sb.append(stringUCoeff);
		len = lenUCoeff;
		
		// print all powers one by one in descending order - they are already sorted
		StringBuilder tempSb = new StringBuilder();
		while (ii < size) {
			tempSb.append(this.powers.get(ii).print());
			ii++;
		}
		
		String stringPowers = tempSb.toString();
		int lenPowers = stringPowers.length();
		
		if (len + lenPowers > OGPConstants.MAX_OUTPUT_POLY_CHARS_NUM)
			return "...";
		sb.append(stringPowers);
		return sb.toString();
	}
}