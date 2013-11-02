/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.auxiliary;

import java.util.HashMap;
import java.util.Map;

import com.ogprover.polynomials.Power;
import com.ogprover.polynomials.SymbolicPolynomial;
import com.ogprover.polynomials.SymbolicTerm;
import com.ogprover.polynomials.SymbolicVariable;
import com.ogprover.polynomials.Term;
import com.ogprover.polynomials.Variable;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.polynomials.XTerm;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.pp.tp.geoobject.Segment;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for ratio of two collinear segments</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class RatioOfTwoCollinearSegments {
	/*
	 * Segments of this ratio are collinear i.e. they are on same line
	 * or on parallel lines.
	 */
	
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
	 * <i><b>Symbolic label for first end point of numerator segment</b></i>
	 */
	private static final String ALabel = "A";
	/**
	 * <i><b>Symbolic label for second end point of numerator segment</b></i>
	 */
	private static final String BLabel = "B";
	/**
	 * <i><b>Symbolic label for first end point of denominator segment</b></i>
	 */
	private static final String CLabel = "C";
	/**
	 * <i><b>Symbolic label for second end point of denominator segment</b></i>
	 */
	private static final String DLabel = "D";
	// Symbolic polynomials
	/**
	 * <i>
	 * Numerator of ratio by x coordinates
	 * </i>
	 */
	private static SymbolicPolynomial xNumerator = null;
	/**
	 * <i>
	 * Numerator of ratio by y coordinates
	 * </i>
	 */
	private static SymbolicPolynomial yNumerator = null;
	/**
	 * <i>
	 * Denominator of ratio by x coordinates
	 * </i>
	 */
	private static SymbolicPolynomial xDenominator = null;
	/**
	 * <i>
	 * Denominator of ratio by y coordinates
	 * </i>
	 */
	private static SymbolicPolynomial yDenominator = null;
	// Instantiated Polynomials
	private XPolynomial numerator = null;
	private XPolynomial denominator = null;
	
	// Collinear segments that make this ratio
	private Segment numeratorSegment = null;
	private Segment denominatorSegment = null;
	
	
	// Static initializer of condition member 
	static {
		/*
		 * Ratio of oriented collinear segments AB and CD is
		 * vector(AB) : vector(CD)
		 * 	by x coordinates it becomes
		 * 		(xB - xA)/(xD - xC)
		 *  by y coordinates it becomes
		 * 		(yB - yA)/(yD - yC)
		 */
		if (xNumerator == null) {
			xNumerator = new SymbolicPolynomial();
			
			// Instances of symbolic variables
			SymbolicVariable xA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, ALabel);
			SymbolicVariable xB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, BLabel);
			
			// term xB
			Term t = new SymbolicTerm(1);
			t.addPower(new Power(xB, 1));
			xNumerator.addTerm(t);
			
			// term -xA
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xA, 1));
			xNumerator.addTerm(t);
		}
		
		if (yNumerator == null) {
			yNumerator = new SymbolicPolynomial();
			
			// Instances of symbolic variables
			SymbolicVariable yA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, ALabel);
			SymbolicVariable yB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, BLabel);
			
			// term yB
			Term t = new SymbolicTerm(1);
			t.addPower(new Power(yB, 1));
			yNumerator.addTerm(t);
			
			// term -yA
			t = new SymbolicTerm(-1);
			t.addPower(new Power(yA, 1));
			yNumerator.addTerm(t);
		}
		
		if (xDenominator == null) {
			xDenominator = new SymbolicPolynomial();
			
			// Instances of symbolic variables
			SymbolicVariable xC = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, CLabel);
			SymbolicVariable xD = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, DLabel);
			
			// term xD
			Term t = new SymbolicTerm(1);
			t.addPower(new Power(xD, 1));
			xDenominator.addTerm(t);
			
			// term -xC
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xC, 1));
			xDenominator.addTerm(t);
		}
		
		if (yDenominator == null) {
			yDenominator = new SymbolicPolynomial();
			
			// Instances of symbolic variables
			SymbolicVariable yC = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, CLabel);
			SymbolicVariable yD = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, DLabel);
			
			// term yD
			Term t = new SymbolicTerm(1);
			t.addPower(new Power(yD, 1));
			yDenominator.addTerm(t);
			
			// term -yC
			t = new SymbolicTerm(-1);
			t.addPower(new Power(yC, 1));
			yDenominator.addTerm(t);
		}
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @param numerator the numerator to set
	 */
	public void setNumerator(XPolynomial numerator) {
		this.numerator = numerator;
	}

	/**
	 * @return the numerator
	 */
	public XPolynomial getNumerator() {
		return numerator;
	}

	/**
	 * @param denominator the denominator to set
	 */
	public void setDenominator(XPolynomial denominator) {
		this.denominator = denominator;
	}

	/**
	 * @return the denominator
	 */
	public XPolynomial getDenominator() {
		return denominator;
	}
	
	/**
	 * @param numeratorSegment the numeratorSegment to set
	 */
	public void setNumeratorSegment(Segment numeratorSegment) {
		this.numeratorSegment = numeratorSegment;
	}

	/**
	 * @return the numeratorSegment
	 */
	public Segment getNumeratorSegment() {
		return numeratorSegment;
	}

	/**
	 * @param denominatorSegment the denominatorSegment to set
	 */
	public void setDenominatorSegment(Segment denominatorSegment) {
		this.denominatorSegment = denominatorSegment;
	}

	/**
	 * @return the denominatorSegment
	 */
	public Segment getDenominatorSegment() {
		return denominatorSegment;
	}
	
	

	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param numerator		Numerator segment
	 * @param denominator	Denominator segment
	 */
	public RatioOfTwoCollinearSegments(Segment numerator, Segment denominator) {
		this.numeratorSegment = numerator;
		this.denominatorSegment = denominator;
		// default ratio polynomials are 1s
		this.numerator = new XPolynomial();
		this.numerator.addTerm(new XTerm(1));
		this.denominator = new XPolynomial();
		this.denominator.addTerm(new XTerm(1));
	}
	
	/**
	 * Constructor method
	 * 
	 * @param A		First end point of numerator segment
	 * @param B		Second end point of numerator segment
	 * @param C		First end point of denominator segment
	 * @param D		Second end point of denominator segment
	 */
	public RatioOfTwoCollinearSegments(Point A, Point B, Point C, Point D) {
		this.numeratorSegment = new Segment(A, B);
		this.denominatorSegment = new Segment(C, D);
		// default ratio polynomials are 1s
		this.numerator = new XPolynomial();
		this.numerator.addTerm(new XTerm(1));
		this.denominator = new XPolynomial();
		this.denominator.addTerm(new XTerm(1));
	}
	
	/**
	 * Constructor method
	 * 
	 * @param A		First end point of segment
	 * @param B		Second end point of segment
	 * @param C		Segment division point
	 */
	public RatioOfTwoCollinearSegments(Point A, Point B, Point C) {
		// ratio AC/CB
		this.numeratorSegment = new Segment(A, C);
		this.denominatorSegment = new Segment(C, B);
		// default ratio polynomials are 1s
		this.numerator = new XPolynomial();
		this.numerator.addTerm(new XTerm(1));
		this.denominator = new XPolynomial();
		this.denominator.addTerm(new XTerm(1));
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method that transforms this ratio object to algebraic form - it populates
	 * its numerator and denominator with X-polynomials that represent the algebraic
	 * condition for this ratio.
	 */
	public void transformToAlgebraicForm() {
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		
		pointsMap.put(ALabel, this.numeratorSegment.getFirstEndPoint());
		pointsMap.put(BLabel, this.numeratorSegment.getSecondEndPoint());
		pointsMap.put(CLabel, this.denominatorSegment.getFirstEndPoint());
		pointsMap.put(DLabel, this.denominatorSegment.getSecondEndPoint());
		
		/*
		 * Check whether segments are perpendicular to x-axis:
		 * it is enough to check just one segment since they are collinear
		 * (it is responsibility of user who proves the theorem to provide
		 * correct usage of this class - only for two collinear segments)
		 */
		Variable xA = this.numeratorSegment.getFirstEndPoint().getX();
		Variable xB = this.numeratorSegment.getSecondEndPoint().getX();
		
		// Note: do not reduce numerator and denominator by UTerm division now !!!
		if (xA.getVariableType() == xB.getVariableType() && xA.getIndex() == xB.getIndex()) {
			// AB and CD are perpendicular to x-axis therefore use y-coordinates for condition
			this.numerator = OGPTP.instantiateCondition(yNumerator, pointsMap);
			this.denominator = OGPTP.instantiateCondition(yDenominator, pointsMap);
		}
		else {
			// use x-coordinates
			this.numerator = OGPTP.instantiateCondition(xNumerator, pointsMap);
			this.denominator = OGPTP.instantiateCondition(xDenominator, pointsMap);
		}
	}
	
}


