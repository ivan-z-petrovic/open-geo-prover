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
import com.ogprover.pp.tp.auxiliary.GeneralizedAngleTangent;
import com.ogprover.pp.tp.auxiliary.PointSetRelationshipManager;
import com.ogprover.pp.tp.geoobject.Angle;
import com.ogprover.utilities.io.OGPOutput;
import com.ogprover.utilities.logger.ILogger;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for construction of second angle ray for given 
*     first ray and three times smaller angle</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class TripleAngleRay extends Line {
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
	
	// This line is angle ray OB of angle <AOB which is three times greater than angle <A1O1B1
	/**
	 * <i><b>Symbolic label for generic point from this line</b></i>
	 */
	private static final String M0Label = "0"; // zero
	/**
	 * <i><b>Symbolic label for given angle vertex</b></i>
	 */
	private static final String OLabel = "O"; // 'O' letter
	/**
	 * <i><b>Symbolic label for given point from first ray of angle</b></i>
	 */
	private static final String ALabel = "A";
	/**
	 * <i><b>Symbolic label for vertex of given smaller angle</b></i>
	 */
	private static final String O1Label = "O1"; // 'O' letter
	/**
	 * <i><b>Symbolic label for point from first ray of given smaller angle</b></i>
	 */
	private static final String A1Label = "A1";
	/**
	 * <i><b>Symbolic label for point from second ray of given smaller angle</b></i>
	 */
	private static final String B1Label = "B1";
	/**
	 * Smaller angle
	 */
	private Angle smallAngle;
	/**
	 * First ray point
	 */
	private Point firstRayPoint;
	/**
	 * <i>Symbolic polynomial representing the condition for triple angle ray</i>
	 */
	private static SymbolicPolynomial conditionForTripleAngRay = null;
	
	// Static initializer of condition members 
	static {
		/* 
		 * The symbolic condition is <AOM0 is equal to <A1O1B1.
		 */
		if (conditionForTripleAngRay == null) {
			conditionForTripleAngRay = GeneralizedAngleTangent.getConditionForTripleAngle();
			conditionForTripleAngRay = GeneralizedAngleTangent.substitutePointLabelsForTwoAngles(conditionForTripleAngRay, ALabel, OLabel, M0Label, A1Label, O1Label, B1Label);
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
		return GeoConstruction.GEOCONS_TYPE_TRIPLE_ANGLE_RAY;
	}
	
	/**
	 * @param angle the angle to set
	 */
	public void setSmallAngle(Angle angle) {
		this.smallAngle = angle;
	}

	/**
	 * @return the angle
	 */
	public Angle getSmallAngle() {
		return smallAngle;
	}
	
	/**
	 * @param firstRayPoint the firstRayPoint to set
	 */
	public void setFirstRayPoint(Point firstRayPoint) {
		this.firstRayPoint = firstRayPoint;
	}

	/**
	 * @return the firstRayPoint
	 */
	public Point getFirstRayPoint() {
		return firstRayPoint;
	}

	/**
	 * Method that retrieves symbolic polynomial that represents the condition
	 * that some point belongs to triple angle ray.
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.Line#getCondition()
	 */
	public SymbolicPolynomial getCondition() {
		return conditionForTripleAngRay;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param lineLabel					Label of this line
	 * @param vertex					Vertex of angle
	 * @param firstRayPoint				Point from first ray of angle
	 * @param vertexSmallAng			Vertex of three times smaller angle
	 * @param firstRayPointSmallAng		Point from first ray of three times smaller angle
	 * @param secondRayPointSmallAng	Point from second ray of three times smaller angle
	 */
	public TripleAngleRay(String lineLabel, Point firstRayPoint, Point vertex, 
					Point firstRayPointSmallAng, Point vertexSmallAng, Point secondRayPointSmallAng) {
		this.geoObjectLabel = lineLabel;
		this.points = new Vector<Point>();
		if (vertex != null)
			this.points.add(vertex); // add at the end - vertex is the first point of this line
		this.smallAngle = new Angle(firstRayPointSmallAng, vertexSmallAng, secondRayPointSmallAng);
		this.firstRayPoint = firstRayPoint;
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
			// Triple angle ray is valid if all necessary points have been already constructed 
			Point vertex = this.points.get(0);
		
			// All points must exist
			if (vertex == null || this.firstRayPoint == null || this.smallAngle == null ||
				this.smallAngle.getFirstRayPoint() == null || this.smallAngle.getVertex() == null || this.smallAngle.getSecondRayPoint() == null) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Cannot construct triple angle ray " + this.getGeoObjectLabel() + " because not all necessary points have been constructed.");
				return false;
			}
		
			int indexO = vertex.getIndex();
			int indexA = this.firstRayPoint.getIndex();
			int indexO1 = this.smallAngle.getVertex().getIndex();
			int indexA1 = this.smallAngle.getFirstRayPoint().getIndex();
			int indexB1 = this.smallAngle.getSecondRayPoint().getIndex();
		
			if (indexO < 0 || indexA < 0 || indexO1 < 0 || indexA1 < 0  || indexB1 < 0) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Cannot construct triple angle ray " + this.getGeoObjectLabel() + " because some of necessary points is not added to theorem protocol.");
				return false; // some object is not in theorem protocol
			}
		
			if (indexO >= this.index || indexA >= this.index || indexO1 >= this.index || indexA1 >= this.index || indexB1 >= this.index) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Cannot construct angle ray " + this.getGeoObjectLabel() + " because some of necessary points is not yet constructed.");
				return false; // some object is not constructed before this line
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
		// Note: order of points in angle cannot be changed because it would be different angle
		// i.e here A is always point from first ray and B from second - they cannot be swapped
		Point P = manager.getPoint();
		Map<String, Point> pointsMap = new HashMap<String, Point>(); // collection with current elements
		pointsMap.put(M0Label, P);
		pointsMap.put(OLabel, this.points.get(0));
		pointsMap.put(ALabel, this.firstRayPoint);
		pointsMap.put(O1Label, this.smallAngle.getVertex());
		pointsMap.put(A1Label, this.smallAngle.getFirstRayPoint());
		pointsMap.put(B1Label, this.smallAngle.getSecondRayPoint());
		
		manager.processPointsAndCondition(pointsMap);
		
		if (manager.isErrorFlag()) {
			logger.error("findBestPointsForInstantation() method failed in processing condition for triple angle ray");
			return OGPConstants.ERR_CODE_GENERAL;
		}

		return OGPConstants.RET_CODE_SUCCESS;
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.SetOfPoints#instantiateConditionFromBasicElements(com.ogprover.pp.tp.geoconstruction.Point)
	 */
	public XPolynomial instantiateConditionFromBasicElements(Point P) {
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		pointsMap.put(M0Label, P);
		pointsMap.put(ALabel, this.firstRayPoint);
		pointsMap.put(OLabel, this.points.get(0));
		pointsMap.put(A1Label, this.smallAngle.getFirstRayPoint());
		pointsMap.put(O1Label, this.smallAngle.getVertex());
		pointsMap.put(B1Label, this.smallAngle.getSecondRayPoint());
		
		return this.instantiateCondition(pointsMap); // don't reduce polynomial
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.Line#getAllPossibleConditionsWithMappings()
	 */
	@Override
	public Map<SymbolicPolynomial, ArrayList<Map<String, Point>>> getAllPossibleConditionsWithMappings() {
		Map<SymbolicPolynomial, ArrayList<Map<String, Point>>> retMap = super.getAllPossibleConditionsWithMappings();
		
		ArrayList<Map<String, Point>> allMappings = new ArrayList<Map<String, Point>>();
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		// there is only one possibility since we cannot change the order of
		// points from angle rays because that will give different oriented angle
		pointsMap.put(ALabel, this.firstRayPoint);
		pointsMap.put(OLabel, this.points.get(0));
		pointsMap.put(A1Label, this.smallAngle.getFirstRayPoint());
		pointsMap.put(O1Label, this.smallAngle.getVertex());
		pointsMap.put(B1Label, this.smallAngle.getSecondRayPoint());
		allMappings.add(pointsMap);
		
		retMap.put((SymbolicPolynomial) this.getCondition().clone(), allMappings);
		return retMap;
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getConstructionDesc()
	 */
	@Override
	public String getConstructionDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Angle ray ");
		sb.append(this.geoObjectLabel);
		sb.append(" of angle with vertex ");
		sb.append(this.points.get(0).getGeoObjectLabel());
		sb.append(" and point ");
		sb.append(this.firstRayPoint.getGeoObjectLabel());
		sb.append(" from first ray, which is three times greater than angle ");
		sb.append(this.smallAngle.getDescription());
		return sb.toString();
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getInputLabels()
	 */
	@Override
	public String[] getInputLabels() {
		String[] inputLabels = new String[5];
		inputLabels[0] = this.firstRayPoint.getGeoObjectLabel();
		inputLabels[1] = this.points.get(0).getGeoObjectLabel();
		inputLabels[2] = this.smallAngle.getFirstRayPoint().getGeoObjectLabel();
		inputLabels[3] = this.smallAngle.getVertex().getGeoObjectLabel();
		inputLabels[4] = this.smallAngle.getSecondRayPoint().getGeoObjectLabel();
		return inputLabels;
	}

	

}


