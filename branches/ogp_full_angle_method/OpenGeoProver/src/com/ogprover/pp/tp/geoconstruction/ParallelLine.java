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
* <dd>Class for construction of parallel line
*     from given point to given line</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class ParallelLine extends Line {
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
	public static SymbolicPolynomial conditionForParallelLine = null;
	// This line is parallel line from point C to line AB
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
	 * <i><b>Symbolic label for base point that belongs to this parallel line</b></i>
	 */
	private static final String CLabel = "C";
	/**
	 * Given line to which this line is parallel
	 */
	private Line baseLine;
	
	
	// Static initializer of condition member 
	static {
		/* 
		 * M0 = (x0, y0) is the point on line for which the condition is calculated;
		 * A = (xA, yA) and B = (xB, yB) are two different points of given line;
		 * C = (xC, yC) is given point from parallel line. 
		 * 
		 * Condition is:
		 *    vector CM0 is collinear to vector AB 
		 *    
		 *    (xB - xA, yB - yA) = k(x0 - xC, y0 - yC)
		 *    (xB - xA)/(x0 - xC) = (yB - yA)/(y0 - yC)
		 *    
		 *    which yields the polynomial
		 *    (xB - xA)(y0 - yC) - (yB - yA)(x0 - xC) = 0
		 *    
		 *    xBy0 - xByC - xAy0 + xAyC - yBx0 + yBxC + yAx0 - yAxC = 0
		 * 
		 */
		if (conditionForParallelLine == null) {
			conditionForParallelLine = new SymbolicPolynomial();
			
			// Instances of symbolic variables
			SymbolicVariable x0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, M0Label);
			SymbolicVariable y0 = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, M0Label);
			SymbolicVariable xA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, ALabel);
			SymbolicVariable yA = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, ALabel);
			SymbolicVariable xB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, BLabel);
			SymbolicVariable yB = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, BLabel);
			SymbolicVariable xC = new SymbolicVariable(Variable.VAR_TYPE_SYMB_X, CLabel);
			SymbolicVariable yC = new SymbolicVariable(Variable.VAR_TYPE_SYMB_Y, CLabel);
			
			// term xB * y0
			Term t = new SymbolicTerm(1);
			t.addPower(new Power(xB, 1));
			t.addPower(new Power(y0, 1));
			conditionForParallelLine.addTerm(t);
			
			// term -xB * yC
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xB, 1));
			t.addPower(new Power(yC, 1));
			conditionForParallelLine.addTerm(t);
			
			// term -xA * y0
			t = new SymbolicTerm(-1);
			t.addPower(new Power(xA, 1));
			t.addPower(new Power(y0, 1));
			conditionForParallelLine.addTerm(t);
			
			// term xA * yC
			t = new SymbolicTerm(1);
			t.addPower(new Power(xA, 1));
			t.addPower(new Power(yC, 1));
			conditionForParallelLine.addTerm(t);
			
			// term -yB * x0
			t = new SymbolicTerm(-1);
			t.addPower(new Power(yB, 1));
			t.addPower(new Power(x0, 1));
			conditionForParallelLine.addTerm(t);
			
			// term yB * xC
			t = new SymbolicTerm(1);
			t.addPower(new Power(yB, 1));
			t.addPower(new Power(xC, 1));
			conditionForParallelLine.addTerm(t);
			
			// term yA * x0
			t = new SymbolicTerm(1);
			t.addPower(new Power(yA, 1));
			t.addPower(new Power(x0, 1));
			conditionForParallelLine.addTerm(t);
			
			// term -yA * xC
			t = new SymbolicTerm(-1);
			t.addPower(new Power(yA, 1));
			t.addPower(new Power(xC, 1));
			conditionForParallelLine.addTerm(t);
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
		return GeoConstruction.GEOCONS_TYPE_PARALLEL;
	}
	
	/**
	 * Method to set the line to which this line is parallel
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
		return conditionForParallelLine;
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
	 * @param baseLine		Line which this line is parallel to
	 * @param pointC		Point that belongs to this line
	 */
	public ParallelLine(String lineLabel, Line baseLine, Point pointC) {
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
			// Parallel line is valid if both its point and base line 
			// have been already constructed and if base line has at least 2 points
			Point pointC = this.points.get(0);
			int indexC, indexLine;
		
			// Point C and base line must exist
			if (pointC == null || this.baseLine == null) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Cannot construct parallel line " + this.getGeoObjectLabel() + " because its base line or point that it is passing through are not constructed");
				return false;
			}
		
			indexC = pointC.getIndex();
			indexLine = this.baseLine.getIndex();
		
			if (indexC < 0 || indexLine < 0) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Cannot construct parallel line " + this.getGeoObjectLabel() + " because its base line or point that it is passing through are not added to theorem protocol");
				return false; // some object is not in theorem protocol
			}
		
			if (indexC >= this.index || indexLine >= this.index) {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Cannot construct parallel line " + this.getGeoObjectLabel() + " because its base line or point that it is passing through are not yet constructed");
				return false; // some object is not constructed before this line
			}
			
			// Validation of base line
			Point secondPoint = null;
			if (this.baseLine.getPoints().size() > 1)
				secondPoint = this.baseLine.getPoints().get(1); // get second point from parallel's base line (this is one of initial points of base line or the first point constructed on that line so it has the smallest index among all points other then initial)
			
			/*
			 * Find minimal index of points from this parallel line (disregard initial point of parallel line).
			 * This is used to check second point from base line - if there is at least one point from this 
			 * parallel line which is not initial point, for its instantiation there must be constructed second
			 * point from base line before that point from parallel line. If there is not such a point from parallel
			 * line, then second point from base line can be any point from that line.
			 */
			int minIdx = -1;
			for (int ii = 1, jj = this.points.size(); ii < jj; ii++) {
				Point pt = this.points.get(ii);
				int ptIdx = pt.getIndex();
				if (ptIdx < minIdx || ii == 1)
					minIdx = ptIdx;
			}
					
			if (secondPoint == null || secondPoint.getIndex() < 0 || (minIdx > -1 && secondPoint.getIndex() >= minIdx)) {
				// there is no second point yet constructed - create random point with random label
				RandomPointFromLine rndOnParaBaseLine = new RandomPointFromLine("tempPoint-" + Math.round(Math.random()*1000) + this.baseLine.getGeoObjectLabel(), this.baseLine);
				// add this construction to protocol when necessary for construction of some other point from parallel line or right after construction of baseline
				output.openItemWithDesc("Info: ");
				output.closeItemWithDesc("Attempting to add the construction of new random point " + rndOnParaBaseLine.getGeoObjectLabel() + " necessary for completion of construction of line " + this.getGeoObjectLabel());
				this.consProtocol.addGeoConstruction((minIdx > -1) ? minIdx : this.baseLine.getIndex() + 1, rndOnParaBaseLine); // here it will not be checked whether object with same name/label is already in CP since the probability for this event is zero
				// then validate this construction - it may generate new random point constructions
				if (rndOnParaBaseLine.isValidConstructionStep() == false)
					return false;
				
				output.openItemWithDesc("Warrning: ");
				StringBuilder sb = new StringBuilder();
				sb.append("Generated new random point ");
				sb.append(rndOnParaBaseLine.getGeoObjectLabel());
				sb.append(" on line ");
				sb.append(this.baseLine.getGeoObjectLabel());
				sb.append(" in order to complete the construction of parallel line ");
				sb.append(this.getGeoObjectLabel());
				output.closeItemWithDesc(sb.toString());
			}
		
			return true;
		} catch (NullPointerException e) {
			logger.error("Parallel line verified as correctly constructed, but some elements are null");
			try {
				output.openItemWithDesc("Error: ");
				output.closeItemWithDesc("Unexpected error has occured during validity check for construction of parallel line " + this.getGeoObjectLabel());
			} catch (IOException e1) {
				logger.error("Failed to write to output file(s).");
				output.close();
			}
			return false;
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
		
		// Pass all points of parallel's base line to search for the first point from it
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
		// Also, we cannot allow A and B to be P because in that case parallel line is
		// same as base line and that would mean we want to instantiate the condition for
		// P to belong to this parallel line by usage of that point from this line.
		for (int iA = 0, jA = this.baseLine.points.size(); iA < jA; iA++) {
			Point pointA = this.baseLine.points.get(iA);
			// choose as given point one of those constructed before P
			if (pointA.getIndex() > P.getIndex()) // A constructed after P or is P - skip it
				continue;
			
			// Pass all points of parallel's base line to search for the second point from it
			for (int iB = 0, jB = this.baseLine.points.size(); iB < jB; iB++) {
				Point pointB = this.baseLine.points.get(iB);
				// choose as given point one of those constructed before P and not before A
				if (pointB.getIndex() > P.getIndex() ||
					pointB.getIndex() <= pointA.getIndex()) // B constructed after P or is P or constructed before A or is A - skip it
					continue;
				
				// Pass all points of this parallel line to search for one point from it
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
						logger.error("findBestPointsForInstantation() method failed in processing condition for parallel line");
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
	 * @see com.ogprover.pp.tp.geoconstruction.Line#getAllPossibleConditionsWithMappings()
	 */
	@Override
	public Map<SymbolicPolynomial, ArrayList<Map<String, Point>>> getAllPossibleConditionsWithMappings() {
		Map<SymbolicPolynomial, ArrayList<Map<String, Point>>> retMap = super.getAllPossibleConditionsWithMappings();
		
		ArrayList<Map<String, Point>> allMappings = new ArrayList<Map<String, Point>>();
		Map<String, Point> pointsMap = null;
		
		// points A and B are equivalent in symbolic polynomial for parallel
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
		
		retMap.put((SymbolicPolynomial) ParallelLine.conditionForParallelLine.clone(), allMappings);
		
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
		sb.append(" parallel with line ");
		sb.append(this.baseLine.getGeoObjectLabel());
		return sb.toString();
	}

	/**
	 * @see com.ogprover.pp.tp.geoconstruction.GeoConstruction#getInputLabels()
	 */
	@Override
	public String[] getInputLabels() {
		String[] strArr = new String[0];
		ArrayList<String> inputLabels = new ArrayList<String>();
		Point firstPt = this.points.get(0);
		inputLabels.add(firstPt.getGeoObjectLabel());
		inputLabels.add(this.baseLine.getGeoObjectLabel());
		// Add points from base line too since they are important for this construction
		// (they are implicit input arguments) - there can be some new random points added.
		// But do not add points that depend on this perpendicular line.
		for (Point pt : this.baseLine.getPoints()) {
			String[] ptInputLabels = pt.getInputLabels();
			Vector<String> ptInputLabelsV = new Vector<String>();
			if (ptInputLabels != null) {
				for (String label : ptInputLabels) {
					if (ptInputLabelsV.indexOf(label) == -1)
						ptInputLabelsV.add(label);
				}
			}
			boolean bDependent = false;
			while (ptInputLabelsV != null && ptInputLabelsV.size() > 0) {
				if (ptInputLabelsV.indexOf(this.getGeoObjectLabel()) != -1) {
					bDependent = true;
					break;
				}
				
				Vector<String> tempV = new Vector<String>();
				for (String label : ptInputLabelsV) {
					String[] inLabels = this.consProtocol.getConstructionMap().get(label).getInputLabels();
					if (inLabels != null) {
						for (String label2 : inLabels) {
							if (tempV.indexOf(label2) == -1)
								tempV.add(label2);
						}
					}
				}
				
				ptInputLabelsV = tempV;
			}
			if (!bDependent && !pt.equals(firstPt))
				inputLabels.add(pt.getGeoObjectLabel());
		}
		return inputLabels.toArray(strArr);
	}
}
