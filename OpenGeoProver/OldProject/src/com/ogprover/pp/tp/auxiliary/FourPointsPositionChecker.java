/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.auxiliary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.ogprover.polynomials.UXVariable;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.geoconstruction.AngleBisector;
import com.ogprover.pp.tp.geoconstruction.Circle;
import com.ogprover.pp.tp.geoconstruction.CircleWithCenterAndPoint;
import com.ogprover.pp.tp.geoconstruction.CircleWithCenterAndRadius;
import com.ogprover.pp.tp.geoconstruction.Line;
import com.ogprover.pp.tp.geoconstruction.LineThroughTwoPoints;
import com.ogprover.pp.tp.geoconstruction.Point;
import com.ogprover.pp.tp.geoobject.Segment;
import com.ogprover.pp.tp.ndgcondition.AlgebraicNDGCondition;
import com.ogprover.pp.tp.thmstatement.CollinearPoints;
import com.ogprover.pp.tp.thmstatement.ConcyclicPoints;
import com.ogprover.pp.tp.thmstatement.FourHarmonicConjugatePoints;
import com.ogprover.pp.tp.thmstatement.LinearCombinationOfOrientedSegments;
import com.ogprover.pp.tp.thmstatement.PointOnSetOfPoints;
import com.ogprover.pp.tp.thmstatement.SegmentsOfEqualLengths;
import com.ogprover.pp.tp.thmstatement.TwoInversePoints;
import com.ogprover.pp.tp.thmstatement.TwoParallelLines;
import com.ogprover.pp.tp.thmstatement.TwoPerpendicularLines;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for checking whether certain points' positions 
*     of four points correspond to specified NDG condition</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class FourPointsPositionChecker extends PointsPositionChecker {
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
	public FourPointsPositionChecker(AlgebraicNDGCondition ndgCond){
		this.initializePointsPositionChecker(ndgCond);
	}
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/*
	 * Specific positions for four points
	 */
	/**
	 * Check if points are collinear.
	 * 
	 * @param A		First point
	 * @param B		Second point
	 * @param C		Third point
	 * @param D		Fourth point
	 * @return		True if condition is met, false otherwise
	 */
	private boolean checkFourCollinearPoints(Point A, Point B, Point C, Point D) {
		this.clearAuxCP();
		this.auxiliaryCP.addGeoConstruction(A);
		this.auxiliaryCP.addGeoConstruction(B);
		this.auxiliaryCP.addGeoConstruction(C);
		this.auxiliaryCP.addGeoConstruction(D);
		ArrayList<Point> pointList = new ArrayList<Point>();
		pointList.add(A);
		pointList.add(B);
		pointList.add(C);
		pointList.add(D);
		this.auxiliaryCP.addThmStatement(new CollinearPoints(pointList));
		XPolynomial statementPoly = this.auxiliaryCP.getTheoremStatement().getAlgebraicForm();
		
		if (statementPoly != null && statementPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial())) {
			Vector<Point> pointsV = new Vector<Point>();
			pointsV.add(A);
			pointsV.add(B);
			pointsV.add(C);
			pointsV.add(D);
			this.ndgCond.addNewTranslation(AlgebraicNDGCondition.NDG_TYPE_4PT_COLLINEAR, pointsV);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check if points are concyclic.
	 * 
	 * @param A		First point
	 * @param B		Second point
	 * @param C		Third point
	 * @param D		Fourth point
	 * @return		True if condition is met, false otherwise
	 */
	private boolean checkFourConcyclicPoints(Point A, Point B, Point C, Point D) {
		/*
		 *  There should not be any three collinear points. In general case such points does not
		 *  satisfy condition for four concyclic points, but in case when three points are on
		 *  same vertical or horizontal line this could happen. 
		 */
		Map<String, UXVariable> xVarMap = new HashMap<String, UXVariable>();
		xVarMap.put(A.getX().toString(), A.getX());
		xVarMap.put(B.getX().toString(), B.getX());
		xVarMap.put(C.getX().toString(), C.getX());
		xVarMap.put(D.getX().toString(), D.getX());
		
		Map<String, UXVariable> yVarMap = new HashMap<String, UXVariable>();
		yVarMap.put(A.getY().toString(), A.getY());
		yVarMap.put(B.getY().toString(), B.getY());
		yVarMap.put(C.getY().toString(), C.getY());
		yVarMap.put(D.getY().toString(), D.getY());
		
		if (xVarMap.size() <= 2 || yVarMap.size() <= 2) // at least three points have one same coordinate 
			return false; // found at least three collinear points
		
		this.clearAuxCP();
		this.auxiliaryCP.addGeoConstruction(A);
		this.auxiliaryCP.addGeoConstruction(B);
		this.auxiliaryCP.addGeoConstruction(C);
		this.auxiliaryCP.addGeoConstruction(D);
		ArrayList<Point> pointList = new ArrayList<Point>();
		pointList.add(A);
		pointList.add(B);
		pointList.add(C);
		pointList.add(D);
		this.auxiliaryCP.addThmStatement(new ConcyclicPoints(pointList));
		XPolynomial statementPoly = this.auxiliaryCP.getTheoremStatement().getAlgebraicForm();
		
		if (statementPoly != null && statementPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial())) {
			Vector<Point> pointsV = new Vector<Point>();
			pointsV.add(A);
			pointsV.add(B);
			pointsV.add(C);
			pointsV.add(D);
			this.ndgCond.addNewTranslation(AlgebraicNDGCondition.NDG_TYPE_4PT_CONCYCLIC, pointsV);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check if segment AB is equal to segment CD.
	 * 
	 * @param A		First point
	 * @param B		Second point
	 * @param C		Third point
	 * @param D		Fourth point
	 * @return		True if condition is met, false otherwise
	 */
	private boolean checkEqualSegments(Point A, Point B, Point C, Point D) {
		this.clearAuxCP();
		this.auxiliaryCP.addGeoConstruction(A);
		this.auxiliaryCP.addGeoConstruction(B);
		this.auxiliaryCP.addGeoConstruction(C);
		this.auxiliaryCP.addGeoConstruction(D);
		this.auxiliaryCP.addThmStatement(new SegmentsOfEqualLengths(A, B, C, D));
		XPolynomial statementPoly = this.auxiliaryCP.getTheoremStatement().getAlgebraicForm();
		
		if (statementPoly != null && statementPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial())) {
			Vector<Point> pointsV = new Vector<Point>();
			pointsV.add(A);
			pointsV.add(B);
			pointsV.add(C);
			pointsV.add(D);
			this.ndgCond.addNewTranslation(AlgebraicNDGCondition.NDG_TYPE_4PT_EQ_SEG, pointsV);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check if lines AB and CD are parallel.
	 * 
	 * @param A		First point
	 * @param B		Second point
	 * @param C		Third point
	 * @param D		Fourth point
	 * @return		True if condition is met, false otherwise
	 */
	private boolean checkParallelLines(Point A, Point B, Point C, Point D) {
		this.clearAuxCP();
		this.auxiliaryCP.addGeoConstruction(A);
		this.auxiliaryCP.addGeoConstruction(B);
		this.auxiliaryCP.addGeoConstruction(C);
		this.auxiliaryCP.addGeoConstruction(D);
		Line AB = new LineThroughTwoPoints("AB", A, B);
		this.auxiliaryCP.addGeoConstruction(AB);
		Line CD = new LineThroughTwoPoints("CD", C, D);
		this.auxiliaryCP.addGeoConstruction(CD);
		this.auxiliaryCP.addThmStatement(new TwoParallelLines(AB, CD));
		XPolynomial statementPoly = this.auxiliaryCP.getTheoremStatement().getAlgebraicForm();
		
		if (statementPoly != null && statementPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial())) {
			Vector<Point> pointsV = new Vector<Point>();
			pointsV.add(A);
			pointsV.add(B);
			pointsV.add(C);
			pointsV.add(D);
			this.ndgCond.addNewTranslation(AlgebraicNDGCondition.NDG_TYPE_4PT_PARALLEL, pointsV);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check if lines AB and CD are perpendicular.
	 * 
	 * @param A		First point
	 * @param B		Second point
	 * @param C		Third point
	 * @param D		Fourth point
	 * @return		True if condition is met, false otherwise
	 */
	private boolean checkPerpendicularLines(Point A, Point B, Point C, Point D) {
		this.clearAuxCP();
		this.auxiliaryCP.addGeoConstruction(A);
		this.auxiliaryCP.addGeoConstruction(B);
		this.auxiliaryCP.addGeoConstruction(C);
		this.auxiliaryCP.addGeoConstruction(D);
		Line AB = new LineThroughTwoPoints("AB", A, B);
		this.auxiliaryCP.addGeoConstruction(AB);
		Line CD = new LineThroughTwoPoints("CD", C, D);
		this.auxiliaryCP.addGeoConstruction(CD);
		this.auxiliaryCP.addThmStatement(new TwoPerpendicularLines(AB, CD));
		XPolynomial statementPoly = this.auxiliaryCP.getTheoremStatement().getAlgebraicForm();
		
		if (statementPoly != null && statementPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial())) {
			Vector<Point> pointsV = new Vector<Point>();
			pointsV.add(A);
			pointsV.add(B);
			pointsV.add(C);
			pointsV.add(D);
			this.ndgCond.addNewTranslation(AlgebraicNDGCondition.NDG_TYPE_4PT_PERPENDICULAR, pointsV);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check if par of points (A, B) and (C, D) are in harmonic conjunction.
	 * 
	 * @param A		First point
	 * @param B		Second point
	 * @param C		Third point
	 * @param D		Fourth point
	 * @return		True if condition is met, false otherwise
	 */
	private boolean checkHarmonicConjugatePoints(Point A, Point B, Point C, Point D) {
		this.clearAuxCP();
		this.auxiliaryCP.addGeoConstruction(A);
		this.auxiliaryCP.addGeoConstruction(B);
		this.auxiliaryCP.addGeoConstruction(C);
		this.auxiliaryCP.addGeoConstruction(D);
		this.auxiliaryCP.addThmStatement(new FourHarmonicConjugatePoints(A, B, C, D));
		XPolynomial statementXPoly = ((FourHarmonicConjugatePoints) this.auxiliaryCP.getTheoremStatement()).getXAlgebraicForm();
		XPolynomial statementYPoly = ((FourHarmonicConjugatePoints) this.auxiliaryCP.getTheoremStatement()).getYAlgebraicForm();
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
			pointsV.add(D);
			this.ndgCond.addNewTranslation(AlgebraicNDGCondition.NDG_TYPE_4PT_HARMONIC, pointsV);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check if segments AB and CD are two congruent collinear segments.
	 * This also covers the case of translated point.
	 * 
	 * @param A		First point
	 * @param B		Second point
	 * @param C		Third point
	 * @param D		Fourth point
	 * @return		True if condition is met, false otherwise
	 */
	private boolean checkCongruentCollinearSegments(Point A, Point B, Point C, Point D) {
		this.clearAuxCP();
		this.auxiliaryCP.addGeoConstruction(A);
		this.auxiliaryCP.addGeoConstruction(B);
		this.auxiliaryCP.addGeoConstruction(C);
		this.auxiliaryCP.addGeoConstruction(D);
		Segment segAB = new Segment(A, B);
		Segment segCD = new Segment(C, D);
		Vector<Segment> segments = new Vector<Segment>();
		segments.add(segAB);
		segments.add(segCD);
		Vector<Double> coefficients = new Vector<Double>();
		coefficients.add(new Double(1));
		coefficients.add(new Double(-1));
		this.auxiliaryCP.addThmStatement(new LinearCombinationOfOrientedSegments(segments, coefficients));
		XPolynomial statementPoly = this.auxiliaryCP.getTheoremStatement().getAlgebraicForm();
		
		if (statementPoly != null && statementPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial())) {
			Vector<Point> pointsV = new Vector<Point>();
			pointsV.add(A);
			pointsV.add(B);
			pointsV.add(C);
			pointsV.add(D);
			this.ndgCond.addNewTranslation(AlgebraicNDGCondition.NDG_TYPE_4PT_CONG_COLL_SEG, pointsV);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check if point D is on angle bisector of angle &lt;ABC.
	 * 
	 * @param A		First point
	 * @param B		Second point
	 * @param C		Third point
	 * @param D		Fourth point
	 * @return		True if condition is met, false otherwise
	 */
	private boolean checkAngleBisectorPoint(Point A, Point B, Point C, Point D) {
		this.clearAuxCP();
		this.auxiliaryCP.addGeoConstruction(A);
		this.auxiliaryCP.addGeoConstruction(B);
		this.auxiliaryCP.addGeoConstruction(C);
		this.auxiliaryCP.addGeoConstruction(D);
		StringBuilder nameSB = new StringBuilder("tempAngBis_");
		nameSB.append(A.getGeoObjectLabel());
		nameSB.append(B.getGeoObjectLabel());
		nameSB.append(C.getGeoObjectLabel());
		Line angBis = new AngleBisector(nameSB.toString(), A, B, C);
		this.auxiliaryCP.addGeoConstruction(angBis);
		this.auxiliaryCP.addThmStatement(new PointOnSetOfPoints(angBis, D));
		XPolynomial statementPoly = this.auxiliaryCP.getTheoremStatement().getAlgebraicForm();
		
		if (statementPoly != null && statementPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial())) {
			Vector<Point> pointsV = new Vector<Point>();
			pointsV.add(A);
			pointsV.add(B);
			pointsV.add(C);
			pointsV.add(D);
			this.ndgCond.addNewTranslation(AlgebraicNDGCondition.NDG_TYPE_4PT_ON_ANG_BIS, pointsV);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check if points C and D are on circle with center A and one its point B.
	 * 
	 * @param A		First point
	 * @param B		Second point
	 * @param C		Third point
	 * @param D		Fourth point
	 * @return		True if condition is met, false otherwise
	 */
	private boolean checkTwoPointsOnCircle(Point A, Point B, Point C, Point D) {
		this.clearAuxCP();
		this.auxiliaryCP.addGeoConstruction(A);
		this.auxiliaryCP.addGeoConstruction(B);
		this.auxiliaryCP.addGeoConstruction(C);
		this.auxiliaryCP.addGeoConstruction(D);
		Circle k = new CircleWithCenterAndPoint("tempCircle", A, B);
		this.auxiliaryCP.addGeoConstruction(k);
		this.auxiliaryCP.addThmStatement(new PointOnSetOfPoints(k, C));
		XPolynomial statementPolyC = this.auxiliaryCP.getTheoremStatement().getAlgebraicForm();
		this.auxiliaryCP.addThmStatement(new PointOnSetOfPoints(k, D));
		XPolynomial statementPolyD = this.auxiliaryCP.getTheoremStatement().getAlgebraicForm();
		XPolynomial statementPoly = null;
		
		if (statementPolyC != null && statementPolyD != null)
			statementPoly = (XPolynomial) statementPolyC.clone().multiplyByPolynomial(statementPolyC)
			                                                    .addPolynomial(statementPolyD.clone().multiplyByPolynomial(statementPolyD));
		
		if (statementPoly != null && statementPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial())) {
			Vector<Point> pointsV = new Vector<Point>();
			pointsV.add(A);
			pointsV.add(B);
			pointsV.add(C);
			pointsV.add(D);
			this.ndgCond.addNewTranslation(AlgebraicNDGCondition.NDG_TYPE_4PT_2_ON_CIRCLE, pointsV);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check if point D is on circle with center A and radius equal to segment BC.
	 * 
	 * @param A		First point
	 * @param B		Second point
	 * @param C		Third point
	 * @param D		Fourth point
	 * @return		True if condition is met, false otherwise
	 */
	private boolean checkPointOnCircle(Point A, Point B, Point C, Point D) {
		this.clearAuxCP();
		this.auxiliaryCP.addGeoConstruction(A);
		this.auxiliaryCP.addGeoConstruction(B);
		this.auxiliaryCP.addGeoConstruction(C);
		this.auxiliaryCP.addGeoConstruction(D);
		Circle k = new CircleWithCenterAndRadius("tempCircle", A, B, C);
		this.auxiliaryCP.addGeoConstruction(k);
		this.auxiliaryCP.addThmStatement(new PointOnSetOfPoints(k, D));
		XPolynomial statementPoly = this.auxiliaryCP.getTheoremStatement().getAlgebraicForm();
		
		if (statementPoly != null && statementPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial())) {
			Vector<Point> pointsV = new Vector<Point>();
			pointsV.add(A);
			pointsV.add(B);
			pointsV.add(C);
			pointsV.add(D);
			this.ndgCond.addNewTranslation(AlgebraicNDGCondition.NDG_TYPE_4PT_ON_CIRCLE, pointsV);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check if points C and D are two inverse points with respect to circle
	 * with center A one one point from it B.
	 * 
	 * @param A		First point
	 * @param B		Second point
	 * @param C		Third point
	 * @param D		Fourth point
	 * @return		True if condition is met, false otherwise
	 */
	private boolean checkInversePoints(Point A, Point B, Point C, Point D) {
		this.clearAuxCP();
		this.auxiliaryCP.addGeoConstruction(A);
		this.auxiliaryCP.addGeoConstruction(B);
		this.auxiliaryCP.addGeoConstruction(C);
		this.auxiliaryCP.addGeoConstruction(D);
		Circle k = new CircleWithCenterAndPoint("tempCircle", A, B);
		this.auxiliaryCP.addGeoConstruction(k);
		this.auxiliaryCP.addThmStatement(new TwoInversePoints(C, D, k));
		XPolynomial statementXPoly = ((TwoInversePoints) this.auxiliaryCP.getTheoremStatement()).getXAlgebraicForm();
		XPolynomial statementYPoly = ((TwoInversePoints) this.auxiliaryCP.getTheoremStatement()).getYAlgebraicForm();
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
			pointsV.add(D);
			this.ndgCond.addNewTranslation(AlgebraicNDGCondition.NDG_TYPE_4PT_INVERSE, pointsV);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Method that checks if some position of four given points can
	 * generate polynomial for attached NDG condition. If can, text of 
	 * this NDG condition is populated with appropriate readable
	 * form that describes specific position of these four points
	 * that generates this NDG condition. The text actually corresponds 
	 * to the negation of attached NDG condition.
	 * 
	 * @param pointList		List with four points
	 * @return		        True if some position generates attached NDG condition,
	 * 				        false otherwise
	 * 
	 * @see com.ogprover.pp.tp.auxiliary.PointsPositionChecker#checkPositions(java.util.Vector)
	 * 
	 */
	public boolean checkPositions(Vector<Point> pointList) {
		if (pointList == null || pointList.size() != 4)
			return false;
		
		boolean checkResult = false;
		boolean singleCheckResult = false;
		
		// we clone points since they will be added to new CP
		Point A = pointList.get(0).clone();
		Point B = pointList.get(1).clone();
		Point C = pointList.get(2).clone();
		Point D = pointList.get(3).clone();
		
		// check if points are collinear
		singleCheckResult = this.checkFourCollinearPoints(A, B, C, D);
		checkResult = (checkResult || singleCheckResult);
		
		// check if points are concyclic
		singleCheckResult = this.checkFourConcyclicPoints(A, B, C, D);
		checkResult = (checkResult || singleCheckResult);
		
		// check equal segments
		singleCheckResult = this.checkEqualSegments(A, B, C, D);
		if (!singleCheckResult) {
			singleCheckResult = this.checkEqualSegments(A, C, B, D);
			
			if (!singleCheckResult) {
				singleCheckResult = this.checkEqualSegments(A, D, B, C);
			}
		}
		checkResult = (checkResult || singleCheckResult);
		
		// check parallel lines
		singleCheckResult = this.checkParallelLines(A, B, C, D);
		if (!singleCheckResult) {
			singleCheckResult = this.checkParallelLines(A, C, B, D);
			
			if (!singleCheckResult) {
				singleCheckResult = this.checkParallelLines(A, D, B, C);
			}
		}
		checkResult = (checkResult || singleCheckResult);
		
		// check perpendicular lines
		singleCheckResult = this.checkPerpendicularLines(A, B, C, D);
		if (!singleCheckResult) {
			singleCheckResult = this.checkPerpendicularLines(A, C, B, D);
			
			if (!singleCheckResult) {
				singleCheckResult = this.checkPerpendicularLines(A, D, B, C);
			}
		}
		checkResult = (checkResult || singleCheckResult);
		
		// check harmonic conjugate points
		singleCheckResult = this.checkHarmonicConjugatePoints(A, B, C, D);
		if (!singleCheckResult) {
			singleCheckResult = this.checkHarmonicConjugatePoints(A, C, B, D);
			
			if (!singleCheckResult) {
				singleCheckResult = this.checkHarmonicConjugatePoints(A, D, B, C);
			}
		}
		checkResult = (checkResult || singleCheckResult);
		
		// check congruent and collinear segments
		singleCheckResult = this.checkCongruentCollinearSegments(A, B, C, D);
		if (!singleCheckResult) {
			singleCheckResult = this.checkCongruentCollinearSegments(A, C, B, D);
			
			if (!singleCheckResult) {
				singleCheckResult = this.checkCongruentCollinearSegments(A, D, B, C);
			}
		}
		checkResult = (checkResult || singleCheckResult);
		
		// check point on angle bisector
		singleCheckResult = this.checkAngleBisectorPoint(C, A, B, D);
		if (!singleCheckResult) {
			singleCheckResult = this.checkAngleBisectorPoint(A, B, C, D);
			
			if (!singleCheckResult) {
				singleCheckResult = this.checkAngleBisectorPoint(B, C, A, D);
				
				if (!singleCheckResult) {
					singleCheckResult = this.checkAngleBisectorPoint(D, A, B, C);
					if (!singleCheckResult) {
						singleCheckResult = this.checkAngleBisectorPoint(A, B, D, C);
						
						if (!singleCheckResult) {
							singleCheckResult = this.checkAngleBisectorPoint(B, D, A, C);
							
							if (!singleCheckResult) {
								singleCheckResult = this.checkAngleBisectorPoint(D, A, C, B);
								if (!singleCheckResult) {
									singleCheckResult = this.checkAngleBisectorPoint(A, C, D, B);
									
									if (!singleCheckResult) {
										singleCheckResult = this.checkAngleBisectorPoint(C, D, A, B);
										
										if (!singleCheckResult) {
											singleCheckResult = this.checkAngleBisectorPoint(D, B, C, A);
											if (!singleCheckResult) {
												singleCheckResult = this.checkAngleBisectorPoint(B, C, D, A);
												
												if (!singleCheckResult) {
													singleCheckResult = this.checkAngleBisectorPoint(C, D, B, A);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		checkResult = (checkResult || singleCheckResult);
		
		// check two points on circle
		singleCheckResult = this.checkTwoPointsOnCircle(A, B, C, D);
		if (!singleCheckResult) {
			singleCheckResult = this.checkTwoPointsOnCircle(B, A, C, D);
			
			if (!singleCheckResult) {
				singleCheckResult = this.checkTwoPointsOnCircle(A, C, B, D);
				
				if (!singleCheckResult) {
					singleCheckResult = this.checkTwoPointsOnCircle(C, A, B, D);
					if (!singleCheckResult) {
						singleCheckResult = this.checkTwoPointsOnCircle(A, D, B, C);
						
						if (!singleCheckResult) {
							singleCheckResult = this.checkTwoPointsOnCircle(D, A, B, C);
							
							if (!singleCheckResult) {
								singleCheckResult = this.checkTwoPointsOnCircle(B, C, A, D);
								if (!singleCheckResult) {
									singleCheckResult = this.checkTwoPointsOnCircle(C, B, D, A);
									
									if (!singleCheckResult) {
										singleCheckResult = this.checkTwoPointsOnCircle(B, D, A, C);
										
										if (!singleCheckResult) {
											singleCheckResult = this.checkTwoPointsOnCircle(D, B, A, C);
											if (!singleCheckResult) {
												singleCheckResult = this.checkTwoPointsOnCircle(C, D, A, B);
												
												if (!singleCheckResult) {
													singleCheckResult = this.checkTwoPointsOnCircle(D, C, A, B);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		checkResult = (checkResult || singleCheckResult);
		
		// check one point on circle
		singleCheckResult = this.checkPointOnCircle(A, B, C, D);
		if (!singleCheckResult) {
			singleCheckResult = this.checkPointOnCircle(A, B, D, C);
			
			if (!singleCheckResult) {
				singleCheckResult = this.checkPointOnCircle(A, C, D, B);
				
				if (!singleCheckResult) {
					singleCheckResult = this.checkPointOnCircle(B, A, C, D);
					if (!singleCheckResult) {
						singleCheckResult = this.checkPointOnCircle(B, A, D, C);
						
						if (!singleCheckResult) {
							singleCheckResult = this.checkPointOnCircle(B, C, D, A);
							
							if (!singleCheckResult) {
								singleCheckResult = this.checkPointOnCircle(C, A, B, D);
								if (!singleCheckResult) {
									singleCheckResult = this.checkPointOnCircle(C, A, D, B);
									
									if (!singleCheckResult) {
										singleCheckResult = this.checkPointOnCircle(C, B, D, A);
										
										if (!singleCheckResult) {
											singleCheckResult = this.checkPointOnCircle(D, A, B, C);
											if (!singleCheckResult) {
												singleCheckResult = this.checkPointOnCircle(D, A, C, B);
												
												if (!singleCheckResult) {
													singleCheckResult = this.checkPointOnCircle(D, B, C, A);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		checkResult = (checkResult || singleCheckResult);
		
		// check inverse points - this is incomplete (see "to do" comment below)
		singleCheckResult = this.checkInversePoints(A, B, C, D);
		if (!singleCheckResult) {
			singleCheckResult = this.checkInversePoints(B, A, C, D);
			
			if (!singleCheckResult) {
				singleCheckResult = this.checkInversePoints(A, C, B, D);
				
				if (!singleCheckResult) {
					singleCheckResult = this.checkInversePoints(C, A, B, D);
					if (!singleCheckResult) {
						singleCheckResult = this.checkInversePoints(A, D, B, C);
						
						if (!singleCheckResult) {
							singleCheckResult = this.checkInversePoints(D, A, B, C);
							
							if (!singleCheckResult) {
								singleCheckResult = this.checkInversePoints(B, C, A, D);
								if (!singleCheckResult) {
									singleCheckResult = this.checkInversePoints(C, B, A, D);
									
									if (!singleCheckResult) {
										singleCheckResult = this.checkInversePoints(B, D, A, C);
										
										if (!singleCheckResult) {
											singleCheckResult = this.checkInversePoints(D, B, A, C);
											if (!singleCheckResult) {
												singleCheckResult = this.checkInversePoints(C, D, A, B);
												
												if (!singleCheckResult) {
													singleCheckResult = this.checkInversePoints(D, C, A, B);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		checkResult = (checkResult || singleCheckResult);
		// TODO - maybe it is enough/correct just to consider inverse points when all 4 points are collinear
		
		// check two points on perpendicular bisector of same segment
		// TODO
		
		// check two right angles (<ABC && <BCD) - isn't this sub-case of AB || CD ?
		// TODO
		
		// check two touching circles (each either with center and point or with diameter)
		// TODO
		
		
		return checkResult;
	}
}
