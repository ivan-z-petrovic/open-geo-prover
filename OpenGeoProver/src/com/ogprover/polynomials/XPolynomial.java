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
import java.util.Vector;

import com.ogprover.main.OGPConstants;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.utilities.logger.ILogger;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for x-polynomial</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public class XPolynomial extends Polynomial {
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
	 * Method that retrieves the type of polynomial
	 * 
	 * @see com.ogprover.polynomials.Polynomial#getType()
	 */
	@Override
	public int getType() {
		return Polynomial.POLY_TYPE_XPOLY;
	}

	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Default Constructor method
	 */
	public XPolynomial(){
		this.terms = new TreeMap<Term, Term>(); // empty tree map collection of terms
	}
	/**
	 * Constructor of polynomial as real constant
	 * 
	 * @param cf	Real constant that represents constant polynomial
	 */
	public XPolynomial(double cf) {
		Term singleTerm = new XTerm(new UFraction(cf));
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
		Polynomial c = new XPolynomial();
		
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
			sb.append("[XPolynomial object: zero polynomial]");
		else {
			sb.append("[XPolynomial object: <terms in descending order>\n");
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
	 * @see com.ogprover.polynomials.RationalAlgebraicExpression#printToLaTeX()
	 */
	public String printToLaTeX() {
		StringBuilder sb = new StringBuilder();
		boolean firstTerm = true;
		ArrayList<Term> list = this.getTermsAsDescList();
		int ii = 0, size = list.size(), chunkSize = 0;
		
		if (size == 0)
			return "0";
		
		while (ii < size) {
			Term t = list.get(ii);
			((XTerm)t).reduce();
			String latexTerm = t.printToLaTeX();
			
			if (latexTerm.startsWith("??")) // it was impossible to print current term in several chunks - it is too long
				return "??";
			
			int len = latexTerm.length();
			int newChunkSize = chunkSize + len;
			
			if (!firstTerm && !((XTerm)t).getUCoeff().isSingleNegativeTerm())
				sb.append(" + ");
			
			if (newChunkSize <= OGPConstants.MAX_OUTPUT_POLY_CHUNK_SIZE) {
				sb.append(latexTerm);
				chunkSize = newChunkSize;
			}
			else {
				if (!firstTerm)
					sb.append("$$");  // chunk separator
				sb.append(latexTerm); // if this term is alone longer then a single chunk
									  // it is already divided in lines
				if (len > OGPConstants.MAX_OUTPUT_POLY_CHUNK_SIZE) {
					sb.append("$$");  // chunk separator
					chunkSize = 0;
				}
				else
					chunkSize = len;
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
		int size = list.size();
		
		if (size > OGPConstants.MAX_OUTPUT_POLY_TERMS_NUM)
			return "??"; // polynomial is too long for output
		
		if (size == 0) { // zero polynomial
			return "<proof_xpoly><proof_xterm><proof_ufrac><proof_ufrac_num><proof_upoly><proof_uterm><proof_coeff>0</proof_coeff></proof_uterm></proof_upoly></proof_ufrac_num></proof_ufrac></proof_xterm></proof_xpoly>";
		}
		
		sb.append("<proof_xpoly>");
		
		boolean firstTerm = true;
		
		for (int ii = 0, jj = list.size(); ii < jj; ii++) {
			XTerm xt = (XTerm)list.get(ii);
			String xmlTerm = xt.printToXML();
			
			if (xmlTerm.startsWith("??")) // polynomial is too long for output
				return "??";
			
			if (firstTerm) {
				// If coefficient of first term is non-negative, remove the + sign
				int signInd = xmlTerm.indexOf("<proof_xsign> +");
				if (signInd >= 0) {
					// skip the sign
					sb.append(xmlTerm.substring(0, xmlTerm.indexOf("<proof_xsign>")));
					sb.append(xmlTerm.substring(xmlTerm.indexOf("</proof_xsign>") + 14));
				}
				else
					sb.append(xmlTerm);
				firstTerm = false;
			}
			else
				sb.append(xmlTerm);
		}
		sb.append("</proof_xpoly>");
		
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
		ArrayList<Term> list = this.getTermsAsDescList();
		int size = list.size(), textSize = 0;
		
		if (size == 0)
			return "0";
		
		for (int ii = 0; ii < size; ii++) {
			Term t = list.get(ii);
			((XTerm)t).reduce();
			String stringTerm = t.print();
			
			if (stringTerm.startsWith("...")) {
				sb.append("+...");
				return sb.toString();
			}
			
			int len = stringTerm.length();
			
			if (ii != 0 && !((XTerm)t).getUCoeff().isSingleNegativeTerm())
				sb.append("+");
			
			if (textSize + len <= OGPConstants.MAX_OUTPUT_POLY_CHARS_NUM) {
				sb.append(stringTerm);
				textSize += len;
			}
			else {
				sb.append("+...");
				return sb.toString();
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * Reduction of this polynomial by division it with powers of u-variables.
	 * Division of denominators of fractions that are coefficients of x-terms
	 * corresponds to multiplying this polynomial by these powers of u-variables.
	 * Division of numerators can be done in two ways - to totally remove common
	 * powers of u-variables or to leave just one of each u-variable in numerators.
	 * The former will be done when making polynomials for constructions where we assume
	 * that NDG conditions are satisfied and we can safely remove all these additional
	 * u-powers. The latter will be used when translating NDG conditions from polynomial
	 * to user-friendly form.
	 * 
	 * @param bTotalReduction	Flag which determines if total reduction of u-powers
	 * 							from numerators will be done
	 * @return					This polynomial, which is result of operation, if successful 
	 * 							and null otherwise
	 */
	public XPolynomial reduceUTerms(boolean bTotalReduction) {
		UTerm numGcd = null, denGcd = null; // GCDs of all numerators and all denominators
		boolean beginNum = true, beginDen = true; // flags for beginning of calculation
		boolean calcNumGcd = true, calcDenGcd = true; // flags for continuing of calculation
		
		// no reduction should be done on zero polynomial
		if (this.terms.size() == 0)
			return this;
		
		// perform simple reduction of each x-term by division of 
		// numerator and denominator from u-fraction by their gcd
		for (Term t : this.getTermsAsDescList())
			((XTerm)t).reduce();
		
		// pass term list and calculate GCD of all numerators and GCD of all denominators
		for (Term t : this.getTermsAsDescList()) {
			UFraction coeff = ((XTerm)t).getUCoeff();
			
			if (calcNumGcd) {
				// at least one term must exist - otherwise numerator is zero
				// and therefore x-term is zero and assumption is that no zero terms
				// are held in this x-polynomial
				for (Term s : coeff.getNumerator().getTermsAsDescList()) {
					if (beginNum) {
						numGcd = (UTerm)s.clone();
						numGcd.setCoeff(1);
						beginNum = false;
					}
					else
						numGcd.gcd(s);
				}
			
				// if current gcd is trivial, further calculation will be stopped
				if (numGcd.getPowers().size() == 0)
					calcNumGcd = false;
			}
			
			if (calcDenGcd) {
				// at least one term must exist - otherwise denominator is zero
				// and therefore x-term is not valid and assumption is that no invalid 
				// terms are held in this x-polynomial
				for (Term s : coeff.getDenominator().getTermsAsDescList()) {
					if (beginDen) {
						denGcd = (UTerm)s.clone();
						denGcd.setCoeff(1);
						beginDen = false;
					}
					else
						denGcd.gcd(s);
				}
			
				// if current gcd is trivial, further calculation will be stopped
				if (denGcd.getPowers().size() == 0)
					calcDenGcd = false;
			}
		}
		
		if (numGcd == null || denGcd == null) {
			OpenGeoProver.settings.getLogger().error("Failed to calculate GCD of u-coefficients from terms of x-polynomial");
			return null;
		}
		
		// If only partial reduction is performed, decrease power of each u-variable from
		// numerators' GCD by one
		if (!bTotalReduction) {
			UTerm tempUT = new UTerm(numGcd.getCoeff());
			
			for (Power uPow : numGcd.getPowers()) {
				if (uPow.getExponent() > 1) {
					Power tempUP = uPow.clone();
					tempUP.addToExponent(-1);
					tempUT.addPower(tempUP);
				}
			}
			
			numGcd = tempUT;
		}
		
		// perform the reduction by division with GCDs
		for (Term t : this.getTermsAsDescList()) {
			UFraction coeff = ((XTerm)t).getUCoeff();
			
			coeff.getNumerator().divideByTerm(numGcd);
			coeff.getDenominator().divideByTerm(denGcd);
		}
		
		// create monic polynomial by dividing it with double coefficient from 
		// leading u-term of numerator of u-fraction which is coefficient of leading 
		// x-term
		double leadingDoubleCoeff = ((UTerm)((XTerm)this.getTermsAsDescList().get(0)).getUCoeff().getNumerator().getTermsAsDescList().get(0)).getCoeff();
		if (leadingDoubleCoeff > -OGPConstants.EPSILON && leadingDoubleCoeff < OGPConstants.EPSILON) {
			OpenGeoProver.settings.getLogger().error("Attempt to divide by zero - leading coefficient must not be zero since zero terms are not kept in memory");
			return null;
		}
		this.multiplyByRealConstant(1/leadingDoubleCoeff);
		
		return this;
	}
	
	/**
	 * Method for reduction of x-polynomial by division of each u-coefficient (fraction)
	 * by simple common divisor of form A/B where A and B are u-terms.
	 * A is actually GCD of all numerators from coefficients of x-terms and B is
	 * GCD of all denominators. This polynomial will be changed at the end of operation.
	 * Also, the coefficient from leading u-term of numerator of u-fraction from leading
	 * x-term will be taken to divide all x-terms in order to get monic form of x-polynomial.
	 * 
	 * E.g. polynomial (u2u1 + u1)x2/(u3^2 + u3u2) + (u1^2 + u1)x1/(u4u3 - u3) 
	 * will be reduced by division with u1/u3 to following polynomial:
	 * (u2 + 1)x2/(u3 + u2) + (u1 + 1)x1/(u4 - 1).
	 * 
	 * @return	This polynomial, which is result of operation, if successful and null otherwise
	 */
	public XPolynomial reduceByUTermDivision() {
		return this.reduceUTerms(true);
	}
	
	/**
	 * Method used in pseudo reminder calculation. 
	 * It searches for all terms in this polynomial
	 * that contain variable with given index and maximal 
	 * possible exponent of that variable in this polynomial.
	 * 
	 * @param varIndex		Index of x variable that is searched for.
	 * @param expDecr		Exponent decrement value that is applied
	 * 						to given variable when making leading coefficient. 
	 * 						Pure leading coefficient doesn't contain power of 
	 * 						variable with given index, but sometimes it is necessary
	 * 						to multiply that coefficient with smaller power of
	 * 						this variable and that is why this argument is used for;
	 * 						like a kind of efficient work - in one pass to find
	 * 						leading coefficient and multiply it with power of variable.
	 * 						Naturally, this value is non-negative, thus a value of -1
	 * 						has meaning to make pure leading coefficient, i.e. remove
	 * 						power of given variable from its terms. This value can be
	 * 						at the most equals to maximal exponent of given variable.
	 * 						When searching for maximal exponent, this value must be
	 * 						at least equals to exponent decrement.
	 * @param leadingCoeff	Polynomial which is standing as coefficient near maximal
	 * 						power of given variable; it is eventually multiplied by
	 * 						smaller power of that variable.
	 * @return				Method returns maximal exponent of given variable in this 
	 * 						polynomial, but it also retrieves its leading coefficient
	 * 						through last argument. In case of error, -1 is returned
	 * 						since exponent of variable in polynomial is non-negative value.
	 * 						Zero as return value is special case - that means that variable
	 * 						with given index is not found in this polynomial, or that all its
	 * 						exponents are not greater than decrement. In both cases
	 * 						whole this polynomial will represent leading coefficient
	 * 						and that will provide the end of successive pseudo division 
	 * 						processes. If decrement is -1, return value of zero only
	 * 						has meaning that variable is not found in polynomial at all.
	 */
	// For example if this polynomial is (x_7)(x_3) + (x_6)(x_3)^2 + (x_5)^5 + 2(u_1)(x_3)^2 + 1
	// then this.getLeadingExpAndCoeff(3, 1, leadingCoeff) returns 2 (maximal exponent of x_3 variable 
	// is 2) and leading coefficient, multiplied by (x_3) since maximal exponent is decreased by 1, is:
	// (x_6)(x_3) + 2(u_1)(x_3).
	private int getLeadingExpAndCoeff(int varIndex, int expDecr, XPolynomial leadingCoeff) {
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (leadingCoeff == null) {
			logger.error("Passed null polynomial - no place where to store terms");
			return OGPConstants.ERR_CODE_NULL; 
		}
		
		ArrayList<Term> termList = this.getTermsAsDescList(); // all terms in descending order
		int maxExp = 0; // value of maximal exponent of given variable
		Vector<Term> coeff = null; // terms from this polynomial that will make resulting leading coefficient
		int currDecr = 0; // current value of decrement - it depends on maximal exponent
		boolean allProcessed = false; // to check whether all terms with given variable have been processed
		
		// pass all terms from this polynomial until all terms with given variable are processed
		for (int ii = 0, jj = termList.size(); ii < jj && !allProcessed; ii++) {
			Term currT = termList.get(ii);
			
			if (currT == null) {
				logger.error("Null object found when expected non-null value");
				return OGPConstants.ERR_CODE_NULL;
			}
			int currExp = currT.getVariableExponent(varIndex); // exponent of given variable in current term
			
			// found variable in current term
			if (currExp > 0) {
				// found exponent must be greater than exponent decrement
				if (currExp >= expDecr) {
					// if current exponent is equals to maximal, add term to 
					// vector of terms that will make leading coefficient
					if (currExp == maxExp) {
						if (coeff == null)
							coeff = new Vector<Term>();
						// add copy of term, with changed exponent of given variable,
						// at the end of vector of terms;
						// this way the descending order will be preserved
						if (currDecr == 0) // no decrement
							coeff.add(currT.clone());
						else
							coeff.add(currT.clone().changePowerExponent(varIndex, -currDecr));
					}
					else if (currExp > maxExp) { // found new maximal value
						maxExp = currExp;
						// if expDecr < 0 (i.e. -1), totally remove power of given variable
						currDecr = ((expDecr >= 0) ? expDecr : maxExp);
						// disregard all previous terms from this collection
						coeff = new Vector<Term>(); 
					
						// add copy of term, with changed exponent of given variable,
						// at the end of vector of terms;
						// this way the descending order will be preserved
						if (currDecr == 0)
							coeff.add(currT.clone());
						else
							coeff.add(currT.clone().changePowerExponent(varIndex, -currDecr));
					}
				}
			}
			// if current term doesn't contain variable and its greatest (first)
			// power is for variable with smaller index than the index of given 
			// variable, that means all terms with given variable have been 
			// processed since terms are sorted in descending order
			else if (currT.getPowers().size() == 0 || currT.getPowers().get(0).getIndex() < varIndex) 
				allProcessed = true;
		}
		
		// Fill in leading coefficient polynomial
		// but only if terms with given variable were found
		// and if passed in exponent decrement is not greater
		// than maximal exponent.
		if (maxExp > 0) {
			for (int ii = 0, jj = coeff.size(); ii < jj; ii++)
				leadingCoeff.addTerm(coeff.get(ii)); // no need to clone objects
		}
		
		return maxExp;
	}
	
	/**
	 * Method for calculation of pseudo reminder of this polynomial with 
	 * another passed in polynomial over variable with passed in index.
	 * 
	 * @param p			Polynomial which this polynomial is pseudo divided by
	 * @param varIndex	Index of variable over which pseudo division is performed
	 * @return			This polynomial which is result of operation -
	 * 					pseudo reminder (this = prem(this, p, varIndex)), or null
	 * 					in case of error
	 */
	public XPolynomial pseudoReminder(XPolynomial p, int varIndex) {
		if (p == null) {
			OpenGeoProver.settings.logGeneralErrorInPseudoDivision("Pseudo division error: Null polynomial passed in.");
			return null;
		}
		
		if (varIndex <= 0) {
			OpenGeoProver.settings.logGeneralErrorInPseudoDivision("Pseudo division error: Bad variable index passed in - it should be positive.");
			return null;
		}
		
		/*
		 * Update space measure
		 */
		int sizeOfThis = this.getTerms().size();
		if (sizeOfThis > OpenGeoProver.settings.getMaxNumOfTerms()) {
			OpenGeoProver.settings.setMaxNumOfTerms(sizeOfThis);
		}
		int sizeOfP = p.getTerms().size();
		if (sizeOfP > OpenGeoProver.settings.getMaxNumOfTerms()) {
			OpenGeoProver.settings.setMaxNumOfTerms(sizeOfP);
		}
		
		// first of all calculate exponent and (pure) leading coefficient in
		// passed in polynomial over variable with given index
		XPolynomial pc = new XPolynomial();
		int pe = p.getLeadingExpAndCoeff(varIndex, -1, pc);
		
		if (pe < 0) { // error has occurred
			OpenGeoProver.settings.logGeneralErrorInPseudoDivision("Pseudo division error: Failed to get leading exponent and coefficient.");
			return null;
		}
		
		if (pe == 0) { // given variable not found in polynomial p
			// in this case polynomial p is constant as polynomial by
			// given variable and thus reminder is zero polynomial
			this.terms = new TreeMap<Term, Term>();
			return this;
		}
		
		boolean canProceed;
		XPolynomial reminder = this; // initial reminder is this polynomial
		
		do {
			canProceed = false;
			// now do same for this polynomial as for p, passing in value of 
			// previously calculated exponent from p as decrement value
			XPolynomial rc = new XPolynomial();
			int re = reminder.getLeadingExpAndCoeff(varIndex, pe, rc);
			
			if (re < 0) { // error has occurred
				OpenGeoProver.settings.logGeneralErrorInPseudoDivision("Pseudo division error: Failed to get leading exponent and coefficient.");
				return null;
			}
			
			// if re >= pe, since pe > 0 here, that means re > 0 so
			// rc contains calculated leading coefficient
			if (re >= pe) { // one step of pseudo division can be done
				canProceed = true; // after division, one more loop step will be done
				// calculate new reminder
				reminder = (XPolynomial)reminder.multiplyByPolynomial(pc).subtractPolynomial(p.clone().multiplyByPolynomial(rc));
				/*
				XPolynomial tempP = (XPolynomial) p.clone();
				tempP.multiplyByPolynomial(rc);
				reminder.multiplyByPolynomial(pc).subtractPolynomial(tempP);
				tempP = null;
				Runtime.getRuntime().gc();
				*/ // used for better memory management
			}
			
			/*
			 * Check space and time limits and update space measure
			 */
			int sizeOfRem = reminder.getTerms().size();
			if (sizeOfRem > OpenGeoProver.settings.getParameters().getSpaceLimit()) {
				OpenGeoProver.settings.logSpaceErrorInPseudoDivision(sizeOfRem);
				return null;
			}
			if (OpenGeoProver.settings.getTimer().isTimeIsUp()) {
				OpenGeoProver.settings.logTimeErrorInPseudoDivision();
				return null;
			}
			if (sizeOfRem > OpenGeoProver.settings.getMaxNumOfTerms()) {
				OpenGeoProver.settings.setMaxNumOfTerms(sizeOfRem);
			}
		} while (canProceed);
		
		// if exponent from this polynomial is smaller than the exponent
		// from p, then result of pseudo reminder is this polynomial
		return this; // this is same as reminder reference
	}
	
	/**
	 * Method that gives greatest exponent of given variable in this polynomial.
	 *  
	 * @param varIndex	Index of variable that is searched for
	 * @return			Greatest exponent of variable (0 means polynomial doesn't
	 * 					contain this variable) or error code (< 0)
	 */
	public int getLeadingExp(int varIndex) {
		ArrayList<Term> termList = this.getTermsAsDescList(); // all terms in descending order
		int maxExp = 0; // value of maximal exponent of given variable
		boolean allProcessed = false; // to check whether all terms with given variable have been processed
		
		// pass all terms from this polynomial until all terms with given variable are processed
		for (int ii = 0, jj = termList.size(); ii < jj && !allProcessed; ii++) {
			Term currT = termList.get(ii);
			
			if (currT == null) {
				OpenGeoProver.settings.getLogger().error("Null object found when expected non-null value");
				return OGPConstants.ERR_CODE_NULL;
			}
			int currExp = currT.getVariableExponent(varIndex); // exponent of given variable in current term
			
			// found variable in current term
			if (currExp > 0) {
				if (currExp > maxExp) // found new maximal value
					maxExp = currExp;
			}
			// if current term doesn't contain variable and its greatest (first)
			// power is for variable with smaller index than the index of given 
			// variable, that means all terms with given variable have been 
			// processed since terms are sorted in descending order
			else {
				Vector<Power> powers = currT.getPowers();
				Power currP = (powers.size() > 0) ? powers.get(0) : null;
				if (currP == null || currP.getIndex() < varIndex) 
					allProcessed = true;
			}
		}
		
		return maxExp;
	}
	
	/**
	 * Method that returns polynomial which is leading coefficient
	 * in this polynomial, of given x-variable.
	 * E.g. in polynomial <br>
	 *    x5^4*x4^3*x3 + x4^2*x3^2 - 2u1*x3^2*x2^4 + (3u3^4 - u1)*x3^2*x2 + x2*x1 + 4u2 = 0
	 *    <br> 
	 * leading coefficient of variable x3 is:
	 * 	x4^2 - 2u1*x2^4 + (3u3^4 - u1)*x2
	 *  
	 * @param xVarIndex		Index of x-variable whose leading coefficient is calculated.
	 * @return				X-polynomial which is leading coefficient in this polynomial,
	 *                      of given x-variable 
	 */
	public XPolynomial getLeadingCoefficientOfVariable(int xVarIndex) {
		XPolynomial leadingCoeffXPoly = new XPolynomial(); // empty i.e. zero polynomial
		
		this.getLeadingExpAndCoeff(xVarIndex, -1, leadingCoeffXPoly); 
		// disregard return value of previous method - if it is <= 0 leading 
		// coefficient will remain empty i.e. zero polynomial: no variable in 
		// this polynomial ==> zero is leading coefficient 
		
		return leadingCoeffXPoly;
	}
	
	/**
	 * Method that extracts all u and x variables from this polynomial.
	 * 
	 * @return	List of all u and x variables from this polynomial.
	 */
	public Vector<UXVariable> extractAllVariables() {
		Map<String, UXVariable> varMap = new HashMap<String, UXVariable>();
		
		for (Term t : this.getTermsAsDescList()) {
			XTerm xt = (XTerm)t;
			UFraction uf = xt.getUCoeff();
			UPolynomial uNum = uf.getNumerator();
			UPolynomial uDen = uf.getDenominator();
			
			for (Term u : uNum.getTermsAsDescList()) {
				UTerm ut = (UTerm)u;
				
				for (Power p : ut.getPowers()) {
					UXVariable uxVar = (UXVariable) p.getVariable(); // safe cast
					String uxvKey = uxVar.toString();
					
					if (!varMap.containsKey(uxvKey))
						varMap.put(uxvKey, uxVar);
				}
			}
			
			for (Term u : uDen.getTermsAsDescList()) {
				UTerm ut = (UTerm)u;
				
				for (Power p : ut.getPowers()) {
					UXVariable uxVar = (UXVariable) p.getVariable();  // safe cast
					String uxvKey = uxVar.toString();
					
					if (!varMap.containsKey(uxvKey))
						varMap.put(uxvKey, uxVar);
				}
			}
			
			for (Power p : xt.getPowers()) {
				UXVariable uxVar = (UXVariable) p.getVariable(); // safe cast
				String uxvKey = uxVar.toString();
				
				if (!varMap.containsKey(uxvKey))
					varMap.put(uxvKey, uxVar);
			}
		}
		
		Collection<UXVariable> allVars = varMap.values();
		
		if (allVars == null)
			return null;
		
		return new Vector<UXVariable>(allVars);
	}
	
	/**
	 * Method that checks if this polynomial matches passed in polynomial
	 * which is algebraic form of some NDG condition.
	 * 
	 * @param ndgcPoly	Polynomial form of NDG condition - non-zero polynomial which is
	 * 					reduced by u-term division (removed sufficient u-terms)
	 * @return			True if this polynomial corresponds to passed in polynomial
	 * 					form of NDG condition.
	 */
	public boolean matchesNDGCPolynomial(XPolynomial ndgcPoly) {
		// If this polynomial is zero, it cannot match polynomial form of NDG condition
		// since that polynomial is non-zero
		if (this.isZero() == true)
			return false;
		
		// This polynomial represents algebraic form of specific position of some points
		XPolynomial positionPoly = ((XPolynomial) this.clone()).reduceUTerms(false);
		XPolynomial conditionPoly = (XPolynomial) ndgcPoly.clone();
		
		// Calculate u factor of position polynomial
		UTerm posPolyUFactor = null;
		
		for (Term t : positionPoly.getTermsAsDescList()) {
			XTerm xt = (XTerm)t;
			
			for (Term ut : xt.getUCoeff().getNumerator().getTermsAsDescList()) {
				if (posPolyUFactor == null) {
					posPolyUFactor = (UTerm)ut.clone();
					posPolyUFactor.setCoeff(1);
				}
				else
					posPolyUFactor.gcd(ut);
			}
		}
		
		// Calculate residual polynomial for position polynomial
		XPolynomial posPolyResidum = (XPolynomial) positionPoly.clone();
		
		for (Term xt : posPolyResidum.getTermsAsDescList())
			((XTerm) xt).getUCoeff().getNumerator().divideByTerm(posPolyUFactor);
		
		/*
		 * Each single u-variable and residual polynomial should be compared 
		 * with polynomial for NDGC. If any of these polynomials is equal to 
		 * polynomial form of NDGC, that means that in certain occasions this 
		 * position can generate specified degenerative condition.
		 */
		for (Power p : posPolyUFactor.getPowers()) {
			// single u-variable
			UPolynomial up = new UPolynomial();
			UTerm ut = new UTerm(1);
			ut.addPower(p.clone());
			up.addTerm(ut);
			XTerm xt = new XTerm(new UFraction(up));
			XPolynomial xp = new XPolynomial();
			xp.addTerm(xt);
			
			if (xp.equals(conditionPoly))
				return true; // found a match
		}
		
		// special case - if NDGC polynomial is single term polynomial, check whether position polynomial is divisible by it
		if (conditionPoly.getTerms().size() == 1) {
			boolean bDivisible = true;
			Term singleTerm = conditionPoly.getTermsAsDescList().get(0);
			
			for (Term xt : posPolyResidum.getTermsAsDescList()) {
				if (!xt.isDivisibleByTerm(singleTerm)) {
					bDivisible = false;
					break;
				}
			}
		
			if (bDivisible)
				return true;
		}
		
		return posPolyResidum.equals(conditionPoly);
	}
}