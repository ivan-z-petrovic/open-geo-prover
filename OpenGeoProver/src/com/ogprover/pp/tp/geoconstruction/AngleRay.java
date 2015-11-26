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
*     first ray and congruent angle</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class AngleRay extends Line {
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
	
	// This line is angle ray OB of angle <AOB which is congruent to angle <A1O1B1
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
	 * <i><b>Symbolic label for vertex of congruent angle</b></i>
	 */
	private static final String O1Label = "O1"; // 'O' letter
	/**
	 * <i><b>Symbolic label for point from first ray of congruent angle</b></i>
	 */
	private static final String A1Label = "A1";
	/**
	 * <i><b>Symbolic label for point from second ray of congruent angle</b></i>
	 */
	private static final String B1Label = "B1";
	/**
	 * Congruent angle
	 */
	private Angle congAngle;
	/**
	 * First ray point
	 */
	private Point firstRayPoint;
	/**
	 * <i>Symbolic polynomial representing the condition for angle ray</i>
	 */
	private static SymbolicPolynomial conditionForAngRay = null;
	
	// Static initializer of condition members 
	static {
		/* 
		 * The symbolic condition is <AOM0 is equal to <A1O1B1.
		 */
		if (conditionForAngRay == null) {
			conditionForAngRay = GeneralizedAngleTangent.getConditionForEqualsConvexAngles();
			conditionForAngRay = GeneralizedAngleTangent.substitutePointLabelsForTwoAngles(conditionForAngRay, A1Label, O1Label, B1Label, ALabel, OLabel, M0Label);
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
		return GeoConstruction.GEOCONS_TYPE_ANGLE_RAY;
	}
	
	/**
	 * @param angle the angle to set
	 */
	public void setCongAngle(Angle angle) {
		this.congAngle = angle;
	}

	/**
	 * @return the angle
	 */
	public Angle getCongAngle() {
		return congAngle;
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
	 * that some point belongs to angle ray.
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.Line#getCondition()
	 */
	public SymbolicPolynomial getCondition() {
		return conditionForAngRay;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param lineLabel				Label of this line
	 * @param vertex				Vertex of angle
	 * @param firstRayPoint			Point from first ray of angle
	 * @param vertexCongAng			Vertex of congruent angle
	 * @param firstRayPointCongAng	Point from first ray of congruent angle
	 * @param secondRayPointCongAng	Point from second ray of congruent angle
	 */
	public AngleRay(String lineLabel, Point firstRayPoint, Point vertex, 
					Point firstRayPointCongAng, Point vertexCongAng, Point secondRayPointCongAng) {
		this.geoObjectLabel = lineLabel;
		this.points = new Vector<Point>();
		if (vertex != null)
			this.points.add(vertex); // add at the end - vertex is the first point of this line
		this.congAngle = new Angle(firstRayPointCongAng, vertexCongAng, secondRayPointCongAng);
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
			// Angle ray is valid if all necessary points have been already constructed 
			Point vertex = this.points.get(0);
		
			// All points must exist
			if (vertex == null || this.firstRayPoint == null || this.congAngle == null ||
				this.congAngle.getFirstRayPoint() == null || this.congAngle.getVertex() == null || this.congAngle.getSecondRayPoint() == null) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Cannot construct angle ray " + this.getGeoObjectLabel() + " because not all necessary points have been constructed.");
				return false;
			}
		
			int indexO = vertex.getIndex();
			int indexA = this.firstRayPoint.getIndex();
			int indexO1 = this.congAngle.getVertex().getIndex();
			int indexA1 = this.congAngle.getFirstRayPoint().getIndex();
			int indexB1 = this.congAngle.getSecondRayPoint().getIndex();
		
			if (indexO < 0 || indexA < 0 || indexO1 < 0 || indexA1 < 0  || indexB1 < 0) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Cannot construct angle ray " + this.getGeoObjectLabel() + " because some of necessary points is not added to theorem protocol.");
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
		pointsMap.put(O1Label, this.congAngle.getVertex());
		pointsMap.put(A1Label, this.congAngle.getFirstRayPoint());
		pointsMap.put(B1Label, this.congAngle.getSecondRayPoint());
		
		manager.processPointsAndCondition(pointsMap);
		
		if (manager.isErrorFlag()) {
			logger.error("findBestPointsForInstantation() method failed in processing condition for angle ray");
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
		pointsMap.put(A1Label, this.congAngle.getFirstRayPoint());
		pointsMap.put(O1Label, this.congAngle.getVertex());
		pointsMap.put(B1Label, this.congAngle.getSecondRayPoint());
		
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
		pointsMap.put(A1Label, this.congAngle.getFirstRayPoint());
		pointsMap.put(O1Label, this.congAngle.getVertex());
		pointsMap.put(B1Label, this.congAngle.getSecondRayPoint());
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
		sb.append(" from first ray, which is congruent to angle ");
		sb.append(this.congAngle.getDescription());
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
		inputLabels[2] = this.congAngle.getFirstRayPoint().getGeoObjectLabel();
		inputLabels[3] = this.congAngle.getVertex().getGeoObjectLabel();
		inputLabels[4] = this.congAngle.getSecondRayPoint().getGeoObjectLabel();
		return inputLabels;
	}

	

}


