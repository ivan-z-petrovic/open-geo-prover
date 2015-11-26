/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.pp.tp.geoconstruction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.ogprover.main.OGPConstants;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.SymbolicPolynomial;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.auxiliary.PointSetRelationshipManager;
import com.ogprover.utilities.logger.ILogger;



/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Abstract class for line</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public abstract class Line extends GeoConstruction implements SetOfPoints {
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
	 * <i><b>Symbolic label for generic point from line</b></i>
	 */
	private static final String M0Label = "0";
	/**
	 * <i><b>Symbolic label for first point that determines the line</b></i>
	 */
	private static final String ALabel = "A";
	/**
	 * <i><b>Symbolic label for second point that determines the line</b></i>
	 */
	private static final String BLabel = "B";
	/**
	 * List of all constructed points that belong to this line
	 */
	protected Vector<Point> points = new Vector<Point>();
	
	
	
	/*
	 * ======================================================================
	 * ========================= ABSTRACT METHODS ===========================
	 * ======================================================================
	 */
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method that sets the list of all points belonging to this line
	 * 
	 * @param points The points to set
	 */
	public void setPoints(Vector<Point> points) {
		this.points = points;
	}

	/**
	 * Method that retrieves the list of all points belonging to this line
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
	 * Method that retrieves the instance of condition for points of this line
	 * expressed as x-polynomial (after symbolic coordinates of all points
	 * have been substituted by UX variables)
	 *  
	 * @param pointsMap		Map of points assigned to labels of common points from
	 * 						symbolic polynomial representing the condition,
	 * 						used for instantiation of that condition
	 * @return				X-polynomial representing the condition for points of this 
	 * 						line in algebraic form or null in case of error
	 */
	public XPolynomial instantiateCondition(Map<String, Point> pointsMap) { 
		return OGPTP.instantiateCondition(this.getCondition(), pointsMap);
	}
	
	/**
	 * Finding best points for instantiation. In base class this method will
	 * implement the default behavior considering this line as plain line through
	 * two points.
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.SetOfPoints#findBestPointsForInstantation(com.ogprover.pp.tp.auxiliary.PointSetRelationshipManager)
	 */
	public int findBestPointsForInstantation(PointSetRelationshipManager manager) {
		ILogger logger = OpenGeoProver.settings.getLogger();
		
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
		
		manager.setCondition(LineThroughTwoPoints.conditionForPlainLine); // will attempt to find best elements 
		                                                       // considering this line as plain line
		
		Map<String, Point> pointsMap = new HashMap<String, Point>(); // collection with current elements
		Point P = manager.getPoint();
		pointsMap.put(M0Label, P);
		
		// Pass all points of this line to search for first point
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
		
		// Points A and B are not equivalent in symbolic polynomial for condition for this line;
		// therefore we have to provide all variations of these points
		for (int iA = 0, jA = this.points.size(); iA < jA; iA++) {
			Point pointA = this.points.get(iA);
			// choose as first point one of those constructed before P
			if (pointA.getIndex() >= P.getIndex()) // A constructed after P or is P - skip it
				continue;
			
			// now pass points again to search for the second point
			for (int iB = 0, jB = this.points.size(); iB < jB; iB++) {
				Point pointB = this.points.get(iB);
				// skip point if constructed after P or is P or if it is A 
				if (pointB.getIndex() >= P.getIndex() || pointB.getIndex() == pointA.getIndex())
					continue;
				
				// put chosen points in map with current elements for instantiation
				pointsMap.put(ALabel, pointA);
				pointsMap.put(BLabel, pointB);
				
				manager.processPointsAndCondition(pointsMap);
				
				if (manager.isErrorFlag()) {
					logger.error("Failed in processing specific condition for plain line i.e. line through two points.");
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
	 * @see com.ogprover.pp.tp.geoconstruction.SetOfPoints#addPointToSet(com.ogprover.pp.tp.geoconstruction.Point)
	 */
	public void addPointToSet(Point P) {
		this.points.add(P);
	}
	
	/**
	 * @see com.ogprover.pp.tp.geoconstruction.SetOfPoints#getAllPossibleConditionsWithMappings()
	 */
	public Map<SymbolicPolynomial, ArrayList<Map<String, Point>>> getAllPossibleConditionsWithMappings() {
		Map<SymbolicPolynomial, ArrayList<Map<String, Point>>> retMap = new HashMap<SymbolicPolynomial, ArrayList<Map<String, Point>>>();
		
		if (this.points.size() < 2) {
			// there's no enough points to consider this line 
			// as plain line with two points
			return retMap; // return empty map rather then null since it will be passed down to 
			               // derived classes to populate it with their polynomials/conditions
		}
		
		// prepare all mappings for line with two points
		ArrayList<Map<String, Point>> allMappings = new ArrayList<Map<String, Point>>();
		Map<String, Point> pointsMap = null;
		
		// we iterate through collection of points and make difference between
		// point A and point B since they are not equivalently used in symbolic
		// polynomial for condition
		for (Point A : this.points) {
			for (Point B : this.points) {
				if (B.getGeoObjectLabel().equals(A.getGeoObjectLabel()))
					continue;
				
				pointsMap = new HashMap<String, Point>();
				pointsMap.put(ALabel, A);
				pointsMap.put(BLabel, B);
				allMappings.add(pointsMap);
			}
		}
		
		retMap.put((SymbolicPolynomial) LineThroughTwoPoints.conditionForPlainLine.clone(), allMappings);
		return retMap;
	}
}
