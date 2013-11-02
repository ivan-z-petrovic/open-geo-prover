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
import com.ogprover.pp.tp.auxiliary.PointSetRelationshipManager;
import com.ogprover.pp.tp.geoobject.Segment;
import com.ogprover.utilities.io.OGPOutput;
import com.ogprover.utilities.logger.ILogger;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for construction of perpendicular bisector
*     of given segment</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class PerpendicularBisector extends Line {
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
	 * <i>Symbolic polynomial representing the condition for point that belongs to this line</i>
	 */
	public static SymbolicPolynomial conditionForPerpendicularBisector = null;
	// This line is perpendicular bisector of given segment AB
	/**
	 * <i><b>Symbolic label for generic point from this line</b></i>
	 */
	private static final String M0Label = "0";
	/**
	 * <i><b>Symbolic label for first end point of segment</b></i>
	 */
	private static final String ALabel = "A";
	/**
	 * <i><b>Symbolic label for second end point of segment</b></i>
	 */
	private static final String BLabel = "B";
	/**
	 * Segment whose perpendicular bisector is this line
	 */
	private Segment segment;
	
	
	// Static initializer of condition member 
	static {
		/* 
		 * M0 = (x0, y0) is the point on line for which the condition is calculated;
		 * A = (xA, yA) and B = (xB, yB) are two end points of given segment;
		 * 
		 * Condition is:
		 *    M0 is equidistant from A and B (segments M0A and M0B are equals)
		 *    M0A = M0B
		 *    M0A^2 = M0B^2
		 *    (x0 - xA)^2 + (y0 - yA)^2 = (x0 - xB)^2 + (y0 - yB)^2
		 *    xA^2 - xB^2 + yA^2 - yB^2 - 2x0xA + 2x0xB - 2y0yA + 2y0yB = 0
		 */
		if (conditionForPerpendicularBisector == null) {
			conditionForPerpendicularBisector = new SymbolicPolynomial();
			
			// Instances of symbolic variables
			SymbolicVariable x0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, M0Label);
			SymbolicVariable y0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, M0Label);
			SymbolicVariable xA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, ALabel);
			SymbolicVariable yA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, ALabel);
			SymbolicVariable xB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, BLabel);
			SymbolicVariable yB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, BLabel);
			
			// term xA^2
			Term t = new SymbolicTerm(1);
			t.addPower(new Power(xA, 2));
			conditionForPerpendicularBisector.addTerm(t);
			
			// term -xB^2
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xB, 2));
			conditionForPerpendicularBisector.addTerm(t);
			
			// term yA^2
			t = new SymbolicTerm(1);
			t.addPower(new Power(yA, 2));
			conditionForPerpendicularBisector.addTerm(t);
			
			// term -yB^2
			t = new SymbolicTerm(-1);
			t.addPower(new Power(yB, 2));
			conditionForPerpendicularBisector.addTerm(t);
			
			// term -2x0xA
			t = new SymbolicTerm(-2);
			t.addPower(new Power(x0, 1));
			t.addPower(new Power(xA, 1));
			conditionForPerpendicularBisector.addTerm(t);
			
			// term 2x0xB
			t = new SymbolicTerm(2);
			t.addPower(new Power(x0, 1));
			t.addPower(new Power(xB, 1));
			conditionForPerpendicularBisector.addTerm(t);
			
			// term -2y0yA
			t = new SymbolicTerm(-2);
			t.addPower(new Power(y0, 1));
			t.addPower(new Power(yA, 1));
			conditionForPerpendicularBisector.addTerm(t);
			
			// term 2y0yB
			t = new SymbolicTerm(2);
			t.addPower(new Power(y0, 1));
			t.addPower(new Power(yB, 1));
			conditionForPerpendicularBisector.addTerm(t);
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
		return GeoConstruction.GEOCONS_TYPE_PERPENDICULAR_BISECTOR;
	}
	
	/**
	 * Method to set the segment
	 * 
	 * @param seg The segment to set
	 */
	public void setSegment(Segment seg) {
		this.segment = seg;
	}

	/**
	 * Method that retrieves the segment
	 * 
	 * @return The segment
	 */
	public Segment getSegment() {
		return segment;
	}

	/**
	 * Method that retrieves the condition for a point that belongs to this line
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.Line#getCondition()
	 */
	public SymbolicPolynomial getCondition() {
		return conditionForPerpendicularBisector;
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param lineLabel		Label of this line
	 * @param pointA		First end point of segment
	 * @param pointB		Second end point of segment
	 */
	public PerpendicularBisector(String lineLabel, Point pointA, Point pointB) {
		this.geoObjectLabel = lineLabel;
		this.points = new Vector<Point>();
		if (pointA != null && pointB != null)
			this.segment = new Segment(pointA, pointB);
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
			// Perpendicular bisector is valid if both segment's end points
			// have been already constructed 
			int indexA, indexB;
		
			// Segment's end points must exist
			if (this.segment == null || this.segment.getFirstEndPoint() == null || this.segment.getSecondEndPoint() == null) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Cannot construct perpendicular bisector " + this.getGeoObjectLabel() + " because some of segment's end points is not constructed");
				return false;
			}
		
			indexA = this.segment.getFirstEndPoint().getIndex();
			indexB = this.segment.getSecondEndPoint().getIndex();
		
			if (indexA < 0 || indexB < 0) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Cannot construct perpendicular bisector " + this.getGeoObjectLabel() + " because some of segment's end points is not added to theorem protocol");
				return false; // some object is not in theorem protocol
			}
		
			if (indexA >= this.index || indexB >= this.index) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Cannot construct perpendicular bisector " + this.getGeoObjectLabel() + " because some of segment's end points is not yet constructed");
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
		// points A and B are used as equivalent in symbolic polynomial 
		// for condition for this line so we will not swap them
		pointsMap.put(ALabel, this.segment.getFirstEndPoint());
		pointsMap.put(BLabel, this.segment.getSecondEndPoint());
					
		manager.processPointsAndCondition(pointsMap);
					
		if (manager.isErrorFlag()) {
			logger.error("findBestPointsForInstantation() method failed in processing condition for perpendicular line");
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
		pointsMap.put(ALabel, this.segment.getFirstEndPoint());
		pointsMap.put(BLabel, this.segment.getSecondEndPoint());
		
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
		
		// we will not swap points A and B since condition for this line
		// is symmetric by their coordinates
		Map<String, Point> pointsMap = new HashMap<String, Point>();
		pointsMap.put(ALabel, this.segment.getFirstEndPoint());
		pointsMap.put(BLabel, this.segment.getSecondEndPoint());
		allMappings.add(pointsMap);
		
		retMap.put((SymbolicPolynomial) PerpendicularBisector.conditionForPerpendicularBisector.clone(), allMappings);
		
		return retMap;
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getConstructionDesc()
	 */
	@Override
	public String getConstructionDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Perpendicular bisector ");
		sb.append(this.geoObjectLabel);
		sb.append(" of segment ");
		sb.append(this.segment.getDescription());
		return sb.toString();
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getInputLabels()
	 */
	@Override
	public String[] getInputLabels() {
		String[] inputLabels = new String[2];
		inputLabels[0] = this.segment.getFirstEndPoint().getGeoObjectLabel();
		inputLabels[1] = this.segment.getSecondEndPoint().getGeoObjectLabel();
		return inputLabels;
	}
}
