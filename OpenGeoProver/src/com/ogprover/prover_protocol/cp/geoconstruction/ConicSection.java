/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.prover_protocol.cp.geoconstruction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/*
import com.ogp.main.OpenGeoProver;
import com.ogp.polynomials.Power;
import com.ogp.polynomials.SymbolicTerm;
import com.ogp.polynomials.SymbolicVariable;
import com.ogp.polynomials.Term;
import com.ogp.polynomials.Variable;
import com.ogp.utilities.io.FileLogger;
*/
import com.ogprover.main.OGPConstants;
import com.ogprover.polynomials.SymbolicPolynomial;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.auxiliary.PointSetRelationshipManager;




/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Abstract class for conic section</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public abstract class ConicSection extends GeoConstruction implements SetOfPoints {
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
	 * <i><b>Symbolic label for generic point from conic section</b></i>
	 */
	private static final String M0Label = "0";
	/**
	 * <i><b>Symbolic label for first point of conic section</b></i>
	 */
	private static final String ALabel = "A";
	/**
	 * <i><b>Symbolic label for second point of conic section</b></i>
	 */
	private static final String BLabel = "B";
	/**
	 * <i><b>Symbolic label for third point of conic section</b></i>
	 */
	private static final String CLabel = "C";
	/**
	 * <i><b>Symbolic label for fourth point of conic section</b></i>
	 */
	private static final String DLabel = "D";
	/**
	 * <i><b>Symbolic label for fifth point of conic section</b></i>
	 */
	private static final String ELabel = "E";
	
	/*
	 * NOTE:
	 *    Following members are under comment since general condition for
	 *    conic section can't be calculated at this moment.
	 *

	 **
	 * <i><b>Symbolic label for first point of first intersecting line</b></i>
	 *
	private static final String A1Label = "A'";
	 **
	 * <i><b>Symbolic label for second point of first intersecting line</b></i>
	 *
	private static final String B1Label = "B'";
	 **
	 * <i><b>Symbolic label for first point of second intersecting line</b></i>
	 *
	private static final String C1Label = "C'";
	 **
	 * <i><b>Symbolic label for second point of second intersecting line</b></i>
	 *
	private static final String D1Label = "D'";
	
	*
	*
	*/
	
	/**
	 * List of all constructed points that belong to this conic section
	 */
	protected Vector<Point> points = new Vector<Point>();
	/**
	 * <i>
	 * Symbolic polynomial representing the condition for point 
	 * that belongs to this conic section (uniquely determined by
	 * five given points)
	 * </i>
	 */
	public static SymbolicPolynomial conditionForConicSection = null;
	
	/*
	 * NOTE:
	 *    Following members are under comment since general condition for
	 *    conic section can't be calculated at this moment.
	 *

	 **
	 * <i>
	 * Symbolic polynomial representing the numerator of x-coordinate
	 * of intersection point of two lines, each passing through two points
	 * </i>
	 *
	private static SymbolicPolynomial intersectPointXNumerator = null;
	 **
	 * <i>
	 * Symbolic polynomial representing the denominator of x-coordinate
	 * of intersection point of two lines, each passing through two points
	 * </i>
	 *
	private static SymbolicPolynomial intersectPointXDenominator = null;
	 **
	 * <i>
	 * Symbolic polynomial representing the numerator of y-coordinate
	 * of intersection point of two lines, each passing through two points
	 * </i>
	 *
	private static SymbolicPolynomial intersectPointYNumerator = null;
	 **
	 * <i>
	 * Symbolic polynomial representing the denominator of y-coordinate
	 * of intersection point of two lines, each passing through two points
	 * </i>
	 *
	private static SymbolicPolynomial intersectPointYDenominator = null;
	 **
	 * <i>
	 * Indicator whether conditions are initialized
	 * </i>
	 *
	public static boolean conditionsInitialized = false;
	
	*
	*
	*/
	
	// Static initializer of condition members 
	/*
	 * NOTE:
	 * 	This code is under comment since time and space complexity for 
	 *  calculation of condition for 6 points on conic section is extremely
	 *  huge. Time could be fixed with more than one thread in multiplication
	 *  of polynomials, but space complexity is limit at this moment.
	 *  
	 *  

	static {
		if (conditionsInitialized == false) {
			 *
			 * In order to calculate the condition for a conic section
			 * we have to provide symbolic polynomials that represent
			 * coordinates of a point that is intersection of two lines
			 * each passing through two points.
			 * 
			 * Using the condition for plain line (x0 - xA)(yB - yA) - (xB - xA)(y0 - yA) = 0
			 * we can obtain symbolic polynomials that represent the coordinates of intersection
			 * point M0 of two lines AB and CD:
			 * 
			 * from line AB we get: x0 = (xB - xA)(y0 - yA)/(yB - yA) + xA
			 * from line CD we get: x0 = (xD - xC)(y0 - yC)/(yD - yC) + xC
			 * 
			 * from this we obtain: [(xB - xA)(yD - yC) - (xD - xC)(yB - yA)]y0 = 
			 *                      (xC - xA)(yB - yA)(yD - yC) + yA(XB - XA)(yD - yC) - yC(xD - xC)(yB - yA)
			 * here we get expressions for numerator and denominator of y coordinate of intersection point M0
			 *    ny0 = (xC - xA)(yB - yA)(yD - yC) + yA(XB - XA)(yD - yC) - yC(xD - xC)(yB - yA)
			 *    dy0 = (xB - xA)(yD - yC) - (xD - xC)(yB - yA)
			 *    
			 * using these expressions in first equation for x0 (from line AB) we obtain:
			 *    (yB - yA)dy0*x0 = (xB - xA)(ny0 - yA*dy0) + xA(yB - yA)dy0
			 * and from this we get expressions for numerator and denominator of x coordinate of intersection point M0
			 *    nx0 = (xB - xA)(ny0 - yA*dy0) + xA(yB - yA)dy0
			 *    dx0 = (yB - yA)dy0
			 *
			
			// Instances of symbolic variables
			SymbolicVariable xA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, A1Label);
			SymbolicVariable yA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, A1Label);
			SymbolicVariable xB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, B1Label);
			SymbolicVariable yB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, B1Label);
			SymbolicVariable xC = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, C1Label);
			SymbolicVariable yC = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, C1Label);
			SymbolicVariable xD = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, D1Label);
			SymbolicVariable yD = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, D1Label);
			Term t;
			SymbolicPolynomial tempFactor, tempAddend;
			
			intersectPointXNumerator = new SymbolicPolynomial();
			intersectPointXDenominator = new SymbolicPolynomial();
			intersectPointYNumerator = new SymbolicPolynomial();
			intersectPointYDenominator = new SymbolicPolynomial();
			
			 *
			 * intersectPointYNumerator = (xC - xA)(yB - yA)(yD - yC) + yA(XB - XA)(yD - yC) - yC(xD - xC)(yB - yA)
			 *
			// addend (xC - xA)(yB - yA)(yD - yC)
			tempAddend = new SymbolicPolynomial();
			
			tempFactor = new SymbolicPolynomial();
			t = new SymbolicTerm(1);
			t.addPower(new Power(xC, 1));
			tempFactor.addTerm(t);
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xA, 1));
			tempFactor.addTerm(t);
			tempAddend.addPolynomial(tempFactor);
			
			tempFactor = new SymbolicPolynomial();
			t = new SymbolicTerm(1);
			t.addPower(new Power(yB, 1));
			tempFactor.addTerm(t);
			t = new SymbolicTerm(-1);
			t.addPower(new Power(yA, 1));
			tempFactor.addTerm(t);
			tempAddend.multiplyByPolynomial(tempFactor);
			
			tempFactor = new SymbolicPolynomial();
			t = new SymbolicTerm(1);
			t.addPower(new Power(yD, 1));
			tempFactor.addTerm(t);
			t = new SymbolicTerm(-1);
			t.addPower(new Power(yC, 1));
			tempFactor.addTerm(t);
			tempAddend.multiplyByPolynomial(tempFactor);
			
			intersectPointYNumerator.addPolynomial(tempAddend);
			
			// addend yA(XB - XA)(yD - yC)
			tempAddend = new SymbolicPolynomial();
			
			tempFactor = new SymbolicPolynomial();
			t = new SymbolicTerm(1);
			t.addPower(new Power(yA, 1));
			tempFactor.addTerm(t);
			tempAddend.addPolynomial(tempFactor);
			
			tempFactor = new SymbolicPolynomial();
			t = new SymbolicTerm(1);
			t.addPower(new Power(xB, 1));
			tempFactor.addTerm(t);
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xA, 1));
			tempFactor.addTerm(t);
			tempAddend.multiplyByPolynomial(tempFactor);
			
			tempFactor = new SymbolicPolynomial();
			t = new SymbolicTerm(1);
			t.addPower(new Power(yD, 1));
			tempFactor.addTerm(t);
			t = new SymbolicTerm(-1);
			t.addPower(new Power(yC, 1));
			tempFactor.addTerm(t);
			tempAddend.multiplyByPolynomial(tempFactor);
			
			intersectPointYNumerator.addPolynomial(tempAddend);
			
			// addend - yC(xD - xC)(yB - yA)
			tempAddend = new SymbolicPolynomial();
			
			tempFactor = new SymbolicPolynomial();
			t = new SymbolicTerm(-1);
			t.addPower(new Power(yC, 1));
			tempFactor.addTerm(t);
			tempAddend.addPolynomial(tempFactor);
			
			tempFactor = new SymbolicPolynomial();
			t = new SymbolicTerm(1);
			t.addPower(new Power(xD, 1));
			tempFactor.addTerm(t);
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xC, 1));
			tempFactor.addTerm(t);
			tempAddend.multiplyByPolynomial(tempFactor);
			
			tempFactor = new SymbolicPolynomial();
			t = new SymbolicTerm(1);
			t.addPower(new Power(yB, 1));
			tempFactor.addTerm(t);
			t = new SymbolicTerm(-1);
			t.addPower(new Power(yA, 1));
			tempFactor.addTerm(t);
			tempAddend.multiplyByPolynomial(tempFactor);
			
			intersectPointYNumerator.addPolynomial(tempAddend);
			
			 *
			 * intersectPointYDenominator = (xB - xA)(yD - yC) - (xD - xC)(yB - yA)
			 *
			// addend (xB - xA)(yD - yC)
			tempAddend = new SymbolicPolynomial();
			
			tempFactor = new SymbolicPolynomial();
			t = new SymbolicTerm(1);
			t.addPower(new Power(xB, 1));
			tempFactor.addTerm(t);
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xA, 1));
			tempFactor.addTerm(t);
			tempAddend.addPolynomial(tempFactor);
			
			tempFactor = new SymbolicPolynomial();
			t = new SymbolicTerm(1);
			t.addPower(new Power(yD, 1));
			tempFactor.addTerm(t);
			t = new SymbolicTerm(-1);
			t.addPower(new Power(yC, 1));
			tempFactor.addTerm(t);
			tempAddend.multiplyByPolynomial(tempFactor);
			
			intersectPointYDenominator.addPolynomial(tempAddend);
			
			// addend - (xD - xC)(yB - yA)
			tempAddend = new SymbolicPolynomial();
			
			tempFactor = new SymbolicPolynomial();
			t = new SymbolicTerm(1);
			t.addPower(new Power(xD, 1));
			tempFactor.addTerm(t);
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xC, 1));
			tempFactor.addTerm(t);
			tempFactor.multiplyByRealConstant(-1);
			tempAddend.addPolynomial(tempFactor);
			
			tempFactor = new SymbolicPolynomial();
			t = new SymbolicTerm(1);
			t.addPower(new Power(yB, 1));
			tempFactor.addTerm(t);
			t = new SymbolicTerm(-1);
			t.addPower(new Power(yA, 1));
			tempFactor.addTerm(t);
			tempAddend.multiplyByPolynomial(tempFactor);
			
			intersectPointYDenominator.addPolynomial(tempAddend);
			
			 *
			 * intersectPointXNumerator = (xB - xA)(ny0 - yA*dy0) + xA(yB - yA)dy0
			 *
			// addend (xB - xA)(ny0 - yA*dy0)
			tempAddend = new SymbolicPolynomial();
			
			tempFactor = new SymbolicPolynomial();
			t = new SymbolicTerm(1);
			t.addPower(new Power(xB, 1));
			tempFactor.addTerm(t);
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xA, 1));
			tempFactor.addTerm(t);
			tempAddend.addPolynomial(tempFactor);
			
			tempFactor = (SymbolicPolynomial) intersectPointYNumerator.clone();
			t = new SymbolicTerm(1);
			t.addPower(new Power(yA, 1));
			tempFactor.subtractPolynomial(intersectPointYDenominator.clone().multiplyByTerm(t));
			tempAddend.multiplyByPolynomial(tempFactor);
			
			intersectPointXNumerator.addPolynomial(tempAddend);
			
			// addend xA(yB - yA)dy0
			tempAddend = new SymbolicPolynomial();
			
			tempFactor = new SymbolicPolynomial();
			t = new SymbolicTerm(1);
			t.addPower(new Power(yB, 1));
			tempFactor.addTerm(t);
			t = new SymbolicTerm(-1);
			t.addPower(new Power(yA, 1));
			tempFactor.addTerm(t);
			t = new SymbolicTerm(1);
			t.addPower(new Power(xA, 1));
			tempFactor.multiplyByTerm(t);
			tempFactor.multiplyByPolynomial(intersectPointYDenominator);
			tempAddend.addPolynomial(tempFactor);
			
			intersectPointXNumerator.addPolynomial(tempAddend);
			
			 *
			 * intersectPointXDenominator = (yB - yA)dy0
			 *
			// addend (yB - yA)dy0
			tempAddend = new SymbolicPolynomial();
			
			tempFactor = new SymbolicPolynomial();
			t = new SymbolicTerm(1);
			t.addPower(new Power(yB, 1));
			tempFactor.addTerm(t);
			t = new SymbolicTerm(-1);
			t.addPower(new Power(yA, 1));
			tempFactor.addTerm(t);
			tempFactor.multiplyByPolynomial(intersectPointYDenominator);
			tempAddend.addPolynomial(tempFactor);
			
			intersectPointXDenominator.addPolynomial(tempAddend);
			
			 *
			 * Condition for a point to belong to a conic section
			 * 
			 * as a geometric property for a conic, Pascal's theorem will be used:
			 * 
			 * if A, B, C, D, E and F are any six points from a conic section then
			 * let P, Q and R are intersections of following pairs of lines:
			 * AB and DE, BC and EF, CA and FD respectively, then points P, Q and R are      <<<< IMPORTANT: not CA x FD but CD x FA !!!
			 * collinear points (we assume these intersections exist).
			 * 
			 * Therefore if A, B, C, D and E are five given points of conic section, 
			 * then they together with M0 must satisfy above mentioned condition if M0 belongs
			 * to that conic section.
			 * 
			 *  P = AB x DE (P is intersection of lines AB and DE)
			 *  Q = BC x EM0
			 *  R = CA x M0D
			 *  
			 *  Coordinates of these intersection points will be
			 *  P = (nxP/dxP, nyP/dyP)
			 *  Q = (nxQ/dxQ, nyQ/dyQ)
			 *  R = (nxR/dxR, nyR/dyR)
			 *  
			 *  These three points must be collinear i.e. they must satisfy following
			 *  condition (for plain line):
			 *  
			 *  (xR - xP)(yQ - yP) - (xQ - xP)(yR - yP) = 0
			 *  
			 *  when this is multiplied by all denominators it becomes
			 *  (let cxP is mark for product of all denominators except dxP, and
			 *  similar notation will be used for other coordinates):
			 *  
			 *  (nxR*cxR - nxP*cxP)(nyQ*cyQ - nyP*cyP) - (nxQ*cxQ - nxP*cxP)(nyR*cyR - nyP*cyP) = 0
			 *
			
			// Coordinates of all intersection points
			Map<String, String> labelsMap = new HashMap<String, String>();
			
			SymbolicPolynomial nxP = (SymbolicPolynomial) intersectPointXNumerator.clone();
			SymbolicPolynomial dxP = (SymbolicPolynomial) intersectPointXDenominator.clone();
			SymbolicPolynomial nyP = (SymbolicPolynomial) intersectPointYNumerator.clone();
			SymbolicPolynomial dyP = (SymbolicPolynomial) intersectPointYDenominator.clone();
			labelsMap.put(A1Label, ALabel);
			labelsMap.put(B1Label, BLabel);
			labelsMap.put(C1Label, DLabel);
			labelsMap.put(D1Label, ELabel);
			nxP.substitute(labelsMap);
			dxP.substitute(labelsMap);
			nyP.substitute(labelsMap);
			dyP.substitute(labelsMap);
			
			
			SymbolicPolynomial nxQ = (SymbolicPolynomial) intersectPointXNumerator.clone();
			SymbolicPolynomial dxQ = (SymbolicPolynomial) intersectPointXDenominator.clone();
			SymbolicPolynomial nyQ = (SymbolicPolynomial) intersectPointYNumerator.clone();
			SymbolicPolynomial dyQ = (SymbolicPolynomial) intersectPointYDenominator.clone();
			labelsMap.put(A1Label, BLabel);
			labelsMap.put(B1Label, CLabel);
			labelsMap.put(C1Label, ELabel);
			labelsMap.put(D1Label, M0Label);
			nxQ.substitute(labelsMap);
			dxQ.substitute(labelsMap);
			nyQ.substitute(labelsMap);
			dyQ.substitute(labelsMap);
			
			SymbolicPolynomial nxR = (SymbolicPolynomial) intersectPointXNumerator.clone();
			SymbolicPolynomial dxR = (SymbolicPolynomial) intersectPointXDenominator.clone();
			SymbolicPolynomial nyR = (SymbolicPolynomial) intersectPointYNumerator.clone();
			SymbolicPolynomial dyR = (SymbolicPolynomial) intersectPointYDenominator.clone();
			labelsMap.put(A1Label, CLabel);
			labelsMap.put(B1Label, ALabel);
			labelsMap.put(C1Label, M0Label);
			labelsMap.put(D1Label, DLabel);
			nxR.substitute(labelsMap);
			dxR.substitute(labelsMap);
			nyR.substitute(labelsMap);
			dyR.substitute(labelsMap);
			
			// Partial products of denominators
			SymbolicPolynomial cxP = (SymbolicPolynomial) dyP.clone().multiplyByPolynomial(dxQ).multiplyByPolynomial(dyQ).multiplyByPolynomial(dxR).multiplyByPolynomial(dyR);
			SymbolicPolynomial cyP = (SymbolicPolynomial) dxP.clone().multiplyByPolynomial(dxQ).multiplyByPolynomial(dyQ).multiplyByPolynomial(dxR).multiplyByPolynomial(dyR);
			
			SymbolicPolynomial cxQ = (SymbolicPolynomial) dyQ.clone().multiplyByPolynomial(dxP).multiplyByPolynomial(dyP).multiplyByPolynomial(dxR).multiplyByPolynomial(dyR);
			SymbolicPolynomial cyQ = (SymbolicPolynomial) dxQ.clone().multiplyByPolynomial(dxP).multiplyByPolynomial(dyP).multiplyByPolynomial(dxR).multiplyByPolynomial(dyR);
			
			SymbolicPolynomial cxR = (SymbolicPolynomial) dyR.clone().multiplyByPolynomial(dxQ).multiplyByPolynomial(dyQ).multiplyByPolynomial(dxP).multiplyByPolynomial(dyP);
			SymbolicPolynomial cyR = (SymbolicPolynomial) dxR.clone().multiplyByPolynomial(dxQ).multiplyByPolynomial(dyQ).multiplyByPolynomial(dxP).multiplyByPolynomial(dyP);
			
			 *
			 * conditionForConicSection = (nxR*cxR - nxP*cxP)(nyQ*cyQ - nyP*cyP) - (nxQ*cxQ - nxP*cxP)(nyR*cyR - nyP*cyP)
			 *
			conditionForConicSection = new SymbolicPolynomial();
			// addend (nxR*cxR - nxP*cxP)(nyQ*cyQ - nyP*cyP)
			tempAddend = new SymbolicPolynomial();
			tempFactor = (SymbolicPolynomial) nxR.clone().multiplyByPolynomial(cxR).subtractPolynomial(nxP.clone().multiplyByPolynomial(cxP));
			tempAddend.addPolynomial(tempFactor);
			tempFactor = (SymbolicPolynomial) nyQ.clone().multiplyByPolynomial(cyQ).subtractPolynomial(nyP.clone().multiplyByPolynomial(cyP));
			//tempAddend.multiplyByPolynomial(tempFactor); // THIS IS CRICICAL PART - TOO MUCH SPACE AND TIME - OVER 14B OF TERMS
			tempAddend.multiplyByPolynomialConcurrently(tempFactor, 20); // THIS IS CRICICAL PART - TOO MUCH SPACE - OVER 14B OF TERMS
			
			conditionForConicSection.addPolynomial(tempAddend);
			
			// addend - (nxQ*cxQ - nxP*cxP)(nyR*cyR - nyP*cyP)
			tempAddend = new SymbolicPolynomial();
			
			tempFactor = (SymbolicPolynomial) nxQ.clone().multiplyByPolynomial(cxQ).subtractPolynomial(nxP.clone().multiplyByPolynomial(cxP));
			tempAddend.addPolynomial(tempFactor);
			tempFactor = (SymbolicPolynomial) nyR.clone().multiplyByPolynomial(cyR).subtractPolynomial(nyP.clone().multiplyByPolynomial(cyP));
			//tempAddend.multiplyByPolynomial(tempFactor); // THIS IS CRICICAL PART - TOO MUCH SPACE AND TIME - OVER 14B OF TERMS
			tempAddend.multiplyByPolynomialConcurrently(tempFactor, 20); // THIS IS CRICICAL PART - TOO MUCH SPACE - OVER 14B OF TERMS

			conditionForConicSection.subtractPolynomial(tempAddend);
			
			conditionsInitialized = true;
		}
	}
	
	*
	*
	*/
	
	/*
	 * ======================================================================
	 * ========================= ABSTRACT METHODS ===========================
	 * ======================================================================
	 */
	/**
	 * Method that retrieves the instance of specific condition for points 
	 * of this conic section expressed as x-polynomial (after symbolic 
	 * coordinates of all points have been substituted by UX variables)
	 *  
	 * @param pointsMap		Map of points assigned to labels of common points from
	 * 						symbolic polynomial representing the condition,
	 * 						used for instantiation of that condition
	 * @return				X-polynomial representing the specific condition for 
	 * 						points of this conic section  in algebraic form or 
	 * 						null in case of error
	 */
	public abstract XPolynomial instantiateCondition(Map<String, Point> pointsMap);
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method that sets the list of all points belonging to this conic section
	 * 
	 * @param points The points to set
	 */
	public void setPoints(Vector<Point> points) {
		this.points = points;
	}

	/**
	 * Method that retrieves the list of all points belonging to this conic section
	 * 
	 * @return the points
	 */
	public Vector<Point> getPoints() {
		return points;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method that retrieves the instance of condition for points of this conic 
	 * section expressed as x-polynomial (after symbolic coordinates of all points
	 * have been substituted by UX variables)
	 *  
	 * @param pointsMap		Map of points assigned to labels of common points from
	 * 						symbolic polynomial representing the condition,
	 * 						used for instantiation of that condition
	 * @return				X-polynomial representing the condition for points of this 
	 * 						conic section in algebraic form or null in case of error
	 */
	public XPolynomial instantiateConditionForGeneralConicSection(Map<String, Point> pointsMap) {
		/*
		 * NOTE:
		 *   This method should never be called since general condition
		 *   for conic section can't be calculated at this moment.
		 * 
		 */
		return OGPCP.instantiateCondition(ConicSection.conditionForConicSection, pointsMap);
	}
	
	/**
	 * Finding best points for instantiation.
	 * 
	 * @see com.ogprover.prover_protocol.cp.geoconstruction.SetOfPoints#findBestPointsForInstantation(com.ogprover.prover_protocol.cp.auxiliary.PointSetRelationshipManager)
	 */
	public int findBestPointsForInstantation(PointSetRelationshipManager manager) {
		/*
		 * NOTE:
		 *   This code is currently under comment since general condition 
		 *   for conic section can't be calculated at this moment.
		 * 
		 *

		FileLogger logger = OpenGeoProver.settings.getLogger();
		
		// NOTE: This method is used to find best elements for process of transformation
		// in algebraic form. It is implemented by calling same methods like in
		// final transformation, but without printing of results to output reports.
		// Also, in order to be able to repeat the good transformation later in
		// final processing, it is necessary to cancel any result i.e. if a polynomial
		// is added to system of polynomials, it has to be removed, and if coordinates
		// are renamed, they have to be back to previous values.
		
		manager.prepareForFirstInstantiation();
		
		if (manager.isErrorFlag()) {
			logger.error("Failed in preparation method");
			return OGPConstants.ERR_CODE_GENERAL;
		}
		
		Map<String, Point> pointsMap = new HashMap<String, Point>(); // collection with current elements
		Point P = manager.getPoint();
		
		// If conic section has five different points, try to instantiate a condition
		manager.setCondition(ConicSection.conditionForConicSection);
		
		// instantiate the condition from this circle using its specific condition for circumscribed circle
		P = manager.getPoint();
		pointsMap.put(M0Label, P);
		
		// Pass all points of this conic section to search for the first point from it
		 *
		 * Note: Do not use here for-loops with iterators (implicit or explicit), 
		 * e.g. "for (Point point : this.points)" or "for (Iterator<Point> it = this.points.iterator(); it.hasNext(); )"
		 * because the point from manager that is being processed is already in set of points from
		 * this manager and during the processing it is being modified (its state and even coordinates
		 * are modified). Therefore the collection of points is modified causing the iterator be in
		 * illegal state and throwing "ConcurrentModificationException" in runtime. Instead of that 
		 * kind of loop we will use iteration over the collection of points by usage of indices (from the
		 * beginning till the end of list) - it is safe because we will not add or remove any point
		 * to/from existing set of points during this iteration. We event won't replace points within 
		 * this iteration, therefore we are sure that iteration will process all elements. It is also
		 * safe to repeat such kind of iteration in nested loops.
		 *
		// All five points from conic that we have to choose are equivalent in symbolic polynomial for condition
		// of belonging to that conic. Therefore we can skip repeating of same combinations of five points by
		// asking for certain index order - e.g. we need five points constructed before point from manager
		// such that A < B < C < D < E.
		for (int iA = 0, jA = this.points.size(); iA < jA; iA++) {
			Point pointA = this.points.get(iA);
			// choose as given point one of those constructed before P
			if (pointA.getIndex() >= P.getIndex()) // A constructed after P or is P - skip it
				continue;
			
			// Pass all points of this conic section to search for the second point from it
			for (int iB = 0, jB = this.points.size(); iB < jB; iB++) {
				Point pointB = this.points.get(iB);
				// choose as given point one of those constructed before P and not before A
				if (pointB.getIndex() >= P.getIndex() || 
					pointB.getIndex() <= pointA.getIndex()) // B constructed after P or is P or constructed before A or is A - skip it
					continue;
				
				// Pass all points of this conic section to search for the third point from it
				for (int iC = 0, jC = this.points.size(); iC < jC; iC++) {
					Point pointC = this.points.get(iC);
					// choose as given point one of those constructed before P and not before A and B
					if (pointC.getIndex() >= P.getIndex() || 
						pointC.getIndex() <= pointA.getIndex() || 
						pointC.getIndex() <= pointB.getIndex()) // C constructed after P or is P or constructed before A/B or is A/B - skip it
						continue;
					
					// Pass all points of this conic section to search for the fourth point from it
					for (int iD = 0, jD = this.points.size(); iD < jD; iD++) {
						Point pointD = this.points.get(iD);
						// choose as given point one of those constructed before P and not before A, B, C
						if (pointD.getIndex() >= P.getIndex() || 
							pointD.getIndex() <= pointA.getIndex() || 
							pointD.getIndex() <= pointB.getIndex() ||
							pointD.getIndex() <= pointC.getIndex()) // D constructed after P or is P or constructed before A/B/C or is A/B/C - skip it
							continue;
						
						// Pass all points of this conic section to search for the fifth point from it
						for (int iE = 0, jE = this.points.size(); iE < jE; iE++) {
							Point pointE = this.points.get(iE);
							// choose as given point one of those constructed before P and not before A, B, C, D
							if (pointE.getIndex() >= P.getIndex() || 
								pointE.getIndex() <= pointA.getIndex() || 
								pointE.getIndex() <= pointB.getIndex() ||
								pointE.getIndex() <= pointC.getIndex() ||
								pointE.getIndex() <= pointD.getIndex()) // E constructed after P or is P or constructed before A/B/C/D or is A/B/C/D - skip it
								continue;
					
							pointsMap.put(ALabel, pointA);
							pointsMap.put(BLabel, pointB);
							pointsMap.put(CLabel, pointC);
							pointsMap.put(DLabel, pointD);
							pointsMap.put(ELabel, pointE);
		
							manager.processPointsAndCondition(pointsMap);
		
							if (manager.isErrorFlag()) {
								logger.error("Failed in processing specific polynomial for circle with three points on it.");
								return OGPConstants.ERR_CODE_GENERAL;
							}
					
							// if polynomial that renames coordinates of point is found, stop further search
							if (manager.getPoint().getPointState() == Point.POINT_STATE_RENAMED)
								return OGPConstants.RET_CODE_SUCCESS;
						}
					}
				}
			}
		}
		
		*
		*
		*/
		
		return OGPConstants.RET_CODE_SUCCESS;
	}
	
	/**
	 * @see com.ogprover.prover_protocol.cp.geoconstruction.SetOfPoints#instantiateConditionFromBasicElements(com.ogprover.prover_protocol.cp.geoconstruction.Point)
	 */
	public XPolynomial instantiateConditionFromBasicElements(Point P) {
		/*
		 * NOTE:
		 *   This method should never be called since general condition
		 *   for conic section can't be calculated at this moment.
		 * 
		 */
		
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		pointsMap.put(M0Label, P);
		
		pointsMap.put(ALabel, this.points.get(0));
		pointsMap.put(BLabel, this.points.get(1));
		pointsMap.put(CLabel, this.points.get(2));
		pointsMap.put(DLabel, this.points.get(3));
		pointsMap.put(ELabel, this.points.get(4));
		
		//return this.instantiateConditionForGeneralConicSection(pointsMap).reduceByUTermDivision();
		return this.instantiateConditionForGeneralConicSection(pointsMap); // don't reduce polynomial
	}

	/**
	 * @see com.ogprover.prover_protocol.cp.geoconstruction.SetOfPoints#getAllPossibleConditionsWithMappings()
	 */
	public Map<SymbolicPolynomial, ArrayList<Map<String, Point>>> getAllPossibleConditionsWithMappings() {
		Map<SymbolicPolynomial, ArrayList<Map<String, Point>>> retMap = new HashMap<SymbolicPolynomial, ArrayList<Map<String, Point>>>();
		
		/*
		 * NOTE:
		 *  This code is under comment since general condition for conic section can't be calculated at this moment
		 * 
		 * 

		ArrayList<Map<String, Point>> allMappings = new ArrayList<Map<String, Point>>();
		Map<String, Point> pointsMap = null;
		 
		
		// In order to be able to add mappings for conic with five points,
		// there must exist at least five points on this conic section
		if (this.points.size() < 5)
			return retMap; // return empty map rather then null since it will be passed
		                   // down to subclasses so they could populate it with their mappings
		
		// points are equivalently used in symbolic condition so we will not swap them
		for (Point A : this.points) {
			for (Point B : this.points) {
				if (B.getIndex() <= A.getIndex())
					continue;
				
				for (Point C : this.points) {
					if (C.getIndex() <= A.getIndex() ||
						C.getIndex() <= B.getIndex())
						continue;
					
					for (Point D : this.points) {
						if (D.getIndex() <= A.getIndex() ||
							D.getIndex() <= B.getIndex() ||
							D.getIndex() <= C.getIndex())
							continue;
						
						for (Point E : this.points) {
							if (E.getIndex() <= A.getIndex() ||
								E.getIndex() <= B.getIndex() ||
								E.getIndex() <= C.getIndex() ||
								E.getIndex() <= D.getIndex())
								continue;
							
							pointsMap = new HashMap<String, Point>();
							pointsMap.put(ALabel, A);
							pointsMap.put(BLabel, B);
							pointsMap.put(CLabel, C);
							pointsMap.put(DLabel, D);
							pointsMap.put(ELabel, E);
							allMappings.add(pointsMap);
						}
					}
				}
			}
		}
		
		retMap.put((SymbolicPolynomial) ConicSection.conditionForConicSection.clone(), allMappings);
		
		*
		*
		*/
		
		return retMap;
	}
	
	
}
