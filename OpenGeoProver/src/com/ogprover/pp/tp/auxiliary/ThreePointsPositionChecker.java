/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.auxiliary;

import java.util.ArrayList;
import java.util.Vector;

import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.geoconstruction.Circle;
import com.ogprover.pp.tp.geoconstruction.CircleWithCenterAndPoint;
import com.ogprover.pp.tp.geoconstruction.Line;
import com.ogprover.pp.tp.geoconstruction.LineThroughTwoPoints;
import com.ogprover.pp.tp.geoconstruction.PerpendicularBisector;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.pp.tp.geoobject.Segment;
import com.ogprover.pp.tp.ndgcondition.AlgebraicNDGCondition;
import com.ogprover.pp.tp.thmstatement.AlgebraicSumOfThreeSegments;
import com.ogprover.pp.tp.thmstatement.CollinearPoints;
import com.ogprover.pp.tp.thmstatement.PointOnSetOfPoints;
import com.ogprover.pp.tp.thmstatement.RatioOfOrientedSegments;
import com.ogprover.pp.tp.thmstatement.TwoPerpendicularLines;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for checking whether certain points' positions 
*     of three points correspond to specified NDG condition</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class ThreePointsPositionChecker extends PointsPositionChecker {
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
	
	
	

	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param ndgCond	NDG condition associated to this points position checker
	 */
	public ThreePointsPositionChecker(AlgebraicNDGCondition ndgCond){
		this.initializePointsPositionChecker(ndgCond);
	}
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/*
	 * Specific positions for three points
	 */
	/**
	 * Check if points are collinear.
	 * 
	 * @param A		First point
	 * @param B		Second point
	 * @param C		Third point
	 * @return		True if condition is met, false otherwise
	 */
	private boolean checkThreeCollinearPoints(Point A, Point B, Point C) {
		this.clearAuxCP();
		this.auxiliaryCP.addGeoConstruction(A);
		this.auxiliaryCP.addGeoConstruction(B);
		this.auxiliaryCP.addGeoConstruction(C);
		ArrayList<Point> pointList = new ArrayList<Point>();
		pointList.add(A);
		pointList.add(B);
		pointList.add(C);
		this.auxiliaryCP.addThmStatement(new CollinearPoints(pointList));
		XPolynomial statementPoly = this.auxiliaryCP.getTheoremStatement().getAlgebraicForm();
		
		if (statementPoly != null && statementPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial())) {
			Vector<Point> pointsV = new Vector<Point>();
			pointsV.add(A);
			pointsV.add(B);
			pointsV.add(C);
			this.ndgCond.addNewTranslation(AlgebraicNDGCondition.NDG_TYPE_3PT_COLLINEAR, pointsV);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check if point C is midpoint of segment AB.
	 * 
	 * @param A		First point
	 * @param B		Second point
	 * @param C		Third point
	 * @return		True if condition is met, false otherwise
	 */
	private boolean checkMidPoint(Point A, Point B, Point C) {
		this.clearAuxCP();
		this.auxiliaryCP.addGeoConstruction(A);
		this.auxiliaryCP.addGeoConstruction(B);
		this.auxiliaryCP.addGeoConstruction(C);
		Segment AC = new Segment(A, C);
		Segment CB = new Segment(C, B);
		this.auxiliaryCP.addThmStatement(new RatioOfOrientedSegments(AC, CB, 1));
		XPolynomial statementXPoly = ((RatioOfOrientedSegments) this.auxiliaryCP.getTheoremStatement()).getXAlgebraicForm();
		XPolynomial statementYPoly = ((RatioOfOrientedSegments) this.auxiliaryCP.getTheoremStatement()).getYAlgebraicForm();
		XPolynomial statementPoly = null;
		
		if (statementXPoly != null && statementYPoly != null)
			statementPoly = (XPolynomial)statementXPoly.clone().multiplyByPolynomial(statementXPoly)
		                  .addPolynomial(statementYPoly.clone().multiplyByPolynomial(statementYPoly));
		
		if ((statementXPoly != null && statementXPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial())) ||
			(statementYPoly != null && statementYPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial())) ||
			(statementPoly != null && statementPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial()))) {
			Vector<Point> pointsV = new Vector<Point>();
			pointsV.add(A);
			pointsV.add(B);
			pointsV.add(C);
			this.ndgCond.addNewTranslation(AlgebraicNDGCondition.NDG_TYPE_3PT_MIDPOINT, pointsV);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check if point C is on perpendicular bisector of segment AB.
	 * 
	 * @param A		First point
	 * @param B		Second point
	 * @param C		Third point
	 * @return		True if condition is met, false otherwise
	 */
	private boolean checkPerpBisectorPoint(Point A, Point B, Point C) {
		this.clearAuxCP();
		this.auxiliaryCP.addGeoConstruction(A);
		this.auxiliaryCP.addGeoConstruction(B);
		this.auxiliaryCP.addGeoConstruction(C);
		Line perpB = new PerpendicularBisector("perpB", A, B);
		this.auxiliaryCP.addGeoConstruction(perpB);
		this.auxiliaryCP.addThmStatement(new PointOnSetOfPoints(perpB, C));
		XPolynomial statementPoly = this.auxiliaryCP.getTheoremStatement().getAlgebraicForm();
		
		if (statementPoly != null && statementPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial())) {
			Vector<Point> pointsV = new Vector<Point>();
			pointsV.add(A);
			pointsV.add(B);
			pointsV.add(C);
			this.ndgCond.addNewTranslation(AlgebraicNDGCondition.NDG_TYPE_3PT_ON_PERP_BIS, pointsV);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check if angle &lt;ACB is right.
	 * 
	 * @param A		First point
	 * @param B		Second point
	 * @param C		Third point
	 * @return		True if condition is met, false otherwise
	 */
	private boolean checkRightAngle(Point A, Point B, Point C) {
		this.clearAuxCP();
		this.auxiliaryCP.addGeoConstruction(A);
		this.auxiliaryCP.addGeoConstruction(B);
		this.auxiliaryCP.addGeoConstruction(C);
		Line AC = new LineThroughTwoPoints("AC", A, C);
		this.auxiliaryCP.addGeoConstruction(AC);
		Line BC = new LineThroughTwoPoints("BC", B, C);
		this.auxiliaryCP.addGeoConstruction(BC);
		this.auxiliaryCP.addThmStatement(new TwoPerpendicularLines(AC, BC));
		XPolynomial statementPoly = this.auxiliaryCP.getTheoremStatement().getAlgebraicForm();
		
		if (statementPoly != null && statementPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial())) {
			Vector<Point> pointsV = new Vector<Point>();
			pointsV.add(A);
			pointsV.add(B);
			pointsV.add(C);
			this.ndgCond.addNewTranslation(AlgebraicNDGCondition.NDG_TYPE_3PT_RIGHT_ANG, pointsV);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check if point C is on circle with center A and one point B.
	 * 
	 * @param A		First point
	 * @param B		Second point
	 * @param C		Third point
	 * @return		True if condition is met, false otherwise
	 */
	private boolean checkPointOnCircle(Point A, Point B, Point C) {
		this.clearAuxCP();
		this.auxiliaryCP.addGeoConstruction(A);
		this.auxiliaryCP.addGeoConstruction(B);
		this.auxiliaryCP.addGeoConstruction(C);
		Circle k = new CircleWithCenterAndPoint("k", A, B);
		this.auxiliaryCP.addGeoConstruction(k);
		this.auxiliaryCP.addThmStatement(new PointOnSetOfPoints(k, C));
		XPolynomial statementPoly = this.auxiliaryCP.getTheoremStatement().getAlgebraicForm();
		
		if (statementPoly != null && statementPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial())) {
			Vector<Point> pointsV = new Vector<Point>();
			pointsV.add(A);
			pointsV.add(B);
			pointsV.add(C);
			this.ndgCond.addNewTranslation(AlgebraicNDGCondition.NDG_TYPE_3PT_ON_CIRCLE, pointsV);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check if segment AB is equal to sum of segments AC and CB.
	 * 
	 * @param A		First point
	 * @param B		Second point
	 * @param C		Third point
	 * @return		True if condition is met, false otherwise
	 */
	private boolean checkAlgebraicSumOfThreeSegments(Point A, Point B, Point C) {
		this.clearAuxCP();
		this.auxiliaryCP.addGeoConstruction(A);
		this.auxiliaryCP.addGeoConstruction(B);
		this.auxiliaryCP.addGeoConstruction(C);
		this.auxiliaryCP.addThmStatement(new AlgebraicSumOfThreeSegments(A, B, A, C, C, B));
		XPolynomial statementPoly = this.auxiliaryCP.getTheoremStatement().getAlgebraicForm();
		
		if (statementPoly != null && statementPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial())) {
			Vector<Point> pointsV = new Vector<Point>();
			pointsV.add(A);
			pointsV.add(B);
			pointsV.add(C);
			this.ndgCond.addNewTranslation(AlgebraicNDGCondition.NDG_TYPE_3PT_SEG_SUM, pointsV);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Method that checks if some position of three given points can
	 * generate polynomial for attached NDG condition. If can, text of 
	 * this NDG condition is populated with appropriate readable
	 * form that describes specific position of these three points
	 * that generates attached NDG condition. The text actually corresponds 
	 * to the negation of attached NDG condition.
	 * 
	 * @param pointList		List with three points
	 * @return		        True if some position generates attached NDG condition,
	 * 				        false otherwise
	 * 
	 * @see com.ogprover.pp.tp.auxiliary.PointsPositionChecker#checkPositions(java.util.Vector)
	 * 
	 */
	public boolean checkPositions(Vector<Point> pointList) {
		if (pointList == null || pointList.size() != 3)
			return false;
		
		boolean checkResult = false;
		boolean singleCheckResult = false;
		
		// we clone points since they will be added to new CP
		Point A = pointList.get(0).clone();
		Point B = pointList.get(1).clone();
		Point C = pointList.get(2).clone();
		
		// check if points are collinear
		singleCheckResult = this.checkThreeCollinearPoints(A, B, C);
		checkResult = (checkResult || singleCheckResult);
		
		// check if one point is midpoint of segment made of other two points
		// (this will also cover the case of central symmetry)
		singleCheckResult = this.checkMidPoint(A, B, C);
		if (!singleCheckResult) {
			singleCheckResult = this.checkMidPoint(B, C, A);
			
			if (!singleCheckResult)
				singleCheckResult = this.checkMidPoint(C, A, B);
		}
		checkResult = (checkResult || singleCheckResult);
		
		// check if one point lies on perpendicular bisector of segment
		// made of other two points
		singleCheckResult = this.checkPerpBisectorPoint(A, B, C);
		if (!singleCheckResult) {
			singleCheckResult = this.checkPerpBisectorPoint(B, C, A);
			
			if (!singleCheckResult)
				singleCheckResult = this.checkPerpBisectorPoint(C, A, B);
		}
		checkResult = (checkResult || singleCheckResult);
		
		// check if these three points form right angle
		// (this will also cover the case when one point is on circle
		// whose diameter is segment made of other two points)
		singleCheckResult = this.checkRightAngle(A, B, C);
		if (!singleCheckResult) {
			singleCheckResult = this.checkRightAngle(B, C, A);
			
			if (!singleCheckResult)
				singleCheckResult = this.checkRightAngle(C, A, B);
		}
		checkResult = (checkResult || singleCheckResult);
		
		// check if one point is on circle determined by other two points
		singleCheckResult = this.checkPointOnCircle(A, B, C);
		if (!singleCheckResult) {
			singleCheckResult = this.checkPointOnCircle(B, A, C);
			
			if (!singleCheckResult) {
				singleCheckResult = this.checkPointOnCircle(B, C, A);
				
				if (!singleCheckResult) {
					singleCheckResult = this.checkPointOnCircle(C, B, A);
					
					if (!singleCheckResult) {
						singleCheckResult = this.checkPointOnCircle(C, A, B);
						
						if (!singleCheckResult)
							singleCheckResult = this.checkPointOnCircle(A, C, B);
					}
				}
			}
		}
		checkResult = (checkResult || singleCheckResult);
		
		// check algebraic sum of three segments
		singleCheckResult = this.checkAlgebraicSumOfThreeSegments(A, B, C);
		if (!singleCheckResult) {
			singleCheckResult = this.checkAlgebraicSumOfThreeSegments(B, C, A);
			
			if (!singleCheckResult)
				singleCheckResult = this.checkAlgebraicSumOfThreeSegments(C, A, B);
		}
		checkResult = (checkResult || singleCheckResult);
		
		return checkResult;
	}
}
