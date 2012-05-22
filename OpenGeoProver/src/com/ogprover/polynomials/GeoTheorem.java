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
 * <dd>Geometry theorem in algebraic form</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public class GeoTheorem {
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
	 * Theorem name
	 */
	private String name;
	/**
	 * Theorem hypotheses
	 */
	private XPolySystem hypotheses;
	/**
	 * Theorem statement
	 */
	private XPolynomial statement;
	/**
	 * Non-degenerative (NDG) conditions - they are calculated when
	 * triangulation is performed for Wu's method; therefore setter method
	 * is not required.
	 */
	private XPolySystem ndgConditions = null;
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method for setting theorem name
	 * 
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Method for getting theorem name
	 * 
	 * @return The name of theorem
	 */
	public String getName() {
		return name;
	}

	/**
	 * Method for setting theorem hypotheses
	 * 
	 * @param hypotheses The hypotheses to set
	 */
	public void setHypotheses(XPolySystem hypotheses) {
		this.hypotheses = hypotheses;
	}

	/**
	 * Method for getting theorem hypotheses
	 * 
	 * @return The hypotheses of theorem
	 */
	public XPolySystem getHypotheses() {
		return hypotheses;
	}

	/**
	 * Method for setting theorem statement
	 * 
	 * @param statement The statement to set
	 */
	public void setStatement(XPolynomial statement) {
		this.statement = statement;
	}

	/**
	 * Method for getting theorem statement
	 * 
	 * @return The statement of theorem
	 */
	public XPolynomial getStatement() {
		return statement;
	}
	
	/**
	 * Method for setting NDG conditions
	 * 
	 * @param ndgCond The ndgConditions to set
	 */
	public void setNDGConditions(XPolySystem ndgCond) {
		/* Setter method is not required
		 * 
		 */
		// this.ndgConditions = ndgCond;
	}

	/**
	 * Method for getting NDG Conditions
	 * 
	 * @return The NDG Conditions of theorem
	 */
	public XPolySystem getNDGConditions() {
		return ndgConditions;
	}
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Default constructor method
	 */
	public GeoTheorem() {
		this(null, null, null);
	}
	
	/**
	 * Constructor method
	 * 
	 * @param name	Theorem name
	 */
	public GeoTheorem(String name) {
		this(name, null, null);
	}
	
	/**
	 * Constructor method
	 * 
	 * @param hypotheses	Theorem hypotheses
	 * @param statement		Theorem statement
	 */
	public GeoTheorem(XPolySystem hypotheses, XPolynomial statement) {
		this(null, hypotheses, statement);
	}
	
	/**
	 * Constructor method
	 * 
	 * @param name			Theorem name
	 * @param hypotheses	Theorem hypotheses
	 * @param statement		Theorem statement
	 */
	public GeoTheorem(String name, XPolySystem hypotheses, XPolynomial statement) {
		this.name = name;
		
		if (hypotheses != null)
			this.hypotheses = hypotheses;
		else
			this.hypotheses = new XPolySystem(); // empty system of x-polynomials
		
		if (statement != null)
			this.statement = statement;
		else
			this.statement = new XPolynomial(); // zero polynomial
		
		// Empty polynomial system for NDG conditions
		this.ndgConditions = new XPolySystem();
	}
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method that fills polynomial system for NDG conditions after triangulation
	 * has been performed on system of hypotheses. This is used in Wu's method.
	 * 
	 * @return	SUCCESS if successful, general error code otherwise.
	 */
	public int fillNDGConditionsForWuProver() {
		ILogger logger = OpenGeoProver.settings.getLogger();
		XPolynomial one = new XPolynomial(1);
		int numOfHypotheses = (this.hypotheses != null) ? this.hypotheses.getPolynomials().size() : 0;
		
		if (numOfHypotheses == 0) {
			logger.error("System of hypotheses is empty.");
			return OGPConstants.ERR_CODE_GENERAL;
		}
		
		Vector<Integer> varList = (this.hypotheses != null) ? this.hypotheses.getVariableList() : null;	
		
		// Check if triangulation has been performed
		if (varList == null || varList.size() != numOfHypotheses) {
			logger.error("Triangulation is still not called.");
			return OGPConstants.ERR_CODE_GENERAL;
		}
		
		// first of all add trivial NDG condition - polynomial 'u1'
		/*
		UPolynomial up = new UPolynomial();
		UTerm ut = new UTerm(1);
		ut.addPower(new Power(Variable.VAR_TYPE_UX_U, 1, 1));
		up.addTerm(ut);
		XTerm xt = new XTerm(new UFraction(up));
		XPolynomial xp = new XPolynomial();
		xp.addTerm(xt);
		this.ndgConditions.addXPoly(xp);
		*/
		
		for (int ii = 0; ii < numOfHypotheses; ii++) {
			Integer intObj = varList.get(ii);
			XPolynomial leadingCoeffPoly = this.hypotheses.getXPoly(ii).getLeadingCoefficientOfVariable(intObj.intValue());
			
			// if leading coefficient is 1 it is already different from 0 therefore
			// do not add it to NDG conditions
			if (leadingCoeffPoly.equals(one))
				continue;
			
			/* 
			 * Now reduce partially this polynomial by u-term division and extract common u-factor:
			 * this way polynomial will be in form p = u_i1 * u_i2 * ... * u_in * q
			 * where q has 1 as common u-factor (u-term that multiplies all x-terms).
			 * 
			 * For each u_ik there will be separate NDG condition and also for remaining q.
			 */
			XPolynomial reducedLCPoly = ((XPolynomial)leadingCoeffPoly.clone()).reduceUTerms(false);
			
			// Calculate common u factor
			UTerm commonUFactor = null;
			
			for (Term t : reducedLCPoly.getTermsAsDescList()) {
				XTerm xt2 = (XTerm)t;
				
				for (Term ut2 : xt2.getUCoeff().getNumerator().getTermsAsDescList()) {
					if (commonUFactor == null) {
						commonUFactor = (UTerm)ut2.clone();
						commonUFactor.setCoeff(1);
					}
					else
						commonUFactor.gcd(ut2);
				}
			}
			
			if (commonUFactor == null || commonUFactor.isZero() == true) {
				logger.error("Failed to extract common u-factor from polynomial form of NDG condition.");
				return OGPConstants.ERR_CODE_GENERAL;
			}
			
			// Calculate residual polynomial
			XPolynomial reducedLCPolyResidum = (XPolynomial) reducedLCPoly.clone();
			
			for (Term xt2 : reducedLCPolyResidum.getTermsAsDescList())
				((XTerm) xt2).getUCoeff().getNumerator().divideByTerm(commonUFactor);
			
			for (Power p : commonUFactor.getPowers()) {
				// single u-variable: u_ik
				UPolynomial up2 = new UPolynomial();
				UTerm ut2 = new UTerm(1);
				ut2.addPower(p.clone());
				up2.addTerm(ut2);
				XTerm xt2 = new XTerm(new UFraction(up2));
				XPolynomial xp2 = new XPolynomial();
				xp2.addTerm(xt2);
				
				// add this new polynomial to NDG conditions only if it is not there already
				if (this.ndgConditions.getPolynomials() == null || !this.ndgConditions.getPolynomials().contains(xp2))
					this.ndgConditions.addXPoly(xp2);
			}
			
			// add residual polynomial to NDG conditions only if it is not there already and if it is not equal to 1
			if (!reducedLCPolyResidum.equals(one) && 
				(this.ndgConditions.getPolynomials() == null || !this.ndgConditions.getPolynomials().contains(reducedLCPolyResidum)))
				this.ndgConditions.addXPoly(reducedLCPolyResidum);
		}
		
		return OGPConstants.RET_CODE_SUCCESS;
	}
	
	/**
	 * Method that fills polynomial system for NDG conditions for Groebner basis prover.
	 * 
	 * @return	SUCCESS if successful, general error code otherwise.
	 */
	public int fillNDGConditionsForGroebnerBasisProver() {
		// TODO
		return OGPConstants.RET_CODE_SUCCESS;
	}
	
}
