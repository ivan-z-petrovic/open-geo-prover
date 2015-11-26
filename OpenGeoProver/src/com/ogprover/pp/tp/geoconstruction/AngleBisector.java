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
import com.ogprover.pp.tp.geoobject.Angle;
import com.ogprover.utilities.io.OGPOutput;
import com.ogprover.utilities.logger.ILogger;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for construction of angle bisector of given angle</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class AngleBisector extends Line {
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
	 * Angle whose bisector is this line
	 */
	private Angle angle;
	/**
	 * <i>Symbolic polynomial representing the check condition for angle bisector
	 *    (if it is perpendicular to X-axis)</i>
	 */
	private static SymbolicPolynomial checkPerpCondition = null;
	/**
	 * <i>Symbolic polynomial representing the condition for angle bisector
	 *    perpendicular to X-axis</i>
	 */
	private static SymbolicPolynomial conditionForPerpAngBisector = null;
	
	// Static initializer of condition members 
	static {
		/* 
		 * Check condition for angle bisector:
		 *    it is important to check whether angle bisector is a line perpendicular
		 *    to X-axis. In that case its slope against X-axis is infinity but condition
		 *    for a point to belong to this bisector then becomes very simple - it only
		 *    has to have x-coordinate same as x-coordinate of the angle's vertex.
		 *    
		 *    The check condition for angle <AOB consists of following:
		 *    	if n is line perpendicular to X-axis through vertex O, then let A'
		 *    	be the point symmetric to point A in regards to line n. If points O, B
		 *    	and A' are collinear, that means that n is the angle bisector of 
		 *    	angle <AOB.
		 *    
		 *     If O=(xO, yO), A=(xA, yA) and B=(xB, yB) it is not difficult to show that
		 *     A'=(2xO-xA, yA) and therefore the condition of collinearity of points 
		 *     O, B and A' becomes:
		 *     
		 *     xOyB - xAyB -2xOyO + xAyO - xByA + xByO + xOyA = 0
		 */
		if (checkPerpCondition == null) {
			checkPerpCondition = new SymbolicPolynomial();
			
			// Instances of symbolic variables
			SymbolicVariable xO = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, OLabel);
			SymbolicVariable yO = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, OLabel);
			SymbolicVariable xA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, ALabel);
			SymbolicVariable yA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, ALabel);
			SymbolicVariable xB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, BLabel);
			SymbolicVariable yB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, BLabel);
			
			// term xO * yB
			Term t = new SymbolicTerm(1);
			t.addPower(new Power(xO, 1));
			t.addPower(new Power(yB, 1));
			checkPerpCondition.addTerm(t);
			
			// term - xA * yB
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xA, 1));
			t.addPower(new Power(yB, 1));
			checkPerpCondition.addTerm(t);
			
			// term -2 * xO *yO
			t = new SymbolicTerm(-2);
			t.addPower(new Power(xO, 1));
			t.addPower(new Power(yO, 1));
			checkPerpCondition.addTerm(t);
			
			// term xA * yO
			t = new SymbolicTerm(1);
			t.addPower(new Power(xA, 1));
			t.addPower(new Power(yO, 1));
			checkPerpCondition.addTerm(t);
			
			// term - xB * yA
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xB, 1));
			t.addPower(new Power(yA, 1));
			checkPerpCondition.addTerm(t);
			
			// term xB * yO
			t = new SymbolicTerm(1);
			t.addPower(new Power(xB, 1));
			t.addPower(new Power(yO, 1));
			checkPerpCondition.addTerm(t);
			
			// term xO * yA
			t = new SymbolicTerm(1);
			t.addPower(new Power(xO, 1));
			t.addPower(new Power(yA, 1));
			checkPerpCondition.addTerm(t);
		}
		
		/*
		 * Condition for angle bisector perpendicular to X-axis:
		 * 	Let M0=(x0, y0) be some point from bisector and O=(xO, yO)
		 * 	is the vertex of angle. Since bisector is perpendicular to
		 * 	X-axis, x-coordinates of these two points have to be same:
		 * 
		 * 	x0 - xO = 0
		 */
		if (conditionForPerpAngBisector == null) {
			conditionForPerpAngBisector = new SymbolicPolynomial();
			
			// Instances of symbolic variables
			SymbolicVariable xO = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, OLabel);
			SymbolicVariable x0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, M0Label);
			
			// term x0
			Term t = new SymbolicTerm(1);
			t.addPower(new Power(x0, 1));
			conditionForPerpAngBisector.addTerm(t);
			
			// term - xO
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xO, 1));
			conditionForPerpAngBisector.addTerm(t);
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
		return GeoConstruction.GEOCONS_TYPE_ANGLE_BISECTOR;
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
	 * that some point belongs to angle bisector.
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.Line#getCondition()
	 */
	public SymbolicPolynomial getCondition() {
		// First of all check whether angle bisector is perpendicular to X-axis
		// with current instances of angle points
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		pointsMap.put(OLabel, this.angle.getVertex());
		pointsMap.put(ALabel, this.angle.getFirstRayPoint());
		pointsMap.put(BLabel, this.angle.getSecondRayPoint());
		XPolynomial checkPerpBisector = OGPTP.instantiateCondition(checkPerpCondition, pointsMap).reduceByUTermDivision();
		
		boolean isPerpendicular = this.consProtocol.isPolynomialConsequenceOfConstructions(checkPerpBisector);
		
		if (isPerpendicular) {
			return conditionForPerpAngBisector; // simple condition that will force renaming of coordinates
		}
		
		// Here bisector is not perpendicular to X-axis;
		// we calculate tg<AOM0 and tg<M0OB and here we must respect
		// the order of line's slopes (in regards to X-axis):
		// if slope k1 of AO is less then slope k2 of line BO then slope
		// k0 of line M0O is between these two: k1 <= k0 <= k2.
		// This is because line M0O is between lines AO and BO as angle
		// bisector of angle <AOB.
		// Also, if k1 >= k2 => k1 >= k0 >= k2.
		// To respect the order of lines' slopes is the same as respecting the
		// angles' orientation: both angles must be of same orientation:
		// e.g. angles <AOM0 and <M0OB are of same orientation as well as
		// angles <M0OA and <BOM0, while angles <AOM0 and <BOM0 and angles
		// <M0OA and <M0OB are of different orientations. If angles have
		// different orientations their generalized tangents are of different signs but
		// with same absolute values (since they are halves of same angle <AOB).
		SymbolicPolynomial conditionAngBisector = GeneralizedAngleTangent.getConditionForEqualsConvexAngles();
		
		// return condition with substituted correct symbolic labels 
		return GeneralizedAngleTangent.substitutePointLabelsForTwoAngles(conditionAngBisector, ALabel, OLabel, M0Label, M0Label, OLabel, BLabel);
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
	public AngleBisector(String lineLabel, Point firstRayPoint, Point vertex, Point secondRayPoint) {
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
				output.closeItemWithDesc("Cannot construct angle bisector " + this.getGeoObjectLabel() + " because its vertex or point from some ray are not constructed");
				return false;
			}
		
			indexV = vertex.getIndex();
			index1 = this.angle.getFirstRayPoint().getIndex();
			index2 = this.angle.getSecondRayPoint().getIndex();
		
			if (indexV < 0 || index1 < 0 || index2 < 0) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Cannot construct angle bisector " + this.getGeoObjectLabel() + " because its vertex or point from some ray are not added to theorem protocol");
				return false; // some object is not in theorem protocol
			}
		
			if (indexV >= this.index || index1 >= this.index || index2 >= this.index) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Cannot construct angle bisector " + this.getGeoObjectLabel() + " because its vertex or point from some ray are not yet constructed");
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
			logger.error("findBestPointsForInstantation() method failed in processing condition for angle bisector");
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
		sb.append("Angle bisector ");
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


