/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.polynomials;

import java.util.Collection;
import java.util.Iterator;

import com.ogprover.main.OGPConstants;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for fraction of two u-polynomials</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public class UFraction implements Cloneable, RationalAlgebraicExpression {
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
	// fraction = numerator/denominator
	/**
	 * Numerator of fraction
	 */
	private UPolynomial numerator;
	/**
	 * Denominator of fraction
	 */
	private UPolynomial denominator;
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method that gives numerator of fraction
	 * 
	 * @return The numerator
	 */
	public UPolynomial getNumerator() {
		return numerator;
	}

	/**
	 * Methods that sets numerator of fraction
	 * 
	 * @param numerator The numerator to set
	 */
	public void setNumerator(UPolynomial numerator) {
		this.numerator = numerator;
	}

	/**
	 * Method that gives denominator of fraction
	 * 
	 * @return The denominator
	 */
	public UPolynomial getDenominator() {
		return denominator;
	}

	/**
	 * Methods that sets denominator of fraction
	 * 
	 * @param denominator The denominator to set
	 */
	public void setDenominator(UPolynomial denominator) {
		this.denominator = denominator;
	}
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param num	Numerator of fraction object
	 * @param den	Denominator of fraction object.
	 */
	public UFraction(UPolynomial num, UPolynomial den){
		this.numerator = num;
		this.denominator = den;
	}
	
	/**
	 * Constructor method
	 * 
	 * @param p		Polynomial which represents fraction (its numerator, while denominator is 1).
	 */
	public UFraction(UPolynomial p) {
		this.numerator = p;
		this.denominator = new UPolynomial(1);
	}
	
	/**
	 * Constructor method
	 * 
	 * @param d		Real number which represents fraction (its numerator, while denominator is 1).
	 */
	public UFraction(double d) {
		this.numerator = new UPolynomial(d);
		this.denominator = new UPolynomial(1);
	}
	
	
	
	/*
	 * ======================================================================
	 * ======================= COMMON OBJECT METHODS ========================
	 * ======================================================================
	 */
	/**
	 * Clone method
	 * 
	 * @see java.lang.Object#clone()
	 */
	public UFraction clone() {
		return new UFraction((UPolynomial)this.numerator.clone(), (UPolynomial)this.denominator.clone());
	}
	
	/**
	 * Method toString
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("[UFraction object:\n Numerator = [");
		sb.append(this.numerator.toString());
		sb.append("];\n Denominator = [");
		sb.append(this.denominator.toString());
		sb.append("]\n]");
		
		return sb.toString();
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * <i>
	 * Examines whether fraction is valid i.e. its denominator is not zero polynomial.
	 * </i>
	 * 
	 * @return		True if valid, false otherwise.
	 */
	public static boolean isValid(UFraction uf){
		return !(uf.getDenominator().isZero());
	}
	
	/**
	 * Examines whether valid fraction is zero i.e. its numerator is zero polynomial.
	 * 
	 * @return		True if zero, false otherwise
	 */
	public boolean isZero() {
		return this.numerator.isZero();
	}
	
	/**
	 * Method for simple reduction of fraction - each term from numerator and denominator
	 * is reduced (i.e. divided) by gcd (greatest common divisor) of all terms.
	 * Also, if denominator is real number, numerator is divided by it.
	 * Assumption is made that this fraction is valid.
	 * 
	 * @return		This object, which is result of operation
	 */
	public UFraction reduce(){
		if (this.numerator.getTerms().size() == 0 || this.denominator.getTerms().size() == 0)
			return this;
		
		Collection<Term> colNum = this.numerator.getTerms().values();
		Iterator<Term> itNum = colNum.iterator();
		Collection<Term> colDen = this.denominator.getTerms().values();
		Iterator<Term> itDen = colDen.iterator();
		boolean isFirst = true;
		UTerm gcdTerm = null;
		
		while (itNum.hasNext() && 
				(isFirst || (gcdTerm != null && gcdTerm.getPowers().size() > 0))) {
			if (isFirst) {
				gcdTerm = (UTerm)itNum.next().clone();
				gcdTerm.setCoeff(1);
				isFirst = false;
			}
			else			
				gcdTerm.gcd(itNum.next());
		}
		
		while (itDen.hasNext() && 
				(gcdTerm != null && gcdTerm.getPowers().size() > 0))			
			gcdTerm.gcd(itDen.next());
		
		// if non-trivial (i.e. not equals 1) gcd exists, divide all terms by gcd;
		// this is safe because order in tree of terms will be kept
		if (gcdTerm.getPowers().size() > 0) {
			itNum = colNum.iterator();
			while (itNum.hasNext())
				itNum.next().divide(gcdTerm);
			
			itDen = colDen.iterator();
			while (itDen.hasNext())
				itDen.next().divide(gcdTerm);
		}
		
		// check whether denominator is constant real number and numerator is not constant real number
		if (this.denominator.getTerms().size() == 1) {
			UTerm ut = (UTerm)this.denominator.getTerms().get(this.denominator.getTerms().firstKey());
			UTerm utn = (UTerm)this.numerator.getTerms().get(this.numerator.getTerms().firstKey());
			
			// check whether this term is constant number
			if (ut.getPowers().size() == 0 && 
				(this.numerator.getTerms().size() != 1 || 
				 utn.getPowers().size() != 0)) {
				itNum = colNum.iterator();
				while (itNum.hasNext())
					itNum.next().mul(1/ut.getCoeff()); // this division is safe because we assume
				                                       // that this fraction is valid and therefore
				                                       // denominator is not zero, which in this
				                                       // special case (single u-term with no powers)
				                                       // means that coefficient is not zero
				
				ut.setCoeff(1);
			}
		}
		
		return this;
	}
	
	/**
	 * Method for addition of two valid fractions
	 * 
	 * @param uf	Passed in fraction
	 * @return		Sum of this and passed in fraction
	 */
	public UFraction add(UFraction uf) {
		this.numerator.multiplyByPolynomial(uf.getDenominator()).addPolynomial(uf.getNumerator().clone().multiplyByPolynomial(this.denominator));
		this.denominator.multiplyByPolynomial(uf.getDenominator());
		return this;
	}
	
	/**
	 * Inversion of sign
	 * 
	 * @return		This object, which contains the result of operation.
	 */
	public UFraction invertSign() {
		this.numerator.invert();
		return this;
	}
	
	/**
	 * Inversion of fraction (reciprocal value of fraction)
	 * 
	 * @return		This object which is result of operation.
	 */
	public UFraction invertFraction() {
		UPolynomial oldNum = this.numerator;
		this.numerator = this.denominator;
		this.denominator = oldNum;
		return this;
	}
	
	/**
	 * Subtraction of two valid fraction
	 * 
	 * @param uf	Passed in fraction
	 * @return		This object, which is result of operation (difference between two fractions)
	 */
	public UFraction subtract(UFraction uf) {
		return this.add(uf.clone().invertSign());
	}
	
	/**
	 * Multiplication of two valid fractions
	 * 
	 * @param uf	Passed in fraction
	 * @return		This object, which is result of operation (product of two fractions)
	 */
	public UFraction mul(UFraction uf) {
		if (this.isZero())
			return this;
		
		if (uf.isZero()) {
			this.numerator = new UPolynomial(); // empty (zero) polynomial
			Term ut = new UTerm(1);
			this.denominator = new UPolynomial();
			this.denominator.addTerm(ut); // denominator contains constant 1
			return this;
		}
		
		this.numerator.multiplyByPolynomial(uf.getNumerator());
		this.denominator.multiplyByPolynomial(uf.getDenominator());
		
		return this;
	}
	
	/**
	 * Multiplication by polynomial
	 * 
	 * @param up	Passed in polynomial
	 * @return		This object, which is result of operation (product with polynomial)
	 */
	public UFraction mul(UPolynomial up) {
		if (this.isZero())
			return this;
		
		if (up.isZero()) {
			this.numerator = new UPolynomial(); // empty (zero) polynomial
			Term ut = new UTerm(1);
			this.denominator = new UPolynomial();
			this.denominator.addTerm(ut); // denominator contains constant 1
			return this;
		}
		
		this.numerator.multiplyByPolynomial(up);
		
		return this;
	}
	
	/**
	 * Multiplication by real constant
	 * 
	 * @param d		Passed in constant
	 * @return		This object, which is result of operation (product with real constant)
	 */
	public UFraction mul(double d) {
		if (this.isZero())
			return this;
		
		if (d > -OGPConstants.EPSILON && d < OGPConstants.EPSILON) {
			this.numerator = new UPolynomial(); // empty (zero) polynomial
			Term ut = new UTerm(1);
			this.denominator = new UPolynomial();
			this.denominator.addTerm(ut); // denominator contains constant 1
			return this;
		}
		
		this.numerator.multiplyByRealConstant(d);
		
		return this;
	}
	
	/**
	 * Method that answers whether fraction consists of single negative term.
	 * This is used when printing fraction.
	 * 
	 * @return	True if fraction is single negative term, false otherwise 
	 */
	public boolean isSingleNegativeTerm(){
		boolean isDenOne = false;
		
		// check whether denominator is constant 1.
		if (this.denominator.getTerms().size() == 1) {
			UTerm ut = (UTerm)this.denominator.getTerms().get(this.denominator.getTerms().firstKey());
			double coeffDiff = ut.getCoeff() - 1;
			
			// check whether this term is constant 1
			if (coeffDiff > -OGPConstants.EPSILON && coeffDiff < OGPConstants.EPSILON && ut.getPowers().size() == 0)
				isDenOne = true;
		}
		
		if (isDenOne && this.numerator.getTerms().size() == 1) {
			UTerm ut = (UTerm)this.numerator.getTerms().get(this.numerator.getTerms().firstKey());
			return (ut.getCoeff() < 0);
		}
		
		return false;
	}
	
	/**
	 * @see com.ogprover.polynomials.RationalAlgebraicExpression#printToLaTeX()
	 */
	public String printToLaTeX() {
		StringBuilder sb = new StringBuilder();
		boolean isDenOne = false;
		
		// check whether denominator is constant 1.
		if (this.denominator.getTerms().size() == 1) {
			UTerm ut = (UTerm)this.denominator.getTerms().get(this.denominator.getTerms().firstKey());
			double coeffDiff = ut.getCoeff() - 1;
			
			// check whether this term is constant 1
			if (coeffDiff > -OGPConstants.EPSILON && coeffDiff < OGPConstants.EPSILON && ut.getPowers().size() == 0)
				isDenOne = true;
		}
		
		String latexNum = this.numerator.printToLaTeX();
		String latexDen;
		
		if (isDenOne) {
			if (latexNum.startsWith("??")) // numerator is too long for output
				return "??";
			
			if (this.numerator.getTerms().size() > 1) {
				sb.append("(");
				sb.append(latexNum);
				sb.append(")");
			}
			else {
				UTerm ut = (UTerm)this.numerator.getTerms().get(this.numerator.getTerms().firstKey());
				double uCoeff = ut.getCoeff();
				double uCoeffDiff = ((uCoeff > 0) ? uCoeff - 1 : uCoeff + 1);
				
				// this single term in u-polynomial is -1 or 1
				if (ut.getPowers().size() == 0 && uCoeffDiff > -OGPConstants.EPSILON && uCoeffDiff < OGPConstants.EPSILON) {
					if (uCoeff < 0)
						sb.append("-");
				}
				else
					sb.append(latexNum);
			}
		}
		else {
			latexDen = this.denominator.printToLaTeX();
			if (latexNum.length() > OGPConstants.MAX_OUTPUT_POLY_CHUNK_SIZE ||
				latexDen.length() > OGPConstants.MAX_OUTPUT_POLY_CHUNK_SIZE)
				return "??"; // impossible to divide fraction
				
			sb.append("\\frac{");
			sb.append(latexNum);
			sb.append("}{");
			sb.append(latexDen);
			sb.append("}");
		}
		
		return sb.toString();
	}
	
	/**
	 * @see com.ogprover.polynomials.RationalAlgebraicExpression#printToXML()
	 */
	public String printToXML() {
		StringBuilder sb = new StringBuilder();
		boolean isDenOne = false;
		
		sb.append("<proof_ufrac>");
		
		// check whether denominator is constant 1.
		if (this.denominator.getTerms().size() == 1) {
			UTerm ut = (UTerm)this.denominator.getTerms().get(this.denominator.getTerms().firstKey());
			double coeffDiff = ut.getCoeff() - 1;
			
			// check whether this term is constant 1
			if (coeffDiff > -OGPConstants.EPSILON && coeffDiff < OGPConstants.EPSILON && ut.getPowers().size() == 0)
				isDenOne = true;
		}
		
		String xmlNum = this.numerator.printToXML();
		String xmlDen;
		
		if (xmlNum.startsWith("??")) // numerator is too long for output
			return "??";
		
		if (isDenOne) {
			sb.append("<proof_ufrac_num>");
			
			if (this.numerator.getTerms().size() > 1) {
				sb.append("<proof_lbrac></proof_lbrac>");
				sb.append(xmlNum);
				sb.append("<proof_rbrac></proof_rbrac>");
			}
			else 
				sb.append(xmlNum);
			
			sb.append("</proof_ufrac_num>");
		}
		else {
			xmlDen = this.denominator.printToXML();
			
			if (xmlDen.startsWith("??")) // denominator is too long for output
				return "??";
				
			sb.append("<proof_ufrac_num>");
			sb.append("<proof_lbrac></proof_lbrac>");
			sb.append(xmlNum);
			sb.append("<proof_rbrac></proof_rbrac>");
			sb.append("</proof_ufrac_num>");
			sb.append("<proof_slash></proof_slash>");
			sb.append("<proof_ufrac_den>");
			sb.append("<proof_lbrac></proof_lbrac>");
			sb.append(xmlDen);
			sb.append("<proof_rbrac></proof_rbrac>");
			sb.append("</proof_ufrac_den>");
		}
		
		sb.append("</proof_ufrac>");
		
		String xmlText = sb.toString();
		
		if (xmlText.length() > OGPConstants.MAX_XML_OUTPUT_POLY_CHARS_NUM)
			return "??";
		
		return xmlText;
	}

	/**
	 * @see com.ogprover.polynomials.RationalAlgebraicExpression#print()
	 */
	public String print() {
		StringBuilder sb = new StringBuilder();
		boolean isDenOne = false;
		
		// check whether denominator is constant 1.
		if (this.denominator.getTerms().size() == 1) {
			UTerm ut = (UTerm)this.denominator.getTerms().get(this.denominator.getTerms().firstKey());
			double coeffDiff = ut.getCoeff() - 1;
			
			// check whether this term is constant 1
			if (coeffDiff > -OGPConstants.EPSILON && coeffDiff < OGPConstants.EPSILON && ut.getPowers().size() == 0)
				isDenOne = true;
		}
		
		String stringNum = this.numerator.print();
		String stringDen;
		
		if (isDenOne) {
			if (stringNum.startsWith("...")) // numerator is too long for output
				return "...";
			
			if (this.numerator.getTerms().size() > 1) {
				sb.append("(");
				sb.append(stringNum);
				sb.append(")");
			}
			else {
				UTerm ut = (UTerm)this.numerator.getTerms().get(this.numerator.getTerms().firstKey());
				double uCoeff = ut.getCoeff();
				double uCoeffDiff = ((uCoeff > 0) ? uCoeff - 1 : uCoeff + 1);
				
				// this single term in u-polynomial is -1 or 1
				if (ut.getPowers().size() == 0 && uCoeffDiff > -OGPConstants.EPSILON && uCoeffDiff < OGPConstants.EPSILON) {
					if (uCoeff < 0)
						sb.append("-");
				}
				else
					sb.append(stringNum);
			}
		}
		else {
			stringDen = this.denominator.print();
			if (stringNum.length() > OGPConstants.MAX_OUTPUT_POLY_CHARS_NUM ||
				stringDen.length() > OGPConstants.MAX_OUTPUT_POLY_CHARS_NUM)
				return "..."; // impossible to divide fraction
				
			sb.append("(");
			sb.append(stringNum);
			sb.append(")");
			sb.append("/");
			if (this.denominator.getTerms().size() > 1) {
				sb.append("(");
				sb.append(stringDen);
				sb.append(")");
			}
			else
				sb.append(stringDen);
		}
		
		return sb.toString();
	}
}