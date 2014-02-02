/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoconstruction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.ogprover.main.OGPConstants;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.SymbolicPolynomial;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.auxiliary.PointSetRelationshipManager;
import com.ogprover.pp.tp.geoobject.Segment;
import com.ogprover.utilities.io.OGPOutput;
import com.ogprover.utilities.logger.ILogger;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for construction of radical axis of two circles</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class RadicalAxis extends Line {
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
	
	// This line is radical axis of two circles
	// k1(O1, r1) and k2(O2, r2)
	/**
	 * <i><b>Symbolic label for generic point from this line</b></i>
	 */
	private static final String M0Label = "0"; // zero
	/**
	 * <i><b>Symbolic label for center of first circle</b></i>
	 */
	private static final String O1Label = "O1"; // 'O' letter
	/**
	 * <i><b>Symbolic label for first point from radius of first circle</b></i>
	 */
	private static final String A1Label = "A1";
	/**
	 * <i><b>Symbolic label for second point from radius of first circle</b></i>
	 */
	private static final String B1Label = "B1";
	/**
	 * <i><b>Symbolic label for center of second circle</b></i>
	 */
	private static final String O2Label = "O2"; // 'O' letter
	/**
	 * <i><b>Symbolic label for first point from radius of second circle</b></i>
	 */
	private static final String A2Label = "A2";
	/**
	 * <i><b>Symbolic label for second point from radius of second circle</b></i>
	 */
	private static final String B2Label = "B2";
	/**
	 * First circle of this radical axis
	 */
	private Circle firstCircle;
	/**
	 * Second circle of this radical axis
	 */
	private Circle secondCircle;
	/**
	 * <i>
	 * Symbolic polynomial representing the condition for some
	 * point to belong to this line
	 * </i>
	 */
	private static SymbolicPolynomial conditionForRadicalAxis = null;
	
	// Static initializer of condition members 
	static {
		/* 
		 * Let M0=(x0, y0) be the point from radical axis of circles
		 * k1(O1, A1B1) and k2(O2, A2B2).
		 * 
		 * If M0C1 and M0C2 are tangent segments from M0 to k1 and k2 respectively,
		 * then since M0 is on radical axis it is satisfied: M0C1 = M0C2.
		 * From this:
		 * M0C1^2 = M0C2^2
		 * M0O1^2 - O1C1^2 = M0O2^2 - O2C2^2
		 * M0O1^2 - M0O2^2 - A1B1^2 + A2B2^2 = 0
		 */
		if (conditionForRadicalAxis == null) {
			conditionForRadicalAxis = new SymbolicPolynomial();
			
			SymbolicPolynomial tempSymbPoly = (SymbolicPolynomial) Segment.getConditionForSquareOfDistance().clone();
			Segment.substitutePointLabelsForSquareOfDistance(tempSymbPoly, M0Label, O1Label);
			conditionForRadicalAxis.addPolynomial(tempSymbPoly);
			
			tempSymbPoly = (SymbolicPolynomial) Segment.getConditionForSquareOfDistance().clone();
			Segment.substitutePointLabelsForSquareOfDistance(tempSymbPoly, M0Label, O2Label);
			conditionForRadicalAxis.subtractPolynomial(tempSymbPoly);
			
			tempSymbPoly = (SymbolicPolynomial) Segment.getConditionForSquareOfDistance().clone();
			Segment.substitutePointLabelsForSquareOfDistance(tempSymbPoly, A1Label, B1Label);
			conditionForRadicalAxis.subtractPolynomial(tempSymbPoly);
			
			tempSymbPoly = (SymbolicPolynomial) Segment.getConditionForSquareOfDistance().clone();
			Segment.substitutePointLabelsForSquareOfDistance(tempSymbPoly, A2Label, B2Label);
			conditionForRadicalAxis.addPolynomial(tempSymbPoly);
		}
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method that gives the type of this construction
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getConstructionType()
	 */
	@Override
	public int getConstructionType() {
		return GeoConstruction.GEOCONS_TYPE_RADICAL_AXIS;
	}
	
	/**
	 * @param firstCircle the firstCircle to set
	 */
	public void setFirstCircle(Circle firstCircle) {
		this.firstCircle = firstCircle;
	}

	/**
	 * @return the firstCircle
	 */
	public Circle getFirstCircle() {
		return firstCircle;
	}

	/**
	 * @param secondCircle the secondCircle to set
	 */
	public void setSecondCircle(Circle secondCircle) {
		this.secondCircle = secondCircle;
	}

	/**
	 * @return the secondCircle
	 */
	public Circle getSecondCircle() {
		return secondCircle;
	}
	
	/**
	 * Method that retrieves symbolic polynomial that represents the condition
	 * that some point belongs to radical axis of two circles.
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.Line#getCondition()
	 */
	public SymbolicPolynomial getCondition() { 
		return conditionForRadicalAxis;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param lineLabel			Label of this line
	 * @param firstCircle		First circle of this line
	 * @param secondCircle		Second circle of this line
	 */
	public RadicalAxis(String lineLabel, Circle firstCircle, Circle secondCircle) {
		this.geoObjectLabel = lineLabel;
		this.firstCircle = firstCircle;
		this.secondCircle = secondCircle;
		this.points = new Vector<Point>();
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method to check the validity of this construction step
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#isValidConstructionStep()
	 */
	@Override
	public boolean isValidConstructionStep() {
		OGPOutput output = OpenGeoProver.settings.getOutput();
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		if (!super.isValidConstructionStep())
			return false;
		
		try {
			// Radical axis is valid if both circles have been already constructed 
			int index1, index2;
		
			// Angle points must exist
			if (this.firstCircle == null || this.secondCircle == null) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Cannot construct radical axis " + this.getGeoObjectLabel() + " because some of circles is not constructed");
				return false;
			}
		
			index1 = this.firstCircle.getIndex();
			index2 = this.secondCircle.getIndex();
		
			if (index1 < 0 || index2 < 0) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Cannot construct radical axis " + this.getGeoObjectLabel() + " because some of circles is not added to theorem protocol");
				return false; // some object is not in theorem protocol
			}
		
			if (index1 >= this.index || index2 >= this.index) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Cannot construct radical axis " + this.getGeoObjectLabel() + " because some of circles is not yet constructed");
				return false; // some object is not constructed before this line
			}
			
			// each circle must have center already constructed
			Point c1 = this.firstCircle.getCenter();
			Point c2 = this.secondCircle.getCenter();
			
			if (c1 == null || c2 == null || 
				c1.getIndex() >= this.index || c2.getIndex() >= this.index ||
				c1.getIndex() < 0 || c2.getIndex() < 0) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Cannot construct radical axis " + this.getGeoObjectLabel() + " because some of circles doesn't have constructed center");
				return false;
			}
		
			return true;
		} catch (IOException e) {
			logger.error("Failed to write to output file(s).");
			output.close();
			return false;
		}
	}

	/**
	 * Method for finding best points for instantiation of condition
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.Line#findBestPointsForInstantation(com.ogprover.pp.tp.auxiliary.PointSetRelationshipManager)
	 */
	@Override
	public int findBestPointsForInstantation(PointSetRelationshipManager manager) {
		ILogger logger = OpenGeoProver.settings.getLogger();
		
		// First call method from superclass - it implements default behavior considering
		// this line as plain line through two points
		super.findBestPointsForInstantation(manager);
		
		// in case of error exit
		if (manager.isErrorFlag()) {
			logger.error("Failed in findBestPointsForInstantation() method from superclass");
			return OGPConstants.ERR_CODE_GENERAL;
		}
		
		if (manager.getPoint().getPointState() == Point.POINT_STATE_RENAMED)
			return OGPConstants.RET_CODE_SUCCESS;
		
		// NOTE: This method is used to find best elements for process of transformation
		// in algebraic form. It is implemented by calling same methods like in
		// final transformation, but without printing of results to output reports.
		// Also, in order to be able to repeat the good transformation later in
		// final processing, it is necessary to cancel any result i.e. if a polynomial
		// is added to system of polynomials, it has to be removed, and if coordinates
		// are renamed, they have to be back to previous values.
		
		// turn the state of point P to UNCHANGED so it can be reset 
		// according to actions taken on it in this method
		manager.getPoint().setPointState(Point.POINT_STATE_UNCHANGED);
		manager.setCondition(this.getCondition());
		
		// instantiate the condition from this line using its specific condition
		Point P = manager.getPoint();
		Map<String, Point> pointsMap = new HashMap<String, Point>(); // collection with current elements
		pointsMap.put(M0Label, P);
		//centers must exist since validation will detect error otherwise
		// we will not swap centers since they are used symmetrically in condition
		pointsMap.put(O1Label, this.firstCircle.getCenter());
		pointsMap.put(O2Label, this.secondCircle.getCenter());
		
		/*
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
		 */
		
		// we don't swap circles since they are used symmetrically in condition;
		// also we don't swap radius end points since they are used symmetrically too
		if (this.firstCircle instanceof CircleWithCenterAndRadius) {
			// order of radius end points is not important since
			// these points are equivalently used in condition
			pointsMap.put(A1Label, ((CircleWithCenterAndRadius)this.firstCircle).getRadius().getFirstEndPoint());
			pointsMap.put(B1Label, ((CircleWithCenterAndRadius)this.firstCircle).getRadius().getSecondEndPoint());
			
			if (this.secondCircle instanceof CircleWithCenterAndRadius) {
				// order of radius end points is not important since
				// these points are equivalently used in condition
				pointsMap.put(A2Label, ((CircleWithCenterAndRadius)this.secondCircle).getRadius().getFirstEndPoint());
				pointsMap.put(B2Label, ((CircleWithCenterAndRadius)this.secondCircle).getRadius().getSecondEndPoint());
				
				manager.processPointsAndCondition(pointsMap);
				
				if (manager.isErrorFlag()) {
					logger.error("findBestPointsForInstantation() method failed in processing condition for radical axis");
					return OGPConstants.ERR_CODE_GENERAL;
				}
				
				// if polynomial that renames coordinates of point is found, stop further search
				if (manager.getPoint().getPointState() == Point.POINT_STATE_RENAMED)
					return OGPConstants.RET_CODE_SUCCESS;
			}
				
			// now check other way - circle with center and one point
			P.setPointState(Point.POINT_STATE_UNCHANGED);
			
			// use center as first radius point
			pointsMap.put(A2Label, this.secondCircle.getCenter());
				
			// pass all points to search for second point of radius
			for (int iB = 0, jB = this.secondCircle.points.size(); iB < jB; iB++) {
				Point pointB = this.secondCircle.points.get(iB);
					
				if (pointB.getIndex() >= P.getIndex()) // B must be constructed before P
					continue;
					
				pointsMap.put(B2Label, pointB);
					
				manager.processPointsAndCondition(pointsMap);
					
				if (manager.isErrorFlag()) {
					logger.error("findBestPointsForInstantation() method failed in processing condition for radical axis");
					return OGPConstants.ERR_CODE_GENERAL;
				}
				
				// if polynomial that renames coordinates of point is found, stop further search
				if (manager.getPoint().getPointState() == Point.POINT_STATE_RENAMED)
					return OGPConstants.RET_CODE_SUCCESS;
			}
		}
		
		// now check other way - circle with center and one point
		P.setPointState(Point.POINT_STATE_UNCHANGED);
		
		// use center as first radius point
		pointsMap.put(A1Label, this.firstCircle.getCenter());
			
		// pass all points to search for second point of radius
		for (int iB1 = 0, jB1 = this.firstCircle.points.size(); iB1 < jB1; iB1++) {
			Point pointB1 = this.firstCircle.points.get(iB1);
				
			if (pointB1.getIndex() >= P.getIndex()) // B must be constructed before P
				continue;
				
			pointsMap.put(B1Label, pointB1);
				
			if (this.secondCircle instanceof CircleWithCenterAndRadius) {
				// order of radius end points is not important since
				// these points are equivalently used in condition
				pointsMap.put(A2Label, ((CircleWithCenterAndRadius)this.secondCircle).getRadius().getFirstEndPoint());
				pointsMap.put(B2Label, ((CircleWithCenterAndRadius)this.secondCircle).getRadius().getSecondEndPoint());
					
				manager.processPointsAndCondition(pointsMap);
					
				if (manager.isErrorFlag()) {
					logger.error("findBestPointsForInstantation() method failed in processing condition for radical axis");
					return OGPConstants.ERR_CODE_GENERAL;
				}
					
				// if polynomial that renames coordinates of point is found, stop further search
				if (manager.getPoint().getPointState() == Point.POINT_STATE_RENAMED)
					return OGPConstants.RET_CODE_SUCCESS;
			}
				
			// now check other way - circle with center and one point
			P.setPointState(Point.POINT_STATE_UNCHANGED);
				
			// use center as first radius point
			pointsMap.put(A2Label, this.secondCircle.getCenter());
					
			// pass all points to search for second point of radius
			for (int iB2 = 0, jB2 = this.secondCircle.points.size(); iB2 < jB2; iB2++) {
				Point pointB2 = this.secondCircle.points.get(iB2);
						
				if (pointB2.getIndex() >= P.getIndex()) // B must be constructed before P
					continue;
						
				pointsMap.put(B2Label, pointB2);
						
				manager.processPointsAndCondition(pointsMap);
						
				if (manager.isErrorFlag()) {
					logger.error("findBestPointsForInstantation() method failed in processing condition for radical axis");
					return OGPConstants.ERR_CODE_GENERAL;
				}
						
				// if polynomial that renames coordinates of point is found, stop further search
				if (manager.getPoint().getPointState() == Point.POINT_STATE_RENAMED)
					return OGPConstants.RET_CODE_SUCCESS;
			}
		}

		return OGPConstants.RET_CODE_SUCCESS;
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.SetOfPoints#instantiateConditionFromBasicElements(com.ogprover.pp.tp.geoconstruction.Point)
	 */
	public XPolynomial instantiateConditionFromBasicElements(Point P) {
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		pointsMap.put(M0Label, P);
		
		pointsMap.put(O1Label, this.firstCircle.getCenter());
		if (this.firstCircle instanceof CircleWithCenterAndRadius) {
			pointsMap.put(A1Label, ((CircleWithCenterAndRadius)this.firstCircle).getRadius().getFirstEndPoint());
			pointsMap.put(B1Label, ((CircleWithCenterAndRadius)this.firstCircle).getRadius().getSecondEndPoint());
		}
		else {
			pointsMap.put(A1Label, this.firstCircle.getCenter());
			pointsMap.put(B1Label, this.firstCircle.getPoints().get(0));
		}
		
		pointsMap.put(O2Label, this.secondCircle.getCenter());
		if (this.secondCircle instanceof CircleWithCenterAndRadius) {
			pointsMap.put(A2Label, ((CircleWithCenterAndRadius)this.secondCircle).getRadius().getFirstEndPoint());
			pointsMap.put(B2Label, ((CircleWithCenterAndRadius)this.secondCircle).getRadius().getSecondEndPoint());
		}
		else {
			pointsMap.put(A2Label, this.secondCircle.getCenter());
			pointsMap.put(B2Label, this.secondCircle.getPoints().get(0));
		}
		
		//return this.instantiateCondition(pointsMap).reduceByUTermDivision();
		return this.instantiateCondition(pointsMap); // don't reduce polynomial
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.Line#getAllPossibleConditionsWithMappings()
	 */
	@Override
	public Map<SymbolicPolynomial, ArrayList<Map<String, Point>>> getAllPossibleConditionsWithMappings() {
		Map<SymbolicPolynomial, ArrayList<Map<String, Point>>> retMap = super.getAllPossibleConditionsWithMappings();
		
		ArrayList<Map<String, Point>> allMappings = new ArrayList<Map<String, Point>>();
		Map<String, Point> pointsMap = null;
		
		if (this.firstCircle instanceof CircleWithCenterAndRadius) {
			if (this.secondCircle instanceof CircleWithCenterAndRadius) {
				pointsMap = new HashMap<String, Point>();
				pointsMap.put(A1Label, ((CircleWithCenterAndRadius)this.firstCircle).getRadius().getFirstEndPoint());
				pointsMap.put(B1Label, ((CircleWithCenterAndRadius)this.firstCircle).getRadius().getSecondEndPoint());
				pointsMap.put(A2Label, ((CircleWithCenterAndRadius)this.secondCircle).getRadius().getFirstEndPoint());
				pointsMap.put(B2Label, ((CircleWithCenterAndRadius)this.secondCircle).getRadius().getSecondEndPoint());
				pointsMap.put(O1Label, this.firstCircle.getCenter());
				pointsMap.put(O2Label, this.secondCircle.getCenter());
				allMappings.add(pointsMap);
			}
			
			for (Point pointB2 : this.secondCircle.getPoints()) {
				pointsMap = new HashMap<String, Point>();
				pointsMap.put(A1Label, ((CircleWithCenterAndRadius)this.firstCircle).getRadius().getFirstEndPoint());
				pointsMap.put(B1Label, ((CircleWithCenterAndRadius)this.firstCircle).getRadius().getSecondEndPoint());
				pointsMap.put(A2Label, this.secondCircle.getCenter());
				pointsMap.put(B2Label, pointB2);
				pointsMap.put(O1Label, this.firstCircle.getCenter());
				pointsMap.put(O2Label, this.secondCircle.getCenter());
				allMappings.add(pointsMap);
			}
		}
		
		for (Point pointB1 : this.firstCircle.getPoints()) {
			if (this.secondCircle instanceof CircleWithCenterAndRadius) {
				pointsMap = new HashMap<String, Point>();
				pointsMap.put(A1Label, this.firstCircle.getCenter());
				pointsMap.put(B1Label, pointB1);
				pointsMap.put(A2Label, ((CircleWithCenterAndRadius)this.secondCircle).getRadius().getFirstEndPoint());
				pointsMap.put(B2Label, ((CircleWithCenterAndRadius)this.secondCircle).getRadius().getSecondEndPoint());
				pointsMap.put(O1Label, this.firstCircle.getCenter());
				pointsMap.put(O2Label, this.secondCircle.getCenter());
				allMappings.add(pointsMap);
			}
			
			for (Point pointB2 : this.secondCircle.getPoints()) {
				pointsMap = new HashMap<String, Point>();
				pointsMap.put(A1Label, this.firstCircle.getCenter());
				pointsMap.put(B1Label, pointB1);
				pointsMap.put(A2Label, this.secondCircle.getCenter());
				pointsMap.put(B2Label, pointB2);
				pointsMap.put(O1Label, this.firstCircle.getCenter());
				pointsMap.put(O2Label, this.secondCircle.getCenter());
				allMappings.add(pointsMap);
			}
		}
		
		retMap.put((SymbolicPolynomial) this.getCondition().clone(), allMappings);
		return retMap;
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getConstructionDesc()
	 */
	@Override
	public String getConstructionDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Radical axis ");
		sb.append(this.geoObjectLabel);
		sb.append(" of circles ");
		sb.append(this.firstCircle.getGeoObjectLabel());
		sb.append(" and ");
		sb.append(this.secondCircle.getGeoObjectLabel());
		return sb.toString();
	}
	
	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getInputLabels()
	 */
	@Override
	public String[] getInputLabels() {
		String[] inputLabels = new String[2];
		inputLabels[0] = this.firstCircle.getGeoObjectLabel();
		inputLabels[1] = this.secondCircle.getGeoObjectLabel();
		return inputLabels;
	}
}


