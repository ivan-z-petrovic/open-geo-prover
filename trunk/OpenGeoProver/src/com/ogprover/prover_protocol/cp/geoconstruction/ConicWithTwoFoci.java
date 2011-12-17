/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.prover_protocol.cp.geoconstruction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.ogprover.main.OGPConstants;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.SymbolicPolynomial;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.auxiliary.PointSetRelationshipManager;
import com.ogprover.prover_protocol.cp.auxiliary.Segment;
import com.ogprover.utilities.io.FileLogger;
import com.ogprover.utilities.io.OGPOutput;



/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for conic with two foci (ellipse or hyperbola)</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class ConicWithTwoFoci extends ConicSection {
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
	 * <i><b>Symbolic label for generic point from conic</b></i>
	 */
	private static final String M0Label = "0";
	/**
	 * <i><b>Symbolic label for some given point of conic</b></i>
	 */
	private static final String ALabel = "A";
	/**
	 * <i><b>Symbolic label for first focus</b></i>
	 */
	private static final String F1Label = "1";
	/**
	 * <i><b>Symbolic label for second focus</b></i>
	 */
	private static final String F2Label = "2";
	/**
	 * List of all constructed points that belong to this conic
	 */
	protected Vector<Point> points = new Vector<Point>();
	/**
	 * First focus of conic
	 */
	protected Point firstFocus = null;
	/**
	 * Second focus of conic
	 */
	protected Point secondFocus = null;
	/**
	 * <i>
	 * Symbolic polynomial representing the condition for point 
	 * that belongs to this conic
	 * </i>
	 */
	public static SymbolicPolynomial conditionForConicWithTwoFoci = null;
	
	// Static initializer of condition members 
	static {
		/*
		 * Let M0 = (x0, y0) be some point from conic whose given elements are:
		 * A = (xA, yA) (point from conic)
		 * F1 = (x1, y1) (first focus)
		 * F2 = (x2, y2) (second focus).
		 * 
		 * Geometry property of ellipse is that sum of distances from any 
		 * of its point to foci is constant value.
		 * Therefore M0F1 + M0F2 = AF1 + AF2
		 * 
		 * Geometry property of hyperbola is that absolute value of difference 
		 * of distances from any of its point to foci is constant value.
		 * Therefore |M0F1 - M0F2| = |AF1 - AF2|
		 * 
		 * These have to be squared until they get free of square roots that are used
		 * to calculate the distance between two points.
		 * 
		 * After first square of above equations we get:
		 *    (+/-)2(M0F1*M0F2 - AF1*AF2) = AF1^2 + AF2^2 - M0F1^2 - M0F2^2
		 *    
		 * After squaring of above equation we get:
		 *    -8*M0F1*M0F2*AF1*AF2 = AF1^4 + AF2^4 + M0F1^4 + M0F2^4 - 
		 *                           -2*(AF1^2*AF2^2 + AF1^2*M0F1^2 + AF1^2*M0F2^2 + 
		 *                               AF2^2*M0F1^2 + AF2^2*M0F2^2 + M0F1^2*M0F2^2)
		 *                              
		 * Right side of above equation can be calculated by usage of square of distances,
		 * and let mark it as f. Therefore the equation becomes:
		 *    -8*M0F1*M0F2*AF1*AF2 = f
		 *    
		 * After third squaring it becomes:
		 *    64*M0F1^2*M0F2^2*AF1^2*AF2^2 - f^2 = 0
		 *    
		 * This will represent the condition for point M0 to belong to conic
		 * with given elements (A, F1, F2).
		 * 
		 * Note that this equation has to be a quadric equation by coordinates
		 * of point M0. Although it looks like it's going to be of 8th degree by
		 * these coordinates, many higher degrees will be reduces. However, this
		 * equation will not be quadric by coordinates of M0 
		 * (see manual test MTestCP.testConicConditions()) but rather the equation
		 * of fifth degree by coordinates of M0. Explanation is following:
		 * 		To distinguish between ellipse and hyperbola we must know whether
		 * apexes are between foci or not.
		 * 		E.g. if foci of this conic is F1(c, 0) and F2(-c, 0) and one apex
		 * from x-axis is A(a, 0) we obtain the canonical equation of conic section with two
		 * foci:
		 * 		x^2/a^2 + y^2/(a^2 - c^2) = 1
		 * If a < c (i.e. apexes are between foci) we have hyperbola, and when a > c (apexes
		 * are out of segment F1F2) we get ellipse; (a=c is degenerative case where
		 * conic is part of line F1F2 - segment F1F2 or two rays not containing the segment).
		 * Since algebraic form of construction uses only polynomials we cannot distinguish
		 * between ellipse and hyperbola by usage only of polynomials. Therefore, the
		 * equation of fifth degree is union of ellipse, hyperbola and their degenerative
		 * case - line F1F2 - it is 2+2+1 = 5th degree in total.  
		 */
		if (conditionForConicWithTwoFoci == null) {
			// squares of distances
			SymbolicPolynomial sdAF1 = Segment.substitutePointLabelsForSquareOfDistance((SymbolicPolynomial) Segment.getConditionForSquareOfDistance().clone(), ALabel, F1Label);
			SymbolicPolynomial sdAF2 = Segment.substitutePointLabelsForSquareOfDistance((SymbolicPolynomial) Segment.getConditionForSquareOfDistance().clone(), ALabel, F2Label);
			SymbolicPolynomial sdM0F1 = Segment.substitutePointLabelsForSquareOfDistance((SymbolicPolynomial) Segment.getConditionForSquareOfDistance().clone(), M0Label, F1Label);
			SymbolicPolynomial sdM0F2 = Segment.substitutePointLabelsForSquareOfDistance((SymbolicPolynomial) Segment.getConditionForSquareOfDistance().clone(), M0Label, F2Label);
			
			conditionForConicWithTwoFoci = (SymbolicPolynomial) sdAF1.clone();
			conditionForConicWithTwoFoci.multiplyByPolynomial(sdAF2);
			conditionForConicWithTwoFoci.multiplyByPolynomial(sdM0F1);
			conditionForConicWithTwoFoci.multiplyByPolynomial(sdM0F2);
			conditionForConicWithTwoFoci.multiplyByRealConstant(64);
			
			SymbolicPolynomial f = (SymbolicPolynomial) sdAF1.clone().multiplyByPolynomial(sdAF1);
			f.addPolynomial((SymbolicPolynomial) sdAF2.clone().multiplyByPolynomial(sdAF2));
			f.addPolynomial((SymbolicPolynomial) sdM0F1.clone().multiplyByPolynomial(sdM0F1));
			f.addPolynomial((SymbolicPolynomial) sdM0F2.clone().multiplyByPolynomial(sdM0F2));
			
			SymbolicPolynomial g = (SymbolicPolynomial) sdAF1.clone().multiplyByPolynomial(sdAF2);
			g.addPolynomial((SymbolicPolynomial) sdAF1.clone().multiplyByPolynomial(sdM0F1));
			g.addPolynomial((SymbolicPolynomial) sdAF1.clone().multiplyByPolynomial(sdM0F2));
			g.addPolynomial((SymbolicPolynomial) sdAF2.clone().multiplyByPolynomial(sdM0F1));
			g.addPolynomial((SymbolicPolynomial) sdAF2.clone().multiplyByPolynomial(sdM0F2));
			g.addPolynomial((SymbolicPolynomial) sdM0F1.clone().multiplyByPolynomial(sdM0F2));
			g.multiplyByRealConstant(-2);
			f.addPolynomial(g);
			
			conditionForConicWithTwoFoci.subtractPolynomial(f.clone().multiplyByPolynomial(f));
			
			/*
			 * IMPORTANT NOTE:
			 * 
			 *    This condition includes not only points from conic but other points from plane that
			 *    satisfy it. This is because we used squaring of equations to get free of square roots.
			 *    But condition like this is mostly useless for theorem proving since it doesn't
			 *    show the relationship among points from conic section but among points from wider
			 *    set of points. That is why some theorems can't be proved by using this condition. 
			 */
		}
	}
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method that sets the list of all points belonging to this conic
	 * 
	 * @param points The points to set
	 */
	public void setPoints(Vector<Point> points) {
		this.points = points;
	}

	/**
	 * Method that retrieves the list of all points belonging to this conic
	 * 
	 * @return the points
	 */
	public Vector<Point> getPoints() {
		return points;
	}
	
	/**
	 * Method that sets the first focus of conic
	 * 
	 * @param firstFocus The first focus of conic to set
	 */
	public void setFirstFocus(Point firstFocus) {
		this.firstFocus = firstFocus;
	}

	/**
	 * Method that retrieves the first focus of this conic
	 * 
	 * @return the first focus
	 */
	public Point getFirstFocus() {
		return firstFocus;
	}
	
	/**
	 * Method that sets the second focus of conic
	 * 
	 * @param secondFocus The second focus of conic to set
	 */
	public void setSecondFocus(Point secondFocus) {
		this.secondFocus = secondFocus;
	}

	/**
	 * Method that retrieves the second focus of this conic
	 * 
	 * @return the second focus
	 */
	public Point getSecondFocus() {
		return secondFocus;
	}
	
	/**
	 * Method that retrieves the condition for some point
	 * to belong to this conic
	 * 
	 * @return The condition
	 */
	public SymbolicPolynomial getCondition() {
		return conditionForConicWithTwoFoci;
	}
	
	/**
	 * Method that gives the type of this construction
	 * 
	 * @see com.ogprover.prover_protocol.cp.geoconstruction.GeoConstruction#getConstructionType()
	 */
	@Override
	public int getConstructionType() {
		return GeoConstruction.GEOCONS_TYPE_CONIC_WITH_TWO_FOCI;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	public ConicWithTwoFoci(String conicLabel, Point pointA, Point firstFocus, Point secondFocus) {
		this.geoObjectLabel = conicLabel;
		this.points = new Vector<Point>();
		if (pointA != null)
			this.points.add(pointA); // add at the end - first point of conic
		this.firstFocus = firstFocus;
		this.secondFocus = secondFocus;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method that retrieves the instance of condition for points of this conic
	 * expressed as x-polynomial (after symbolic coordinates of all points
	 * have been substituted by UX variables)
	 *  
	 * @param pointsMap		Map of points assigned to labels of common points from
	 * 						symbolic polynomial representing the condition,
	 * 						used for instantiation of that condition
	 * @return				X-polynomial representing the condition for points of this 
	 * 						conic in algebraic form or null in case of error
	 */
	public XPolynomial instantiateCondition(Map<String, Point> pointsMap) { 
		return OGPCP.instantiateCondition(this.getCondition(), pointsMap);
	}
	
	/**
	 * Method to check the validity of this construction step
	 * 
	 * @see com.ogprover.prover_protocol.cp.geoconstruction.GeoConstruction#isValidConstructionStep()
	 */
	@Override
	public boolean isValidConstructionStep() {
		OGPOutput output = OpenGeoProver.settings.getOutput();
		FileLogger logger = OpenGeoProver.settings.getLogger();
		
		if (!super.isValidConstructionStep())
			return false;
		
		try {
			// Construction of conic is valid if both its foci and given point are previously constructed
			Point pointA = this.points.get(0);
			Point firstFocus = this.firstFocus;
			Point secondFocus = this.secondFocus;
			int indexA, indexF1, indexF2;
		
			if (pointA == null || firstFocus == null || secondFocus == null) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Conic with two foci " + this.getGeoObjectLabel() + " can't be constructed because some of necessary points is not constructed");
				return false;
			}
		
			indexA = pointA.getIndex();
			indexF1 = firstFocus.getIndex();
			indexF2 = secondFocus.getIndex();
		
			if (indexA < 0 || indexF1 < 0 || indexF2 < 0) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Conic with two foci " + this.getGeoObjectLabel() + " can't be constructed because some of necessary points is not added to construction protocol");
				return false; // some point is not in construction protocol
			}
		
			boolean valid = this.index > indexA && this.index > indexF1 && this.index > indexF2;
			
			if (!valid) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Conic with two foci " + this.getGeoObjectLabel() + " can't be constructed because some of necessary points is not yet constructed");
			}
			
			return valid;
		} catch (IOException e) {
			logger.error("Failed to write to output file(s).");
			output.close();
			return false;
		}
	}
	
	/**
	 * Finding best points for instantiation.
	 * 
	 * @see com.ogprover.prover_protocol.cp.geoconstruction.SetOfPoints#findBestPointsForInstantation(com.ogprover.prover_protocol.cp.auxiliary.PointSetRelationshipManager)
	 */
	public int findBestPointsForInstantation(PointSetRelationshipManager manager) {
		FileLogger logger = OpenGeoProver.settings.getLogger();
		
		// First call method from superclass - it implements default behavior considering
		// this conic as conic section through five points
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
		manager.setCondition(ConicWithTwoFoci.conditionForConicWithTwoFoci);
		
		// instantiate the condition from this conic using its specific condition
		Point P = manager.getPoint();
		Map<String, Point> pointsMap = new HashMap<String, Point>(); // collection with current elements
		pointsMap.put(M0Label, P);
		// It is not necessary to swap foci points F1 and F2 because they are equivalent in symbolic condition
		// polynomial for this locus (and there is symmetry in that polynomial by coordinates of these points).
		pointsMap.put(F1Label, this.firstFocus);
		pointsMap.put(F2Label, this.secondFocus);
		
		// Pass all points of this conic to search for one point from this conic
		for (int iA = 0, jA = this.points.size(); iA < jA; iA++) {
			Point pointA = this.points.get(iA);
			// choose as given point one of those constructed before P
			if (pointA.getIndex() >= P.getIndex()) // A constructed after P or is P - skip it
				continue;
					
			pointsMap.put(ALabel, pointA);
		
			manager.processPointsAndCondition(pointsMap);
		
			if (manager.isErrorFlag()) {
				logger.error("Failed in processing specific polynomial for conic with two foci and one point on it.");
				return OGPConstants.ERR_CODE_GENERAL;
			}
					
			// if polynomial that renames coordinates of point is found, stop further search
			if (manager.getPoint().getPointState() == Point.POINT_STATE_RENAMED)
				return OGPConstants.RET_CODE_SUCCESS;
		}
		
		return OGPConstants.RET_CODE_SUCCESS;
	}
	
	/**
	 * @see com.ogprover.prover_protocol.cp.geoconstruction.SetOfPoints#instantiateConditionFromBasicElements(com.ogprover.prover_protocol.cp.geoconstruction.Point)
	 */
	@Override
	public XPolynomial instantiateConditionFromBasicElements(Point P) {
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		pointsMap.put(M0Label, P);
		pointsMap.put(ALabel, this.points.get(0));
		pointsMap.put(F1Label, this.firstFocus);
		pointsMap.put(F2Label, this.secondFocus);
		
		//return this.instantiateCondition(pointsMap).reduceByUTermDivision();
		return this.instantiateCondition(pointsMap); // don't reduce polynomial
	}
	
	/**
	 * @see com.ogprover.prover_protocol.cp.geoconstruction.SetOfPoints#addPointToSet(com.ogprover.prover_protocol.cp.geoconstruction.Point)
	 */
	public void addPointToSet(Point P) {
		this.points.add(P);
	}

	/**
	 * @see com.ogprover.prover_protocol.cp.geoconstruction.ConicSection#getAllPossibleConditionsWithMappings()
	 */
	@Override
	public Map<SymbolicPolynomial, ArrayList<Map<String, Point>>> getAllPossibleConditionsWithMappings() {
		Map<SymbolicPolynomial, ArrayList<Map<String, Point>>> retMap = super.getAllPossibleConditionsWithMappings();
		
		ArrayList<Map<String, Point>> allMappings = new ArrayList<Map<String, Point>>();
		Map<String, Point> pointsMap = null;
		
		for (Point A : this.points) {
			pointsMap = new HashMap<String, Point>();
			// F1 and F2 are equivalent points in condition for this conic
			// (they are used symmetrically) so they will not be swapped
			pointsMap.put(F1Label, this.firstFocus);
			pointsMap.put(F2Label, this.secondFocus);
			pointsMap.put(ALabel, A);
			allMappings.add(pointsMap);
		}
		
		retMap.put((SymbolicPolynomial) ConicWithTwoFoci.conditionForConicWithTwoFoci.clone(), allMappings);
		return retMap;
	}

	/**
	 * @see com.ogprover.prover_protocol.cp.geoconstruction.GeoConstruction#getConstructionDesc()
	 */
	@Override
	public String getConstructionDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Conic  ");
		sb.append(this.geoObjectLabel);
		sb.append(" with two foci ");
		sb.append(this.firstFocus.getGeoObjectLabel());
		sb.append(" and ");
		sb.append(this.secondFocus.getGeoObjectLabel());
		return sb.toString();
	}
	
	
}
