/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.polynomials;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.ogprover.main.OGPConstants;
import com.ogprover.main.OpenGeoProver;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for symbolic polynomials</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public class SymbolicPolynomial extends Polynomial {
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
	
	// Constants for numerator and denominator of first derivative of symbolic polynomial
	/**
	 * <i><b>
	 * Index of symbolic polynomial in array of two polynomials, representing the numerator
	 * of first derivative of symbolic polynomial, by some point
	 * </b></i>
	 */
	public static final int FIRST_DERIVATIVE_NUMERATOR = 0;
	/**
	 * <i><b>
	 * Index of symbolic polynomial in array of two polynomials, representing the denominator
	 * of first derivative of symbolic polynomial, by some point
	 * </b></i>
	 */
	public static final int FIRST_DERIVATIVE_DENOMINATOR = 1;
	
	
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
		return Polynomial.POLY_TYPE_SYMBOLIC;
	}
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Default constructor method
	 */
	public SymbolicPolynomial(){
		this.terms = new TreeMap<Term, Term>(); // empty tree map collection of terms
	}
	/**
	 * Constructor method (constructs polynomial from real constant)
	 * 
	 * @param cf	real constant that represents constant polynomial
	 */
	public SymbolicPolynomial(double cf) {
		Term singleTerm = new SymbolicTerm(cf);
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
		Polynomial c = new SymbolicPolynomial();
		
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
			sb.append("[SymbPolynomial object: zero polynomial]");
		else {
			sb.append("[SymbPolynomial object: <terms in descending order>\n");
			while (ii < size) {
				sb.append("\t" + list.get(ii).toString() + "\n");
				ii++;
			}
			sb.append("]");
		}
		
		return sb.toString();
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	// Necessary for using these objects as keys of HashMaps
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * @see com.ogprover.polynomials.RationalAlgebraicExpression#printToLaTeX()
	 */
	public String printToLaTeX() {
		StringBuilder sb = new StringBuilder();
		boolean firstTerm = true;
		ArrayList<Term> list = this.getTermsAsDescList();
		int ii = 0, size = list.size(), chunkSize = 0, newChunkSize = 0;
		
		while (ii < size) {
			Term t = list.get(ii);
			String latexTerm = t.printToLaTeX();
			int len = latexTerm.length();
			newChunkSize = chunkSize + len;
			
			if (latexTerm.startsWith("??"))
				return "??"; // term is too long for output
			
			if (!firstTerm && ((SymbolicTerm)t).getCoeff() > 0)
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
		
		sb.append("<proof_spoly>");
		
		boolean firstTerm = true;
		
		for (int ii = 0, jj = list.size(); ii < jj; ii++) {
			SymbolicTerm st = (SymbolicTerm)list.get(ii);
			String xmlTerm = st.printToXML();
			
			if (xmlTerm.startsWith("??")) // polynomial is too long for output
				return "??";
			
			if (firstTerm) {
				// If coefficient of first term is non-negative, remove the + sign
				if (st.getCoeff() >= 0) {
					// skip the sign
					sb.append(xmlTerm.substring(0, xmlTerm.indexOf("<proof_sign>")));
					sb.append(xmlTerm.substring(xmlTerm.indexOf("</proof_sign>") + 13));
				}
				else
					sb.append(xmlTerm);
				firstTerm = false;
			}
			else
				sb.append(xmlTerm);
		}
		sb.append("</proof_spoly>");
		
		String xmlText = sb.toString();
		
		if (xmlText.length() > OGPConstants.MAX_XML_OUTPUT_POLY_CHARS_NUM)
			return "??"; // polynomial is too long for output
		
		return xmlText;
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
	
	/**
	 * Method for simplifying this symbolic polynomial, by
	 * merging all same powers and terms. For example:
	 * polynomial xA * yB * xA + xA^2 * yB + xC
	 * will be simplified by: 2 * XA^2 * yB + xC.
	 * 
	 * These polynomials can appear as result of renaming of some
	 * labels of symbolic variables in symbolic polynomials.
	 * For example: if in polynomial xA * yB * XM + xA^2 * yB + xC
	 * label M is replaced by label A, polynomial from above condition
	 * will be obtained and therefore it will require simplification.
	 * 
	 * @param unsortedListOfTerms	List of all terms from this polynomial
	 * 								which is result of renaming (terms are 
	 * 								unsorted as well as powers in each term)
	 * 
	 * @return						This polynomial which is result of operation.
	 */
	private SymbolicPolynomial simplify(ArrayList<Term> unsortedListOfTerms) {
		this.terms = new TreeMap<Term, Term>(); // new empty map of terms
		
		for (Term symTerm : unsortedListOfTerms) {
			Term newSymTerm = new SymbolicTerm(((SymbolicTerm)symTerm).getCoeff());
			
			for (Power symPower: symTerm.getPowers())
				newSymTerm.addPower(symPower.clone());
			
			this.addTerm(newSymTerm);
		}
		
		return this;
	}
	
	/**
	 * Method that substitutes labels of variables in this 
	 * symbolic polynomial with new labels. It is used when
	 * some symbolic polynomial has to be applied to some
	 * specific set of points (e.g. general condition for 
	 * line through two points will be for line AB and point M0
	 * on it, but when need to be applied to line CD and point
	 * E on it, these general labels have to be substituted).
	 *  
	 * @param labelsMap	Map of labels where each new label is mapped to 
	 * 					some old label
	 * @return			This polynomial which is result of operation or
	 * 					null in case of error
	 */
	public SymbolicPolynomial substitute(Map<String, String> labelsMap) {
		/*
		 * To substitute labels in symbolic polynomial we first must
		 * rewrite all terms in separate list and will rename labels
		 * there, then return them back to polynomial. The reason why
		 * this is necessary is because after renaming directly in tree
		 * of polynomials, tree structure (i.e. the order of terms) will
		 * be broken. 
		 */
		ArrayList<Term> tempListOfTerms = new ArrayList<Term>();
		
		for (Term t : this.getTermsAsDescList())
			tempListOfTerms.add(t.clone());
		
		for (Term t : tempListOfTerms) {
			for (Power p : t.getPowers()) {
				SymbolicVariable sv = (SymbolicVariable) p.getVariable();
				String replacementLabel = labelsMap.get(sv.getPointLabel());
				
				if (replacementLabel != null)
					sv.setPointLabel(replacementLabel);
				else {
					OpenGeoProver.settings.getLogger().error("Failed to substitute label " + sv.getPointLabel());
					return null;
				}
			}
		}
		
		return this.simplify(tempListOfTerms);
	}
	
	/**
	 * Method that substitutes label of variable in this 
	 * symbolic polynomial with new label. It is used when
	 * some symbolic polynomial has to be applied to some
	 * specific set of points (e.g. general condition for 
	 * line through two points will be for line AB and point M0
	 * on it, but when need to be applied to line AB and point
	 * E on it, these general label M0 has to be substituted by E).
	 *  
	 * @param srcLabel	Original label that has to be substituted
	 * @param destLabel Replacement label
	 * @return			This polynomial which is result of operation or
	 * 					null in case of error
	 */
	public SymbolicPolynomial substitute(String destLabel, String srcLabel) {
		if (destLabel == null || srcLabel == null) {
			OpenGeoProver.settings.getLogger().error("Null label passed in");
			return null;
		}
		
		/*
		 * To substitute labels in symbolic polynomial we first must
		 * rewrite all terms in separate list and will rename labels
		 * there, then return them back to polynomial. The reason why
		 * this is necessary is because after renaming directly in tree
		 * of polynomials, tree structure (i.e. the order of terms) will
		 * be broken. 
		 */
		ArrayList<Term> tempListOfTerms = new ArrayList<Term>();
		
		for (Term t : this.getTermsAsDescList())
			tempListOfTerms.add(t.clone());
		
		for (Term t : tempListOfTerms) {
			for (Power p : t.getPowers()) {
				SymbolicVariable sv = (SymbolicVariable) p.getVariable();
				
				if (sv.getPointLabel().equals(srcLabel))
					sv.setPointLabel(destLabel);
			}
		}
		
		return this.simplify(tempListOfTerms);
	}
	
	/**
	 * This method calculates first derivative of symbolic polynomial by some point.
	 * It will be returned as fraction. 
	 * E.g. if we want o calculate the first derivative of symbolic polynomial
	 * 		xB*yA^3*xC + 2*xA^2*yC^2*xC - 3*yC + 4*XA = 0
	 * by point C, then we first obtain following equation (by derivation):
	 * 		xB*yA^3 + 2*xA^2*yC^2 + 4*xA^2*yC*yC'*xC - 3*yC' = 0
	 * and hence following fraction as first derivative by point C:
	 * 		numerator = -xB*yA^3 - 2*xA^2*yC^2
	 * 		denominator = 4*xA^2*yC*xC - 3
	 * 
	 * @param pointLabel	Label of point by which we calculate the first derivative
	 * @return				Array of two symbolic polynomials - one represents the 
	 * 						numerator and another the denominator of first derivative;
	 * 						in case of error null is return value
	 */
	public ArrayList<SymbolicPolynomial> calcFirstDerivativeByPoint(String pointLabel){
		ArrayList<SymbolicPolynomial> retArr = new ArrayList<SymbolicPolynomial>();
		SymbolicPolynomial numerator = new SymbolicPolynomial();
		SymbolicPolynomial denominator = new SymbolicPolynomial();
		
		SymbolicTerm numeratorPart = null;
		SymbolicTerm denominatorPart = null;
		SymbolicTerm commonPart = null;
		Power xPower = null;
		Power yPower = null;
		
		for (Term term : this.getTermsAsDescList()) {
			SymbolicTerm symbTerm = (SymbolicTerm)term;
			commonPart = (SymbolicTerm) symbTerm.clone();
			
			xPower = null;
			yPower = null;
			
			for (Power pow : symbTerm.getPowers()) {
				SymbolicVariable symbVar = (SymbolicVariable) pow.getVariable();
				SymbolicTerm tempTerm = null;
				
				if (symbVar.getPointLabel().equals(pointLabel)) {
					tempTerm = new SymbolicTerm(1);
					
					if (symbVar.getVariableType() == SymbolicVariable.VAR_TYPE_SYMB_X) {
						xPower = pow.clone();
						tempTerm.addPower(xPower);
					}
					else if (symbVar.getVariableType() == SymbolicVariable.VAR_TYPE_SYMB_Y) {
						yPower = pow.clone();
						tempTerm.addPower(yPower);
					}
					
					commonPart.divide(tempTerm);
				}
			}
			
			if (xPower == null) {
				if (yPower == null) {
					// derivative of current term by specified point is zero
				}
				else {
					// (yP^n)' = n*(yP^(n-1))*yP'
					int yExp = yPower.getExponent();
					commonPart.mul(yExp);
					if (yExp > 1) {
						yPower.addToExponent(-1);
						commonPart.addPower(yPower);
					}
					denominator.addTerm(commonPart);
				}
			}
			else {
				if (yPower == null) {
					// (xP^n)' = n*(xP^(n-1))
					int xExp = xPower.getExponent();
					commonPart.mul(xExp);
					if (xExp > 1) {
						xPower.addToExponent(-1);
						commonPart.addPower(xPower);
					}
					numerator.addTerm(commonPart);
				}
				else {
					// (xP^n*yP^m)' = n*(xP^(n-1))*yP^m + m*xP^n*yP^(m-1)*yP'
					
					// n*(xP^(n-1))*yP^m
					Power tempPower = xPower.clone();
					numeratorPart = (SymbolicTerm) commonPart.clone();
					numeratorPart.addPower(yPower.clone());
					int xExp = tempPower.getExponent();
					numeratorPart.mul(xExp);
					if (xExp > 1) {
						tempPower.addToExponent(-1);
						numeratorPart.addPower(tempPower);
					}
					numerator.addTerm(numeratorPart);
					
					// m*xP^n*yP^(m-1)
					tempPower = yPower.clone();
					denominatorPart = (SymbolicTerm) commonPart.clone();
					denominatorPart.addPower(xPower.clone());
					int yExp = tempPower.getExponent();
					denominatorPart.mul(yExp);
					if (yExp > 1) {
						tempPower.addToExponent(-1);
						denominatorPart.addPower(tempPower);
					}
					denominator.addTerm(denominatorPart);
				}
			}
		}
		
		// move all numerator terms to the right side of equality i.e. multiply
		// all terms by -1 (change the sign) to get numerator of fraction
		numerator.multiplyByRealConstant(-1);
		retArr.add(SymbolicPolynomial.FIRST_DERIVATIVE_NUMERATOR, numerator);
		retArr.add(SymbolicPolynomial.FIRST_DERIVATIVE_DENOMINATOR, denominator);
		return retArr;
	}
	
	/**
	 * Method that retrieves the list of all point labels
	 * used in this symbolic polynomial.
	 * 
	 * @return	List of point labels
	 */
	public ArrayList<String> getAllPointLabels() {
		Map<String, String> allLabelsMap = new HashMap<String, String>();
		
		for (Term t : this.getTermsAsDescList()) {
			for (Power p : t.getPowers()) {
				SymbolicVariable sv = (SymbolicVariable)p.getVariable().clone();
				allLabelsMap.put(sv.getPointLabel(), sv.getPointLabel());
			}
		}
		
		return new ArrayList<String>(allLabelsMap.values());
	}
	
}