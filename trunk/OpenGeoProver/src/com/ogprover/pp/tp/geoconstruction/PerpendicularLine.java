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
import com.ogprover.utilities.io.OGPOutput;
import com.ogprover.utilities.logger.ILogger;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for construction of perpendicular line
*     from given point to given line</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class PerpendicularLine extends Line {
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
	public static SymbolicPolynomial conditionForPerpendicularLine = null;
	// This line is perpendicular line from point C to line AB
	/**
	 * <i><b>Symbolic label for generic point from this line</b></i>
	 */
	private static final String M0Label = "0";
	/**
	 * <i><b>Symbolic label for first point that determines the base line</b></i>
	 */
	private static final String ALabel = "A";
	/**
	 * <i><b>Symbolic label for second point that determines the base line</b></i>
	 */
	private static final String BLabel = "B";
	/**
	 * <i><b>Symbolic label for base point that belongs to this perpendicular line</b></i>
	 */
	private static final String CLabel = "C";
	/**
	 * Given line to which this line is perpendicular
	 */
	private Line baseLine;
	
	
	// Static initializer of condition member 
	static {
		/* 
		 * M0 = (x0, y0) is the point on line for which the condition is calculated;
		 * A = (xA, yA) and B = (xB, yB) are two different points of given line;
		 * C = (xC, yC) is given point from perpendicular line. 
		 * 
		 * Condition is:
		 *    vector CM0 is orthogonal to vector AB i.e. their scalar product is zero, 
		 *    which yields the polynomial
		 *    (x0 - xC)(xB - xA) + (y0 - yC)(yB - yA) = 0
		 * 
		 */
		if (conditionForPerpendicularLine == null) {
			conditionForPerpendicularLine = new SymbolicPolynomial();
			
			// Instances of symbolic variables
			SymbolicVariable x0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, M0Label);
			SymbolicVariable y0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, M0Label);
			SymbolicVariable xA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, ALabel);
			SymbolicVariable yA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, ALabel);
			SymbolicVariable xB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, BLabel);
			SymbolicVariable yB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, BLabel);
			SymbolicVariable xC = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, CLabel);
			SymbolicVariable yC = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, CLabel);
			
			// term x0 * xB
			Term t = new SymbolicTerm(1);
			t.addPower(new Power(x0, 1));
			t.addPower(new Power(xB, 1));
			conditionForPerpendicularLine.addTerm(t);
			
			// term -x0 * xA
			t = new SymbolicTerm(-1);
			t.addPower(new Power(x0, 1));
			t.addPower(new Power(xA, 1));
			conditionForPerpendicularLine.addTerm(t);
			
			// term -xC * xB
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xC, 1));
			t.addPower(new Power(xB, 1));
			conditionForPerpendicularLine.addTerm(t);
			
			// term xC * xA
			t = new SymbolicTerm(1);
			t.addPower(new Power(xC, 1));
			t.addPower(new Power(xA, 1));
			conditionForPerpendicularLine.addTerm(t);
			
			// term y0 * yB
			t = new SymbolicTerm(1);
			t.addPower(new Power(y0, 1));
			t.addPower(new Power(yB, 1));
			conditionForPerpendicularLine.addTerm(t);
			
			// term -y0 * yA
			t = new SymbolicTerm(-1);
			t.addPower(new Power(y0, 1));
			t.addPower(new Power(yA, 1));
			conditionForPerpendicularLine.addTerm(t);
			
			// term -yC * yB
			t = new SymbolicTerm(-1);
			t.addPower(new Power(yC, 1));
			t.addPower(new Power(yB, 1));
			conditionForPerpendicularLine.addTerm(t);
			
			// term yC * yA
			t = new SymbolicTerm(1);
			t.addPower(new Power(yC, 1));
			t.addPower(new Power(yA, 1));
			conditionForPerpendicularLine.addTerm(t);
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
		return GeoConstruction.GEOCONS_TYPE_PERPENDICULAR;
	}
	
	/**
	 * Method to set the line to which this line is perpendicular
	 * 
	 * @param line The base line to set
	 */
	public void setBaseLine(Line line) {
		this.baseLine = line;
	}

	/**
	 * Method that retrieves the base line
	 * 
	 * @return The base line
	 */
	public Line getBaseLine() {
		return baseLine;
	}

	/**
	 * Method that retrieves the condition for a point that belongs to this line
	 * 
	 * @see com.ogprover.pp.tp.geoconstruction.Line#getCondition()
	 */
	public SymbolicPolynomial getCondition() {
		return conditionForPerpendicularLine;
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
	 * @param baseLine		Line which this line is perpendicular to
	 * @param pointC		Point that belongs to this line
	 */
	public PerpendicularLine(String lineLabel, Line baseLine, Point pointC) {
		this.geoObjectLabel = lineLabel;
		this.points = new Vector<Point>();
		if (pointC != null)
			this.points.add(pointC); // add at the end - first point
		if (baseLine != null)
			this.baseLine = baseLine;
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
			// Perpendicular line is valid if both its point and base line 
			// have been already constructed 
			Point pointC = this.points.get(0);
			int indexC, indexLine;
		
			// Point C and base line must exist
			if (pointC == null || this.baseLine == null) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Cannot construct perpendicular line " + this.getGeoObjectLabel() + " because its base line or point that it is passing through are not constructed");
				return false;
			}
		
			indexC = pointC.getIndex();
			indexLine = this.baseLine.getIndex();
		
			if (indexC < 0 || indexLine < 0) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Cannot construct perpendicular line " + this.getGeoObjectLabel() + " because its base line or point that it is passing through are not added to theorem protocol");
				return false; // some object is not in theorem protocol
			}
		
			if (indexC >= this.index || indexLine >= this.index) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Cannot construct perpendicular line " + this.getGeoObjectLabel() + " because its base line or point that it is passing through are not yet constructed");
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
		
		// Pass all points of perpendicular's base line to search for the first point from it
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
		
		// Points A and B are used as equivalent in symbolic polynomial for condition
		// of this line - therefore we will not swap them.
		// Here we can even allow points A and B to be equals to point P from manager since
		// that would happen if it is intersection of this perpendicular line and base line
		// so we can use it for instantiation of condition for this perpendicular line. 
		// However, when that same intersection point is used in base line for instantiation of
		// that condition, we have to provide points that are not P and are constructed before P.
		for (int iA = 0, jA = this.baseLine.points.size(); iA < jA; iA++) {
			Point pointA = this.baseLine.points.get(iA);
			// choose as given point one of those constructed before P
			if (pointA.getIndex() > P.getIndex()) // A constructed after P - skip it
				continue;
			
			// Pass all points of perpendicular's base line to search for the second point from it
			for (int iB = 0, jB = this.baseLine.points.size(); iB < jB; iB++) {
				Point pointB = this.baseLine.points.get(iB);
				// choose as given point one of those constructed before P and not before A
				if (pointB.getIndex() > P.getIndex() || 
					pointB.getIndex() <= pointA.getIndex()) // B constructed after P or constructed before A or is A - skip it
					continue;
				
				// Pass all points of this perpendicular line to search for one point from it
				for (int iC = 0, jC = this.points.size(); iC < jC; iC++) {
					Point pointC = this.points.get(iC);
					// choose as given point one of those constructed before P;
					// if it is the case, A and B can be same points as C
					if (pointC.getIndex() >= P.getIndex()) // C constructed after P or is P - skip it
						continue;
					
					pointsMap.put(ALabel, pointA);
					pointsMap.put(BLabel, pointB);
					pointsMap.put(CLabel, pointC);
					
					manager.processPointsAndCondition(pointsMap);
					
					if (manager.isErrorFlag()) {
						logger.error("findBestPointsForInstantation() method failed in processing condition for perpendicular line");
						return OGPConstants.ERR_CODE_GENERAL;
					}
					
					// if polynomial that renames coordinates of point is found, stop further search
					if (manager.getPoint().getPointState() == Point.POINT_STATE_RENAMED)
						return OGPConstants.RET_CODE_SUCCESS;
				}
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
		pointsMap.put(ALabel, this.baseLine.points.get(0));
		pointsMap.put(BLabel, this.baseLine.points.get(1));
		pointsMap.put(CLabel, this.points.get(0));
		
		//return this.instantiateCondition(pointsMap).reduceByUTermDivision();
		return this.instantiateCondition(pointsMap); // don't reduce polynomial
	}
	
	/**
	 * Method that checks the validity of construction of some point from 
	 * perpendicular line. It will be called from validation method for
	 * construction of some point from point set.
	 * 
	 * @param P	Point from perpendicular line whose construction is being verified
	 * @return	True if construction is valid, false otherwise
	 */
	public boolean isPerpendicularLinePointConstructionValid(Point P) {
		ILogger logger = OpenGeoProver.settings.getLogger();
		OGPOutput output = OpenGeoProver.settings.getOutput();
		
		try {
			// Construction of some point on perpendicular line is valid if that line has been 
			// already constructed and there are two points on base line of that perpendicular line;
			// if one point from perpendicular's base line is missing, then new random point is chosen
		
			// check perpendicular's base line
			Line perpBaseLine = this.getBaseLine();
			// since perpendicular line has been already constructed, the validity
			// of that construction has already been verified; therefore its base line
			// has been already constructed (it's not null and has at least one 
			// constructed point) and it is only required to check the second point
			Point secondPoint = null;
			if (perpBaseLine.getPoints().size() > 1)
				secondPoint = perpBaseLine.getPoints().get(1); // get second point from perpendicular's base line
			
			if (secondPoint == null || secondPoint.getIndex() < 0 || secondPoint.getIndex() >= P.getIndex()) {
				// there is no second point yet constructed - create random point with random label starting with GP - "General Point"
				RandomPointFromLine rndOnPerpBaseLine = new RandomPointFromLine("GP#" + Math.round(Math.random()*1000), perpBaseLine);
				// add this theorem to protocol right before this current construction
				output.openItemWithDesc("Info: ");
				output.closeItemWithDesc("Attempting to add the construction of new random point " + rndOnPerpBaseLine.getGeoObjectLabel() + " necessary for completion of construction of point " + P.getGeoObjectLabel());
				this.consProtocol.addGeoConstruction(P.getIndex(), rndOnPerpBaseLine); // here it will not be checked whether object with same name/label is already in CP since the probability for this event is zero
				// then validate this construction - it may generate new random point constructions
				if (rndOnPerpBaseLine.isValidConstructionStep() == false)
					return false;
				
				output.openItemWithDesc("Warrning: ");
				StringBuilder sb = new StringBuilder();
				sb.append("Generated new random point ");
				sb.append(rndOnPerpBaseLine.getGeoObjectLabel());
				sb.append(" on line ");
				sb.append(perpBaseLine.getGeoObjectLabel());
				sb.append(" in order to complete the construction of point ");
				sb.append(P.getGeoObjectLabel());
				sb.append(" on perpendicular line ");
				sb.append(this.getGeoObjectLabel());
				output.closeItemWithDesc(sb.toString());
			}
		} catch (NullPointerException e) {
			logger.error("Perpendicular line verified as correctly constructed, but some elements are null");
			try {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Unexpected error has occured during validity check for construction of point " + P.getGeoObjectLabel() + " on perpendicular line " + this.getGeoObjectLabel());
			} catch (IOException e1) {
				logger.error("Failed to write to output file(s).");
				output.close();
			}
			return false;
		} catch (IOException e1) {
			logger.error("Failed to write to output file(s).");
			output.close();
			return false;
		}
		
		return true;
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.Line#getAllPossibleConditionsWithMappings()
	 */
	@Override
	public Map<SymbolicPolynomial, ArrayList<Map<String, Point>>> getAllPossibleConditionsWithMappings() {
		Map<SymbolicPolynomial, ArrayList<Map<String, Point>>> retMap = super.getAllPossibleConditionsWithMappings();
		
		ArrayList<Map<String, Point>> allMappings = new ArrayList<Map<String, Point>>();
		Map<String, Point> pointsMap = null;
		
		// points A and B are equivalent in symbolic polynomial for perpendicular
		// line, therefore we will not swap them
		for (Point A : this.baseLine.getPoints()) {
			for (Point B : this.baseLine.getPoints()) {
				if (B.getIndex() <= A.getIndex())
					continue;
				
				for (Point C : this.points) {
					pointsMap = new HashMap<String, Point>();
					pointsMap.put(ALabel, A);
					pointsMap.put(BLabel, B);
					pointsMap.put(CLabel, C);
					allMappings.add(pointsMap);
				}
			}
		}

		retMap.put((SymbolicPolynomial) PerpendicularLine.conditionForPerpendicularLine.clone(), allMappings);
		
		return retMap;
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getConstructionDesc()
	 */
	@Override
	public String getConstructionDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("Line ");
		sb.append(this.geoObjectLabel);
		sb.append(" through point ");
		sb.append(this.points.get(0).getGeoObjectLabel());
		sb.append(" perpendicular to line ");
		sb.append(this.baseLine.getGeoObjectLabel());
		return sb.toString();
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getInputLabels()
	 */
	@Override
	public String[] getInputLabels() {
		String[] inputLabels = new String[2];
		inputLabels[0] = this.points.get(0).getGeoObjectLabel();
		inputLabels[1] = this.baseLine.getGeoObjectLabel();
		return inputLabels;
	}
}
