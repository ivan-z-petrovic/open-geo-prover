/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoobject;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.ogprover.polynomials.Power;
import com.ogprover.polynomials.SymbolicPolynomial;
import com.ogprover.polynomials.SymbolicTerm;
import com.ogprover.polynomials.SymbolicVariable;
import com.ogprover.polynomials.Term;
import com.ogprover.polynomials.Variable;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.auxiliary.GeneralizedSegment;
import com.ogprover.pp.tp.geoconstruction.Point;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for segment between two points and for vector from the first 
* 		towards the second point</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class Segment implements GeneralizedSegment, PointList {
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
	
	// This is segment AB
	/**
	 * <i><b>Symbolic label for first end point</b></i>
	 */
	private static final String ALabel = "A";
	/**
	 * <i><b>Symbolic label for second end point</b></i>
	 */
	private static final String BLabel = "B";
	/**
	 * <i>
	 * Symbolic polynomial representing the square of distance between
	 * two end points i.e. the square of segment's length
	 * </i>
	 */
	private static SymbolicPolynomial squareOfDistance = null;
	/**
	 * <i>
	 * Symbolic polynomial representing the x coordinate of oriented segment
	 * </i>
	 */
	private static SymbolicPolynomial xCoordOfOrientedSeg = null;
	/**
	 * <i>
	 * Symbolic polynomial representing the y coordinate of oriented segment
	 * </i>
	 */
	private static SymbolicPolynomial yCoordOfOrientedSeg = null;
	/**
	 * First end point of this segment
	 */
	private Point firstEndPoint = null;
	/**
	 * Second end point of this segment
	 */
	private Point secondEndPoint = null;
	/**
	 * Label of segment
	 */
	private String segLabel = null;
	
	// Static initializer of condition member 
	static {
		/*
		 * If A = (xA, yA) and B = (xB, yB) then square of distance between
		 * these two points is 
		 *    (xA - xB)^2 + (yA - yB)^2 = 0
		 *    xA^2 + xB^2 + yA^2 + yB^2 -2xAxB - 2yAyB = 0
		 *       
		 */
		if (squareOfDistance == null) {
			squareOfDistance = new SymbolicPolynomial();
			
			// Instances of symbolic variables
			SymbolicVariable xA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, ALabel);
			SymbolicVariable yA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, ALabel);
			SymbolicVariable xB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, BLabel);
			SymbolicVariable yB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, BLabel);
			
			// term xA^2
			Term t = new SymbolicTerm(1);
			t.addPower(new Power(xA, 2));
			squareOfDistance.addTerm(t);
			
			// term xB^2
			t = new SymbolicTerm(1);
			t.addPower(new Power(xB, 2));
			squareOfDistance.addTerm(t);
			
			// term yA^2
			t = new SymbolicTerm(1);
			t.addPower(new Power(yA, 2));
			squareOfDistance.addTerm(t);
			
			// term yB^2
			t = new SymbolicTerm(1);
			t.addPower(new Power(yB, 2));
			squareOfDistance.addTerm(t);
			
			// term -2 * xA * xB
			t = new SymbolicTerm(-2);
			t.addPower(new Power(xA, 1));
			t.addPower(new Power(xB, 1));
			squareOfDistance.addTerm(t);
			
			// term -2 * yA * yB
			t = new SymbolicTerm(-2);
			t.addPower(new Power(yA, 1));
			t.addPower(new Power(yB, 1));
			squareOfDistance.addTerm(t);
		}
		
		/*
		 * X coordinate of oriented segment (vector) AB is xB - xA.
		 */
		if (xCoordOfOrientedSeg == null) {
			xCoordOfOrientedSeg = new SymbolicPolynomial();
			
			// Instances of symbolic variables
			SymbolicVariable xA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, ALabel);
			SymbolicVariable xB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, BLabel);
			
			// term xB
			Term t = new SymbolicTerm(1);
			t.addPower(new Power(xB, 1));
			xCoordOfOrientedSeg.addTerm(t);
			
			// term -xA
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xA, 1));
			xCoordOfOrientedSeg.addTerm(t);
		}
		
		/*
		 * Y coordinate of oriented segment (vector) AB is yB - yA.
		 */
		if (yCoordOfOrientedSeg == null) {
			yCoordOfOrientedSeg = new SymbolicPolynomial();
			
			// Instances of symbolic variables
			SymbolicVariable yA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, ALabel);
			SymbolicVariable yB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, BLabel);
			
			// term yB
			Term t = new SymbolicTerm(1);
			t.addPower(new Power(yB, 1));
			yCoordOfOrientedSeg.addTerm(t);
			
			// term -yA
			t = new SymbolicTerm(-1);
			t.addPower(new Power(yA, 1));
			yCoordOfOrientedSeg.addTerm(t);
		}
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @param firstEndPoint the firstEndPoint to set
	 */
	public void setFirstEndPoint(Point firstEndPoint) {
		this.firstEndPoint = firstEndPoint;
	}

	/**
	 * @return the firstEndPoint
	 */
	public Point getFirstEndPoint() {
		return firstEndPoint;
	}

	/**
	 * @param secondEndPoint the secondEndPoint to set
	 */
	public void setSecondEndPoint(Point secondEndPoint) {
		this.secondEndPoint = secondEndPoint;
	}

	/**
	 * @return the secondEndPoint
	 */
	public Point getSecondEndPoint() {
		return secondEndPoint;
	}
	
	/**
	 * @see com.ogprover.pp.tp.geoobject.GeoObject#getGeoObjectLabel()
	 */
	public String getGeoObjectLabel() {
		return this.segLabel;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param firstP	First end point
	 * @param secondP	Second end point
	 */
	public Segment(Point firstP, Point secondP) {
		this.firstEndPoint = firstP;
		this.secondEndPoint = secondP;
		StringBuilder sb = new StringBuilder();
		sb.append("|");
		sb.append(firstP.getGeoObjectLabel());
		sb.append(secondP.getGeoObjectLabel());
		sb.append("|");
		this.segLabel = sb.toString(); 
	}
	
	/**
	 * Constructor method
	 * 
	 * @param firstP	First end point
	 * @param secondP	Second end point
	 * @param label		Label of segment
	 */
	public Segment(Point firstP, Point secondP, String label) {
		this(firstP, secondP);
		this.segLabel = label;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * <i>
	 * Method to populate the map of points for condition about square of distance
	 * </i>
	 * 
	 * @param pointA	First end point
	 * @param pointB	Second end point
	 * @return			Map of points with assigned points to symbolic labels
	 */
	public static Map<String, Point> getPointsMapForSquareOfDistance(Point pointA, Point pointB) {
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		
		pointsMap.put(ALabel, pointA);
		pointsMap.put(BLabel, pointB);
		
		return pointsMap;
	}
	
	/**
	 * <i>
	 * Method to populate the map of points' labels for condition about 
	 * square of distance
	 * </i>
	 * 
	 * @param labelA	Label of first end point
	 * @param labelB	Label of second end point
	 * @return			Map of points' labels with assigned new labels to symbolic labels
	 */
	public static Map<String, String> getLabelsMapForSquareOfDistance(String labelA, String labelB) {
		Map<String, String> labelsMap = new HashMap<String, String>();
		
		labelsMap.put(ALabel, labelA);
		labelsMap.put(BLabel, labelB);
		
		return labelsMap;
	}
	
	/**
	 * <i>
	 * Method that substitutes common points' labels with
	 * labels of passed in points in given symbolic polynomial representing
	 * some condition for segment.
	 * </i>
	 * 
	 * @param labelA	Label of first end point
	 * @param labelB	Label of second end point
	 * @return			Symbolic polynomial with substituted labels
	 */
	public static SymbolicPolynomial substitutePointLabelsForSquareOfDistance(SymbolicPolynomial symbPoly, String labelA, String labelB) {
		Map<String, String> labelsMap = getLabelsMapForSquareOfDistance(labelA, labelB);
		
		return symbPoly.substitute(labelsMap);
	}
	
	/**
	 * <i>
	 * Method that returns generic symbolic polynomial that represents the condition 
	 * for square of distance between two end points of this segment
	 * </i>
	 *  
	 * @return	Symbolic polynomial representing the condition
	 * 			for square of distance between two end points 
	 */
	public static SymbolicPolynomial getConditionForSquareOfDistance() {
		return squareOfDistance;
	}
	
	/**
	 * <i>
	 * Method that returns symbolic polynomial that represents the condition 
	 * for square of distance between two end points of this segment
	 * </i>
	 *  
	 * @return	Symbolic polynomial representing the condition
	 * 			for square of distance between two end points 
	 */
	public static SymbolicPolynomial getSubstitutedConditionForSquareOfDistance(String labelA, String labelB) {
		Map<String, String> labelsMap = new HashMap<String, String>();
		
		labelsMap.put(ALabel, labelA);
		labelsMap.put(BLabel, labelB);
		
		return ((SymbolicPolynomial) squareOfDistance.clone()).substitute(labelsMap);
	}
	
	/**
	 * Method that retrieves polynomial for square of this segment
	 * 
	 * @return	XPolynomial object that represents the polynomial for 
	 * 			square of this segment
	 */
	public XPolynomial getInstantiatedConditionForSquareOfSegment() {
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		
		pointsMap.put(ALabel, this.firstEndPoint);
		pointsMap.put(BLabel, this.secondEndPoint);
		
		return OGPTP.instantiateCondition(Segment.squareOfDistance, pointsMap); // do not call reduction by UTerm division here
	}
	
	/**
	 * Method that retrieves polynomial for x coordinate of oriented segment
	 * 
	 * @return	XPolynomial object that represents the polynomial for x coordinate
	 * 			of this segment as oriented segment
	 */
	public XPolynomial getInstantiatedXCoordinateOfOrientedSegment() {
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		
		pointsMap.put(ALabel, this.firstEndPoint);
		pointsMap.put(BLabel, this.secondEndPoint);
		
		return OGPTP.instantiateCondition(Segment.xCoordOfOrientedSeg, pointsMap); // do not call reduction by UTerm division here
	}
	
	/**
	 * Method that retrieves polynomial for y coordinate of oriented segment
	 * 
	 * @return	XPolynomial object that represents the polynomial for y coordinate
	 * 			of this segment as oriented segment
	 */
	public XPolynomial getInstantiatedYCoordinateOfOrientedSegment() {
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		
		pointsMap.put(ALabel, this.firstEndPoint);
		pointsMap.put(BLabel, this.secondEndPoint);
		
		return OGPTP.instantiateCondition(Segment.yCoordOfOrientedSeg, pointsMap); // do not call reduction by UTerm division here
	}

	/**
	 * @see com.ogprover.pp.tp.auxiliary.GeneralizedSegment#getDescription()
	 */
	public String getDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.firstEndPoint.getGeoObjectLabel());
		sb.append(this.secondEndPoint.getGeoObjectLabel());
		return sb.toString();
	}

	/**
	 * @see com.ogprover.pp.tp.geoobject.PointList#getPoints()
	 */
	public Vector<Point> getPoints() {
		Vector<Point> points = new Vector<Point>();
		points.add(this.firstEndPoint);
		points.add(this.secondEndPoint);
		return points;
	}
	
}


