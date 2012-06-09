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
* <dd>Class for construction of angle trisector of given angle</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class AngleTrisector extends Line {
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
	
	// This line is angle bisector of angle <AOB
	/**
	 * <i><b>Symbolic label for generic point from this line</b></i>
	 */
	private static final String M0Label = "0"; // zero
	/**
	 * <i><b>Symbolic label for angle vertex</b></i>
	 */
	private static final String OLabel = "O"; // 'O' letter
	/**
	 * <i><b>Symbolic label for point from first ray of angle</b></i>
	 */
	private static final String ALabel = "A";
	/**
	 * <i><b>Symbolic label for point from second ray of angle</b></i>
	 */
	private static final String BLabel = "B";
	/**
	 * Angle whose trisector is this line
	 */
	private Angle angle;
	
	
	
	
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
		return GeoConstruction.GEOCONS_TYPE_ANGLE_TRISECTOR;
	}
	
	/**
	 * @param angle the angle to set
	 */
	public void setAngle(Angle angle) {
		this.angle = angle;
	}

	/**
	 * @return the angle
	 */
	public Angle getAngle() {
		return angle;
	}
	
	/**
	 * Method that retrieves symbolic polynomial that represents the condition
	 * that some point belongs to angle trisector.
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.Line#getCondition()
	 */
	public SymbolicPolynomial getCondition() {
		return GeneralizedAngleTangent.substitutePointLabelsForTwoAngles(GeneralizedAngleTangent.getConditionForTripleAngle(), ALabel, OLabel, BLabel, ALabel, OLabel, M0Label);
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
	 * @param vertex			Vertex of angle
	 * @param firstRayPoint		Point from first ray of angle
	 * @param secondRayPoint	Point from second ray of angle
	 */
	public AngleTrisector(String lineLabel, Point firstRayPoint, Point vertex, Point secondRayPoint) {
		this.geoObjectLabel = lineLabel;
		this.points = new Vector<Point>();
		if (vertex != null)
			this.points.add(vertex); // add at the end - vertex is the first point of this line
		this.angle = new Angle(firstRayPoint, vertex, secondRayPoint);
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
			// Angle bisector is valid if vertex and points from
			// both rays have been already constructed 
			Point vertex = this.points.get(0);
			int indexV, index1, index2;
		
			// Angle points must exist
			if (vertex == null || this.angle.getFirstRayPoint() == null || this.angle.getSecondRayPoint() == null) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Cannot construct angle trisector " + this.getGeoObjectLabel() + " because its vertex or point from some ray are not constructed");
				return false;
			}
		
			indexV = vertex.getIndex();
			index1 = this.angle.getFirstRayPoint().getIndex();
			index2 = this.angle.getSecondRayPoint().getIndex();
		
			if (indexV < 0 || index1 < 0 || index2 < 0) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Cannot construct angle trisector " + this.getGeoObjectLabel() + " because its vertex or point from some ray are not added to theorem protocol");
				return false; // some object is not in theorem protocol
			}
		
			if (indexV >= this.index || index1 >= this.index || index2 >= this.index) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Cannot construct angle trisector " + this.getGeoObjectLabel() + " because its vertex or point from some ray are not yet constructed");
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
		Point P = manager.getPoint();
		Map<String, Point> pointsMap = new HashMap<String, Point>(); // collection with current elements
		pointsMap.put(M0Label, P);
		pointsMap.put(OLabel, this.angle.getVertex());
		// order of points cannot be changed because it would be different angle
		// i.e A here is always point from first ray and B from second - cannot be swapped
		pointsMap.put(ALabel, this.angle.getFirstRayPoint());
		pointsMap.put(BLabel, this.angle.getSecondRayPoint());
		
		manager.processPointsAndCondition(pointsMap);
		
		if (manager.isErrorFlag()) {
			logger.error("findBestPointsForInstantation() method failed in processing condition for angle trisector");
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
		pointsMap.put(ALabel, this.angle.getFirstRayPoint());
		pointsMap.put(OLabel, this.angle.getVertex());
		pointsMap.put(BLabel, this.angle.getSecondRayPoint());
		
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
		// points A and B because that will give different oriented angle
		pointsMap.put(OLabel, this.angle.getVertex());
		pointsMap.put(ALabel, this.angle.getFirstRayPoint());
		pointsMap.put(BLabel, this.angle.getSecondRayPoint());
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
		sb.append("Angle trisector ");
		sb.append(this.geoObjectLabel);
		sb.append(" of angle ");
		sb.append(this.angle.getDescription());
		return sb.toString();
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getInputLabels()
	 */
	@Override
	public String[] getInputLabels() {
		String[] inputLabels = new String[3];
		inputLabels[0] = this.angle.getFirstRayPoint().getGeoObjectLabel();
		inputLabels[1] = this.angle.getVertex().getGeoObjectLabel();
		inputLabels[2] = this.angle.getSecondRayPoint().getGeoObjectLabel();
		return inputLabels;
	}

}


