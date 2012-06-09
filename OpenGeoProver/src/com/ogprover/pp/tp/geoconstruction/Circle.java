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
import com.ogprover.polynomials.Power;
import com.ogprover.polynomials.SymbolicPolynomial;
import com.ogprover.polynomials.SymbolicTerm;
import com.ogprover.polynomials.SymbolicVariable;
import com.ogprover.polynomials.Term;
import com.ogprover.polynomials.Variable;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.pp.tp.OGPTP;
import com.ogprover.pp.tp.auxiliary.GeneralizedAngleTangent;
import com.ogprover.pp.tp.auxiliary.PointSetRelationshipManager;
import com.ogprover.utilities.logger.ILogger;



/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Abstract class for circle</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public abstract class Circle extends GeoConstruction implements SetOfPoints {
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
	 * <i><b>Symbolic label for generic point from circle</b></i>
	 */
	protected static final String M0Label = "0";
	/**
	 * <i><b>
	 * Symbolic label for first point of circle or 
	 * for first point of circle's radius or
	 * for first point of circle's diameter
	 * </b></i>
	 */
	protected static final String ALabel = "A";
	/**
	 * <i><b>
	 * Symbolic label for second point of circle or 
	 * for second point of circle's radius or
	 * for second point of circle's diameter
	 * </b></i>
	 */
	protected static final String BLabel = "B";
	/**
	 * <i><b>Symbolic label for third point of circle</b></i>
	 */
	protected static final String CLabel = "C";
	/**
	 * <i><b>Symbolic label for center of circle</b></i>
	 */
	protected static final String OLabel = "O";
	/**
	 * List of all constructed points that belong to this circle
	 */
	protected Vector<Point> points = new Vector<Point>();
	/**
	 * Center of circle
	 */
	protected Point center = null;
	/**
	 * <i>
	 * Symbolic polynomial representing the condition for point 
	 * that belongs to this circle as plain circle with center
	 * and one point</i>
	 */
	public static SymbolicPolynomial conditionForCircleWithCenterAndPoint = null;
	/**
	 * <i>
	 * Symbolic polynomial representing the condition for point 
	 * that belongs to this circle as circle with center and radius</i>
	 */
	public static SymbolicPolynomial conditionForCircleWithCenterAndRadius = null;
	/**
	 * <i>
	 * Symbolic polynomial representing the condition for point 
	 * that belongs to this circle as circle with diameter</i>
	 */
	public static SymbolicPolynomial conditionForCircleWithDiameter = null;
	/**
	 * <i>
	 * Symbolic polynomial representing the condition for point 
	 * that belongs to this circle as circle through three points
	 * </i>
	 */
	public static SymbolicPolynomial conditionForCircumscribedCircle = null;
	
	// Static initializer of condition members 
	static {
		/*
		 * M0=(x0, y0) is some point from circle; O=(xO, yO) is center of circle and
		 * A=(xA, yA) is given point from circle. These points satisfy following equation:
		 * 	M0O = AO or in algebraic form:
		 * 
		 *  (x0 - xO)^2 + (y0 - yO)^2 = (xA - xO)^2 + (yA - yO)^2
		 *  x0^2 - 2x0xO + y0^2 - 2y0yO - xA^2 + 2xAxO - yA^2 + 2yAyO = 0
		 */
		if (conditionForCircleWithCenterAndPoint == null) {
			conditionForCircleWithCenterAndPoint = new SymbolicPolynomial();
			
			// Instances of symbolic variables
			SymbolicVariable x0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, M0Label);
			SymbolicVariable y0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, M0Label);
			SymbolicVariable xO = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, OLabel);
			SymbolicVariable yO = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, OLabel);
			SymbolicVariable xA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, ALabel);
			SymbolicVariable yA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, ALabel);
			
			// term x0^2
			Term t = new SymbolicTerm(1);
			t.addPower(new Power(x0, 2));
			conditionForCircleWithCenterAndPoint.addTerm(t);
			
			// term -2 * x0 * xO
			t = new SymbolicTerm(-2);
			t.addPower(new Power(x0, 1));
			t.addPower(new Power(xO, 1));
			conditionForCircleWithCenterAndPoint.addTerm(t);
			
			// term y0^2
			t = new SymbolicTerm(1);
			t.addPower(new Power(y0, 2));
			conditionForCircleWithCenterAndPoint.addTerm(t);
			
			// term -2 * y0 * yO
			t = new SymbolicTerm(-2);
			t.addPower(new Power(y0, 1));
			t.addPower(new Power(yO, 1));
			conditionForCircleWithCenterAndPoint.addTerm(t);
			
			// term -xA^2
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xA, 2));
			conditionForCircleWithCenterAndPoint.addTerm(t);
			
			// term 2 * xA * xO
			t = new SymbolicTerm(2);
			t.addPower(new Power(xA, 1));
			t.addPower(new Power(xO, 1));
			conditionForCircleWithCenterAndPoint.addTerm(t);
			
			// term -yA^2
			t = new SymbolicTerm(-1);
			t.addPower(new Power(yA, 2));
			conditionForCircleWithCenterAndPoint.addTerm(t);
			
			// term 2 * yA * yO
			t = new SymbolicTerm(2);
			t.addPower(new Power(yA, 1));
			t.addPower(new Power(yO, 1));
			conditionForCircleWithCenterAndPoint.addTerm(t);
		}
		
		/*
		 * M0=(x0, y0) is some point from circle; O=(xO, yO) is center of circle and
		 * A=(xA, yA) and B=(xB, yB) are given end points of segment congruent to circle's 
		 * radius. These points satisfy following equation:
		 * 	M0O = AB or in algebraic form:
		 * 
		 *  (x0 - xO)^2 + (y0 - yO)^2 = (xA - xB)^2 + (yA - yB)^2
		 *  x0^2 - 2x0xO + xO^2 + y0^2 - 2y0yO + yO^2 - xA^2 + 2xAxB - xB^2 - yA^2 + 2yAyB - yB^2 = 0
		 */
		if (conditionForCircleWithCenterAndRadius == null) {
			conditionForCircleWithCenterAndRadius = new SymbolicPolynomial();
			
			// Instances of symbolic variables
			SymbolicVariable x0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, M0Label);
			SymbolicVariable y0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, M0Label);
			SymbolicVariable xO = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, OLabel);
			SymbolicVariable yO = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, OLabel);
			SymbolicVariable xA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, ALabel);
			SymbolicVariable yA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, ALabel);
			SymbolicVariable xB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, BLabel);
			SymbolicVariable yB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, BLabel);
			
			// term x0^2
			Term t = new SymbolicTerm(1);
			t.addPower(new Power(x0, 2));
			conditionForCircleWithCenterAndRadius.addTerm(t);
			
			// term -2 * x0 * xO
			t = new SymbolicTerm(-2);
			t.addPower(new Power(x0, 1));
			t.addPower(new Power(xO, 1));
			conditionForCircleWithCenterAndRadius.addTerm(t);
			
			// term xO^2
			t = new SymbolicTerm(1);
			t.addPower(new Power(xO, 2));
			conditionForCircleWithCenterAndRadius.addTerm(t);
			
			// term y0^2
			t = new SymbolicTerm(1);
			t.addPower(new Power(y0, 2));
			conditionForCircleWithCenterAndRadius.addTerm(t);
			
			// term -2 * y0 * yO
			t = new SymbolicTerm(-2);
			t.addPower(new Power(y0, 1));
			t.addPower(new Power(yO, 1));
			conditionForCircleWithCenterAndRadius.addTerm(t);
			
			// term yO^2
			t = new SymbolicTerm(1);
			t.addPower(new Power(yO, 2));
			conditionForCircleWithCenterAndRadius.addTerm(t);
			
			// term -xA^2
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xA, 2));
			conditionForCircleWithCenterAndRadius.addTerm(t);
			
			// term 2 * xA * xB
			t = new SymbolicTerm(2);
			t.addPower(new Power(xA, 1));
			t.addPower(new Power(xB, 1));
			conditionForCircleWithCenterAndRadius.addTerm(t);
			
			// term -xB^2
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xB, 2));
			conditionForCircleWithCenterAndRadius.addTerm(t);
			
			// term -yA^2
			t = new SymbolicTerm(-1);
			t.addPower(new Power(yA, 2));
			conditionForCircleWithCenterAndRadius.addTerm(t);
			
			// term 2 * yA * yB
			t = new SymbolicTerm(2);
			t.addPower(new Power(yA, 1));
			t.addPower(new Power(yB, 1));
			conditionForCircleWithCenterAndRadius.addTerm(t);
			
			// term -yB^2
			t = new SymbolicTerm(-1);
			t.addPower(new Power(yB, 2));
			conditionForCircleWithCenterAndRadius.addTerm(t);
		}
		
		/*
		 * M0=(x0, y0) is some point from circle; AB is diameter of circle where
		 * A=(xA, yA) and B=(xB, yB). These points satisfy following equation:
		 * 	M0A is perpendicular to M0B or in algebraic form:
		 *  vector(AM0)*vector(BM0) = 0
		 *  (x0-xA, y0-yA)*(x0-xB, y0-yB)=0
		 *  
		 *  (x0-xA)(x0-xB) + (y0-yA)(y0-yB) = 0
		 *  x0^2 - xA*x0 - xB*x0 + xA*xB + y0^2 - yA*y0 - yB*y0 + yA*yB = 0
		 */
		if (conditionForCircleWithDiameter == null) {
			conditionForCircleWithDiameter = new SymbolicPolynomial();
			
			// Instances of symbolic variables
			SymbolicVariable x0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, M0Label);
			SymbolicVariable y0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, M0Label);
			SymbolicVariable xA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, ALabel);
			SymbolicVariable yA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, ALabel);
			SymbolicVariable xB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, BLabel);
			SymbolicVariable yB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, BLabel);
			
			// term x0^2
			Term t = new SymbolicTerm(1);
			t.addPower(new Power(x0, 2));
			conditionForCircleWithDiameter.addTerm(t);
			
			// term -xA * xO
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xA, 1));
			t.addPower(new Power(x0, 1));
			conditionForCircleWithDiameter.addTerm(t);
			
			// term -xB * xO
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xB, 1));
			t.addPower(new Power(x0, 1));
			conditionForCircleWithDiameter.addTerm(t);
			
			// term xA * xB
			t = new SymbolicTerm(1);
			t.addPower(new Power(xA, 1));
			t.addPower(new Power(xB, 1));
			conditionForCircleWithDiameter.addTerm(t);
			
			// term y0^2
			t = new SymbolicTerm(1);
			t.addPower(new Power(y0, 2));
			conditionForCircleWithDiameter.addTerm(t);
			
			// term -yA * yO
			t = new SymbolicTerm(-1);
			t.addPower(new Power(yA, 1));
			t.addPower(new Power(y0, 1));
			conditionForCircleWithDiameter.addTerm(t);
			
			// term -yB * yO
			t = new SymbolicTerm(-1);
			t.addPower(new Power(yB, 1));
			t.addPower(new Power(y0, 1));
			conditionForCircleWithDiameter.addTerm(t);
			
			// term yA * yB
			t = new SymbolicTerm(1);
			t.addPower(new Power(yA, 1));
			t.addPower(new Power(yB, 1));
			conditionForCircleWithDiameter.addTerm(t);
		}
		
		/*
		 * If A, B and C are three non-collinear points, then they determine the unique circle.
		 * Point M0 belongs to that circle iff angles <ACB and <AM0B are equals or supplementary
		 * angles as two inscribed angles of same circle that subtend the same chord AB of
		 * that circle. If C and M0 are on same circle arc AB then <ACB and <AM0B are angles of
		 * same orientation hence their generalized tangents are equals. If C and M0 are on 
		 * different arcs of circle then <ACB and <AM0B are with opposite orientations and
		 * although they are supplementary angles (as plain geometric angles) they are with 
		 * same generalized tangents (their signs and absolute values are same). Therefore,
		 * the condition for a point M0 to belong to this circle is tg<ACB - tg<AM0B = 0.
		 */
		if (conditionForCircumscribedCircle == null) {
			conditionForCircumscribedCircle = GeneralizedAngleTangent.getConditionForEqualsConvexAngles();
			conditionForCircumscribedCircle = GeneralizedAngleTangent.substitutePointLabelsForTwoAngles(conditionForCircumscribedCircle, ALabel, CLabel, BLabel, ALabel, M0Label, BLabel);
		}
	}
	
	
	
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
	 * Method that sets the list of all points belonging to this circle
	 * 
	 * @param points The points to set
	 */
	public void setPoints(Vector<Point> points) {
		this.points = points;
	}

	/**
	 * Method that retrieves the list of all points belonging to this circle
	 * 
	 * @return the points
	 */
	public Vector<Point> getPoints() {
		return points;
	}
	
	/**
	 * Method that sets the center of circle
	 * 
	 * @param center The center of circle to set
	 */
	public void setCenter(Point center) {
		this.center = center;
	}

	/**
	 * Method that retrieves the center of this circle
	 * 
	 * @return the center
	 */
	public Point getCenter() {
		return center;
	}
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method that retrieves the instance of condition for points of this circle
	 * expressed as x-polynomial (after symbolic coordinates of all points
	 * have been substituted by UX variables)
	 *  
	 * @param pointsMap		Map of points assigned to labels of common points from
	 * 						symbolic polynomial representing the condition,
	 * 						used for instantiation of that condition
	 * @return				X-polynomial representing the condition for points of this 
	 * 						circle in algebraic form or null in case of error
	 */
	public XPolynomial instantiateCondition(Map<String, Point> pointsMap) {
		return OGPTP.instantiateCondition(this.getCondition(), pointsMap);
	}
	
	/**
	 * Finding best points for instantiation.
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
		
		Map<String, Point> pointsMap = new HashMap<String, Point>(); // collection with current elements
		Point P = manager.getPoint();
		
		// If circle has a center and if it is constructed before point from 
		// manager, try first to instantiate a condition considering this circle 
		// as circle with center and radius or circle with center and one point.
		// Center of circle need not be created in the moment of creation of circle
		// object (e.g. CircumscribedCircle), but it would be possible to construct
		// it later, so it is important to be sure not to use it for points constructed
		// before it.
		if (this.center != null && this.center.getIndex() < P.getIndex()) {
			pointsMap.put(M0Label, P);
			pointsMap.put(OLabel, this.center);
			
			// try first with circle with center and radius
			if (this instanceof CircleWithCenterAndRadius) { 
				// any point from this type of circle is constructed
				// after center and radius end points - validation of
				// constructions from CP provides this constraint
				CircleWithCenterAndRadius crCircle = (CircleWithCenterAndRadius)this;
				
				manager.setCondition(Circle.conditionForCircleWithCenterAndRadius);
				
				// No need to swap points A and B here since symbolic polynomial
				// is symmetric by coordinates of these points so same instance 
				// will be obtained after swapping labels
				pointsMap.put(ALabel, crCircle.getRadius().getFirstEndPoint());
				pointsMap.put(BLabel, crCircle.getRadius().getSecondEndPoint());
				
				manager.processPointsAndCondition(pointsMap);
				
				if (manager.isErrorFlag()) {
					logger.error("Failed in processing specific condition for circle with given center and radius.");
					return OGPConstants.ERR_CODE_GENERAL;
				}
				
				// if polynomial that renames coordinates of point is found, stop further search
				if (manager.getPoint().getPointState() == Point.POINT_STATE_RENAMED)
					return OGPConstants.RET_CODE_SUCCESS;
				
				// turn the state of point P to UNCHANGED so it can be reset 
				// according to actions taken on it in this method
				manager.getPoint().setPointState(Point.POINT_STATE_UNCHANGED);
			}
			
			// now consider this circle as circle with center and point on it
			manager.setCondition(Circle.conditionForCircleWithCenterAndPoint);
		
			// Pass all points of this circle to search for a point from circle
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
			
			for (int iA = 0, jA = this.points.size(); iA < jA; iA++) {
				Point pointA = this.points.get(iA);
				
				// choose as given point one of those constructed before P
				if (pointA.getIndex() >= P.getIndex()) // A constructed after P or is P - skip it
					continue;
				
				// put chosen points in map with current elements for instantiation
				pointsMap.put(ALabel, pointA.clone());
				
				manager.processPointsAndCondition(pointsMap);
				
				if (manager.isErrorFlag()) {
					logger.error("Failed in processing specific condition for circle with given center and one its point.");
					return OGPConstants.ERR_CODE_GENERAL;
				}
				
				// if polynomial that renames coordinates of point is found, stop further search
				if (manager.getPoint().getPointState() == Point.POINT_STATE_RENAMED)
					return OGPConstants.RET_CODE_SUCCESS;
			}
		}
		
		// turn the state of point P to UNCHANGED so it can be reset 
		// according to actions taken on it in this method
		manager.getPoint().setPointState(Point.POINT_STATE_UNCHANGED);
		manager.setCondition(Circle.conditionForCircumscribedCircle);
		
		// instantiate the condition from this circle using its specific condition for circumscribed circle
		P = manager.getPoint();
		pointsMap.put(M0Label, P);
		
		// For circumscribed circle's condition, not all symbolic points are equivalent,
		// therefore it is important to swap them all correctly - we need three different
		// points constructed before point from manager.
		
		// Pass all points of this circle to search for first point from circle
		for (int iA = 0, jA = this.points.size(); iA < jA; iA++) {
			Point pointA = this.points.get(iA);
			// choose as given point one of those constructed before P
			if (pointA.getIndex() >= P.getIndex()) // A constructed after P or is P - skip it
				continue;
			
			// Pass all points of this circle to search for second point from circle
			for (int iB = 0, jB = this.points.size(); iB < jB; iB++) {
				Point pointB = this.points.get(iB);
				// choose as given point one of those constructed before P and not A
				if (pointB.getIndex() >= P.getIndex() || pointB.getIndex() == pointA.getIndex()) // B constructed after P or is P or is A - skip it
					continue;
				
				// Pass all points of this circle to search for third point from circle
				for (int iC = 0, jC = this.points.size(); iC < jC; iC++) {
					Point pointC = this.points.get(iC);
					// choose as given point one of those constructed before P and not A and B
					if (pointC.getIndex() >= P.getIndex() || pointC.getIndex() == pointA.getIndex() || pointC.getIndex() == pointB.getIndex()) // C constructed after P or is P or is A/B - skip it
						continue;
					
					pointsMap.put(ALabel, pointA);
					pointsMap.put(BLabel, pointB);
					pointsMap.put(CLabel, pointC);
		
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
		
		/*
		 * Finally check if circle is special case of circle with given diameter
		 */
		// instantiate the condition from this circle using its specific condition for circle with diameter
		P = manager.getPoint();
		// turn the state of point P to UNCHANGED so it can be reset 
		// according to actions taken on it in this method
		P.setPointState(Point.POINT_STATE_UNCHANGED);
		pointsMap.put(M0Label, P);
		
		if (this instanceof CircleWithDiameter) { 
			// any point from this type of circle is constructed
			// after diameter end points - validation of
			// constructions from CP provides this constraint
			CircleWithDiameter dCircle = (CircleWithDiameter)this;
			
			manager.setCondition(Circle.conditionForCircleWithDiameter);
			
			// No need to swap points A and B here since symbolic polynomial
			// is symmetric by coordinates of these points so same instance 
			// will be obtained after swapping labels
			pointsMap.put(ALabel, dCircle.getDiameter().getFirstEndPoint());
			pointsMap.put(BLabel, dCircle.getDiameter().getSecondEndPoint());
			
			manager.processPointsAndCondition(pointsMap);
			
			if (manager.isErrorFlag()) {
				logger.error("Failed in processing specific condition for circle with given diameter.");
				return OGPConstants.ERR_CODE_GENERAL;
			}
			
			// if polynomial that renames coordinates of point is found, stop further search
			if (manager.getPoint().getPointState() == Point.POINT_STATE_RENAMED)
				return OGPConstants.RET_CODE_SUCCESS;
			
			// turn the state of point P to UNCHANGED so it can be reset 
			// according to actions taken on it in this method
			manager.getPoint().setPointState(Point.POINT_STATE_UNCHANGED);
		}
		
		return OGPConstants.RET_CODE_SUCCESS;
	}
	
	/**
	 * @see com.ogprover.pp.tp.geoconstruction.SetOfPoints#instantiateConditionFromBasicElements(com.ogprover.pp.tp.geoconstruction.Point)
	 */
	public XPolynomial instantiateConditionFromBasicElements(Point P) {
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		pointsMap.put(M0Label, P);
		
		if (this.getConstructionType() == GeoConstruction.GEOCONS_TYPE_CIRCLE_WITH_CENTER_AND_POINT) {
			pointsMap.put(OLabel, this.center);
			pointsMap.put(ALabel, this.points.get(0));
		}
		else if (this.getConstructionType() == GeoConstruction.GEOCONS_TYPE_CIRCLE_WITH_CENTER_AND_RADIUS) {
			pointsMap.put(OLabel, this.center);
			pointsMap.put(ALabel, ((CircleWithCenterAndRadius)this).getRadius().getFirstEndPoint());
			pointsMap.put(BLabel, ((CircleWithCenterAndRadius)this).getRadius().getSecondEndPoint());
		}
		else if (this.getConstructionType() == GeoConstruction.GEOCONS_TYPE_CIRCLE_WITH_DIAMETER) {
			pointsMap.put(ALabel, ((CircleWithDiameter)this).getDiameter().getFirstEndPoint());
			pointsMap.put(BLabel, ((CircleWithDiameter)this).getDiameter().getSecondEndPoint());
		}
		else if (this.getConstructionType() == GeoConstruction.GEOCONS_TYPE_CIRCUMSCRIBED_CIRCLE) {
			pointsMap.put(ALabel, this.points.get(0));
			pointsMap.put(BLabel, this.points.get(1));
			pointsMap.put(CLabel, this.points.get(2));
		}
		
		//return this.instantiateCondition(pointsMap).reduceByUTermDivision();
		return this.instantiateCondition(pointsMap); // don't reduce polynomial
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
		ArrayList<Map<String, Point>> allMappings = null;
		Map<String, Point> pointsMap = null;
		
		if (this instanceof CircleWithCenterAndRadius) {
			allMappings = new ArrayList<Map<String, Point>>();
			pointsMap = new HashMap<String, Point>();
			pointsMap.put(OLabel, this.center); // in this case it must exist
			// no need to swap points A and B since they are equivalently used
			// in symbolic polynomial for condition
			pointsMap.put(ALabel, ((CircleWithCenterAndRadius)this).getRadius().getFirstEndPoint());
			pointsMap.put(BLabel, ((CircleWithCenterAndRadius)this).getRadius().getSecondEndPoint());
			allMappings.add(pointsMap);
			retMap.put((SymbolicPolynomial) Circle.conditionForCircleWithCenterAndRadius.clone(), allMappings);
		}
		
		// for other type of circles this circle has to have points constructed on itself
		if (this.points.size() > 0) {
			// first of all check if there is center
			if (this.center != null) {
				allMappings = new ArrayList<Map<String, Point>>();
				
				for (Point A : this.points) {
					pointsMap = new HashMap<String, Point>();
					pointsMap.put(OLabel, this.center);
					pointsMap.put(ALabel, A);
					allMappings.add(pointsMap);
				}
				
				retMap.put((SymbolicPolynomial) Circle.conditionForCircleWithCenterAndPoint.clone(), allMappings);
			}
			
			// now check if there are at least three points on it
			if (this.points.size() >= 3) {
				allMappings = new ArrayList<Map<String, Point>>();
				
				// points A, B and C are not used equivalently in symbolic condition, 
				// therefore we search for all variations of three different points
				for (Point A : this.points) {
					for (Point B : this.points) {
						if (B.getGeoObjectLabel().equals(A.getGeoObjectLabel()))
							continue;
						
						for (Point C : this.points) {
							if (C.getGeoObjectLabel().equals(A.getGeoObjectLabel()) ||
								C.getGeoObjectLabel().equals(B.getGeoObjectLabel()))
								continue;
							
							pointsMap = new HashMap<String, Point>();
							pointsMap.put(ALabel, A);
							pointsMap.put(BLabel, B);
							pointsMap.put(CLabel, C);
							allMappings.add(pointsMap);
						}
					}
				}
				
				retMap.put((SymbolicPolynomial) Circle.conditionForCircumscribedCircle.clone(), allMappings);
			}
			
			// check if circle is with given diameter
			if (this instanceof CircleWithDiameter) {
				allMappings = new ArrayList<Map<String, Point>>();
				pointsMap = new HashMap<String, Point>();
				// no need to swap points A and B since they are equivalently used
				// in symbolic polynomial for condition
				pointsMap.put(ALabel, ((CircleWithDiameter)this).getDiameter().getFirstEndPoint());
				pointsMap.put(BLabel, ((CircleWithDiameter)this).getDiameter().getSecondEndPoint());
				allMappings.add(pointsMap);
				retMap.put((SymbolicPolynomial) Circle.conditionForCircleWithDiameter.clone(), allMappings);
			}
		}
		
		return retMap;
	}
	
	
}
